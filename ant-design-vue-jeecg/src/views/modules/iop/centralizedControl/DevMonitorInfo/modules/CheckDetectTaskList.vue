<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
            <a-col :span="6">
              <a-form-item label="任务号:" :labelCol="{span:4}" :wrapperCol="{span:15}">
                <a-input placeholder="" v-model="queryParam.detectTaskNo"></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="6">
              <a-form-item label="线体名称:" :labelCol="{span:4}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" :trigger="true" v-model="queryParam.lineNo" dictCode="o_area,area_name,area_id"
                                       placeholder="请选择检定线" />
              </a-form-item>
            </a-col>

            <a-col :span="12" :xl="6" :lg="7" :md="8" :sm="24"  :offset="6">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
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
        :loading="loading"
        :pagination="false"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="tableScroll">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">说明</a>
        </span>
      </a-table>
    </div>

    <checkTaskExplain ref="explain"></checkTaskExplain>
  </a-card>


</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import checkTaskExplain from './CheckDetectTaskExplain'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "CheckDetectTaskList",
    mixins:[JeecgListMixin],
    components: {
        checkTaskExplain,
        JDictSelectTag,
        JDictSelectTagOra
    },
    data () {
      let vm = this
      return {
        description: 'T_DETECT_TASK管理页面',
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
            title:'任务号',
            align:"center",
            dataIndex: 'detectTaskNo'
          },
          {
              title:'线体名称',
              align:"center",
              dataIndex: 'lineNo',
              customRender:function (text) {
                  return vm.lineName(text)
              }
          },
          {
              title:'任务类别',
              align:"center",
              dataIndex: 'taskType'
          },
          {
              title:'启动时间',
              align:"center",
              dataIndex: 'taskBgnDate'
          },
          {
              title:'完成时间',
              align:"center",
              dataIndex: 'taskEndDate'
          },
          {
              title:'审核结果',
              align:"center",
              dataIndex: 'auditRemark'
          },
          {
              title:'已检定量-线体/MDS/仓储',
              align:"center",
              dataIndex: 'hadCheckNum'
          },
          {
              title:'合格量-线体/MDS/仓储',
              align:"center",
              dataIndex: 'detectConcP'
          },
          {
              title:'不合格量-线体/MDS/仓储',
              align:"center",
              dataIndex: 'detectConcU'
          },
          {
              title: '操作',
              align:"center",
              dataIndex: 'action',
              scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/ptm/mtDetectTaskInfo/exceptionList",
        },
        dictOptions:{},
        tableScroll:{x :47*30+50},
        lineNameData:[]
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
      handleEdit(record){
          this.$refs.explain.initialData(record);
      },
      lineName(text){
          for(let i = 0 ;i < this.lineNameData.length ;i++){
              if(this.lineNameData[i].value === text){
                  return this.lineNameData[i].title;
              }
          }
      },
    },
    created:function() {
        let lineNo="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'";
        initDictOptionsOra(lineNo).then(res => {
            this.lineNameData = res.result
        });
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>