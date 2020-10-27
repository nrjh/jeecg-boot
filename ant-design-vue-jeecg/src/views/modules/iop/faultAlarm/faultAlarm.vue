<template>
  <a-card :bordered="false" >
    <div>
      <a-row :gutter="16">
        <a-col :span="12">
          <div>
            <form>
              <a-row >
                <a-col :span="10">
                  <a-form-item label="故障报警时间" :labelCol="{span:9}" :wrapperCol="{span:15}">
                    <j-date  placeholder="请选择开始时间" v-model="queryParam.alarmTimeStart"  :trigger-change="true" style="width: 100%"
                             @change="clickDate"/>
                  </a-form-item>
                </a-col>
                <a-col :span="10">
                  <a-form-item label="至" :labelCol="{span:9}" :wrapperCol="{span:15}">
                    <j-date   placeholder="请选择结束时间"   v-model="queryParam.alarmTimeEnd" :trigger-change="true" style="width: 100%"
                              @change="clickDate"/>
                  </a-form-item>
                </a-col>
                <a-col :span="4">
                  <a-form-item style="float: right" >
                    <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
                  </a-form-item>
                </a-col>
              </a-row>

            </form>
          </div>
          <div>
            <a-row>
              <a-col :span="24">
                <div id="layOutChart" style="padding-top: 9%">
                  <a-layout>
                    <a-layout-header :style="'font-size: 2px;height: 30px;line-height: 35px;    background: white;padding-left: 1%;'">分级报警</a-layout-header>
                    <a-layout>
                      <a-layout-content>
                        <a-table
                          className='ListTable'
                          style="word-break:break-all"
                          ref="table"
                          size="middle"
                          bordered
                          rowKey="id"
                          :pagination="false"
                          :columns="columnsA"
                          :dataSource="dataSourceA"
                          :scroll="{ y: 250 }"
                        >
                        </a-table>
                      </a-layout-content>
                    </a-layout>
                    <a-layout>
                      <a-layout-content>
                        <a-table
                          style="word-break:break-all"
                          ref="table"
                          size="middle"
                          bordered
                          rowKey="id"
                          :pagination="false"
                          :columns="columnsB"
                          :dataSource="dataSourceB"
                          :scroll="{ y: 250 }"
                        >
                        </a-table>
                      </a-layout-content>
                    </a-layout>
                    <a-layout>
                      <a-layout-content>
                        <a-table
                          style="word-break:break-all;font-size: 1px;"
                          ref="table"
                          size="middle"
                          bordered
                          :pagination="false"
                          rowKey="id"
                          :columns="columnsC"
                          :dataSource="dataSourceC"
                          :scroll="{ y: 250 }"
                        >
                        </a-table>
                      </a-layout-content>
                    </a-layout>
                  </a-layout>
                </div>
              </a-col>
              <a-col :span="12">
<!--                <a-layout>
                  <a-layout-header :style="'background: rgb(6 139 136 / 31%);font-size: 1px;height: 30px;line-height: 35px;'">隔离报警</a-layout-header>
                  <a-layout-content>
                    <a-table
                      style="word-break:break-all;font-size: 1px;"
                      ref="table"
                      size="middle"
                      bordered
                      rowKey="id"
                      :pagination="false"
                      :columns="columns"
                      :dataSource="dataSource"
                      :loading="loading"
                      :scroll="{ y: 1000 }"
                    >
                    </a-table>
                  </a-layout-content>
                </a-layout>-->

              </a-col>
            </a-row>
          </div>
        </a-col>
