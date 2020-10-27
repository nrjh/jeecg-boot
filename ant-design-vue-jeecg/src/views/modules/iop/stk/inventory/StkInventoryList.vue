<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务编号">
              <a-input placeholder="" v-model="queryParam.taskNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务名称">
              <a-input placeholder="" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <JDictSelectTag v-model="queryParam.status" placeholder="请选择状态" dictCode="IOP_PUB_ACTION"/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">创建任务</a-button>
      <a-button @click="handleCheck(1)" type="primary" icon="plus">查看</a-button>
      <a-button @click="handleTheNext(1)" type="primary" icon="plus">下发</a-button>
      <a-button @click="handleCheck(2)" type="primary" icon="plus">盘点</a-button>
      <a-button @click="handleTheNext(3)" type="primary" icon="plus">作废</a-button>
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

    <stkInventory-modal ref="modalForm" @ok="modalFormOk"></stkInventory-modal>
    <checkModal ref="showCheckForm"></checkModal>
    <lookModal ref="showLookForm"></lookModal>
    <a-modal
      title=""
      :visible="visibleModel"
      :confirm-loading="confirmLoading"
      @ok="handleOkModel"
      @cancel="handleCancelModel"
    >
      <p>您确定将{{nameDate}}任务下发给{{execByDate}}人？</p>
    </a-modal>

    <a-modal
      title=""
      :visible="cancelModel"
      :confirm-loading="confirmLoading"
      @ok="handleOkModel"
      @cancel="handleCancelModel2"
    >
      <p>您确定将{{nameDate}}任务作废吗？</p>
    </a-modal>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import StkInventoryModal from './modules/StkInventoryModal'
  import {httpAction, getAction} from '@/api/manage'
  import AModal from "ant-design-vue/es/modal/Modal";
  import checkModal from './modules/checkModal'
  import lookModal from './modules/lookModal.vue'

  export default {
    name: "StkInventoryList",
    mixins: [JeecgListMixin],
    components: {
      AModal, StkInventoryModal, checkModal, lookModal
    },
    data() {
      return {
        description: 'stk_inventory管理页面',
        visibleModel: false,
        cancelModel: false,
        visibleModelCheck: false,
        confirmLoading: false,
        nameDate: '',
        execByDate: '',
        valueModel: '',
        dataSourceCheck: [],
        taskNoCheck: '',
        nameCheck: '',
        execByCheck: '',
        stateData: '',        //判断是否作废状态
        model: {},
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'id',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '任务编号',
            dataIndex: 'taskNo',
            align: "center"
          },
          {
            title: '任务名称',
            align: "center",
            dataIndex: 'name'
          },
          {
            title: '盘点库房',
            align: "center",
            dataIndex: 'sbWarehouse',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '盘点人员',
            align: "center",
            dataIndex: 'execBy',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '盘点范围',
            align: "center",
            dataIndex: 'locationName',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '开始时间',
            align: "center",
            dataIndex: 'planStartTime'
          },
          {
            title: '结束时间	',
            align: "center",
            dataIndex: 'planEndTime',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '任务状态',
            align: "center",
            dataIndex: 'state',
            customRender: function (value) {
              if (value == 'draft') {
                return '创建';
              } else if (value == 'cancel') {
                return '已作废';
              } else if (value == 'confirm') {
                return '进行中';
              } else if (value == 'done') {
                return '完成';
              }
            }
          },
          {
            title: '下发状态',
            align: "center",
            dataIndex: 'issueState',
            customRender: function (text) {
              if (text == 'confirm') {
                return "已下发";
              } else {
                return "未下发";
              }
            }
          },
          {
            title: '盘点结果',
            align: "center",
            dataIndex: 'inventoryResult',
            customRender: function (text) {
              return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
            }
          }
        ],
        url: {
          list: "/StkInventory/stkInventory/list",
          delete: "/StkInventory/stkInventory/delete",
          deleteBatch: "/StkInventory/stkInventory/deleteBatch",
          exportXlsUrl: "/StkInventory/stkInventory/exportXls",
          importExcelUrl: "StkInventory/stkInventory/importExcel",
          sendInventory: "StkInventory/stkInventory/send",
          cancelInventory: "StkInventory/stkInventory/cancel",
          getInventoryProd: "StkInventory/stkInventory/getInventoryProd",
          queryById: "StkInventory/stkInventory/queryById",
          inventoryResult: "StkInventory/stkInventory/inventoryResult",
        },
        dictOptions: {},
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig() {
      },
      handleOkModel() {
        this.confirmLoading = true;
        console.log('------操作------' + this.valueModel);
        if (this.valueModel == 1) {
          console.log('------操作------' + this.valueModel);
          getAction(this.url.sendInventory, {id: this.selectedRowKeys[0]}).then((res) => {
            if (res.success) {
              this.visibleModel = false;
              this.confirmLoading = false;
              this.loadData()
            } else {
              console.log(res.message);
            }
          })
        } else if (this.valueModel == 3) {
          console.log('------操作------' + this.valueModel);
          getAction(this.url.cancelInventory, {id: this.selectedRowKeys[0]}).then((res) => {
            if (res.success) {
              this.cancelModel = false;
              this.confirmLoading = false;
            } else {
              console.log(res.message);
            }
          })
        }

        this.searchQuery();
        this.onClearSelected();
      },
      handleCancelModel() {
        this.visibleModel = false;
      },
      handleCancelModel2() {
        this.cancelModel = false;
      },
      handleTheNext(v) {
        console.log(v);
        if (this.selectedRowKeys.length == 1) {

          getAction(this.url.queryById, {id: this.selectedRowKeys[0]}).then((res) => {
            if (res.success) {
              this.nameDate = res.result.name
              this.execByDate = res.result.execBy
              this.stateData = res.result.state
              var b = 1
              var c = 3
              if (v == b) {
                if (this.stateData == "cancel") {
                  this.$message.warning("此盘点任务单已作废")
                } else {
                  this.visibleModel = true;
                }
              }
              if (v == c) {
                this.cancelModel = true;
              }
              this.valueModel = v;
            }
          })
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning("只能勾选一条记录")
        } else {
          this.$message.warning("请选择一条记录")
        }

      },
      handleCheck(v) {
        if (this.selectedRowKeys.length == 1) {
          if (v == 1) {
            this.$refs.showLookForm.showCheck(this.selectedRowKeys[0], v);
          } else {
            this.$refs.showCheckForm.showCheck(this.selectedRowKeys[0], v);
          }
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning("只能勾选一条记录")
        } else {
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