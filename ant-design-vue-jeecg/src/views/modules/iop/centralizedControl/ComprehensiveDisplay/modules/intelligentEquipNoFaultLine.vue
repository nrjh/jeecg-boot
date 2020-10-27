<template>
  <div>
  <a-modal
    :title="title"
    :visible="visible"
    :width="1200"
    :bodyStyle="bodyStyle"
    @cancel="handleCancel"
    :footer="null"
    cancelText="关闭">

    <a-col :span="24">
      <a-col :xxl="8" :md="8" :sm="24">
        <div class="card" :bordered="false" >
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false"
            >
          </a-table>
        </div>
      </a-col>
      <a-col :xxl="16" :md="16" :sm="24">
        <div class="card" :bordered="false">
          <line-chart-multid title="设备故障纵向对比" :height="height" :dataSource="faultData" @click="handleClick"/>
        </div>
      </a-col>
    </a-col>
    <a-col :span="24">
      <a-col :xxl="24" :md="24" :sm="24">
        <a-textarea :rows="3" placeholder="..." v-model="resultMessge" :readOnly="readonly"/>
      </a-col>
    </a-col>
  </a-modal>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import LineChartMultid from '@/components/chart/LineChartMultid'
  export default {
    name: "intelligentAnalysisModal",
    mixins:[ChartEventMixins],
    components: {
      LineChartMultid
    },
    data () {
      return {
        visible:false,
        title:"设备故障纵向对比",
        id:'',
        readonly:true,
        paramData:{},
        resultMessge:'',
        faultData:[],
        height:400,
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight-200)+"px"
        },
        tableScroll:{y :400},
        columns:[
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'故障名称',
            align:"center",
            dataIndex: 'month',
            customRender: function(text) {
              return text+'月'
            }

          },
          {
            title:'频次',
            align:"center",
            dataIndex: 'jeecg'
          },
        ],
        dataSource:[],
        url: {
          list: "/faultMonitoring/faultMonitoring/detailFaultComparisonByAreaIdAndEquipNO",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.paramData=param;
        this.visible=true;
        this.title='设备故障纵向对比('+param.startDate+'-'+param.endDate+')'
        this.getPieData(param);
      },
      handleCancel () {
        this.visible = false;
      },
      getPieData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
            console.log("返回值11111", res.result)
            this.faultData = res.result.result.mapData;
            this.dataSource = res.result.result.mapData;
            var min = res.result.result.mapData[0].jeecg;
            //用于记录连续增加的月份次数
            var count = 0;
            for (var i = this.faultData.length - 2; i >= 0 ; i--){
              if (min > this.faultData[i].jeecg){
                count++;
                min = this.this.faultData[i].jeecg
              }
              if (min == 0){
                break;
              }
            }
            //本月
            var monthValue = res.result.result.mapData[0].jeecg
            //上一个月
            var lastMonthValue = res.result.result.mapData[1].jeecg;
            if (monthValue != 0) {
              this.resultMessge = '通过对比，' + param.equipName + res.result.result.mapData[0].month + '月故障为' + monthValue + '次，较上月同比' + (monthValue - lastMonthValue < 0 ? "减少" : "增长") +  ((monthValue - lastMonthValue) / monthValue * 100) + "%" + '（连续' + count + '月同比增加，建议进行设备预防性更换设备）';
            }else {
              this.resultMessge = '';
            }
          }
        })
      },
      handleClick(event, chart) {
        // let equipType=event.data.point.equip_type;
        // let dataParamTemp={
        //   equipType:equipType
        // }
        // this.paramData= Object.assign(this.paramData, dataParamTemp);
        // console.log('this.paramData----',this.paramData)
        // this.$refs.barArea.show(this.paramData);
      },
    }
  }
</script>