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



        <a-form-item label="品类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入品类名称"></a-input>
        </a-form-item>
        <a-form-item label="上级品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-search-select-tag-iop placeholder="请选择上级品类" v-decorator="[ 'pid', validatorRules.pid]"
            dict="PRD_CATEGORY,COMPLETE_NAME,ID"  :async="true">
          </j-search-select-tag-iop>
        </a-form-item>
        <a-form-item label="品类类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag v-decorator="[ 'categoryType', validatorRules.categoryType]" :triggerChange="true"
                                 placeholder="请选择品类类型"
                                 dictCode="IOP_PRD_CATEGORY_TYPE"/>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="启用" unCheckedChildren="禁用"  v-decorator="['active', validatorRules.active]"/>
        </a-form-item>
        <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: none">
          <a-input-number v-decorator="[ 'sequence', validatorRules.sequence]" placeholder="请输入序号" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="下架策略" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: none">
          <a-input v-decorator="[ 'removalStrategyId', validatorRules.removalStrategyId]" placeholder="请输入下架策略"></a-input>
        </a-form-item>
        <a-form-item label="名称路径" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: none">
          <a-input v-decorator="[ 'completeName', validatorRules.completeName]" placeholder="请输入名称路径"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JSearchSelectTagIop from '@/components/dict/JSearchSelectTagIop'

  export default {
    name: "PrdCategoryModal",
    components: {
      JDictSelectTag,
      JSearchSelectTagIop
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
            {required: true, message: '请输入名称!'},
          ]},
          pid: {rules: [
              {required: true, message: '请选择上级品类!'},
          ]},
          categoryType: {rules: [
            {required: true, message: '请选择品类类型!'},
          ]},
          active: {
            valuePropName: 'checked',
            rules: [
            {required: true, message: '请选择状态!'},
          ]},
          sequence: {rules: [
              {required: false, message: '请输入序号!'},
            ]},
          removalStrategyId: {rules: []},
          completeName: {rules: []},
        },
        url: {
          add: "/iop/prd/category/add",
          edit: "/iop/prd/category/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({active:1});
      },
      edit (record) {
        debugger;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.active = record.active==1 ? true:false;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','pid','categoryType','active','sequence','completeName','removalStrategyId'))
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
            values.active = values.active==true?1:0;  //将true改为1，false改为0
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
        this.form.setFieldsValue(pick(this.model,'name','pid','categoryType','active','sequence','completeName','removalStrategyId'))
      },

      
    }
  }
</script>