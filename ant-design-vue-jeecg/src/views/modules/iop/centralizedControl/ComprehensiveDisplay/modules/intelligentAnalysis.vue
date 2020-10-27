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
    <intelligentBarByArea ref="barArea"></intelligentBarByArea>
  </div>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import Pie from '@/components/chart/Pie'
  import { ChartEventMixins} from '@/components/chart/mixins/ChartMixins'
  import intelligentBarByArea from './intelligentBarByArea'
  export default {
    name: "intelligentAnalysisModal",
    mixins:[ChartEventMixins],
    components: {
      Pie,
      intelligentBarByArea
    },
    data () {
      return {
        visible:false,
        title:"设备类型故障统计",
        url:'',
        id:'',
        readonly:true,
        paramData:{},
        resultMessge:'本月设备故障次数为：569次，A类故障239次，B类故障200次，C类故障100次，同比增加10%',
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
          list: "/faultMonitoring/faultMonitoring/getFailureEquipTypePieByDay",
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
            console.log("返回值11111",res.result)
            this.faultData = res.result.mapData;
            this.dataSource = res.result.mapData;
            if(res.result.mapData.length>=3){
              this.resultMessge='本月设备故障次数为：'+res.result.nowTotal+'次，'
                + res.result.mapData[0].item+'故障'+res.result.mapData[0].count+'次，'
                + res.result.mapData[1].item+'故障'+res.result.mapData[1].count+'次，'
                + res.result.mapData[2].item+'故障'+res.result.mapData[2].count+'次，'
                +'同比增加'+ res.result.yearAddition+'%'
            }else if (res.result.mapData.length>0 &&  res.result.mapData.length<3) {
               this.resultMessge='本月设备故障次数为：'+res.result.nowTotal+'次，'+'同比增加'+ res.result.yearAddition+'%'
            }

          }
        })
      },
      handleClick(event, chart) {
        let equipType=event.data.point.equip_type;
        let dataParamTemp={
          equipType:equipType
        }
        this.paramData= Object.assign(this.paramData, dataParamTemp);
        console.log('this.paramData----',this.paramData)
        this.$refs.barArea.show(this.paramData);
      },
    }
  }
</script>