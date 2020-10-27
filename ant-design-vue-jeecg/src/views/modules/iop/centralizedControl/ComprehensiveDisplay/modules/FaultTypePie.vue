<template>
  <!--故障类型饼图-->
  <div id="pieThree" style="width: 100%;height:300px;"></div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name:"FaultType",
    data(){
      return{
        url: {
          list: "/faultMonitoring/faultMonitoring/faultType",
        }
      }

    },
    mounted () {
      // this.createAreaLinePie();
    },
    methods:{
      getPieData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              // console.log("返回值11111",res.result)
            this.createAreaLinePie(res.result)
          }
        })
      },
      createAreaLinePie(result){
        let echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('pieThree'));
        var name = result.faultNameList;
        var faultData = result.mapData;
        var option = {
          title: {
            text: '故障类型',
            subtext: '',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            data: name
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: '55%',
              center: ['50%', '60%'],
              data: faultData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        myChart.setOption(option);
      },

      getsData(_rowData) {
        var rowData = JSON.parse(JSON.stringify(_rowData))
        var sum = rowData.reduce(function (o, v, i) {
          o += parseFloat(v.value);
          return o;
        }, 0)
        if (isNaN(sum) || sum == 0) {
          return []
        }
        rowData.sort(function (a, b) {
          return a.value - b.value;
        }).reverse();
        var val = 0;//其他的相加
        for (var i = rowData.length - 1; i > 0; i--) {
          var _row = rowData[i], sub;
          sub = _row.value / sum;
          //如果为0或者小于.05的比例就将此元素移除
          if (sub < 0.03) {
            rowData.length = i;//去掉最后一个
            val += parseFloat(_row.value);
          }
        }
        //在后面追加一个其他的项
        if (val > 0) {
          rowData[length].name = "其他";
          rowData[length].value = val;
        }
        return rowData;
      }
    }
  }
</script>