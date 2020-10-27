<template>
  <div>
    <a-modal
      :title="title"
      :width="width"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭">
    <a-card :bordered="false">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-input v-model="queryParam.faultType" hidden="hidden"></a-input>
      </a-form>
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

        </a-table>
      </div>
    </a-card>
    </a-modal>
  </div>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction} from '@/api/manage'

  export default {
    name: 'RelatedPrd',
    mixins: [JeecgListMixin],
    components: {
    },
    data() {
      return {
        description: 'FaultName',
        visible: false,
        title: "关联设备",
        width: 800,
        queryParam: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // 表头
        columns: [
          {
            title: '检定线',
            align: 'center',
            dataIndex: 'lineName'
          },
          {
            title: '班组',
            align: 'center',
            dataIndex: 'team_dictText'
          },
          {
                title: '领料单号',
                align: 'center',
                dataIndex: 'orderOutId'
          },
          {
              title: '申请时间',
              align: 'center',
              dataIndex: 'createTime'
          }
        ],
        url: {
          list: '/outOrder/stockOutOrder/list',
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
      },
      show(){
        this.loadData();
        this.visible=true;
      },
      handleOk () {
        if(this.selectedRowKeys.length==1){
          this.$emit('relatedPrd',this.selectionRows[0]);
          this.visible=false;
          this.onClearSelected();
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }
      },
      handleCancel(){
        this.visible=false;
        this.onClearSelected();
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, {state:'done'}).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            console.log("传入的值111111",this.dataSource)
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },
      getQueryParams() {
        //获取查询条件
        let sqp = {}
        var param = Object.assign(sqp, this.queryParam);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return this.filterObj(param);
      },
      filterObj(obj) {
        if (!(typeof obj == 'object')) {
          return;
        }
        for ( var key in obj) {
          if (obj.hasOwnProperty(key)
            && (obj[key] == null || obj[key] == undefined || obj[key] === '')) {
            delete obj[key];
          }
        }
        return obj;
      }
    }
  }

</script>
<style scoped>
  @import '~@assets/less/common.less';
  @import '~@assets/less/index.less';
  .ant-alert-info {
    border: 1px solid #068b881f;
    background-color: #068b881f;
  }



</style>