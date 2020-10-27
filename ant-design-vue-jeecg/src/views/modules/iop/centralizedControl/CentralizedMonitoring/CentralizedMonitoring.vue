<template>
  <!-- 查询区域 -->
  <div class="table-page-search-wrapper" >
    <a-form :form="form" layout="inline">
      <a-card>
        <a-card style="background: #ffffff;padding: 15px 20px;border: 1px #e4e3e3 solid;">
          <a-row :gutter="24">

            <a-col :span="8">
              <a-form-item label="设备类型:" :labelCol="{span:4}" :wrapperCol="{span:18}">
<!--                <j-dict-select-tag v-decorator="['equipCateg', validatorRules.equipCateg]"-->
<!--                                 type="list"-->
<!--                                 :trigger-change="true"-->
<!--                                dictCode="Centralized_ControlMonitor_EquipCateg" placeholder="请选择设备类型"/>-->
                <j-dict-select-tag-ora type="list"
                                       v-decorator="['equipCateg', validatorRules.equipCateg]"
                                       dictCode="P_COMM_CODE,param_name,param_id, SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'"
                                       @change="handleChange"
                                       :trigger-change="true"
                />
              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item label="设备规格:" :labelCol="{span:4}" :wrapperCol="{span:18}">
                <j-dict-select-tag v-decorator="['equipSpecCode', validatorRules.equipSpecCode]"
                                 type="list"
                                 :trigger-change="true"
                                dictCode="CentralizedControlMonitor_EquipSepcCode" placeholder="请选择设备规格"/>
              </a-form-item>
            </a-col>

          </a-row>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="检定线:" :labelCol="{span:4}" :wrapperCol="{span:18}">
<!--                <j-dict-select-tag-ora type="list" :trigger="true"-->
<!--                                       v-decorator="['lineNo', validatorRules.lineNo]"-->
<!--                                       :trigger-change="true"-->
<!--                                       dictCode="o_area,area_name,area_id"-->
<!--                                       placeholder="请选择检定线"/>-->
                <a-select  v-decorator="['lineNo', validatorRules.lineNo]" placeholder="请选择">
                  <a-select-option v-for="item in equipTypeLists" :key="item.text" :value="item.value">
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="统计维度" :labelCol="{span:12}" :wrapperCol="{span:12}">
                <a-radio-group v-model="timeFlag" @change="changeRadio">
                  <a-radio value="1">
                    小时
                  </a-radio>
                  <a-radio value="2">
                    按天
                  </a-radio>
                  <a-radio value="3">
                    按月
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>

            <a-col :span="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
            </a-col>

          </a-row>
          <a-row :gutter="24" v-if="this.toggleSearchStatus">
            <a-col :span="8">
              <a-form-item label="监控日期:" :labelCol="{span:12}" :wrapperCol="{span:12}">
                <j-date v-model="startDealTime" placeholder="请选择开始时间"
                        style="width: 100%"></j-date>

              </a-form-item>
            </a-col>

            <a-col :span="8">
              <a-form-item label="结束日期:" :labelCol="{span:12}" :wrapperCol="{span:12}">
                <j-date  v-model="endDealTime"  placeholder="结束日期"
                        style="width: 100%"></j-date>
              </a-form-item>
            </a-col>

          </a-row>
        </a-card>
        <a-row class="detection-trend">
            <a-col :xxl="1" :lg="1" :md="1" style="border:1px solid rgb(228, 228, 228);text-align: center;">
              <div class="detection-trend-header">
                生成任务跟踪
              </div>
            </a-col>
            <a-col :xxl="23" :lg="23" :md="12">
              <a-row>
                <a-col :xxl="12" :md="12" :sm="24">
                  <a-card>
                    <PrdTaskTrack ref="prdTaskTrack"/>
                  </a-card>
                </a-col>

                <a-col :xxl="12" :md="12" :sm="24">
                  <a-card>
                    <CheckDevRate ref="checkDevRate"></CheckDevRate>
                  </a-card>
                </a-col>
              </a-row>

              <a-row>
                <a-col :xxl="12" :md="12" :sm="24">
                  <a-card>
                    <Operation ref="operation"></Operation>
                  </a-card>
                </a-col>
                <a-col :xxl="12" :md="12" :sm="24">
                  <a-card>
                    <CheckPassRate ref="checkPassRate"></CheckPassRate>
                  </a-card>
                </a-col>
              </a-row>
              <a-row style="margin-top:2vh">
                <a-table
                  class="tb"
                  ref="table"
                  size="middle"
                  :columns="columns"
                  :pagination="false"
                  :data-source="dataSource"
                  :scroll="tableScroll"
                  :loading="loading"
                  :rowClassName="setRowClassName"
                  bordered>
                  <template slot="name" slot-scope="text">
                    <a>{{ text }}</a>
                  </template>
                </a-table>
              </a-row>
            </a-col>
          </a-row>
      </a-card>
    </a-form>



  </div>
  <!-- 查询区域-END -->
