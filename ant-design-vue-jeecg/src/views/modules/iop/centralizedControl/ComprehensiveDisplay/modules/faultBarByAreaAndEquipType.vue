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
<!--          <pie title="饼图" :height="height" :dataSource="faultData" @click="handleClick"/>-->
          <bar title="设备类型区域分析数据" :dataSource="barData" :height="height" @click="handleClick"/>
        </div>
      </a-col>
    </a-col>
    <a-col :span="24">
      <a-col :xxl="24" :md="24" :sm="24">
        <a-textarea :rows="3" placeholder="..." v-model="resultMessge" :readOnly="readonly"/>
      </a-col>
    </a-col>

  </a-modal>
    <intelligentSimpleEquipNoPie ref="barModal"></intelligentSimpleEquipNoPie>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Bar from '@/components/chart/Bar'
  import BarMultid from '@/components/chart/BarMultid'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import intelligentSimpleEquipNoPie from "./intelligentSimpleEquipNoPie";
  export default {
    name: "intelligentAnalysisModal",
    mixins:[ChartEventMixins],
    components: {
      Bar,
      BarMultid,
      intelligentSimpleEquipNoPie
    },
    data () {
      return {
        visible:false,
        title:"设备类型故障统计",
        id:'',
        readonly:true,
        resultMessge:'',
        paramData:{},
        areaId:'',
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
            title:'设备名称',
            align:"center",
            dataIndex: 'x'
          },
          {
            title:'频次',
            align:"center",
            dataIndex: 'y'
          },
        ],
        dataSource:[],
        url: {
          list: "/faultMonitoring/faultMonitoring/detailEquipByAreaIdAndEquipNO",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.paramData=param;
        this.visible=true;
        this.title='同类设备故障对比('+param.startDate+'-'+param.endDate+')'
        this.getPieData(param);
      },
      handleCancel () {
        this.visible = false;
      },
      getPieData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, {
          endDate:param.endDate,
          startDate:param.startDate,
          areaId:param.areaId,
          equipType:param.equipType
        }).then((res) => {
          if (res.success) {
            console.log("barAREA------",res.result)
            var pieData = [];
            var data = res.result.result.data.data;
            console.log(data)
            let total = 0;
            for (var i = 0; i < data.length; i++) {
              //this.pieData.push(this.pieData(this.paramData.mapData[i]));
              if (i <= 9) {
                pieData.push(this.pieDatas(data[i].name, data[i].value, data[i].equipNO, data[i].areaId));
              }else {
                total += data[i].value;
              }
            }
            if (data.length > 10){
              pieData.push(this.pieDatas("其他", total,"",""));
            }
            if (data.length > 0){
              this.resultMessge = '通过对比，' + data[0].name + '发生故障次数较多，可以重点进行巡检、检修。'
            }
            this.barData = pieData;

            this.dataSource = pieData;
            console.log(this.dataSource);
            //this.resultMessge='通过对比，'+res.result.mapData[0].x+'故障频率较高，可以重点进行巡检、检修。';
            // this.createAreaLinePie(res.result)
          }
        })
      },
      pieDatas(name,value,equipNO,areaId){
        let d = {
          x: name,
          y:value,
          equipNO:equipNO,
          areaId:areaId
        }
        return d;
      },
      handleClick(event, chart) {
        if (event.data._origin.x != '其他') {
          console.log(event)
          console.log(event.data._origin)
          let dataParamTemp = {
            areaId: event.data._origin.areaId,
            equipNO: event.data._origin.equipNO
          }
          this.paramData = Object.assign(this.paramData, dataParamTemp);
          console.log('this.paramData----', this.paramData)
          var param = {
            areaId: this.paramData.areaId,
            classify: "day",
            endDate: this.paramData.endDate,
            equipCateg: "",
            equipNO: this.paramData.equipNO,
            equipNo: this.paramData.equipNO,
            equipType: this.paramData.equipType,
            startDate: this.paramData.startDate,
            equipName: this.paramData.item + '-' +  event.data._origin.x
          }
          this.$refs.barModal.show(param);
          //this.$refs.barModal.show(this.paramData);
          // this.$refs.barModal.show(this.paramData);
        }
      },
    }
  }
</script>