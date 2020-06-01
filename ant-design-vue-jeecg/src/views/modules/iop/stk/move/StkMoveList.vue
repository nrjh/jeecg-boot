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
            <a-form-item label="物品">
              <a-input placeholder="请输入物品" v-model="queryParam.productId"></a-input>
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
        <template v-if="toggleSearchStatus">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <JDictSelectTag v-model="queryParam.state" placeholder="请选择状态" dictCode="IOP_STK_MOVE_STATE"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="作业类型">
                <JDictSelectTagIop v-model="queryParam.pickingTypeId" placeholder="请选择作业类型" dictCode="stk_picking_type,name,id"/>
              </a-form-item>
            </a-col>
          </a-row>
        </template>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--
      <a-button type="primary" icon="download" @click="handleExportXls('移库单')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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

    <stkMove-modal ref="modalForm" @ok="modalFormOk"></stkMove-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StkMoveModal from './modules/StkMoveModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'StkMoveList',
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,JDictSelectTagIop,
      StkMoveModal
    },
    data() {
      return {
        description: '移库单管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '作业类型',
            align: 'center',
            dataIndex: 'pickingTypeId_dictText'
          },
          {
            title: '物品',
            align: 'center',
            dataIndex: 'productId_dictText'
          },
          {
            title: '计量单位',
            align: 'center',
            dataIndex: 'productUomId_dictText'
          },
          {
            title: '实际数量',
            align: 'center',
            dataIndex: 'productQty'
          },
          {
            title: '需求数量',
            align: 'center',
            dataIndex: 'productUomQty'
          },
          {
            title: '仓库',
            align: 'center',
            dataIndex: 'warehouseId_dictText'
          },
          {
            title: '源位置',
            align: 'center',
            dataIndex: 'locationSrcId_dictText'
          },
          {
            title: '目标位置',
            align: 'center',
            dataIndex: 'locationDestId_dictText'
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'state_dictText'
          },
          // {
          //   title:'批次/序列号',
          //   align:"center",
          //   dataIndex: 'productLotId'
          // },
          // {
          //   title:'源单据',
          //   align:"center",
          //   dataIndex: 'origin'
          // },
          //
          // {
          //   title:'调拨单',
          //   align:"center",
          //   dataIndex: 'pickingId'
          // },
          // {
          //   title:'盘点单',
          //   align:"center",
          //   dataIndex: 'inventoryId'
          // },
          // {
          //   title:'是否退回移库',
          //   align:"center",
          //   dataIndex: 'toRefund'
          // },
          // {
          //   title:'关联源移库单',
          //   align:"center",
          //   dataIndex: 'originReturnedMoveId'
          // },
          // {
          //   title:'关联单据说明',
          //   align:"center",
          //   dataIndex: 'reference'
          // },
          //
          // {
          //   title:'实际执行时间',
          //   align:"center",
          //   dataIndex: 'dateDone'
          // },
          // {
          //   title:'预计执行时间',
          //   align:"center",
          //   dataIndex: 'dateExpected'
          // },
          // {
          //   title:'说明',
          //   align:"center",
          //   dataIndex: 'note'
          // },
          // {
          //   title:'单位',
          //   align:"center",
          //   dataIndex: 'companyId_dictText'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/iop/stk/move/list',
          delete: '/iop/stk/move/delete',
          deleteBatch: '/iop/stk/move/deleteBatch',
          exportXlsUrl: '/iop/stk/move/exportXls',
          importExcelUrl: 'iop/stk/move/importExcel'
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
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>