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
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择报警时间" v-decorator="[ 'alarmTime', validatorRules.alarmTime]"  disabled :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id"
                                     placeholder="请选择检定线"  disabled @change="changeLine"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" @select="selectEquipNo"  disabled>
                <a-select-option v-for="item in equipLists" :key="item.equipNo" :value="item.equipId">
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="故障等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]"  disabled>
                <a-radio value="hight">
                  A级
                </a-radio>
                <a-radio value="centre">
                  B级
                </a-radio>
                <a-radio value="low">
                  C级
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="故障类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag v-decorator="['faultLabe', validatorRules.faultLabe]" :trigger-change="true"
                              dictCode="FAULT_LABE" placeholder="请选择故障类型"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24" style="margin-left: -115px;">
          <a-col :span="24">
            <a-form-item label="报警内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="[ 'alarmDesc', validatorRules.alarmDesc]" placeholder="请输入报警类容"  disabled></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="原处理人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag v-decorator="[ 'dispatchToUser', validatorRules.dispatchToUser]"
                              disabled
                              :triggerChange="true"
                              placeholder=""
                              dictCode="sys_user,realname,id" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24" style="margin-left: -115px;">
          <a-col :span="24">
            <a-form-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag v-model="createByNow"
                              :trigger="true"
                              placeholder=""
                              dictCode="sys_user,realname,id" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction ,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import AInput from "ant-design-vue/es/input/Input";
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "MtEquipAlarmInfoModal",
    components: {
      AInput, ATextarea, JDate,JDictSelectTagOra,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"",
        width:800,
        visible: false,
        model: {},
        equipLists: [],
        parent_param_id: '',
        equipNo: '',
        createByNow:'',
        calibrationLine:[],
        faultData: {},
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
          detectTaskNo: {rules: [
            ]},
          lineNo: {rules: [
            ]},
          oldId: {rules: [
            ]},
          equipNo: {rules: [
            ]},
          equipName: {rules: [
            ]},
          alarmTime: {rules: [
            ]},
          alarmLevel: {rules: [
            ]},
          alarmSource: {rules: [
            ]},
          alarmReason: {rules: [
            ]},
          alarmDesc: {rules: [
            ]},
          alarmSugg: {rules: [
            ]},
          alarmStatus: {rules: [
            ]},
          endTime: {rules: [
            ]},
          execMode: {rules: [
            ]},
          execDesc: {rules: [
            ]},
          remark: {rules: [
            ]},
          opFlag: {rules: [
            ]},
          writeTime: {rules: [
            ]},
          createBy: {rules: [
            ]},
          faultLabe: {rules: [
            ]},
        },
        url: {
          add: "/workOrder/ordWorkOrderInfo/transferInfo",
          edit: "/alarm/record/mtEquipAlarmInfo/edit",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
        }
      }
    },
    created () {
    },
    computed: {
      whereDictCode: function() {
        return  "O_EQUIP,name,equip_id";
      },
    },
    methods: {
      initDictConfig() {

      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        // let lineNo="FAULT_TASK_INFO,ID,FAULT_LABE,FAULT_NAME = '"+this.model.alarmReason+"' AND EQUIP_NO = '"+this.model.equipNo+"'";
        // initDictOptionsOra(lineNo).then(res => {
        //   console.log("11111",res.result)
        //   this.faultData = res.result;
        // });
        // this.model.faultLabe = this.faultData.faultLabe;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'alarmTime','lineNo','equipName','alarmDesc','alarmLevel','dispatchToUser'))
        })
      },
      close () {
        this.$emit('close');
        this.$emit('ok');
        this.visible = false;
        this.equipLists=[];
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            httpurl+=this.url.add;
            method = 'post';
            let formData = Object.assign(this.model, values);
            formData.dispatchToUser = this.createByNow;
            console.log("表单提交数据",formData)
            let formDataList = [];
            formDataList.push(formData);
            httpAction(httpurl,formDataList,method).then((res)=>{
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
        this.form.setFieldsValue(pick(row,'detectTaskNo','lineNo','oldId','equipNo','equipName','alarmTime','alarmLevel','alarmSource','alarmReason','alarmDesc','alarmSugg','alarmStatus','endTime','execMode','execDesc','remark','opFlag','writeTime'))
      },

      changeLine(value){
        this.equipLists=[];
        getAction(this.url.queryEquip,{paramId:value}).then(res=>{
          console.log("res",res)
          console.log("result",res.result)
          this.equipLists=res.result;
        })
      },
      selectEquipNo(key){
        console.log("key----",key)
        this.equipNo=key;

      }


    }
  }
</script>