<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-form :form="form">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item v-if="status == 1" label="年份" :labelCol="{span:3}" :wrapperCol="{span:20}">
                <a-input autocomplete="off" v-decorator="['cycleYear', validatorRules.cycleYear]" placeholder="请输入年份"></a-input>
              </a-form-item>
              <a-form-item v-if="status == 2" label="月份" :labelCol="{span:3}" :wrapperCol="{span:20}">
                <a-input autocomplete="off" v-decorator="['cycleMonth', validatorRules.cycleMonth]" placeholder="请输入月份"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
    </a-form>
  </a-modal>
</template>
<script>

  import { httpAction ,getAction } from '@/api/manage'
  export default {
    name: "AddModel",
    components: {

    },
    data () {
      return {
        status:"",
        parentId:"",
        validatorRules: {
          cycleYear: {rules: [{required:true,message:"请输入年份"}]},
          cycleMonth:{rules: [{required:true,message:"请输入月份"}]}
        },
        title:"操作",
        confirmLoading:false,
        form: this.$form.createForm(this),
        //token header
        width:800,
        visible: false,
        model: {},
      }
    },
    created () {
    },
    methods: {
      add(parentId,status) {
        this.form.resetFields();
        this.parentId = parentId;
        this.status = status;
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '/prod/devProduceCycle/add';
            let method = 'post';
            let formData = Object.assign({parentId:this.parentId,isDel:'0'}, values);
            console.log("表数据22222222",formData);
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
      handleCancel() {
        this.close()
      }
    }
  }
</script>