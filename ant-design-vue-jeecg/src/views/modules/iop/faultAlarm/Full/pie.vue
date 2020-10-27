<template>
  <div class="pie-wrap">
    <div ref="pieWrap" class="pie">
      <v-chart ref="pie" :options="pie" />
    </div>
    <div class="info">
      <div class="head">
        <span class="line"></span>
        {{title}}
      </div>
      <span class="text">
        <span v-if="state == '1'">计划数：</span>
        <span v-if="state == '2'">完成数：</span>
        <span v-if="state == '3'">待检数：</span>
        {{num1}}
      </span>
      <span class="text-line">
         <!--<img width="100%" height="100%" src="@/assets/right/icon_line2.png" />-->
      </span>
      <span class="text">
        <span v-if="state == '1'">完成数：</span>
        <span v-if="state == '2'">设计产能：</span>
        <span v-if="state == '3'">合格数：</span>
        {{num2}}
      </span>
      <span v-if="state == '3'" class="text">
        不合格数：
        {{num3}}
      </span>
    </div>
  </div>
</template>
<script>
import Echart from 'vue-echarts'
import 'echarts/lib/chart/pie'
import echarts from 'echarts'

export default {
  name: 'pie',
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
      //1代表全部，2代表除智能仓储，3代表智能仓储以外的检定线
      state:"1",
      title:"",
      num1:"",
      num2:"",
      num3:"",
      themeColor: {
        zero: [
          new echarts.graphic.LinearGradient(1, 0, 0, 1, [
            { offset: 0, color: '#EFEFEF' },
            { offset: 1, color: '#303030' }
          ]),
          '#2A3A46'
        ],
        default: [
          new echarts.graphic.LinearGradient(1, 0, 0, 1, [
            { offset: 0, color: '#4CDEFB' },
            { offset: 1, color: '#0386F2' }
          ]),
          '#082242'
        ]
      },
      data: [{value:2},{value: 5}]
    }
  },
  watch: {
    rootSize(val) {
      this.resizeChart()
    },
    dataList(newVal,oldVal) {
      console.log("zzzzzzzzxxxxxxxxxxx",newVal);
      this.title = newVal.title;
      this.num1 = newVal.num1
      this.num2 = newVal.num2
      this.state = newVal.state
      this.num3 = newVal.num3
      this.data[0].value =  newVal.data[0].value;
      this.data[1].value =  newVal.data[1].value;
    }
  },
  computed: {
    rootSize: vm => vm.getRootSize(),
    pie: vm => ({
      dataset: {
        source: vm.data
      },
      series: [
        {
          type: 'pie',
          radius: ['80%', '100%'],
          hoverAnimation: false,
          avoidLabelOverlap: false,
          color: vm.data[0].value === 0 ? vm.themeColor.zero : vm.themeColor.default,
          label: {
            show: true,
            position: 'center',
            formatter(params) {
              return params.dataIndex === 0 ? `${parseInt(params.percent)}%` : ''
            },
            color: vm.data[0].value === 0 ? '#D3D3D3' : '#0386F2',
            fontFamily: 'Source Han Sans CN',
            fontWeight: 'bold',
            fontSize: vm.rootSize * 0.54
          }
        }
      ]
    })
  },
  methods: {
    resizeChart() {
      if (this.$refs.pieWrap) {
        this.$refs.pie.resize(this.$refs.pieWrap.clientWidth, this.$refs.pieWrap.clientHeight)
      }
    }
  }
}
</script>
<style lang="less" scoped>
.echarts {
  width: 100%;
  height: 100%;
}
.pie-wrap {
  display: flex;
  align-items: flex-end;
  .pie {
    margin: 0 0.45rem 0 0;
    width: 2.05rem;
    height: 2.05rem;
  }
  .info {
    font-size: 0.5rem;
    font-family: Source Han Sans CN;
    font-weight: 500;
    .head {
      .line {
        margin: 0 0.28rem 0 0;
        display: inline-block;
        width: 0.1rem;
        height: 0.45rem;
        background: #00fff1;
      }
      color: #e4e9ff;
    }
    .text {
      display: block;
      color: #fff;
      font-weight: 400;
      line-height: 0.75rem;
    }
    .text-line {
      display: block;
      height: 0.03rem;
      width: 100%;
      background: linear-gradient(90deg, rgba(0, 204, 255, 0.97) 44%);
    }
  }
}
</style>
