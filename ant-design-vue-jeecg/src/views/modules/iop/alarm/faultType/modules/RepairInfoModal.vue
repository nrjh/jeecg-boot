<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :footer="null"
    @cancel="handleCancel"
    >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
            <a-form-item label="修复方法"  :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input autocomplete ='off' v-decorator="[ 'repairName', validatorRules.repairName]" placeholder=""></a-input>
            </a-form-item>
            <a-form-item label="修复类型"  :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag  v-decorator="['execMode', validatorRules.execMode]" :trigger-change="true" dictCode="EXEC_MODE" placeholder=""/>
            </a-form-item>
            <a-form-item label="显示顺序"  :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number  v-decorator="[ 'sequence', validatorRules.sequence]" placeholder=""></a-input-number >
            </a-form-item>
            <a-form-item label="是否更换" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-decorator="['isReplace', validatorRules.isReplace]"  >
                <a-radio value="Y">
                  是
                </a-radio>
                <a-radio value="N">
                  否
                </a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item
              label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol"
              >
              <a-textarea :rows="3" placeholder="..." v-decorator="[ 'remark', { rules: [] } ]" />
            </a-form-item>
      </a-form>
      <!-- 操作按钮区域 -->
      <div class="ant-table-footer" style="text-align: center">
        <a-button @click="handleCancel" type="primary" style="margin: 5px" icon="delete">关闭</a-button>
        <a-button @click="handleOk" type="primary"  style="margin: 5px" icon="plus">保存</a-button>
      </div>
    </a-spin>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import AInput from "ant-design-vue/es/input/Input";
  export default {
    name: "RepairInfoModal",
    components: {
      AInput, JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"新增修复方法",
        width:800,
        visible: false,
        model: {},
        formIndex:"",
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
          repairName: {rules: [
            { required: true, message: '请输入修复方法!' }
          ]},
          execMode: {rules: [
            { required: true, message: '请选择修复类型!' }
          ]},
          isReplace: {rules: [
            { required: true, message: '是否更换!' }
          ]},
          sequence: {rules: [
            { required: true, message: '请输入显示顺序!' }
          ]},

        },
        url: {
          add: "/faultType/faultTaskInfo/add",
          edit: "/faultType/faultTaskInfo/edit",
        }
      }
    },
    created () {
    },
    methods: {
      close () {
        this.$emit('close');
        this.visible = false;
        this.confirmLoading = false;
        this.form.resetFields();
      },
      add(){
          this.visible=true;
          this.form.resetFields();
      },
      edit(index,record){
          this.formIndex = index;
          this.form.resetFields();
          this.model = Object.assign({}, record);
          this.visible = true;
          this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'repairName', 'execMode', 'sequence', 'isReplace','remark'))
          });
      },
      handleOk(){
        let  returnData=[];
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
//            let formData = Object.assign(this.model, values);
            returnData=values;
//            console.log("表单提交数据",formData)
            console.log("returnData",returnData);
            this.$emit('returnDataRepair',returnData,this.formIndex)
            this.close();
          }
        })
      },
      handleCancel(){
        this.close();
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },

    }
  }
</script>