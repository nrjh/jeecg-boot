<template>
<div>
<!-- 新增按钮弹出表单-->
  <a-modal
    :title="title1"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    :footer="null"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="报废盘点单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'scrapInventoryOrder', validatorRules.scrapInventoryOrder]" placeholder="请输入报废盘点单号" v-model='mscrapInventoryOrder'></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12"> 
              <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop v-decorator="['type', validatorRules.type]"
                                              :triggerChange="true"
                                              placeholder="请选择类别"
                                              dictCode="scrap_type" v-model='mstatus' />
              </a-form-item>
            </a-col> 

          <a-col :span="12">
            <a-form-item label="申请人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'applicationWorker', validatorRules.applicationWorker]" :placeholder="userName" disabled v-model='mapplicationWorker'></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date   v-decorator="[ 'applicationDate', validatorRules.applicationDate]" disabled  v-model='mapplicationDate' style="width: 100%"/>
            </a-form-item>
          </a-col>
        

     
          <a-col :span="24">
            <a-form-item label="申请说明" :labelCol="{span:3}" :wrapperCol="{span:20}">
              <a-textarea v-decorator="['applicationAbout', validatorRules.applicationAbout]" rows="4" placeholder="请输入申请说明" v-model='mapplicationAbout'/>
            </a-form-item>
          </a-col>
        </a-row>
      <!-- 这里加上新增报废弹出框的自定义表单，table 和下方的四个按钮-->      
      <j-editable-table
        ref="editableTable"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSourceCheck"
      >
       <span  slot="action" slot-scope="record">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                      <a>删除</a>
                </a-popconfirm>
        </span>
      </j-editable-table>
            
      <!-- 添加自定义按钮-->
      <div class="table-operator" style="text-align: center;">
      <a-button @click="handleAddwl" type="primary" icon="add">添加物料</a-button>
      <a-button @click="handleSave" type="primary" >保存</a-button>
      <a-button @click="handleSubmit" type="primary" >提交</a-button>
      <a-button @click="handleClose" type="primary" >关闭</a-button>
      </div>

      </a-form>
    </a-spin>
  </a-modal>
  <!--新增报废表单的 添加物料 按钮 弹出表单-->
    <a-modal
    :title="title2"
    :width="width"
    :visible="addwlvisible"
    :confirmLoading="confirmLoading"
    @ok="handleAddwlOk"
    @cancel="handleAddwlCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row :gutter="24"> 
          <a-col :span="8"> 
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'rpoductName', validatorRules.proName]" placeholder="请输入物料名称" v-model='mproName'></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8"> 
            <a-form-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'productNo', validatorRules.proNo]" placeholder="请输入物料编号" v-model='mproNo'></a-input>
            </a-form-item>
          </a-col> 
          <a-col :span="8"> 
            <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'attributeCategoryID', validatorRules.proCategoryID]" placeholder="请输入规格信息" v-model='mproCategoryID'></a-input>
            </a-form-item>
          </a-col> 
        </a-row> 

        <a-row :gutter="24">   
           <a-col :span="8"> 
              <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'vendorID', validatorRules.proVendorID]" placeholder="请输入供应商信息" v-model='mproVendorID'></a-input>
              </a-form-item>
            </a-col>
            <!-- 
            <a-col :span="6"> 
              <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop v-decorator="['status', validatorRules.status]"
                                              :triggerChange="true"
                                              placeholder="请选择状态"
                                              dictCode="stk_stock,status,id" v-model='mstatus' />
              </a-form-item>
            </a-col> -->
            <a-col :span="8"> 
              <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop v-decorator="['categoryID', validatorRules.categoryID]"
                                              :triggerChange="true"
                                              placeholder="请选择类别"
                                              dictCode="prd_category,name,id" v-model='mcategoryID'/>
              </a-form-item>
            </a-col>  
          
          <a-col :span="8">
            <a-form-item label="库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTagIop v-decorator="['locationID', validatorRules.locationID]"
                                          :triggerChange="true"
                                          placeholder="请选择库房"
                                          dictCode="stk_location,complete_name,id" v-model='mlocationID'/>

            </a-form-item>  
          </a-col>  
        </a-row>  
        <a-button @click="handleAddwlQueryBtn" type="primary" >查询</a-button>
        
      <!-- 这里加上添加物料弹出框的自定义表单--> 
      <!--添加表格多选以及删除功能-->        
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns2"
        :dataSource="dataSourceAddwl"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          
       >
      </a-table>
            
      <!-- 添加物料表单下方两个按钮，可以不加这两个按钮，弹出框有确认和关闭按钮-->
      <div class="table-operator">
      <a-button @click="handleAddwlOk" type="primary" >确定</a-button>
      <a-button @click="handleAddwlCancel" type="primary" >关闭</a-button>
      </div>

      </a-form>
    </a-spin>
  </a-modal>
  </div>