<!--   左右屏分割线      -->
        <a-col :span="12">
          <div class="table-page-search-wrapper">
            <div style="margin-bottom: 8px; height: 30px; font-size:12px; line-height:35px; padding-left:5px;">
                故障工单
                  <span style="float: right;overflow: hidden;">
                  <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                  <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                 </span>
            </div>
            <a-form :form="form" layout="inline" @keyup.enter.native="searchQuery">
              <a-row :gutter="8" style="margin-left: 3px">
                <a-col :span="10">
                  <a-form-item label="故障等级" :labelCol="{span:6}" :wrapperCol="{span:18}">
                    <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]">
                      <a-radio value="A">
                        A
                      </a-radio>
                      <a-radio value="B">
                        B
                      </a-radio>
                      <a-radio value="C">
                        C
                      </a-radio>
                    </a-radio-group>
                  </a-form-item>
                </a-col>
                <a-col :span="7">
                  <a-form-item label="检定线" style="padding-left: 0px" :labelCol="{span:6}" :wrapperCol="{span:18}">
                    <j-dict-select-tag-ora type="list" :trigger-change="true"  v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id"
                                           placeholder="请选择检定线" @change="changeLine"/>
                  </a-form-item>
                </a-col>
                <a-col :span="7">
                  <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:18}">
                    <a-select  v-decorator="[ 'equipId', validatorRules.equipNo]" >
                      <a-select-option v-for="item in equipLists" :key="item.equipId" :value="item.equipNo">
                        {{ item.title }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-row>

              <a-row :gutter="8" style="margin-left: 3px">
                <a-col :span="8">
                  <a-form-item label="申请时间" :labelCol="{span:9}" :wrapperCol="{span:15}">
                    <j-date  placeholder="开始时间" v-decorator="[ 'startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label="至" :labelCol="{span:9}" :wrapperCol="{span:15}">
                    <j-date   placeholder="结束时间"  v-decorator="[ 'endTime', validatorRules.endTime]"  :trigger-change="true" style="width: 100%"/>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-form-item label="工单状态：" :labelCol="{span:9}" :wrapperCol="{span:15}">
                    <j-dict-select-tag type="list"
                                       placeholder=""
                                       :trigger-change="true"
                                       dict-code="ORA_ORD_WORK_ORDER_INFO_PROCESS_STATUS"
                                       v-decorator="['processStatus', validatorRules.processStatus]"/>
                  </a-form-item>
                </a-col>
              </a-row>
<!--              <a-row :gutter="16">-->
<!--                <a-col >-->
<!--                  <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">-->
<!--                  <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>-->
<!--                  <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
<!--                 </span>-->
<!--                </a-col>-->
<!--              </a-row>-->
            </a-form>
          </div>
          <a-table
            style="word-break:break-all"
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :pagination="ipaginationWorkOrder"
            :columns="columnsWorkOrder"
            :dataSource="dataSourceWorkOrder"
            :scroll="tableScroll"
          >
            <span slot="action" slot-scope="index,record">　
              <a @click="apportion(index,record)"><u>分派  </u></a>
              <a @click="redeploy(index,record)">转派  </a>
              <a @click="supervise(index,record)"><u>催办</u></a>
            </span>
          </a-table>
        </a-col>
      </a-row>
    </div>
    <mtEquipAlarmInfo-modal ref="modalForm" @ok="modalFormOk"></mtEquipAlarmInfo-modal>
    <CompleteOrdWorkOrderInfoModal ref="modalDispatching" @ok="modalFormOk"></CompleteOrdWorkOrderInfoModal>
    <OrdWorkOrderRedeployModal ref = "modalRedeploy" @ok="modalFormOk"></OrdWorkOrderRedeployModal>
    <OrdWorkOrderSuperviseModal ref="modalSupervise" @ok="modalFormOk"></OrdWorkOrderSuperviseModal>
  </a-card>
</template>

<script>
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import JDate from '@/components/jeecg/JDate'
  import { filterObj } from '@/utils/util';
  import {httpAction, getAction} from '@/api/manage'
  import { initDictOptionsOra,initDictOptionsIop } from '@/components/dict/JDictSelectUtil'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import MtEquipAlarmInfoModal from '../alarm/record/modules/MtEquipAlarmInfoModal'
  import CompleteOrdWorkOrderInfoModal from '../workOrder/todo/modules/CompleteOrdWorkOrderInfoModal'
  import OrdWorkOrderRedeployModal from './modules/OrdWorkOrderRedeployModal'
  import OrdWorkOrderSuperviseModal from '../workOrder/todo/modules/OrdWorkOrderSuperviseModal'
  export default {
    name: "faultAlarm",
    components: {
      AFormItem,
      ACol,
      ARow,
      JDictSelectTag,
      JDictSelectTagIop,
      JDictSelectTagOra,
      JDate,
      MtEquipAlarmInfoModal,
      CompleteOrdWorkOrderInfoModal,
      OrdWorkOrderRedeployModal,
      OrdWorkOrderSuperviseModal
    },
    data () {
      return {
        form: this.$form.createForm(this),
        type: '',
        processStatus:'',
        description: '故障运维工单表管理页面',
        visibleModel: false,
        radio:'radio',
        queryParam: {},
        queryParamA: {},
        queryParamB: {},
        queryParamC: {},
        queryParamWorkOrder:{},
        dataSource:[],
        dataSourceA: [],
        dataSourceB: [],
        dataSourceC: [],
        dataSourceWorkOrder:[],
        equipLists: [],
        loading: false,
        /* table选中keys*/
        selectedRowKeys: [],
        /* table选中records*/
        selectionRows: [],
        // ipagination: {
        //   current: 1,
        //   pageSize: 15,
        //   pageSizeOptions: ['15', '30', '45'],
        //   total:0,
        //   showTotal: (total, range) => {
        //     return range[0] + "-" + range[1] + " 共" + total + "条"
        //   },
        // },
        // ipaginationA: {
        //   current: 1,
        //   pageSize: 15,
        //   pageSizeOptions: ['15', '30', '45'],
        //   total:0,
        //   showTotal: (total, range) => {
        //     return range[0] + "-" + range[1] + " 共" + total + "条"
        //   },
        // },
        // ipaginationB: {
        //   current: 1,
        //   pageSize: 15,
        //   pageSizeOptions: ['15', '30', '45'],
        //   total:0,
        //   showTotal: (total, range) => {
        //     return range[0] + "-" + range[1] + " 共" + total + "条"
        //   },
        // },
        // ipaginationC: {
        //   current: 1,
        //   pageSize: 15,
        //   pageSizeOptions: ['15', '30', '45'],
        //   total:0,
        //   showTotal: (total, range) => {
        //     return range[0] + "-" + range[1] + " 共" + total + "条"
        //   },
        // },
        ipaginationWorkOrder: {
          current: 1,
          pageSize: 15,
          pageSizeOptions: ['15', '30', '45'],
          total:0,
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
        },
        /* 排序参数 */
        isorter:{
          column: 'alarmTime',
          order: 'desc',
        },
        // 表头
        columns: [
          {
            title:'编号',
            align:"center",
            width: 75,
            style:'font-size:1px;',
            dataIndex: 'alarmNo'
          },
          {
            title:'报警时间',
            align:"center",
            width: 75,
            dataIndex: 'alarmTime'
          },
          {
            title:'故障名称',
            align:"center",
            width: 75,
            dataIndex: 'realReason'
          },
          {
            title:'设备',
            align:"center",
            dataIndex: 'equipName'
          },
        ],

        columnsA: [
          {
            title: '',
            dataIndex: 'equipNo',
            width: 35,
            align:"center",
            customRender: (value, row, index) => {
              let obj = {
                children: "",
                attrs: {}
              }
              if (index === 0) {
                obj = {
                  children: "A类",
                  attrs: { rowSpan: this.dataSource.length }
                }
              }else {
                obj.attrs.colSpan = 0
              }
              return obj
            },
          },
          {
            title:'编号',
            align:"center",
            width: 50,
            dataIndex: 'alarmNo'
          },
          {
            title:'报警时间',
            align:"center",
            width: 70,
            dataIndex: 'alarmTime'
          },
          {
            title:'故障名称',
            align:"center",
            width: 70,
            dataIndex: 'realReason'
          },
          {
            title:'频率',
            align:"center",
            width: 20,
            dataIndex: 'alarmAmount'
          },
        ],

        columnsB: [
          {
            title: '',
            dataIndex: 'equipNo',
            width: 35,
            align:"center",
            customRender: (value, row, index) => {
              let obj = {
                children: "",
                attrs: {}
              }
              if (index === 0) {
                obj = {
                  children: "B类",
                  attrs: { rowSpan: this.dataSource.length }
                }
              }else {
                obj.attrs.colSpan = 0
              }
              return obj
            },
          },
          {
            title:'编号',
            align:"center",
            width: 50,
            dataIndex: 'alarmNo'
          },
          {
            title:'报警时间',
            align:"center",
            width: 70,
            dataIndex: 'alarmTime'
          },
          {
            title:'故障名称',
            align:"center",
            width: 70,
            dataIndex: 'realReason'
          },
          {
            title:'频率',
            align:"center",
            width: 20,
            dataIndex: 'alarmAmount'
          },
        ],

        columnsC: [
          {
            title: '',
            dataIndex: 'equipNo',
            width: 35,
            align:"center",
            customRender: (value, row, index) => {
              let obj = {
                children: "",
                attrs: {}
              }
              if (index === 0) {
                obj = {
                  children: "C类",
                  attrs: { rowSpan: this.dataSource.length }
                }
              }else {
                obj.attrs.colSpan = 0
              }
              return obj
            },
          },
          {
            title:'编号',
            align:"center",
            width: 50,
            dataIndex: 'alarmNo'
          },
          {
            title:'报警时间',
            align:"center",
            width: 70,
            dataIndex: 'alarmTime'
          },
          {
            title:'故障名称',
            align:"center",
            width: 70,
            dataIndex: 'realReason'
          },
          {
            title:'频率',
            align:"center",
            width: 20,
            dataIndex: 'alarmAmount'
          },
        ],
        columnsWorkOrder: [
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
            title:'报警时间',
            align:"center",
            dataIndex: 'alarmTime'
          },
          {
            title:'报警编号',
            align:"center",
            dataIndex: 'faultId'
          },
          {
            title:'故障名称',
            align:"center",
            dataIndex: 'alarmReason'
          },
          {
            title:'工单号',
            align:"center",
            dataIndex: 'id'
          },
          {
            title:'检定线',
            align:"center",
            dataIndex: 'lineNo_dictText'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'equipNo'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName'
          },
          {
            title:'故障等级',
            align:"center",
            dataIndex: 'alarmLevel_dictText'
          },
          {
            title:'报警次数',
            align:"center",
            dataIndex: 'alarmAmount',
          },
          {
            title:'处理状态',
            align:"center",
            dataIndex: 'processStatus_dictText'
          },
          {
            title:'处理人',
            align:"center",
            dataIndex: 'createName'
          },
          {
            title:'管时操作',
            align:"center",
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
          }

        ],

        validatorRules: {
          prdCategory: {
            rules: []
          },
          datePlanned: {
            rules: []
          },
        },
        url: {
          list: "/faultAlarm/vAlarmInfo/list",
          workOrderList: "/faultAlarm/vAlarmInfo/workOrderList",
          exportXlsUrl: "/workOrder/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrder/ordWorkOrderInfo/importExcel",
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
        },
        dictOptions:{},
        tableScroll:{x :47*35+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    created() {
      if(!this.disableMixinCreated){
        console.log(' -- mixin created -- ')
        //初始化字典配置 在自己页面定义
        this.getData();
        this.getDataA();
        this.getDataB();
        this.getDataC();
        this.getDataWorkOrder()
      }
    },
    methods: {
      clickDate(){
        this.getDataA();
        this.getDataB();
        this.getDataC();
      },
      apportion(index,record){
        console.log("record1111",record)
        console.log("index11111",index)
          if(record.processStatus == "01"){
            this.$refs.modalDispatching.faultAlarmShow(record.id,record);
            this.$refs.modalDispatching.disableSubmit = false;
          }else {
            this.$message.warning("此工单已派工")
          }
      },
      redeploy(index,record){
        if(record.processStatus == "03"){
          this.$refs.modalRedeploy.edit(record);
          this.$refs.modalRedeploy.disableSubmit = false;
        }else {
          this.$message.warning("此工单不能转派")
        }
      },
      supervise(index,record){
        if(record.processStatus == "02"){
          this.$refs.modalSupervise.supervise(record.id);
        }else {
          this.$message.warning("此工单不能督办")
        }
      },
      handleEdit2(){

      },
      // handleTableChange(pagination) {
      //   this.ipagination.current = pagination.current;
      //   this.ipagination.pageSize = pagination.pageSize;
      //   this.getData();
      // },
      // handleTableChangeA(pagination) {
      //   this.ipaginationA.current = pagination.current;
      //   this.ipaginationA.pageSize = pagination.pageSize;
      //   this.getDataA();
      // },
      // handleTableChangeB(pagination) {
      //   this.ipaginationB.current = pagination.current;
      //   this.ipaginationB.pageSize = pagination.pageSize;
      //   this.getDataB();
      // },
      // handleTableChangeC(pagination) {
      //   this.ipaginationC.current = pagination.current;
      //   this.ipaginationC.pageSize = pagination.pageSize;
      //   this.getDataC();
      // },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.getData();
      },
      handleAdd(){
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      onSelectChangeUser(){

      },
      getData(){
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //获取查询条件
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        console.log("查询参数11111111111",params)
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            console.log("查询数据1111111111",this.dataSource)
            // this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      getDataA(){
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //获取查询条件
        this.queryParamA.alarmLevel = "A"
        var param = Object.assign({},this.queryParam, this.queryParamA, this.isorter);
        console.log("查询query参数222222",this.queryParamA)
        // param.pageNo = this.ipaginationA.current;
        // param.pageSize = this.ipaginationA.pageSize;
        var params =  filterObj(param);
        this.loading = true;
        console.log("查询参数222222",params)
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSourceA = res.result.records;
            console.log("查询数据222222",this.dataSourceA)
            // this.ipaginationA.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      getDataB(){
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //获取查询条件
        this.queryParamB.alarmLevel = "B"
        var param = Object.assign({},this.queryParam,this.queryParamB, this.isorter);
        console.log("查询query参数333333333",this.queryParamB)
        // param.pageNo = this.ipaginationB.current;
        // param.pageSize = this.ipaginationB.pageSize;
        var params =  filterObj(param);
        this.loading = true;
        console.log("查询参数333333333",params)
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSourceB = res.result.records;
            console.log("查询数据33333",this.dataSourceB)
            // this.ipaginationB.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      getDataC(){
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //获取查询条件
        this.queryParamC.alarmLevel = "C"
        var param = Object.assign({},this.queryParam,this.queryParamC, this.isorter);
        console.log("查询query参数4444444",this.queryParamB)
        // param.pageNo = this.ipaginationC.current;
        // param.pageSize = this.ipaginationC.pageSize;
        var params =  filterObj(param);
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSourceC = res.result.records;
            console.log("查询数据4444444",this.dataSourceC)
            // this.ipaginationC.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      getDataWorkOrder(){
        //获取查询条件
        this.form.validateFieldsAndScroll((err, values) => {
          var param = Object.assign({},values, this.isorter);
          // param.pageNo = this.ipaginationWorkOrder.current;
          // param.pageSize = this.ipaginationWorkOrder.pageSize;
          var params =  filterObj(param);
          this.loading = true;
          getAction(this.url.workOrderList, params).then((res) => {
            if (res.success) {
              this.dataSourceWorkOrder = res.result.records;
              console.log("查询数据4444444",this.dataSourceWorkOrder)
              this.ipaginationWorkOrder.total = res.result.total;
            }
            if(res.code===510){
              this.$message.warning(res.message)
            }
            this.loading = false;
          })
        })

      },
      getQueryParams() {
        //获取查询条件
        var param = Object.assign(this.queryParam, this.isorter);
        // param.pageNo = this.ipagination.current;
        // param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      // handleOkModel(){
      //   this.confirmLoading = true;
      //   deleteAction(this.url.delete,{id:this.selectedRowKeys[0]}).then((res)=>{
      //     if(res.success){
      //       this.visibleModel = false;
      //       this.confirmLoading = false;
      //       this.searchQuery();
      //       this.onClearSelected();
      //     }else{
      //       console.log(res.message);
      //     }
      //   })
      // },
      handleCancelModel(){
        this.visibleModel = false;
      },

      // changeLine(value){
      //   this.equipLists=[];
      //   getAction(this.url.queryEquip,{paramId:value}).then(res=>{
      //     console.log("res",res)
      //     console.log("result",res.result)
      //     this.equipLists=res.result;
      //   })
      //   console.log("---equipTypeLists",this.equipLists)
      // },
      changeLine(value){
        this.equipLists=[];
        this.form.setFieldsValue({equipName:''})
        this.form.setFieldsValue({equipId:'请选择'})
        let equipVaule='';
        let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
          console.log("设备信息11111",res.result)
          equipVaule=res.result[0].value;
          if(equipVaule != null){
            getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
              this.equipLists=res.result;
            })
          }
        })
      },
      searchQuery(){
        // this.getDataA();
        // this.getDataB();
        // this.getDataC();
        this.getDataWorkOrder()
      },
      searchReset() {
        this.queryParam = {};
        this.form.resetFields();
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .ListTable{
    font-size: 1px;
  }
</style>