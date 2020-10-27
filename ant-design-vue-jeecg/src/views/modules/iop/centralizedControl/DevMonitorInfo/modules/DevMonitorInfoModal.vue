<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :z-index="1011"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id"
                                     placeholder="请选择检定线" @change="changeLine"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select  v-decorator="[ 'equipType', validatorRules.equipType]"  @change="changEquipType">
                <a-select-option v-for="item in equipTypeLists" :key="item.paramId" :value="item.paramId" >
                  {{ item.paramName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-select  v-decorator="[ 'equipNo', validatorRules.equipNo]"  @change="changEquipName">
                  <a-select-option v-for="item in equipLists" :key="item.paramId" :value="item.equipNo">
                    {{ item.paramName }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备别名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input autocomplete ='off'  v-decorator="[ 'equipLabe', validatorRules.equipLabe]" placeholder="请输入设备别名"></a-input>
              </a-form-item>
            </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="设备IP" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input autocomplete ='off' v-decorator="[ 'equipIp', validatorRules.equipIp]" placeholder="请输入设备IP"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
         <a-row>
           <a-col :span="12">
           <a-form-item label="用户名" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <a-input autocomplete ='off'  v-decorator="[ 'loginName', validatorRules.loginName]" placeholder="请输入用户名"></a-input>
           </a-form-item>
           </a-col>
           <a-col :span="12">
           <a-form-item label="密码" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <a-input autocomplete ='off' v-decorator="[ 'loginPasswd', validatorRules.loginPasswd]" placeholder="请输入密码"></a-input>
           </a-form-item>
           </a-col>
         </a-row>
          <a-row>
            <a-col :span="24">
                <a-form-item label="备注"  :labelCol="{lg: {span: 2}}"
                                           :wrapperCol="{lg: {span: 18} }">
                  <a-textarea v-decorator="['reason', validatorRules.reason]" rows="4" placeholder="请输入备注"/>
                </a-form-item>
            </a-col>
          </a-row>
      </a-form>
    </a-spin>
<!--    <img  :src="getImgData">-->
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import { httpAction ,getAction } from '@/api/manage'
  import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "DevMonitorInfoModal",
    components: { 
      JDictSelectTag,
      JDictSelectTagOra
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        model: {},
        equipLists: [],
        imgByte: '',
        getImgData: '',
        equipTypeLists: [],
        equipTypeId: '',
        equipNo: '',
        isEdit:false,
        labelCol: {
          xs: { span: 12 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 12 },
          sm: { span: 12 },
        },
        confirmLoading: false,
        validatorRules: {
          lineNo: {rules: [  { required: true, message: '请选择检定线!'},
          ]},
          equipType: {rules: [
              { required: true, message: '请设备类别!'},
          ]},
          equipNo: {rules: [
              { required: true, message: '请选择设备!'},
          ]},
          equipLabe: {rules: [
              {required: true, message: '请输入设备别名!',validator:this.validateEquipLabe}
          ]},
          equipIp: {rules: [
          ]},
          loginName: {rules: [
          ]},
          loginPasswd: {rules: [
          ]},
          reason: {rules: [
          ]},
          status: {rules: [
          ]},
        },
        url: {
          add: "/DevMonitorInfo/devMonitorInfo/add",
          edit: "/DevMonitorInfo/devMonitorInfo/edit",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
          queryEquipType: "/alarm/record/mtEquipAlarmInfo/queryEquipType",
          selectEquipByEquipTypeId: "/alarm/record/mtEquipAlarmInfo/selectEquipByEquipTypeId",
          img: "/DevMonitorInfo/devMonitorInfo/qrimage",
        }
      }
    },
    created () {
    },
    // watch(){
    //   this.imgByte=this.getImgData
    // },
    methods: {
      add () {
        // this.edit({});
        this.form.resetFields();
        this.equipTypeLists=[];
        this.equipLists=[];
        this.visible = true;
      },
      inputTextAreaId(val) {
        this.form.setFieldsValue({ areaName: val })
      },
      changeLine(value){
        this.equipTypeLists=[];
        this.form.setFieldsValue({equipType:''})
        this.form.setFieldsValue({equipName:''})
        let equipVaule='';
        let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
          getAction(this.url.queryEquipType,{paramId:res.result[0].value}).then(res=>{
            this.equipTypeLists=res.result;
            console.log('this.equipTypeLists-----',this.equipTypeLists)
          })
        })
      },
      changEquipType(value){
        this.equipLists=[];
        this.equipTypeId=value;
        this.form.setFieldsValue({equipNo:''})
        getAction(this.url.selectEquipByEquipTypeId,{paramId:value}).then(res=>{
          console.log("res.result------",res.result)
          this.equipLists=res.result;
        })
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.isEdit=true;
        this.editequipNo=this.model.equipNo;
        this.equipTypeId=this.model.equipType;
        console.log("----this.model----",this.model)
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'lineNo','equipType','equipName','equipLabe','equipIp','loginName','loginPasswd','reason','status'))
          this.form.setFieldsValue({equipNo:this.model.equipName_dictText})
          this.form.setFieldsValue({equipType:this.model.equipType_dictText})
        })
      },
      close () {
        this.$emit('close');
        this.$emit('searchQuery');
        this.$emit('onClearSelected');
        this.visible = false;
        this.model={};
      },
      handleOk () {
        const that = this;

        // 触发表单验证
        this.form.validateFields((err, values) => {
          console.log("editequipNo",this.editequipNo)
          console.log("equipTypeId",this.equipTypeId)
          let edit={
            equipNo:this.editequipNo,
            equipType:this.equipTypeId
          }
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            let formData;
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
              formData = Object.assign(this.model, values)
            }else{
              httpurl+=this.url.edit;
               method = 'put';
               console.log("va-----",values)
              formData = Object.assign(this.model, values)
              formData = Object.assign(formData,edit );
            }
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
        this.form.setFieldsValue(pick(row,'lineNo','equipType','equipName','equipLabe','equipIp','loginName','loginPasswd','reason','status'))
      },
      changEquipName(v){
        this.editequipNo=v;
      },
      validateEquipLabe(rule,value,callback){
        console.log("value.length----",value.length)
        if(value==null || !value){
          callback('设备别名不能为空');
        }else if(value.length<=4){
          callback();
        }else {
          callback("设备别名不能超过4个汉字!");
        }
      }
    }
  }
</script>