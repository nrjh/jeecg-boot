<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="任务号:" :labelCol="{span:4}" :wrapperCol="{span:15}">
              <a-input placeholder=""></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="状态:" :labelCol="{span:4}" :wrapperCol="{span:15}">

              <j-dict-select-tag type="list"
                                 placeholder=""
                                 v-model="queryParam.taskStatus"
                                 dict-code="PRD_TASK_MONITOR_STATUS"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="线体名称:" :labelCol="{span:4}" :wrapperCol="{span:15}">
              <j-dict-select-tag-ora type="list" :trigger="true" v-model="queryParam.lineNo" dictCode="o_area,area_name,area_id"
                                     placeholder="请选择检定线" />
            </a-form-item>
          </a-col>


        </a-row>

        <a-row :gutter="24">

          <a-col :span="8">
            <a-form-item label="供应商:" :labelCol="{span:4}" :wrapperCol="{span:15}" >
              <a-input placeholder="" v-model="queryParam.supplyNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8" :xl="6" :lg="7" :md="8" :sm="24"  :offset="10">
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
        :pagination="false"
        :loading="loading"
        :scroll="tableScroll"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

      </a-table>
    </div>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'

  export default {
    name: "MtDetectTaskInfoList",
    mixins:[JeecgListMixin],
    components: {
        JDictSelectTag,
        JDictSelectTagOra
    },
    data () {
      return {
        description: '检定任务信息(新平台)实体管理页面',
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
            title:'任务号',
            align:"center",
            dataIndex: 'detectTaskNo'
          },
          {
              title:'到货的批次号',
              align:"center",
              dataIndex: 'arriveBatchNo'
          },
          {
              title:'线体名称',
              align:"center",
              dataIndex: 'lineNo'
          },
          {
              title:'任务类型',
              align:"center",
              dataIndex: 'taskType'
          },
          {
              title:'供应商',
              align:"center",
              dataIndex: 'supplyNo'
          },
          {
              title:'模块类型',
              align:"center",
              dataIndex: 'modularType'
          },
          {
              title:'任务状态',
              align:"center",
              dataIndex: 'taskStatus'
          },
          {
              title:'启动时间',
              align:"center",
              dataIndex: 'detectStartDate'
          },
          {
              title:'预计完成时间',
              align:"center",
              dataIndex: 'detectEndDate'
          },
          {
              title:'运行状态',
              align:"center",
              dataIndex: 'workingStatus'
          },
          {
              title:'任务量',
              align:"center",
              dataIndex: 'equipQty'
          },
          {
              title:'已检定量',
              align:"center",
              dataIndex: 'hadCheckNum'
          },
          {
              title:'合格量',
              align:"center",
              dataIndex: 'detectConcP'
          },
          {
              title:'合格率',
              align:"center",
              dataIndex: 'passRate'
          },
          {
              title:'执行进度',
              align:"center",
              dataIndex: 'taskRate'
          }
        ],
        url: {
          list: "/ptm/mtDetectTaskInfo/checkTaskMonitorList",
        },
        dictOptions:{},
        tableScroll:{x :47*40+50},
        toggleSearchStatus:false
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>