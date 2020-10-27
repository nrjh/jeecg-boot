<template>
  <a-modal
    title="挂单"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row >
          <a-col >
            <a-form-item label="操作类型" :labelCol="{span:3}" :wrapperCol="{span:18}">
              <a-radio-group v-model="queryParam.suspendStauts">
                <a-radio value="01">
                  挂起
                </a-radio>
                <a-radio value="02">
                  解除
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="this.queryParam.suspendStauts == '01'">
          <a-col>
            <a-form-item label="挂起原因" :labelCol="{span:3}" :wrapperCol="{span:18}">
              <a-textarea :rows="3" placeholder="..." v-model="queryParam.suspendReason"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row v-if="this.queryParam.suspendStauts == '02'">
          <a-col>
            <a-form-item label="解除原因" :labelCol="{span:3}" :wrapperCol="{span:18}">
              <a-textarea :rows="3" placeholder="..." v-model="queryParam.suspendReason"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col>
            <a-form-item label="结束时间" :labelCol="{span:3}" :wrapperCol="{span:18}">
              <j-date placeholder="结束时间" v-model="queryParam.suspendEndTime"
                      :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
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
        width:800,
        visible: false,
        model: {},
        workData: {},
        queryParam: {},
        uploadMessage: '',
        confirmLoading: false,
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
          add: "/ordSuspend/ordSuspend/add",
          edit: "/workOrder/ordWorkOrderInfo/edit",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
          minioUpload: "/sys/upload/uploadMinio",
          upload: "/oss/file/upload",

        }
      }
    },
    created () {
    },
    methods: {
      edit(record) {
        this.form.resetFields();
        this.queryParam.workOrderId = record.id
        // this.model = Object.assign({}, record);
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        // this.form.validateFields((err, values) => {
        //   if (!err) {
        //     that.confirmLoading = true;
        //     let httpurl = '';
        //     let method = '';
        //     if(!this.model.id){
        //       httpurl+=this.url.add;
        //       method = 'post';
        //     }else{
        //       httpurl+=this.url.edit;
        //        method = 'put';
        //     }
        // this.workData.
        // console.log("表单提交数据",formData)
        // httpAction(httpurl,this.returnData,method).then((res)=>{
        //   if(res.success){
        //     that.$message.success(res.message);
        //   }
        // }
        // let formData = Object.assign(this.queryParam, values);
        that.confirmLoading = true;
        let httpurl = '';
        let method = '';
        httpurl += this.url.add;
        method = 'post';
        httpAction(httpurl, this.queryParam, method).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.$emit('ok');
          } else {
            that.$message.warning(res.message);
          }
        }).finally(() => {
          that.confirmLoading = false;
          that.close();
        })
        // })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'orderCreLabe', 'orderType', 'lineNo', 'equipName', 'alarmLevel', 'alarmSource', 'alarmReason', 'alarmDesc', 'alarmSugg', 'createTime', 'pressTime', 'pressLabe', 'processStatus', 'createName', 'remoteStatus', 'isSuspend'))
      },
    }
  }
</script>