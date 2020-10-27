<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="检定线："  :labelCol="{span:9}" :wrapperCol="{span:15}">
              <!--              <j-dict-select-tag type="list"-->
              <!--                                 placeholder=""-->
              <!--                                 v-model="type"-->
              <!--                                 dict-code="IOP_PUB_CATEGORY"/>-->
              <j-dict-select-tag-ora type="list" :trigger-change="true"
                                     v-model="queryParam.lineNo"
                                     v-decorator="[ 'lineNo', validatorRules.lineNo]"
                                     dictCode="o_area,area_name,area_id,new_flag = '1'"
                                     style="width: 50%"
                                     placeholder="请选择检定线"  @change="changeLine"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-select v-model="queryParam.equipName"
                        v-decorator="[ 'equipName', validatorRules.equipName]"
                        placeholder="请选择"
                        style="width: 50%">
                <a-select-option v-for="item in equipLists" :key="item.value" :value="item.title">
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="工单类型：" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-select
                label-in-value
                style="width: 50%"
                v-model="queryParam.orderType">
                <a-select-option value="01">
                  故障工单
                </a-select-option>
                <a-select-option value="09">
                  督办工单
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="故障等级" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-radio-group v-model="queryParam.alarmLevel" v-decorator="['alarmLevel', validatorRules.alarmLevel]" style="width: 50%">
                <a-radio value="hight">
                  A
                </a-radio>
                <a-radio value="centre">
                  B
                </a-radio>
                <a-radio value="low">
                  C
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>

<!--          <a-col :span="8">
            <a-form-item label="单位：" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input placeholder="" v-model="queryParam.pickingNo"></a-input>
            </a-form-item>
          </a-col>-->
<!--          <a-col :span="8">
            <a-form-item label="工单类型：" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-select
                label-in-value
                v-model="queryParam.orderType">
                <a-select-option value="01">
                  故障工单
                </a-select-option>
                <a-select-option value="09">
                  督办工单
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>-->

          <a-col :span="4">
            <a-form-item label="报警时间:" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startAlarmTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="止" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date   placeholder="请选择结束时间"  v-model="queryParam.endAlarmTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="工单状态：" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <!--              <j-dict-select-tag type="list"-->
              <!--                                 placeholder=""-->
              <!--                                 v-model="queryParam.processStatus"-->
              <!--                                 dict-code="ord_work_order_status"/>-->
              <j-dict-select-tag type="list"
                                 placeholder=""
                                 style="width: 50%"
                                 v-model="queryParam.processStatus"
                                 dict-code="ORA_ORD_WORK_ORDER_INFO_PROCESS_STATUS"/>
            </a-form-item>
          </a-col>

        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="派发人："  :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-dict-select-tag type="list"
                                 placeholder=""
                                 style="width: 50%"
                                 v-model="queryParam.dispatchToUser"
                                 dict-code="sys_user,realname,id"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="派发日期起：" 派发时间 :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="this.toggleSearchStatus">
              <j-date  placeholder="请选择派发日期起" v-model="queryParam.distributeStartDate"  :trigger-change="true" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="派发日期止："  :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="this.toggleSearchStatus">
              <j-date  placeholder="请选择派发日期止" v-model="queryParam.distributeEndDate"  :trigger-change="true" style="width: 50%"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="是否下载：" :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-dict-select-tag placeholder="是否下载" v-model="queryParam.isLoad"  dictCode="is_download" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="下载时间起："  :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-date  placeholder="请选择下载时间起" v-model="queryParam.downloadStartDate"  :trigger-change="true" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="下载时间止："  :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-date  placeholder="请选择下载时间止" v-model="queryParam.downloadEndDate"  :trigger-change="true" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="采集任务来源：" :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-dict-select-tag placeholder="采集任务来源" v-model="queryParam.collectionTaskSource"  dictCode="college" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工单来源：" :labelCol="{span:9}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-dict-select-tag placeholder="工单来源" v-model="queryParam.orderCreLabe"  dictCode="source" style="width: 50%"/>
            </a-form-item>
          </a-col>
         <!-- <a-col :span="4">
            <a-form-item label="申请时间" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startApplyDate"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="至" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date   placeholder="请选择结束时间" v-model="queryParam.endApplyDate"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工单状态：" :labelCol="{span:12}" :wrapperCol="{span:12}">
