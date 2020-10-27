<template>
  <div>
    <a-row :gutter="10">
      <a-card :bordered="false" >
      <a-col :md="6" :sm="24"  class="tree-scroll">
          <a-col :md="10" :sm="24">
            <template>
               <span style="user-select: none">
                  <a-tree
                    @select="onSelect"
                    @check="onCheck"
                    :selectedKeys="selectedKeys"
                    :checkedKeys="checkedKeys"
                    :treeData="departTree"
                    :checkStrictly="checkStrictly"
                    @expand="onExpand"/>
                </span>
            </template>
          </a-col>
      </a-col>
      <a-col :md="18" :sm="24">
        <a-tabs defaultActiveKey="1">
          <a-tab-pane tab="基本信息" key="1" >
            <div class="table-page-search-wrapper">
              <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                  <a-col :xl="6" :lg="7" :md="8" :sm="24">
                    <a-form-item label="报警名称">
                      <a-input placeholder="" v-model="queryParam.faultName"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="6" :lg="7" :md="8" :sm="24">
                    <a-form-item label="故障类型" :xl="6" :lg="7" :md="8" :sm="24">
                      <JDictSelectTag v-model="queryParam.faultLabe" :trigger="true"
                                      dictCode="FAULT_LABE" placeholder="请选择故障类型"/>
                    </a-form-item>
                  </a-col>
                  <a-col :xl="6" :lg="7" :md="8" :sm="24">
                    <a-form-item label="状态">
                      <a-radio-group v-model="queryParam.isStatus" >
                        <a-radio value="1">
                          启用
                        </a-radio>
                        <a-radio value="2">
                          禁用
                        </a-radio>
                      </a-radio-group>
                    </a-form-item>
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
            <!-- 操作按钮区域 -->
            <div class="table-operator">
              <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
              <a-button @click="handleAddEdit" type="primary" icon="plus">编辑</a-button>
              <a-button @click="handleLook" type="primary" icon="plus">查看</a-button>
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
          </a-tab-pane>
        </a-tabs>
      </a-col>
      </a-card>
    </a-row>
    <faultTaskInfo-modal ref="modalForm" @ok="modalFormOk"></faultTaskInfo-modal>
    <LookInfoModal ref="lookModalForm" ></LookInfoModal>
    <a-modal
      title=""
      :visible="visibleModel"
      @ok="handleOkModel"
      @cancel="handleCancelModel"
    >
      <p>是否删除报警类型！</p>
    </a-modal>
  </div>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import FaultTaskInfoModal from './modules/FaultTaskInfoModal'
  import LookInfoModal from './modules/LookInfoModal.vue'
  import { getAction ,deleteAction,postAction} from '@/api/manage'
  import { initDictOptionsOra, filterDictText } from '@/components/dict/JDictSelectUtil'
  import pick from 'lodash.pick'

  export default {
    name: "FaultTaskInfoList",
    mixins:[JeecgListMixin],
    components: {
      FaultTaskInfoModal,
      LookInfoModal
    },
    data () {
      let vm = this
      return {
        dictOptions: {
          shiftDict: {}
        },
        description: '报警类型管理管理页面',
        iExpandedKeys: [],
        loading: false,
        autoExpandParent: false,
        currFlowId: '',
        currFlowName: '',
        disable: true,
        treeData: [],
        visible: false,
        departTree: [],
        faultType: '',
        rightClickSelectedKey: '',
        hiding: true,
        model: {},
        dropTrigger: '',
        selectedKeys: [],
        checkedKeys: [],
        onselectedRecond: [],
        autoIncr: 1,
        currSelected: {},
        allTreeKeys:[],
        checkStrictly: true,
        visibleModel: false,
        form: this.$form.createForm(this),
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
            title:'报警名称',
            align:"center",
            dataIndex: 'faultName'
          },
          {
              title:'系统名称',
              align:"center",
              dataIndex: 'systemName'
          },
          {
            title:'故障运维班组',
            align:"center",
            dataIndex: 'lineTeam',
            customRender: function(t, r, index) {
              let codeArr = vm._.split(t, ',')
              let nameArr = []
              for (let i = 0; i < codeArr.length; i++) {
                let str = filterDictText(vm.dictOptions.shiftDict, codeArr[i])
                nameArr.push(str)
              }
              return vm._.join(nameArr, ',')
            }
          },
          {
            title:'故障类型',
            align:"center",
            dataIndex: 'faultLabe',
            customRender:function (t,r,index) {
              if(t=='01'){
                return '机械故障'
              }else if (t=='02'){
                return '电控故障'
              }
            }

          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'isStatus',
            customRender:function (t,r,index) {
                if(t=='1'){
                  return '启用'
                }else if (t=='2'){
                  return '禁用'
                }
            }

          }
//          ,
//          {
//            title: '操作',
//            dataIndex: 'action',
//            align:"center",
//            scopedSlots: { customRender: 'action' }
//          }
        ],
        url: {
          list: "/alarmType/faultTaskInfo/list",
          delete: "/alarmType/faultTaskInfo/delete",
          deleteBatch: "/alarmType/faultTaskInfo/deleteBatch",
          exportXlsUrl: "/alarmType/faultTaskInfo/exportXls",
          importExcelUrl: "alarmType/faultTaskInfo/importExcel",
          queryTreeList: "alarmType/faultTaskInfo/queryTreeList",
        },
      }
    },
    computed: {
    },
    methods: {
      initDictConfig(){
        initDictOptionsOra('O_SHIFT,SHIFT_NAME,SHIFT_ID').then(res => {
          this.dictOptions.shiftDict = res.result
        });
        this.loadTree();
      },
      loadTree() {
        var that = this
        that.treeData = []
        that.departTree = []
        getAction(this.url.queryTreeList).then((res)=>{
          if(res.success){
            if (res.success) {
              for (let i = 0; i < res.result.length; i++) {
                let temp = res.result[i]
                that.treeData.push(temp)
                that.departTree.push(temp)
                that.setThisExpandedKeys(temp)
                // console.log(temp.id)
              }
              this.loading = false
            }
          }
        });
      },
      setThisExpandedKeys(node) {
        if (node.children && node.children.length > 0) {
          this.iExpandedKeys.push(node.key)
          for (let a = 0; a < node.children.length; a++) {
            this.setThisExpandedKeys(node.children[a])
          }
        }
      },
      onCheck(checkedKeys, info) {
        console.log('onCheck', checkedKeys, info)
        this.hiding = false
        //this.checkedKeys = checkedKeys.checked
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
        if(this.checkStrictly){
          this.checkedKeys = checkedKeys.checked;
        }else{
          this.checkedKeys = checkedKeys
        }
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      },
      onSelect(selectedKeys, e) {
        console.log('selected', selectedKeys, e)
        this.hiding = false
        let record = e.node.dataRef
        console.log('onSelect-record', record)
        this.currSelected = Object.assign({}, record)
        this.model = this.currSelected
        this.selectedKeys = [record.key]
        this.model.parentId = record.parentId
        this.onselectedRecond=Object.assign({}, record);
        this.faultType=this.onselectedRecond.value;
        this.refreshTableData(record);

      },
      onExpand(expandedKeys) {
        console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        this.autoExpandParent = false
      },
      handleAdd(){
        if(this.onselectedRecond.leaf){
          this.$refs.modalForm.add(this.onselectedRecond,this.dataSource,{});
        }else {
          this.$message.warning("请选择左边检定线设备！！")
        }
      },
      modalFormOk () {
        this.refreshTableData();
      },
      refreshTableData(){
          //选择设备查询右边数据
          if(this.onselectedRecond.value==null){
              return
          }
        this.onClearSelected();
        var params = this.getQueryParams();//查询条件
        let obj = {
          faultType:this.onselectedRecond.value,
        };
        params = Object.assign(obj, params);
        getAction(this.url.list,params).then((res)=>{
          if(res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        });
      },
      searchReset() {
        this.queryParam = {};
        this.refreshTableData();
      },
      searchQuery(){
        this.refreshTableData();
      },
      handleAddEdit(){
        if(this.selectedRowKeys.length==1){
          // this.$refs.modalForm.add(this.onselectedRecond,this.selectionRows[0]);
            this.$refs.modalForm.edit(this.onselectedRecond,this.selectionRows[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
      },
      handleLook(){
        if(this.selectedRowKeys.length==1){
          this.$refs.lookModalForm.add(this.onselectedRecond,this.selectionRows[0]);
        }else if (this.selectedRowKeys.length>1) {
          this.$message.warning("只能勾选一条记录")
        }else {
          this.$message.warning("请选择一条记录")
        }
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
      loadData() {
        this.searchReset();
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .tree-scroll { height:600px;overflow-y:scroll;}
  .ant-alert-info {
    border: 1px solid #068b881f;
    background-color: #068b881f;
  }
</style>