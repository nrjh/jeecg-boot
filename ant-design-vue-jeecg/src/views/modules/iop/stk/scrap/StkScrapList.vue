<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="报废盘点单号">
              <a-input placeholder="请输入报废盘点单号" v-model="queryParam.scrapInventoryOrder"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="类别">
              <j-dict-select-tag placeholder="请选择类别" v-model="queryParam.type" dictCode="scrap_type"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="申请日期">
                <j-date placeholder="请选择申请日期" v-model="queryParam.applicationDate"></j-date>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-dict-select-tag placeholder="请选择状态" v-model="queryParam.status" dictCode="scrap_status"/>
              </a-form-item>
            </a-col>
          </template>
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
      <!--<a-button @click="handleEdit" type="primary" icon="edit">修改</a-button> 操作栏上有编辑功能，此修改按钮可以不用-->
      <a-button @click="handleCheck(1)" type="primary" icon="search">查看</a-button>
      <a-button @click="handleCheck(2)" type="primary" icon="check">确认</a-button>
      <a-button @click="handleCheck(3)" type="primary" icon="check-square">审核</a-button>
      <a-button @click="handleCheck(4)" type="primary" icon="close">作废</a-button>
         
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

    <stkScrap-modal ref="modalForm" @ok="modalFormOk"></stkScrap-modal>
    <lookScrapModal ref="showLookForm" ></lookScrapModal>
    <sureScrapModal ref="showSureForm" ></sureScrapModal>
    <shenheScrapModal ref="showShenheForm" ></shenheScrapModal>
    <badScrapModal ref="showBadForm" ></badScrapModal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StkScrapModal from './modules/StkScrapModal.vue'
  import lookScrapModal from './modules/lookScrapModal.vue'
  import sureScrapModal from './modules/sureScrapModal.vue'
  import shenheScrapModal from './modules/shenheScrapModal.vue'
  import badScrapModal from './modules/badScrapModal.vue'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "StkScrapList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      StkScrapModal,
      lookScrapModal,
      sureScrapModal,
      shenheScrapModal,
      badScrapModal
    },
    data () {
      return {
        description: '报废管理页面',
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
            title:'报废盘点单号',
            align:"center",
            dataIndex: 'scrapInventoryOrder'
          },
          {
            title:'类别',
            align:"center",
            dataIndex: 'type',
            customRender:function (value) {
            if(value=='1'){
              return '报废';
            }else if(value=='2'){
              return '盘点';
              }
            }
          },
          {
            title:'申请人',
            align:"center",
            dataIndex: 'applicationWorker'
          },
          {
            title:'申请日期',
            align:"center",
            dataIndex: 'applicationDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status',
             customRender:function (value) {
            if(value=='1'){
              return '保存';
            }else if(value=='2'){
              return '提交';
            } else if(value=='3'){
              return '确认';
            } else if(value=='4') {
              return '审核';
            } else if(value=='5') {
              return '作废';
            }
      }
          },        
          {
            title:'审批信息',
            align:"center",
            dataIndex: 'checkInfo'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/scrap/stkScrap/list",
          delete: "/scrap/stkScrap/delete",
          deleteBatch: "/scrap/stkScrap/deleteBatch",
          exportXlsUrl: "/scrap/stkScrap/exportXls",
          importExcelUrl: "scrap/stkScrap/importExcel",
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
      },
     
      handleCheck(v){
        if(this.selectedRowKeys.length==1){
          //console.log("报废单据号",this.selectedRowKeys[0]);
            if(v==1){//查看按钮
              this.$refs.showLookForm.showCheck(this.selectedRowKeys[0],v);
            }else if(v==2){//确认按钮
              this.$refs.showSureForm.showCheck(this.selectedRowKeys[0],v);
            }else if(v==3){//审核按钮
              this.$refs.showShenheForm.showCheck(this.selectedRowKeys[0],v);
            }else{//作废按钮
              this.$refs.showBadForm.showCheck(this.selectedRowKeys[0],v);
            }
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