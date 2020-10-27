<template>
  <!--故障总时长线图-->
  <div id="lineOne" style="width: 100%;height:400px;"></div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name:"FailureEquipmentBar",
    data(){
      return{
        url: {
          list: "/faultMonitoring/faultMonitoring/totalTime",
        }
      }

    },
    mounted () {
      // this.createFailureTotalTimeLine();
    },
    methods:{
      getLineData(param){
        console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              console.log("返回值11111",res.result)
            this.createFailureTotalTimeLine(res.result)
          }
        })
      },
      createFailureTotalTimeLine(result){
        let echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('lineOne'));
        var date = [];
        var totalTime = [];
        var totalFailure = [];
        for(var i = 0; i<result.length; i++){
          date.push(result[i].date);
          totalTime.push(result[i].totalTime);
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
            data: ['故障总数',  '故障总时长']
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
              name: '总时长',
              // min: 0,
              // max: 25,
              // interval: 5,
              axisLabel: {
                formatter: '{value} min'
              }
            }
          ],
          series: [
            {
              name: '故障总数',
              type: 'bar',
              data: totalFailure
            },
            {
              name: '故障总时长',
              type: 'line',
              yAxisIndex: 1,
              data: totalTime
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