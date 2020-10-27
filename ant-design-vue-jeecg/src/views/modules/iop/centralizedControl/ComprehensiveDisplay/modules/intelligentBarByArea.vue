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
    <intelligentBarByEquipType ref="barModal"></intelligentBarByEquipType>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Bar from '@/components/chart/Bar'
  import BarMultid from '@/components/chart/BarMultid'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import intelligentBarByEquipType from './intelligentBarByEquipType'
  export default {
    name: "intelligentAnalysisModal",
    mixins:[ChartEventMixins],
    components: {
      Bar,
      BarMultid,
      intelligentBarByEquipType
    },
    data () {
      return {
        visible:false,
        title:"设备类型故障统计",
        url:'',
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
            title:'区域',
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
          list: "/faultMonitoring/faultMonitoring/getFailureEquipTypeBarByDay",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.paramData=param;
        this.visible=true;
        this.title='设备类型故障统计('+param.startDate+'-'+param.endDate+')'
        this.getPieData(param);
      },
      handleCancel () {
        this.visible = false;
      },
      getPieData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
            console.log("barAREA------",res.result)
            this.barData = res.result.mapData;
            this.dataSource = res.result.mapData;
            this.resultMessge='通过对比，'+res.result.mapData[0].x+'故障频率较高，可以重点进行巡检、检修。';
            // this.createAreaLinePie(res.result)
          }
        })
      },
      handleClick(event, chart) {
       this.areaId= event.data._origin.area_id
        let dataParamTemp={
          areaId:  this.areaId,
        }
        this.paramData= Object.assign(this.paramData, dataParamTemp);
        console.log('this.paramData----',this.paramData)
        this.$refs.barModal.show(this.paramData);
        // this.$refs.barModal.show(this.paramData);
      },
    }
  }
</script>