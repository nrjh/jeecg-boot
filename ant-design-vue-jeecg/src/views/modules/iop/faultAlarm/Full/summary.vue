<template>
  <section class="summary-show">
    <div class="left">
      <h3>{{title}}待检</h3>
      <span class="icon">
        <img width="100%" height="100%" src="@/assets/right/icon_month.png" />
      </span>
    </div>
    <div class="right">
      <div class="top">
        <div class="data">
          <h3>{{title}}待检总数</h3>
          <ul class="num-list">
            <li class="num" v-for="(n, i) in num1 + ''" :key="i">
              {{ n }}
            </li>
          </ul>
        </div>
        <span class="line">
          <img width="100%" height="100%" src="@/assets/right/icon_line.png" />
        </span>
        <div class="data">
          <h3>{{title}}完成检定数</h3>
          <ul class="num-list">
            <li class="num" v-for="(n, i) in num2 + ''" :key="i">
              {{ n }}
            </li>
          </ul>
        </div>
      </div>
      <div class="bottom">
        <span class="title">{{title}}完成率</span>
        <span ref="barWrap" class="bar">
          <v-chart ref="bar" :options="bar" />
        </span>
        <span class="pre">{{cur}}%</span>
      </div>
    </div>
  </section>
</template>
<script>
import Echart from 'vue-echarts'
import 'echarts/lib/chart/bar'

export default {
  name: 'Summary',
  props: {
    dataList:{
      type:Array,
      required:true
    }
  },
  components: {
    VChart: Echart
  },
  inject: ['getRootSize'],
  data() {
    return {
      num1: "00001",
      num2:"1111",
      total: 100,
      cur: "1",
      title:"当日",
    }
  },
  computed: {
    rootSize: vm => vm.getRootSize(),
    bar: vm => ({
      yAxis: {
        type: 'category',
        show: false
      },
      xAxis: {
        type: 'value',
        show: false
      },
      grid: {
        top: 0,
        bottom: 0,
        left: 0,
        right: 0
      },
      series: [
        {
          type: 'bar',
          data: [vm.cur],
          stack: '总量',
          barWidth: vm.rootSize * 0.15,
          color: '#098EF3'
        },
        {
          type: 'bar',
          data: [vm.total],
          stack: '总量',
          color: '#00365F'
        }
      ]
    })
  },
  watch: {
    rootSize() {
      this.resizeChart()
    },
    dataList(newVal,oldVal) {
      console.log("zzzzzzzzxxxxxxxxxxx",newVal);
      this.title = newVal.title;
      this.cur = newVal.cur;
      this.num1 = newVal.num1
      this.num2 = newVal.num2
    }
  },
  methods: {
    resizeChart() {
      if (this.$refs.barWrap) {
        this.$refs.bar.resize(this.$refs.barWrap.clientWidth, this.$refs.barWrap.clientHeight)
      }
    },
  }
}
</script>
<style lang="less" scoped>
.echarts {
  width: 100%;
  height: 100%;
}
.summary-show {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: flex-end;
  h3 {
    margin: 0 0 0.4rem 0;
    font-size: 0.35rem;
    font-family: Source Han Sans CN;
    font-weight: 400;
    color: #00cfcd;
  }
  .left {
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-end;
    .icon {
      width: 2.69rem;
      height: 1.8rem;
    }
  }
  .right {
    padding: 0 0 0 0.15rem;
    display: inline-flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    .top {
      display: inline-flex;
      align-items: flex-start;
      .data {
        .num-list {
          padding: 0;
          margin: 0;
          list-style: none;
          .num {
            display: inline-block;
            width: 0.75rem;
            height: 0.9rem;
            background: url('~@/assets/right/num_bg.png') no-repeat;
            background-size: contain;
            font-size: 0.75rem;
            line-height: 0.9rem;
            font-family: Source Han Sans CN;
            font-weight: bold;
            font-style: italic;
            color: #ffffff;
            text-align: center;
            & + .num {
              margin: 0 0 0 0.2rem;
            }
          }
        }
      }
      .line {
        display: inline-block;
        margin: 0 0.57rem 0 0.6rem;
        height: 2.48rem;
        width: 0.03rem;
      }
    }
    .bottom {
      display: inline-flex;
      align-items: center;
      justify-content: flex-start;
      margin: -0.2rem 0 0.2rem 0;
      span {
        font-size: 0.35rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #ffffff;
      }
      .bar {
        display: inline-block;
        margin: 0 0.43rem 0 0.3rem;
        width: 8.8rem;
        height: 0.15rem;
      }
    }
  }
}
</style>