&lt;!&ndash;              <j-dict-select-tag type="list"&ndash;&gt;
&lt;!&ndash;                                 placeholder=""&ndash;&gt;
&lt;!&ndash;                                 v-model="processStatus"&ndash;&gt;
&lt;!&ndash;                                 dict-code="FA_PROCE_STATUS"/>&ndash;&gt;
              <j-dict-select-tag type="list"
                                 placeholder=""
                                 v-model="queryParam.processStatus"
                                 dict-code="ORA_ORD_WORK_ORDER_INFO_PROCESS_STATUS"/>
            </a-form-item>
          </a-col>

          <a-col :span="4">
            <a-form-item label="故障等级" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-radio-group v-decorator="['alarmLevel', validatorRules.alarmLevel]">
                <a-radio value="hight">
                  A
                </a-radio>
                <a-radio value="centre">
                  B
                </a-radio>
                <a-radio value="low">
                  C
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>-->
          <a-col :span="24">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="superviseHandle" type="primary" icon="plus">督办</a-button>
<!--      <a-button @click="dispatching" type="primary" icon="plus">派工</a-button>-->
      <a-button type="primary"  @click="dealOrder" icon="plus">处理</a-button>
      <a-button @click="hangUp" type="primary" icon="plus">挂起</a-button>
    </div>

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
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="tableScroll"
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

    <ordWorkOrderInfo-modal ref="modalForm" @ok="modalFormOk"></ordWorkOrderInfo-modal>
    <HangUpOrdWorkOrderInfoModal ref="modalUp"></HangUpOrdWorkOrderInfoModal>
    <CompleteOrdWorkOrderInfoModal ref="modalDispatching"></CompleteOrdWorkOrderInfoModal>
    <DealWorkOrder ref="dealWorkOrder" @ok="modalFormOk"></DealWorkOrder>
    <OrdWorkOrderSuperviseModal ref="modalSupervise"></OrdWorkOrderSuperviseModal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { httpAction ,getAction } from '@/api/manage'
  import OrdWorkOrderInfoModal from './modules/OrdWorkOrderInfoModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import JDate from '@/components/jeecg/JDate'
  import HangUpOrdWorkOrderInfoModal from './modules/HangUpOrdWorkOrderInfoModal'
  import CompleteOrdWorkOrderInfoModal from './modules/CompleteOrdWorkOrderInfoModal'
  import DealWorkOrder from './modules/DealWorkOrder.vue'
  import OrdWorkOrderSuperviseModal from './modules/OrdWorkOrderSuperviseModal'
  import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'


  export default {
    name: "OrdWorkOrderInfoList",
    mixins:[JeecgListMixin],
    components: {
      ACol,
      ARow,
      JDictSelectTag,
      OrdWorkOrderInfoModal,
      JDictSelectTagOra,
      JDictSelectTagIop,
      JDate,
      HangUpOrdWorkOrderInfoModal,
      CompleteOrdWorkOrderInfoModal,
      DealWorkOrder,
      OrdWorkOrderSuperviseModal

    },
    data () {
      return {
        equipLists:[],
        type: '',
        processStatus:'',
        description: '故障运维工单表管理页面',
        // 表头
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
            title:'工单号',
            width:150,
            align:"center",
            dataIndex: 'id'
          },
          {
            title:'工单类型',
            align:"center",
            width:80,
            dataIndex: 'orderType_dictText'
          },
          {
            title:'工单名称',
            align:"center",
            width:120,
            dataIndex: 'alarmReason'
          },
          {
            title:'线体编号',
            align:"center",
            width:120,
            dataIndex: 'lineNo_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            width:120,
            dataIndex: 'equipName'
          },
          {
            title:'故障等级',
            align:"center",
            width:80,
            dataIndex: 'alarmLevel',
            scopedSlots: {customRender: 'coleClass'}
          },
          {
            title:'报警次数',
            align:"center",
            width:80,
            dataIndex: 'alarmAmount'
          },
          {
            title:'报警内容',
            align:"center",
            width:100,
            dataIndex: 'alarmDesc'
          },
          {
            title:'派发人工号',
            align:"center",
            width:100,
            dataIndex: 'dispatchToUser_dictText'
          },
          {
            title:'派发人',
            align:"center",
            width:110,
            dataIndex: 'dispatchUser_dictText'
          },
          {
            title:'是否下载',
            align:"center",
            width:80,
            dataIndex: 'isLoad'
          },
          {
            title:'督办人',
            align:"center",
            width:80,
            dataIndex: 'ordPressBy_dictText'
          },
          {
            title:'是否督办',
            align:"center",
            dataIndex: 'pressLabe',
            width:80,
            customRender:function (text) {
              return (text == "1") ? "是":"否"
            }
          },
          {
            title:'要求完成时间',
            align:"center",
            width:150,
            dataIndex: 'pressTime'
            /*customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }*/
          },
          {
            title:'工单状态',
            align:"center",
            width:80,
            dataIndex: 'processStatus_dictText'
          },
          {
            title:'是否挂单',
            align:"center",
            width:80,
            dataIndex: 'isSuspend_dictText'
          },
          {
            title:'创建人姓名',
            align:"center",
            width:100,
            dataIndex: 'createName'
          },
          {
            title:'工单来源',
            align:"center",
            width:80,
            dataIndex: 'orderCreLabe_dictText'
          },
          {
            title:'采集任务来源',
            align:"center",
            width:80,
            dataIndex: 'alarmSource_dictText'
          },
          {
            title:'生成时间',
            align:"center",
            width:150,
            dataIndex: 'createTime',
/*            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }*/
          },
          {
            title:'派发时间',
            align:"center",
            width:150,
            dataIndex: 'dispatchTime',
            /*customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }*/
          },
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
          list: "/workOrder/ordWorkOrderFeedBack/list",
          delete: "/workOrder/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrder/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrder/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrder/ordWorkOrderInfo/importExcel",
        },
        dictOptions:{},
        tableScroll:{x :47*40+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      changEquipNo(value){
        this.queryParam.equipName = value
        // this.form.setFieldsValue({alarmReasonId:null})
        // getAction(this.url.queryEquipNo,{id:value}).then(res=>{
        //   this.equipNo=res.message;
        // })
      },
      changeLine(value){
        this.queryParam.lineNo = value
        this.equipLists=[];
        // let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        // initDictOptionsOra(str).then(res => {
        //   equipVaule=res.result[0].value;
        //   if(equipVaule != null){
        //     getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
        //       this.equipLists=res.result;
        //     })
        //   }
        // })
        let str="O_EQUIP,name,EQUIP_NO,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
            // console.log("res.result",res.result)
            this.equipLists=res.result;
            console.log(this.equipLists)
        })
      },
      initDictConfig(){
      },
      hangUp: function () {
        if(this.selectedRowKeys.length==1){
          this.$refs.modalUp.edit(this.selectionRows[0]);
          this.$refs.modalUp.disableSubmit = false;
          // let record = this.selectionRows[0]
          // if (record.state == 'draft' || record.state == 'back') {
          //   this.$refs.modalForm.edit(this.selectedRowKeys[0],v);
          //   this.$refs.modalForm.title = "修改"
          // } else {
          //   this.$message.warning('提交审批后不能修改')
          // }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }

      },
      dispatching(){
        if(this.selectedRowKeys.length==1){
          if(this.selectionRows[0].processStatus == "01"){
            this.$refs.modalDispatching.show(this.selectedRowKeys,this.selectionRows);
            this.$refs.modalDispatching.disableSubmit = false;
          }else {
            this.$message.warning("此工单已派工")
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }

      },
      dealOrder(){
        if(this.selectedRowKeys.length==1){
            this.$refs.dealWorkOrder.edit(this.selectionRows[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      superviseHandle(){
        if(this.selectedRowKeys.length==1){
          this.$refs.modalSupervise.supervise(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>