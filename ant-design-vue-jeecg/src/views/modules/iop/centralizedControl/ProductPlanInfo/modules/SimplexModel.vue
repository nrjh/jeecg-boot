<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <span v-if="flag == 'D'">
        <a-form-item label="单相频率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            :trigger-change="true"
            type="list" v-decorator="['equipRateD', validatorRules.equipRateD]"
                                 dictCode="D_EQUIP_RATE"
                                 placeholder="请选择单相频率"/>
        </a-form-item>
        </span>
        <span v-if="flag == 'S'">
        <a-form-item label="三相物料描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            type="list"
            :trigger-change="true"
            v-decorator="['equipDescribeS', validatorRules.equipDescribeS]"
            dictCode="EQUIP_DESCRIBE_S"
            placeholder="请选择三相物料描述"/>
        </a-form-item>

        <a-form-item label="三相频率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            :trigger-change="true"
            v-decorator="['equipRateS', validatorRules.equipRateS]"
            type="list"
            dictCode="D_EQUIP_RATE_S"
            placeholder="请输入三相频率"/>
        </a-form-item>

                  <a-form-item label="三相电流" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            :trigger-change="true"
            v-decorator="['equipI', validatorRules.equipI]"
            type="list"
            dictCode="D_EQUIP_I_S"
            placeholder="请选择三相电流"/>
        </a-form-item>
        </span>
        <span v-if="flag == 'H'">
        <a-form-item label="变比" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            :trigger-change="true"
            v-decorator="['equipScale', validatorRules.equipScale]"
            type="list"
            dictCode="H_EQUIP_SCALE"
            placeholder="请选择变比"/>
        </a-form-item>
        </span>
        <span v-if="flag == 'C'">
        <a-form-item label="终端物料描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            :trigger-change="true"
            v-decorator="['equipDescribeC', validatorRules.equipDescribeC]"
            type="list"
            dictCode="EQUIP_DESCRIBE_C"
            placeholder="请选择终端物料描述"/>
        </a-form-item>

        <a-form-item label="终端载波频率" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            :trigger-change="true"
            v-decorator="['equipRateC', validatorRules.equipRateC]"
            type="list"
            dictCode="C_EQUIP_RATE"
            placeholder="请选择终端载波频率"/>
        </a-form-item>

                  <a-form-item label="终端电压/通信方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag
            style="width: 100%"
            v-decorator="['equipSignalC', validatorRules.equipSignalC]"
            type="list"
            :trigger-change="true"
            dictCode="C_EQUIP_SIGNAL"
            placeholder="请选择终端电压/通信方式"/>
        </a-form-item>
        </span>
        <a-form-item label="拟检测数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input autocomplete="off" placeholder="请输入拟检测数"
                   v-decorator="['equipNumber', validatorRules.equipNumber]"/>
        </a-form-item>

        <a-form-item label="检测日类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-radio-group
            v-decorator="['dateType', validatorRules.dateType]">
            <a-radio value="G">
              工作日
            </a-radio>
            <a-radio value="Z">
              自然日
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="计划检测天数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input autocomplete="off" placeholder="请输入计划检测天数"
                   v-decorator="['planDate', validatorRules.planDate]"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import moment from 'moment'
  import Vue from 'vue'
  import pick from "lodash.pick";
  // 引入搜索部门弹出框的组件
  import JSelectPosition from '@/components/jeecgbiz/JSelectPosition'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import {postAction, putAction} from "../../../../../../api/manage";
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'

  export default {
    name: 'SimplexModel',
    components: {
      AFormItem,
      JSelectPosition,
      JDictSelectTagOra,
      JDictSelectTag
    },
    moment,
    data() {
      return {
        flag:'D',
        produceCycleId:"",
        validatorRules: {
          equipRateD: {rules: [
              {required: true, message: '请选择单相频率'},
            ]},
          equipNumber: {rules: [
              {required: true, message: '请输入拟检测数'},
            ]},
          dateType: {rules: [
              {required: true, message: '请选择检测日类型'},
            ]},
          planDate: {rules: [
              {required: true, message: '请输入计划检测天数'},
            ]},
          equipDescribeS: {rules: [
              {required: true, message: '请选择三相物料描述'},
            ]},
          equipRateS: {rules: [
              {required: true, message: '请输入三相频率'},
            ]},
          equipI: {rules: [
              {required: true, message: '请选择三相电流'},
            ]},
          equipScale: {rules: [
              {required: true, message: '请选择变比'},
            ]},
          equipDescribeC: {rules: [
              {required: true, message: '请选择终端物料描述'},
            ]},
          equipRateC: {rules: [
              {required: true, message: '请选择终端载波频率'},
            ]},
          equipSignalC: {rules: [
              {required: true, message: '请选择终端电压/通信方式'},
            ]},
        },
        id:"",
          forms:{
          dateType:"",
          planDate:"",
          equipNumber:"",
          equipRateD:"",
          equipDescribeS:"",
          equipRateS:"",
          equipI:"",
          equipScale:"",
          equipDescribeC:"",
          equipRateC:"",
          equipSignalC:""
        },
        width:800,
        title: '操作',
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        headers: {},
        form: this.$form.createForm(this),
        url: {
          add: '/prod/devProducePlan/add',
          edit:'/prod/devProducePlan/edit',
          checkPlan:"/prod/devProducePlan/checkPlan"
        }
      }
    },
    created() {
      const token = Vue.ls.get(ACCESS_TOKEN)
      this.headers = { 'X-Access-Token': token }
    },
    computed: {

    },
    methods: {
      edit(record){
        //this.form.resetFields();
        this.confirmLoading = false;
        this.visible = true;
        this.produceCycleId = record.produceCycleId;
        this.flag = record.equipCateg;
        this.id = record.id;
        this.model = Object.assign({}, record);
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'dateType','planDate','equipNumber','equipRateD',
          'equipDescribeS','equipRateS','equipI','equipScale','equipDescribeC','equipRateC','equipSignalC'
          ));
        });
      },
      show(id,state){
        this.form.resetFields();
        this.confirmLoading = false;
        this.visible = true;
        this.id = '';
        this.produceCycleId = id;
        this.flag = state;
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleSubmit() {
        const that = this
        this.form.validateFields((err, values) => {
          console.log("22222222222222222222",values);
          if (!err) {
            var values = {
              dateType: values.dateType,
              planDate: values.planDate,
              equipNumber: values.equipNumber,
              equipRateD: values.equipRateD,
              isDel: 0,
              id: this.id,
              equipCateg: this.flag,
              produceCycleId: this.produceCycleId,
              equipDescribeS: values.equipDescribeS,
              equipRateS: values.equipRateS,
              equipI: values.equipI,
              equipScale: values.equipScale,
              equipDescribeC: values.equipDescribeC,
              equipRateC: values.equipRateC,
              equipSignalC: values.equipSignalC
            };
            let formData = Object.assign({}, values);
          if (this.id != ''){
              putAction(this.url.edit, formData).then((res) => {
                if (res.success) {
                  this.$message.success(res.message);
                } else {
                  this.$message.warning(res.message)
                }
              }).finally(() => {
                this.visible = false;
                this.$emit('ok');
                this.$emit('loadDateAdd');
              })
            }else{
            postAction(this.url.checkPlan, formData).then((res) => {
              console.log(res)
              if (res.success) {
                console.log(formData)
                  postAction(this.url.add, formData).then((res) => {
                    if (res.success) {
                      this.$message.success(res.message);
                    } else {
                      this.$message.warning(res.message)
                    }
                  }).finally(() => {
                    this.visible = false;
                    this.$emit('ok');
                    this.$emit('loadDateAdd');
                  })
              } else {
                this.$message.warning(res.message)
              }
            })
          }
          }
        });
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>

<style scoped>
  .avatar-uploader > .ant-upload {
    width: 104px;
    height: 104px;
  }

  .ant-upload-select-picture-card i {
    font-size: 49px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 10px;
    padding-bottom: 10px;
  }

  .drawer-bootom-button {
    position: absolute;
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>