<template>
  <div id="Operation"></div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
      name:"Operation",
      data(){
          return{
              url:{
                  operationUrl:"/centralized/centralizedMonitoring/operationStatus"
              }
          }
      },
      mounted () {
      },
      methods:{
          showCharts(param){
              let queryData = this._.cloneDeep(param)
              if  (queryData.equipSpecCode === "全部"){
                  queryData.equipSpecCode = "-1"
              }
              if  (queryData.equipCateg === "全部"){
                  queryData.equipCateg = "-1"
              }
              getAction(this.url.operationUrl,queryData).then((res) => {
                  if(res.success){
                      this.$nextTick(() => {
                          let checkCountArray = []
                          let taskCountArray = []
                          let actionTimeArray =[]
                          let xTimeArray = []
                          let titleArray = []
                          if(res.result != null){
                              checkCountArray = res.result.checkCountArray;
                              taskCountArray = res.result.taskCountArray;
                              actionTimeArray = res.result.actionTimeArray;
                              xTimeArray = res.result.xTimeArray;
                              let showTimeFlag = '运行时长';
                              if(queryData.timeFlag === '1'){
                                  showTimeFlag = showTimeFlag+"(时)"
                              }else {
                                  showTimeFlag = showTimeFlag+"(天)"
                              }
                              titleArray = ['检定量', '任务数', showTimeFlag]
                          }
                          let YLeftMax = checkCountArray.sort((v1,v2) => v2 - v1)[0];
                          let YRightMax = checkCountArray.sort((v1,v2) => v2-v1)[0];
                          this.drawLine(checkCountArray,taskCountArray,actionTimeArray,xTimeArray,YLeftMax,YRightMax,titleArray);
                      });
                  }
              })
          },
          drawLine(checkCountArray, taskCountArray,actionTimeArray, xTimeArray, YLeftMax, YRightMax,titleArray){
              // 基于准备好的dom，初始化echarts实例
              let Operation = this.$echarts.init(document.getElementById('Operation'))
              var colors = ['#5793f3', '#d14a61', '#675bba'];
              // 绘制图表
              Operation.setOption({
                  color: colors,
                  title: {
                      text: '运行情况统计',
                      subtext: '简介',
                      left:'center'
                  },
                  grid:{
                      // x:25,
                      y:125,
                      // x2:5,
                      y2:20,
                      borderWidth:1
                  },
                  tooltip: {
                      trigger: 'axis',
                      axisPointer: {
                          type: 'cross'
                      }
                  },
                  legend: {
                      data: titleArray,
                      top:60
                  },
                  xAxis: [
                      {
                          type: 'category',
                          axisTick: {
                              alignWithLabel: true
                          },
                          data: xTimeArray
                      }
                  ],
                  yAxis: [
                      {
                          type: 'value',
                          axisLine: {show: false},
                          axisTick: {show: false},
                          name: '左边',
                          max: YLeftMax,
                          min:0,
                          splitNumber:5,
                          interval: (YLeftMax - 0) / 5,
                          position: 'left',
                          axisLine: {
                              lineStyle: {
                                  color: colors[0]
                              }
                          },
                          axisLabel: {
                              formatter: '{value}'
                          }
                      },
                      {
                          type: 'value',
                          axisLine: {show: false},
                          axisTick: {show: false},
                          name: '检定量',
                          max:YRightMax,
                          splitNumber:5,
                          interval:(YLeftMax - 0) / 5,
                          position: 'right',
                          axisLine: {
                              lineStyle: {
                                  color: colors[1]
                              }
                          },
                          axisLabel: {
                              formatter: '{value}'
                          },
                          // min: 0,
                          // max: Max2*2,
                          // splitNumber: 6,
                          // interval: (Max2*2 - 0) / 6
                      }
                  ],
                  series: [
                      {
                          name: titleArray[0],
                          type: 'bar',
                          yAxisIndex: 0,
                          data: checkCountArray
                      },
                      {
                          name: titleArray[1],
                          type: 'line',
                          yAxisIndex: 1,
                          data: taskCountArray
                      },
                      {
                          name: titleArray[2],
                          type: 'line',
                          yAxisIndex: 1,
                          data: actionTimeArray
                      }
                  ]
              })
          }
      }
  }
</script>
<style>
  #Operation {
    width: 500px;
    height: 300px;
  }
</style>