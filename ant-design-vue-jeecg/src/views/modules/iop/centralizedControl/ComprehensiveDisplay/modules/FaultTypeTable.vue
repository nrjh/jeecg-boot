<template>
  <!--故障类型表-->
  <a-table
    ref="table"
    size="middle"
    bordered
    rowKey="id"
    :columns="columns"
    :dataSource="dataSource"
    :scroll="{ y: 350 }"
    :pagination=false
    >

  </a-table>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  export default {
    name:"FaultTypeTable",
    data(){
      return{
        dataSource: [],
        // ipagination: {
        //   current: 1,
        //   pageSize: 15,
        //   pageSizeOptions: ['15', '30', '45'],
        //   showTotal: (total, range) => {
        //     return range[0] + "-" + range[1] + " 共" + total + "条"
        //   }
        // },
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
            title:'线体名称',
            align:"center",
            dataIndex: 'lineName'
          },
          {
            title:'报警类型',
            align:"center",
            dataIndex: 'faultLabe'
          },
          {
            title:'故障频率',
            align:"center",
            dataIndex: 'frequency'
          }
        ],
        url: {
          list: "/faultMonitoring/faultMonitoringTable/faultTypeTable",
        }
      }

    },
    mounted () {
      // this.createFailureTotalTimeLine();
    },
    methods:{
      getFaultTypeTableData(param){
        console.log("参数33333",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              console.log("返回值33333",res.result)
              this.dataSource = res.result;
          }
        })
      },
    }
  }
</script>