</template>

<script>

 import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { httpAction , getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate';
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop';  
  import JDictSelectTag from '@/components/dict/JDictSelectTag';
  import JMultiSelectTagIop from '@/components/dict/JMultiSelectTagIop';
  import store from '@/store';
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes , getRefPromise} from '@/utils/JEditableTableUtil'

  export default {
    name: "StkScrapModal",
    mixins:[JeecgListMixin],
    components: { 
       ATextarea, JDate,JDictSelectTagIop,JDictSelectTag,JMultiSelectTagIop,JEditableTable
    },
    data () {
      
      return {
        ipagination:{
            pageIndex: 1,
            pageSize: 10,
            total: 0
        },
        form: this.$form.createForm(this),
        title1:"新增报废",
        title2:"添加物料",
        width:1200,
        hight:800,
        visible: false,      //第一个弹出框
        addwlvisible:false,  //第一个弹出框里的添加物料弹出框
        mscrapInventoryOrder:'',
        mapplicationWorker:'',
        mapplicationDate:'',
        mapplicationAbout:'',//第一个表单数据，下面为第二个表单数据
        mproName:'', 
        mproNo:'',
        mproCategoryID:'',
        mproVendorID:'',
        mstatus:'',
        mcategoryID:'',
        mlocationID:'',
        //第一个弹出框新增报废的table列表
        columns: [
               // {
               //   title: '序号',
               //   dataIndex: '',
               //   key:'rowIndex',
               //   width:60,
               //   align:"center",
               //   customRender:function (t,r,index) {
               //     return parseInt(index)+1;
               //   }
               // },
                {
                  title:'物料名称',
                  align:"center",
                  key: 'rpoductName'
                },
                {
                  title:'物料编号',
                  align:"center",
                  key: 'productNo'
                },
                {
                  title:'规格',
                  align:"center",
                  key: 'attributeCategoryID'
                },
                {
                  title:'单位',
                  align:"center",
                  key: 'productUomID'
                },
                {
                  title:'供应商',
                  align:"center",
                  key: 'vendorID'
                },
                {
                  title:'合同号',
                  align:"center",
                  key: 'name',
                },
                {
                  title:'库存状况',
                  align:"center",
                  key: 'locationID'
                },
                {
                  title:'库存件数',
                  align:"center",
                  key: 'productQty'
                },
                {
                  title:'报废件数',//用户输入
                  align:"center",
                  key: 'scrapQty',
                   width: '8%',
                  type: FormTypes.inputNumber,
                  allowInput: true,
                  defaultValue: '0',
                  placeholder: '请输入${title}',
                  disabled: false
            
                },
                 {
                  title:'金额',
                  align:"center",
                  key: 'price'
                },
                {
                  title: '操作',
                  key: 'action',
                  // width: '8%',
                  width: '100px',
                  type: FormTypes.slot,
                  slotName: 'action',
                  defaultValue: '删除'
                }
              ],
        //第二个弹出框添加物料的table列表,需要增加列表勾选多项
        columns2: [
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
                  dataIndex: 'vendorID'
                },
                //对应第一个弹框table中的库存状况
                {
                  title:'货位',
                  align:"center",
                  dataIndex: 'locationID'
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
                  title:'合同号',
                  align:"center",
                  dataIndex: 'name',
                },
                 {
                  title:'类别',
                  align:"center",
                  dataIndex: 'categoryID'
                },
                {
                  title:'规格',
                  align:"center",
                  dataIndex: 'attributeCategoryID'
                },
                {
                  title:'单位',
                  align:"center",
                  dataIndex: 'productUomID'
                },
                 {
                  title:'状态',
                  align:"center",
                  dataIndex: 'status'
                },
                
                {
                  title:'库存件数',
                  align:"center",
                  dataIndex: 'productQty'
                }               
        ],
        dictOptions:{},
        dataSourceCheck:[],
        dataSourceAddwl:[],
        selectedData:[],
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
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
        url: {
          list:"/scrap/stkScrap/wlList",//查询物料列表(查询物品表和库存表)
          save:"/scrap/stkScrap/save",//保存物料列表
          submit:"/scrap/stkScrap/submit",//提交状态
          
          add: "/scrap/stkScrap/add",
          edit: "/scrap/stkScrap/edit",
        }
      }
    },
    created () {
    },
    methods: {
      initDictConfig(){
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'scrapInventoryOrder','type','applicationWorker','applicationDate','status','scrapDetail','checkInfo','applicationAbout'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleDelete(id){
        console.log("data----",id.index)
        this.dataSourceCheck.splice(id.index);
      },
      handleAddwl(){
        //弹出添加物料表单
        this.addwlvisible = true
      },
      //添加物料表单查询按钮
      handleAddwlQueryBtn(){
        
        console.log("开始查询后台物料数据...",this.mproName);
        let obj = {
          rpoductName : this.mproName,productNo : this.mproNo,attributeCategoryID : this.mproCategoryID,vendorID : this.mproVendorID,
          status : this.mstatus,categoryID : this.mcategoryID,locationID : this.mlocationID,
        }
        getAction(this.url.list, {rpoductName : this.mproName,productNo : this.mproNo,attributeCategoryID : this.mproCategoryID,vendorID : this.mproVendorID,
          status : this.mstatus,categoryID : this.mcategoryID,locationID : this.mlocationID,
          pageNo : this.ipagination.pageIndex,pageSize :this.ipagination.pageSize}).then((res) => {
            //console.log("查询结果",res.result);
            if (res.success) {
              this.dataSourceAddwl = res.result;
              this.ipagination.total = res.result.total;
            } else {
              this.dataSourceAddwl = null;
            }
          })

      },
      //添加物料确认
      handleAddwlOk(){
        //这里把当前表单数据传给上一个弹出form列表
        //this.dataSourceCheck = this.dataSourceAddwl; 
        //console.log("勾选数据",this.$emit('close', rowSelection));
        let selectedData=[];
          for (let item of this.selectedRowKeys) {
            this.dataSourceAddwl.forEach(function(record,index){
              if(index==item){
                selectedData.push(record);
              }
            })
        }
        //this.$emit("returnData",selectedData);
        this.dataSourceCheck = selectedData;
        this.$emit('close');
        this.addwlvisible = false;
      },
  
       handleAddwlCancel () {
        this.$emit('close');
        this.dataSourceAddwl = [];
        this.addwlvisible = false;
      },
      handleSave(){
        //如果用户没有输入报废件数，给出提示不能保存
        const that = this;
        let httpurl =this.url.save;
        let method = 'post';
        
        let obj = {
          scrapInventoryOrder : this.mscrapInventoryOrder,dataSourceCheck: this.dataSourceCheck,
        }
        obj.dataSourceCheck.forEach(item =>{//表单数据组装到DataSource数组里
          item.scrapInventoryOrder = obj.scrapInventoryOrder
        })
        //console.log("參數對象",obj.dataSourceCheck);
        let formData = Object.assign(obj.dataSourceCheck);
       
        //console.log("报废单号",formData);
        //表单信息转换为对象
        httpAction(httpurl,formData,method).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.$emit('ok');
          }else{
            that.$message.warning(res.message);
          }
        })//.finally(() => {     保存成功不关闭弹窗
         // that.confirmLoading = false;
         // that.close();
        //})
       
      },
      handleSubmit(){
       //修改单据状态为提交
        const that = this;
       // let httpurl =this.url.submit;
       // let method = 'post';   
       // const that = this;
       // 触发表单验证
       //console.log("this form 属性",this.form);
        this.form.validateFields((err, values) => {
          
          if (!err) {
            that.confirmLoading = true;
            let httpurl = this.url.submit;
            let method = 'post';  
           
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
        //let obj = {
        //  scrapInventoryOrder : this.mscrapInventoryOrder,applicationWorker : this.mapplicationWorker,
        //  applicationDate : this.mapplicationDate, applicationAbout : this.mapplicationAbout,
        //}   
       
        //console.log("---传入的对象:",obj);
        //httpAction(httpurl,scrapInventoryOrder,method).then((res)=>{
          
        //  if(res.success){
        //   that.$message.success(res.message);
        //    that.$emit('ok');
        // }else{
        //    that.$message.warning(res.message);
        //  }
        //}).finally(() => {
        //  that.confirmLoading = false;
        //  that.close();
        //})
      },
       handleClose(){
        this.$emit('close');
        this.dataSourceCheck = [];//清空data列表
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
         
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      handleCancel () {
        this.close()
      },
       onCellChange(key, dataIndex, value) {
      const dataSourceCheck = [...this.dataSourceCheck];
      const target = dataSourceCheck.find(item => item.key === key);
      if (target) {
        target[dataIndex] = value;
        this.dataSourceCheck = dataSourceCheck;
      }
    },
     
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'scrapInventoryOrder','type','applicationWorker','applicationDate','status','scrapDetail','checkInfo','applicationAbout'))
      }
      
    },
    computed: {
      userName: function() {
        let userName = store.getters.userInfo.realname
        console.log(userName, store)
        return userName
      }
    }
  }
</script>