<template>
  <a-card :bordered="false">
    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
-->
      <a-button @click="handleCheck" type="primary" icon="search">试算</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
<!--      <a-table
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
      </a-table>-->
    </div>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'

  export default {
    name: "SimplexList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag
    },
    data () {
      return {
        description: '办公用品管理页面',
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
            title:'品类',
            align:"center",
            dataIndex: 'categoryId_dictText'
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
            title:'缩写',
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
            title:'状态',
            align:"center",
            dataIndex: 'statusPro'
          },         
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/officesupplies/stkOfficeSupplies/list",
          delete: "/officesupplies/stkOfficeSupplies/delete",
          deleteBatch: "/officesupplies/stkOfficeSupplies/deleteBatch",
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