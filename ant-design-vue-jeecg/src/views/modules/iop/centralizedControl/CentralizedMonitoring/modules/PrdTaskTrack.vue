<template>
  <div id="PrdTaskTrackChart"></div>
</template>
<script>
    import {httpAction, getAction} from '@/api/manage'
    export default {
      name:"PrdTaskTrack",
      data(){
          return{
              url:{
                  prdTaskTrace:"/centralized/centralizedMonitoring/prdTaskTrace"
              },
              actualData:[],
              planData:[],
              xList:[],
              prdTaskTraceNum:0
          }
      },
      mounted () {
      },
      created(){
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
              getAction(this.url.prdTaskTrace, queryData).then((res) => {
                  if (res.success) {
                      if(res.result != null){
                          this.actualData = res.result.actNumArray
                          this.planData = res.result.planNumArray
                          this.xList = res.result.timeArray
                          for(let i = 0 ;i <this.planData.length ;i++){
                              if(this.planData[i] === "null"){
                                  this.planData[i] = 0;
                              }
                          }
                          console.log("this.planData----",this.planData)
                      }else{
                          this.actualData = []
                          this.planData = []
                          this.xList = []
                      }
                      console.log("PrdTaskTrack",res.result)
                      this.$nextTick(() => {
                          this.drawLine(this.actualData,this.planData,this.xList);
                      });
                  }
              })
          },
          drawLine(actualData,planData,xList){
              // 基于准备好的dom，初始化echarts实例
              let PrdTaskTrackChart = this.$echarts.init(document.getElementById('PrdTaskTrackChart'))
              PrdTaskTrackChart.resize();//直接加这句即可
              // 绘制图表
              PrdTaskTrackChart.setOption({
                  title: {
                      text: '生产任务跟踪图',
                      subtext: '根据查询条件显示七条线的生产计划与实际检测的对比图',
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
                      trigger: 'axis'
                  },
                  legend: {
                      data: ['实际', '计划'],
                      top:60
                  },
                  xAxis: {
                      type: 'category',
                      boundaryGap: false,
                      data: xList
                  },
                  yAxis: {
                      type: 'value',
                      axisLabel: {
                          formatter: '{value}'
                      }
                  },
                  series: [
                      {
                          name: '实际',
                          type: 'line',
                          top:50,
                          data: actualData,
                          // markPoint: {
                          //     data: [
                          //         {type: 'max', name: '最大值'},
                          //         {type: 'min', name: '最小值'}
                          //     ]
                          // },
                          // markLine: {
                          //     data: [
                          //         {type: 'average', name: '平均值'}
                          //     ]
                          // }
                      },
                      {
                          name: '计划',
                          type: 'line',
                          data: planData,
                          // markPoint: {
                          //     data: [
                          //         {name: '周最低', value: -2, xAxis: 1, yAxis: -1.5}
                          //     ]
                          // },
                          // markLine: {
                          //     data: [
                          //         {type: 'average', name: '平均值'},
                          //         [{
                          //             symbol: 'none',
                          //             x: '90%',
                          //             yAxis: 'max'
                          //         }, {
                          //             symbol: 'circle',
                          //             // label: {
                          //             //     position: 'start',
                          //             //     formatter: '最大值'
                          //             // },
                          //             type: 'max',
                          //             name: '最高点'
                          //         }]
                          //     ]
                          // }
                      }
                  ]
              })
          },
          maxNum(lList,rList){
              if (lList.sort((v1, v2) => v2 - v1)[0] > rList.sort((v1, v2) => v2 - v1)[0]) {
                  return lList.sort((v1, v2) => v2 - v1)[0]
              }else {
                  return rList.sort((v1, v2) => v2 - v1)[0]
              }
          }
      }
  }
</script>

<style>
  #PrdTaskTrackChart {
    width: 500px;
    height: 300px;
  }
</style>