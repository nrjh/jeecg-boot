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

        <a-form-item label="故障编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'faultTaskId', validatorRules.faultTaskId]" placeholder="请输入故障编号"></a-input>
        </a-form-item>
        <a-form-item label="检定线编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lineNo', validatorRules.lineNo]" placeholder="请输入检定线编号"></a-input>
        </a-form-item>
        <a-form-item label="物品" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'productId', validatorRules.productId]" placeholder="请输入物品" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'faultGrade', validatorRules.faultGrade]" placeholder="请输入等级"></a-input>
        </a-form-item>
        <a-form-item label="绑定状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'status', validatorRules.status]" placeholder="请输入绑定状态"></a-input>
        </a-form-item>
        <a-form-item label="设备故障标识码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productFaultCode', validatorRules.productFaultCode]" placeholder="请输入设备故障标识码"></a-input>
        </a-form-item>
        <a-form-item label="创建人ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createId', validatorRules.createId]" placeholder="请输入创建人ID"></a-input>
        </a-form-item>
        <a-form-item label="创建人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createName', validatorRules.createName]" placeholder="请输入创建人姓名"></a-input>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="修改人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入修改人"></a-input>
        </a-form-item>
        <a-form-item label="修改人名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateName', validatorRules.updateName]" placeholder="请输入修改人名称"></a-input>
        </a-form-item>
        <a-form-item label="修改时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择修改时间" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
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
    name: "FaultPrdProductInfoModal",
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
          faultTaskId: {rules: [
          ]},
          lineNo: {rules: [
          ]},
          productId: {rules: [
          ]},
          faultGrade: {rules: [
          ]},
          status: {rules: [
          ]},
          productFaultCode: {rules: [
          ]},
          createId: {rules: [
          ]},
          createName: {rules: [
          ]},
          createTime: {rules: [
          ]},
          updateBy: {rules: [
          ]},
          updateName: {rules: [
          ]},
          updateTime: {rules: [
          ]},
        },
        url: {
          add: "/alarm.bind/faultPrdProductInfo/add",
          edit: "/alarm.bind/faultPrdProductInfo/edit",
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
          this.form.setFieldsValue(pick(this.model,'faultTaskId','lineNo','productId','faultGrade','status','productFaultCode','createId','createName','createTime','updateBy','updateName','updateTime'))
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
        this.form.setFieldsValue(pick(row,'faultTaskId','lineNo','productId','faultGrade','status','productFaultCode','createId','createName','createTime','updateBy','updateName','updateTime'))
      },

      
    }
  }
</script>