<template>
  <div>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOkSubmit"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="16">

          <a-col :span="8">
            <a-form-item label="采购单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input autocomplete ='off'  v-decorator="[ 'pickingNo', validatorRules.pickingNo]"  placeholder="请输入编号" disabled></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请人" :labelCol="{span:9}" :wrapperCol="{span:15}">
               <a-input autocomplete ='off' v-decorator="[ 'createBy', validatorRules.createBy]" @change="getGroupNo" :placeholder="userName" disabled ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请时间" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-time  placeholder="" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" disabled style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-dict-select-tag-ora type="list" v-decorator="['lineId', validatorRules.lineId]"
                                     @inputText="inputTextAreaId"
                                     :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                     v-bind:disabled="diasabledInput"/>
              <a-input v-show="false" v-decorator="['lineName']"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="班组" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-dict-select-tag-ora type="list" v-decorator="['groupNo', validatorRules.groupNo]"
                                     :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"
                                     disabled/>
<!--              <a-input v-show="false" v-decorator="['groupNo']"></a-input>-->
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="需求时间" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-date  placeholder="" v-decorator="[ 'scheduledDate', validatorRules.scheduledDate]" :trigger-change="true" v-bind:disabled="diasabledInput"  style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="物料类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-radio-group v-decorator="['categoryType', validatorRules.categoryType]" v-bind:disabled="diasabledInput">
                <a-radio value="equip">
                  生产设施
                </a-radio>
                <a-radio value="spare">
                  备品备件
                </a-radio>
                <a-radio value="office_supplies">
                  办公用品
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="单据类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-radio-group v-decorator="['orderType',validatorRules.orderType]" v-bind:disabled="diasabledInput">
                <a-radio value="normal">
                  正常单
                </a-radio>
                <a-radio value="urgency">
                  紧急单
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item :labelCol="{span:3}" :wrapperCol="{span:15}" label="领料说明"
              hasFeedback>
              <a-textarea :rows="3" placeholder="..." v-decorator="[ 'note', { rules: [] } ]" v-bind:disabled="diasabledInput"  />
            </a-form-item>
          </a-col>
        </a-row>

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
          <template slot="productUomQty" slot-scope="text,record,index">
            <a-input-number v-model="dataSourceProd[index].productUomQty"/>
          </template>
          <template slot="vendorCode" slot-scope="text,record,index">
            <j-dict-select-tag-iop type="list"  v-model="dataSourceProd[index].partnerId"
                               :trigger="true" dictCode="prd_brand,manufactor_name,id,is_del='0'" placeholder="请选择供应商"
                              />
          </template>
          <template slot="action" slot-scope="text,record,index">
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(index)">
              <a>删除</a>
            </a-popconfirm>
          </template>
        </a-table>
      </a-form>
    </a-spin>
  </a-modal>

  <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>
  </div>
</template>

