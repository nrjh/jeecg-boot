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
        <a-form :form="form" >

            <a-form-item label="品牌名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入品牌名称" ></a-input>
            </a-form-item>
            <a-form-item label="首字母" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'firstLetter', validatorRules.firstLetter]" placeholder="请输入首字母"></a-input>
            </a-form-item>
            <a-form-item label="制造商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'manufactorId', validatorRules.manufactorId]" :triggerChange="true"
                                     placeholder="请选择制造商"
                                     dictCode="RES_PARTNER,NAME,ID"/>
            </a-form-item>
            <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-switch checkedChildren="启用" unCheckedChildren="禁用"  v-model="active"/>
            </a-form-item>
            <a-form-item label="简介" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['description', validatorRules.description]" rows="4" placeholder="请输入简介"/>
            </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue,iopValidateDuplicateValue } from '@/utils/util'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'

  export default {
    name: "PrdBrandModal",
    components: {
      JDictSelectTagIop
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        componentDisabled: null,
        model: { },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        active:true , // 默认启用
        viewStatus:false, // 是否详情页面
        validatorRules: {
          name: {rules: [
            {required: true, message: '请输入名称!'},
            { validator: (rule, value, callback) => iopValidateDuplicateValue('prd_brand', 'name', value, this.model.id, callback)},
          ]},
          firstLetter: {rules: [
            {required: true, message: '请输入首字母!'},
          ]},
          manufactorId: {rules: [
            {required: true, message: '请选择制造商!'},
          ]},
          active: {rules: [
              {required: true, message: '请选择状态!',},
            ]},
          description: {rules: [
          ]},
        },
        url: {
          add: "/iop/prd/brand/add",
          edit: "/iop/prd/brand/edit",
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
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.active = record.active==1 ? true:false;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'active','name','firstLetter','manufactorId','description','companyId','createBy','createTime','updateBy','updateTime'))
        })
      },
      view (record) {
        this.viewStatus = true;
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.active = record.active==1 ? true:false;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'active','name','firstLetter','manufactorId','description','companyId','createBy','createTime','updateBy','updateTime'))
        })
        // 修改控件状态为不可用

        // 增加操作按钮

        // 增加相关信息展示
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        if(this.viewStatus == true){
          this.$emit('close');
          this.visible = false;
        }else{
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
        }
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'active','name','firstLetter','manufactorId','description','companyId','createBy','createTime','updateBy','updateTime'))
      },

      
    }
  }
</script>