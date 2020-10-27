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
            <a-form-item label="状态">
              <j-dict-select-tag v-model="queryParam.active" placeholder="请选择状态" dictCode="IOP_PUB_ACTION"/>
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
        <a-row :gutter="24">
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="品类类型">
                <j-dict-select-tag  v-model="queryParam.categoryType" placeholder="请选择品类类型" dictCode="IOP_PRD_CATEGORY_TYPE"/>
              </a-form-item>
            </a-col>
          </template>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--
      <a-button type="primary" icon="download" @click="handleExportXls('品类')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      -->
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
          <a @click="handleDetail(record,'品牌 / 详情')">查看</a>
          &nbsp;&nbsp;
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

    <prdCategory-modal ref="modalForm" @ok="modalFormOk"></prdCategory-modal>
    <prdCategory-modalView ref="modalFormView" @ok="modalFormOk"></prdCategory-modalView>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PrdCategoryModal from './modules/PrdCategoryModal'
  import PrdCategoryModalView from './modules/PrdCategoryModalView'
  import { initDictOptions,initDictOptionsIop, filterDictText } from '@/components/dict/JDictSelectUtil'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'

  export default {
    name: "PrdCategoryList",
    mixins:[JeecgListMixin],
    components: {
      PrdCategoryModal,
      PrdCategoryModalView,
      JDictSelectTag,
      JDictSelectTagIop
    },
    data () {
      return {
        description: '品类管理页面',
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
            align:"left",
            dataIndex: 'name'
          },
          {
            title:'全名',
            align:"left",
            dataIndex: 'completeName'
          },
          {
            title:'品类类型',
            align:"center",
            dataIndex: 'categoryType',
            customRender: (text, record, index) => {
              return filterDictText(this.categporyDictOptions, text)
            }
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'active',
            customRender: (text, record, index) => {
              return filterDictText(this.activeDictOptions, text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/iop/prd/category/list",
          delete: "/iop/prd/category/delete",
          deleteBatch: "/iop/prd/category/deleteBatch",
          exportXlsUrl: "/iop/prd/category/exportXls",
          importExcelUrl: "/iop/prd/category/importExcel",
        },
        dictOptions:{},
      }
    },
    created() {
      this.initDictConfig();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        //初始化字典 - 启用状态
        initDictOptions('IOP_PUB_ACTION').then((res) => {
          if (res.success) {
            this.activeDictOptions = res.result
          }
        });
        //初始化字典 - 品类类型   分类
        initDictOptions('IOP_PRD_CATEGORY_TYPE').then((res) => {
          if (res.success) {
            this.categporyDictOptions = res.result
          }
        });
      },

      // 查看详情页面
      handleDetail:function(record,title){
        this.$refs.modalFormView.detail(record);
        if(!title ||  typeof(title) != 'string'){
          title = "详情";
        }
        this.$refs.modalFormView.title=title;
        this.$refs.modalFormView.disableSubmit = true;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>