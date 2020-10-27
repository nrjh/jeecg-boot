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

        <a-form-item label="检定任务编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'detectTaskNo', validatorRules.detectTaskNo]" placeholder="请输入检定任务编号"></a-input>
        </a-form-item>
        <a-form-item label="线体编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lineNo', validatorRules.lineNo]" placeholder="请输入线体编号"></a-input>
        </a-form-item>
        <a-form-item label="原系统标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'oldId', validatorRules.oldId]" placeholder="请输入原系统标识"></a-input>
        </a-form-item>
        <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'equipNo', validatorRules.equipNo]" placeholder="请输入设备编号"></a-input>
        </a-form-item>
        <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'equipName', validatorRules.equipName]" placeholder="请输入设备名称"></a-input>
        </a-form-item>
        <a-form-item label="报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择报警时间" v-decorator="[ 'alarmTime', validatorRules.alarmTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="报警级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'alarmLevel', validatorRules.alarmLevel]" placeholder="请输入报警级别"></a-input>
        </a-form-item>
        <a-form-item label="报警来源" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'alarmSource', validatorRules.alarmSource]" placeholder="请输入报警来源"></a-input>
        </a-form-item>
        <a-form-item label="报警原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'alarmReason', validatorRules.alarmReason]" placeholder="请输入报警原因"></a-input>
        </a-form-item>
        <a-form-item label="报警描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'alarmDesc', validatorRules.alarmDesc]" placeholder="请输入报警描述"></a-input>
        </a-form-item>
        <a-form-item label="处理建议" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'alarmSugg', validatorRules.alarmSugg]" placeholder="请输入处理建议"></a-input>
        </a-form-item>
        <a-form-item label="报警状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'alarmStatus', validatorRules.alarmStatus]" placeholder="请输入报警状态"></a-input>
        </a-form-item>
        <a-form-item label="解除时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择解除时间" v-decorator="[ 'endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'execMode', validatorRules.execMode]" placeholder="请输入处理方式"></a-input>
        </a-form-item>
        <a-form-item label="处理过程描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'execDesc', validatorRules.execDesc]" placeholder="请输入处理过程描述"></a-input>
        </a-form-item>
        <a-form-item label="备注信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注信息"></a-input>
        </a-form-item>
        <a-form-item label="操作标志" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'opFlag', validatorRules.opFlag]" placeholder="请输入操作标志"></a-input>
        </a-form-item>
        <a-form-item label="数据写入时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择数据写入时间" v-decorator="[ 'writeTime', validatorRules.writeTime]" :trigger-change="true" style="width: 100%"/>
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
    name: "MtEquipAlarmInfoModal",
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
          detectTaskNo: {rules: [
          ]},
          lineNo: {rules: [
            {required: true, message: '请输入线体编号!'},
          ]},
          oldId: {rules: [
          ]},
          equipNo: {rules: [
          ]},
          equipName: {rules: [
          ]},
          alarmTime: {rules: [
          ]},
          alarmLevel: {rules: [
          ]},
          alarmSource: {rules: [
          ]},
          alarmReason: {rules: [
          ]},
          alarmDesc: {rules: [
          ]},
          alarmSugg: {rules: [
          ]},
          alarmStatus: {rules: [
          ]},
          endTime: {rules: [
          ]},
          execMode: {rules: [
          ]},
          execDesc: {rules: [
          ]},
          remark: {rules: [
          ]},
          opFlag: {rules: [
          ]},
          writeTime: {rules: [
          ]},
        },
        url: {
          add: "/alarm.record/mtEquipAlarmInfo/add",
          edit: "/alarm.record/mtEquipAlarmInfo/edit",
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
          this.form.setFieldsValue(pick(this.model,'detectTaskNo','lineNo','oldId','equipNo','equipName','alarmTime','alarmLevel','alarmSource','alarmReason','alarmDesc','alarmSugg','alarmStatus','endTime','execMode','execDesc','remark','opFlag','writeTime'))
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
        this.form.setFieldsValue(pick(row,'detectTaskNo','lineNo','oldId','equipNo','equipName','alarmTime','alarmLevel','alarmSource','alarmReason','alarmDesc','alarmSugg','alarmStatus','endTime','execMode','execDesc','remark','opFlag','writeTime'))
      },

      
    }
  }
</script>