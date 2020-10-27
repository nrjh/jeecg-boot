<template>
  <div ref="pieWrap" class="pie">
    <v-chart ref="pie" :options="pie" />
  </div>
</template>
<script>
import Echart from 'vue-echarts'
import 'echarts/lib/chart/pie'

export default {
  name: 'FaultPie',
  props: {
    needPadding: {
      type: Boolean,
      default: false
    },
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
      title: "",
      data: [],
      names: [],
    }
  },
  watch: {
    rootSize(val) {
      this.resizeChart()
    },
    dataList(newVal,oldVal) {
      this.data = newVal.data;
      this.title = newVal.title;
      if(newVal.names.length<=11){
        this.names = newVal.names;
      }
      // this.names = newVal.names;
      // this.names = [];
    }
  },
  mounted() {

  },
  computed: {
    rootSize: vm => vm.getRootSize(),
    pie: vm => ({
      tooltip: {
        padding: [vm.rootSize * 0.18, vm.rootSize * 0.45],
        borderColor: '#4CDEFB',
        borderWidth: 1,
        backgroundColor: '#1C3343',
        textStyle: {
          color: '#fff',
          fontSize: vm.rootSize * 0.35,
          fontWeight: 400,
          fontfamily: 'Source Han Sans CN',
          lineHeight: vm.rootSize * 0.6
        },
        formatter(params) {
          return `${params.data.name} <br/>故障数：${params.data.value} <br/>占比：${params.percent}%`
        }
      },
      legend: {
        orient: 'vertical',
        top: vm.needPadding ? vm.rootSize * 1.3 : vm.rootSize * 0.1,
        left: 0,
        itemWidth: vm.rootSize * 0.3,
        itemHeight: vm.rootSize * 0.2,
        itemGap: vm.rootSize * 0.35,
        textStyle: {
          color: '#E4E9FF',
          fontSize: vm.rootSize * 0.3,
          fontFamily: 'Source Han Sans CN',
          fontWeight: 400
        },
        data: vm.names
      },
      dataset: {
        source: vm.data
      },
      series: [
        {
          type: 'pie',
          radius: ['70%', '100%'],
          top: vm.rootSize * 1.3,
          left: vm.needPadding ? '50%' : '',
          bottom: vm.rootSize * 0.2,
          hoverOffset: 4,
          avoidLabelOverlap: false,
          color: [
            '#2BFFF3',
            '#1499FF',
            '#649CEC',
            '#1D7AB0',
            '#B053FF',
            '#18C0BC',
            '#0F8967',
            '#FFEBC2',
            '#E2FA57',
            '#FFC570',
            '#FFB895',
            '#FF7C7C',
            '#F27EA5',
            '#CC68A6'
          ],
          itemStyle: {
            borderWidth: 4,
            borderColor: 'RGBA(2, 28, 45, 0.9)'
          },
          label: {
            show: true,
            position: 'center',
            formatter(params) {
              return params.dataIndex === 0 ? vm.title : ''
            },
            color: '#0386F2',
            fontSize: vm.rootSize * 0.4,
            fontWeight: 'bold',
            fontFamily: ' Microsoft YaHei',
            lineHeight: vm.rootSize * 0.6
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
.pie {
  width: 33.33%;
  height: 100%;
}
</style>
