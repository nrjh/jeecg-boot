<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleOkSubmit"
      @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row >
            <a-col :span="8">
              <a-form-item label="领料出库单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'orderOutId', validatorRules.orderOutId]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="领料人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="申请日期" v-decorator="[ 'createTime', validatorRules.createTime]"
                        :trigger-change="true" style="width: 100%" disabled/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="8">
              <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <!--                <j-dict-select-tag type="list"-->
                <!--                                   placeholder=""-->
                <!--                                   v-model="type"-->
                <!--                                   dict-code="IOP_PUB_CATEGORY"/>-->
                <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineId', validatorRules.lineId]"  dictCode="o_area,area_name,area_id"
                                       placeholder="请选择检定线" @change="changeLine"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTag v-model="team"
                                placeholder=""
                                dictCode="sys_user,realname,realname" />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="设备" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <!--                <JDictSelectTag v-decorator="['execBy', validatorRules.execBy]"-->
                <!--                                :triggerChange="true"-->
                <!--                                placeholder=""-->
                <!--                                v-bind:disabled="diasabledInput"-->
                <!--                                dictCode="sys_user,username,id"/>-->
                <a-select  v-decorator="[ 'equipName', validatorRules.equipName]"  @change="changEquipNo" placeholder="请选择">
                  <a-select-option v-for="item in equipLists" :key="item.equipNo" :value="item.equipId">
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="12">
              <a-form-item label="物料类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-radio-group v-decorator="['prdCategory', validatorRules.prdCategory]">
                  <a-radio value="equip" defaultChecked>
                    备品/易耗品
                  </a-radio>
                  <a-radio value="spare">
                    办公用品
                  </a-radio>
                  <a-radio value="office_supplies">
                    工器具
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预计需求时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="预计需求时间" v-decorator="[ 'datePlanned', validatorRules.datePlanned]"
                        :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col>
              <a-form-item label="申请类型" :labelCol="{span:3}" :wrapperCol="{span:9}">
                <a-radio-group v-decorator="['orderType', validatorRules.orderType]">
                  <a-radio value="urgency">
                    紧急
                  </a-radio>
                  <a-radio value="normal">
                    正常
                  </a-radio>
                  <a-radio value="order">
                    检修工单
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col>
              <a-form-item label="领料说明" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-textarea :rows="3" placeholder="..." v-decorator="[ 'note', { rules: [] } ]"
                            v-bind:disabled="diasabledInput"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

        <!-- 操作按钮区域 -->
        <div class="table-operator">
          <a-button @click="handleAddProd" type="primary" icon="plus">添加物料</a-button>
        </div>

        <!-- 这里加上添加物料弹出框的自定义表单-->
        <a-table
          ref="editableTable"
          size="middle"
          bordered
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd"
        >
          <!--<template slot="productQty" slot-scope="text,record,index">-->
          <!--<a-input-number v-if="editable" v-model="dataSourceProd[index].productQty"/>-->
          <!--<span v-else>{{dataSourceProd[index].productQty}}</span>-->
          <!--</template>-->
          <template slot="product_ply_qty" slot-scope="text,record,index">
            <a-input-number  v-model="dataSourceProd[index].product_ply_qty"  :max="dataSourceProd[index].productQty" :min="0"/>
          </template>

          <template slot="action" slot-scope="text,record,index">
            <a-popconfirm  title="确定删除吗?" @confirm="() => handleDelete(record)">
              <a>删除</a>
            </a-popconfirm>
          </template>
        </a-table>
      </a-spin>
      <template slot="footer">
        <!--        <a-button key="submitApproval" type="primary" @click="handleOkSubmit('approve')">-->
        <!--          提交审批-->
        <!--        </a-button>-->
        <a-button key="submit" type="primary" @click="handleOkSubmit('approve')">
          确定
        </a-button>
        <a-button key="back" @click="handleCancel">
          取消
        </a-button>
      </template>
    </a-modal>
    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>

  </div>
</template>

