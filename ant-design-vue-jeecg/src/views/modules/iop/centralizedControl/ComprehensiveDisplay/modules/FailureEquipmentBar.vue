<template>
  <div id="main" style="width: 100%;height:400px;"></div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name:"FailureEquipmentBar",
    data(){
      return{
        url: {
          list: "/faultMonitoring/faultMonitoring/list",
        }
      }

    },
    // mounted () {
    //   this.getBarData();
    // },
    methods:{
      getBarData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              // console.log("返回值11111",res.result)
            this.createFailureEquipmentBar(res.result)
          }
        })
      },
      createFailureEquipmentBar(result){
        let echarts = require('echarts');
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('main'));
        var date = [];
        var f1 = [];
        var f2 = [];
        var f3 = [];
        var f4 = [];
        var f5 = [];
        for(var i = 0; i<result.length; i++){
          date.push(result[i].date);
          // if(result[i].single != null){
          //   f1.push(result[i].single)
          // }else {
          //   f1.push("0")
          // }
          f1.push(result[i].single == null? 0 : result[i].single)
          f2.push(result[i].triphase == null? 0 : result[i].triphase)
          f3.push(result[i].mutual == null? 0 : result[i].mutual)
          f4.push(result[i].terminal == null? 0 : result[i].terminal)
          f5.push(result[i].storage == null? 0 : result[i].storage)
        }
        console.log("x轴1111111111",date);
        let option = {
          title:{
            text:"故障设备汇总",
            x: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
              type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          legend: {
            top: 30,
            data: ['单相表检定线', '三相表检定线', '互感器检定线', '采集终端检定线', '智能化仓储']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: date
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '单相表检定线',
              type: 'bar',
              stack: '故障设备',
              barWidth: 15,
              data: f1
            },
            {
              name: '三相表检定线',
              type: 'bar',
              stack: '故障设备',
              barWidth: 15,
              data: f2
            },
            {
              name: '互感器检定线',
              type: 'bar',
              stack: '故障设备',
              barWidth: 15,
              data: f3
            },
            {
              name: '采集终端检定线',
              type: 'bar',
              stack: '故障设备',
              barWidth: 15,
              data: f4
            },
            {
              name: '智能化仓储',
              type: 'bar',
              stack: '故障设备',
              barWidth: 15,
              data: f5
            },
          ]
        };
        myChart.setOption(option);
        window.onresize = function () {
          myChart.resize();
        }
      }
    }
  }
</script>