<template>
  <v-chart class="pie" :forceFit="true" :height="height" :data="data" :scale="scale" :onClick="handleClick">
    <v-tooltip :showTitle="false" dataKey="item*percent"/>
    <v-axis/>
    <v-legend dataKey="item"/>
    <v-pie position="percent" color="item" :v-style="pieStyle" :label="labelConfig"/>
    <v-coord type="theta"/>
  </v-chart>
</template>
<script>
  const DataSet = require('@antv/data-set')
  import { ChartEventMixins } from '../../../../../../components/chart/mixins/ChartMixins'
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name: 'FaultTypePie',
    mixins: [ChartEventMixins],
    props: {
      title: {
        type: String,
        default: ''
      },
      height: {
        type: Number,
        default: 254
      },
      dataSource: {
        type: Array,
        default: () => [{item: '', count: 0}]
      }
    },
    mounted() {
      this.get(-1)
    },
    methods: {
      show(id){
        console.log("z>1111111111>>>>>" + id)
      },
      get(id){
        getAction(this.url.faultTypeList + "?ids=" + id, {}).then((res) => {
          if (res.success && res.result != null) {
            this.dataSource = res.result;
          }
        })
      }
    },
    data() {
      return {
        scale: [{
          dataKey: 'percent',
          min: 0,
          formatter: '.0%'
        }],
        pieStyle: {
          stroke: '#fff',
          lineWidth: 1
        },
        labelConfig: ['percent', {
          formatter: (val, item) => {
            return item.point.item + ': ' + val
          }
        }],
        url:{
          faultTypeList:"/centralized/rightScreen/faultTypeList"
        }
      }
    },
    computed: {
      data() {
        let dv = new DataSet.View().source(this.dataSource)
        // 计算数据百分比
        dv.transform({
          type: 'percent',
          field: 'count',
          dimension: 'item',
          as: 'percent'
        })
        return dv.rows
      }
    }
  }
</script>
<style scoped>
  .pie{
    margin: 0;
    padding: 0;
  }
</style>