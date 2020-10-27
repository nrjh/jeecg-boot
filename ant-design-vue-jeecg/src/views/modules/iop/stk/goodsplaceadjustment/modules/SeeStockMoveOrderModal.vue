<template>
  <div>
    <a-modal
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @cancel="handleCancel"
      :footer="null"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-row>
            <a-form-item label="货位调整单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'orderMoveId', validatorRules.orderMoveId]" placeholder="请输入移库单号（源单据）" disabled="disabled"></a-input>
            </a-form-item>
          </a-row>

          <a-row :span="24">
            <a-col :span="12">
              <a-form-item label="调整人名称" :labelCol="{span:10}" :wrapperCol="{span:12}">
                <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="请输入调整人名称" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="调整日期" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <j-date placeholder="请选择调整日期" v-decorator="[ 'createOutTime', validatorRules.createOutTime]" :trigger-change="true" style="width: 100%" disabled="disabled"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['note', validatorRules.note]" rows="4" placeholder="请输入备注" disabled="disabled"/>
            </a-form-item>
          </a-row>
        </a-form>

        <!-- 这里加上添加物料弹出框的自定义表单-->
        <a-table
          ref="editableTable"
          size="middle"
          bordered
          rowKey="productId"
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd"
        >
          <template slot="productMoveQty" slot-scope="text,record,index">
            <a-input-number v-if="editable" v-model="dataSourceProd[index].productMoveQty" disabled="disabled"/>
            <span v-else>{{dataSourceProd[index].productMoveQty}}</span>
          </template>

        </a-table>
      </a-spin>
    </a-modal>
  </div>
</template>

