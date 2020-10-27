<template>
  <a-modal
    title="派工"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <template>
      <a-form>
        <a-row :gutter="24">
          <div style="width: 98%;margin: 10px auto;">
            <a-col :span="8">
              <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag-ora type="list" :trigger="true"  v-model="queryParamUser.lineNo"   dictCode="o_area,area_name,area_id"
                                       placeholder="请选择检定线"/>
              </a-form-item>
              <a-form-item label="派单对象" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input  placeholder="请输入派单对象" v-model="queryParamUser.realname" ></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="8">
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

              <a-form-item label="要求完成时间:" :labelCol="{span:7}" :wrapperCol="wrapperCol">
                <j-date placeholder="要求完成时间" v-model="queryParam.pressTime"
                        :trigger-change="true" style="width: 100%"/>
              </a-form-item>

            </a-col>
          </div>

            <a-col :span="8">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                    <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                </span>
            </a-col>

        </a-row>
        <a-row :gutter="24">
          <div style="width: 98%;margin: 10px auto;">
            <a-col :span="12">
              <a-table
                ref="table"
                size="small"
                bordered
                rowKey="id"
                :columns="columnsUser"
                :dataSource="dataSourceUser"
                :pagination="ipaginationUser"
                :loading="loading"
                :rowSelection="{fixed:true,type:radio,selectedRowKeys: selectedRowUserKeys, onChange: onSelectChangeUser}"
              >
               <span slot="action" slot-scope="text,record,index">　
                <a @click="handleEdit2(index,record)">关联</a>
               </span>

              </a-table>
            </a-col>
            <a-col :span="12">
              <a-table
                ref="table"
                bordered
                size="small"
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :scroll="tableScroll"
                :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
              >

              </a-table>
            </a-col>
          </div>
        </a-row>
      </a-form>


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
    import moment from 'moment'
    export default {
        name: "CompleteOrdWorkOrderInfoModal",
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
                    pageSize: 6,
                    pageSizeOptions: ['6', '12', '18'],
                    showTotal: (total, range) => {
                        return range[0] + "-" + range[1] + " 共" + total + "条"
                    },
                },
                bodyStyle:{
                    padding: "0",
                    height:(window.innerHeight-150)+"px",
                },
                modalWidth:200,
                labelCol: {
                    xs: {span: 24},
                    sm: {span: 5},
                },
                wrapperCol: {
                    xs: {span: 24},
                    sm: {span: 16},
                },
                tableScroll:{x :47*35+50},
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
              ipagination: {
                current: 1,
                pageSize: 6,
                pageSizeOptions: ['6', '12', '18'],
                showTotal: (total, range) => {
                  return range[0] + "-" + range[1] + " 共" + total + "条"
                },
              },
                columns:[
                    {
                        title: '工单号',
                        align: "center",
                        dataIndex:'id'
                    },
                    {
                        title: '工单类型',
                        align: "center",
                        dataIndex: 'orderType',
                        customRender:function (text) {
                            if(text === '01'){
                                return "故障工单"
                            }else if(text === '09'){
                                return "督办工单"
                            }else {
                                return ""
                            }
                        }
                    },
                    {
                        title: '工单名称',
                        align: "center",
                        dataIndex: 'alarmReason'
                    },
                    {
                        title:'检定线',
                        align:"center",
                        dataIndex: 'lineNo_dictText'
                    },
                    {
                        title:'故障等级',
                        align:"center",
                        dataIndex: 'alarmLevel_dictText'
                    },
                    {
                        title:'报警次数',
                        align:"center",
                        dataIndex: 'alarmAmount',
                    },
                    {
                        title: '报警内容',
                        align: "center",
                        dataIndex: 'alarmDesc'
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
                    queryUserInfo:"/workOrder/ordWorkOrderInfo/queryUserInfo",
                    list: "/workOrder/ordWorkOrderInfo/list",
                    delete: "/workOrder/ordWorkOrderInfo/delete",
                    deleteBatch: "/workOrder/ordWorkOrderInfo/deleteBatch",
                    exportXlsUrl: "/workOrder/ordWorkOrderInfo/exportXls",
                    importExcelUrl: "workOrder/ordWorkOrderInfo/importExcel",
                    dispatchingInfo:"/workOrder/ordWorkOrderInfo/dispatchingInfo",
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
                // 指派对象
                this.checkedUser.dispatchToUser = userId
                this.selectedRowKeys = keys;
                this.selectionRows = records;
                this.checked.push(records[0].id)
                this.getData ()
                this.visible = true
            },
            //故障报警模块跳转
            faultAlarmShow(key,record){
                let userId = store.getters.userInfo.id
                this.selectedRowUserKeys.push(userId)
                this.selectedRowKeys[0] = key;
                this.selectionRows[0] = record;
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
                // const that = this;
                // let params = [];
                // for(var i=0;i<this.checked.length;i++){
                //     let obj = {};
                //     obj.id = this.checked[i];
                //     obj.dispatchToUser = this.checkedUser.dispatchToUser;
                //     obj.pressTime = this.queryParam.pressTime;
                //     params.push(obj)
                // }
                // console.log("派工数据111111",params)
                // httpAction(this.url.dispatchingInfo,params,"post").then((res) => {
                //     if (res.success) {
                //         this.$message.success(res.message);
                //         that.$emit('ok');
                //     } else {
                //         this.$message.warning(res.message)
                //     }
                // }).finally(() => {
                //     this.visible = false;
                // })
                this.visible = false;
            },
            handleCancel () {
                this.visible = false;
                this.$emit('ok');
            },
            handleEdit2(index,record){
                // console.log("index---",index)
                // console.log("record---",record)
                const that = this;
                let params = [];
                // console.log("选择的工单信息:",this.checked)
                // console.log("处理人:",this.checkedUser)
                // debugger
                // 催办时间
                let timeFlag = moment().format("YYYY-MM-DD")
                for(var i=0;i<this.checked.length;i++){
                    let obj = {};
                    // 获取工单号
                    obj.id = this.checked[i];
                    // 获取工单派发对象
                    obj.dispatchToUser = this.checkedUser.dispatchToUser;
                    // 工单催办时间
                    if(this.queryParam.pressTime){
                        obj.pressTime = this.queryParam.pressTime;
                    }else{
                        obj.pressTime = timeFlag
                    }
                    params.push(obj)
                }
                console.log("提交的表单数据:",params)
                // debugger
                httpAction(this.url.dispatchingInfo,params,"post").then((res) => {
                    if (res.success) {
                        this.getData ()
                        this.$message.success("关联成功");
                    } else {
                        this.$message.warning("关联失败")
                    }
                })
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
                console.log("人员数据1111111",this.checked)
            },
        }
    }
</script>

<style scoped>
  .ant-modal{
    width: 80% !important;
  }
</style>