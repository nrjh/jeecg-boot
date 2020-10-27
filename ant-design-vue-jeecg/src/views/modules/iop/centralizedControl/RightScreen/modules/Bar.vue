<template>
  <div id="myChart" :style="{width: '1100px', height: '400px'}" class="bar"></div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
        name: "Bar",
        mounted(){
          this.get(-1);
        },
      data() {
        return {
          url:{
            detectTrendList:"/centralized/rightScreen/detectTrendList"
          },
          legend:[],
          series:[],
          max:0,
          step:0
        }
      },
        methods: {
          show(id){
            console.log("z>>>>kkkkkkkkkkkkkkkk>>" + id)
            this.get(id)
          },
          get(id){
            getAction(this.url.detectTrendList + "?areaId=" + id, {}).then((res) => {
              if (res.success && res.result != null) {
                console.log(res)
                this.series = res.result.series;
                this.legend = res.result.legend;
                this.max = res.result.max;
                this.step = res.result.step
                this.drawLine();
              }
            })
          },
          drawLine(){
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
          // 绘制图表
          myChart.setOption({
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'cross',
                crossStyle: {
                  color: '#999'
                }
              },
            },
            toolbox: {
              feature: {
                dataView: {show: false, readOnly: false},
                magicType: {show: false, type: ['line', 'bar']},
                restore: {show: false},
                saveAsImage: {show: false}
              }
            },
            legend: {
              data: this.legend,
              x: 'center' //居中显示
            },
            xAxis: [
              {
                type: 'category',
                data: ["1时","2时","3时","4时","5时","6时","7时","8时","9时","10时","11时","12时","13时","14时","15时","16时","17时","18时","19时","20时","21时","22时","23时","24时"],
                axisPointer: {
                  type: 'shadow'
                }
              }
            ],
            yAxis: [
              {
                type: 'value',
                name: '数量',
                min: 0,
                max: parseInt(this.max),
                interval: parseInt(this.step),
                axisLabel: {
                  formatter: '{value} 个'
                }
              }
            ],
            series: this.series
          },true);
          }
      }
    }
</script>

<style scoped>
.bar{

}
</style>