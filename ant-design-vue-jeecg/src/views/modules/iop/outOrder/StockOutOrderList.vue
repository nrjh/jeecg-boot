<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="领料单号：" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="" v-model="queryParam.orderOutId"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="物料名称:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="" v-model="queryParam.prodName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="领料人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="" v-model="queryParam.createOutName"></a-input>
<!--              <j-dict-select-tag placeholder="" v-model="queryParam.prodName"-->
<!--                                 dictCode="sys_user,realname,id"/>-->
            </a-form-item>
          </a-col>


        </a-row>

        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="状态信息" :labelCol="labelCol" :wrapperCol="wrapperCol">

              <JDictSelectTag placeholder="状态" v-model="queryParam.state"
                              dictCode="ORDER_STATUS"/>

            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请时间:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startDate"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <a-form-item label="至"  :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date   placeholder="请选择结束时间" v-model="queryParam.endDate"   :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>


        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="检定线名:"  :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="toggleSearchStatus">
              <j-dict-select-tag-ora placeholder="请选择检定线" v-model="queryParam.lineId"
                                     dictCode="o_area,area_name,area_id"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="班组:" :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="toggleSearchStatus">
              <j-dict-select-tag-ora  v-model="queryParam.team"
                                     :trigger="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"/>
            </a-form-item>
          </a-col>

          <a-col :span="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
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
      <a-button @click="handleAddInOrder" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleEdit(2)" type="primary" icon="plus">修改</a-button>
      <a-button @click="handleLook" type="primary" icon="plus">查看</a-button>
      <a-button @click="handleDelete" type="primary" icon="plus">删除</a-button>
<!--      <a-button @click="handledata" type="primary" icon="plus">返回</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('领料出库单')">导出</a-button>
    </div>
<!--    <div class="table-operator">-->
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->

<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
<!--    </div>-->

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

<!--        这里传入的值分别是：record：当前行的原始数据，index：当前行的索引-->
<!--        <span slot="action" slot-scope="index,record">　-->
<!--          <a @click="handleEdit2(index,record)">审批详情</a>-->
<!--        </span>-->
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

<!--        <span slot="action" slot-scope="text, record">-->
<!--          <a @click="handleEdit(record)">审批详情</a>-->

<!--          <a-divider type="vertical" />-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
<!--            <a-menu slot="overlay">-->
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
<!--        </span>-->
      </a-table>

      <a-modal
        title=""
        :visible="visibleModel"
        @ok="handleOkModel"
        @cancel="handleCancelModel"
      >
        <p>是否删除此领料出库单！</p>
      </a-modal>
    </div>

    <stockOutOrder-modal ref="modalForm" @ok="modalFormOk"></stockOutOrder-modal>
    <LookStockOutOrderModal ref="LookStockOutOrderModal" @onClear="onClearSelected" ></LookStockOutOrderModal>
    <StockOutOrderModalApproval ref="modalApproval" @ok="modalFormOk"></StockOutOrderModalApproval>
  </a-card>
</template>

<script>
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StockOutOrderModal from './modules/StockOutOrderModal'
  import StockOutOrderModalApproval from './modules/StockOutOrderModalApproval'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDate from '@/components/jeecg/JDate'
  import { getAction ,deleteAction} from '@/api/manage'
  import LookStockOutOrderModal from './modules/LookStockOutOrderModal.vue'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'
  export default {
    name: "StockOutOrderList",
    mixins:[JeecgListMixin],
    components: {
      StockOutOrderModal,
      JDictSelectTag,
      JDictSelectTagIop,
      JDate,
      LookStockOutOrderModal,
      StockOutOrderModalApproval,
      JDictSelectTagOra,
    },
    data () {
      const vm = this
      return {
        description: '领料出库单管理页面',
        visibleModel: false,
        calibrationLine:[],
        toggleSearchStatus: false,
        labelCol: {
          span: 12
        },
        wrapperCol: {
          span: 12
        },
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
            title:'出库单号',
            align:"center",
            dataIndex: 'orderOutId'
          },
          {
            title:'申请日期',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'领料人',
            align:"center",
            dataIndex: 'createOutName',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'检定线',
            align:"center",
            dataIndex: 'lineName',
            // customRender:function (text) {
            //   return vm.getcalibrationLineValue(text)
            // }
          },
          {
            title:'班组',
            align:"center",
            dataIndex: 'team',
            customRender:function (text) {
              return vm.getTeamLineValue(text)
            }
          },
          {
            title:'物料类型',
            align:"center",
            dataIndex: 'prdCategory_dictText'
          },
          {
            title:'申请类型',
            align:"center",
            dataIndex: 'orderType_dictText'
          },
          {
            title:'领料物料详情',
            align:"center",
            dataIndex: 'prodDetails'
          },
          {
            title:'预计需求日期',
            align:"center",
            dataIndex: 'datePlanned'
          },

          {
            title:'状态',
            align:"center",
            dataIndex: 'state_dictText'
          },
          // {
          //   title:'审批信息',
          //   align:"center",
          //   dataIndex: 'action',
          //   scopedSlots: { customRender: 'action' },
          // }
        ],
        url: {
          list: "/outOrder/stockOutOrder/list",
          delete: "/outOrder/stockOutOrder/delete",
          deleteBatch: "/outOrder/stockOutOrder/deleteBatch",
          exportXlsUrl: "/outOrder/stockOutOrder/exportXls",
          importExcelUrl: "outOrder/stockOutOrder/importExcel",
          queryStockOutOrderLineByMainId: "/outOrder/stockOutOrder/queryStockOutOrderLineByMainId",
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
      // initDictConfig(){
      //   let lineNo="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'";
      //   initDictOptionsOra(lineNo).then(res => {
      //     this.calibrationLine = res.result;
      //   });
      // },
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
      handleEdit(v){
        if(this.selectedRowKeys.length==1){
          let record = this.selectionRows[0]
          if (record.state == 'draft' || record.state == 'back') {
            this.$refs.modalForm.edit(this.selectedRowKeys[0],v);
            this.$refs.modalForm.title = "修改"
          } else {
            this.$message.warning('提交审批后不能修改')
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleEdit2(index,record){
        console.log(record,2222211111)
        this.$refs.modalApproval.edit(record.id);
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
      handleLook(){
        if(this.selectedRowKeys.length==1){
          this.$refs.LookStockOutOrderModal.look(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleAddInOrder: function () {
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