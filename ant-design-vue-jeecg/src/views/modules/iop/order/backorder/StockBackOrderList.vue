<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row  :gutter="24">
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="退回入库单号">
              <a-input placeholder="" v-model="queryParam.orderBackId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="退回人">
              <JDictSelectTag v-model="queryParam.createOutName"

                              placeholder=""
                              dictCode="sys_user,realname,realname" />
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称">
              <a-input placeholder="" v-model="queryParam.orign"></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="班组">-->
<!--              <JDictSelectTag v-model="queryParam.createOutName"-->

<!--                              placeholder=""-->
<!--                              dictCode="sys_user,realname,realname" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
        </a-row>

        <a-row  :gutter="24">
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料编号">
              <a-input placeholder="" v-model="queryParam.orign"></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="检定线">-->
<!--              &lt;!&ndash;<a-input placeholder="" v-model="queryParam.orderBackId"></a-input>&ndash;&gt;-->
<!--              <j-dict-select-tag type="list"-->
<!--                                 placeholder=""-->
<!--                                 v-model="queryParam.type"-->
<!--                                 dict-code="IOP_PUB_CATEGORY"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="入库类型">
              <JDictSelectTag v-model="queryParam.orderType"
                              placeholder="请选择退还类型" dictCode="order_type" ></JDictSelectTag>
            </a-form-item>
          </a-col>

          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <JDictSelectTag v-model="queryParam.state"
                              placeholder="" dictCode="IOP_STK_MOVE_STATE" ></JDictSelectTag>
            </a-form-item>
          </a-col>
        </a-row>


        <a-row :gutter="24">
          <template v-if="toggleSearchStatus">
            <a-col :span="8">
              <a-form-item label="申请日期" >
                <j-date  placeholder="请选择开始时间" v-model="queryParam.startTime"  :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="8" >
              <a-form-item label="至">
                <j-date   placeholder="请选择结束时间"  v-model="queryParam.endTime"  :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </template>
          <!--<a-col :span="6">-->
            <!--<a-form-item label="入库时间" >-->
              <!--<j-date  placeholder="请选择开始时间" v-model="queryParam.startTime"  :trigger-change="true" style="width: 100%"/>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="6" >-->
            <!--<a-form-item label="至">-->
              <!--<j-date   placeholder="请选择结束时间"  v-model="queryParam.endTime"  :trigger-change="true" style="width: 100%"/>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
        </a-row>
        <a-row>
          <a-col>
            <a-form-item >
              <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
               <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                  <a @click="handleToggleSearch" style="margin-left: 8px">
                 {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                  </a>
             </span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAddBackOrder" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleEditOrder" type="primary" icon="plus">修改</a-button>
      <a-button @click="handleLook" type="primary" icon="plus">查看</a-button>
      <a-button @click="handleDelete" type="primary" icon="plus">删除</a-button>
<!--      <a-button @click="handleAdd" type="primary" icon="plus">审批</a-button>-->
<!--      <a-button @click="handleAdd" type="primary" icon="plus">打印</a-button>-->
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

      </a-table>
    </div>

    <stockBackOrder-modal ref="modalForm" @ok="modalFormOk"></stockBackOrder-modal>
    <LookStockBackOrderModal ref="lookForm" @ok="modalFormOk"></LookStockBackOrderModal>

    <a-modal
      title=""
      :visible="visibleModel"
      @ok="handleOkModel"
      @cancel="handleCancelModel"
    >
      <p>是否删除此单！</p>
    </a-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StockBackOrderModal from './modules/StockBackOrderModal'
  import LookStockBackOrderModal from './modules/LookStockBackOrderModal.vue'
  import { getAction ,deleteAction,postAction} from '@/api/manage'
  import ARow from "ant-design-vue/es/grid/Row"
  import JDate from '@/components/jeecg/JDate'
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "StockBackOrderList",
    mixins:[JeecgListMixin],
    components: {
      AFormItem,
      ARow, StockBackOrderModal,JDate,LookStockBackOrderModal
    },
    data () {
      const vm = this
      return {
        description: '退库单管理页面',
        visibleModel:false,
        calibrationLine:[],
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
            title:'退回入库单号',
            align:"center",
            dataIndex: 'orderBackId'
          },
          {
            title:'入库类型',
            align:"center",
            dataIndex: 'orderType',
            customRender:function (v) {
              if(v=='unqualified'){
                  return "不合格退回"
              }else  if (v=='normal'){
                return "正常退回"
              }else  if (v=='damage'){
                return "损坏退回"
              }
            }
          },
          // {
          //   title:'检定线',
          //   align:"center",
          //   dataIndex: ''
          // },
          // {
          //   title:'申请班组',
          //   align:"center",
          //   dataIndex: 'team',
          //   customRender:function (text) {
          //     return vm.getTeamLineValue(text)
          //   }
          // },
          {
            title:'退回人名称',
            align:"center",
            dataIndex: 'createOutName'
          },
          {
            title:'申请日期',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'退回物料详情',
            align:"center",
            dataIndex: 'prodLines'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'state',
            customRender:function (v) {
              if(v=='draft'){
                return "新建"
              }else  if (v=='done'){
                return "完成"
              }else  if (v=='submit'){
                return "提交"
              }
            }
          }
        ],
        url: {
          list: "/backorder/stockBackOrder/list",
          delete: "/backorder/stockBackOrder/delete",
          deleteBatch: "/backorder/stockBackOrder/deleteBatch",
          exportXlsUrl: "/backorder/stockBackOrder/exportXls",
          importExcelUrl: "backorder/stockBackOrder/importExcel",
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
      handleDelete(){
        if(this.selectedRowKeys.length==1){
          this.visibleModel = true;
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleOkModel(){
        this.confirmLoading = true;
        deleteAction(this.url.delete,{id:this.selectedRowKeys[0]}).then((res)=>{
          if(res.success){
            this.visibleModel = false;
            this.confirmLoading = false;
            this.searchQuery();
            this.onClearSelected();
          }else{
            console.log(res.message);
          }
        })
      },
      handleCancelModel(){
        this.visibleModel = false;
      },
      handleEditOrder(){
        console.log("this.selectedRowKeys[0]",this.selectedRowKeys[0])
        if(this.selectedRowKeys.length==1){
          this.$refs.modalForm.editOrder(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleLook(){
        console.log("this.selectedRowKeys[0]",this.selectedRowKeys[0])
        if(this.selectedRowKeys.length==1){
          this.$refs.lookForm.lookOrder(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleApprove(){
        console.log("this.selectedRowKeys[0]",this.selectedRowKeys[0])
        if(this.selectedRowKeys.length==1){
          this.$refs.approveFrom.editOrder(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleAddBackOrder: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
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