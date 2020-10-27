<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="任务号:" :labelCol="{span:4}" :wrapperCol="{span:15}">
              <a-input placeholder="" v-model="queryParam.taskNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="出入类别:" :labelCol="{span:4}" :wrapperCol="{span:15}">
              <j-dict-select-tag type="list"
                                 placeholder=""
                                 v-model="queryParam.ioFlag"
                                 dict-code="PRD_TASK_MONITOR_IO_TYPE"/>
            </a-form-item>
          </a-col>


          <a-col :span="6" :xl="6" :lg="7" :md="8" :sm="24"  :offset="6">
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="tableScroll"
        @change="handleTableChange">

      </a-table>
    </div>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "DIoDetList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: 'D_IO_DET管理页面',
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
            dataIndex: 'taskNo'
          },
          {
            title:'出入类别',
            align:"center",
            dataIndex: 'ioFlag_dictText',

          },
          {
            title:'任务类别',
            align:"center",
            dataIndex: 'taskPrio'
          },
          {
            title:'启动时间',
            align:"center",
            dataIndex: 'createDate'
          },
          {
            title:'任务量',
            align:"center",
            dataIndex: 'qty'
          },
          {
            title:'已完成量',
            align:"center",
            dataIndex: 'finishedIoQty'
          },
          {
            title:'执行进度',
            align:"center",
            dataIndex: 'taskRate'
          }
        ],
        url: {
          list: "/ptm/dIoTask/list",
        },
        dictOptions:{},
        tableScroll:{x :47*30}
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