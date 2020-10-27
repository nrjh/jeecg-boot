<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="采购申请单号">
              <a-input placeholder="" v-model="queryParam.pickingNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请人">
              <a-input placeholder="" v-model="queryParam.createBy"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="物料名称">
              <a-input placeholder="" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="申请时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date  placeholder="请选择开始时间" v-model="queryParam.startTime"  :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="至" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date   placeholder="请选择结束时间" v-model="queryParam.endTime" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>

            <a-col :span="8">
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleEdit(1)" type="primary" icon="plus">设置供应商</a-button>
      <a-button @click="handleLook" type="primary" icon="plus">查看</a-button>
      <a-button @click="handleEdit(2)" type="primary" icon="plus">修改</a-button>
      <a-button @click="handleDelete" type="primary" icon="plus">删除</a-button>
    </div>

    <addOrderModal ref="addOrderForm" @ok="modalFormOk" @onClear="onClearSelected"></addOrderModal>
    <lookOrderModal ref="lookOrderModal" @onClear="onClearSelected" ></lookOrderModal>

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
        :loading="false"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, onSelected: onSelected}"
        @change="handleTableChange">

      </a-table>

      <a-modal
        title=""
        :visible="visibleModel"
        @ok="handleOkModel"
        @cancel="handleCancelModel"
      >
        <p>是否删除此采购计划单！</p>
      </a-modal>
    </div>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
  import { getAction ,deleteAction} from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import addOrderModal from './modules/addOrderModal.vue'
  import lookOrderModal from './modules/lookOrderModal.vue'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'orderPlanList',
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      JDictSelectTagIop,
      JDate,
      addOrderModal,
      lookOrderModal
    },
    data() {
      const vm = this;
      return {
        description: '采购计划',
        visibleModel: false,
        groupNoList:[],
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
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '采购单号',
            align: 'center',
            dataIndex: 'pickingNo'
          },
          {
            title: '申请日期',
            align: 'center',
            dataIndex: 'createTime'
          },
          {
            title: '申请人',
            align: 'center',
            dataIndex: 'createBy'
          },
          {
            title: '检定线',
            align: 'center',
            dataIndex: 'lineName'
          },
          {
            title: '班组',
            align: 'center',
            dataIndex: 'groupNo',
            customRender:function (text) {
              return vm.getGroupNoValue(text)
            }
          },
          {
            title: '单据类型',
            align: 'center',
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
            title: '采购物料详情',
            align: 'center',
            dataIndex: 'prodDetails'
          },
          {
            title: '预计需求日期',
            align: 'center',
            dataIndex: 'scheduledDate'
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'state',
            customRender: function(value) {
              if(value=='draft'){
                return "新建"
              }else if(value=='to_audit'){
                return "待审核"
              }else if(value=='to_purchas'){
                return "待采购"
              }else if(value=='to_stock'){
                return "待入库"
              }else if(value=='done'){
                return "完成"
              }
            }
          },
          // {
          //   title: '待审批人',
          //   align: 'center',
          //   dataIndex: ''
          // }
        ],
        url: {
          list: '/iop/order/orderPlan/list',
          delete: '/iop/order/orderPlan/delete',
          deleteBatch: '/iop/order/orderPlan/deleteBatch',
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
      initDictConfig() {
        let lineNo="O_SHIFT,SHIFT_NAME,SHIFT_ID";
        initDictOptionsOra(lineNo).then(res => {
          this.groupNoList = res.result;
          console.log("班组信息1111", this.groupNoList)
        });
      },
      getGroupNoValue(value){
        for(let i = 0;i<this.groupNoList.length;i++){
              if(this.groupNoList[i].value == value){
                return this.groupNoList[i].title;
              }
            }
      },
      updateState(recoredId,newState) {
        let params = {id:recoredId, state:newState};
        getAction(this.url.updateStateUrl, params).then((res) => {
          if (res.success) {
            this.loadData();
            this.$message.info(res.message);
          }
        })
      },
      handleAdd(){
        this.$refs.addOrderForm.add();
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
      handleLook(){
        if(this.selectedRowKeys.length==1){
          this.$refs.lookOrderModal.look(this.selectedRowKeys[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleEdit(v){
        if(this.selectedRowKeys.length==1){
          this.$refs.addOrderForm.edit(this.selectedRowKeys[0],v);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleOkModel(){
        this.confirmLoading = true;
        console.log(this.selectedRowKeys[0])
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
      onSelected(row){
        console.log("onSelected",row)
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
  /deep/ td{
    word-break: break-all !important;
    white-space: break-spaces !important;
  }
</style>