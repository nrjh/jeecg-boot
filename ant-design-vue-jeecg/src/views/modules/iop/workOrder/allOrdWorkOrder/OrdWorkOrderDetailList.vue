<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
<!--        <a-row :gutter="24">
&lt;!&ndash;          <a-col :span="8">
            <a-form-item label="单位:" :labelCol="{span:4}" :wrapperCol="{span:15}">
              <a-input placeholder="" v-model="queryParam.pickingNo"></a-input>
            </a-form-item>
          </a-col>&ndash;&gt;
          <a-col :span="8">
            <a-form-item label="检定线:" :labelCol="{span:4}" :wrapperCol="{span:18}">
              <j-dict-select-tag-ora type="list" :trigger="true" v-model="queryParam.lineNo" v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="o_area,area_name,area_id,new_flag = '1'"
                                     placeholder="请选择检定线" />
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="故障等级" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-radio-group v-model="queryParam.alarmLevel" v-decorator="['alarmLevel', validatorRules.alarmLevel]">
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

        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="系统名称:" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input placeholder="" v-model="queryParam.pickingNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="报警时间:" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startAlarmTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="止" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date   placeholder="请选择结束时间"  v-model="queryParam.endAlarmTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">

          <a-col :span="8">
            <a-form-item label="处理时间起:" :labelCol="{span:12}" :wrapperCol="{span:12}">
                <j-date  placeholder="请选择开始时间" v-model="queryParam.startResolveTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="处理时间止:" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date   placeholder="请选择结束时间"  v-model="queryParam.endResolveTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>


          <a-col :span="8">
            <a-form-item label="工单状态:" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-dict-select-tag type="list"
                                 placeholder=""
                                 v-model="queryParam.processStatus"
                                 dict-code="ORA_ORD_WORK_ORDER_INFO_PROCESS_STATUS"/>
            </a-form-item>
          </a-col>

        </a-row>-->




