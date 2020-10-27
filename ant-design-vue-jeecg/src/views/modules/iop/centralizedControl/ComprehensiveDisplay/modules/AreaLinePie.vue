<template>
  <!--流水线故障占比饼图-->
  <div>
  <div id="pieOne" style="width: 100%;height:300px;"></div>

    <faultAnalysis ref="modalForm" ></faultAnalysis>

  </div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  import faultAnalysis from "./faultAnalysis";
  export default {
    name:"FailureEquipmentBar",
    components: {
      faultAnalysis
    },
    data(){
      return{
        paramData:{},
        paramss:{},
        url: {
          list: "/faultMonitoring/faultMonitoring/areaLinePie",
        }
      }
    },
    mounted () {
      // this.createAreaLinePie();
    },
    methods:{
      getPieData(param){
        console.log("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",this.paramss)
        // console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
            // console.log("返回值11111",res.result)
            this.createAreaLinePie(res.result)
            this.paramData = res.result;
            console.log(param)
            this.paramData = Object.assign({startDate:param.startDate,endDate:param.endDate},this.paramData);
            //构造数据
            
            console.log("ggogogogog",this.paramData.mapData);
          }
        })
      },
      modalShow(param){
        console.log('param---',param)
        this.$refs.modalForm.show(param);
      },
      createAreaLinePie(result){
        let echarts = require('echarts');
        let that=this;
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('pieOne'));
        var name = result.faultNameList;
        var faultData = result.mapData;
        var option = {
          title: {
            text: '流水线故障占比',
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
        myChart.on('click', function (param){
          that.modalShow(that.paramData)
        });
      }
    }
  }
</script>