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


    <a-col :span="24">
      <a-col :xxl="8" :md="8" :sm="24">
        <div class="card" >
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false"
            >
          </a-table>
        </div>
      </a-col>
      <a-col :xxl="16" :md="16" :sm="24">
        <div class="card" :bordered="false">
          <pie title="" :height="height" :dataSource="faultData" @click="handleClick"/>
        </div>
      </a-col>
    </a-col>
    <a-col :span="24">
      <a-col :xxl="24" :md="24" :sm="24">
        <a-textarea :rows="3" placeholder="..." v-model="resultMessge" readonly/>
      </a-col>
    </a-col>

  </a-modal>
    <faultBarByAreaAndEquipType ref="faultBarByArea"></faultBarByAreaAndEquipType>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Pie from '@/components/chart/Pie'
  import BarMultid from '@/components/chart/BarMultid'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import faultBarByAreaAndEquipType from "./faultBarByAreaAndEquipType";
  export default {
    name: "faultPieByArea",
    mixins:[ChartEventMixins],
    components: {
      Pie,
      BarMultid,
      faultBarByAreaAndEquipType
    },
    data () {
      return {
        paramData:[],
        faultData:[],
        visible:false,
        title:"设备类型故障统计",
        id:'',
        resultMessge: '',
        barData:[],
        height:400,
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
            title:'设备类型',
            align:"center",
            dataIndex: 'item'
          },
          {
            title:'频次',
            align:"center",
            dataIndex: 'count'
          },
        ],
        dataSource:[],
        url: {
          list: "/faultMonitoring/faultMonitoring/detailEquipTypeByAreaId",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.paramData = param;
        this.visible=true;
        this.title='区域内设备故障分布('+param.startDate+'-'+param.endDate+')'
        this.getPieData(param);
      },
      handleCancel () {
        this.visible = false;
      },
      getPieData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, {
          startDate:param.startDate,
          endDate:param.endDate,
          areaId:param.areaId
        }).then((res) => {
          if (res.success) {
            console.log("barAREA------",res.result)
            var pieData = [];
            var data = res.result.result;
            let total = 0;
            for (var i = 0; i < data.mapData.length; i++){
              //this.pieData.push(this.pieData(this.paramData.mapData[i]));
              if (i <= 9) {
                pieData.push(this.pieDatas(data.mapData[i].item, data.mapData[i].count, data.mapData[i].equipType, data.mapData[i].areaId));
              }else{
                total += this.paramData.mapData[i].value;
              }
            }
            if (data.mapData.length > 10){
              pieData.push(this.pieDatas("其他", total,"",""));
            }
            if (data.mapData.length > 0){
              this.resultMessge = '通过对比，' + data.mapData[0].item + '发生故障次数较多，可以重点进行巡检、检修、甚至改造';
            }
            this.faultData = pieData;

            this.dataSource = pieData;
            console.log(this.dataSource);
            console.log(this.faultData)
            // this.createAreaLinePie(res.result)
          }
        })
      },
      pieDatas(name,value,equipType,areaId){
        let d = {
          item: name,
          count:value,
          equipType:equipType,
          areaId:areaId
        }
        return d;
      },
      handleClick(event, chart) {
        if (event.data.point.item != '其他') {
          console.log('event----', event)
          console.log('chart----', chart)
          let dataParamTemp = {
            areaId: event.data.point.areaId,
            equipType: event.data.point.equipType,
            item:event.data.point.item
          }
          this.paramData = Object.assign(this.paramData, dataParamTemp);
          console.log(this.paramData);
          this.$refs.faultBarByArea.show(this.paramData);
        }
      },
    }
  }
</script>