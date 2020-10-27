<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="采购入库单号">
              <a-input placeholder="请输入采购入库单号" v-model="queryParam.orderInId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="采购员">
              <a-input placeholder="请输入采购员" v-model="queryParam.createName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <j-dict-select-tag placeholder="请选择状态" v-model="queryParam.state" dictCode="IOP_STK_IN_STATE"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料编号">
              <a-input placeholder="请输入物料编号" v-model="queryParam.defaultCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="9" :md="10" :sm="24">
            <a-form-item label="采购日期">
              <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="5" :md="6" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAddInOrder" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleRead" type="primary">查看</a-button>
      <a-button @click="handleStockIn" type="primary">入库</a-button>
      <a-button @click="handleCheck" type="primary">审核</a-button>
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('入库单')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
      <!--                @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <!--      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
      <!--        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{-->
      <!--        selectedRowKeys.length }}</a>项-->
      <!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
      <!--      </div>-->

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

        <span slot="action" slot-scope="text, record,index">
          <a v-if="record.state == 'draft'" @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-popconfirm v-if="record.state == 'draft'" title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <stockInOrder-modal ref="modalForm" @ok="modalFormOk"></stockInOrder-modal>
    <lookOrderModal ref="lookModalForm" @ok="modalFormOk"></lookOrderModal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StockInOrderModal from './modules/StockInOrderModal'
  import lookOrderModal from './modules/lookOrderModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDate from '@/components/jeecg/JDate.vue'
  import { initDictOptionsIop, filterDictText } from '@/components/dict/JDictSelectUtil'
  import moment from 'moment'
  import store from "@/store"
  import {initDictOptionsOra} from "../../../../../components/dict/JDictSelectUtil";
  export default {
    name: 'StockInOrderList',
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      JDate,
      StockInOrderModal,
      lookOrderModal
    },
    data() {
      const vm = this
      return {
        dataSource:[],
        description: '入库单管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '采购入库单号',
            align: 'center',
            width: 180,
            dataIndex: 'orderInId'
          },
          {
            title: '采购单号',
            align: 'center',
            width: 180,
            dataIndex: 'pickingNo'
          },
          {
            title: '合同单号',
            align: 'center',
            dataIndex: 'contract'
          },
          // {
          //   title: '供应商',
          //   align: 'center',
          //   dataIndex: 'partnerName'
          // },
          {
            title: '采购员',
            align: 'center',
            dataIndex: 'createName'
          },
          {
            title: '采购日期',
            align: 'center',
            dataIndex: 'createTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '入库物料列表',
            align: 'center',
            dataIndex: 'allInPro',
            ellipsis: true,
            customRender: function(t, r, index) {
              let stockInOrderLineList = r.stockInOrderLineList
              return vm.formatterLine(stockInOrderLineList)
            }
          }, {
            title: '待入库物料列表',
            align: 'center',
            dataIndex: 'toInPro',
            ellipsis: true,
            customRender: function(t, r, index) {
              let stockInOrderLineList = r.stockInOrderLineList
              let list = vm._.filter(stockInOrderLineList, { 'status': 'freeze' })
              return vm.formatterLine(list)
            }
          },
          {
            title: '入库完成时间',
            align: 'center',
            dataIndex: 'orderInDate',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '提交状态',
            align: 'center',
            dataIndex: 'state',
            customRender:function (text) {
              return vm.getState(text);
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        ipagination: {
          current: 1,
          pageSize: 15,
          pageSizeOptions: ['15', '30', '45'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        url: {
          list: '/stk/in/stockInOrder/list',
          delete: '/stk/in/stockInOrder/delete',
          deleteBatch: '/stk/in/stockInOrder/deleteBatch',
          exportXlsUrl: '/stk/in/stockInOrder/exportXls',
          importExcelUrl: 'stk/in/stockInOrder/importExcel'
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
      getState(text){
        var data = this.dictOptions.state;
        for (var i = 0; i < data.length; i++){
          if (data[i].value == text){
            return data[i].text;
          }
        }
      },
      initDictConfig() {
        initDictOptionsIop('prd_attribute_category,name,id').then(res => {
          this.dictOptions.categoryDict = res.result
        })
        initDictOptionsIop('uom_uom,name,id').then(res => {
          this.dictOptions.unitDict = res.result
        })
        //查询数据字典
        initDictOptionsOra('IOP_STK_IN_STATE').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.dictOptions.state = res.result;
        })
      },
      formatterLine(stockInOrderLineList) {
        let str = ''
        for (let i = 0; i < stockInOrderLineList.length; i++) {
          str += '' + this.stringTrans(stockInOrderLineList[i]['productName'])
            + '[' + this.stringTrans(filterDictText(this.dictOptions.categoryDict, stockInOrderLineList[i]['attributeCategoryId'])) + ']'
            + ':' + this.stringTrans(stockInOrderLineList[i]['productActualQty'] )+ this.stringTrans(filterDictText(this.dictOptions.unitDict, stockInOrderLineList[i]['productUomId']))
            + '-' + this.stringTrans(stockInOrderLineList[i]['priceTotal']) + '元'
          str += ';'
        }
        return str
      },
      stringTrans(str){
        if(str==null || str=='null' || str.length==0){
          return '';
        }else {
          return str;
        }
      },
      handleRead() {
        if (this.selectedRowKeys.length == 1) {
          this.$refs.lookModalForm.look(this.selectionRows[0]);
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning('只能勾选一条记录')
        } else {
          this.$message.warning('请选择一条记录')
        }
        this.clearSelected();
      },
      handleStockIn() {
        if (this.selectedRowKeys.length == 1) {
          let record = this.selectionRows[0]
          if (record.state != 'submit') {
            this.$message.warning('只能对待入库的记录进行入库操作')
          } else {
            // var paramsData = {}
            var paramsData = this.selectionRows[0]
            var now = moment().format('YYYY-MM-DD HH:mm:ss')
            paramsData.orderInDate = now
            let userName = store.getters.userInfo.username;
            paramsData.orderInName = userName
            console.log("数据源1111111111",paramsData)
            this.$refs.modalForm.stockIn(paramsData)
            this.$refs.modalForm.title = '入库'
          }
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning('只能勾选一条记录')
        } else {
          this.$message.warning('请选择一条记录')
        }
        this.clearSelected();
      },
      clearSelected(){
        this.selectedRowKeys = [];
      },
      handleCheck() {
        if (this.selectedRowKeys.length == 1) {
          let record = this.selectionRows[0]
          if (record.state != 'approve') {
            this.$message.warning('只能对待审批的记录进行审批')
          } else {
            this.$refs.modalForm.check(this.selectionRows[0])
          }
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning('只能勾选一条记录')
        } else {
          this.$message.warning('请选择一条记录')
        }
        this.clearSelected();
      },
      handleAddInOrder: function () {
        this.$refs.modalForm.addInOrder();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>