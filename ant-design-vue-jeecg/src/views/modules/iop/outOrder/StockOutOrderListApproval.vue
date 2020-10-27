<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="领料单号：" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-input placeholder="" v-model="queryParam.orderOutId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="领料人：" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-input placeholder="" v-model="queryParam.createOutName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="物料名称：" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <a-input placeholder="" v-model="queryParam.prodName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="状态" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <JDictSelectTag placeholder="状态" v-model="queryParam.state"
                              dictCode="ORDER_STATUS"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="检定线名" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-dict-select-tag-ora placeholder="请选择检定线" v-model="queryParam.lineId"
                                     dictCode="o_area,area_name,area_id"/>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="班组" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-dict-select-tag-ora type="list" v-model="queryParam.team"
                                     :trigger="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"/>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="申请时间" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startDate"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="至" :labelCol="{span:12}" :wrapperCol="{span:12}">
              <j-date   placeholder="请选择结束时间" v-model="queryParam.endDate"   :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleLook" type="primary" icon="plus">查看</a-button>
      <a-button @click="handleApproval" type="primary" icon="plus">审核</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('领料出库单')">导出</a-button>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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

    <StockOutOrderModalApprovalCompletion ref="modalForm" @ok="modalFormOk"></StockOutOrderModalApprovalCompletion>
    <LookStockOutOrderModal ref="LookStockOutOrderModal" @onClear="onClearSelected" ></LookStockOutOrderModal>
  </a-card>
</template>

<script>
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StockOutOrderModalApprovalCompletion from './modules/StockOutOrderModalApprovalCompletion'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDate from '@/components/jeecg/JDate'
  import { getAction ,deleteAction} from '@/api/manage'
  import LookStockOutOrderModal from './modules/LookStockOutOrderModal.vue'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import ARow from "ant-design-vue/es/grid/Row";
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "StockOutOrderList",
    mixins:[JeecgListMixin],
    components: {
      ARow,
      StockOutOrderModalApprovalCompletion,
      JDictSelectTag,
      JDictSelectTagIop,
      JDate,
      LookStockOutOrderModal,
      JDictSelectTagOra
    },
    data () {
      const vm = this
      return {
        description: '领料出库单管理页面',
        visibleModel: false,
        calibrationLine:[],
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:50,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'出库单号',
            align:"center",
            width: 100,
            dataIndex: 'orderOutId'
          },
          {
            title:'申请日期',
            align:"center",
            width: 100,
            dataIndex: 'createTime'
          },
          {
            title:'领料人',
            align:"center",
            width: 70,
            dataIndex: 'createOutName',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'检定线',
            align:"center",
            width: 70,
            dataIndex: 'lineName'
          },
          {
            title:'班组',
            align:"center",
            dataIndex: 'team',
            width: 100,
            customRender:function (text) {
              return vm.getTeamLineValue(text)
            }
          },
          {
            title:'物料类型',
            align:"center",
            width: 70,
            dataIndex: 'prdCategory_dictText'
          },
          {
            title:'申请类型',
            align:"center",
            width: 70,
            dataIndex: 'orderType_dictText'
          },
          {
            title:'领料物料详情',
            align:"center",
            width: 200,
            dataIndex: 'prodDetails'
          },
          {
            title:'预计需求日期',
            align:"center",
            width: 70,
            dataIndex: 'datePlanned'
          },
          {
            title:'状态',
            align:"center",
            width: 70,
            dataIndex: 'state_dictText'
          },
          // {
          //   title:'待审批人',
          //   align:"center",
          //   dataIndex: ''
          // }
        ],
        url: {
          list: "/outOrder/stockOutOrderApproval/list",
          delete: "/outOrder/stockOutOrderApproval/delete",
          deleteBatch: "/outOrder/stockOutOrderApproval/deleteBatch",
          exportXlsUrl: "/outOrder/stockOutOrderApproval/exportXls",
          importExcelUrl: "/outOrder/stockOutOrderApproval/importExcel",
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
        let teamId="O_SHIFT,SHIFT_NAME,SHIFT_ID";
        initDictOptionsOra(teamId).then(res => {
          this.calibrationLine = res.result;
        });
      },
      getTeamLineValue(num){
        for(let i = 0;i<this.calibrationLine.length;i++){
          if(this.calibrationLine[i].value == num){
            return this.calibrationLine[i].title;
            console.log("111111",this.calibrationLine[i].title)
          }
        }
      },
      handleLook(){
        if(this.selectedRowKeys.length==1){
          this.$refs.LookStockOutOrderModal.look(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleApproval(){
        if(this.selectedRowKeys.length==1){
          this.$refs.modalForm.edit(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .ant-alert-info {
    border: 1px solid #068b881f;
    background-color: #068b881f;
  }
  /deep/ td{
    word-break: break-all !important;
    white-space: break-spaces !important;
  }
</style>