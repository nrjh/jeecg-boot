<template>
  <!--故障设备报警表-->
  <a-table
    ref="table"
    size="middle"
    bordered
    rowKey="id"
    :columns="columns"
    :dataSource="dataSource"
    :pagination=false
    :scroll="{ y: 350 }"
    >

  </a-table>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name:"FaultEquipmentAlarmTable",
    data(){
      return{
        dataSource: [],
        columns: [
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
            title:'检定线',
            align:"center",
            dataIndex: 'lineName'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName'
          },
          {
            title:'故障频率',
            align:"center",
            dataIndex: 'frequency'
          },
          {
            title:'处理时长',
            align:"center",
            dataIndex: 'totalTime'
          }
        ],
        url: {
          list: "/faultMonitoring/faultMonitoringTable/faultEquipmentAlarmTable",
        }
      }

    },
    mounted () {
      // this.createFailureTotalTimeLine();
    },
    methods:{
      getFaultEquipmentAlarmTableData(param){
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              this.dataSource = res.result;
          }
        })
      },
    }
  }
</script>