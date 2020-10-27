<template>
  <!--备品备件表-->
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
    name:"SparePartTable",
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
            title:'备件名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'消耗量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'库存',
            align:"center",
            dataIndex: 'productQty'
          },
          {
            title:'预警情况',
            align:"center",
            dataIndex: '预警'
          }
        ],
        url: {
          list: "/faultMonitoring/faultMonitoringTable/sparePartTable",
        }
      }

    },
    mounted () {
      // this.createFailureTotalTimeLine();
    },
    methods:{
      getSparePartTableData(param){
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
              this.dataSource = res.result;
          }
        })
      },
    }
  }
</script>