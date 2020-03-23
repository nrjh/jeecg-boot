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

        <a-form-item label="尺寸" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletSize', validatorRules.palletSize]" placeholder="请输入尺寸"></a-input>
        </a-form-item>
        <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletColor', validatorRules.palletColor]" placeholder="请输入颜色"></a-input>
        </a-form-item>
        <a-form-item label="制作材质" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletMaterial', validatorRules.palletMaterial]" placeholder="请输入制作材质"></a-input>
        </a-form-item>
        <a-form-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletNo', validatorRules.palletNo]" placeholder="请输入编号"></a-input>
        </a-form-item>
        <a-form-item label="序号 用于排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'palletOrder', validatorRules.palletOrder]" placeholder="请输入序号 用于排序" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="所属货位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeId', validatorRules.storeId]" placeholder="请输入所属货位"></a-input>
        </a-form-item>
        <a-form-item label="托盘状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeStatus', validatorRules.storeStatus]" placeholder="请输入托盘状态"></a-input>
        </a-form-item>
        <a-form-item label="是否虚拟" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'storeIsvirtual', validatorRules.storeIsvirtual]" placeholder="请输入是否虚拟"></a-input>
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
  
  export default {
    name: "IopOReservePalletModal",
    components: { 
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
          palletSize: {rules: [
          ]},
          palletColor: {rules: [
          ]},
          palletMaterial: {rules: [
          ]},
          palletNo: {rules: [
          ]},
          palletOrder: {rules: [
          ]},
          storeId: {rules: [
          ]},
          storeStatus: {rules: [
          ]},
          storeIsvirtual: {rules: [
          ]},
        },
        url: {
          add: "/iop/reserve/iopOReservePallet/add",
          edit: "/iop/reserve/iopOReservePallet/edit",
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
          this.form.setFieldsValue(pick(this.model,'palletSize','palletColor','palletMaterial','palletNo','palletOrder','storeId','storeStatus','storeIsvirtual','optDate','optCode'))
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
        this.form.setFieldsValue(pick(row,'palletSize','palletColor','palletMaterial','palletNo','palletOrder','storeId','storeStatus','storeIsvirtual','optDate','optCode'))
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