<script>

    import {getAction} from '@/api/manage'
    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import JDate from '@/components/jeecg/JDate'
    import { initDictOptionsIop } from '@/components/dict/JDictSelectUtil'
    export default {
        name: "SeeStockMoveOrderModal",
        components: {
            JDate,
        },
        data () {
            const vm = this
            return {
                form: this.$form.createForm(this),
                title:"操作",
                width:1500,
                visible: false,
                model: {},
                visibleProd: false,
                dataSource:[],
                tempCategoryName:"",
                // 规格
                tempCategoryNameList:[],
                editable:true,
                dataSourceProd: [],
                columns: [
                    {
                        title: '序号',
                        dataIndex: '',
                        key:'rowIndex',
                        width:60,
                        align:"center",
                        customRender:function (t,r,index) {
                            return parseInt(index)+1;
                        }
                    },
                    {
                        title: '物料名称',
                        align: "center",
                        dataIndex: 'productName'
                    },
                    {
                        title: '物料编号',
                        align: "center",
                        dataIndex: 'productId'
                    },
                    {
                        title: '物料类别',
                        align: "center",
                        dataIndex:'categoryType',
                        customRender:function (text) {
                            if(text === 'equip'){
                                return "生产设施"
                            }else if(text === 'spare'){
                                return "备品备件"
                            }else {
                                return "办公用品"
                            }
                        }
                    },
                    {
                        title: '规格',
                        align: "center",
                        dataIndex:'attributeCategoryId',
                        customRender:function (text) {
                            return vm.getCategoryName(text)
                        }
                    },
                    {
                        title: '标识',
                        align: "center",
                        dataIndex:'stockInOrderId',
                    },
                    {
                        title: '单位',
                        align: "center",
                        dataIndex: 'productUomId',
                        customRender:function (text) {
                            return vm.getUomUomNameWithText(text)
                        }
                    },
                    {
                        title: '供应商',
                        align: "center",
                        dataIndex: 'partnerName',
                    },
                    {
                        title: '移出货位',
                        align: "center",
                        dataIndex: 'unknowColumn',
                    },
                    {
                        title: '库存件数',
                        align: "center",
                        dataIndex: 'productBeforeActualQty',
                    },
                    {
                        title: '计划移出件数',
                        align: "center",
                        dataIndex: 'productMoveQty',
                        scopedSlots: { customRender: 'productMoveQty' }
                    },
                    {
                        title: '剩余移出件数',
                        align: "center",
                        dataIndex: 'productActualQty',
                        customRender:function (t,r,index) {
                            return vm.surplusCompute(index)
                        }
                    },
                    {
                        title: '已分配货位详情',
                        align: "center",
                        dataIndex: 'whInputStockLotName',
                    },
                    {
                        title: '状态',
                        align: "center",
                        dataIndex: 'status',
                        customRender:function (value) {
                            if(value === 'normal'){
                                return '正常';
                            }else if(value ==='freeze') {
                                return '冻结'
                            }
                        }
                    }
                ],
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 },
                },
                confirmLoading: false,
                validatorRules: {
                    orderMoveId: {rules: [
                            {required: true, message: '请输入移库单号（源单据）!'},
                        ]},
                    createOutBy: {rules: [
                            {required: true, message: '请输入调整人ID!'},
                        ]},
                    createOutName: {rules: [
                            {required: true, message: '请输入调整人名称!'},
                        ]},
                    createOutTime: {rules: [
                            {required: true, message: '请输入调整日期!'},
                        ]},
                    note: {rules: [
                        ]},
                },
                url: {
                    queryByOrderMoveId: "/stk/goodsplaceadjustment/queryByOrderMoveId",
                    attributeCategoryName:"/iop/prd/attrcategory/queryById",
                    uomUomList:"/iop/uom/category/uomUomList",
                    attributeCategoryNameList:"/iop/prd/attrcategory/attributeCategoryNameList",

                }
            }
        },
        created () {
            this.getUomUomList();
            this.getAttributeCategoryNameList();

        },
        methods: {
            // 根据单位id获取单位名称
            getUomUomNameWithText(text){
                for(var i = 0;i<this.uomUomList.length;i++){
                    if(text === this.uomUomList[i].id){
                        return this.uomUomList[i].name
                    }
                }
            },
            // 获取单位字典
            getUomUomList(){
                getAction(this.url.uomUomList,{}).then((res) => {
                    if (res.success) {
                        this.uomUomList =  res.result
                    }else {
                        this.uomUomList =  []
                    }
                })
            },

            // 获取规格字典
            getAttributeCategoryNameList(){
                getAction(this.url.attributeCategoryNameList,{}).then((res) => {
                    if (res.success) {
                        this.tempCategoryNameList =  res.result
                    }else {
                        this.tempCategoryNameList =  []
                    }
                })
            },

            // 根据规格id获取规格名称
            getCategoryName(text){
                for(var i = 0 ;i<this.tempCategoryNameList.length ; i++){
                    if(text === this.tempCategoryNameList[i].id){
                        return this.tempCategoryNameList[i].name
                    }
                }
            },
            // 计算剩余移出的件数
            surplusCompute(index){
                return (this.dataSourceProd[index].productBeforeActualQty - this.dataSourceProd[index].productMoveQty)
            },
            see(moveId){
                const that = this;
                console.log(moveId)
                getAction(this.url.queryByOrderMoveId,{orderMoveId:moveId}).then((res) => {
                    if(res.success){
                        this.form.resetFields();
                        this.visible = true;
                        this.model.orderMoveId = res.result.orderMoveId
                        this.model.createOutBy = res.result.createOutBy
                        this.model.createOutName = res.result.createOutName
                        this.model.createOutTime = res.result.createOutTime
                        this.model.note = res.result.note
                        this.dataSourceProd = res.result.stockMoveOrderLineList
                        this.$nextTick(() => {
                            this.form.setFieldsValue(pick(this.model,'orderMoveId','createOutBy','createOutName','createOutTime','note'))
                        })
                    }else{
                        that.$message.warning(res.message);
                    }
                })
                // .finally(() => {
                //       that.confirmLoading = false;
                //       that.close();
                // })
            },
            close() {
                this.$emit('close');
                this.visible = false;
            },
            handleOk(){
                // const that = this;
                // // 触发表单验证
                // this.form.validateFields((err, values) => {
                //   if (!err) {
                //     that.confirmLoading = true;
                //     let httpurl = '';
                //     let method = '';
                //     if(!this.model.id){
                //       httpurl+=this.url.add;
                //       method = 'post';
                //     }else{
                //       httpurl+=this.url.edit;
                //        method = 'put';
                //     }
                //     let formData = Object.assign(this.model, values);
                //     console.log("表单提交数据",formData)
                //     httpAction(httpurl,formData,method).then((res)=>{
                //       if(res.success){
                //         that.$message.success(res.message);
                //         that.$emit('ok');
                //       }else{
                //         that.$message.warning(res.message);
                //       }
                //     }).finally(() => {
                //       that.confirmLoading = false;
                //       that.close();
                //     })
                //   }
                //
                // })
            },
            handleCancel () {
                this.close()
            },
            popupCallback(row){
                this.form.setFieldsValue(pick(row,'orderMoveId','createOutBy','createOutName','createOutTime','note','state','updateInBy','updateInName','updateInTime','inState','	inBy','inName','	inTime'))
            },


        }
    }
</script>