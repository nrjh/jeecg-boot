<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="托盘编号" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input placeholder="请输入托盘编号查询" v-model="queryParam.palletNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属货位" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input placeholder="请输入所属货位查询" v-model="queryParam.storeId"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!--
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
              -->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('托盘维护')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"

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

    <iopOReservePallet-modal ref="modalForm" @ok="modalFormOk"></iopOReservePallet-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import IopOReservePalletModal from './modules/IopOReservePalletModal'
  import { initDictOptions,initDictOptionsIop, filterDictText } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'IopOReservePalletList',
    mixins: [JeecgListMixin],
    components: {
      IopOReservePalletModal
    },
    data() {
      return {
        description: '托盘维护管理页面',
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
            title: '所属货位',
            align: 'center',
            dataIndex: 'storeId',
            customRender: (text, record, index) => {
              return filterDictText(this.storeDictOptions, text)
            }
          },
          {
            title: '编号',
            align: 'center',
            dataIndex: 'palletNo'
          },
          {
            title: '序号',
            align: 'center',
            dataIndex: 'palletOrder'
          },
          {
            title: '尺寸',
            align: 'center',
            dataIndex: 'palletSize'
          },
          {
            title: '颜色',
            align: 'center',
            dataIndex: 'palletColor'
          },
          {
            title: '制作材质',
            align: 'center',
            dataIndex: 'palletMaterial'
          },
          {
            title: '是否虚拟',
            align: 'center',
            dataIndex: 'storeIsvirtual',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictText(this.torfDictOptions, text)
            }
          },
          {
            title: '操作时间',
            align: 'center',
            dataIndex: 'optDate',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '操作人',
            align: 'center',
            dataIndex: 'optCode'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/iop/reserve/iopOReservePallet/list',
          delete: '/iop/reserve/iopOReservePallet/delete',
          deleteBatch: '/iop/reserve/iopOReservePallet/deleteBatch',
          exportXlsUrl: '/iop/reserve/iopOReservePallet/exportXls',
          importExcelUrl: 'reserve.pallet/iopOReservePallet/importExcel'
        },
        dictOptions: {}
      }
    },
    created() {
      this.initDictConfig()
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      initDictConfig() {
        //初始化字典 - 是否虚拟
        initDictOptions('IOP_TORF').then((res) => {
          if (res.success) {
            this.torfDictOptions = res.result
          }
        })
        // 初始化库房字典
        initDictOptionsIop('IOP_O_RESERVE_STORE,STORE_NAME,ID,STORE_LEVEL=4').then((res) => {
          if (res.success) {
            this.storeDictOptions = res.result
          }
        })
      }
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>