<template>
  <div>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检定线类别">
              <j-dict-select-tag-ora type="list" v-model="queryParam.lineNo"
                                     dictCode="o_area,area_name,area_id,new_flag = '1'"
                                     placeholder="请选择检定线"/>
            </a-form-item>
          </a-col>
          <!--<a-col :xl="6" :lg="7" :md="8" :sm="24">-->
            <!--<a-form-item label="检定线">-->
              <!--<j-dict-select-tag-ora type="list" v-model="queryParam.areaId" dictCode="O_AREA,AREA_NAME,AREA_ID"-->
                                     <!--placeholder="请选择检定线"/>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="故障名称">
              <a-input placeholder="请输入故障名称" v-model="queryParam.realReason"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="故障等级">
              <j-dict-select-tag type="list" v-model="queryParam.faultGrade" dictCode="FAULT_GRADE"
                                 style="width: 90%" placeholder="请选择等级"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工单状态">
              <j-dict-select-tag type="list" v-model="queryParam.status" dictCode="FAULT_GRADE"
                                 style="width: 90%" placeholder="请选择工单状态"/>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="9" :md="10" :sm="24">
            <a-form-item label="报警时间">
              <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="5" :md="6" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleProductOrder" type="primary" icon="plus">生成工单</a-button>
      <a-button @click="handleEdit" type="primary" icon="plus">修改</a-button>
      <a-button @click="handleAlarmLevel" type="primary" icon="plus">调整等级</a-button>
      <a-button @click="handleDelete" type="primary" icon="delete">删除</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">


        <template slot="coleClass" slot-scope="text, record">
          <span  v-if="text==='A'"        style="color: red">A级</span>
          <span  v-else-if="text==='B'"   style="color: orange">B级</span>
          <span  v-else-if="text==='C'"   style="color: yellow">C级</span>
          <span  v-else-if="text==='D'" >隔离</span>
          <span   v-else > 暂无绑定</span>
        </template>

      </a-table>
    </div>

    <mtEquipAlarmInfo-modal ref="modalForm" @ok="modalFormOk"  @onClearSelected="onClearSelected"></mtEquipAlarmInfo-modal>
    <workModul ref="workModul" @ok="modalFormOk" @onClearSelected="onClearSelected"></workModul>
    <a-modal
      title=""
      :visible="visibleModel"
      @ok="handleOkModel"
      @cancel="handleCancelModel"
    >
      <p>是否删除该报警记录！</p>
    </a-modal>

    <a-modal
      title="调整故障等级"
      :visible="visibleAlarmLevel"
      @ok="handleOkAlarm"
      @cancel="handleCancelAlarm"
    >
      <a-form-item label="故障等级" :labelCol="labelCol" :wrapperCol="wrapperCol" >
        <!--<a-input v-decorator="[ 'alarmLevel', validatorRules.alarmLevel]" placeholder="请输入报警级别"></a-input>-->
        <a-radio-group @change="handleInput">
          <a-radio value="A">
            A级
          </a-radio>
          <a-radio value="B">
            B级
          </a-radio>
          <a-radio value="C">
            C级
          </a-radio>
        </a-radio-group>
      </a-form-item>
    </a-modal>
  </a-card>
  </div>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MtEquipAlarmInfoModal from './modules/MtEquipAlarmInfoModal'
  import workModul from './modules/workModul.vue'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDate from '@/components/jeecg/JDate'
  import { getAction ,deleteAction,postAction} from '@/api/manage'

  export default {
    name: 'MtEquipAlarmInfoList',
    mixins: [JeecgListMixin],
    components: {
      MtEquipAlarmInfoModal,
      JDictSelectTagOra,
      JDictSelectTag,
      JDate,
      workModul,
    },
    data() {
      return {
        description: 'MT_EQUIP_ALARM_INFO管理页面',
        visibleModel: false,
        visibleAlarmLevel: false,
        alarmLevelId:"",
        alarmLevel:"",
        tableScroll:{x :47*35+50},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '报警时间',
            align: 'center',
            width: 200,
            dataIndex: 'alarmTime'
          },
          {
            title: '检定线',
            align: 'center',
            dataIndex: 'lineNo_dictText',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '设备名称',
            align: 'center',
            dataIndex: 'equipName',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '故障名称',
            align: 'center',
            dataIndex: 'realReason',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '报警次数',
            align: 'center',
            dataIndex: 'alarmAmount'
          },
          // {
          //   title: '设备编号',
          //   align: 'center',
          //   dataIndex: 'equipNo'
          // },
          {
              title: '报警来源',
              align: 'center',
              dataIndex: 'alarmSource'
          },
          {
            title: '故障等级',
            align: 'center',
            dataIndex: 'alarmLevel',
            scopedSlots: {customRender: 'coleClass'},
            // customRender: function(t, r, index) {
            //   if(r.alarmLevel=='A'){
            //     return 'A级';
            //   }else if(r.alarmLevel=='B'){
            //     return 'B级';
            //   }else if(r.alarmLevel=='C'){
            //     return 'C级';
            //   }else if(r.alarmLevel=='D'){
            //     return '隔离';
            //   }else{
            //     return '暂没绑定等级';
            //   }
            // }
          },
          {
            title: '是否生成工单',
            align: 'center',
            dataIndex: 'isOrder',
            customRender: function(value) {
              if(value=='1'){
                return '是';
              }else {
                return '否';
              }
            }


          },
          {
            title: '工单编号',
            align: 'center',
            dataIndex: 'ordWorkOrderInfo.id'
          },
          // {
          //   title: '派发人工号',
          //   align: 'center',
          //   dataIndex: 'dispatchToUser_dictText'
          // },
          // {
          //   title: '派发人',
          //   align: 'center',
          //   dataIndex: 'ordWorkOrderInfo.dispatchUser'
          // },
          // {
          //   title: '是否下载',
          //   align: 'center',
          //   dataIndex: 'test6'
          // },
          {
            title: '处理人',
            align: 'center',
            dataIndex: 'ordWorkOrderInfo.processUser'
          },
          {
            title: '督办人',
            align: 'center',
            dataIndex: 'test8'
          },
          {
            title: '是否督办',
            align: 'center',
            dataIndex: 'ordWorkOrderInfo.orderType',
            customRender: function(value) {
              if(value=='01'){
                return '否';
              }else if(value=='09'){
                return '是';
              }
            }
          },
          {
            title: '工单状态',
            align: 'center',
            dataIndex: 'ordWorkOrderInfo.processStatus',
            customRender: function(value) {
              if(value=='01'){
                return '待派工';
              }else if(value=='02'){
                return '已派工';
              }else if(value=='03'){
                return '已处理';
              }else if(value=='04'){
                return '已转派';
              }else if(value=='04'){
                return '已归档';
              }
            }
          }
        ],
        url: {
          list: '/alarm/record/mtEquipAlarmInfo/list',
          delete: '/alarm/record/mtEquipAlarmInfo/delete',
          deleteBatch: '/alarm/record/mtEquipAlarmInfo/deleteBatch',
          exportXlsUrl: '/alarm/record/mtEquipAlarmInfo/exportXls',
          importExcelUrl: 'alarm/record/mtEquipAlarmInfo/importExcel',
          updateAlarmLevel: 'alarm/record/mtEquipAlarmInfo/updateAlarmLevel',
        },
        dictOptions: {}
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      initDictConfig() {
      },
      handleDelete(){
        if(this.selectedRowKeys.length==1){
            //todo 可删除的报警记录只能是未生成工单的
          console.log("this.selectionRows[0]",this.selectionRows[0])
          if(this.selectionRows[0].isOrder=='1'){
            this.$message.warning("只能删除未创建工单的报警记录！")
          }else {
            if(this.selectionRows[0].alarmSource=='人工录入'){
                this.visibleModel = true;
            }else {
              this.$message.warning("只能删除人工创建的报警记录！")
            }
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleOkModel(){
        this.confirmLoading = true;
        deleteAction(this.url.delete,{id:this.selectedRowKeys[0]}).then((res)=>{
          if(res.success){
            this.visibleModel = false;
            this.confirmLoading = false;
            this.searchQuery();
            this.onClearSelected();
          }else{
            console.log(res.message);
          }
        })
      },
      handleCancelModel(){
        this.visibleModel = false;
        this.onClearSelected();
      },
      handleAlarmLevel(){
        if(this.selectedRowKeys.length==1){
          if(this.selectionRows[0].isOrder=='0'){
            this.visibleAlarmLevel=true;
            this.alarmLevelId=this.selectionRows[0].id;
          }else {
            this.$message.warning("只能处理未生成工单的记录")
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleOkAlarm(){
          let params={
            id:this.alarmLevelId,
            alarmLevel:this.alarmLevel
          }
          let  obj=Object.assign(params)
          console.log("obj----",obj)
          postAction(this.url.updateAlarmLevel,obj).then((res)=>{
          if(res.success){
            this.visibleModel = false;
            this.confirmLoading = false;
            this.searchQuery();
            this.onClearSelected();
            this.handleCancelAlarm();
          }else{
            console.log(res.message);
          }
        })
      },
      handleCancelAlarm(){
        this.visibleAlarmLevel=false;
        this.alarmLevelId='';
        this.alarmLevel='';
      },
      handleInput(e){
        this.alarmLevel=e.target.value;
      },
      handleEdit(){
        if(this.selectedRowKeys.length==1){
          if(this.selectionRows[0].isOrder=='0'){
            this.$refs.modalForm.edit(this.selectionRows[0]);
          }else {
            this.$message.warning("只能处理未生成工单的记录")
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleProductOrder(){
        if(this.selectedRowKeys.length==1){
          if(this.selectionRows[0].isOrder=='0'){
            this.$refs.workModul.edit(this.selectionRows[0]);
          }else {
            this.$message.warning("只能处理未生成工单的记录")
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      tableRowClass(record,index){
         if(record.alarmLevel=='A'){
           console.log('red')
              return 'trcolor'
         }else if(record.alarmLevel=='B'){
              return 'trcolorB'
         }else if(record.alarmLevel=='C'){
               return 'trcolorC'
         }
         // else if(record.alarmLevel=='D'){
         //
         // }
      }
    }
  }

</script>
<style scoped>
    @import '~@assets/less/common.less';
    @import '~@assets/less/index.less';
  .ant-alert-info {
    border: 1px solid #068b881f;
    background-color: #068b881f;
  }



</style>