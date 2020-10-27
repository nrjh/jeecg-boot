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
        <div class="card" :bordered="false" >
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
          <pie title="饼图" :height="height" :dataSource="faultData" @click="handleClick"/>
        </div>
      </a-col>
    </a-col>
    <a-col :span="24">
      <a-col :xxl="24" :md="24" :sm="24">
        <a-textarea :rows="3" placeholder="..." v-model="resultMessge" :readOnly="readonly"/>
      </a-col>
    </a-col>
  </a-modal>
    <intelligentEquipNoFaultLine ref="barArea"></intelligentEquipNoFaultLine>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Pie from '@/components/chart/Pie'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import intelligentEquipNoFaultLine from './intelligentEquipNoFaultLine'
  export default {
    name: "intelligentAnalysisModal",
    mixins:[ChartEventMixins],
    components: {
      Pie,
      intelligentEquipNoFaultLine
    },
    data () {
      return {
        visible:false,
        title:"单一设备故障分布",
        id:'',
        readonly:true,
        paramData:{},
        resultMessge:'',
        faultData:[],
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
            title:'故障名称',
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
          list: "/faultMonitoring/faultMonitoring/detailFaultByEquipNOAndAreaId",
        }
      }
    },
    created () {
    },
    methods: {
      show (param) {
        this.resultMessge = ''
        this.paramData=param;
        console.log("2222222222222222222222222222222222222222222",param)
        this.visible=true;
        this.title='单一设备故障分布('+param.startDate+'-'+param.endDate+')'
        this.getPieData(param);
      },
      handleCancel () {
        this.visible = false;
      },
      getPieData(param){
        // console.log("参数11111",param)
        getAction(this.url.list, param).then((res) => {
          if (res.success) {
            console.log("res.result.data------------2222222222222222222222222222222222222",res.result.result)
            this.faultData = res.result.result.data.data;
            var top = res.result.result.data.top;
            if (this.faultData != ''){
              if (top.length > 0) {
                this.resultMessge = '通过对比，' + param.equipName + '，';
                for (var i = 0; i < top.length; i++) {
                  this.resultMessge += top[i].name + top[i].prop + "%，"
                }
                this.resultMessge += '可在检修、巡检时，对该故障相关的硬件进行重点的检查';
              }
            }
            this.dataSource = res.result.result.data.data;
          }
        })
      },
      handleClick(event, chart) {
        let faultName=event.data.point.item;
        let dataParamTemp={
          faultName:faultName,
          equipName:  this.paramData.equipName + '-' + event.data.point.item
        }
        this.paramData= Object.assign(this.paramData, dataParamTemp);
        console.log('this.paramData----',this.paramData)
        this.$refs.barArea.show(this.paramData);
      },
    }
  }
</script>