<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'description', validatorRules.description]" placeholder="请输入描述"></a-input>
        </a-form-item>
        <a-form-item label="仅内部" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'internal', validatorRules.internal]" placeholder="请输入仅内部" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="父级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择父级"
            v-decorator="['pid', validatorRules.pid]"
            dict="message_subtype,name,id"
            pidField="parent_id"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select>
        </a-form-item>
        <a-form-item label="默认" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['isDefault', validatorRules.isDefault]" :trigger-change="true" dictCode="" placeholder="请选择默认"/>
        </a-form-item>
        <a-form-item label="顺序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sequence', validatorRules.sequence]" :trigger-change="true" dictCode="" placeholder="请选择顺序"/>
        </a-form-item>
        <a-form-item label="隐藏" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'isHidden', validatorRules.isHidden]" placeholder="请输入隐藏"></a-input>
        </a-form-item>
        
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  
  export default {
    name: "MessageSubtypeModal",
    components: { 
      JDictSelectTag,
      JTreeSelect
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
            { validator: (rule, value, callback) => validateDuplicateValue('message_subtype', 'name', value, this.model.id, callback)},
          ]},
          description: {rules: [
          ]},
          internal: {rules: [
          ]},
          pid: {rules: [
          ]},
          isDefault: {rules: [
            {required: true, message: '请输入默认!'},
          ]},
          sequence: {rules: [
          ]},
          isHidden: {rules: [
            {required: true, message: '请输入隐藏!'},
          ]},
        },
        url: {
          add: "/iop/message/subtype/add",
          edit: "/iop/message/subtype/edit",
        },
        expandedRowKeys:[],
        pidField:"pid"
     
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
          this.form.setFieldsValue(pick(this.model,'name','description','internal','pid','isDefault','sequence','isHidden'))
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
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values);
            let new_pid = this.model[this.pidField]
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.submitSuccess(formData,old_pid==new_pid)
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
        this.form.setFieldsValue(pick(row,'name','description','internal','pid','isDefault','sequence','isHidden'))
      },
      submitSuccess(formData,flag){
        if(!formData.id){
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys=[]
          this.getExpandKeysByPid(formData[this.pidField],treeData,treeData)
          this.$emit('ok',formData,this.expandedRowKeys.reverse());
        }else{
          this.$emit('ok',formData,flag);
        }
      },
      getExpandKeysByPid(pid,arr,all){
        if(pid && arr && arr.length>0){
          for(let i=0;i<arr.length;i++){
            if(arr[i].key==pid){
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['pid'],all,all)
            }else{
              this.getExpandKeysByPid(pid,arr[i].children,all)
            }
          }
        }
      }
      
      
    }
  }
</script>