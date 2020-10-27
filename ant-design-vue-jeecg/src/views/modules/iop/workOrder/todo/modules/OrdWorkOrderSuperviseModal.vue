<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form"  style="border: 1px solid #e8e8e8;">
<!--        <a-row>-->
<!--          <a-col>-->
<!--            <a-form-item label="填报人电话" :labelCol="{span:3}" :wrapperCol="{span:19}">-->
<!--              <a-input v-model="phone" placeholder="系统自动生成" disabled autocomplete="off"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
        <div style="border: 1px solid #e8e8e8;">
        <a-row  >
          <a-col >
            <a-form-item label="工单名称：" :labelCol="{span:3}" :wrapperCol="{span:19}">
              <a-input placeholder=""  v-decorator="['alarmReason', validatorRules.alarmReason]" disabled></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col>
            <a-form-item label="工单内容" :labelCol="{span:3}" :wrapperCol="{span:19}">
              <a-textarea :rows="3" placeholder="..." v-decorator="['alarmDesc', validatorRules.alarmDesc]" disabled/>
            </a-form-item>
          </a-col>
        </a-row>
        </div>
        <a-row>
          <a-col>
            <a-form-item label="接收人："  :labelCol="{span:3}" :wrapperCol="{span:19}">
              <j-dict-select-tag type="list"
                                 placeholder="请选择接收人"
                                 v-decorator="['dispatchToUser', validatorRules.dispatchToUser]"
                                 :trigger-change="true"
                                 dict-code="sys_user,realname,id"
                                 @change="changePhone"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col>
            <a-form-item label="目标及要求" :labelCol="{span:3}" :wrapperCol="{span:19}">
              <a-textarea :rows="3" placeholder="..." v-decorator="['alarmSugg', validatorRules.alarmSugg]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col >
            <a-form-item label="要求完成时间" :labelCol="{span:3}" :wrapperCol="{span:19}">
              <a-date-picker show-time placeholder="预计需求时间" v-decorator="[ 'pressTime', validatorRules.pressTime]"
                      :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col>
            <a-form-item label="附件：" :labelCol="{span:3}" :wrapperCol="{span:19}">
              <a-input placeholder="" v-model="uploadMessage"></a-input>
               <a-upload
                name="file"
                :multiple="false"
                :action="minioUploadAction"
                :headers="tokenHeader"
                :showUploadList="false"
                :beforeUpload="beforeUpload"
                @change="handleChange">
                <a-button>
                  <a-icon type="upload"/>
                  上传
                </a-button>
              </a-upload>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" type="cancel" @click="handleCancel">
        取消
      </a-button>
      <a-button key="submitAndDispatch" type="primary" @click="handleOk('02')">
        提交并派发
      </a-button>
    </template>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import { httpAction ,getAction } from '@/api/manage'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import { initDictOptions } from '@/components/dict/JDictSelectUtil'


  export default {
    name: "OrdWorkOrderInfoModal",
    components: {
      JDate,
      JDictSelectTag,
      JDictSelectTagOra,
      JDictSelectTagIop,
      ATextarea
    },
    data () {
      return {
        form: this.$form.createForm(this),
        //token header
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        title:"督办登记",
        width:1200,
        visible: false,
        model: {},
        equipLists: [],
        workData: {},
        phone: '',
        uploadMessage: '',
        confirmLoading: false,
        provinceCode: '',
        validatorRules: {
          orderType: {rules: [
          ]},
          lineNo: {rules: [
          ]},
          equipName: {rules: [
          ]},
          alarmLevel: {rules: [
          ]},
          alarmReason: {rules: [
          ]},
          alarmDesc: {rules: [
          ]},
          alarmSugg: {rules: [
          ]},
          pressTime: {rules: [
          ]},
          pressLabe: {rules: [
          ]},
        },
        url: {
          add: "/workOrder/ordWorkOrderInfo/add",
          edit: "/workOrder/ordWorkOrderInfo/edit",
          minioUpload: "/sys/upload/uploadMinio",
          upload: "/oss/file/upload",
          queryById: "/workOrder/ordWorkOrderInfo/queryById",
          queryUserInfoById:"/workOrder/ordWorkOrderFeedBack/queryUserInfoById",
          queryEnclosureInfo:"/ordEnclosure/ordEnclosureInfo/queryEnclosureInfo"
        }
      }
    },
    created () {
    },
    computed: {
      uploadAction() {
        return window._CONFIG['domianURL'] + this.url.upload;
      },
      minioUploadAction() {
        return window._CONFIG['domianURL'] + this.url.minioUpload;
      },
    },
    methods: {
      add () {
        this.edit({});
        initDictOptions("province_code").then(res => {
          console.log("----",res.result[0].value)
          this.provinceCode = '';
          this.provinceCode = res.result[0].value
        })
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'orderCreLabe','orderType','lineNo','equipName','alarmLevel','alarmSource','alarmReason','alarmDesc','alarmSugg','createTime','pressTime','pressLabe','processStatus','createName','remoteStatus','isSuspend'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk (state) {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            httpurl+=this.url.add;
            method = 'post';
            this.workData.processStatus = state
            this.workData.pressLabe = "1"
            this.workData.orderType = "09"
            this.workData.id = this.model.id
            this.workData.provinceCode = this.provinceCode
            let formData = Object.assign(this.workData, values);
            console.log("表数据22222222",formData)
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
        this.form.setFieldsValue(pick(row,'orderCreLabe','orderType','lineNo','equipName','alarmLevel','alarmSource','alarmReason','alarmDesc','alarmSugg','createTime','pressTime','pressLabe','processStatus','createName','remoteStatus','isSuspend'))
      },
      changePhone(value){
        this.phone = ''
        getAction(this.url.queryUserInfoById,{id:value}).then(res=>{
          console.log("res",res)
          console.log("result",res.result)
          this.phone=res.result.phone;
        })
      },
      beforeUpload(file) {
        var fileType = file.type;
        if (fileType === 'image') {
          if (fileType.indexOf('image') < 0) {
            this.$message.warning('请上传图片');
            return false;
          }
        } else if (fileType === 'file') {
          if (fileType.indexOf('image') >= 0) {
            this.$message.warning('请上传文件');
            return false;
          }
        }
        return true
      },
      handleChange(info) {
        console.log("info----------",info)
        if (info.file.status === 'done') {
          if (info.file.response.success) {
            this.uploadMessage = info.file.name;
            this.workData.enclosureName = info.file.name;
            this.workData.enclosureUrl = info.file.response.message;
            this.$message.success(`${info.file.name} 上传成功!`);
          } else {
            this.$message.error(`${info.file.response.message}`);
          }
        } else if (info.file.status === 'error') {
          this.$message.error(`${info.file.response.message}`);
        }
      },

      supervise(id){
        this.form.resetFields();
        getAction(this.url.queryById, {id: id}).then((res) => {
          if (res.success) {
            this.model = Object.assign({}, res.result);
            // console.log(this.model, '77777777777')
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'dispatchToUser','alarmReason','alarmDesc', 'alarmSugg', 'pressTime',))
            })
            this.changePhone(this.model.dispatchToUser)
          }
        });
        getAction(this.url.queryEnclosureInfo, {id: id}).then((res) => {
          if (res.success) {
            this.uploadMessage = res.result.enclosureName;
          }
        });
      }
    }
  }
</script>