<template>
  <div>
  <a-modal
    :title="title"
    :visible="visible"
    :width="1200"
    :bodyStyle="bodyStyle"
    @cancel="handleCancel"
    :footer="null"
    cancelText="关闭">

    <!--故障设备智能分析1 饼图-->

<!--      <div id="pieOne" style="width: 100%;height:300px;">-->
<!--      </div>-->

    <a-col :span="24">
      <a-col :xxl="8" :md="8" :sm="24">
        <div class="card" :bordered="false" >
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false">
          </a-table>
        </div>
      </a-col>
      <a-col :xxl="16" :md="16" :sm="24">
        <div class="card" :bordered="false">
          <pie title="检定线故障统计" :height="height" :dataSource="faultData" @click="handleClick"/>
        </div>
      </a-col>
    </a-col>
    <a-col :span="24">
      <a-col :xxl="24" :md="24" :sm="24">
        <a-textarea :rows="3" placeholder="..." v-model="resultMessge" readonly/>
      </a-col>
    </a-col>
  </a-modal>
    <faultPieByArea ref="faultPieByArea"></faultPieByArea>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Pie from '@/components/chart/Pie'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import faultPieByArea from "./faultPieByArea";
  export default {
    name: "faultAnalysis",
    mixins:[ChartEventMixins],
    components: {
      Pie,
      faultPieByArea
    },
    data () {
      return {
        visible:false,
        title:"设备类型故障统计",
        id:'',
        paramData:{},
        resultMessge:'',
        faultData:[],
        height:270,
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight-200)+"px"
        },
        tableScroll:{y :400},
        columns:[
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'流水线',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'频次',
            align:"center",
            dataIndex: 'value'
          },
        ],
        dataSource:[],
        url: {
          list: "/faultMonitoring/faultMonitoring/detailFaultComparisonByDay",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.paramData=param;
        var pieData = [];
        this.visible=true;
        this.title='设备类型故障统计('+param.startDate+'-'+param.endDate+')'
        var  total = 0;
        for (var i = 0; i < this.paramData.mapData.length; i++) {
          //this.pieData.push(this.pieData(this.paramData.mapData[i]));
          if (i <= 9) {
            pieData.push(this.pieDatas(this.paramData.mapData[i].name, this.paramData.mapData[i].value));
          }else{
            total += this.paramData.mapData[i].value;
          }
        }
        if (this.paramData.mapData.length > 10){
          pieData.push(this.pieDatas("其他", total));
        }
        if (this.paramData.mapData.length > 0){
          this.getPieData('通过对比，' + this.paramData.mapData[0].name + '频率较高，');
        }
        this.faultData = pieData;
        this.dataSource = param.mapData;
      },
      pieDatas(name,value){
        let d = {
          item: name,
          count:value
        }
        return d;
      },
      handleCancel () {
        this.visible = false;
      },
      getPieData(data){
        // console.log("参数11111",param)
        getAction(this.url.list, {}).then((res) => {
          if (res.success) {
            console.log("返回值11111",res.result)
            //this.faultData = res.result.mapData;
            //this.dataSource = res.result.mapData;
            this.resultMessge = data + '同比' + res.result.msg + res.result.yearAddition;
            // this.createAreaLinePie(res.result)
          }
        })
      },
      handleClick(event, chart) {
        console.log(event);
        if (event.data.point.item != '其他') {
          var areaId;
          for (var i = 0; i < this.paramData.mapData.length; i++) {
            if (this.paramData.mapData[i].name == event.data.point.item) {
              areaId = this.paramData.mapData[i].areaId;
              break;
            }
          }
          console.log(areaId)
          let dataParamTemp = {
            areaId: areaId
          }
          this.paramData = Object.assign(this.paramData, dataParamTemp);
          console.log('this.paramData----', this.paramData)
          this.$refs.faultPieByArea.show(this.paramData);
        }
      },
    }
  }
</script>