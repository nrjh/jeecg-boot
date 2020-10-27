<template>
  <a-layout id="components-layout-demo-responsive">
    <!--      <a-layout-header :style="{ background: '#fff', padding: 0 }">-->
    <a-layout-header :style="{ background: 'rgba(245, 245, 245, 1)', padding: 0,height:'100px' }">
      <a-row>
        <a-col :span="24">
          <div class="fault-header">
            <div class="fault-cal" style="padding-top: 22px">
              <form >
                <a-row >
                  <a-col :span="4">
                    <a-form-item label="设备类型" :labelCol="{span:8}" :wrapperCol="{span:15}">
                      <j-dict-select-tag-ora type="list" v-model="queryParam.equipCateg"
                                             dictCode="P_COMM_CODE,param_name,param_id, SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'"
                                             @change="handleChange"
                                             :trigger-change="true"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="4">
                    <a-form-item label="检定线" :labelCol="{span:6}" :wrapperCol="{span:15}">
                      <a-select  v-model="queryParam.areaId" placeholder="请选择">
                        <a-select-option v-for="item in equipTypeLists" :key="item.text" :value="item.value">
                          {{ item.title }}
                        </a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :span="5">
                    <a-form-item label="统计维度" :labelCol="{span:8}" :wrapperCol="{span:16}">
                      <a-radio-group v-model="queryParam.classify">
                        <a-radio value="day">
                          按天
                        </a-radio>
                        <a-radio value="month">
                          按月
                        </a-radio>
                      </a-radio-group>
                    </a-form-item>
                  </a-col>
                  <a-col :span="4">
                    <a-form-item label="监控日期:" :labelCol="{span:8}" :wrapperCol="{span:16}">
                      <j-date  placeholder="开始日期" v-model="queryParam.startDate"  :trigger-change="true" style="width: 100%"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="3">
                    <a-form-item label="至"  :labelCol="{span:6}" :wrapperCol="{span:16}">
                      <j-date   placeholder="结束日期"    :trigger-change="true" style="width: 100%" v-model="queryParam.endDate"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="4">
                    <a-form-item>
                        <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                          <a-button type="primary" @click="getDataList" icon="search">查询</a-button>
                          <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                        </span>
                    </a-form-item>
                  </a-col>
                </a-row>
              </form>
            </div>
          </div>
        </a-col>
      </a-row>
    </a-layout-header>

    <!--         头和内容分割线          -->

    <a-layout-content :style="{ margin: '4px' }">
      <a-row>
        <a-col :xxl="1" :lg="1" :md="1">
          <div class="content-solid">
            <h2>故障任务跟踪</h2>
          </div>
        </a-col>
        <a-col :xxl="23" :lg="23" :md="23">
          <div class="card">
            <a-row>
              <a-col :span="24">
                <a-row>
                  <a-col :xxl="16" :md="16" :sm="24">
                    <div class="card">
                      <FailureEquipmentBar ref="failureBar"/>
                    </div>
                  </a-col>
                  <a-col :xxl="8" :md="8" :sm="24">
                    <div class="card">
                      <FaultTypeTable ref="faultTypeTable"/>
                    </div>
                  </a-col>
                </a-row>
              </a-col>

              <a-col :span="24">
                <a-row>
                  <a-col :xxl="8" :md="8" :sm="24">
                    <div class="card">
                      <AreaLinePie ref="failurePie"/>
                    </div>
                  </a-col>
                  <a-col :xxl="8" :md="8" :sm="24">
                    <div class="card">
                      <FailureEquipmentPie ref="failureEquipmentPie"/>
                    </div>
                  </a-col>
                  <a-col :xxl="8" :md="8" :sm="24">
                    <div class="card">
                      <FaultTypePie ref="faultTypePie"/>
                    </div>
                  </a-col>
                </a-row>
              </a-col>

              <a-col :span="24">
                <a-col :xxl="16" :md="16" :sm="24">
                  <div class="card">
                    <FailureTotalTimeLine ref="failureTotalTime"/>
                  </div>
                </a-col>
                <a-col :xxl="8" :md="8" :sm="24">
                  <div class="card">
                    <FaultEquipmentAlarmTable ref="faultEquipmentAlarmTable"/>
                  </div>
                </a-col>
              </a-col>

              <a-col :span="24">
                <a-col :xxl="16" :md="16" :sm="24">
                  <div class="card">
                    <FailureHandlingTimeRateLine ref="failureHandlingTimeRate"/>
                  </div>
                </a-col>
                <a-col :xxl="8" :md="8" :sm="24">
                  <div class="card">
                    <SparePartTable ref="sparePartTable"/>
                  </div>
                </a-col>
              </a-col>
            </a-row>

          </div>
        </a-col>
        <!--              <a-row>-->
        <!--                <a-col :span="24">-->
        <!--                  <div class="card-cal"></div>-->
        <!--                </a-col>-->
        <!--              </a-row>-->
        <!--              <a-row>-->
        <!--                <a-col :span="24">-->
        <!--                  <div class="card">-->
        <!--                    <a-row>-->
        <!--                      <a-col :span="24">-->
        <!--                        <a-row>-->
        <!--                          <a-col :md="8" :sm="24">-->
        <!--                            <div class="card">-->
        <!--&lt;!&ndash;                              <DemoPlaceholder class="card-cal"/>&ndash;&gt;-->
        <!--                            </div>-->
        <!--                          </a-col>-->
        <!--                          <a-col :md="8" :sm="24">-->
        <!--                            <div class="card">-->
        <!--&lt;!&ndash;                              <DemoPlaceholder class="card-cal"/>&ndash;&gt;-->
        <!--                            </div>-->
        <!--                          </a-col>-->
        <!--                          <a-col :md="8" :sm="24">-->
        <!--                            <div class="card">-->
        <!--&lt;!&ndash;                              <DemoPlaceholder class="card-cal"/>&ndash;&gt;-->
        <!--                            </div>-->
        <!--                          </a-col>-->
        <!--                        </a-row>-->
        <!--                      </a-col>-->
        <!--                      <a-col :xl="12" :lg="12" :sm="24">-->
        <!--                        <div class="card">-->
        <!--&lt;!&ndash;                          <DemoTable/>&ndash;&gt;-->
        <!--                        </div>-->
        <!--                      </a-col>-->
        <!--                      <a-col :xl="12" :lg="12" :sm="24">-->
        <!--                        <div class="card">-->
        <!--&lt;!&ndash;                          <DemoTable/>&ndash;&gt;-->
        <!--                        </div>-->
        <!--                      </a-col>-->
        <!--                    </a-row>-->
        <!--                  </div>-->
        <!--                </a-col>-->
        <!--              </a-row>-->

        <!--          <a-col :xxl="4" :lg="5" :md="7">-->
        <!--            <div class="card">-->
        <!--              <h2>配送任务信息</h2>-->
        <!--              <DemoTable/>-->
        <!--            </div>-->
        <!--            <div class="card">-->
        <!--              <h2>通知公告</h2>-->
        <!--              <DemoTable/>-->
        <!--            </div>-->
        <!--          </a-col>-->
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ACol from "ant-design-vue/es/grid/Col";
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDate from '@/components/jeecg/JDate'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import { initDictOptionsOra,initDictOptionsIop } from '@/components/dict/JDictSelectUtil'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import FailureEquipmentBar from './modules/FailureEquipmentBar'
  import AreaLinePie from './modules/AreaLinePie'
  import FailureEquipmentPie from './modules/FailureEquipmentPie'
  import FaultTypePie from './modules/FaultTypePie'
  import FailureTotalTimeLine from './modules/FailureTotalTimeLine'
  import FailureHandlingTimeRateLine from './modules/FailureHandlingTimeRateLine'
  import FaultEquipmentAlarmTable from './modules/FaultEquipmentAlarmTable'
  import SparePartTable from './modules/SparePartTable'
  import FaultTypeTable from './modules/FaultTypeTable'
  import moment from 'moment'
  import ARow from "ant-design-vue/es/grid/Row";
  export default {
    name: "FaultMonitoring",
    mixins:[JeecgListMixin],
    components: {
      ARow,
      AFormItem,
      ACol,
      JDictSelectTagOra,
      JDate,
      JDictSelectTag,
      FailureEquipmentBar,
      AreaLinePie,
      FailureEquipmentPie,
      FaultTypePie,
      FailureTotalTimeLine,
      FailureHandlingTimeRateLine,
      FaultEquipmentAlarmTable,
      SparePartTable,
      FaultTypeTable

    },
    data () {
      return {
        description: '办公用品管理页面',
        equipTypeLists:[],
        queryParam:{
          equipCateg: '',
          classify:'',
          startDate:'',
          endDate:''
        },
        tatus:true,
        days:{},
        months:{},
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
        ],
        validatorRules: {
          prdCategory: {
            rules: []
          },
          datePlanned: {
            rules: []
          },
          team: {
            rules: []
          },
        },
        url: {
          list: "/DevMonitorInfo/devMonitorInfo/queryDevList",
          add: "/outOrder/stockOutOrder/add",
          edit: "/outOrder/stockOutOrder/edit",
          queryById: "/outOrder/stockOutOrder/queryById",
          queryStockOutOrderLineByMainId: "/outOrder/stockOutOrder/queryStockOutOrderLineByMainId",
          submitStockApproval: '/outOrder/stockOutOrder/submitApproval',
          },
      }
    },
    created(){
      this.queryParam.classify = 'day';
      var now = moment().format('YYYY-MM-DD')
      var dayBefore = moment().subtract(30,'d').format('YYYY-MM-DD');
      console.log("11111",dayBefore)
      this.queryParam.startDate = dayBefore
      this.queryParam.endDate = now;
    },
    methods: {
      getDataList(){
        this.GetNumberOfDays(this.queryParam.startDate,this.queryParam.endDate)
        this.GetNumberOfMonths(this.queryParam.startDate,this.queryParam.endDate)
        if(this.queryParam.classify === "day"){
          if(this.days>31 || this.days<0){
            this.$message.warning("选择的日期超出范围");
          }else{
            this.$refs.failureBar.getBarData(this.queryParam);
            this.$refs.failurePie.getPieData(this.queryParam);
            this.$refs.failureEquipmentPie.getPieData(this.queryParam);
            this.$refs.faultTypePie.getPieData(this.queryParam);
            this.$refs.failureTotalTime.getLineData(this.queryParam);
            this.$refs.failureHandlingTimeRate.getRateLineData(this.queryParam);
            this.$refs.faultEquipmentAlarmTable.getFaultEquipmentAlarmTableData(this.queryParam);
            this.$refs.sparePartTable.getSparePartTableData(this.queryParam);
            this.$refs.faultTypeTable.getFaultTypeTableData(this.queryParam);
          }
        }else if(this.queryParam.classify === "month"){
          if(this.months>12 || this.months<0){
            this.$message.warning("选择的日期超出范围")
          }else{
            this.$refs.failureBar.getBarData(this.queryParam);
            this.$refs.failurePie.getPieData(this.queryParam);
            this.$refs.failureEquipmentPie.getPieData(this.queryParam);
            this.$refs.faultTypePie.getPieData(this.queryParam);
            this.$refs.failureTotalTime.getLineData(this.queryParam);
            this.$refs.failureHandlingTimeRate.getRateLineData(this.queryParam);
            this.$refs.faultEquipmentAlarmTable.getFaultEquipmentAlarmTableData(this.queryParam);
            this.$refs.sparePartTable.getSparePartTableData(this.queryParam);
            this.$refs.faultTypeTable.getFaultTypeTableData(this.queryParam);
          }
        }

      },
      GetNumberOfDays(date1,date2){//获得天数
        //date1：开始日期，date2结束日期
        var a1 = Date.parse(new Date(date1));
        var a2 = Date.parse(new Date(date2));
        var day = parseInt((a2-a1)/ (1000 * 60 * 60 * 24));//核心：时间戳相减，然后除以天数
        this.days = day;
        console.log("天数11111",day)
      },
      GetNumberOfMonths(date1,date2){
        let m1 = (date1 || '').split('-')
        let month1 = parseInt(m1[0]) * 12 + parseInt(m1[1]);
        let m2 = (date2 || '').split('-')
        let month2 = parseInt(m2[0]) * 12 + parseInt(m2[1]);
        console.log("月份11111",m1)
        console.log("月份22222",m2)
        this.months = month2-month1
        console.log("月份333333",this.months)

      },
      inputTextAreaId(val){
        this.form.setFieldsValue({ lineName: val })
      },
      handleChange(value){
        this.queryParam.equipCateg = value
        this.equipTypeLists=[];
        let str="o_area,area_name,area_id,EQUIP_CATEG='"+value+"'";
        console.log("传值11111111",str)
        initDictOptionsOra(str).then(res => {
          this.equipTypeLists=res.result;
          console.log("传值22222",res)
        })
      },

    },
    mounted() {
      this.getDataList();
    }
  };



</script>

<style>
  #components-layout-demo-responsive {
    height: 100vh;
  }

  .card .card-cal {
    padding: 40px 0px 0px 40px;
    height: 600px;
  }
  .fault-header{
    border: 2px solid #BDBDBD;
    margin: 4px;
    border-radius: 4px;
    box-shadow: 0px 6px 12px 0 #d9d9d9;
  }
  .fault-header fault-cal{
    text-align: center;
  }
  .content-solid{
    height: 1550px;
    border: 2px solid #BDBDBD;
    margin: 4px;
    border-radius: 4px;
    box-shadow: 0px 6px 12px 0 #d9d9d9;
    text-align:center;
    padding-top: 600px;
  }

  .card {
    border: 2px solid #BDBDBD;
    margin: 4px;
    border-radius: 4px;
    box-shadow: 0px 6px 12px 0 #d9d9d9;
  }

</style>


<style scoped>
  @import '~@assets/less/common.less';
</style>
