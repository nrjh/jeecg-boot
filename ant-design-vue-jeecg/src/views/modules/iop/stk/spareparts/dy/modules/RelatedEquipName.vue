<template>
  <div>
    <a-modal
      :title="title"
      :width="width"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="12" :lg="7" :md="4" :sm="16">
              <a-form-item label="设备类型">
                <a-input placeholder="设备类型" v-model="equipType"></a-input>
              </a-form-item>
            </a-col> <a-col :xl="12" :lg="7" :md="4" :sm="16">
                <a-button @click="queryEquipType" type="primary" icon="plus">查询</a-button>
            </a-col>

          </a-row>
        </a-form>
      </div>
    <a-card :bordered="false">
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
  import { initDictOptionsOra } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'RelatedEquipName',
    mixins: [JeecgListMixin],
    components: {
    },
    data() {
      return {
        description: 'FaultName',
        visible: false,
        equipType:'',
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
          // {
          //   title: '检定线',
          //   align: 'center',
          //   dataIndex: 'areaId_dictText'
          // },
          {
            title: '设备类型',
            align: 'center',
            dataIndex: 'value'
          },
          {
            title: '设备类型编号',
            align: 'center',
            dataIndex: 'text'
          },
        ],
        url: {
          list: '/OEquip/oEquip/list',
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
      show(v){
        console.log("v-------",v),
        this.queryParam.faultType=v;
        this.loadData();
        this.visible=true;
      },
      handleOk () {
        if(this.selectedRowKeys.length==1){
          this.$emit('relatedEquipName',this.selectionRows[0]);
          this.visible=false;
          this.equipType="";
          this.onClearSelected();
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }
      },
      handleCancel(){
        this.visible=false;
        this.equipType="";
        this.onClearSelected();
      },
      loadData(equip) {
        // this.loading = true;
        // getAction(this.url.list, params).then((res) => {
        //   if (res.success) {
        //     this.dataSource = res.result.records;
        //     console.log("传入的值111111",this.dataSource)
        //     this.ipagination.total = res.result.total;
        //   }
        //   this.loading = false;
        // })
        let str='';
        // let str="P_COMM_CODE,param_id,param_name,area_id='"+value+"'";
        console.log("equip@@@",equip)
        if(equip!=null &&  equip ){
           str="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_TYPE' AND STATUS = '1' and param_name='"+equip+"'";
        }else {
           str="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_TYPE' AND STATUS = '1'";
        }
        initDictOptionsOra(str).then(res => {
          console.log("res.result----",res.result)
          this.dataSource=res.result;
        }
        )
      },

      queryEquipType(){
        let equip=this.equipType;
        console.log("equip----------------",equip)
        this.loadData(equip)
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