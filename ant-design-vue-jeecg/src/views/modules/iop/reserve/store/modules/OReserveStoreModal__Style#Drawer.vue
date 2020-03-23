<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="节点标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeId', validatorRules.storeId]" placeholder="请输入节点标识"></a-input>
        </a-form-item>
        <a-form-item label="节点名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeName', validatorRules.storeName]" placeholder="请输入节点名称"></a-input>
        </a-form-item>
        <a-form-item label="节点编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeNo', validatorRules.storeNo]" placeholder="请输入节点编号"></a-input>
        </a-form-item>
        <a-form-item label="节点位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeAddress', validatorRules.storeAddress]" placeholder="请输入节点位置"></a-input>
        </a-form-item>
        <a-form-item label="节点级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'storeLevel', validatorRules.storeLevel]" placeholder="请输入节点级别" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeParent', validatorRules.storeParent]" placeholder="请输入父级节点"></a-input>
        </a-form-item>
        <a-form-item label="节点状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeStatus', validatorRules.storeStatus]" placeholder="请输入节点状态"></a-input>
        </a-form-item>
        <a-form-item label="是否虚拟" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeIsvirtual', validatorRules.storeIsvirtual]" placeholder="请输入是否虚拟"></a-input>
        </a-form-item>
        <a-form-item label="四线一库设备" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeEquipCateg', validatorRules.storeEquipCateg]" placeholder="请输入四线一库设备"></a-input>
        </a-form-item>
        <a-form-item label="层" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'storeLayer', validatorRules.storeLayer]" placeholder="请输入层" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="列" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'storeColumn', validatorRules.storeColumn]" placeholder="请输入列" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="操作时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择操作时间" v-decorator="[ 'optDate', validatorRules.optDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'optCode', validatorRules.optCode]" placeholder="请输入操作人"></a-input>
        </a-form-item>
        
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  
  export default {
    name: "OReserveStoreModal",
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
          storeId: {rules: [
            {required: true, message: '请输入节点标识!'},
          ]},
          storeName: {rules: [
          ]},
          storeNo: {rules: [
          ]},
          storeAddress: {rules: [
          ]},
          storeLevel: {rules: [
          ]},
          storeParent: {rules: [
          ]},
          storeStatus: {rules: [
          ]},
          storeIsvirtual: {rules: [
          ]},
          storeEquipCateg: {rules: [
          ]},
          storeLayer: {rules: [
          ]},
          storeColumn: {rules: [
          ]},
          optDate: {rules: [
          ]},
          optCode: {rules: [
          ]},
        },
        url: {
          add: "/iop/reserve/store/add",
          edit: "/iop/reserve/store/edit",
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
          this.form.setFieldsValue(pick(this.model,'storeId','storeName','storeNo','storeAddress','storeLevel','storeParent','storeStatus','storeIsvirtual','storeEquipCateg','storeLayer','storeColumn','optDate','optCode'))
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
        this.form.setFieldsValue(pick(row,'storeId','storeName','storeNo','storeAddress','storeLevel','storeParent','storeStatus','storeIsvirtual','storeEquipCateg','storeLayer','storeColumn','optDate','optCode'))
      }
      
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>