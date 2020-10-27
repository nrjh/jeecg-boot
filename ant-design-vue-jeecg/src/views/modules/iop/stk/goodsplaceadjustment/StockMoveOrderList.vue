<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="货位调整单号">
              <a-input placeholder="请输入货位调整单号" v-model="queryParam.orderMoveId"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="调整人">
              <a-input placeholder="请输入调整人" v-model="queryParam.createOutName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料编号">
              <a-input placeholder="请输入物料编号" v-model="queryParam.productId"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="调整时间">
              <j-date placeholder="请选择调整开始时间" class="query-group-cust" v-model="queryParam.startAdjustTime"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date placeholder="请选择调整结束时间" class="query-group-cust" v-model="queryParam.endAdjustTime"></j-date>
            </a-form-item>
          </a-col>

          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <a-input placeholder="请输入状态" v-model="queryParam.state"></a-input>
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleRead" type="primary" icon="zoom-in">查看</a-button>
      <a-button @click="handleModify" type="primary" icon="edit">编辑</a-button>
      <a-button @click="batchDel" type="primary" icon="delete">删除</a-button>
      <a-button @click="handleRead" type="primary" icon="highlight">冲红</a-button>
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
          <a @click="handleSee(record)">查看</a>
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">修改</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.orderMoveId)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <stockMoveOrder-modal ref="modalForm" @ok="modalFormOk"></stockMoveOrder-modal>
    <SeeStockMoveOrderModal ref="seeModalForm"></SeeStockMoveOrderModal>
  </a-card>
</template>

<script>
  import JDate from '@/components/jeecg/JDate.vue'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StockMoveOrderModal from './modules/StockMoveOrderModal'
  import SeeStockMoveOrderModal from './modules/SeeStockMoveOrderModal'
  import { deleteAction } from '@/api/manage'
  export default {
    name: "StockMoveOrderList",
    mixins:[JeecgListMixin],
    components: {
      StockMoveOrderModal,
      SeeStockMoveOrderModal,
      JDate
    },
    data () {
      return {
        description: '货位调整页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'货位调整单号',
            align:"center",
            dataIndex: 'orderMoveId'
          },
          {
            title:'调整人员',
            align:"center",
            dataIndex: 'createOutName'
          },
          {
            title:'调整日期',
            align:"center",
            dataIndex: 'createOutTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'note'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'state',
            customRender:function (value) {
            if(value === 'normal'){
                return '正常';
            } else if(value === 'freeze') {
                return '冻结'
            }
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
          list: "/stk/goodsplaceadjustment/list",
          delete:"/stk/goodsplaceadjustment/delete",
          deleteBatch:"/stk/goodsplaceadjustment/deleteBatch",
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
      // 批量删除
      batchDel: function () {
          if(!this.url.deleteBatch){
              this.$message.error("请设置url.deleteBatch属性!")
              return
          }
          if (this.selectedRowKeys.length <= 0) {
              this.$message.warning('请选择一条记录！');
              return;
          } else {
              var ids = "";
              for (var a = 0; a < this.selectionRows.length; a++) {
                  ids += this.selectionRows[a].orderMoveId + ",";
              }
              var that = this;
              this.$confirm({
                  title: "确认删除",
                  content: "是否删除选中数据?",
                  onOk: function () {
                      that.loading = true;
                      deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                          if (res.success) {
                              that.$message.success(res.message);
                              that.loadData();
                              that.onClearSelected();
                          } else {
                              that.$message.warning(res.message);
                          }
                      }).finally(() => {
                          that.loading = false;
                      });
                  }
              });
          }
      },
      // 删除操作
      handleDelete: function (id) {
          if(!this.url.delete){
              this.$message.error("请设置url.delete属性!")
              return
          }
          var that = this;
          deleteAction(that.url.delete, {orderMoveId: id}).then((res) => {
              if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
              } else {
                  that.$message.warning(res.message);
              }
          });
      },
      // 查看出货调整详情
      handleSee(records){
          this.$refs.seeModalForm.see(records.orderMoveId);
      },
      handleRead(){
          if (this.selectedRowKeys.length == 1) {
              let one = this.selectionRows[0]
              this.$refs.seeModalForm.see(one.orderMoveId);
          } else if (this.selectedRowKeys.length > 1) {
              this.$message.warning('只能勾选一条记录')
          } else {
              this.$message.warning('请选择一条记录')
          }
      },
      // 编辑操作
      handleModify(){
          if (this.selectedRowKeys.length == 1) {
              let one = this.selectionRows[0]
              this.$refs.modalForm.edit(one);
              this.$refs.modalForm.title = "编辑";
              this.$refs.modalForm.disableSubmit = false;
          } else if (this.selectedRowKeys.length > 1) {
              this.$message.warning('只能勾选一条记录')
          } else {
              this.$message.warning('请选择一条记录')
          }
      },
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>