<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="16">
          <a-col :span="8">
            <a-form-item label="检定线："  :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-dict-select-tag-ora @change="changeLine" type="list" :trigger-change="true" v-model="queryParam.lineNo"
                                     dictCode="o_area,area_name,area_id,new_flag = '1'"
                                     placeholder="请选择检定线" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-select  v-model="queryParam.equipName" placeholder="请选择" style="width: 50%">
                <a-select-option v-for="item in equipLists" :key="item.value" :value="item.title">
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工单类型：" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-select
                style="width: 50%"
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
          </a-col>

          <a-col :span="8">
            <a-form-item label="故障等级" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-radio-group v-model="queryParam.alarmLevel" v-decorator="['alarmLevel', validatorRules.alarmLevel]" style="width: 100%">
                <a-radio value="">
                  全部
                </a-radio>
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
          <a-col :span="8" >
            <a-form-item label="工单名称：" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input placeholder="" v-model="queryParam.alarmReason" style="width: 50%"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="工单状态：" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <!--              <j-dict-select-tag type="list"-->
              <!--                                 placeholder=""-->
              <!--                                 v-model="processStatus"-->
              <!--                                 dict-code="FA_PROCE_STATUS"/>-->
              <j-dict-select-tag type="list"
                                 placeholder=""
                                 style="width: 50%"
                                 v-model="queryParam.processStatus"
                                 dict-code="ORA_ORD_WORK_ORDER_INFO_PROCESS_STATUS"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">

          <a-col :span="8">
            <a-form-item label="工单创建时间" :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="toggleSearchStatus">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startApplyDate"  :trigger-change="true" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="至" :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="toggleSearchStatus">
              <j-date   placeholder="请选择结束时间"  v-model="queryParam.endApplyDate"  :trigger-change="true" style="width: 50%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="hangUp" type="primary" icon="plus">挂起</a-button>
      <a-button @click="dispatching" type="primary" icon="plus">派工</a-button>
      <a-button type="primary"  @click="dealOrder" icon="plus">处理</a-button>
      <a-button type="primary"  @click="deleteOrder" icon="plus">删除</a-button>
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

        <span slot="handleRemote" slot-scope="text, record">
