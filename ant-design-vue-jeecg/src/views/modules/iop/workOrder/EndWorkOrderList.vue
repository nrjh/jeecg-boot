<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="线体编号">
              <a-input placeholder="请输入线体编号" v-model="queryParam.lineNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
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
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'

  export default {
    name: "OrdWorkOrderDetailList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
    },
    data () {
      return {
        description: '故障运维工单列表',
        // 表头
        columns: [
          {
            title: '',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'工单创建标识',
            align:"center",
            dataIndex: 'orderCreLabe'
          },
          {
            title:'工单类型',
            align:"center",
            dataIndex: 'orderType_dictText'
          },
          {
            title:'报警编号',
            align:"center",
            dataIndex: 'faultId'
          },
          {
            title:'检定任务编号',
            align:"center",
            dataIndex: 'detectTaskNo'
          },
          {
            title:'线体编号',
            align:"center",
            dataIndex: 'lineNo_dictText'
          },
          {
            title:'原系统标识',
            align:"center",
            dataIndex: 'oldId'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'equipNo'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName_dictText'
          },
          {
            title:'报警时间',
            align:"center",
            dataIndex: 'alarmTime',
            customRender: function(text) {
                return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'报警级别',
            align:"center",
            dataIndex: 'alarmLevel_dictText'
          },
          {
            title:'报警来源',
            align:"center",
            dataIndex: 'alarmSource'
          },
          {
            title:'报警原因',
            align:"center",
            dataIndex: 'alarmReason'
          },
          {
            title:'报警描述',
            align:"center",
            dataIndex: 'alarmDesc'
          },
          {
            title:'处理建议',
            align:"center",
            dataIndex: 'alarmSugg'
          },
          {
            title:'报警状态',
            align:"center",
            dataIndex: 'alarmStatus',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'超期完成时间',
            align:"center",
            dataIndex: 'overdurTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
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
            title:'工单接单时间',
            align:"center",
            dataIndex: 'acceptTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'工单派工时间',
            align:"center",
            dataIndex: 'dispatchTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'工单催办时间',
            align:"center",
            dataIndex: 'pressTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'催办标识',
            align:"center",
            dataIndex: 'pressLabe'
          },
          {
            title:'工单锁定时间',
            align:"center",
            dataIndex: 'lockTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'工单处理时间',
            align:"center",
            dataIndex: 'processTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'异常恢复时间',
            align:"center",
            dataIndex: 'resumeTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'工单归档时间',
            align:"center",
            dataIndex: 'recoveryTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'异常处理方式',
            align:"center",
            dataIndex: 'processType',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'工单状态',
            align:"center",
            dataIndex: 'processStatus'
          },
          {
            title:'创建人ID',
            align:"center",
            dataIndex: 'createId',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'创建人姓名',
            align:"center",
            dataIndex: 'createName'
          },
          {
            title:'工单派发人',
            align:"center",
            dataIndex: 'dispatchUser'
          },
          {
            title:'工单现场处理人',
            align:"center",
            dataIndex: 'processUser',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'远程处理状态',
            align:"center",
            dataIndex: 'remoteStatus'
          },
          {
            title:'远程检测是否成功',
            align:"center",
            dataIndex: 'isRemoteSucc'
          },
          {
            title:'是否更换设备',
            align:"center",
            dataIndex: 'meterReplace'
          },
          {
            title:'领料单号',
            align:"center",
            dataIndex: 'orderOutId'
          },
          {
            title:'掌机是否下载',
            align:"center",
            dataIndex: 'isLoad'
          },
          {
            title:'知识库设备类型',
            align:"center",
            dataIndex: 'knEquipType'
          },
          {
            title:'是否挂单',
            align:"center",
            dataIndex: 'isSuspend'
          },
          {
            title:'反馈人',
            align:"center",
            dataIndex: 'fbUser'
          },
          {
            title:'工单派发班组',
            align:"center",
            dataIndex: 'dispatchTeam'
          },
          {
            title:'工单派发对象',
            align:"center",
            dataIndex: 'dispatchToUser'
          },
          {
            title:'恢复类型',
            align:"center",
            dataIndex: 'resumeType'
          },
          {
            title:'派发超期时间',
            align:"center",
            dataIndex: 'dpOverdurTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'反馈超期时间',
            align:"center",
            dataIndex: 'fbOverdurTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'修改人',
            align:"center",
            dataIndex: 'updateBy'
          },
          {
            title:'修改人名称',
            align:"center",
            dataIndex: 'updateName'
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title:'结单时间',
            align:"center",
            dataIndex: 'endTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          }
        ],
        url: {
          list: "/workOrder/ordWorkOrderDetail/endList",
          delete: "/workOrderInfo/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrderInfo/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrderInfo/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrderInfo/ordWorkOrderInfo/importExcel",
        },
        dictOptions:{},
        tableScroll:{x :47*147+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      handleLook(){
        this.$refs.workOrderForm.look();
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>