<script>
  import { httpAction , getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JMultiSelectTagIop from '@/components/dict/JMultiSelectTagIop'
  import addProdModal from './addProdModal.vue'
  import { FormTypes , getRefPromise} from '@/utils/JEditableTableUtil'
  import store from '@/store'
  import moment from 'moment'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import JTime from '@/components/jeecg/JTime';
  import JEditableTable from '@/components/jeecg/JEditableTable';

  export default {
    name: "addOrderModal",
    components: {
      JTime,
      AFormItem,
      ACol,
      ARow,
      ATextarea, JDate,JDictSelectTagIop,JDictSelectTag,JMultiSelectTagIop,addProdModal,JEditableTable,JDictSelectTagOra
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"新增",
        width:800,
        visible: false,
        visibleProd: false,
        diasabledInput:false,
        model: {},
        dataSource: [],
        dataSourceProd: [],
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
        companyName:"",
        companyId:"",
        confirmLoading: false,
        validatorRules: {
          name: {rules: [
          ]},
          dateDone: {rules: [
          ]},
          companyId: {rules: [
          ]},
          taspickingNOkNo: {rules: [
            {required: true, message: '请输入编号!'},
          ]},
          scheduledDate: {rules: [
            {required: true, message: '请输入计划时间!'},
          ]},
          createtime: {rules: [
            {required: true, message: '请输入申请时间!'},
          ]},
          groupNo: {rules: [
          ]},
          remark: {rules: [
          ]},
          stockLocationId: {rules: [
          ]},
          orderType: {rules: [
            {required: true, message: ''},
          ]},
        },
        columns: [
          {
            title:'物料名称',
            align:"center",
            key: 'rpoductName',
            dataIndex:'rpoductName'
          },
          {
            title:'物料编号',
            align:"center",
            key: 'productNo',
            dataIndex:'productNo'
          },
          {
            title:'类别',
            align:"center",
            key: 'categoryType',
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
            title:'规格',
            align:"center",
            key: 'acName',
            dataIndex:'acName'
          },
          {
            title:'单位',
            align:"center",
            key: 'uuName',
            dataIndex:'uuName'
          },
          {
            title:'申请件数',
            align:"center",
            key:'productUomQty',
            scopedSlots: { customRender: 'productUomQty' }
          },
          {
            title:'供应商',
            align:"center",
            key: 'vendorId',
            width: '12%',
            scopedSlots: { customRender: 'vendorCode' }
          },
          {
            title: '操作',
            key: 'action',
            // width: '8%',
            width: '100px',
            scopedSlots: { customRender: 'action' }
          },
        ],
        url: {
          add: "/iop/order/orderPlan/add",
          edit: "/iop/order/orderPlan/edit",
          queryById: "/iop/order/orderPlan/queryById",
          queryStkMoveByMainId: "/iop/order/orderPlan/queryStkMoveByMainId",
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
      inputTextShiftId(val) {
        this.form.setFieldsValue({ groupNo: val })
      },
      inputTextAreaId(val){
        this.form.setFieldsValue({ lineName: val })
      },
      add () {
        this.visible = true;
        this.form.resetFields();
        var now = moment().format("YYYYMMDDHHmmss")
        var pkId = "CGJH" + now
        let userName = store.getters.userInfo.realname
        let shiftId = store.getters.userInfo.shiftId;
        this.model.createTime = moment().format('YYYY-MM-DD HH:mm:ss')
        this.model.pickingNo = pkId
        this.model.createBy = userName
        this.model.groupNo = shiftId
        this.model.orderType = "normal"
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'createTime','createBy','pickingNo','groupNo','orderType'))
        })
      },
      edit (v,opt) {
        this.form.resetFields();
         getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'pickingNo','createTime','createBy',
                'lineId','groupNo','scheduledDate','categoryType','orderType','note'))
            })
          }
        });
        getAction(this.url.queryStkMoveByMainId,{id:v}).then((res)=>{
          if(res.success){
            console.log('res.result----',res.result)
            this.dataSourceProd=res.result;
          }
        });

        if(opt==1){
          this.diasabledInput=true;
          // this.getAllTable().then(editableTables => {
          //   editableTables[0].columns[6].disabled=true;
          // })
        }

      },
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'editableTable'),
        ])
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.diasabledInput=false;
        this.getAllTable().then(editableTables => {
          editableTables[0].columns[6].disabled=false;
        })
        this.companyName='';
        this.dataSourceProd=[];
        this.model=[];
        this.$emit('onClear');
      },
      handleOkSubmit () {
        let pordData=this.dataSourceProd;
        console.log("----",pordData)
        const that = this;
        // 触发表单验证

        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            let obj={
              prdProduct :pordData
            };
            let modelEdit=[];
            let formData=[];
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
              formData = Object.assign(obj, values);
            }else{
              httpurl+=this.url.edit;
              method = 'put';
              modelEdit = Object.assign(this.model, values);
              formData = Object.assign(obj, modelEdit);
            }
            console.log("表单提交数据",formData);
            console.log("表单提交数据httpurl",httpurl);
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
                that.$emit('searchQuery');
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
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'name','dateDone','state','companyId','taskNo','planStartTime','planEndTime','sendTime','endTime','remark'))
      },
      changMsg(value){
        getAction(this.url.userWithDepart,{userId:value}).then((res)=>{
          if(res.success){
            this.form.setFieldsValue({
              companyId:res.result[0].key,
            });
            this.companyName=res.result[0].title;
          }else{
            console.log(res.message);
          }
        })
      },
      handleAddProd(){
        this.form.validateFields((err, values) => {
          this.$refs.addProdFrom.add(values.categoryType);
        })

      },
      returnData(data){
        this.visibleProd=true;
        this.dataSourceProd = this.dataSourceProd.concat(data);
        console.log("返回数据111111",this.dataSourceProd)
      },
      handleDelete(id){
        console.log("data----",id)
        this.dataSourceProd.splice(id,1);
      },
      handleOk(){
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            values.active = values.active == true ? 1 : 0
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values)
            let new_pid = this.model[this.pidField]
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.submitSuccess(formData, old_pid == new_pid)
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }

        })
      },
      getGroupNo(){

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