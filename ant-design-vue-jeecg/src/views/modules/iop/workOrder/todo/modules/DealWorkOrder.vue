<template>
  <div>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="border: 1px solid #e8e8e8;">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="工单编号：" :labelCol="{span:6}" :wrapperCol="{span:15}" disabled="disabled">
              <a-input placeholder=""  v-decorator="['id', validatorRules.id]" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="工单名称：" :labelCol="{span:6}" :wrapperCol="{span:15}" >
              <a-input placeholder=""  v-decorator="['alarmReason', validatorRules.alarmReason]" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :span="24">
          <a-col :span="8">
            <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}" >
              <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"   dictCode="o_area,area_name,area_id"
                                     placeholder="请选择检定线" @change="changeLine" disabled="disabled"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="设备名称" :labelCol="{span:9}" :wrapperCol="{span:15}" >
              <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" disabled="disabled">
                <a-select-option v-for="item in equipLists" :key="item.equipNo" :value="item.equipId" >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="故障等级" :labelCol="{span:9}" :wrapperCol="{span:15}" >
              <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]" disabled="disabled">
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
        </a-row>

        <a-row :gutter="16">
          <a-col>
            <a-form-item label="工单内容" :labelCol="{span:3}" :wrapperCol="{span:15}" >
              <a-textarea :rows="3" placeholder="..." v-decorator="['alarmDesc', validatorRules.alarmDesc]" disabled="disabled"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col>
            <a-form-item label="附件：" :labelCol="{span:3}" :wrapperCol="{span:15}" >
              <a-input placeholder="" v-model="additional " disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <a-form style="border: 1px solid #e8e8e8;" >
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="处理结果：" :labelCol="{span:6}" :wrapperCol="{span:18}" >
              <a-radio-group v-decorator="['result', validatorRules.result]" >
                <a-radio value="1">
                  处理
                </a-radio>
                <a-radio value="0">
                  无需处理
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="故障原因" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-select  v-decorator="[ 'errorReasonName', validatorRules.errorReasonName]" @change="conduction">
                <a-select-option v-for="item in equipOfErrorReasonNameAndIdList" :key="item.id" :value="item.reasonName">
                  {{ item.reasonName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
<!--        <a-row :gutter="16">-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="故障原因" :labelCol="{span:6}" :wrapperCol="{span:18}">-->
<!--              <a-select  v-decorator="[ 'errorReasonName', validatorRules.errorReasonName]" @change="conduction">-->
<!--                <a-select-option v-for="item in equipOfErrorReasonNameAndIdList" :key="item.id" :value="item.reasonName">-->
<!--                  {{ item.reasonName }}-->
<!--                </a-select-option>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="修复方法" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-select  v-decorator="[ 'repairName', validatorRules.repairName]" @change="conduction">
                <a-select-option v-for="item in faultRepairInfoList" :key="item.isReplace" :value="item.repairName">
                  {{ item.repairName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12"  v-if="isReplaces == 'Y'">
            <a-button @click="selectProduct" type="primary" icon="plus">
              选择物料
            </a-button>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="领料编号：" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-input placeholder=""  v-decorator="['orderOutId', validatorRules.orderOutId]" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="备注" :labelCol="{span:3}" :wrapperCol="{span:15}">
              <a-textarea :rows="3" placeholder="..." v-decorator="['reason', validatorRules.reason]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col>
            <a-form-item label="附件：" :labelCol="{span:3}" :wrapperCol="{span:15}">
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
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="处理人："  :labelCol="{span:6}" :wrapperCol="{span:18}">
              <j-dict-select-tag type="list"
                                 placeholder="请选择处理人"
                                 v-decorator="['createId', validatorRules.createId]"
                                 :trigger-change="true"
                                 dict-code="sys_user,realname,id"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="处理时间" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <j-date placeholder="处理时间" v-decorator="[ 'createTime', validatorRules.createTime]"
                      :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
  <GdStockOutOrderModal ref="modalForm" @ok="getOrderOutId"></GdStockOutOrderModal>
  </div>
</template>

<script>

    import pick from 'lodash.pick'
    import JDate from '@/components/jeecg/JDate'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'
    import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
    import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
    import ATextarea from 'ant-design-vue/es/input/TextArea'
    import { getAction, httpAction } from '@/api/manage'
    import Vue from 'vue'
    import { ACCESS_TOKEN } from '@/store/mutation-types'
    import GdStockOutOrderModal from './GdStockOutOrderModal'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'

    export default {
    name: "DealWorkOrder",
    mixins:[JeecgListMixin],
    components: {
      JDate,
      JDictSelectTag,
      JDictSelectTagOra,
      JDictSelectTagIop,
      ATextarea,
      GdStockOutOrderModal
    },
    data () {
      return {
        form: this.$form.createForm(this),
        //token header
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        title:"处理",
        width:1200,
        visible: false,
        model: {},
        equipLists: [],
        faultRepairInfoList: [],
        workData: {},
        additional: '',
        uploadMessage: '',
        confirmLoading: false,
        provinceCode: '',
        isReplaces: '',
        stockOutId: {},
        validatorRules: {
          orderType: {rules: [
          ]},
          lineNo: {rules: [
          ]},
          equipName: {rules: [
          ]},
          id: {rules: [
          ]},
          errorReasonName:{rules: [
          ]},
        },
        url: {
          list: "/workOrder/ordWorkOrderInfo/list",
          add: "/ordProcessInfo/ordProcessInfo/add",
          edit: "/workOrder/ordWorkOrderInfo/edit",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
          minioUpload: "/sys/upload/uploadMinio",
          upload: "/oss/file/upload",
          queryEnclosureInfo:"/ordEnclosure/ordEnclosureInfo/queryEnclosureInfo",
          queryRepairInfo:"/ordProcessInfo/ordProcessInfo/queryRepairInfo",
          errorReasonNameAndId:"/alarmType/faultTaskInfo/errorReasonNameAndId",
        },
        equipOfErrorReasonNameAndIdList:[],
        equipOfErrorReasonId:"",
      }
    },
    created () {
    },
    computed: {
      // uploadAction() {
      //   return window._CONFIG['domianURL'] + this.url.upload;
      // },
      minioUploadAction() {
        return window._CONFIG['domianURL'] + this.url.minioUpload;
      },
    },
    methods: {
      getOrderOutId(outId){
        this.stockOutId.orderOutId = outId
        console.log("获取id1111111",outId)
        this.form.setFieldsValue(pick(this.stockOutId,'orderOutId'))
        /*更新工单表插入工单领料出库单号*/
        var paramData = {};
        paramData.id = this.model.id
        paramData.orderOutId = outId
        httpAction(this.url.edit, paramData,"put").then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          }else{
            this.$message.warning(res.message);
          }
        });
      },
      conduction(value,option){
        this.isReplaces = ''
        this.isReplaces = option.data.key;
        this.equipOfErrorReasonId = option.data.key
      },
      add () {
        this.edit({});
      },
      edit (record) {
        console.log("record----",record)
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'id','lineNo','equipName','alarmLevel','alarmReason','alarmDesc','orderOutId'))
        })
        let paramData = {};
        paramData.faultName = this.model.alarmReason
        paramData.equipNo = this.model.equipNo
        console.log("this.faultRepairInfoList----",paramData)
        let param = Object.assign({}, paramData);
        // 修复方法数据
        httpAction(this.url.queryRepairInfo, param,"post").then((res) => {
          if (res.success) {
            this.faultRepairInfoList = res.result;
          }else{
            this.$message.warning(res.message);
          }
        });
        // 查询附件信息
        getAction(this.url.queryEnclosureInfo, {id: this.model.id}).then((res) => {
          if (res.success) {
            this.additional = res.result.enclosureName;
          }else{
            this.$message.warning(res.message);
          }
        });
        // 获取故障原因
        getAction(this.url.errorReasonNameAndId,{lineNo:this.model.lineNo,equipNo:this.model.equipNo}).then((res) => {
            if (res.success) {
                this.equipOfErrorReasonNameAndIdList = res.result
            }
        })
      },
      close () {
        this.$emit('close');
        this.$emit('onClearSelected');
        this.$emit('ok');
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
            httpurl+=this.url.add;
            method = 'post';
            this.workData.workOrderId = this.model.id
            this.workData.errorReasonId=this.equipOfErrorReasonId
            console.log("故障名称")
            console.log(this.model.errorReasonName)
            let formData = Object.assign({},this.workData, values);
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
      changeLine(value){
        this.equipLists=[];
        getAction(this.url.queryEquip,{paramId:value}).then(res=>{
          console.log("res",res)
          console.log("result",res.result)
          this.equipLists=res.result;
        })
        console.log("---equipTypeLists",this.equipLists)
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
            // let  workName=info.file.name;
            // let  sop=info.file.response.message;
            this.workData.workName = info.file.name;
            this.workData.sop = info.file.response.message;
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

      selectProduct(){
        this.form.validateFields((err, values) => {
          if (!err) {
            this.$refs.modalForm.add(values);
            this.$refs.modalForm.title = "新增";
            this.$refs.modalForm.disableSubmit = false;
          }
        })
      },
    }
  }
</script>