<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备别名">
              <a-input placeholder="请输入设备别名" v-model="queryParam.equipLabe"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!--              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleAddEdit" type="primary" icon="plus">编辑</a-button>
      <a-button @click="handleDelete" type="primary" icon="delete">删除</a-button>
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

      </a-table>
    </div>

    <devMonitorInfo-modal ref="modalForm" @ok="modalFormOk" @searchQuery="searchQuery" @onClearSelected="onClearSelected"></devMonitorInfo-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevMonitorInfoModal from './modules/DevMonitorInfoModal'
  import store from '@/store/'
  import { getAction ,deleteAction,postAction} from '@/api/manage'

  export default {
    name: "DevMonitorInfoList",
    mixins:[JeecgListMixin],
    components: {
      DevMonitorInfoModal
    },
    data () {
      return {
        description: '监控设备管理管理页面',

        stopTimer:false,
        websock: null,
        lockReconnect:false,
        heartCheck:null,
        formData:{},
        openPath:'',
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
            title:'线体编号',
            align:"center",
            dataIndex: 'lineNo_dictText'
          },
          {
            title:'设备类型',
            align:"center",
            dataIndex: 'equipType_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName_dictText'
          },
          {
            title:'设备别名',
            align:"center",
            dataIndex: 'equipLabe'
          },
          {
            title:'设备IP',
            align:"center",
            dataIndex: 'equipIp'
          },
          {
            title:'用户名',
            align:"center",
            dataIndex: 'loginName'
          },
          {
            title:'密码',
            align:"center",
            dataIndex: 'loginPasswd'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'reason'
          },
        ],
        url: {
          list: "/DevMonitorInfo/devMonitorInfo/list",
          delete: "/DevMonitorInfo/devMonitorInfo/delete",
          deleteBatch: "/DevMonitorInfo/devMonitorInfo/deleteBatch",
          exportXlsUrl: "/DevMonitorInfo/devMonitorInfo/exportXls",
          importExcelUrl: "DevMonitorInfo/devMonitorInfo/importExcel",
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
      loadDataMess () {
        console.log("-------加载集中管控左屏数据----")
      },

      handleAddEdit(){
        if(this.selectedRowKeys.length==1){
          console.log("this.selectionRows---",this.selectionRows[0])
          this.$refs.modalForm.edit(this.selectionRows[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleDelete(){
        if(this.selectedRowKeys.length==1){
          deleteAction(this.url.delete,{id:this.selectedRowKeys[0]}).then((res)=>{
            if(res.success){
              this.searchQuery();
              this.onClearSelected();
            }else{
              console.log(res.message);
            }
          })
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      }
    },
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>