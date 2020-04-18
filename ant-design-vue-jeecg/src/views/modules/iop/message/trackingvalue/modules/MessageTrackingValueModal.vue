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

        <a-form-item label="字段代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'field', validatorRules.field]" placeholder="请输入字段代码"></a-input>
        </a-form-item>
        <a-form-item label="字段描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fieldDesc', validatorRules.fieldDesc]" placeholder="请输入字段描述"></a-input>
        </a-form-item>
        <a-form-item label="字段类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fieldType', validatorRules.fieldType]" placeholder="请输入字段类型"></a-input>
        </a-form-item>
        <a-form-item label="旧整数值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'oldValueInteger', validatorRules.oldValueInteger]" placeholder="请输入旧整数值" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="旧浮点值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'oldValueFloat', validatorRules.oldValueFloat]" placeholder="请输入旧浮点值" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="旧货币值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'oldValueMonotary', validatorRules.oldValueMonotary]" placeholder="请输入旧货币值" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="旧字符值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'oldValueChar', validatorRules.oldValueChar]" placeholder="请输入旧字符值"></a-input>
        </a-form-item>
        <a-form-item label="旧文本值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'oldValueText', validatorRules.oldValueText]" placeholder="请输入旧文本值"></a-input>
        </a-form-item>
        <a-form-item label="旧日期时间值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择旧日期时间值" v-decorator="[ 'oldValueDatetime', validatorRules.oldValueDatetime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="新整数值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'newValueInteger', validatorRules.newValueInteger]" placeholder="请输入新整数值" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="新浮点值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'newValueFloat', validatorRules.newValueFloat]" placeholder="请输入新浮点值" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="新货币值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'newValueMonotary', validatorRules.newValueMonotary]" placeholder="请输入新货币值" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="新字符值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'newValueChar', validatorRules.newValueChar]" placeholder="请输入新字符值"></a-input>
        </a-form-item>
        <a-form-item label="新文本值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'newValueText', validatorRules.newValueText]" placeholder="请输入新文本值"></a-input>
        </a-form-item>
        <a-form-item label="新日期时间值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择新日期时间值" v-decorator="[ 'newValueDatetime', validatorRules.newValueDatetime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="消息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'messageId', validatorRules.messageId]" placeholder="请输入消息"></a-input>
        </a-form-item>
        <a-form-item label="跟踪序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'trackingSequence', validatorRules.trackingSequence]" placeholder="请输入跟踪序号" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: "MessageTrackingValueModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
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
          field: {rules: [
            {required: true, message: '请输入字段代码!'},
          ]},
          fieldDesc: {rules: [
            {required: true, message: '请输入字段描述!'},
          ]},
          fieldType: {rules: [
            {required: true, message: '请输入字段类型!'},
          ]},
          oldValueInteger: {rules: [
          ]},
          oldValueFloat: {rules: [
          ]},
          oldValueMonotary: {rules: [
          ]},
          oldValueChar: {rules: [
          ]},
          oldValueText: {rules: [
          ]},
          oldValueDatetime: {rules: [
          ]},
          newValueInteger: {rules: [
          ]},
          newValueFloat: {rules: [
          ]},
          newValueMonotary: {rules: [
          ]},
          newValueChar: {rules: [
          ]},
          newValueText: {rules: [
          ]},
          newValueDatetime: {rules: [
          ]},
          messageId: {rules: [
            {required: true, message: '请输入消息!'},
          ]},
          trackingSequence: {rules: [
            {required: true, message: '请输入跟踪序号!'},
          ]},
        },
        url: {
          add: "/iop/message/trackingvalue/add",
          edit: "/iop/message/trackingvalue/edit",
        }
      }
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
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'field','fieldDesc','fieldType','oldValueInteger','oldValueFloat','oldValueMonotary','oldValueChar','oldValueText','oldValueDatetime','newValueInteger','newValueFloat','newValueMonotary','newValueChar','newValueText','newValueDatetime','messageId','trackingSequence'))
        })
      },
      close () {
        this.$emit('close');
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
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'field','fieldDesc','fieldType','oldValueInteger','oldValueFloat','oldValueMonotary','oldValueChar','oldValueText','oldValueDatetime','newValueInteger','newValueFloat','newValueMonotary','newValueChar','newValueText','newValueDatetime','messageId','trackingSequence'))
      },

      
    }
  }
</script>