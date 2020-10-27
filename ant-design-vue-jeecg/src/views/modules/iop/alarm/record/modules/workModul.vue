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
            <a-form-item label="报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <j-date placeholder="请选择报警时间" v-decorator="[ 'alarmTime', validatorRules.alarmTime]" disabled="disabled" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id"
                                     disabled="disabled"
                                     placeholder="请选择检定线" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" disabled="disabled">
                <a-select-option v-for="item in equipLists" :key="item.equipNo" :value="item.equipId">
                  {{ item.title }}
                </a-select-option>
                <!--<j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'equipName', validatorRules.equipName]"  dictCode="whereDictCode"-->
              </a-select>
              <!--<a-input v-model="equipNo" v-decorator="[ 'equipNo', validatorRules.equipNo]" hidden></a-input>-->
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="故障等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<a-input v-decorator="[ 'alarmLevel', validatorRules.alarmLevel]" placeholder="请输入报警级别"></a-input>-->
              <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]" disabled="disabled">
                <a-radio value="A">
                  A级
                </a-radio>
                <a-radio value="B">
                  B级
                </a-radio>
                <a-radio value="C">
                  C级
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="故障类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<a-input v-decorator="[ 'alarmSource', validatorRules.alarmSource]" placeholder="请输入报警来源"></a-input>-->
              <JDictSelectTag v-decorator="['faultLabe', validatorRules.faultLabe]" :trigger-change="true" disabled="disabled"
                              dictCode="FAULT_LABE" placeholder="请选择故障类型"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24" style="margin-left: -10px;">
          <a-col :span="12">
            <a-form-item label="报警内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'alarmReason', validatorRules.alarmReason]" placeholder="请输入报警描述" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24" >
          <a-col :span="12">
            <a-form-item label="处理班组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-ora v-decorator="['createByGroup', validatorRules.createByGroup]"
                                            :triggerChange="true"
                                            placeholder=""
                                            disabled="disabled"
                                            dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" />
<!--              <a-input v-model="createByGroup" placeholder="请输入处理班组" :trigger-change="true" disabled="disabled"></a-input>-->
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <JDictSelectTag v-decorator="['createBy', validatorRules.createBy]"
                                            :triggerChange="true"
                                            placeholder=""
                                            disabled="disabled"
                                            dictCode="sys_user,realname,id" />
<!--              <a-input v-model="createBy" v-decorator="['createBy', validatorRules.createBy]" placeholder="请输入处理人" disabled="disabled"></a-input>-->
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
  import { initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "workModul",
    components: {
      AInput, ATextarea, JDate,JDictSelectTagOra
    },
    data () {
      return {
        provinceCode:"",
        form: this.$form.createForm(this),
        title:"",
        width:1200,
        visible: false,
        model: {},
        createByGroup:'',
        createBy:'',
        equipLists: [],
        parent_param_id: '',
        equipNo: '',
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
          createByGroup: {rules: [
          ]},
        },
        url: {
          add: "/alarm/record/mtEquipAlarmInfo/add",
          edit: "/alarm/record/mtEquipAlarmInfo/edit",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
          productWorkOrder: "/alarm/record/mtEquipAlarmInfo/productWorkOrder",
          editlist: '/alarm/record/mtEquipAlarmInfo/queryById',
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
      add () {
        this.edit({});
      },
      edit (record) {
        initDictOptions("province_code").then(res => {
          console.log("----",res.result[0].value)
          this.provinceCode = '';
          this.provinceCode = res.result[0].value
        })
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true
        let temp = {}
        temp.id=record.id;
        getAction(this.url.editlist, temp).then((resRe) => {
          console.log("resRe----",resRe.result)
          let editTemp = resRe.result
          console.log("this.model",this.model)
          this.form.setFieldsValue({faultLabe:editTemp.faultLabe})
          this.form.setFieldsValue({alarmLevel:editTemp.alarmLevel})
          this.form.setFieldsValue({alarmReason:editTemp.alarmReason})
          this.createBy = editTemp.createBy
          this.form.setFieldsValue({createBy:editTemp.createBy})
          this.createByGroup = editTemp.createByGroup
          this.form.setFieldsValue({createByGroup:editTemp.createByGroup})
        })
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'alarmTime','lineNo','equipNo','equipName','alarmReason','alarmLevel','alarmSource','createBy','faultLabe','createByGroup'))
        })
        this.alarmTime=this.model.alarmTime;
      },
      close () {
        this.$emit('close');
        this.$emit('onClearSelected');
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
            httpurl+=this.url.productWorkOrder;
            method = 'put';
            this.$set(values, "provinceCode", this.provinceCode)
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
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'detectTaskNo','lineNo','oldId','equipNo','equipName','alarmTime','alarmLevel','alarmSource','alarmReason','alarmDesc','alarmSugg','alarmStatus','endTime','execMode','execDesc','remark','opFlag','writeTime'))
      },


    }
  }
</script>