<script>
    import JDate from '@/components/jeecg/JDate'
    import {httpAction, getAction} from '@/api/manage'
    import addProdModal from '../../../order/orderplan/modules/addProdModal.vue'
    import pick from 'lodash.pick'
    import {FormTypes, getRefPromise} from '@/utils/JEditableTableUtil'
    import {JEditableTableMixin} from '@/mixins/JEditableTableMixin'
    import {validateDuplicateValue} from '@/utils/util'
    import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
    import JDictSelectTag from "@/components/dict/JDictSelectTag"
    import moment from 'moment'
    import store from '@/store'
    import ATextarea from "ant-design-vue/es/input/TextArea";
    import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'

    export default {
        name: 'StockOutOrderModal',
        mixins: [JEditableTableMixin],
        components: {
            JDictSelectTag,
            addProdModal,
            JDate,
            JDictSelectTagOra,
            ATextarea
        },
        data() {
            return {
                // form: this.$form.createForm(this),
                team:'',
                type:'',
                title: "新增",
                visible: false,
                visibleProd: false,
                equipNo:'',
                diasabledInput: false,
                model: {},
                equipLists: [],
                dataSource: [],
                dataSourceProd: [],
                confirmLoading: false,
                dataForm: [],
                columns: [
                    {
                        title: '物料名称',
                        align: "center",
                        dataIndex: 'rpoductName'
                    },
                    {
                        title: '物料编号',
                        align: "center",
                        dataIndex: 'productNo'
                    },
                    {
                        title: '类别',
                        align: "center",
                        dataIndex: 'cateName'
                    },
                    {
                        title: '规格',
                        align: "center",
                        dataIndex: 'acName',
                    },
                    {
                        title: '库存件数',
                        align: "center",
                        dataIndex: 'productQty',
                    },
                    {
                        title: '单位',
                        align: "center",
                        dataIndex: 'uuName',
                    },
                    {
                        title: '出库数量',
                        align: "center",
                        dataIndex: 'product_ply_qty',
                        scopedSlots: { customRender: 'product_ply_qty' }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        scopedSlots: { customRender: 'action' }
                    },
                ],
                labelCol: {
                    span: 9
                },
                wrapperCol: {
                    span: 15
                },
                visibleProd: false,
                // 新增时子表默认添加几行空数据
                addDefaultRowNum: 1,
                validatorRules: {
                    lineId: {rules: [{required:true,message:"请选择检定线"}]},
                    equipName: {rules: [{required:true,message:"请选择设备"}]},
                    prdCategory: {
                        rules: []
                    },
                    datePlanned: {
                        rules: []
                    },
                    belongEquip: {
                        rules: []
                    },
                    team: {
                        rules: []
                    },
                    orderType: {
                        rules: []
                    },
                    note: {
                        rules: []
                    },
                    team: {
                        rules: []
                    },
                },
                refKeys: ['stockOutOrderLine',],
                tableKeys: ['stockOutOrderLine',],
                activeKey: 'stockOutOrderLine',
                // 出库明细表
                stockOutOrderLineTable: {
                    loading: false,
                    dataSource: [],

                },
                url: {
                    add: "/outOrder/stockOutOrder/add",
                    edit: "/outOrder/stockOutOrder/edit",
                    queryById: "/outOrder/stockOutOrder/queryById",
                    queryStockOutOrderLineByMainId: "/outOrder/stockOutOrder/queryStockOutOrderLineByMainId",
                    submitStockApproval: '/outOrder/stockOutOrder/submitApproval',
                    queryStockOutById: "/workOrder/ordWorkOrderInfo/queryStockOutById",
                    queryEquipNo:"/workOrder/ordWorkOrderInfo/queryEquipNo",
                    queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
                    stockOutOrderLine: {
                        list: '/outOrder/stockOutOrder/queryStockOutOrderLineByMainId'
                    },
                }
            }
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, {name: 'dynamic_form_item'});
            this.form.getFieldDecorator('keys', {initialValue: [], preserve: true});
        },
        methods: {
            changEquipNo(value){
                this.form.setFieldsValue({alarmReasonId:null})
                getAction(this.url.queryEquipNo,{id:value}).then(res=>{
                    console.log("res1111111111",res)
                    console.log("result2222222222",res.result)

                    this.equipNo=res.message;
                })
            },
            changeLine(value){
                this.equipLists=[];
                this.form.setFieldsValue({equipName:'请选择'})
                let equipVaule='';
                let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
                initDictOptionsOra(str).then(res => {
                    equipVaule=res.result[0].value;
                    if(equipVaule != null){
                        getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
                            this.equipLists=res.result;
                        })
                    }
                })
            },
            add(records) {
                console.log("88888888",records)
                this.form.resetFields();
                this.dataSourceProd = [];
                var outId = ''
                if(!(records.orderOutId)){
                    var now = moment().format("YYYYMMDDHHmmss")
                    outId = "GD" + moment().format("YYYYMMDDHHmmss")
                    let userName = store.getters.userInfo.realname
                    this.model.createTime = now
                    this.model.orderOutId = outId
                    this.model.createOutName = userName
                    this.model.prdCategory = 'equip'
                    this.model.orderType = 'order'
                    this.visible = true;
                    this.$nextTick(() => {
                        this.form.setFieldsValue(pick(this.model,'createTime','createOutName','orderOutId','prdCategory','orderType',))
                    })
                }else{
                    this.edit(records.orderOutId)
                }

            },
            getAllTable() {
                let values = this.tableKeys.map(key => getRefPromise(this, key))
                return Promise.all(values)
            },
            handleAddProd() {
                this.$refs.addProdFrom.add();
            },
            returnData(data) {
                // this.visibleProd = true;
                // console.log("物料返回数据222222",data)
                // this.dataSourceProd = data;
              this.visibleProd = true;
              for(var i = 0;i<data.length;i++){
                if(data[i].productQty ==0){
                  data.splice(i,1);
                }
              }
              console.log("查看返回数据111111",data)
              this.dataSourceProd = this.dataSourceProd.concat(data);
            },
            /** 调用完edit()方法之后会自动调用此方法 */
            editAfter() {

            },
            /** 整理成formData */
            classifyIntoFormData(allValues) {

            },
            validateError(msg) {
                this.$message.error(msg)
            },
            popupCallback(row) {
                this.form.setFieldsValue(pick(row, 'orderOutId', 'createOutName', 'createOutTime', 'prdCategory', 'belongEquip', 'team', 'orderType', 'datePlanned', 'note', 'state', 'updateInBy', 'updateInName', 'updateInTime', 'outState'))
            },

            handleOkSubmit(state) {
                let pordData =  this.dataSourceProd;
                console.log("物料数据",pordData);
                this.dataForm = [];
                // debugger;
                for (let i = 0; i < pordData.length; i++) {
                    let temp = {}
                    temp.productId = pordData[i].productId
                    // 物料名称
                    temp.productName = pordData[i].rpoductName
                    // 类别
                    temp.categoryType = pordData[i].categoryType
                    // 物料编号
                    temp.defaultCode = pordData[i].productNo
                    // 规格
                    temp.attributeCategoryId = pordData[i].attributeCategoryId
                    // 出库数量
                    temp.productPlyQty = pordData[i].product_ply_qty
                    // 单位
                    temp.productUomId = pordData[i].productUomId
                    // 库存件数
                    // temp.stockProductQty = pordData[i].stockProductQty
                    temp.status = 'freeze'
                    this.dataForm.push(temp);
                }
                // console.log("物料数据2222",this.dataForm);
                const that = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        that.confirmLoading = true;
                        let httpurl = '';
                        let method = '';
                        let obj = {
                            stockOutOrderLineList: this.dataForm,
                            state: state
                        };
                        let modelEdit = [];
                        let formData = [];
                        if (!this.model.id) {
                            httpurl += this.url.add;
                            method = 'post';
                            formData = Object.assign(obj, values);
                        } else {
                            httpurl += this.url.edit;
                            method = 'put';
                            if(this.model.state == "back"){
                                this.model.state = "approve"
                            }else{
                                this.model.state = state
                            }
                            modelEdit = Object.assign(this.model, values);
                            formData = Object.assign(obj, modelEdit);
                        }
                        console.log("表单提交数据", formData);
                        console.log("表单提交数据httpurl", httpurl);
                        httpAction(httpurl, formData, method).then((res) => {
                            if (res.success) {
                                that.$message.success(res.message);
                                console.log("获取id22222222", values.orderOutId);
                                that.$emit('ok',values.orderOutId);
                            } else {
                                that.$message.warning(res.message);
                            }
                        }).finally(() => {
                            that.confirmLoading = false;
                            that.close();
                        })
                    }
                })
            },
            handleDelete(record){

                console.log(record)
            },
            handleCancel() {
                this.close()
            },

            edit(v) {
                this.form.resetFields();
                console.log(v,222222222222222222222)
                getAction(this.url.queryStockOutById, {orderOutId: v}).then((res) => {
                    if (res.success) {
                        this.model = Object.assign({}, res.result);
                        console.log(this.model, '77777777777')
                        this.visible = true;
                        this.$nextTick(() => {
                            this.form.setFieldsValue(pick(this.model,'createTime','createOutName','orderOutId', 'prdCategory', 'datePlanned', 'orderType',
                                'note','lineId','equipName','team'))
                            getAction(this.url.queryStockOutOrderLineByMainId, {id: this.model.id}).then((res) => {
                                if (res.success) {
                                    this.dataSourceProd = []
                                    console.log(res.result, "11111111111111")
                                    for (let i = 0; i < res.result.length; i++) {
                                        let temp = {}
                                        temp.productId = res.result[i].productId
                                        // 物料名称
                                        temp.rpoductName = res.result[i].productName
                                        // 类别
                                        if (res.result[i].categoryType === 'equip') {
                                            temp.cateName = '生产设施'
                                        }else if(res.result[i].categoryType === 'spare'){
                                            temp.cateName = '备品备件'
                                        }else {
                                            temp.cateName = '办公用品'
                                        }
                                        // 物料编号
                                        temp.productNo = res.result[i].defaultCode
                                        // 规格
                                        temp.acName = res.result[i].attributeCategoryId_dictText
                                        // 计划出库数量
                                        temp.product_ply_qty = res.result[i].productPlyQty
                                        // 单位
                                        temp.uuName = res.result[i].productUomId_dictText
                                        temp.status = res.result[i].status
                                        // 库存件数
                                        temp.productQty = res.result[i].stockProductQty
                                        this.dataSourceProd.push(temp);
                                        console.log(this.dataSourceProd, "33333333333")
                                    }
                                }
                            });
                        })
                    }
                });

            },
            submitApproval() {
                if (this.model.id) {
                    var v = this.model.id
                    var j = parseInt(v)
                    console.log("id111111111111111111", j)
                    getAction(this.url.submitStockApproval, {id:j}).then((res) => {
                        if (res.success) {
                            this.$message.success(res.message);
                        } else {
                            this.$message.warning(res.message)
                        }
                    }).finally(() => {
                        this.visible = false;
                    })
                } else {
                    alert("请先提交数据")
                }
            },
        }
    }
</script>

<style scoped>
</style>