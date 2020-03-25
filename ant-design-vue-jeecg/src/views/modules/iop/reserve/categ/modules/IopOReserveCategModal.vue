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
        <a-form-item label="所属类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select-iop
            ref="treeSelect"
            placeholder="请选择所属类别"
            v-decorator="['pid', validatorRules.pid]"
            dict="iop_o_reserve_categ,CATEG_NAME,id"
            pidField="pid"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select-iop>
        </a-form-item>
        <a-form-item label="类别名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'categName', validatorRules.categName]" placeholder="请输入类别名称"></a-input>
        </a-form-item>
        <a-form-item label="类别编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'categNo', validatorRules.categNo]" placeholder="请输入类别编码"></a-input>
        </a-form-item>
        <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'categOrder', validatorRules.categOrder]" placeholder="请输入序号" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['categUnit', validatorRules.categUnit]" :trigger-change="true" dictCode="IOP_RESERVE_UNIT" placeholder="请输入计量单位"/>
        </a-form-item>
        <a-form-item label="安全库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'categSafetyNum', validatorRules.categSafetyNum]" placeholder="请输入安全库存" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="预警库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'categWarnNum', validatorRules.categWarnNum]" placeholder="请输入预警库存" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="类别状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <!--
          <a-input v-decorator="[ 'categStatus', validatorRules.categStatus]" placeholder="请输入类别状态"></a-input>
          -->
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-model="categStatus"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue,iopValidateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JTreeSelectIop from '@/components/jeecg/JTreeSelectIop'
  
  export default {
    name: "IopOReserveCategModal",
    components: { 
      JDate,
      JDictSelectTag,
      JTreeSelectIop
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
        categStatus: true, // 启用状态
        validatorRules: {
          pid: {rules: [
            {required: true, message: '请选择所属类别!'},
          ]},
          categName: {rules: [
            {required: true, message: '请输入类别名称!'},
          ]},
          categNo: {rules: [
            {required: true, message: '请输入类别编码!'},
            { validator: (rule, value, callback) => iopValidateDuplicateValue('iop_o_reserve_categ', 'categ_no', value, this.model.id, callback)},
          ]},
          categOrder: {rules: [
          ]},
          categUnit: {rules: [
            {required: true, message: '请输入计量单位!'},
          ]},
          categSafetyNum: {rules: [
            {required: true, message: '请输入安全库存!'},
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
          categWarnNum: {rules: [
            {required: true, message: '请输入预警库存!'},
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
        },
        url: {
          add: "/iop/reserve/categ/add",
          edit: "/iop/reserve/categ/edit",
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
        if(record.categStatus==1){
          this.categStatus = true;
        }else{
          this.categStatus = false; // 升级兼容 如果没有（后台没有传过来、或者是新建）默认为false
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'pid','categName','categNo','categOrder','categUnit','categSafetyNum','categWarnNum','categStatus'))
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
            debugger
            this.model.categStatus = this.categStatus==true?1:0;  //将true改为1，false改为0
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
        this.form.setFieldsValue(pick(row,'pid','categName','categNo','categOrder','categUnit','categSafetyNum','categWarnNum','categStatus'))
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
              this.getExpandKeysByPid(arr[i]['parentId'],all,all)
            }else{
              this.getExpandKeysByPid(pid,arr[i].children,all)
            }
          }
        }
      }
      
      
    }
  }
</script>