<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <div style="padding-left:25px;padding-right:25px">
          <a-row :gutter="24">
  <!--          <a-col :span="8">
              <a-form-item label="单位：" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag placeholder="单位" v-model="queryParam.unit"  dictCode="unit"/>
              </a-form-item>
            </a-col>-->
            <a-col :span="8">
              <a-form-item label="检定线："  :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list"
                                       :trigger-change="true"
                                       v-model="queryParam.lineNo"
                                       v-decorator="[ 'lineNo', validatorRules.lineNo]"
                                       dictCode="o_area,area_name,area_id,new_flag = '1'"
                                       style="width: 50%"
                                       @change="changeLine"
                                       placeholder="请选择检定线" />
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
                <a-select  v-model="queryParam.equipName" v-decorator="[ 'equipName', validatorRules.equipName]"  placeholder="请选择" style="width: 50%">
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

          </a-row>

          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="故障等级" :labelCol="{span:12}" :wrapperCol="{span:12}">
                <a-radio-group v-model="queryParam.alarmLevel" style="width: 100%">
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

          <div class="table-operator">
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
                <a-form-item label="数据时间起:" :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="toggleSearchStatus">
                  <j-date  placeholder="请选择开始时间" v-model="queryParam.startResolveTime2"  :trigger-change="true" style="width: 50%"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="数据时间止:" :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="toggleSearchStatus">
                  <j-date   placeholder="请选择结束时间"  v-model="queryParam.endResolveTime21"  :trigger-change="true" style="width: 50%"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="工单来源：" :labelCol="{span:9}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
                  <j-dict-select-tag placeholder="工单来源" v-model="queryParam.orderCreLabe"  dictCode="source" style="width: 50%"/>
                </a-form-item>
              </a-col>
              <a-col style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                </a>
              </a-col>
            </a-row>
          </div>
        </div>
<!--        <a-row :gutter="24">

          &lt;!&ndash;          <a-col :span="8">
                      <a-form-item label="报警时间" :labelCol="{span:12}" :wrapperCol="{span:12}">
                        <j-date  placeholder="请选择报警开始时间" v-model="queryParam.startAlarmTime"  :trigger-change="true" style="width: 100%"/>
                      </a-form-item>
                    </a-col>
                    <a-col :span="8">
                      <a-form-item label="至" :labelCol="{span:12}" :wrapperCol="{span:12}">
                        <j-date   placeholder="请选择报警结束时间" v-model="queryParam.endAlarmTime" :trigger-change="true" style="width: 100%"/>
                      </a-form-item>
                    </a-col>&ndash;&gt;

        </a-row>-->

      </a-form>
      <a-row :gutter="24">
        <a-col >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button @click="fowards" type="primary" icon="plus" style="margin-right: 10px;">转派</a-button>
              <a-button @click="checks" type="primary" icon="plus" style="margin-right: 10px;">结单</a-button>
            </span>
        </a-col>
      </a-row>
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

      </a-table>
    </div>

    <UnfiledCompleteOrdWorkOrderInfoModal ref="unfileCompleteForm" @ok="modalFormOk"></UnfiledCompleteOrdWorkOrderInfoModal>
    <CheckOrdWorkOrderInfoModal ref="checkForm" @ok="modalFormOk"></CheckOrdWorkOrderInfoModal>
  </a-card>
</template>

