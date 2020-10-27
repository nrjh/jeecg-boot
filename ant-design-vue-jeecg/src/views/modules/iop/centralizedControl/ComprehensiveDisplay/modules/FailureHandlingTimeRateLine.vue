<template>
  <!--故障处理及时率-->
  <div id="lineTwo" style="width: 100%;height:400px;"></div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name:"FailureEquipmentBar",
    data(){
      return{
        url: {
          list: "/faultMonitoring/faultMonitoring/timeRate",
        }
      }

    },
    mounted () {
      // this.createFailureTotalTimeLine();
    },
    methods:{
      getRateLineData(param){
        console.log("参数22222",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              console.log("返回值222222",res.result)
            this.createFailureHandlingTimeRateLine(res.result)
          }
        })
      },
      createFailureHandlingTimeRateLine(result){
        let echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('lineTwo'));
        var date = [];
        var avgs = [];
        var totalFailure = [];
        for(var i = 0; i<result.length; i++){
          date.push(result[i].date);
          avgs.push(result[i].avgs == null? 0 : result[i].avgs);
          totalFailure.push(result[i].count);
        }
        let option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999'
              }
            }
          },
          toolbox: {
            feature: {
              dataView: {show: true, readOnly: false},
              magicType: {show: true, type: ['line', 'bar']},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          legend: {
            data: ['故障总数',  '处理及时率']
          },
          xAxis: [
            {
              type: 'category',
              data: date,
              axisPointer: {
                type: 'shadow'
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              name: '故障总数',
              // min: 0,
              // max: 250,
              // interval: 50,
              axisLabel: {
                formatter: '{value} 个'
              }
            },
            {
              type: 'value',
              name: '处理及时率',
              // min: 0,
              // max: 25,
              // interval: 5,
            }
          ],
          series: [
            {
              name: '故障总数',
              type: 'bar',
              data: totalFailure
            },
            {
              name: '处理及时率',
              type: 'line',
              yAxisIndex: 1,
              data: avgs
            }
          ]
        };
        myChart.setOption(option);
        window.onresize = function () {
          myChart.resize();
        }
      }
    }
  }
</script>