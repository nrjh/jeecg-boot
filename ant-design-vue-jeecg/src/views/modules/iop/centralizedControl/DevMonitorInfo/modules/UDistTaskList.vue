<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="车牌号:" :labelCol="{span:4}" :wrapperCol="{span:15}">
              <a-input placeholder="" v-model="queryParam.autoInfo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="接受单位:" :labelCol="{span:4}" :wrapperCol="{span:18}">
<!--              <j-dict-select-tag-ora type="list" :trigger="true" v-model="queryParam.lineNo" v-decorator="[ 'lineNo', validatorRules.lineNo]"  dictCode="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'"-->
<!--                                     placeholder="请选择检定线" />-->
              <a-input placeholder="暂时不知道数据，先空着" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="6" :xl="6" :lg="7" :md="8" :sm="24"  :offset="6">
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

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :pagination="false">

      </a-table>
    </div>
  </a-card>
</template>

<script>

    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
    export default {
        name: "uDistTaskList",
        mixins:[JeecgListMixin],
        components: {
            JDictSelectTagOra,
        },
        data () {
            return {
                description: 'U_DIST_TASK管理页面',
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
                        title:'车牌号',
                        align:"center",
                        dataIndex: 'autoInfo'
                    },
                    {
                        title:'接收单位',
                        align:"center",
                        dataIndex: 'orgNo'
                    },
                    {
                        title:'设备数量',
                        align:"center",
                        dataIndex: 'distTaskNum'
                    },
                    {
                        title:'出发时间',
                        align:"center",
                        dataIndex: 'madeTime'
                    },
                    {
                        title:'完成时间',
                        align:"center",
                        dataIndex: 'signDate'
                    },
                ],
                url: {
                    list: "/ptm/uDistTask/list"
                }
            }
        },
        methods: {
            initDictConfig(){
            }
        },
        created:function () {
        }
    }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>