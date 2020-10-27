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

        <a-form-item label="物品" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['productId', validatorRules.productId]" :trigger-change="true" dictCode="PRD_PRODUCT,NAME,ID" placeholder="请选择物品"/>
        </a-form-item>
        <a-form-item label="批次/序列号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['productLotId', validatorRules.productLotId]" :trigger-change="true" dictCode="stk_production_lot,name,id" placeholder="请选择批次/序列号"/>
        </a-form-item>
        <a-form-item label="仓库位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'locationId', validatorRules.locationId]" placeholder="请输入仓库位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="完成数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'quantity', validatorRules.quantity]" placeholder="请输入完成数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['companyId', validatorRules.companyId]" :trigger-change="true" dictCode="uom_uom,name,id" placeholder="请选择单位"/>
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "StkQuantModal",
    components: { 
      JDictSelectTag,
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
          productId: {rules: [
            {required: true, message: '请输入物品!'},
          ]},
          productLotId: {rules: [
            {required: true, message: '请输入批次/序列号!'},
          ]},
          locationId: {rules: [
            {required: true, message: '请输入仓库位置!'},
          ]},
          quantity: {rules: [
            {required: true, message: '请输入完成数量!'},
           {pattern:/^-?\d+\.?\d*$/, message: '请输入数字!'},
          ]},
          companyId: {rules: [
            {required: true, message: '请输入单位!'},
          ]},
        },
        url: {
          add: "/stk.quant/stkQuant/add",
          edit: "/stk.quant/stkQuant/edit",
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
          this.form.setFieldsValue(pick(this.model,'productId','productLotId','locationId','quantity','companyId'))
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
        this.form.setFieldsValue(pick(row,'productId','productLotId','locationId','quantity','companyId'))
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