<template>
  <a-card :bordered="false">
    <!-- 操作按钮区域 -->
    <div class="table-operator">

      <a-button @click="handleAdd" type="primary">新增</a-button>
      <a-button @click="handleCheck" type="primary">试算</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSourceTotal"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <div v-if="record.rowIndex != '合计'">
          <a @click="editData(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() =>handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          </div>
        </span>
      </a-table>
      <SimplexModel ref="simpleForm" @ok="modalFormOk" @loadDateAdd="loadDateAdd"></SimplexModel>
      <SimplexView ref="simpleView" @ok="modalFormOk"></SimplexView>
    </div>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {deleteAction} from "../../../../../api/manage";
  import SimplexModel from "./modules/SimplexModel";
  import { getAction,postAction } from '@/api/manage'
  import SimplexView from "./modules/SimplexView";

  export default {
    name: "SimplexList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      SimplexModel,
      SimplexView
    },
    data () {
      return {
        flag:"D",
        produceCycleId:"",
        equipNumberTotal:0,
        planDateTotal:0,
        state:"D",
        planId:"",
        results : [],
        dataSourceTotal:[],
        description: '',
        // 表头
        columns: [
          {
            title: '序号',
            align: 'center',
            dataIndex: 'rowIndex',
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'单相频率',
            align:"center",
            dataIndex: 'equipRateD_dictText',
            customRender: function(text) {
                  return text;
            }
          },
          {
            title:'拟检测数',
            align:"center",
            dataIndex: 'equipNumber'
          },
          {
            title:'检测日类型',
            align:"center",
            dataIndex: 'dateType_dictText'
          },
           {
            title:'计划天数',
            align:"center",
            dataIndex: 'planDate'
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
            align: 'center',
            dataIndex: 'rowIndex',
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'单相频率',
            align:"center",
            dataIndex: 'equipRateD_dictText',
            customRender: function(text) {
              return text;
            }
          },
          {
            title:'拟检测数',
            align:"center",
            dataIndex: 'equipNumber'
          },
          {
            title:'检测日类型',
            align:"center",
            dataIndex: 'dateType_dictText'
          },
          {
            title:'计划天数',
            align:"center",
            dataIndex: 'planDate'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns2:[
          {
            title: '序号',
            align: 'center',
            dataIndex: 'rowIndex',
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'三相物料描述',
            align:"center",
            dataIndex: 'equipDescribeS_dictText',
            customRender: function(text) {
              return text;
            }
          },
          {
            title:'三相频率',
            align:"center",
            dataIndex: 'equipRateS_dictText'
          },
          {
            title:'三相电流',
            align:"center",
            dataIndex: 'equipI_dictText'
          },
          {
            title:'拟检测数',
            align:"center",
            dataIndex: 'equipNumber'
          },
          {
            title:'检测日类型',
            align:"center",
            dataIndex: 'dateType_dictText'
          },
          {
            title:'计划检测天数',
            align:"center",
            dataIndex: 'planDate'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns3:[
          {
            title: '序号',
            align: 'center',
            dataIndex: 'rowIndex',
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'终端物料描述',
            align:"center",
            dataIndex: 'equipDescribeC_dictText',
            customRender: function(text) {
              return text;
            }
          },
          {
            title:'终端载波频率',
            align:"center",
            dataIndex: 'equipRateC_dictText'
          },
          {
            title:'终端电压/通信方式',
            align:"center",
            dataIndex: 'equipSignalC_dictText'
          },
          {
            title:'拟检测数',
            align:"center",
            dataIndex: 'equipNumber'
          },
          {
            title:'检测日类型',
            align:"center",
            dataIndex: 'dateType_dictText'
          },
          {
            title:'计划检测天数',
            align:"center",
            dataIndex: 'planDate'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        columns4:[
          {
            title: '序号',
            align: 'center',
            dataIndex: 'rowIndex',
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title:'变比',
            align:"center",
            dataIndex: 'equipScale_dictText',
            customRender: function(text) {
              return text;
            }
          },
          {
            title:'拟检测数',
            align:"center",
            dataIndex: 'equipNumber'
          },
          {
            title:'检测日类型',
            align:"center",
            dataIndex: 'dateType_dictText'
          },
          {
            title:'计划检测天数',
            align:"center",
            dataIndex: 'planDate'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/prod/devProducePlan/list?categ=D&produceCycleId=1304689173908377601",
          del: "/prod/devProducePlan/delete",
          trial: "/prod/devProducePlan/trial"
        },
        dictOptions:{},
      }
    },
    beforeCreate() {

    },
    computed: {

    },
    mounted() {

    },
    methods: {
      editData(record){
        console.log(record);
        this.$refs.simpleForm.edit(record);
      },
        /** 表格增加合计行 */
        tableAddTotalRow(columns, dataSource) {
          let numKey = 'rowIndex'
          let totalRow = { [numKey]: '合计' }
          columns.forEach(column => {
            console.log("rrrrrrrrrrrrrrrrrrrrrrr",column)
            if (column.dataIndex == 'equipNumber' || column.dataIndex == 'planDate' || column.dataIndex == 'rowIndex') {
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
              }
            }else{
              totalRow[column.dataIndex] = '-'
            }
          })

          this.dataSourceTotal.push(totalRow)
        },
      handleAdd(){
        this.$refs.simpleForm.show(this.produceCycleId,this.flag);
      },
      handleDelete(id){
        deleteAction(this.url.del,{id:id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loadDateAdd();
        })
      },
      modalFormOk(){
        this.searchQuery();
        this.onClearSelected();
      },
      initDictConfig(){

      },
      show1(id,state){
        this.produceCycleId = id;
        this.state = state;
        console.log(state);
        console.log(id);
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
        this.url.list = "/prod/devProducePlan/list?categ=" + state +"&produceCycleId=" + id;
        getAction(this.url.list,{}).then((res)=>{
          if(res.success){
            console.log(res)
            this.dataSourceTotal = res.result.records;
            let results = [];
            for (var i = 0; i < res.result.records.length; i++){
              results.push(res.result.records[i]);
            }
            this.tableAddTotalRow(this.columns, results);
            this.$refs.simpleView.edit(id,id,state);
          }else{
            this.$refs.simpleView.edit(id,id,state);
            this.planId = "";
            console.log(res.message);
          }
        });
        this.$emit('ok');
        this.$emit('loadAdd');
      },
      handleCheck() {
            const that = this
            postAction(this.url.trial + "?produceCycleId=" + this.produceCycleId + "&categ=" + this.state,{}).then((res) => {
              if (res.success) {
                this.$message.success(res.message);
              } else {
                this.$message.warning(res.message)
              }
            }).finally(() => {
              this.$refs.simpleView.edit(this.produceCycleId,this.produceCycleId,this.state);
            })
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      loadDateAdd(){
            this.show1( this.produceCycleId, this.state);
      }
    },
    created() {
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>