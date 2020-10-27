<template>
  <div class="trend-bar">
    <h2>当日检定趋势图</h2>
    <span class="angle angle-ld"></span>
    <span class="angle angle-rd"></span>
    <div ref="barWrap" class="bar">
      <div id="myChart"></div>
    </div>
  </div>
</template>
<script>
import Echart from 'vue-echarts'
import 'echarts/lib/chart/bar'
import echarts from 'echarts'
import {httpAction, getAction} from '@/api/manage'

export default {
  name: 'Trend',
  components: {
    VChart: Echart
  },
  inject: ['getRootSize'],
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
  mounted() {
    this.getTrendList("0");
  },
  watch: {
/*    rootSize(rs) {
      this.resizeChart()
    },*/
    data() {
      this.resizeChart()
    }
  },
  computed: {
    rootSize: vm => vm.getRootSize(),
    trend: vm => ({
      xAxis: {
        type: 'category',
        axisLine: {
          lineStyle: {
            color: '#fff'
          }
        },
        axisTick: {
          show: false
        }
      },
      yAxis: {
        nameTextStyle: {
          color: '#fff'
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#fff'
          }
        },
        axisTick: {
          show: true,
          inside: true
        },
        splitLine: {
          lineStyle: {
            type: 'solid',
            color: 'rgba(251, 251, 251, 0.1)'
          }
        }
      },
      grid: {
        top: 10,
        bottom: 20,
        left: 40,
        right: 0
      },
      dataset: {
        source: vm.data
      },
      series: [
        {
          type: 'bar',
          barWidth: vm.rootSize * 0.4,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#4CDEFB' },
              { offset: 1, color: '#0386F2' }
            ])
          }
        }
      ]
    })
  },
  methods: {
    show(areaId){
      this.getTrendList(areaId);
    },
    resizeChart() {
      if (this.$refs.barWrap) {
        this.$refs.bar.resize(this.$refs.barWrap.clientWidth, this.$refs.barWrap.clientHeight)
      }
    },
    getTrendList(areaId){
      getAction(this.url.detectTrendList, {areaId:areaId}).then((res) => {
        if (res.success) {
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
              color: '#dd2c23'
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
          textStyle: {
            color: '#ffffff'
          },
          data: this.legend,
          x: 'center' //居中显示
        },
        xAxis: [
          {
            type: 'category',
            data: ["1时","2时","3时","4时","5时","6时","7时","8时","9时","10时","11时","12时","13时","14时","15时","16时","17时","18时","19时","20时","21时","22时","23时","24时"],
            axisPointer: {
              type: 'shadow'
            },
            axisLabel : {
              textStyle: {
                color: '#ffffff'
              }
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
              formatter: '{value}',
              textStyle: {
                color: '#ffffff'
              }
            },
            }
        ],
        series: this.series
      },true);
    }
  }
}
</script>
<style lang="less" scoped>
 #myChart{
/*   padding: 0;
   margin: 0;
   height: 200px;*/
   width: 100%;
   height: 100%;
   padding: 0;
   margin: 0;
 }
.echarts {
  width: 100%;
  height: 100%;
}
.trend-bar {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('~@/assets/right/bg_trend.png') no-repeat;
  background-size: contain;
  h2 {
    position: absolute;
    top: 0.33rem;
    left: 12.2rem;
    font-size: 0.45rem;
    font-family: Source Han Sans CN;
    font-weight: bold;
    color: #00fff1;
    line-height: 0.6rem;
  }
  .angle {
    display: block;
    position: absolute;
    bottom: 0.05rem;
    width: 1rem;
    height: 1rem;
  }
  .angle-ld {
    left: 0.15rem;
    background: url('~@/assets/right/left_down.png') no-repeat;
    background-size: contain;
  }
  .angle-rd {
    right: 0.1rem;
    background: url('~@/assets/right/right_down.png') no-repeat;
    background-size: contain;
  }
  .bar {
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    padding: 1.3rem 0.9rem 0.5rem 0.8rem;
  }
}
</style>
