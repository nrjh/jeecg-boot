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

        <a-form-item label="主题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'subject', validatorRules.subject]" placeholder="请输入主题"></a-input>
        </a-form-item>
        <a-form-item label="作者" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'authorId', validatorRules.authorId]" placeholder="请输入作者"></a-input>
        </a-form-item>
        <a-form-item label="发布时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择发布时间" v-decorator="[ 'pubTime', validatorRules.pubTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="正文" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'body', validatorRules.body]" placeholder="请输入正文"></a-input>
        </a-form-item>
        <a-form-item label="上级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择上级"
            v-decorator="['pid', validatorRules.pid]"
            dict="message_message,subject,id"
            pidField="parent_id"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select>
        </a-form-item>
        <a-form-item label="消息类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'messageType', validatorRules.messageType]" placeholder="请输入消息类型"></a-input>
        </a-form-item>
        <a-form-item label="子类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'subtypeId', validatorRules.subtypeId]" placeholder="请输入子类型"></a-input>
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
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  
  export default {
    name: "MessageMessageModal",
    components: { 
      JDate,
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
          subject: {rules: [
            {required: true, message: '请输入主题!'},
          ]},
          authorId: {rules: [
            {required: true, message: '请输入作者!'},
          ]},
          pubTime: {rules: [
            {required: true, message: '请输入发布时间!'},
          ]},
          body: {rules: [
            {required: true, message: '请输入正文!'},
          ]},
          pid: {rules: [
            {required: true, message: '请输入上级!'},
          ]},
          messageType: {rules: [
            {required: true, message: '请输入消息类型!'},
          ]},
          subtypeId: {rules: [
            {required: true, message: '请输入子类型!'},
          ]},
        },
        url: {
          add: "/iop/message/message/add",
          edit: "/iop/message/message/edit",
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
          this.form.setFieldsValue(pick(this.model,'subject','authorId','pubTime','body','pid','messageType','subtypeId','createBy','createTime','updateBy','updateTime'))
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
        this.form.setFieldsValue(pick(row,'subject','authorId','pubTime','body','pid','messageType','subtypeId','createBy','createTime','updateBy','updateTime'))
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