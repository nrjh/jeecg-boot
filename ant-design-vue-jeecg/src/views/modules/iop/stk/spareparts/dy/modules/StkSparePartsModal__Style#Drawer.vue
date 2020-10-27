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

        <a-form-item label="物料编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productNo', validatorRules.productNo]" placeholder="请输入物料编号"></a-input>
        </a-form-item>
        <a-form-item label="物料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productName', validatorRules.productName]" placeholder="请输入物料名称"></a-input>
        </a-form-item>
        <a-form-item label="物料名称缩写" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productNameAbb', validatorRules.productNameAbb]" placeholder="请输入物料名称缩写"></a-input>
        </a-form-item>
        <a-form-item label="物料类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['categoryId', validatorRules.categoryId]" :trigger-change="true" dictCode="" placeholder="请选择物料类别"/>
        </a-form-item>
        <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['attributeCategoryId', validatorRules.attributeCategoryId]" :trigger-change="true" dictCode="" placeholder="请选择规格"/>
        </a-form-item>
        <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['uomId', validatorRules.uomId]" :trigger-change="true" dictCode="" placeholder="请选择单位"/>
        </a-form-item>
        <a-form-item label="货位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['locationId', validatorRules.locationId]" :trigger-change="true" dictCode="" placeholder="请选择货位"/>
        </a-form-item>
        <a-form-item label="最低库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'stockMin', validatorRules.stockMin]" placeholder="请输入最低库存" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="最高库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'stockMax', validatorRules.stockMax]" placeholder="请输入最高库存" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="关联设备" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'joinParts', validatorRules.joinParts]" placeholder="请输入关联设备"></a-input>
        </a-form-item>
        <a-form-item label="初始数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'initQty', validatorRules.initQty]" placeholder="请输入初始数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="初始总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'initAmount', validatorRules.initAmount]" placeholder="请输入初始总金额" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="警戒库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'stockWarn', validatorRules.stockWarn]" placeholder="请输入警戒库存" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true"></j-upload>
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
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "StkSparePartsModal",
    components: { 
      JUpload,
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
          productNo: {rules: [
          ]},
          productName: {rules: [
          ]},
          productNameAbb: {rules: [
          ]},
          categoryId: {rules: [
          ]},
          attributeCategoryId: {rules: [
          ]},
          uomId: {rules: [
          ]},
          locationId: {rules: [
          ]},
          stockMin: {rules: [
          ]},
          stockMax: {rules: [
          ]},
          joinParts: {rules: [
          ]},
          initQty: {rules: [
          ]},
          initAmount: {rules: [
          ]},
          stockWarn: {rules: [
          ]},
          remark: {rules: [
          ]},
          photo: {rules: [
          ]},
        },
        url: {
          add: "/spareparts/stkSpareParts/add",
          edit: "/spareparts/stkSpareParts/edit",
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
          this.form.setFieldsValue(pick(this.model,'productNo','productName','productNameAbb','categoryId','attributeCategoryId','uomId','statusPro','locationId','stockMin','stockMax','joinParts','initQty','initAmount','stockWarn','remark','photo','priceAvg'))
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
        this.form.setFieldsValue(pick(row,'productNo','productName','productNameAbb','categoryId','attributeCategoryId','uomId','statusPro','locationId','stockMin','stockMax','joinParts','initQty','initAmount','stockWarn','remark','photo','priceAvg'))
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