<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="采购单号">
                <a-input placeholder="" v-model="queryParam.orign"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="订单号">
                <a-input placeholder="" v-model="queryParam.pickingNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="合同编号">
                <a-input placeholder="" v-model="queryParam.contract"></a-input>
              </a-form-item>
            </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="单据类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag placeholder="请输入单据类型" :trigger-change="true" v-model="queryParam.orderType" dictCode="order_type"/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="申请日期" >
              <j-date  placeholder="请选择开始时间" v-model="queryParam.startTime"  :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="6" >
            <a-form-item label="至">
              <j-date   placeholder="请选择结束时间" v-model="queryParam.endTime" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
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
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button @click="handleCommple" type="primary" icon="plus">合并生成</a-button>-->
<!--      <a-button @click="handleEditOrder" type="primary" icon="plus">修改</a-button>-->
<!--      <a-button @click="handleDelete" type="primary" icon="plus">删除</a-button>-->
      <a-button @click="handleLook" type="primary" icon="plus">查看</a-button>
      <a-button @click="handleAdd" type="primary" icon="plus">打印</a-button>
      <a-button @click="handleApprove" type="primary" icon="plus">审核</a-button>
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

    <pcsPurchaseOrder-modal ref="modalForm" @ok="modalFormOk"></pcsPurchaseOrder-modal>
    <LookPurchaseOrderModal ref="lookForm" @ok="modalFormOk"></LookPurchaseOrderModal>
    <ApprovePurchaseOrderModal ref="approveFrom" @ok="modalFormOk"></ApprovePurchaseOrderModal>
    <CommpleOrderModal ref="commpleForm" @ok="modalFormOk"></CommpleOrderModal>

    <a-modal
      title=""
      :visible="visibleModel"
      @ok="handleOkModel"
      @cancel="handleCancelModel"
    >
      <p>是否删除此采购单！</p>
    </a-modal>
    <a-modal
      title=""
      :visible="visibleCommple"
      @ok="handleOkCommple"
      @cancel="handleCancelCommple"
    >
      <p>是否合并采购计划！</p>
    </a-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PcsPurchaseOrderModal from './modules/PcsPurchaseOrderModal'
  import LookPurchaseOrderModal from './modules/lookPurchaseOrderModal.vue'
  import ApprovePurchaseOrderModal from './modules/ApprovePurchaseOrderModal.vue'
  import JDate from '@/components/jeecg/JDate'
  import { getAction ,deleteAction,postAction} from '@/api/manage'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import store from '@/store'
  import CommpleOrderModal from "./modules/CommpleOrderModal";
  const temp = {} // 当前重复的值,支持多列
  const mergeCellKey = (text, array, columns) => {
    let i = 0
    if (text !== temp[columns]) {
      temp[columns] = text
      array.forEach((item) => {
        if (item[columns] === temp[columns]) {
          i += 1
        }
      })
    }
    return i
  }

  export default {
    name: "PcsPurchaseOrderList",
    mixins:[JeecgListMixin],
    components: {
      CommpleOrderModal,PcsPurchaseOrderModal,JDate,LookPurchaseOrderModal,ApprovePurchaseOrderModal,JDictSelectTag,initDictOptions,filterDictText,mergeCellKey
    },
    data () {
      return {
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        description: '采购单管理页面',
        visibleModel: false,
        visibleCommple: false,
        rowValues:[],
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              console.log("index", index);
              return parseInt(index)+1;
            }
          },
          {
            title:'订单号码',
            align:"center",
            dataIndex: 'pickingNo',
            customRender: (value, row, index) => {
              // if(value!=null && value.length>0){
              //   const obj = {
              //     children: value,
              //     attrs: {},
              //   };
              //   obj.attrs.rowSpan = mergeCellKey(row.pickingNo, this.dataSource, 'pickingNo')
              //   return obj;
              // }else {
              //   return value
              // }
              return value;
            },
          },
          {
            title:'采购单号',
            align:"center",
            dataIndex: 'orign'
          },
          {
            title:'下单时间',
            align:"center",
            dataIndex: 'dateOrder',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // }
            // customRender: (value, row, index) => {
            //   if(value!=null && value.length>0){
            //     const obj = {
            //       children: value,
            //       attrs: {},
            //     };
            //     obj.attrs.rowSpan = mergeCellKey(row.dateOrder, this.dataSource, 'dateOrder')
            //     return obj;
            //   }else {
            //     return value
            //   }
            // },
          },
          {
            title:'预计到货日期',
            align:"center",
            dataIndex: 'datePlanned',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'供应商',
          //   align:"center",
          //   dataIndex: 'partnerId',
          //   customRender:function (text) {
          //     if(text==1){
          //         return "供应商A";
          //     }else if (text==2){
          //       return "供应商B";
          //     }else {
          //         return "";
          //     }
          //   }
          // },
           {
            title:'合同编号',
            align:"center",
            dataIndex: 'contract'
          },
          {
            title:'单据级别',
            align:"center",
            dataIndex: 'orderType',
            customRender: function(value) {
              if(value=='urgency'){
                return "紧急单"
              }else if(value=='normal'){
                return "正常单"
              }
            }
          },
          {
            title:'采购人',
            align:"center",
            dataIndex: 'createName'
          },
          {
            title:'采购详情',
            align:"center",
            dataIndex: 'orignContent'
          },
          {
            title:'总件数',
            align:"center",
            dataIndex: 'numberTotal'
          },
          {
            title:'总价',
            align:"center",
            dataIndex: 'amountTotal'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'state',
            customRender: function(value) {
              if(value=='draft'){
                return "待采购"
              }else if(value=='to_audit'){
                return "待审核"
              }else if(value=='refuse'){
                return "审核拒绝"
              }else if(value=='done'){
                return "审核通过"
              }
            }
          }
        ],
        url: {
          list: "/order/pcsPurchaseOrder/approveList",
          delete: "/order/pcsPurchaseOrder/delete",
          deleteBatch: "/order/pcsPurchaseOrder/deleteBatch",
          exportXlsUrl: "/order/pcsPurchaseOrder/exportXls",
          importExcelUrl: "order/pcsPurchaseOrder/importExcel",
          commple: "order/pcsPurchaseOrder/commple",
        },
        dictOptions:{},
      }
    },
    beforeCreate() {
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      handleDelete(){
        if(this.selectedRowKeys.length==1){
          let userName = store.getters.userInfo.realname;
          let createBy = this.selectionRows[0].createName;
          console.log("userName",userName)
          console.log("createBy",createBy)
          if(userName!=createBy){
             this.$message.warning("只能删除当前登录人创建的订单！")
          }else if(this.selectionRows[0].pickingNo!=this.selectionRows[0].orign) {
            this.$message.warning("合并的订单不能删除！")
          }else if(this.selectionRows[0].state!='draft'){
            this.$message.warning("只能删除待审核的数据！")
          }else {
            this.visibleModel = true;
          }
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
             console.log(" store.getters.userInfo", store.getters.userInfo)
             console.log(" this.selectedRowKeys[0]", this.selectionRows[0])
          let userName = store.getters.userInfo.username;
          let createBy = this.selectionRows[0].createBy;
          if(userName!=createBy){
            this.$message.warning("只能修改当前登录人创建的订单！")
          }else if(this.selectionRows[0].state!='draft'){
            this.$message.warning("只能修改待审核的数据！")
          }else {
            this.$refs.modalForm.editOrder(this.selectedRowKeys[0]);
            this.$refs.modalForm.title = '修改'
          }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleLook(){
        console.log("this.selectedRowKeys[0]",this.selectedRowKeys[0])
        if(this.selectedRowKeys.length==1){
          this.$refs.lookForm.editOrder(this.selectedRowKeys[0]);
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

            // if(this.selectionRows[0].state=='draft'){
            //   this.$refs.approveFrom.editOrder(this.selectedRowKeys[0]);
            // }else {
            //   this.$message.warning("该订单已经审核,请勿重新审核！")
            // }
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleCommple(){
        const that = this;
        let orderType="";
        let partnerId = ""
        if(this.selectedRowKeys.length==0){
          this.$message.warning("请选择一条记录")
          return;
        }
        const b=  this.selectionRows.some((item, i) =>{
          if(item.pickingNo!=null && item.pickingNo!='') {
            that.$message.warning("已经生成的采购订单不能合并！")
            return true;
          }
          if(orderType==''){
            orderType=item.orderType;
          }else if(orderType != item.orderType)  {
            that.$message.warning("单据级别不相同的采购计划单不能合并！")
            return true;
          }
          if (partnerId == ""){
            partnerId = item.partnerId
          }else if(partnerId != item.partnerId){
            that.$message.warning("供应商不相同的采购计划单不能合并！")
            return true;
          }
        })
        if(!b){
          this.$refs.commpleForm.editOrder(this.selectedRowKeys,this.selectionRows);
        }
      },
      handleOkCommple(){
          let ids=[];
        this.selectedRowKeys.forEach(item =>{
          ids.push(item)
        })
        console.log("ids",ids.join(';'));
        getAction(this.url.commple,{ids:ids.join(';')}).then((res)=>{
          if(res.success){
            this.visibleCommple=false;
            this.modalFormOk();
          }
        });
      },
      handleCancelCommple(){
        this.visibleCommple=false;
      },
      modalFormOk(){
          this.searchQuery();
          this.onClearSelected();
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .ant-alert-info {
  border: 1px solid #068b881f;
  background-color: #068b881f;
  }
</style>