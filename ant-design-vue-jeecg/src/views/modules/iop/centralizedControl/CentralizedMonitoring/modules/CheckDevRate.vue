<template>
  <div id="CheckDevRateChart"></div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
      name:"CheckDevRate",
      data(){
          return{
              url:{
                  checkEquipOccupyRate:"/centralized/centralizedMonitoring/checkEquipOccupyRate"
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
              getAction(this.url.checkEquipOccupyRate,queryData).then((res) => {
                  if(res.success){
                      this.$nextTick(() => {
                          // legend
                          let dataLegend = res.result.nameList[0];
                          let dataSource = res.result.nameAndValueList[0];
                          this.drawLine(dataLegend,dataSource);
                      });
                  }

              });
          },
          drawLine(dataLegend,dataSource){
              // 基于准备好的dom，初始化echarts实例
              let CheckDevRateChart = this.$echarts.init(document.getElementById('CheckDevRateChart'))
              // 自适应
              CheckDevRateChart.resize();//直接加这句即可
              // 绘制图表
              CheckDevRateChart.setOption({
                  title: {
                      text: '检定设备占比',
                      subtext: '简介',
                      left: 'center'
                  },
                  tooltip: {
                      trigger: 'item',
                      formatter: '{a} <br/>{b} : {c} ({d}%)'
                  },
                  legend: {
                      orient: 'vertical',
                      left: 'left',
                      data: dataLegend
                  },
                  series: [
                      {
                          name: '数据来源',
                          type: 'pie',
                          radius: '55%',
                          center: ['50%', '60%'],
                          data: dataSource,
                          emphasis: {
                              itemStyle: {
                                  shadowBlur: 10,
                                  shadowOffsetX: 0,
                                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                              }
                          }
                      }
                  ]
              })
          }
      }
  }
</script>
<style>
  #CheckDevRateChart {
    width: 500px;
    height: 300px;
  }
</style>