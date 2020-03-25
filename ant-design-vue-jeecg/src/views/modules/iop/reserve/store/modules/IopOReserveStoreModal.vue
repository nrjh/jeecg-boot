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

        <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select-iop
            ref="treeSelect"
            placeholder="请选择父级节点"
            v-decorator="['pid', validatorRules.pid]"
            dict="iop_o_reserve_store,STORE_NAME,id"
            pidField="pid"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select-iop>
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
          <j-dict-select-tag v-decorator="[ 'storeId', validatorRules.storeLevel]" :triggerChange="true"
                                 placeholder="请选择节点级别"
                                 dictCode="IOP_RESERVE_STORE_LEVEL"/>
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
        <a-form-item label="是否虚拟" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="是" unCheckedChildren="否" v-model="storeIsvirtual"/>
        </a-form-item>
        <a-form-item label="节点状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-model="storeStatus"/>
        </a-form-item>
        
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue ,iopValidateDuplicateValue} from '@/utils/util'
  import JTreeSelectIop from '@/components/jeecg/JTreeSelectIop'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "IopOReserveStoreModal",
    components: {
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
        storeIsvirtual: false, // 是否虚拟
        storeStatus: true,  // 启用状态
        validatorRules: {
          pid: {rules: [
          ]},
          storeName: {rules: [
            {required: true, message: '请输入节点名称!'},
          ]},
          storeNo: {rules: [
            {required: true, message: '请输入节点编号!'},
            { validator: (rule, value, callback) => iopValidateDuplicateValue('iop_o_reserve_store', 'store_no', value, this.model.id, callback)},
          ]},
          storeAddress: {rules: [
          ]},
          storeLevel: {rules: [
              {required: true, message: '请选择节点级别!'},
          ]},
          storeEquipCateg: {rules: [
          ]},
          storeLayer: {rules: [
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
          storeColumn: {rules: [
            {pattern:/^-?\d+$/, message: '请输入整数!'},
          ]},
        },
        url: {
          add: "/iop/reserve/store/add",
          edit: "/iop/reserve/store/edit",
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
        if(record.storeIsvirtual==1){
          this.storeIsvirtual = true;
        }else{
          this.storeIsvirtual = false; // 升级兼容 如果没有（后台没有传过来、或者是新建）默认为false
        }
        if(record.storeStatus==1){
          this.storeStatus = true;
        }else{
          this.storeStatus = false; // 升级兼容 如果没有（后台没有传过来、或者是新建）默认为false
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'pid','storeName','storeNo','storeAddress','storeLevel','storeStatus','storeIsvirtual','storeEquipCateg','storeLayer','storeColumn'))
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
            this.model.storeIsvirtual = this.storeIsvirtual==true?1:0;  //将true改为1，false改为0
            this.model.storeStatus = this.storeStatus==true?1:0;  //将true改为1，false改为0
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
        this.form.setFieldsValue(pick(row,'pid','storeName','storeNo','storeAddress','storeLevel','storeStatus','storeIsvirtual','storeEquipCateg','storeLayer','storeColumn'))
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