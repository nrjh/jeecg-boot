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
      <a-form :form="form" style="border: 1px solid #e8e8e8;">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-time disabled placeholder="请选择报警时间" v-model="alarmTime" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
           <a-col :span="12">
              <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id,new_flag = '1'"
                                       placeholder="请选择检定线" @change="changeLine"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" @select="selectEquipNo" >
                <a-select-option v-for="item in equipLists" :key="item.value" :value="item.title">
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
              <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]">
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
              <JDictSelectTag v-decorator="['faultLabe', validatorRules.faultLabe]" :trigger-change="true"
                              dictCode="FAULT_LABE" placeholder="请选择故障类型"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24" style="margin-left: -10px;">
          <a-col :span="12">
            <a-form-item label="报警名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--              <a-textarea v-decorator="[ 'alarmReason', validatorRules.alarmReason]" placeholder="请输入报警类容"></a-textarea>-->
              <a-select  v-decorator="[ 'alarmReason', validatorRules.alarmReason]" @select="selectAlarmReason">
                <a-select-option v-for="item in alarmReasonList" :key="item.id" :value="item.faultName">
                  {{ item.faultName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="系统名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--              <a-textarea v-decorator="[ 'alarmReason', validatorRules.alarmReason]" placeholder="请输入报警类容"></a-textarea>-->
              <a-select  v-decorator="[ 'systemName', validatorRules.systemName]" >
                <a-select-option v-for="item in systemNameList" :key="item.id" :value="item.value">
                  {{ item.value }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24" >
          <a-col :span="12">
            <a-form-item label="处理班组" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <j-dict-select-tag-ora v-decorator="['createByGroup', validatorRules.createByGroup]"
                              :triggerChange="true"
                              placeholder=""
                              dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" />
<!--              <a-input v-model="createByGroup" placeholder="请输入处理班组" :trigger-change="true" disabled="disabled"></a-input>-->
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag v-decorator="['createBy', validatorRules.createBy]"
                              :triggerChange="true"
                              placeholder=""
                              dictCode="sys_user,realname,id" />
<!--              <a-input v-model="createBy" placeholder="请输入处理人" disabled="disabled"></a-input>-->
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
  import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'
  import moment from 'moment'
  import JTime from "@/components/jeecg/JTime";

  export default {
    name: "MtEquipAlarmInfoModal",
    components: {
      AInput, ATextarea, JDate,JDictSelectTagOra,JTime
    },
    data () {
      return {
        createBy:"",
        createByGroup:"",
        form: this.$form.createForm(this),
        title:"",
        width:1200,
        visible: false,
        model: {},
        alarmTime: '',
        equipLists: [],
        alarmReasonList: [],
        systemNameList:[],
        parent_param_id: '',
        areaNo:'',
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
          systemName:{rules: [{ required: true, message: '请选择系统名称' }]},
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
          user:"/alarm/record/mtEquipAlarmInfo/user",
          list: '/alarm/bind/faultPrdProductInfo/queryList',
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
      getUser(){
        getAction(this.url.user, {}).then((res) => {
          if (res.success && res.result != null) {
             console.log(res.result.realname);
             this.createBy = res.result.realname
             this.createByGroup = res.result.realname;
          }
        })
      },
      initDictConfig() {

      },
      add () {
        this.title='新增'
        this.visible = true;
        //调用获取user的方法
        this.getUser();
        this.alarmTime = moment().format('YYYY-MM-DD HH:mm:ss');
      },
      edit (record) {
        this.title='修改',
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
          this.form.setFieldsValue({createBy:editTemp.createBy})
          this.form.setFieldsValue({createByGroup:editTemp.createByGroup})
          this.form.setFieldsValue({systemName:editTemp.systemName})
        })
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'alarmTime','lineNo','equipNo','equipName','alarmReason','alarmLevel','alarmSource','createBy','faultLabe'))
        })
        this.alarmTime=this.model.alarmTime;
      },
      close () {
        this.form.resetFields();
        this.$emit('close');
        this.$emit('onClearSelected');
        this.$emit('ok');
        this.visible = false;
        this.alarmTime = '';
        this.equipLists=[];
        this.areaNo='';
        this.model={};
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
              that.model.alarmTime=this.alarmTime;
              that.model.equipNo=this.equipNo;
              that.model.createBy = this.createBy;
              that.model.createByGroup = this.createByGroup
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
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

      changeLine(value){
        this.equipLists=[];
        this.areaNo=value;
        let str="O_EQUIP,name,EQUIP_NO,area_id='"+value+"'";
        console.log("str",str)
        initDictOptionsOra(str).then(res => {
            console.log("res.result",res.result)
            this.equipLists=res.result;
        })
      },
      selectAlarmReason(value,key){
          console.log("selectAlarmReason:value报警名称----",value)
          console.log("selectAlarmReason:key----",key.data.key)
          let str="FAULT_TASK_INFO,ID,SYSTEM_NAME,FAULT_NAME='"+value+"'";
          initDictOptionsOra(str).then(res => {
              if(res.success){
                  this.systemNameList = res.result
                  // console.log(res.result)
              }
              // let temp = {}
              // temp.lineNo = this.areaNo
              // temp.productId = res.result[0].value
              // getAction(this.url.list, temp).then((resRe) => {
              //     console.log("resRe----",resRe.result)
              //     if (resRe.success) {
              //         this.alarmReasonList = resRe.result
              //     }
              // })
          })
      },
      selectEquipNo(value,key){
          console.log("value----",value)
          console.log("key----",key.data.key)
          this.equipNo=key.data.key;
          this.onSelect(key.data.key)
      },
      onSelect(id) {
        let str="O_EQUIP,name,equip_id,EQUIP_NO='"+id+"'";
        console.log("str",str)
        initDictOptionsOra(str).then(res => {
          let temp = {}
          temp.lineNo = this.areaNo
          temp.productId = res.result[0].value
          getAction(this.url.list, temp).then((resRe) => {
            console.log("resRe----",resRe.result)
            if (resRe.success) {
              this.alarmReasonList = resRe.result
            }
          })
        })
      }


    }
  }
</script>