</template>

<script>
    import pick from 'lodash.pick'
    import {mergeCellKey,renderContent} from './util/MergecolSpan'
    import {twoMergeCellKey} from './util/MergecolSpanTwo'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
    import { initDictOptionsOra,initDictOptionsIop } from '@/components/dict/JDictSelectUtil'
    import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
    import JDate from '@/components/jeecg/JDate'
    import PrdTaskTrack from './modules/PrdTaskTrack'
    import CheckDevRate from './modules/CheckDevRate'
    import Operation from './modules/Operation'
    import CheckPassRate from './modules/CheckPassRate'
    import moment from 'moment'
    import { getAction } from '@/api/manage'
    export default {
        name: 'CentralizedMonitoring',
        mixins: [JeecgListMixin],
        components: {
            JDictSelectTag,
            JDictSelectTagOra,
            JDate,
            PrdTaskTrack,
            CheckDevRate,
            Operation,
            CheckPassRate
        },
        data () {
            return {
                equipTypeLists:[],
                initQueryData:{},
                loading:false,
                form: this.$form.createForm(this),
                toggleSearchStatus:false,
                startDealTime:'',
                endDealTime:'',
                tableScroll:{x :47*30+50,y:350},
                url:{
                    detectTaskList:"/centralized/centralizedMonitoring/detectTaskList",
                },
                // 不发送请求标识
                unRequest:"",
                dataSource:[],
                // 字段校验
                validatorRules:{
                    equipCateg:{

                        rules: [
                        ]
                    },
                    equipSpecCode:{rules: [
                        ]
                    },
                    startDealTime:{
                        rules: [
                        ]
                    },
                    endDealTime:{
                        rules: [{ required: true}]
                    },
                    lineNo:{
                        rules: [
                        ]
                    }
                },
                // 表格
                columns: [
                    {
                        title:'日期',
                        dataIndex:'checkDateTime',
                        align:"center",
                        customRender: (value, row, index) => {
                            const obj = {
                                children: value,
                                attrs: {},
                            };
                            obj.attrs.rowSpan = mergeCellKey(row.checkDateTime, this.dataSource, 'checkDateTime')
                            return obj;
                        },
                    },
                    {
                        title:'任务类型',
                        dataIndex:'isNormalCheck',
                        align:"center",
                        customRender: (value, row, index) => {
                            const obj = {
                                children: value,
                                attrs: {},
                            };
                            obj.attrs.rowSpan = twoMergeCellKey(row.isNormalCheck, this.dataSource,'isNormalCheck',index)
                            return obj;
                        },
                    },
                    {
                        title:'检定线',
                        dataIndex:'lineNo',
                        align:"center",
                    },
                    {
                        title:'任务单号',
                        dataIndex:'taskNo',
                        align:"center",
                    },
                    {
                        title:'批次',
                        dataIndex:'arriveBatchNo',
                        align:"center",
                    },
                    {
                        title:'供应商',
                        dataIndex:'supplyNo',
                        align:"center",
                    },
                    {
                        title:'任务类型 ',
                        dataIndex:'detectType',
                        align:"center",
                    },
                    {
                        title:'费率',
                        dataIndex:'useRate',
                        align:"center",
                    },
                    {
                        title:'设备描述',
                        dataIndex:'equipDescribe',
                        align:"center",
                    },
                    {
                        title:'检定时间',
                        dataIndex:'taskBgnDate',
                        align:"center",
                    },
                    {
                        title:'任务状态',
                        dataIndex:'taskStatus',
                        align:"center",
                    },
                    {
                        title:'任务总量',
                        dataIndex:'detectQty',
                        align:"center",
                    },
                    {
                        title:'当天完成量',
                        dataIndex:'detectConcP',
                        align:"center",
                    },
                    {
                        title:'当天合格率',
                        dataIndex:'passRate',
                        align:"center",
                    },
                    {
                        title:'累计完成量',
                        dataIndex:"cumulativeFinishCount",
                        align:"center"
                    },
                    {
                        title:'累计合格率',
                        dataIndex:"cumulativePassRate",
                        align:"center"
                    },
                    {
                        title:'审核状态',
                        dataIndex:"auditStatus",
                        align:"center"
                    },
                    {
                        title:'方案核对人',
                        dataIndex:"makerName",
                        align:"center"
                    },
                    {
                        title:'备注',
                        dataIndex:'remarks',
                        align:"center"
                    }
                ],
                timeFlag:"2",
                days:{},
                months:{},
            }
        },
        methods:{
            handleChange(value){
                this.equipTypeLists=[];
                let str="o_area,area_name,area_id,EQUIP_CATEG='"+value+"'";
                console.log("传值11111111",str)
                initDictOptionsOra(str).then(res => {
                    this.equipTypeLists=res.result;
                    console.log("model---",this.model)
                    this.model.lineNo = null
                    this.$nextTick(() => {
                        this.form.setFieldsValue(pick(this.model,'lineNo'))
                    })
                })
            },
            loadData(arg) {
                if(!this.unRequest){
                    console.log('不发送请求22333')
                    return
                }
            },
            setRowClassName(record){
                if (record.isNormalCheck === '非常规检定') {
                    return 'rowcolor'
                  } else {
                    return 'rowcolor2'
                  }
            },
            changeRadio(val){
                this.timeFlag = val.target.value
            },
            searchQuery(){
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    let formData = Object.assign(this.model, values);
                    formData.timeFlag = this.timeFlag
                    formData.startDealTime = this.startDealTime
                    formData.endDealTime = this.endDealTime
                    this.initData(formData)
                })

            },
            searchReset(){
                this.model = Object.assign({}, {});
                this.model.equipSpecCode = "全部"
                this.model.equipCateg = "全部"
                this.model.lineNo = null
                this.equipTypeLists = []
                this.endDealTime = moment().format("YYYY-MM-DD")
                this.model.endDealTime = moment().format("YYYY-MM-DD")
                this.startDealTime =  moment().subtract(7,'d').format('YYYY-MM-DD')
                this.model.startDealTime =  moment().subtract(7,'d').format('YYYY-MM-DD')
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model,'equipCateg','startDealTime','endDealTime','equipSpecCode','lineNo'))
                })
                let formData = Object.assign(this.model, {});
                this.timeFlag = "2"
                formData.timeFlag = this.timeFlag
                this.$message.success("重置成功!");
                this.initData(formData)
            },
            initData(queryData){
                this.GetNumberOfDays(this.startDealTime,this.endDealTime)
                this.GetNumberOfMonths(this.startDealTime,this.endDealTime)
                this.loading = true;
                // 拿到查询条件
                // 以小时
                if(queryData.timeFlag === '1'){
                    if(this.days > 1 || this.days<0){
                        this.$message.warning("选择的日期超出范围");
                    }else{
                        // 执行组件逻辑
                        this.getData(queryData)
                        // 获取生产任务图
                        this.$refs.prdTaskTrack.showCharts(queryData)
                        // 设备占比图
                        this.$refs.checkDevRate.showCharts(queryData)
                        // 获取运行情况统计图
                        this.$refs.operation.showCharts(queryData)
                        // 获取检定合格率
                        this.$refs.checkPassRate.showCharts(queryData)
                    }
                }
                // 以天数
                else if(queryData.timeFlag === '2'){
                    if(this.days>31 || this.days<0){
                        this.$message.warning("选择的日期超出范围")
                    }else {
                        // 执行组件逻辑
                        // 执行组件逻辑
                        this.getData(queryData)
                        // 获取生产任务图
                        this.$refs.prdTaskTrack.showCharts(queryData)
                        // 设备占比图
                        this.$refs.checkDevRate.showCharts(queryData)
                        // 获取运行情况统计图
                        this.$refs.operation.showCharts(queryData)
                        // 获取检定合格率
                        this.$refs.checkPassRate.showCharts(queryData)
                    }
                }
                // 以月份
                else {
                    if(this.months>12 || this.months<0){
                        this.$message.warning("选择的日期超出范围")
                    }else{
                        // 执行组件逻辑
                        this.getData(queryData)
                        // 获取生产任务图
                        this.$refs.prdTaskTrack.showCharts(queryData)
                        // 设备占比图
                        this.$refs.checkDevRate.showCharts(queryData)
                        // 获取运行情况统计图
                        this.$refs.operation.showCharts(queryData)
                        // 获取检定合格率
                        this.$refs.checkPassRate.showCharts(queryData)
                    }
                }
            },
            GetNumberOfDays(date1,date2){//获得天数
                //date1：开始日期，date2结束日期
                var a1 = Date.parse(new Date(date1));
                var a2 = Date.parse(new Date(date2));
                var day = parseInt((a2-a1)/ (1000 * 60 * 60 * 24));//核心：时间戳相减，然后除以天数
                this.days = day;
            },
            GetNumberOfMonths(date1,date2){
                let m1 = (date1 || '').split('-')
                let month1 = parseInt(m1[0]) * 12 + parseInt(m1[1]);
                let m2 = (date2 || '').split('-')
                let month2 = parseInt(m2[0]) * 12 + parseInt(m2[1]);
                this.months = month2-month1

            },
            getData(paramData){
                let queryData = this._.cloneDeep(paramData)
                if  (queryData.equipSpecCode === "全部"){
                    queryData.equipSpecCode = "-1"
                }
                if  (queryData.equipCateg === "全部"){
                    queryData.equipCateg = "-1"
                }
                getAction(this.url.detectTaskList,queryData).then((res) => {
                    if (res.success) {
                        this.dataSource = res.result;
                    }
                    this.loading = false;
                });
            }
        },
        created () {
            this.model = Object.assign({}, {});
            this.model.equipSpecCode = "全部"
            this.model.equipCateg = "全部"
            this.endDealTime = moment().format("YYYY-MM-DD")
            this.model.endDealTime = moment().format("YYYY-MM-DD")
            this.startDealTime =  moment().subtract(7,'d').format('YYYY-MM-DD')
            this.model.startDealTime =  moment().subtract(7,'d').format('YYYY-MM-DD')
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model,'equipCateg','startDealTime','endDealTime','equipSpecCode'))
            })

        },
        mounted () {
            let formData = Object.assign(this.model, {});
            formData.timeFlag = this.timeFlag
            this.loading = true;
            this.initQueryData = formData
            this.initData(formData)
        }
    }
</script>

<style>
  .detection-trend-header{
    height: 1100px;
    writing-mode: vertical-lr;/*从左向右 从右向左是 writing-mode: vertical-rl;*/
    /*writing-mode: tb-lr;IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
    /*border: 1px solid #333333;*/
    text-align: center;
    display: inline-block;
    padding: 5px;
  }
  .detection-trend{
    /*margin-left: 10px;*/
    /*margin-right: 10px;*/
    /*height: 400px;*/
    margin-top: 10px;
  }
  .detection-trend-body{
    display: inline-block;
    border-top: 1px solid #333333;
    border-right: 1px solid #333333;
    border-bottom: 1px solid #333333;
    border-left: 1px solid #333333;
    width: 100%;
    float: right;
    height: 400px;
  }
  .rowcolor :nth-child(n+3){
    background: yellowgreen;
  }
  .rowcolor2 :nth-child(n+3){
    background: blue;
  }
</style>