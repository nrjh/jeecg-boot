<template>
  <a-card :bordered="true">
    <a-row :gutter="24">
      <a-form :form="form"  v-show="isShowForm">
        <a-col :span="24">

          <a-row :gutter="24">
            <a-col :span="18">
              <a-form-item label="设备名称" :labelCol="{span: 3}" :wrapperCol="{span: 18}">
                <a-input placeholder="请输入设备名称" v-model="dataList.equipName" disabled="disabled"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">
            <a-col :span="18">
              <a-form-item label="时间范围" :labelCol="{span:3}" :wrapperCol="{span: 18}">
<!--                <a-input placeholder="请输入时间范围" v-model="dataList.timeRange" :disabled="isDisabled" />-->
                <j-dict-select-tag type="list" v-model="dataList.timeRange"
                                   :disabled="isDisabled"
                                   :trigger="true" dictCode="AUTOMATION_CHECK_TIME_RANGE" placeholder="请选择时间范围"
                />
              </a-form-item>

            </a-col>
          </a-row>

          <a-row :gutter="24">
            <a-col>
              <a-col :span="6">
                <a-form-item label="故障数量" :labelCol="{span: 8}" :wrapperCol="{span: 16}">
                  <a-input placeholder="请输入故障数量" v-model="dataList.failureCountA" :disabled="isDisabled"/>
                </a-form-item>
              </a-col>
              <a-col :span="3">
                <div style="padding: 5px;font-size: 18px;">< A类或 </div>
              </a-col>
            </a-col>

            <a-col>
              <a-col :span="4">
                <a-form-item :wrapperCol="{span: 24}">
                  <a-input placeholder="请输入故障数量" v-model="dataList.failureCountB" :disabled="isDisabled"/>
                </a-form-item>
              </a-col>
              <a-col :span="3">
                <div style="padding: 5px;font-size: 18px;">< B类或 </div>
              </a-col>
            </a-col>

            <a-col>
              <a-col :span="4">
                <a-form-item :wrapperCol="{span: 24}">
                  <a-input placeholder="请输入故障数量" v-model="dataList.failureCountC" :disabled="isDisabled"/>
                </a-form-item>
              </a-col>
              <a-col :span="3">
                <div style="padding: 5px;font-size: 18px;">< C类或 </div>
              </a-col>
            </a-col>

          </a-row>

          <a-row :gutter="24">
            <a-col :span="18">
              <a-form-item label="巡查频率" :labelCol="{span:3}" :wrapperCol="{span: 18}">
<!--                <a-input placeholder="请输入巡查频率" v-model="dataList.patrolFrequency" :disabled="isDisabled"/>-->
                <j-dict-select-tag type="list" v-model="dataList.patrolFrequency"
                                   :disabled="isDisabled"
                                   :trigger="true" dictCode="AUTOMATION_CHECK_RATE" placeholder="请选择巡查频率"
                />
              </a-form-item>

            </a-col>
          </a-row>

          <a-row :gutter="24">
            <a-col :span="18">
              <a-form-item label="状态" :labelCol="{span:3}" :wrapperCol="{span:9}">
                <a-radio-group v-model="dataList.state"  :disabled="isDisabled">
                  <a-radio :value="1">
                    启用
                  </a-radio>
                  <a-radio :value="0">
                    禁用
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>

          <div style="margin-top: 10px;margin-left: 25%;" v-show="!isDisabled">
            <a-button type="primary" @click="handleOkSave()">保存</a-button>
            <a-button style="margin-left: 8px" type="default" @click="cancel()">取消</a-button>
          </div>
      </a-col>
      </a-form>
    </a-row>
  </a-card>
</template>

<script>
  import { getAction } from '@/api/manage'
  import { initDictOptionsOra, filterDictText } from '@/components/dict/JDictSelectUtil'
  import AreaEquipTree from './AreaEquipTree'
  import pick from 'lodash.pick'
  import moment from 'moment'
  import { httpAction } from '../../../../../../api/manage'
  export default {
    name: 'LookFailuresCount',
    components: {
      AreaEquipTree
    },
    created() {
    },
    data() {
      return {
          isDisabled:true,
          form: this.$form.createForm(this),
          activeKey: 'partsInfoTable',
          refKeys: ['partsInfoTable', 'prodsInfoTable'],
          loading: false,
          areaId: '',
          model: {},
          isShowForm: false,
          isAdd: false,
          confirmLoading: false,
          dataList:{},
          url:{
              byEquipName:"/intelligent/automation/failuresCount/queryByEquipName",
              add:"/intelligent/automation/failuresCount/add"
          },
          labelCol: {
              span: 6
          },
          wrapperCol: {
              span: 16
          },
          id:"",
          obj:{}
      }
    },
    computed: {
    },
    methods: {
      equipInfo(id,obj){
          // 清空表单数据
          this.form.resetFields();
          this.dataList = {}
          this.isDisabled = true;
            // this.model={};
          // 根据设备名称查询数据库
          getAction(this.url.byEquipName, { equipName: obj.title }).then(res => {
              if(res.success){
                  this.isShowForm=true;
                  this.dataList = res.result
              }else {
                  this.isShowForm=false;
              }
          });
      },
      add(id,obj){
          // 清空表单数据
          this.form.resetFields();
          this.dataList = {}
          this.isDisabled = false;
          this.id = id
          this.obj = obj
          // 若该设备名称已存在则不给添加
          getAction(this.url.byEquipName, { equipName: obj.title }).then(res => {
              if(res.success){
                  this.$message.warn('该设备已存在《故障次数》信息')
                  this.isShowForm=false;
              }else {
                  this.isShowForm=true;
                  console.log(res.result)
              }
          });
          // console.log("obj----",obj)
          this.dataList.equipName = obj.title

      },
      handleOkSave(){
          let formDate = this.dataList;
          console.log("表单提交的数据:",formDate)
          if(!formDate.timeRange || !formDate.failureCountA || !formDate.failureCountB || !formDate.failureCountC || !formDate.patrolFrequency || formDate.state === null ){
              this.$message.warn('请填写表单内容')
              return ;
          }
          httpAction(this.url.add,formDate,"post").then((res) => {
             if(res.success){
                 this.equipInfo(this.id,this.obj)
                 this.$message.success('保存成功')
             }
          });
      },
      handEdit(id,obj){
          // 清空表单数据
          this.form.resetFields();
          this.dataList = {}
          this.isDisabled = false;
          // 根据设备名称查询数据库
          getAction(this.url.byEquipName, { equipName: obj.title }).then(res => {
              if(res.success){
                  this.isShowForm=true;
                  this.dataList = res.result
              }else {
                  this.isShowForm=false;
                  return ;
              }
          });
          this.dataList.equipName = obj.title
          this.id = id
          this.obj = obj
      },
      cancel(){
          // 清空表单数据
          this.form.resetFields();
          // 表格数据
          this.dataList = {}
          // 输入库不可编辑
          this.isDisabled = true;
          // 表单不可看
          this.isShowForm=false;
      }
    },
    watch: {
      status(val) {
        // this.loadData()
      }
    },
    mounted () {
        console.log('加载‘故障次数组件’')
    }
  }
</script>

<style>

</style>