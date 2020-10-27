<template>
  <div>
    <a-modal
      :width="width"
      :visible="visible"
      :title="title"
      :confirmLoading="confirmLoading"
      :footer="null"
      @ok="handleAddOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">

          <a-row :gutter="24">

            <a-col :span="8">
              <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input  placeholder="请输入物料名称" v-model='mproName'></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input  placeholder="请输入物料编号" v-model='mproNo'></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input  placeholder="请输入规格信息" v-model='mproCategoryID'></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input  placeholder="请输入供应商信息（Id）" v-model='mproVendorId'></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <!--               <JDictSelectTagIop :triggerChange="true"-->
                <!--                                  placeholder="请选择状态"-->
                <!--                                  dictCode="stk_stock,status,id" v-model='mstatus' />-->
                <j-dict-select-tag placeholder="请输入物料状态" v-model="mstatus" dictCode="IOP_STK_SPARE_PARTS_STATUS_PRO"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag
                  placeholder="请选择类别"
                  dictCode="IOP_PRD_CATEGORY_TYPE" v-model='mcategoryID'/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input  placeholder="请输入库房" v-model='locationID'></a-input>
              </a-form-item>
            </a-col>
          </a-row>


          <a-button @click="handleAddQueryBtn" type="primary" >查询</a-button>

          <!-- 这里加上添加物料弹出框的自定义表单-->
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="rowKey"
            :columns="columns"
            :dataSource="dataSource"
            :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          >
          </a-table>

          <!-- 添加物料表单下方两个按钮，可以不加这两个按钮，弹出框有确认和关闭按钮-->
          <div class="table-operator">
           <span style="overflow: hidden; padding-left: 40%;" class="table-page-search-submitButtons">
             <a-button @click="handleAddOk" type="primary" >确定</a-button>
             <a-button @click="handleCancel" type="primary" >关闭</a-button>
           </span>
          </div>

        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script>
    import { httpAction , getAction } from '@/api/manage'
    import { getUserDep } from '@/api/api'
    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import JDate from '@/components/jeecg/JDate'
    import ATextarea from "ant-design-vue/es/input/TextArea";
    import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'
    import JMultiSelectTagIop from '@/components/dict/JMultiSelectTagIop'


    export default {
        name: "goodsPlaceAddProdModal.vue",
        components: {
            ATextarea, JDate,JDictSelectTagIop,JDictSelectTag,JMultiSelectTagIop
        },
        data () {
            return {
                form: this.$form.createForm(this),
                title:"添加物料",
                width:800,
                visible: false,
                dataSource: [],
                model: {},
                labelCol: {
                    span: 6
                },
                wrapperCol: {
                    span: 16
                },
                formItemLayoutWithOutLabel: {
                    wrapperCol: {
                        xs: { span: 24, offset: 0 },
                        sm: { span: 20, offset: 4 },
                    },
                },
                validatorRules: {
                    scrapInventoryOrder: {rules: [
                        ]},
                    applicationWorker: {rules: [
                        ]},
                    applicationDate: {rules: [
                        ]},
                    applicationAbout: {rules: [
                        ]},
                },
                ipagination:{
                    pageIndex: 1,
                    pageSize: 10,
                    total: 0
                },
                selectedRowKeys: [],
                selectedRows: [],
                mscrapInventoryOrder:'',
                mapplicationWorker:'',
                mapplicationDate:'',
                mapplicationAbout:'',//第一个表单数据，下面为第二个表单数据
                mproName:'',
                mproNo:'',
                mproCategoryID:'',
                mproVendorId:'',
                mstatus:'',
                locationID:'',
                mcategoryID:'',
                selectedData:[],
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
                     title:'供应商',
                     align:"center",
                     dataIndex: 'vendorId'
                   },
                  //对应第一个弹框table中的库存状况
                   {
                     title:'货位',
                     align:"center",
                     dataIndex: 'proLocation'
                   },
                    {
                        title:'物料名称',
                        align:"center",
                        dataIndex: 'rpoductName'
                    },
                    {
                        title:'物料编号',
                        align:"center",
                        dataIndex: 'productNo'
                    },
                    {
                        title:'合同编号',
                        align:"center",
                        dataIndex: 'name',
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
                        title:'类别',
                        align:"center",
                        dataIndex: 'categoryType',
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
                        title:'规格',
                        align:"center",
                        dataIndex: 'acName'
                    },
                    {
                        title:'单位',
                        align:"center",
                        dataIndex: 'uuName'
                    },
                    {
                        title:'状态',
                        align:"center",
                        dataIndex: 'status',
                        customRender:function (value) {
                            if(value=='normal'){
                                return '正常';
                            }
                        }
                    },

                    {
                        title:'库存件数',
                        align:"center",
                        dataIndex: 'virtualQty'
                    }
                ],
                confirmLoading: false,
                url: {
                    list:"/iop/order/orderPlan/wlList",
                }
            }
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'dynamic_form_item' });
            this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
        },
        created () {
        },
        methods: {
            add (value) {
                console.log("传值222222222",value)
                this.visible = true;
                this.mcategoryID = value;
                this.handleAddQueryBtn();
            },
            close () {
                // this.$emit('close');
                this.visible = false;
                this.companyName='';
                this.selectedRowKeys= [],
                    this.selectedRows=[]
            },
            handleCancel () {
                this.close()
            },
            popupCallback(row){
                this.form.setFieldsValue(pick(row,'name','dateDone','state','companyId','taskNo','planStartTime','planEndTime','sendTime','endTime','remark'))
            },
            handleAddQueryBtn(){
                getAction(this.url.list,
                    {
                        rpoductName : this.mproName,
                        productNo : this.mproNo,
                        attributeCategoryId : this.mproCategoryID,
                        vendorId : this.mproVendorId,
                        status : this.mstatus,
                        categoryType : this.mcategoryID,
                        pageNo : this.ipagination.pageIndex,
                        pageSize :this.ipagination.pageSize,
                        locationID:this.locationID
                    }
                    ).then((res) => {
                    if (res.success) {
                        this.dataSource = res.result;
                        this.ipagination.total = res.result.total;
                    } else {
                        this.dataSource = null;
                    }
                })

            },
            onSelectChange(selectedRowKeys, selectionRows) {
                this.selectedRowKeys = selectedRowKeys;
                this.selectionRows = selectionRows;
            },
            handleAddOk(){
                // 封装获取选中行的值
                let selectedData=[];
                for (let item of this.selectedRowKeys) {
                    this.dataSource.forEach(function(record,index){
                        if(index==item){
                            selectedData.push(record);
                        }
                    })
                }
                this.close();
                this.$emit("returnData",selectedData);
            },
            handleAddwlCancel(){
                this.$emit('close');
                this.dataSource = [];
                this.visible = false;
            },

        }
    }


</script>