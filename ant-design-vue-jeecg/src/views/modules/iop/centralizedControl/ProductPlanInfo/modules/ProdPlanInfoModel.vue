<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    :width="drawerWidth"
    placement="right"
    :closable="true"
    @close="handleCancel"
    :visible="visible"
    style="">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="生产日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-month-picker
            style="width: 100%"
            placeholder="请选择生产日期"
            v-decorator="['dateTime', validatorRules.dateTime]"/>
        </a-form-item>

        <a-form-item label="单相电能表数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入单相电能表数量" v-decorator="[ 'cycleSumD', validatorRules.cycleSumD]"/>
        </a-form-item>

        <a-form-item label="三相电能表数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入三相电能表数量" v-decorator="[ 'cycleSumS', validatorRules.cycleSumS]"/>
        </a-form-item>

        <a-form-item label="互感器数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入互感器数量" v-decorator="[ 'cycleSumH', validatorRules.cycleSumH]"/>
        </a-form-item>

        <a-form-item label="采集终端数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input placeholder="请输入采集终端数量" v-decorator="[ 'cycleSumC', validatorRules.cycleSumC]"/>
        </a-form-item>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="confirmLoading">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
  import pick from 'lodash.pick'
  import moment from 'moment'
  import Vue from 'vue'
  // 引入搜索部门弹出框的组件
  import JSelectPosition from '@/components/jeecgbiz/JSelectPosition'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import { getAction } from '@/api/manage'
  import { disabledAuthFilter } from '@/utils/authFilter'
  import { duplicateCheck } from '@/api/api'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import {postAction, putAction} from "../../../../../../api/manage";

  export default {
    name: 'ProdPlanInfoModel',
    components: {
      AFormItem,
      JSelectPosition,
      JDictSelectTagOra
    },
    moment,
    data() {
      return {
        id:"",
        modalWidth: 800,
        drawerWidth: 700,
        modaltoggleFlag: true,
        confirmDirty: false,
        disableSubmit: false,
        dateFormat: 'YYYY-MM',
        validatorRules: {
          cycleSumD: {
            rules: [{
              required: true, message: '请输入单相电能表数量'
            }]
          },
          dateTime: {
            rules: [{
              required: true, message: '请选择生产日期'
            }]
          },
          cycleSumS: {
            rules: [{
              required: true, message: '请输入三相电能表数量'
            }]
          },
          cycleSumC: {
            rules: [{
              required: true, message: '请输入采集终端数量'
            }]
          },
          cycleSumH: {
            rules: [{
              required: true, message: '请输入互感器数量'
            }]
          },
        },
        departIdShow: false,
        departIds: [], //负责部门id
        title: '操作',
        visible: false,
        model: {},
        roleList: [],
        selectedRole: [],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        uploadLoading: false,
        confirmLoading: false,
        headers: {},
        form: this.$form.createForm(this),
        picUrl: '',
        url: {
          add: '/prod/devProduceCycle/add',
          edit: '/prod/devProduceCycle/edit',
          get: '/prod/devProduceCycle/queryById'
        },
        identity: '1',
        fileList: []
      }
    },
    created() {
      const token = Vue.ls.get(ACCESS_TOKEN)
      this.headers = { 'X-Access-Token': token }
    },
    computed: {
      uploadAction: function() {
        return this.url.fileUpload
      }
    },
    methods: {
      inputTextAreaId(val) {
        this.form.setFieldsValue({ areaName: val })
      },
      inputTextShiftId(val) {
        this.form.setFieldsValue({ shiftName: val })
      },
      isDisabledAuth(code) {
        return disabledAuthFilter(code)
      },
      //窗口最大化切换
      toggleScreen() {
        if (this.modaltoggleFlag) {
          this.modalWidth = window.innerWidth
        } else {
          this.modalWidth = 800
        }
        this.modaltoggleFlag = !this.modaltoggleFlag
      },
      initialRoleList() {
        queryall().then((res) => {
          if (res.success) {
            this.roleList = res.result
          } else {
            console.log(res.message)
          }
        })
      },
      loadUserRoles(userid) {
        queryUserRole({ userid: userid }).then((res) => {
          if (res.success) {
            this.selectedRole = res.result
          } else {
            console.log(res.message)
          }
        })
      },
      refresh() {
        this.selectedDepartKeys = []
        this.checkedDepartKeys = []
        this.checkedDepartNames = []
        this.checkedDepartNameString = ''
        this.userId = ''
        this.resultDepartOptions = []
        this.departId = []
        this.departIdShow = false
      },
      add() {
        this.picUrl = ''
        this.refresh()
        this.edit({ activitiSync: '1' })
      },
      show(id){
        this.form.resetFields();
        this.confirmLoading = false;
        this.visible = true;
        if (id != ''){
          this.id = id;
          getAction(this.url.get,{id:id}).then((res)=>{
            if(res.success){
              var str = res.result.cycleYear + "-" + res.result.cycleMonth;
              this.model = Object.assign({dateTime : Date.parse(str)}, res.result);
              console.log(this.model)
              this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model,'dateTime','cycleSumC','cycleSumD','cycleSumH','cycleSumS'))
                this.form.setFieldsValue({dateTime: this.model.dateTime ? moment(this.model.dateTime) : null})
              })
            }
          });
        }
      },
