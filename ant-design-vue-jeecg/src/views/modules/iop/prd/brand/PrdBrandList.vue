<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="品牌名称">
              <a-input placeholder="请输入品牌名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <JDictSelectTag  v-model="queryParam.active" placeholder="请选择状态" dictCode="IOP_PUB_ACTION"/>
            </a-form-item>
          </a-col>


          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
            </span>
          </a-col>

        </a-row>

        <a-row :gutter="24">
          <template v-if="true">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="首字母">
                <a-input placeholder="请输入首字母" v-model="queryParam.firstLetter"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="制造商">
                <!-- 下拉
                <j-dict-select-tag-iop v-model="queryParam.manufactorId" placeholder="请选择制造商"
                                   dictCode="RES_PARTNER,NAME,ID"/>
                -->
                <!-- 异步搜索 -->
<!--                <j-search-select-tag-iop-->
<!--                  placeholder="请选择制造商"-->
<!--                  v-model="queryParam.manufactorId"-->
<!--                  dict="RES_PARTNER,NAME,ID"-->
<!--                  :async="true">-->
<!--                </j-search-select-tag-iop>-->
                <a-input placeholder="请输入制造商" v-model="queryParam.manufactorName"></a-input>
              </a-form-item>
            </a-col>
          </template>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd('品牌 / 新建')" type="primary" icon="plus">新建</a-button>
      <!--
      <a-button type="primary" icon="download" @click="handleExportXls('品牌')">导出</a-button>
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
          <a @click="handleEdit(record,'品牌 / 编辑')">编辑</a>
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

    <prdBrand-modal ref="modalForm" @ok="modalFormOk"></prdBrand-modal>
    <prdBrand-modalView ref="modalFormView" @ok=""></prdBrand-modalView>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PrdBrandModal from './modules/PrdBrandModal'
  import PrdBrandModalView from './modules/PrdBrandModalView'
  import { initDictOptions,initDictOptionsIop, filterDictText } from '@/components/dict/JDictSelectUtil'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JSearchSelectTagIop from '@/components/dict/JSearchSelectTagIop'


  export default {
    name: "PrdBrandList",
    mixins:[JeecgListMixin],
    components: {
      PrdBrandModal,
      PrdBrandModalView,
      JDictSelectTag,
      JDictSelectTagIop,
      JSearchSelectTagIop
    },
    data () {
      return {
        description: '品牌管理页面',
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
            title:'首字母',
            align:"center",
            dataIndex: 'firstLetter'
          },
          {
            title:'品牌名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'制造商',
            align:"center",
            dataIndex: 'manufactorName',
            // customRender: (text, record, index) => {
            //   return filterDictText(this.manufactorDictOptions, text)
            // }
          },
          {
            title: '状态',
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
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/iop/prd/brand/list",
          delete: "/iop/prd/brand/delete",
          deleteBatch: "/iop/prd/brand/deleteBatch",
          exportXlsUrl: "/iop/prd/brand/exportXls",
          importExcelUrl: "/iop/prd/brand/importExcel",
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

        // 初始化 制造商
        // initDictOptionsIop('RES_PARTNER,NAME,ID').then((res) => {
        //   if (res.success) {
        //     this.manufactorDictOptions = res.result
        //   }
        // });
      },
      handleEdit: function (record,title) {
        this.$refs.modalForm.edit(record);
        if(!title ||  typeof(title) != 'string'){
          title = "编辑";
        }
        this.$refs.modalForm.title = title;
        this.$refs.modalForm.disableSubmit = false;
      },
      handleAdd: function (title) {
        this.$refs.modalForm.add();
        if(!title || typeof(title) != 'string'){
          title = "新增";
        }
        this.$refs.modalForm.title = title;
        this.$refs.modalForm.disableSubmit = false;
      },
      // 查看详情页面
      handleDetail:function(record,title){
        this.$refs.modalFormView.detail(record);
        if(!title ||  typeof(title) != 'string'){
          title = "详情";
        }
        this.$refs.modalFormView.title=title;
        this.$refs.modalFormView.disableSubmit = false;
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>