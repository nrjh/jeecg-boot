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
          <bar title="同类设备故障对比" :dataSource="barData" :height="height" @click="handleClick"/>
        </div>
      </a-col>
    </a-col>
    <a-col :span="24">
      <a-col :xxl="24" :md="24" :sm="24">
        <a-textarea :rows="3" placeholder="..." v-model="resultMessge" :readOnly="readonly"/>
      </a-col>
    </a-col>

  </a-modal>
    <intelligentSimpleEquipNoPie ref="pieModal"></intelligentSimpleEquipNoPie>
    </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Bar from '@/components/chart/Bar'
  import BarMultid from '@/components/chart/BarMultid'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import intelligentSimpleEquipNoPie from './intelligentSimpleEquipNoPie'
  export default {
    name: "intelligentBarByEquipType",
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
        url:'',
        id:'',
        areaId:'',
        equipType:'',
        readonly:true,
        paramData:{},
        resultMessge:'',
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
          list: "/faultMonitoring/faultMonitoring/getFailureEquipNoBarByEquipType",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.visible=true;
        this.paramData=param;
        this.title='设备类型故障统计('+param.startDate+'-'+param.endDate+')'
        this.equipType=param.equipType;
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
            this.resultMessge='通过对比，'+res.result.mapData[0].x+'故障次数较多，现系统已将故障巡检频率调整为3天';
            // this.createAreaLinePie(res.result)
          }
        })
      },
      handleClick(event, chart) {
         let equip_no=event.data._origin.equip_no;
          let dataParamTemp={
            equipNo:  equip_no,
            equipNO:equip_no
          }
        this.paramData= Object.assign(this.paramData, dataParamTemp);
        console.log('this.paramData----',this.paramData)
        this.$refs.pieModal.show(this.paramData);
      },
    }
  }
</script>