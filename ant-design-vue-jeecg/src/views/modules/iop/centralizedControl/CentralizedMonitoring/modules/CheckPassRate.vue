<template>
  <div id="CheckPassRate"></div>
</template>
<script>
    import {httpAction, getAction} from '@/api/manage'

    export default {
      name:"CheckPassRate",
      data(){
          return{
              url:{
                  checkPassRateUrl:"/centralized/centralizedMonitoring/checkPassRate"
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
              getAction(this.url.checkPassRateUrl,queryData).then((res) => {
                  if(res.success){
                      this.$nextTick(() => {
                          // 基准值，由字典获取(产品说的)
                          let standardArray = []
                          let passRate = []
                          let taskArray = []
                          let xArray = []
                          if(res.result != null){
                              for(let i = 0;i<res.result.passRate.length;i++){
                                  standardArray[i] = 6;
                              }
                              // 合格率
                              passRate = res.result.passRate;
                              // 任务数量
                              taskArray = res.result.taskCountArray;
                              // x轴
                              xArray = res.result.xTimeArray;
                          }
                          this.drawLine(passRate,standardArray,taskArray,xArray);
                      })
                  }
              })
          },
          drawLine(passRate,standardArray,taskArray,xArray){
              // 基于准备好的dom，初始化echarts实例
              let CheckPassRate = this.$echarts.init(document.getElementById('CheckPassRate'))
              CheckPassRate.resize();//直接加这句即可
              // 绘制图表
              CheckPassRate.setOption({
                  title: {
                      text: '检定合格率',
                      subtext: '简介',
                      left: 'center'
                  },
                  grid:{
                      //   x:25,
                      y:125,
                      //   x2:5,
                      y2:20,
                      borderWidth:1
                  },
                  tooltip: {
                      trigger: 'axis',
                      axisPointer: {
                          type: 'cross',
                          crossStyle: {
                              color: '#999'
                          }
                      }
                  },
                  legend: {
                      data: ['基准值', '合格率'],
                      top:60
                  },
                  xAxis: [
                      {
                          type: 'category',
                          data: xArray,
                          axisPointer: {
                              type: 'shadow'
                          }
                      }
                  ],
                  yAxis: [
                      {
                          type: 'value',
                          name: '任务量',
                          min: 0,
                          max: 10,
                          interval: 1,
                          axisLabel: {
                              formatter: '{value}'
                          }
                      },
                      {
                          type: 'value',
                          name: '标准',
                          min: 0,
                          max: 10,
                          interval: 1,
                          axisLabel: {
                              formatter: '{value}'
                          }
                      }
                  ],
                  series: [
                      {
                          name: '任务量 ',
                          yAxisIndex: 0,
                          type: 'bar',
                          data: taskArray
                      },
                      {
                          name: '基准值',
                          yAxisIndex: 1,
                          type: 'line',
                          color:'gray',
                          data: standardArray
                      },
                      {
                          name: '合格率',
                          type: 'line',
                          color:'#6cb5a0',
                          yAxisIndex: 1,
                          data: passRate
                      }
                  ]
              })
          }

      }
  }
</script>

<style>
  #CheckPassRate {
    width: 500px;
    height: 300px;
  }
</style>