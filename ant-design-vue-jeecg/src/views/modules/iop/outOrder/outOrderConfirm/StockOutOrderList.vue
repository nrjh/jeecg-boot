<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="领料出库单号">
              <a-input placeholder="请输入领料出库单号" v-model="queryParam.orderOutId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="领料人">
              <a-input placeholder="请输入领料人" v-model="queryParam.createOutName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="物料编号">-->
<!--              <a-input placeholder="请输入物料编号" v-model="queryParam.defaultCode"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检定线">
              <a-input placeholder="请选择检定线" v-model="queryParam.line"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="班组">
              <a-input placeholder="请输入班组" v-model="queryParam.team"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出库人">
              <a-input placeholder="请输入出库人" v-model="queryParam.ouBy"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出库状态">
              <a-input placeholder="请输入出库状态" v-model="queryParam.outState"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                 {{ toggleSearchStatus ? '收起' : '展开' }}
                 <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
          <templete v-if="toggleSearchStatus">
            <a-col :xl="9" :lg="9" :md="10" :sm="24">
              <a-form-item label="申请日期">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
              </a-form-item>
            </a-col>
            <a-col :xl="9" :lg="9" :md="10" :sm="24">
              <a-form-item label="出库时间">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.outTime_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.outTime_end"></j-date>
              </a-form-item>
            </a-col>
          </templete>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleOut" type="primary">出库</a-button>
      <a-button @click="handleBack" type="primary">退回</a-button>
      <a-button @click="handleRead" type="primary">查看</a-button>
      <a-button v-print="'#printContent'" type="primary">打印</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <section ref="print" id="printContent">
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
            <img v-else :src="getImgView(text)" height="25px" alt="图片不存在"
                 style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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
      </section>
      <a-modal
        title=""
        :visible="visibleModel"
        @ok="handleOkModel"
        @cancel="handleCancelModel"
      >
        <p>是否退回此领料出库单！</p>
      </a-modal>
    </div>

    <stockOutOrder-modal ref="modalForm" @ok="modalFormOk"></stockOutOrder-modal>
    <!--    <OutStockOutOrderModal  ref="modalForm" @onClear="onClearSelected" ></OutStockOutOrderModal>-->
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StockOutOrderModal from './modules/StockOutOrderModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JDate from '@/components/jeecg/JDate.vue'
  import { deleteAction } from '@/api/manage'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'StockOutOrderList',
    mixins: [JeecgListMixin],
    components: {
      JDictSelectTag,
      JDictSelectTagIop,
      JDate,
      StockOutOrderModal
    },
    data() {
      const vm = this
      return {
        description: 'stock_out_order管理页面',
        calibrationLine:[],
        visibleModel: false,
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
            title: '领料出库单号',
            align: 'center',
            dataIndex: 'orderOutId'
          },
          {
            title: '领料人',
            align: 'center',
            dataIndex: 'createOutName'
          },
          {
            title: '检定线',
            align: 'center',
            dataIndex: 'lineName'
          },
          {
            title: '班组',
            align: 'center',
            dataIndex: 'team',
            customRender:function (text) {
              return vm.getTeamLineValue(text)
            }
          },
          {
            title: '申请日期',
            align: 'center',
            dataIndex: 'createTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '物料类型',
            align: 'center',
            dataIndex: 'prdCategory',
            customRender: function(text) {
              if(text=='spare'){
                return  '备品备件';
              }else if(text=='office_supplies'){
                return '办公用品'
              }else if(text=='equip'){
                return '生产设施'
              }
            }
          },
          {
            title: '申请类型',
            align: 'center',
            dataIndex: 'orderType',
            customRender: function(text) {
              if(text=='normal'){
                return  '正常单';
              }else {
                return '紧急单'
              }
            }
          },
          {
            title: '领料物料详情',
            align: 'center',
            dataIndex: 'prodDetails',
            // customRender:function (text) {
            //     var result = "";
            //     for (let i = 0; i < text.length; i++) {
            //         var skuName = text[i].skuName;
            //         var skuNum = text[i].skuNum;
            //         result += skuName+",数量:"+skuNum;
            //     }
            //     return result+"";
            // }
          },
          {
            title: '出库人',
            align: 'center',
            dataIndex: 'ouName'
          },
          {
            title: '出库时间',
            align: 'center',
            dataIndex: 'outTime',
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'state_dictText',
            // customRender: function(text) {
            //   return text =="freeze" ? '未出库' : "已出库出库"
            // }
          }
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align: 'center',
          //   scopedSlots: { customRender: 'action' }
          // }
        ],
        url: {
          list: '/outOrder/stockOutOrder/confirm/list',
          delete: '/outOrder/stockOutOrder/delete',
          deleteBatch: '/outOrder/stockOutOrder/deleteBatch',
          exportXlsUrl: '/outOrder/stockOutOrder/exportXls',
          importExcelUrl: 'outOrder/stockOutOrder/importExcel',
          back: '/outOrder/stockOutOrder/confirm/back'
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
      handleOut() {
        if (this.selectedRowKeys.length == 1) {
          if (this.selectionRows[0].outState != 'freeze') {
            this.$message.warning('只能对未出库的记录进行入库操作')
          } else {
            this.$refs.modalForm.edit(this.selectionRows[0])
            console.log("查看数据1111",this.selectionRows[0])
            this.$refs.modalForm.title = '出库确认'
            this.$refs.modalForm.disableSubmit = false
          }
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning('只能勾选一条记录')
        } else {
          this.$message.warning('请选择一条记录')
        }
      },
      handleBack() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length == 0) {
          this.$message.warning('请至少选择一行！！！')
        } else {
          for(let item of this.selectionRows){
            if(item.state == 'done'){
              this.$message.warning('出库已完成的出库单不能退回，请重新选择！！！')
              return
            }
          }
          this.visibleModel = true;
        }
      },
      handleOkModel(){
        deleteAction(this.url.back, {
          ids: this._.join(this.selectedRowKeys, ',')
        }).then(res => {
          if (res.success) {
            this.$message.success(res.message)
            this.visibleModel = false;
            this.searchQuery()
          } else {
            this.$message.warning(res.message)
          }
        })
      },
      handleRead() {
        if (this.selectedRowKeys.length == 1) {
          this.$refs.modalForm.read(this.selectionRows[0])
          this.$refs.modalForm.title = '查看'
          this.$refs.modalForm.disableSubmit = false
        } else if (this.selectedRowKeys.length > 1) {
          this.$message.warning('只能勾选一条记录')
        } else {
          this.$message.warning('请选择一条记录')
        }
      },

      handleCancelModel(){
        this.visibleModel = false;
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  /deep/ td{
    word-break: break-all !important;
    white-space: break-spaces !important;
  }
</style>