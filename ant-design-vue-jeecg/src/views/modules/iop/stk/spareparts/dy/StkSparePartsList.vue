<template>
  <a-card :bordered="false">
    <!--定义备品备件页面 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="4" :sm="16">
            <a-form-item label="物料编号">
              <a-input placeholder="请输入物料编号" v-model="queryParam.productNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称缩写">
              <a-input placeholder="请输入物料名称缩写" v-model="queryParam.productNameAbb"></a-input>
            </a-form-item>
          </a-col>


        </a-row>
        <a-row :gutter="24">

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料类别">
<!--              <a-input placeholder="请输入物料类别" v-model="queryParam.categoryId"></a-input>-->
<!--              <JDictSelectTag :triggerChange="true"-->
<!--                              placeholder="请输入物料类别"-->
<!--                              dictCode="IOP_PRD_CATEGORY_ID" v-model='queryParam.categoryId'/>-->
              <j-dict-select-tag-iop placeholder="物料类别" v-model="queryParam.categoryId" dictCode="prd_category,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料状态">
<!--              <a-input placeholder="请输入物料状态" v-model="queryParam.statusPro"></a-input>-->
<!--              <j-dict-select-tag-iop placeholder="请输入物料状态" v-model="queryParam.statusPro" dictCode="STK_SPARE_PARTS,STATUS_PRO,ID"/>-->
              <j-dict-select-tag placeholder="请输入物料状态" v-model="queryParam.statusPro" dictCode="IOP_STK_SPARE_PARTS_STATUS_PRO"/>
            </a-form-item>
          </a-col>

          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleCheck" type="primary" icon="search">查看</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('备品备件')">导出</a-button>
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
        :scroll="tableScroll"
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

    <stkSpareParts-modal ref="modalForm" @ok="modalFormOk"></stkSpareParts-modal>
    <lookSparePartsModal ref="showLookForm" ></lookSparePartsModal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StkSparePartsModal from './modules/StkSparePartsModal.vue'
  import lookSparePartsModal from './modules/lookSparePartsModal.vue'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  export default {
    name: "StkSparePartsList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDictSelectTagIop,
      StkSparePartsModal,
      lookSparePartsModal
    },
    data () {
      return {
        toggleSearchStatus:false,
        description: '备品备件管理页面',
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
            title:'物料编号',
            align:"center",
            dataIndex: 'productNo'
          },
          {
            title:'物料名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'物料类别',
            align:"center",
            dataIndex: 'categoryId_dictText',
            customRender:function (text) {
             return '备品备件'
            }
          },
          {
            title:'品牌',
            align:"center",
            dataIndex: 'brandId_dictText'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'attributeCategoryId_dictText'
          },
          {
            title:'物料名称缩写',
            align:"center",
            dataIndex: 'productNameAbb'
          },
          //{
          //  title:'标识',
          //  align:"center",
          //  dataIndex: 'productNameAbb'
          //},
          {
            title:'单位',
            align:"center",
            dataIndex: 'uomId_dictText'
          },
          {
            title:'初始数量',
            align:"center",
            dataIndex: 'initQty'
          },
          {
            title:'初始金额',
            align:"center",
            dataIndex: 'initAmount'
          },
          {
            title:'当前均价',
            align:"center",
            dataIndex: 'priceAvg'
          },
          {
            title:'最低库存',
            align:"center",
            dataIndex: 'stockMin'
          },
          {
            title:'最高库存',
            align:"center",
            dataIndex: 'stockMax'
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'locationId_dictText'
          },
          {
            title:'物料状态',
            align:"center",
            dataIndex: 'statusPro_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            width:150,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/spareparts/stkSpareParts/list?flag=1",
          delete: "/spareparts/stkSpareParts/delete",
          deleteBatch: "/spareparts/stkSpareParts/deleteBatch",
          exportXlsUrl: "/spareparts/stkSpareParts/exportXls",
          importExcelUrl: "spareparts/stkSpareParts/importExcel",
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
      },
      handleCheck(){
        if(this.selectedRowKeys.length==1){
          //查看按钮
          console.log("id:",this.selectedRowKeys[0]);
          this.$refs.showLookForm.showCheck(this.selectedRowKeys[0]);

        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>