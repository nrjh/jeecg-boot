<template>
  <a-modal
    title="转派"
    :width="modalWidth"
    :visible="visible"
    :bodyStyle="bodyStyle"
    style="top: 30px;"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <template>
      <div class="test">
        <a-row :gutter="10">
          <a-card :bordered="false" >
            <a-col :md="12" :sm="24">
              <template>
                <a-form >
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-dict-select-tag-ora type="list" :trigger="true"  v-model="queryParam.lineNo"   dictCode="o_area,area_name,area_id"
                                               placeholder="请选择检定线"/>
                      </a-form-item>
                      <a-form-item label="派单对象" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-input  placeholder="请输入派单对象" v-model="queryParamUser.realname" ></a-input>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                          <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                          <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                        </span>
                    </a-col>
                  </a-row>
                </a-form>
              </template>
              <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                :columns="columnsUser"
                :dataSource="dataSourceUser"
                :pagination="ipaginationUser"
                :loading="loading"
                :rowSelection="{fixed:true,type:radio,selectedRowKeys: selectedRowUserKeys, onChange: onSelectChangeUser}"
              >
               <span slot="action" slot-scope="index,record">　
                <a @click="handleEdit2(index,record)">关联</a>
               </span>

              </a-table>
            </a-col>
            <a-col :md="12" :sm="24">
              <div class="table-page-search-wrapper">
                <a-form>
                  <a-row>
                    <a-col :span="12">
                      <a-form-item label="归集方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <a-radio-group v-model="isorter.column" @change="getData">
                          <a-radio value="equipNo">
                            设备
                          </a-radio>
                          <a-radio value="lineNo">
                            检定线
                          </a-radio>
                          <a-radio value="alarmLevel">
                            等级
                          </a-radio>
                          <a-radio value="alarmTime">
                            报警
                          </a-radio>
                        </a-radio-group>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="要求完成时间:" :labelCol="{span:8}" :wrapperCol="{span:16}">
                        <j-date placeholder="要求完成时间" v-model="queryParam.pressTime"
                                :trigger-change="true" style="width: 60%"
                        />
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-form>
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
              >

              </a-table>
            </a-col>
          </a-card>
        </a-row>
      </div>
    </template>

  </a-modal>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import {httpAction, getAction} from '@/api/manage'
  import ACol from "ant-design-vue/es/grid/Col";
  import JDate from '@/components/jeecg/JDate';
  import store from '@/store'
  export default {
    name: "UnfiledCompleteOrdWorkOrderInfoModal",
    mixins: [JeecgListMixin],
    components:{
      ACol,
      JDictSelectTagOra,
      JDate
    },
    data () {
      return {
        visible: false,
        dataSource:[],
        dataSourceUser:[],
        queryParamUser:{},
        queryParam:{},
        checked: [],
        checkedUser: {},
        model: {},
        /* table选中keys*/
        selectedRowUserKeys: [],
        /* table选中records*/
        selectionUserRows: [],
        /* table选中keys*/
        selectedRowKeys: [],
        /* table选中records*/
        selectionRows: [],
        radio:"radio",
        isorter:{
          column: 'createTime',
          order: 'desc',
        },
        ipaginationUser: {
          current: 1,
          pageSize: 15,
          pageSizeOptions: ['15', '30', '45'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
        },
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight-250)+"px"
        },
        modalWidth:400,
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        columnsUser:[
          {
            title: '工号',
            align: "center",
            dataIndex:'workNo'
          },
          {
            title: '名称',
            align: "center",
            dataIndex: 'realname'
          },
          {
            title: '待处理数量',
            align: "center",
            dataIndex: 'size'
          },
          {
            title:'操作',
            align:"center",
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
          }
        ],

        columns:[
          {
            title: '工单号',
            align: "center",
            dataIndex:'id'
          },
          {
            title:'检定线',
            align:"center",
            dataIndex: 'lineNo',
            customRender:function (text) {
              if(text === '01'){
                return "单相电能表"
              }else if(text === '02'){
                return "三相电能表"
              }else if(text === '03'){
                return "互感器"
              }else if(text === "04"){
                return "采集终端"
              }else if (text === "05"){
                return "智能仓储"
              }
            }
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName',
          },          {
            title:'故障等级',
            align:"center",
            dataIndex: 'faultGrade',
            customRender:function (text) {
              if(text === 'hight'){
                return "高"
              }else if(text === 'centre'){
                return "中"
              }else if (text === 'low'){
                return "低"
              }
            }
          },
          {
            title:'报警次数',
            align:"center",
            dataIndex: 'alarmAmount',
          },
          {
            title: '报警内容',
            align: "center",
            dataIndex: 'alarmReason'
          },
          {
            title:'第一次报警时间',
            align:"center",
            dataIndex: 'alarmTime',
          },
          {
            title: '要求完成时间',
            align: "center",
            dataIndex: 'pressTime'
          },
        ],
        validatorRules: {
          orderType: {
            rules: []
          },
        },
        url: {
          queryUserInfo:"/workOrder/ordWorkOrderInfo/queryUserInfo2",
          list: "/workOrder/ordWorkOrderDetail/list",
          delete: "/workOrder/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrder/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrder/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrder/ordWorkOrderInfo/importExcel",
          dispatchingInfo:"/workOrder/ordWorkOrderInfo/transferInfo",
        },
      }
    },

    created () {
      this.modalWidth = window.innerWidth-0;
    },
    methods: {
      show(keys,records){
        let userId = store.getters.userInfo.id
        this.selectedRowUserKeys.push(userId)
        this.selectedRowKeys = keys;
        this.selectionRows = records;
        this.getData ()
        this.visible = true
      },
      getData () {
        var temp = {}
        var parameter = Object.assign(temp, this.queryParamUser,this.isorter);
        parameter.pageNo = this.ipaginationUser.current;
        parameter.pageSize = this.ipaginationUser.pageSize;
        getAction(this.url.queryUserInfo,parameter).then((res) => {
          if (res.success) {
            this.dataSourceUser = [];
            this.dataSourceUser = res.result.records
            this.ipaginationUser.total = res.result.total;
          }
        })
        //获取查询条件
        var sqp = {};
        var param = Object.assign(sqp, this.queryParam, this.isorter );
        // param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;

        getAction(this.url.list,param).then((res) => {
          if (res.success) {
            this.dataSource = [];
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total;
          }else {
            this.$message.warning(res.message)
          }
        })

      },

      handleOk(){
        let params = [];
        for(var i=0;i<this.checked.length;i++){
          let obj = {};
          obj.id = this.checked[i];
          obj.dispatchToUser = this.checkedUser.dispatchToUser;
          obj.pressTime = this.queryParam.pressTime;
          params.push(obj)
        }
        httpAction(this.url.dispatchingInfo,params,"post").then((res) => {
          if (res.success) {
            this.$message.success(res.message);
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.visible = false;
        })
      },
      handleCancel () {
        this.visible = false;
      },
      handleEdit2(){

      },
      onSelectChangeUser(selectedRowUserKeys,selectionRows){
        this.selectedRowUserKeys = selectedRowUserKeys;
        this.selectionUserRows = selectionRows;
        if (this.selectedRowUserKeys.length == 1) {
          this.checkedUser.dispatchToUser = selectionRows[0].id
        }else {
          this.$message.warning('请选择一条记录')
        }
      },
      onSelectChange(selectedRowKeys,selectionRows){
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        this.checked = [];
        for(var i=0; i< selectionRows.length;i++){
          this.checked.push(selectionRows[i].id)
        }
      },
    }
  }
</script>

<style scoped lang="less">
  /deep/ .ant-modal{
    width: 80% !important;
  }

</style>