<!--        <a-row :gutter="24">

          <a-col :span="8">
            <a-form-item label=" 处理人：" :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="this.toggleSearchStatus">
              <a-input placeholder="" v-model="queryParam.processUser"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8" :xl="6" :lg="7" :md="8" :sm="24"  :offset="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>-->


        <a-row :gutter="12">
          <a-col :span="8">
            <a-form-item label="检定线："  :labelCol="{span:9}" :wrapperCol="{span:15}">
              <!--              <j-dict-select-tag type="list"-->
              <!--                                 placeholder=""-->
              <!--                                 v-model="type"-->
              <!--                                 dict-code="IOP_PUB_CATEGORY"/>-->
              <j-dict-select-tag-ora type="list"
                                     :trigger-change="true"
                                     v-model="queryParam.lineNo"
                                     v-decorator="[ 'lineNo', validatorRules.lineNo]"
                                     dictCode="o_area,area_name,area_id,new_flag = '1'"
                                     placeholder="请选择检定线"
                                     @change="changeLine"
                                     style="width: 50%;"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
              <a-select
                v-decorator="[ 'equipName', validatorRules.equipName]"
                v-model="queryParam.equipName"
                placeholder="请选择" style="width: 50%;">
                <a-select-option v-for="item in equipLists" :key="item.value" :value="item.title">
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="工单类型：" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-select
                style="width: 50%;"
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
              <a-radio-group v-model="queryParam.alarmLevel" v-decorator="['alarmLevel', validatorRules.alarmLevel]" >
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

          <a-col :span="8">
            <a-form-item label="采集任务来源：" :labelCol="{span:6}" :wrapperCol="{span:15}">
              <j-dict-select-tag placeholder="采集任务来源" v-model="queryParam.collectionTaskSource"  dictCode="college" style="width: 50%;"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="工单状态：" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-dict-select-tag type="list"
                                 style="width: 50%;"
                                 placeholder=""
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
                                 style="width: 50%;"
                                 v-model="queryParam.dispatchToUser"
                                 dict-code="sys_user,realname,id"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="工单来源：" :labelCol="{span:9}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-dict-select-tag placeholder="工单来源" v-model="queryParam.orderCreLabe"  dictCode="source" style="width: 50%;"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="是否下载：" :labelCol="{span:6}" :wrapperCol="{span:15}" v-if="toggleSearchStatus">
              <j-dict-select-tag placeholder="是否下载" v-model="queryParam.isLoad"  dictCode="is_download" style="width: 50%;"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="下载时间起："  :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="toggleSearchStatus">
              <j-date  placeholder="请选择下载时间起" v-model="queryParam.downloadStartDate"  :trigger-change="true" style="width: 100%;"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="下载时间止："  :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="toggleSearchStatus">
              <j-date  placeholder="请选择下载时间止" v-model="queryParam.downloadEndDate"  :trigger-change="true" style="width: 100%;"/>
            </a-form-item>
          </a-col>

          <a-col :span="4">
            <a-form-item label="报警时间:" :labelCol="{span:12}" :wrapperCol="{span:12}"  v-if="toggleSearchStatus">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startAlarmTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="止" :labelCol="{span:12}" :wrapperCol="{span:12}"  v-if="toggleSearchStatus">
              <j-date   placeholder="请选择结束时间"  v-model="queryParam.endAlarmTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>


          <a-col :span="4">
            <a-form-item label="派发日期起：" 派发时间 :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="this.toggleSearchStatus">
              <j-date  placeholder="请选择派发日期起" v-model="queryParam.distributeStartDate"  :trigger-change="true" style="width: 100%;"/>
            </a-form-item>
          </a-col>
          <a-col :span="4">
            <a-form-item label="派发日期止："  :labelCol="{span:12}" :wrapperCol="{span:12}" v-if="this.toggleSearchStatus">
              <j-date  placeholder="请选择派发日期止" v-model="queryParam.distributeEndDate"  :trigger-change="true" style="width: 100%;"/>
            </a-form-item>
          </a-col>


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
      <a-button @click="handleLook" type="primary" icon="plus">查看详情</a-button>
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

    <workOrderDetail ref="workOrderForm" ></workOrderDetail>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import workOrderDetail from '../todo/modules/workOrderDetail.vue'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDate from '@/components/jeecg/JDate'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'
  export default {
    name: "OrdWorkOrderDetailList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      workOrderDetail,
      JDictSelectTagOra,
      JDate
    },
    data () {
      const vm = this
      return {
        equipLists:[],
        toggleSearchStatus:false,
        description: '故障运维工单列表',
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
                    return '故障工单'
                }else if(text === '09'){
                    return '督办工单'
                }
            }
          },
          {
            title:'检定线',
            align:"center",
            dataIndex: 'lineNo_dictText',
            // customRender:function (text) {
            //     return vm.getcalibrationLineValue(text)
            // }
          },
          {
            title:'设备名称',
            align:"center",
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
                }else {
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
            dataIndex: 'dispatchToUser_dictText'
          },
          {
            title:'派发人',
            align:"center",
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
            dataIndex: 'processUser'
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
                // return vm.getOrdWokOrderStatus(text);
            }
          },
          {
            title:'工单状态',
            align:"center",
            dataIndex: 'processStatus',
            customRender:function(text){
                return vm.getOrdWokOrderStatus(text);
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
            customRender:function(text){
                if(text === 'soft'){
                    return "上位软件"
                }else if(text === 'Electrical'){
                    return '电测'
                }else {
                    return '人工录入'
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
          list: "/workOrder/ordWorkOrderDetail/list",
          detail: "/workOrder/ordWorkOrderDetail/detail",
          delete: "/workOrderInfo/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrderInfo/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrderInfo/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrderInfo/ordWorkOrderInfo/importExcel",
        },
        dictOptions:{},
        tableScroll:{x :47*65+50},
        calibrationLine:[],
        // 工单状态字典
        ordWokOrderStatus:[],
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
      initDictConfig(){
          let lineNo="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'";
          initDictOptionsOra(lineNo).then(res => {
              this.calibrationLine = res.result;
          });
           initDictOptions("ord_work_order_status").then(res => {
             this.ordWokOrderStatus = res.result
          });

      },
      handleLook(){
          if(this.selectedRowKeys.length == 1){
              this.$refs.workOrderForm.look(this.selectedRowKeys[0]);
          }else if(this.selectedRowKeys.length > 1){
              this.$message.warning("只能勾选一条记录")
          }else {
              this.$message.warning("请选择一条记录")
          }
      },
      // getcalibrationLineValue(num){
      //     for(let i = 0;i<this.calibrationLine.length;i++){
      //         if(this.calibrationLine[i].value == num){
      //             return this.calibrationLine[i].title;
      //         }
      //     }
      // },
      getOrdWokOrderStatus(num){
          for(let i = 0;i<this.ordWokOrderStatus.length;i++){
              if(this.ordWokOrderStatus[i].value == num){
                  return this.ordWokOrderStatus[i].title;
              }
          }
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>