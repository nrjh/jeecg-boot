<template>
  <a-card :bordered="false">
    <!-- 办公用品列表页面 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料编号">
              <a-input placeholder="请输入物料编号" v-model="queryParam.productNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="供应商">
                <a-input placeholder="请输入供应商" v-model="queryParam.vendorId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="物料类别">
                <a-input placeholder="请输入物料类别" v-model="queryParam.categoryId"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="入库日期">
                <j-date  placeholder="请选择开始时间" v-model="queryParam.enterDate"  :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="至">
                <j-date   placeholder="请选择结束时间"    :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="出入库类型">
                <a-input placeholder="请出入库类型" v-model="queryParam.inOutType"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="物料状态">
                <a-input placeholder="请输入物料状态" v-model="queryParam.statusPro"></a-input>
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
      <a-button @click="handleCheck" type="primary" icon="search">查看</a-button>

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


      </a-table>
    </div>
    <lookOfficeSuppliesModal ref="showLookForm" ></lookOfficeSuppliesModal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDate from '@/components/jeecg/JDate'
  import lookOfficeSuppliesModal from '../list/modules/lookOfficeSuppliesModal.vue'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'

  export default {
    name: "OfficeSuppliesList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      lookOfficeSuppliesModal
    },
    data () {
      return {
        description: '办公用品列表页面',
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
            title:'物料名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'物料编号',
            align:"center",
            dataIndex: 'productNo'
          },

          {
            title:'品类',
            align:"center",
            dataIndex: 'categoryId_dictText'
          },
          {
            title:'缩写',
            align:"center",
            dataIndex: 'productNameAbb'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'attributeCategoryId_dictText'
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
            title:'供应商',
            align:"center",
            dataIndex: 'vendorId'
          },
          {
            title:'物料状态',
            align:"center",
            dataIndex: 'statusPro',
            customRender:function (text) {
              if(text=='normal'){
                return '正常'
              }else{
                return '冻结'
              }
            }
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'locationId_dictText'
          },
          {
            title:'库存件数',
            align:"center",
            dataIndex: 'productQty'
          },
           {
            title:'出入库时间',
            align:"center",
            dataIndex: 'enterDate'
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
            title:'库存年限',
            align:"center",
            dataIndex: 'stockYear'
          },
        ],
        url: {
          list: "/officesupplies/stkOfficeSupplies/list?flag=2",
          exportXlsUrl: "/officesupplies/stkOfficeSupplies/exportXls",
          importExcelUrl: "officesupplies/stkOfficeSupplies/importExcel",

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