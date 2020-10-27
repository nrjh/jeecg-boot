<template>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :pagination="false"
        :dataSource="dataSourceTotal"
        :loading="loading"
        :scroll="tableScroll"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
        <span  slot="action" slot-scope="text, record">
          <a-popconfirm v-if="record.rowIndex != '合计'" title="确定删除吗?" @confirm="() =>handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
        <template slot="footer" slot-scope="currentPageData">
          <a-button @click="handleAdd" type="primary" style="margin-right: 10px;">合成</a-button>
          <a-button @click="handleCheck" type="primary">提交</a-button>
        </template>
      </a-table>
    </div>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import { getAction,postAction } from '@/api/manage'
  import {deleteAction} from "../../../../../../api/manage";
  import { initDictOptions } from '@/components/dict/JDictSelectUtil'
  export default {
    name: "SimplexList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag
    },
    data () {
      const vm = this;
      return {
        state:"",
        produceCycleId:"",
        equipNumberTotal:0,
        planDateTotal:0,
        planId:"",
        dataSourceTotal:[],
        results : [],
        //tableScroll:{y:47*35+50},
        tableScroll:{y:300},

        //回显字典数组列表
        destination: {
          equipNumber: [],
          equipRateD: [],
          equipDescribeS: [],
          equipRateS: [],
          equipI: [],
          equipScale: [],
          equipDescribeC: [],
          equipRateC: [],
          equipSignalC: []
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: 'rowIndex',
            width:60,
            align:"center",
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'cycleDay'
          },
          {
            title:'单相频率',
            align:"center",
            dataIndex: 'equipRateD',
            customRender:function (text) {
              return vm.getEquipRateD(text)
            }
          },
          {
            title:'当日计划拟检测数',
            align:"center",
            dataIndex: 'planDayNumber'
          },
           {
            title:'实际检测数',
            align:"center",
            dataIndex: 'realDayNumber'
          },
          {
            title:'实际总检测数',
            align:"center",
            dataIndex: 'realSumNumber'
          },
          {
            title:'完成总计划占比',
            align:"center",
            dataIndex: 'realSumRatio'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns1: [
          {
            title: '序号',
            dataIndex: 'rowIndex',
            width:60,
            align:"center",
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'cycleDay'
          },
          {
            title:'单相频率',
            align:"center",
            dataIndex: 'equipRateD',
            customRender:function (text) {
              return vm.getEquipRateD(text)
            }
          },
          {
            title:'当日计划拟检测数',
            align:"center",
            dataIndex: 'planDayNumber'
          },
          {
            title:'实际检测数',
            align:"center",
            dataIndex: 'realDayNumber'
          },
          {
            title:'实际总检测数',
            align:"center",
            dataIndex: 'realSumNumber'
          },
          {
            title:'完成总计划占比',
            align:"center",
            dataIndex: 'realSumRatio'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns2: [
          {
            title: '序号',
            dataIndex: 'rowIndex',
            width:60,
            align:"center",
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'cycleDay'
          },
          {
            title:'物料描述',
            align:"center",
            dataIndex: 'equipDescribeS',
            customRender:function (text) {
              return vm.getEquipDescribeS(text)
            }
          },
          {
            title:'频率',
            align:"center",
            dataIndex: 'equipRateS',
            customRender:function (text) {
              return vm.getEquipRateS(text)
            }
          },
          {
            title:'三相电流',
            align:"center",
            dataIndex: 'equipI',
            customRender:function (text) {
              return vm.getEquipI(text)
            }
          },
          {
            title:'当日计划拟检测数',
            align:"center",
            dataIndex: 'planDayNumber'
          },
          {
            title:'实际检测数',
            align:"center",
            dataIndex: 'realDayNumber'
          },
          {
            title:'实际总检测数',
            align:"center",
            dataIndex: 'realSumNumber'
          },
          {
            title:'完成总计划占比',
            align:"center",
            dataIndex: 'realSumRatio'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns3: [
          {
            title: '序号',
            dataIndex: 'rowIndex',
            width:60,
            align:"center",
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'cycleDay'
          },
          {
            title:'物料描述',
            align:"center",
            dataIndex: 'equipDescribeC',
            customRender:function (text) {
              return vm.getEquipDescribeC(text)
            }
          },
          {
            title:'载波频率',
            align:"center",
            dataIndex: 'equipRateC',
            customRender:function (text) {
              return vm.getEquipRateC(text)
            }
          },
          {
            title:'电压/通信方式',
            align:"center",
            dataIndex: 'equipSignalC',
            customRender:function (text) {
              return vm.getEquipSignalC(text)
            }
          },
          {
            title:'当日计划拟检测数',
            align:"center",
            dataIndex: 'planDayNumber'
          },
          {
            title:'实际检测数',
            align:"center",
            dataIndex: 'realDayNumber'
          },
          {
            title:'实际总检测数',
            align:"center",
            dataIndex: 'realSumNumber'
          },
          {
            title:'完成总计划占比',
            align:"center",
            dataIndex: 'realSumRatio'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns4: [
          {
            title: '序号',
            dataIndex: 'rowIndex',
            width:60,
            align:"center",
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'cycleDay'
          },
          {
            title:'变比',
            align:"center",
            dataIndex: 'equipScale',
            customRender:function (text) {
              return vm.getEquipScale(text)
            }
          },
          {
            title:'当日计划拟检测数',
            align:"center",
            dataIndex: 'planDayNumber'
          },
          {
            title:'实际检测数',
            align:"center",
            dataIndex: 'realDayNumber'
          },
          {
            title:'实际总检测数',
            align:"center",
            dataIndex: 'realSumNumber'
          },
          {
            title:'完成总计划占比',
            align:"center",
            dataIndex: 'realSumRatio'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/prod/devProduceDplan/list?categ=D&produceCycleId=1304689173908377601",
          del: "/prod/devProduceDplan/delete",
          submit:"/prod/devProduceDplan/submit",
          synthesis:"/prod/devProduceDplan/synthesis"
        },
        dictOptions:{},
      }
    },
    beforeCreate() {

    },
    computed: {

    },
    methods: {
      /** 表格增加合计行 */
      tableAddTotalRow(columns, dataSource) {
        let numKey = 'rowIndex'
        let totalRow = { [numKey]: '合计' }
        columns.forEach(column => {
          if (column.dataIndex == 'planDayNumber' || column.dataIndex == 'rowIndex' || column.dataIndex == 'realSumRatio' || column.dataIndex == 'realSumNumber' || column.dataIndex == 'realDayNumber') {
            let {key, dataIndex} = column
            if (![key, dataIndex].includes(numKey)) {
              let total = 0
              dataSource.forEach(data => {
                total += /^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN
                //console.log(data[dataIndex], ':', (/^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN))
              })

              if (Number.isNaN(total)) {
                total = '-'
              }
              totalRow[dataIndex] = total
              totalRow['realDayNumber'] = 0;
              totalRow['realSumNumber'] = 0;
              totalRow['realSumRatio'] = '100%';
            }
          }else{
            totalRow[column.dataIndex] = '-'
          }
        })

        this.dataSourceTotal.push(totalRow)
      },
      getEquipRateD(value){
        var data = this.destination.equipRateD;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipDescribeS(value){
        var data = this.destination.equipDescribeS;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipRateS(value){
        var data = this.destination.equipRateS;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipI(value){
        var data = this.destination.equipI;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipScale(value){
        var data = this.destination.equipScale;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipDescribeC(value){
        var data = this.destination.equipDescribeC;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipRateC(value){
        var data = this.destination.equipRateC;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      getEquipSignalC(value){
        var data = this.destination.equipSignalC;
        for(let i = 0;i<data.length;i++){
          if(data[i].value == value){
            return data[i].title;
          }
        }
      },
      edit(cid,id,state){
        this.produceCycleId = cid;
        this.state = state;
        this.planId = id;
        console.log(this.produceCycleId)
        console.log(this.state)
        console.log(this.planId)
        if (state == 'D'){
          this.columns = this.columns1;
        }else if (state == 'S'){
          this.columns = this.columns2;
        }else if (state == 'H'){
          this.columns = this.columns4;
        }else if (state == 'C'){
          this.columns = this.columns3;
        }
        this.flag = state;
        this.url.list = "/prod/devProduceDplan/list?categ=" + state +"&produceCycleId=" + id;
        getAction(this.url.list,{}).then((res)=>{
          if(res.success){
            console.log(res)
            this.dataSourceTotal = res.result.records;
            let results = [];
            for (var i = 0; i < res.result.records.length; i++){
              results.push(res.result.records[i]);
            }
            console.log(res)
            this.tableAddTotalRow(this.columns, results);
          }
        });
      },
      handleAdd(){
        if (this.selectedRowKeys.length < 2){
          this.$message.warning("至少选择2条记录")
        }else {
          const that = this
          var ids = [];
          console.log(this.selectionRows);
          for (var i = 0; i < this.selectionRows.length; i++) {
            if (this.selectionRows[i].rowIndex != '合计') {
              ids.push(this.selectionRows[i].id);
            }
          }
          postAction(this.url.synthesis + "?ids=" + ids.join(","),{}).then((res) => {
            if (res.success) {
              this.$message.success(res.message);
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.modalFormOk();
          })
        }
        //this.$refs.simpleForm.show(this.produceCycleId);
      },
      handleDelete(id){
        deleteAction(this.url.del,{id:id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.modalFormOk();
        })
      },
      modalFormOk(){
        this.searchQuery();
        //this.onClearSelected();
      },
      searchQuery(){
        this.edit(this.produceCycleId,this.planId,this.state);
      },
      initDictConfig(){
        initDictOptions('D_EQUIP_RATE').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipRateD = res.result;
        })
        initDictOptions('H_EQUIP_SCALE').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipScale = res.result;
        })

        initDictOptions('EQUIP_DESCRIBE_S').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipDescribeS = res.result;
        })

        initDictOptions('D_EQUIP_RATE_S').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipRateS = res.result;
        })

        initDictOptions('D_EQUIP_I_S').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipI = res.result;
        })

        initDictOptions('EQUIP_DESCRIBE_C').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipDescribeC = res.result;
        })

        initDictOptions('C_EQUIP_RATE').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipRateC = res.result;
        })

        initDictOptions('C_EQUIP_SIGNAL').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.equipSignalC = res.result;
        })

        initDictOptions('DATE_TYPE').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.destination.dateType = res.result;
        })

      },
      show1(id){
        this.produceCycleId = id;
      },
      handleCheck() {
        if (this.selectedRowKeys.length > 0){
          const that = this
          var ids = [];
          for (var i = 0; i < this.selectionRows.length; i++) {
            if (this.selectionRows[i].rowIndex != '合计') {
              ids.push(this.selectionRows[i].id);
            }
          }
          postAction(this.url.submit + "?ids=" + ids.join(","),{}).then((res) => {
            if (res.success) {
              this.$message.success(res.message);
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.modalFormOk();
          })
        }else{
          this.$message.warning("请选择1条记录")
        }
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
    },
    created() {
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>