<!--            <a @click="handleRemote(record)">-->
          <!--              远程处理-->
          <!--            </a>-->
        <span v-if="record.remoteStatus == '1'" class="click-button" @click="openRemot(record)">
          远程处理
        </span>
          <span class="not-click" v-else>远程处理</span>
    </span>
      </a-table>
    </div>
    <a-modal
      title=""
      :visible="visibleDeleteModel"
      @ok="handleOkModel"
      @cancel="handleCancelModel"
    >
      <p>是否删除此工单！</p>
    </a-modal>


    <ordWorkOrderInfo-modal ref="modalForm" @ok="modalFormOk"></ordWorkOrderInfo-modal>
    <HangUpOrdWorkOrderInfoModal ref="modalUp" @ok="modalFormOk"></HangUpOrdWorkOrderInfoModal>
    <CompleteOrdWorkOrderInfoModal ref="modalDispatching" @ok="modalFormOk"></CompleteOrdWorkOrderInfoModal>
    <DealWorkOrder ref="dealWorkOrder" @ok="modalFormOk" @onClearSelected="onClearSelected"></DealWorkOrder>
    <remotModal ref="modalRemoteModelForm" @cancelEquip="cancelEquip"></remotModal>
  </a-card>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OrdWorkOrderInfoModal from './modules/OrdWorkOrderInfoModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import JDate from '@/components/jeecg/JDate'
  import HangUpOrdWorkOrderInfoModal from './modules/HangUpOrdWorkOrderInfoModal'
  import CompleteOrdWorkOrderInfoModal from './modules/CompleteOrdWorkOrderInfoModal'
  import DealWorkOrder from './modules/DealWorkOrder.vue'
  import {deleteAction} from '@/api/manage'
  import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'
  import remotModal from "../../centralizedControl/DevMonitorInfo/Full/remotModal";
  import { httpAction ,getAction } from '@/api/manage'
  export default {
    name: "OrdWorkOrderInfoList",
    mixins:[JeecgListMixin],
    components: {
      ACol,
      ARow,
      JDictSelectTag,
      JDictSelectTagOra,
      OrdWorkOrderInfoModal,
      JDictSelectTagIop,
      JDate,
      HangUpOrdWorkOrderInfoModal,
      CompleteOrdWorkOrderInfoModal,
      DealWorkOrder,
      remotModal
    },
    data () {
      return {
        type: '',
        tempData:{},
        equipLists: [],
        processStatus:'',
        description: '故障运维工单表管理页面',
        visibleDeleteModel: false,
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
            width: 130,
            dataIndex: 'id'
          },
          {
            title:'工单类型',
            align:"center",
            width: 80,
            dataIndex: 'orderType_dictText'
          },
          {
            title:'工单名称',
            align:"center",
            width: 100,
            dataIndex: 'alarmReason'
          },
          {
            title:'检定线',
            align:"center",
            width: 100,
            dataIndex: 'lineNo_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            width: 100,
            dataIndex: 'equipName'
          },
          {
            title:'报警时间',
            align:"center",
            width: 150,
            dataIndex: 'alarmTime',
/*            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }*/
          },
          {
            title:'故障等级',
            align:"center",
            dataIndex: 'alarmLevel',
            width: 80,
            scopedSlots: {customRender: 'coleClass'}
          },
          {
            title:'远程处理',
            align:"center",
            dataIndex: 'remoteStatus',
            width: 80,
            scopedSlots: {customRender: 'handleRemote'}
          },

          {
            title:'要求完成时间',
            align:"center",
            dataIndex: 'pressTime',
            width: 150,
            customRender: function(text) {
              //return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
              return text;
            }
          },
        {
          title:'工单状态',
          align:"center",
          width: 80,
          dataIndex: 'processStatus_dictText'
        },
          {
            title:'是否挂单',
            align:"center",
            width: 70,
            dataIndex: 'isSuspend',
            customRender:function (text) {
                if(text === '01'){
                    return "是"
                }else{
                    return "否"
                }
            }
          },
          {
            title:'创建人姓名',
            align:"center",
            dataIndex: 'createName',
            width: 100,
            customRender: function (t,r,index){
                if(r.orderCreLabe === 'artificial'){
                    return r.createName
                }else if(r.orderCreLabe === 'system'){
                    return r.lineNo_dictText
                }else{
                    return '暂无创建人';
                }
            }
          },
          {
            title:'工单来源',
            align:"center",
            width: 100,
            dataIndex: 'alarmSource_dictText',

          },
          {
            title:'生成时间',
            align:"center",
            width: 150,
            dataIndex: 'createTime',
            customRender: function(text) {
              //return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
              return text;
            }
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
          list: "/workOrder/ordWorkOrderInfo/list",
          delete: "/workOrder/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrder/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrder/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrder/ordWorkOrderInfo/importExcel",
        },
        dictOptions:{},
        tableScroll:{x : 47*35+50},
        orderCreLabe:[],
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      openRemot(record){
        console.log("11111111",record);
        let data={
          areaId:record.lineNo,
          equipNo:record.equipNo
        }
        this.tempData=data;
        this.updDateEquip(data,'updateEquiopStatue');
      },
      updDateEquip(data,url){
        if(data!=null){
          let edit={
            areaId:data.areaId,
            equipNo:data.equipNo
          }
          let httpurl = '/DevMonitorInfo/devMonitorInfo/'+url;
          let method = 'put';
          let formData;
          formData = Object.assign(edit);
          console.log("表单提交数据",formData)
          httpAction(httpurl,formData,method).then((res)=>{
            if (res.success && res.result != null) {
              console.log("res.success----", res.success)
              this.$refs.modalRemoteModelForm.show();
            }
          })
        }
      },
      cancelEquip(){
        this.updDateEquip(this.tempData,'cancleEquiopStatue');
      },
      changEquipNo(value){
        this.form.setFieldsValue({alarmReasonId:null})
        getAction(this.url.queryEquipNo,{id:value}).then(res=>{
          console.log("res1111111111",res)
          console.log("result2222222222",res.result)

          this.equipNo=res.message;
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
      dispatching(){
        if(this.selectedRowKeys.length==1){
          if (this.selectionRows[0].isSuspend === '01'){
            this.$message.warning("此工单已挂起，请解除后操作")
            return;
          }else {
            if (this.selectionRows[0].processStatus == "01") {
              this.$refs.modalDispatching.show(this.selectedRowKeys, this.selectionRows);
              this.$refs.modalDispatching.disableSubmit = false;
            } else {
              this.$message.warning("此工单已派工")
            }
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }

      },
      dealOrder(){
        if(this.selectedRowKeys.length==1){
          if (this.selectionRows[0].isSuspend === '01'){
            this.$message.warning("此工单已挂起，请解除后操作")
            return;
          }else {
            this.$refs.dealWorkOrder.edit(this.selectionRows[0]);
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      deleteOrder(){
        console.log(this.selectedRowKeys)
        if(this.selectedRowKeys.length==1){
            this.visibleDeleteModel = true
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      // tableRowClass(record,index){
      //   if(record.alarmLevel=='A'){
      //     console.log('red')
      //     return 'trcolor'
      //   }else if(record.alarmLevel=='B'){
      //     return 'trcolorB'
      //   }else if(record.alarmLevel=='C'){
      //     return 'trcolorC'
      //   }
      //   // else if(record.alarmLevel=='D'){
      //   //
      //   // }
      // },
      handleOkModel(){
        this.confirmLoading = true;
        deleteAction(this.url.delete,{id:this.selectedRowKeys[0]}).then((res)=>{
          if(res.success){
            this.visibleDeleteModel = false;
            this.confirmLoading = false;
            this.searchQuery();
            this.onClearSelected();
          }else{
            console.log(res.message);
          }
        })
      },
      handleCancelModel(){
        this.visibleDeleteModel = false;
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .click-button{
    color: #068B88;
    cursor: pointer;
  }
  .not-click{
    cursor: text;
    color: black;
  }
</style>