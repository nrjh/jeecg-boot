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

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="作业（出入库）类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'code', validatorRules.code]" placeholder="请输入作业（出入库）类型"></a-input>
        </a-form-item>
        <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'color', validatorRules.color]" placeholder="请输入颜色" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'sequence', validatorRules.sequence]" placeholder="请输入序号" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="参考序列" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'sequenceId', validatorRules.sequenceId]" placeholder="请输入参考序列" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sequenceCode', validatorRules.sequenceCode]" placeholder="请输入代码"></a-input>
        </a-form-item>
        <a-form-item label="默认源位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'defaultLoactionSrcId', validatorRules.defaultLoactionSrcId]" placeholder="请输入默认源位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="默认目标位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'defaultLocationDestId', validatorRules.defaultLocationDestId]" placeholder="请输入默认目标位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="退回作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'returnPickingTypeId', validatorRules.returnPickingTypeId]" placeholder="请输入退回作业类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="整个包裹" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'showEntirePacks', validatorRules.showEntirePacks]" placeholder="请输入整个包裹" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'warehouseId', validatorRules.warehouseId]" placeholder="请输入仓库" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'active', validatorRules.active]" placeholder="请输入启用" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="使用新建批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'useCreateLots', validatorRules.useCreateLots]" placeholder="请输入使用新建批号" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="利用已存在批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'useExstingLots', validatorRules.useExstingLots]" placeholder="请输入利用已存在批号" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="详细作业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'showOperations', validatorRules.showOperations]" placeholder="请输入详细作业" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="被保留详细作业" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'showReserved', validatorRules.showReserved]" placeholder="请输入被保留详细作业" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="条码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'barcode', validatorRules.barcode]" placeholder="请输入条码"></a-input>
        </a-form-item>
        <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'companyId', validatorRules.companyId]" placeholder="请输入单位" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: "StkPickingTypeModal",
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
          name: {rules: [
          ]},
          code: {rules: [
          ]},
          color: {rules: [
          ]},
          sequence: {rules: [
          ]},
          sequenceId: {rules: [
          ]},
          sequenceCode: {rules: [
          ]},
          defaultLoactionSrcId: {rules: [
          ]},
          defaultLocationDestId: {rules: [
          ]},
          returnPickingTypeId: {rules: [
          ]},
          showEntirePacks: {rules: [
          ]},
          warehouseId: {rules: [
          ]},
          active: {rules: [
          ]},
          useCreateLots: {rules: [
          ]},
          useExstingLots: {rules: [
          ]},
          showOperations: {rules: [
          ]},
          showReserved: {rules: [
          ]},
          barcode: {rules: [
          ]},
          companyId: {rules: [
          ]},
        },
        url: {
          add: "/iop/stk/pickingtype/add",
          edit: "/iop/stk/pickingtype/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','code','color','sequence','sequenceId','sequenceCode','defaultLoactionSrcId','defaultLocationDestId','returnPickingTypeId','showEntirePacks','warehouseId','active','useCreateLots','useExstingLots','showOperations','showReserved','barcode','companyId'))
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
        this.form.setFieldsValue(pick(row,'name','code','color','sequence','sequenceId','sequenceCode','defaultLoactionSrcId','defaultLocationDestId','returnPickingTypeId','showEntirePacks','warehouseId','active','useCreateLots','useExstingLots','showOperations','showReserved','barcode','companyId'))
      },

      
    }
  }
</script>