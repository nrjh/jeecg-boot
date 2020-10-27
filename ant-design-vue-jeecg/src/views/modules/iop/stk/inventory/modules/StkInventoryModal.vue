<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-row :gutter="24">

          <a-col :span="12">
            <a-form-item label="任务编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'taskNo', validatorRules.taskNo]" placeholder="请输入编号" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
<!--            <a-form-item label="盘点单位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="companyName" placeholder="请输入单位" style="width: 100%"  disabled="disabled"/>-->
<!--              <a-input v-decorator="[ 'companyId', validatorRules.companyId]" hidden="hidden" style="width: 100%"  disabled="disabled"/>-->
<!--            </a-form-item>-->
            <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-ora type="list" v-decorator="['areaId', validatorRules.areaId]"
                                     @inputText="inputTextAreaId"
                                     :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"/>
              <a-input v-show="false" v-decorator="['areaName']"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="盘点人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag v-decorator="['execBy', validatorRules.execBy]"
                                 :triggerChange="true"
                                 placeholder=""
                                  @change="changMsg"
                                 dictCode="sys_user,username,id" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="盘点时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<a-date-picker show-time format="YYYY-MM-DD HH:mm"  placeholder="请选择盘点开始时间" v-decorator="[ 'planStartTime', validatorRules.planStartTime]" :trigger-change="true" style="width: 100%" />-->
              <j-date  placeholder="请选择盘点开始时间" v-decorator="[ 'planStartTime', validatorRules.planStartTime]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="至" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date   placeholder="请选择盘点结束时间" v-decorator="[ 'planEndTime', validatorRules.planEndTime]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>


        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="盘点范围" :labelCol="{span:3}" :wrapperCol="{span:21}">
                    <a-form-item label="库房/货位" :labelCol="{span:3}" :wrapperCol="{span:20}">
                      <JMultiSelectTagIop v-decorator="['stockLocationId', validatorRules.stockLocationId]"
                                         :triggerChange="true"
                                         placeholder="请选择库存位置"
                                         dictCode="stk_location,complete_name,id" />
                    </a-form-item>
            </a-form-item>
          </a-col>
        </a-row>



        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item
              :labelCol="{span:3}"
              :wrapperCol="{span:20}"
              label="备注"
              hasFeedback>
              <a-textarea :rows="3" placeholder="..." v-decorator="[ 'remark', { rules: [] } ]" />
            </a-form-item>
          </a-col>
        </a-row>


      </a-form>
    </a-spin>
  </a-modal>
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
  import moment from 'moment'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  let id = 0;
  export default {
    name: "StkInventoryModal",
    components: {
      ATextarea, JDate,JDictSelectTagIop,JDictSelectTag,JMultiSelectTagIop,JDictSelectTagOra
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
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
        companyName:"",
        companyId:"",
        confirmLoading: false,
        validatorRules: {
          name: {rules: [
            {required: true, message: '请输入名称!'},
          ]},
          dateDone: {rules: [
          ]},
          companyId: {rules: [
          ]},
          taskNo: {rules: [

          ]},
          planStartTime: {rules: [
            {required: true, message: '请输入计划开始时间!'},
          ]},
          planEndTime: {rules: [
            {required: true, message: '请输入计划结束时间	!'},
          ]},
          sendTime: {rules: [
          ]},
          remark: {rules: [
          ]},
          stockLocationId: {rules: [
          ]},
          execBy: {rules: [
          ]},
        },
        url: {
          add: "/StkInventory/stkInventory/add",
          edit: "/StkInventory/stkInventory/edit",
          userWithDepart: "/sys/user/userDepartList", // 引入为指定用户查看部门信息需要的url
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
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        var now = "PD" + moment().format("YYYYMMDDHHmmss")
        this.model.taskNo = now
        // var day = moment("1995-12-25");
        // console.log("时间数据22222222222",day)
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','dateDone','state','companyId','taskNo','planStartTime','planEndTime','sendTime','endTime','remark'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.companyName='';
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
                this.companyName='';
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
              this.loadData()
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
            })
            this.companyName=res.result[0].title;
          }else{
            console.log(res.message);
          }
        })
      }


    }
  }
</script>