/*      edit(record) {
        this.resetScreenSize() // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
        let that = this
        that.initialRoleList()
        that.checkedDepartNameString = ''
        that.form.resetFields()
        if (record.hasOwnProperty('id')) {
          that.loadUserRoles(record.id)
          setTimeout(() => {
            this.fileList = record.avatar
          }, 5)
        }
        that.userId = record.id
        that.visible = true
        that.model = Object.assign({}, record)
        that.$nextTick(() => {
          that.form.setFieldsValue(pick(this.model, 'username', 'sex', 'realname', 'email', 'phone', 'activitiSync',
            'workNo', 'telephone', 'post', 'shiftId', 'areaId', 'areaName', 'shiftName'))
        })
        //身份为上级显示负责部门，否则不显示
        if (this.model.identity == '2') {
          this.identity = '2'
          this.departIdShow = true
        } else {
          this.identity = '1'
          this.departIdShow = false
        }
        // 调用查询用户对应的部门信息的方法
        that.checkedDepartKeys = []
        that.loadCheckedDeparts()
      },*/
      //
      loadCheckedDeparts() {
        let that = this
        if (!that.userId) {
          return
        }
        getAction(that.url.userWithDepart, { userId: that.userId }).then((res) => {
          that.checkedDepartNames = []
          if (res.success) {
            var depart = []
            var departId = []
            for (let i = 0; i < res.result.length; i++) {
              that.checkedDepartNames.push(res.result[i].title)
              this.checkedDepartNameString = this.checkedDepartNames.join(',')
              that.checkedDepartKeys.push(res.result[i].key)
              //新增负责部门选择下拉框
              depart.push({
                key: res.result[i].key,
                title: res.result[i].title
              })
              departId.push(res.result[i].key)
            }
            that.resultDepartOptions = depart
            //判断部门id是否存在，不存在择直接默认当前所在部门
            if (this.model.departIds) {
              this.departIds = this.model.departIds.split(',')
            } else {
              this.departIds = departId
            }
            that.userDepartModel.departIdList = that.checkedDepartKeys
          } else {
            console.log(res.message)
          }
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
        this.disableSubmit = false
        this.selectedRole = []
        this.userDepartModel = { userId: '', departIdList: [] }
        this.checkedDepartNames = []
        this.checkedDepartNameString = ''
        this.checkedDepartKeys = []
        this.selectedDepartKeys = []
        this.resultDepartOptions = []
        this.departIds = []
        this.departIdShow = false
        this.identity = '1'
        this.fileList = []
      },
      handleSubmit() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log(values);
            that.confirmLoading = true
            if (!values.dateTime) {
              values.dateTime = ''
            } else {
              values.dateTime = values.dateTime.format(this.dateFormat)
            }
            var year = values.dateTime.split('-')[0];
            var month = values.dateTime.split('-')[1];
            let obj = {
              cycleYear : year,
              cycleMonth : month,
              isDel : '0'
            };
            let formData = Object.assign(obj, values);
            console.log(formData)
             if (this.id != ''){
               this.$set(formData,'id',this.id)
               console.log('---------------' + formData)
               putAction(this.url.edit,formData).then((res) => {
                 if (res.success) {
                   this.$message.success(res.message);
                 } else {
                   this.$message.warning(res.message)
                 }
               }).finally(() => {
                 this.visible = false;
                 this.$emit('ok');
               })
             }else{
               postAction(this.url.add,formData).then((res) => {
                 if (res.success) {
                   this.$message.success(res.message);
                 } else {
                   this.$message.warning(res.message)
                 }
               }).finally(() => {
                 this.visible = false;
                 this.$emit('ok');
               })
             }
          }
        })
        this.id = ''
      },
      handleCancel() {
        this.id = ''
        this.close()
      },
      validateUsername(rule, value, callback) {
        var params = {
          tableName: 'sys_user',
          fieldName: 'username',
          fieldVal: value,
          dataId: this.userId
        }
        duplicateCheck(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback('用户名已存在!')
          }
        })
      },

      // 获取用户对应部门弹出框提交给返回的数据
      modalFormOk(formData) {
        this.checkedDepartNames = []
        this.selectedDepartKeys = []
        this.checkedDepartNameString = ''
        this.userId = formData.userId
        this.userDepartModel.userId = formData.userId
        this.departIds = []
        this.resultDepartOptions = []
        var depart = []
        for (let i = 0; i < formData.departIdList.length; i++) {
          this.selectedDepartKeys.push(formData.departIdList[i].key)
          this.checkedDepartNames.push(formData.departIdList[i].title)
          this.checkedDepartNameString = this.checkedDepartNames.join(',')
          //新增部门选择，如果上面部门选择后不为空直接付给负责部门
          depart.push({
            key: formData.departIdList[i].key,
            title: formData.departIdList[i].title
          })
          this.departIds.push(formData.departIdList[i].key)
        }
        this.resultDepartOptions = depart
        this.userDepartModel.departIdList = this.selectedDepartKeys
        this.checkedDepartKeys = this.selectedDepartKeys  //更新当前的选择keys
      },
      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize() {
        let screenWidth = document.body.clientWidth
        if (screenWidth < 500) {
          this.drawerWidth = screenWidth
        } else {
          this.drawerWidth = 700
        }
      },
      identityChange(e) {
        if (e.target.value === '1') {
          this.departIdShow = false
        } else {
          this.departIdShow = true
        }
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