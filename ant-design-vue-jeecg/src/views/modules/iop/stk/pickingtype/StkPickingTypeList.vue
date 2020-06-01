<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="作业类型">
              <JDictSelectTag v-model="queryParam.code" placeholder="请选择作业类型" dictCode="IOP_STK_PICKING_TYPE"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('作业类型')">导出</a-button>
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

    <stkPickingType-modal ref="modalForm" @ok="modalFormOk"></stkPickingType-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StkPickingTypeModal from './modules/StkPickingTypeModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'

  export default {
    name: "StkPickingTypeList",
    mixins:[JeecgListMixin],
    components: {
      StkPickingTypeModal,JDictSelectTag
    },
    data () {
      return {
        description: '作业类型管理页面',
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
            title:'名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'作业类型',
            align:"center",
            dataIndex: 'code_dictText'
          },
          {
            title:'颜色',
            align:"center",
            dataIndex: 'color_dictText'
          },
          {
            title:'默认源位置',
            align:"center",
            dataIndex: 'defaultLoactionSrcId_dictText'
          },
          {
            title:'默认目标位置',
            align:"center",
            dataIndex: 'defaultLocationDestId_dictText'
          },
          {
            title:'退回作业类型',
            align:"center",
            dataIndex: 'returnPickingTypeId_dictText'
          },
          {
            title:'整个包裹',
            align:"center",
            dataIndex: 'showEntirePacks_dictText'
          },
          {
            title:'仓库',
            align:"center",
            dataIndex: 'warehouseId_dictText'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'active_dictText'
          },
          // {
          //   title:'使用新建批号',
          //   align:"center",
          //   dataIndex: 'useCreateLots'
          // },
          // {
          //   title:'利用已存在批号',
          //   align:"center",
          //   dataIndex: 'useExstingLots'
          // },
          // {
          //   title:'详细作业',
          //   align:"center",
          //   dataIndex: 'showOperations'
          // },
          // {
          //   title:'被保留详细作业',
          //   align:"center",
          //   dataIndex: 'showReserved'
          // },
          // {
          //   title:'条码',
          //   align:"center",
          //   dataIndex: 'barcode'
          // },
          // {
          //   title:'单位',
          //   align:"center",
          //   dataIndex: 'companyId'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/iop/stk/pickingtype/list",
          delete: "/iop/stk/pickingtype/delete",
          deleteBatch: "/iop/stk/pickingtype/deleteBatch",
          exportXlsUrl: "/iop/stk/pickingtype/exportXls",
          importExcelUrl: "/iop/stk/pickingtype/importExcel",
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