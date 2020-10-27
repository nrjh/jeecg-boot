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
      <a-form :form="form">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="检定线" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id,new_flag = '1'"
                                     placeholder="请选择检定线" @change="changeLine"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-select  v-decorator="[ 'equipName', validatorRules.equipName]"  @change="changEquipNo" placeholder="请选择">
                <a-select-option v-for="item in equipLists" :key="item.equipNo" :value="item.equipId">
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="故障等级" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]">
                <a-radio value="A">
                  A
                </a-radio>
                <a-radio value="B">
                  B
                </a-radio>
                <a-radio value="C">
                  C
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="工单类型：" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-select
                v-decorator="['orderType', validatorRules.orderType]"
              >
                <a-select-option key="1" value="01" >
                  故障工单
                </a-select-option>
                <a-select-option key="2" value="09">
                  督办工单
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="故障名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'alarmReasonId', validatorRules.alarmReasonId]" :dictCode="whereDictCode"
                                     placeholder="" @change="changeFaultName" />
              <a-input v-model="alarmReason" hidden="hidden"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="要求完成时间" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-date-picker show-time placeholder="请选择要求完成时间" v-decorator="[ 'pressTime', validatorRules.pressTime]" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="工单内容" :labelCol="{span:3}" :wrapperCol="{span:20}">
              <a-textarea :rows="6" placeholder="..." v-decorator="['alarmDesc', validatorRules.alarmDesc]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="目标及要求" :labelCol="{span:3}" :wrapperCol="{span:20}">
              <a-textarea :rows="6" placeholder="..." v-decorator="['alarmSugg', validatorRules.alarmSugg]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
<!--            <a-form-item label="接收人："  :labelCol="{span:3}" :wrapperCol="{span:14}">
              <j-dict-select-tag type="list"
                                 placeholder="请选择接收人"
                                 v-decorator="['dispatchToUser', validatorRules.dispatchToUser]"
                                 :trigger-change="true"
                                 dict-code="sys_user,realname,id"/>
            </a-form-item>-->
            <a-form-item label="处理人："  :labelCol="{span:3}" :wrapperCol="{span:14}">
              <j-dict-select-tag type="list"
                                 placeholder="请选择处理人"
                                 v-decorator="['processUser', validatorRules.processUser]"
                                 :trigger-change="true"
                                 dict-code="sys_user,realname,id"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="处理班组："  :labelCol="{span:3}" :wrapperCol="{span:14}">
              <j-dict-select-tag-ora type="list"
                                 placeholder="请选择处理班组"
                                 v-decorator="['dispatchTeam', validatorRules.dispatchTeam]"
                                 :trigger-change="true"
                                 dict-code="O_SHIFT,SHIFT_NAME,SHIFT_ID"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="附件：" :labelCol="{span:3}" :wrapperCol="{span:14}">
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
      <a-button key="submit" type="primary" @click="handleOk('01')">
        提交
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
  import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'

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
        title:"操作",
        width:800,
        visible: false,
        model: {},
        equipLists: [],
        equipNo:'',
        workData: {},
        uploadMessage: '',
        alarmReason: '',
        confirmLoading: false,
        provinceCode: '',
        validatorRules: {
          orderType: {rules: [
          ]},
          lineNo: {rules: [{required:true,message:"请选择检定线"}]},
          equipName: {rules: [{required:true,message:"请选择设备"}]},
          alarmLevel: {rules: [
          ]},
          alarmReason: {rules: [{required:true,message:"请选择故障名称"}]},
          alarmReasonId:{rules: [{required:true,message:"请选择故障名称"}]},
          alarmDesc: {rules: [
          ]},
          alarmSugg: {rules: [
          ]},
          pressTime: {rules: [
              {required:true,message:"请选择完成时间"}
          ]},
          pressLabe: {rules: [
          ]},
          processUser:{rules: [
          ]},
          dispatchTeam:{rules: [
            ]},
        },
        url: {
          add: "/workOrder/ordWorkOrderInfo/add",
          edit: "/workOrder/ordWorkOrderInfo/edit",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
          minioUpload: "/sys/upload/uploadMinio",
          upload: "/oss/file/upload",
          queryEquipNo:"/workOrder/ordWorkOrderInfo/queryEquipNo"

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
      whereDictCode: function() {
        console.log("equipNo",this.equipNo)
        return "MIDHD.MT_EQUIP_ALARM_INFO,ALARM_REASON,ID,EQUIP_NO='"+this.equipNo+"' and ALARM_REASON is not null ";
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
        this.equipLists=[];
        this.uploadMessage = ''
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
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
              this.workData.processStatus = state
              this.workData.alarmReason = this.alarmReason
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            values.pressTime = this.timeFormate( values.pressTime);
            console.log(values);
            this.workData.provinceCode = this.provinceCode
            let formData = Object.assign(this.workData, values);
            console.log("表数据22222222",formData)
            // debugger
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
      //显示当前时间（年月日时分秒）
      timeFormate(timeStamp) {
        let year = new Date(timeStamp).getFullYear();
        let month =new Date(timeStamp).getMonth() + 1 < 10? "0" + (new Date(timeStamp).getMonth() + 1): new Date(timeStamp).getMonth() + 1;
        let date =new Date(timeStamp).getDate() < 10? "0" + new Date(timeStamp).getDate(): new Date(timeStamp).getDate();
        let hh =new Date(timeStamp).getHours() < 10? "0" + new Date(timeStamp).getHours(): new Date(timeStamp).getHours();
        let mm =new Date(timeStamp).getMinutes() < 10? "0" + new Date(timeStamp).getMinutes(): new Date(timeStamp).getMinutes();
        let ss =new Date(timeStamp).getSeconds() < 10? "0" + new Date(timeStamp).getSeconds(): new Date(timeStamp).getSeconds();
        return  year + "-" + month + "-" + date +" "+hh+":"+mm+':'+ss;
      },
      changeLine(value){
        this.equipLists=[];
        this.form.setFieldsValue({equipName:'请选择'})
        let equipVaule='';
        let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
            equipVaule=res.result[0].value;
            if(equipVaule != null){
                getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
                    this.equipLists=res.result;
                })
            }
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
            // this.workData={
            //   workName:workName,
            //   sop:sop
            // };
            // this.$emit('returnWork',returnData)

            this.$message.success(`${info.file.name} 上传成功!`);
          } else {
            this.$message.error(`${info.file.response.message}`);
          }
        } else if (info.file.status === 'error') {
          this.$message.error(`${info.file.response.message}`);
        }
      },
      changEquipNo(value){
        this.form.setFieldsValue({alarmReasonId:null})
        getAction(this.url.queryEquipNo,{id:value}).then(res=>{
          console.log("res1111111111",res)
          console.log("result2222222222",res.result)

          this.equipNo=res.message;
        })
      },
      changeFaultName(value){
        let str="MIDHD.MT_EQUIP_ALARM_INFO,ALARM_REASON,ID,ID='"+value+"'";
        initDictOptionsOra(str).then(res => {
          console.log("res.result----", res.result)
          this.alarmReason = res.result[0].text
        });
      },
    }
  }
</script>