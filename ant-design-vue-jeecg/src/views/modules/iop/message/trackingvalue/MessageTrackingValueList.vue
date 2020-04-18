<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="字段描述">
              <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.fieldDesc_begin"></a-input>
              <span class="query-group-split-cust"></span>
              <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.fieldDesc_end"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="消息">
              <a-input placeholder="请输入最小值" class="query-group-cust" v-model="queryParam.messageId_begin"></a-input>
              <span class="query-group-split-cust"></span>
              <a-input placeholder="请输入最大值" class="query-group-cust" v-model="queryParam.messageId_end"></a-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('变更记录')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
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
        
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <messageTrackingValue-modal ref="modalForm" @ok="modalFormOk"></messageTrackingValue-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MessageTrackingValueModal from './modules/MessageTrackingValueModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "MessageTrackingValueList",
    mixins:[JeecgListMixin],
    components: {
      MessageTrackingValueModal
    },
    data () {
      return {
        description: '变更记录管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'字段代码',
            align:"center",
            dataIndex: 'field'
          },
          {
            title:'字段描述',
            align:"center",
            dataIndex: 'fieldDesc'
          },
          {
            title:'字段类型',
            align:"center",
            dataIndex: 'fieldType'
          },
          {
            title:'旧整数值',
            align:"center",
            dataIndex: 'oldValueInteger'
          },
          {
            title:'旧浮点值',
            align:"center",
            dataIndex: 'oldValueFloat'
          },
          {
            title:'旧货币值',
            align:"center",
            dataIndex: 'oldValueMonotary'
          },
          {
            title:'旧字符值',
            align:"center",
            dataIndex: 'oldValueChar'
          },
          {
            title:'旧文本值',
            align:"center",
            dataIndex: 'oldValueText'
          },
          {
            title:'旧日期时间值',
            align:"center",
            dataIndex: 'oldValueDatetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'新整数值',
            align:"center",
            dataIndex: 'newValueInteger'
          },
          {
            title:'新浮点值',
            align:"center",
            dataIndex: 'newValueFloat'
          },
          {
            title:'新货币值',
            align:"center",
            dataIndex: 'newValueMonotary'
          },
          {
            title:'新字符值',
            align:"center",
            dataIndex: 'newValueChar'
          },
          {
            title:'新文本值',
            align:"center",
            dataIndex: 'newValueText'
          },
          {
            title:'新日期时间值',
            align:"center",
            dataIndex: 'newValueDatetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'消息',
            align:"center",
            dataIndex: 'messageId'
          },
          {
            title:'跟踪序号',
            align:"center",
            dataIndex: 'trackingSequence'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/iop/message/trackingvalue/list",
          delete: "/iop/message/trackingvalue/delete",
          deleteBatch: "/iop/message/trackingvalue/deleteBatch",
          exportXlsUrl: "/iop/message/trackingvalue/exportXls",
          importExcelUrl: "/iop/message/trackingvalue/importExcel",
        },
        dictOptions:{},
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