<script>

  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDate from '@/components/jeecg/JDate'
  import UnfiledCompleteOrdWorkOrderInfoModal from "./modules/UnfiledCompleteOrdWorkOrderInfoModal";
  import CheckOrdWorkOrderInfoModal from "./modules/CheckOrdWorkOrderInfoModal";
  export default {
    name: "OrdWorkOrderInfoList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTagOra,
      ACol,
      ARow,
      JDictSelectTag,
      JDictSelectTagIop,
      JDate,
      UnfiledCompleteOrdWorkOrderInfoModal,
      CheckOrdWorkOrderInfoModal
    },
    data () {
      return {
        labelCol: {
            span: 6
        },
        wrapperCol: {
            span: 16
        },
        labelCol2: {
            span: 3
        },
        wrapperCol2: {
            span: 20
        },
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
            align:"center",
            width: 150,
            dataIndex: 'id'
          },
          {
            title:'工单类型',
            align:"center",
            dataIndex: 'orderType',
            customRender:function (text) {
              if(text === '01'){
                return "故障工单"
              }else if(text === '09'){
                return "督办工单"
              }else {
                return ""
              }
            }
          },
          {
            title:'检定线',
            align:"center",
            width: 120,
            dataIndex: 'lineNo_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            width: 120,
            dataIndex: 'equipName'
          },
          {
            title:'故障等级',
            align:"center",
            dataIndex: 'faultGrade',
            customRender:function (text) {
              if(text === 'hight'){
                return "A级"
              }else if(text === 'centre'){
                return "B级"
              }else if (text === 'low'){
                return "C级"
              }
            }
          },
          {
            title:'报警次数',
            align:"center",
            dataIndex: 'alarmAmount'
          },
          {
            title:'报警内容',
            align:"center",
            dataIndex: 'alarmReason'
          },
          {
            title:'派发人工号',
            align:"center",
            width: 150,
            dataIndex: 'dispatchToUser_dictText'
          },
          {
            title:'派发人',
            align:"center",
            width: 120,
            dataIndex: 'dispatchToUserName'
          },
          {
            title:'掌机是否下载',
            align:"center",
            dataIndex: 'isLoad'
          },
          {
            title:'处理人',
            align:"center",
            dataIndex: 'processUser_dictText'
          },
          {
            title:'督办人',
            align:"center",
            dataIndex: 'urgeName'
          },
          {
            title:'是否督办',
            align:"center",
            dataIndex: 'pressLabe',
            customRender:function(text){
              if(text === '1'){
                return "已督办"
              }else{
                return "未督办"
              }
            }

          },
          {
            title:'工单状态',
            align:"center",
            dataIndex: 'processStatus',
            customRender:function (text) {
              if(text === '01'){
                return "待派工"
              }else if(text === '02'){
                return "已派工"
              }else if(text === '03'){
                return "已处理"
              }else if(text === "04"){
                return "已转派"
              }else if (text === "09"){
                return "已归档"
              }
            }
          },
          {
            title:'创建人姓名',
            align:"center",
            dataIndex: 'createName'
          },
          {
            title:'工单来源',
            align:"center",
            dataIndex: 'orderCreLabe',
            customRender:function (text) {
              if(text === 'SYSTEM'){
                return "系统生成"
              }else if(text === 'artificial'){
                return "手工生成"
              }else {
                return ""
              }
            }
          },
          {
            title:'采集侧任务来源',
            align:"center",
            dataIndex: 'collectSideTaskSource'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'派发时间',
            align:"center",
            dataIndex: 'dispatchTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'要求完成时间',
            align:"center",
            dataIndex: 'pressTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'下载时间',
            align:"center",
            dataIndex: 'downTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'处理时间',
            align:"center",
            dataIndex: 'processTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'dataUploadTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'结单时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
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
          list: "/workOrder/ordWorkOrderDetail/page",
          delete: "/workOrder/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrder/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrder/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrder/ordWorkOrderInfo/importExcel",
        },
        dictOptions:{},
        tableScroll:{x : 47*65+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      changEquipNo(value){
        this.form.setFieldsValue({alarmReasonId:null})
        getAction(this.url.queryEquipNo,{id:value}).then(res=>{
          console.log("res1111111111",res)
          console.log("result2222222222",res.result)

          this.equipNo=res.message;
        })
      },
      changeLine(value){
        // this.equipLists=[];
        // this.form.setFieldsValue({equipName:'请选择'})
        // let equipVaule='';
        // let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        // initDictOptionsOra(str).then(res => {
        //   equipVaule=res.result[0].value;
        //   if(equipVaule != null){
        //     getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
        //       this.equipLists=res.result;
        //     })
        //   }
        // })
          this.queryParam.lineNo = value
          this.equipLists=[];
          let str="O_EQUIP,name,EQUIP_NO,area_id='"+value+"'";
          initDictOptionsOra(str).then(res => {
              // console.log("res.result",res.result)
              this.equipLists=res.result;
              console.log(this.equipLists)
          })
      },
      checks(){
        if(this.selectedRowKeys.length==1){
          if(this.selectionRows[0].processStatus == "04" || this.selectionRows[0].processStatus == "03"){
            this.$refs.checkForm.edit(this.selectionRows[0]);
          }else if (this.selectionRows[0].processStatus == "09"){
            this.$message.warning("此工单已结单")
          }else{
            this.$message.warning("工单流程不正确");
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      onClearSelected(){
        this.selectedRowKeys = []
      },
      fowards(){
        if(this.selectedRowKeys.length==1){
          if(this.selectionRows[0].processStatus == "03"){
            this.$refs.unfileCompleteForm.show(this.selectedRowKeys,this.selectionRows);
          }else if (this.selectionRows[0].processStatus == "04"){
            this.$message.warning("此工单已转派")
          }else {
            this.$message.warning("工单流程不正确");
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      initDictConfig(){
      },
      hangUp: function () {
        if(this.selectedRowKeys.length==1){
          //this.$refs.modalUp.edit(this.selectionRows[0]);
          //this.$refs.modalUp.disableSubmit = false;
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
            //this.$refs.modalDispatching.show(this.selectedRowKeys,this.selectionRows);
            //this.$refs.modalDispatching.disableSubmit = false;
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
            //this.$refs.dealWorkOrder.edit(this.selectionRows[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      modalFormOk(){
        this.searchQuery();
        this.onClearSelected();
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>