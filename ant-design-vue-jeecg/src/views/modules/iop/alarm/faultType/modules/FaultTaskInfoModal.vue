<template>
  <div>
    <a-modal
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      :footer="null"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form" style="border: 1px solid #e8e8e8;">
          <a-row>
            <a-col>
              <a-form-item label="报警类型" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-input v-model="typeName" read-only></a-input>
                <a-input v-model="faultType" hidden="hidden"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8" style="width: 30%;margin-left: 5%;">
              <a-form-item label="报警名称" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <!--            <j-dict-select-tag-ora type="list" :triggerChange="trigger" v-decorator="[ 'faultNameId', validatorRules.faultNameId]" :dictCode="whereDictCode"-->
                <!--                                   placeholder="请输入报警名称" @change="changeFaultName"/>-->
                <a-input v-model="faultName"></a-input>
<!--                <a-select show-search-->
<!--                          :value="value"-->
<!--                          :autoClearSearchValue="false"-->
<!--                          :default-active-first-option="false"-->
<!--                          :show-arrow="false"-->
<!--                          :filter-option="true"-->
<!--                          :not-found-content="null"-->
<!--                          @search="handleSearch"-->
<!--                          @change="handleChange"-->
<!--                          @blur="handleBlur"-->
<!--                >-->
<!--                  <a-select-option v-for="d in selectFaultNamedata"-->
<!--                                   :key="(d.EQUIP_TYPE+d.FAULE_NAME)" :value="d.FAULE_NAME">-->
<!--                    {{ d.FAULE_NAME }}-->
<!--                  </a-select-option>-->
<!--                </a-select>-->
              </a-form-item>
            </a-col>
            <a-col :span="8" style="width: 1%;">
              <a-button @click="handleAddFaultName" type="primary" icon="plus">选择</a-button>
            </a-col>
            <a-col :span="8" style="width: 37%;margin-left: 6%;">
              <a-form-item label="系统名称" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'systemName', validatorRules.systemName]" placeholder=""></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <a-form-item label="故障运维班组" :labelCol="{span:3}" :wrapperCol="{span:15}">
                  <!--<a-checkbox-group @change="lineTeamChange" style="width: 100%" v-model="checkedList" :options="lineTeamList">-->
                  <a-row style="border: 1px solid #e8e8e8;">
                    <a-col :span="12" v-for="(item,index) in lineTeamList" :key="5+index" style="margin-left: 12px;width: 40%;">
                      <a-checkbox :value="item.value" @change="lineTeamChange" >{{ item.text }}</a-checkbox>
                    </a-col>
                    <a-col :span="12" v-for="(item,index) in checkedList" :key="index" style="margin-left: 12px;width: 40%;">
                      <a-checkbox :value="item.value" @change="lineTeamChange"  :defaultChecked="checked">{{ item.text }}</a-checkbox>
                    </a-col>
                  </a-row>
                  <!--</a-checkbox-group>-->
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <a-form-item label="故障类型" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <JDictSelectTag v-decorator="['faultLabe', validatorRules.faultLabe]" :trigger-change="true"
                                dictCode="FAULT_LABE"  placeholder="请选择故障类型"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <a-form-item label="状态" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-radio-group v-decorator="['isStatus', validatorRules.isStatus]" defaultValue="1" >
                  <a-radio value="1" >
                    启用
                  </a-radio>
                  <a-radio value="2">
                    禁用
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>


        </a-form>

        <!-- 子表单区域 -->
        <a-tabs v-model="activeKey" @change="handleChangeTabs" style="border: 1px solid #e8e8e8;">
          <a-tab-pane tab="故障原因" :key="refKeys[2]" :forceRender="true">
            <a-button type="primary" @click="handleAdd" style="margin-top: 5px;margin-bottom: 10px;margin-left: 10px;">添加</a-button>
            <a-table
              :ref="refKeys[2]"
              bordered
              :columns="faultReason.columns"
              :dataSource="faultReason.dataSource"
              :loading="faultReason.loading"
              :pagination="faultReason.ipagination"
              @change="getShowSource"
            >
              <span slot="faultReasonAction" slot-scope="text, record, index">
                <a
                  @click="actionModify(index,record)">
                  修改
                </a>
                <a-divider type="vertical"/>
                <a
                  @click="actionDelete(record, index)">
                  删除
                </a>
              </span>
            </a-table>
          </a-tab-pane>
          <a-tab-pane tab="修复方法" :key="refKeys[0]" :forceRender="true">
            <a-button type="primary" @click="handleAdd" style="margin-top: 5px;margin-bottom: 10px;margin-left: 10px;">添加</a-button>
            <a-table
              :ref="refKeys[0]"
              bordered
              :columns="faultRepairInfoTable.columns"
              :dataSource="faultRepairInfoTable.dataSource"
              :loading="faultRepairInfoTable.loading"
              :pagination="faultRepairInfoTable.ipagination"
              @change="getShowSource">

              <span slot="action" slot-scope="text, record, index">
                <a
                  @click="actionModify(index,record)">
                  修改
                </a>
                <a-divider type="vertical"/>
                <a
                  @click="actionDelete(record, index)">
                  删除
                </a>
              </span>
            </a-table>
          </a-tab-pane>

          <a-tab-pane tab="作业指导" :key="refKeys[1]" :forceRender="true">
            <a-button type="primary" @click="handleAdd" style="margin-top: 5px;margin-bottom: 10px;margin-left: 10px;">添加</a-button>
            <a-table
              :ref="refKeys[1]"
              bordered
              :columns="workTable.columns"
              :dataSource="workTable.dataSource"
              :loading="workTable.loading"
              :pagination="workTable.ipagination"
              @change="getWorkTableShowSource"
            >
<!--              <template slot="action" slot-scope="text,recond">-->
<!--                <a-button-->
<!--                  :ghost="true"-->
<!--                  type="primary"-->
<!--                  icon="download"-->
<!--                  size="small"-->
<!--                  @click="uploadFile(recond)">-->
<!--                  下载-->
<!--                </a-button>-->
<!--              </template>-->
<!--              <template slot="action" slot-scope="text,recond">-->
<!--                <a-->
<!--                  @click="handleAdd">-->
<!--                  添加-->
<!--                </a>-->
<!--              </template>-->
              <span slot="action" slot-scope="text, record, index">
<!--                <a-->
<!--                  @click="actionModify(index,record)">-->
<!--                  修改-->
<!--                </a>-->
<!--                <a-divider type="vertical"/>-->
                <a
                  @click="actionDelete(record, index)">
                  删除
                </a>
              </span>
            </a-table>
          </a-tab-pane>

        </a-tabs>

        <!-- 操作按钮区域 -->
        <div class="ant-table-footer" style="text-align: center">
<!--          <a-button @click="handleAdd" type="primary" style="margin: 5px" icon="plus">新增行</a-button>-->
          <a-button @click="handleCancel" type="primary" style="margin: 5px" icon="delete">关闭</a-button>
          <a-button @click="handleOk" type="primary" style="margin: 5px" icon="plus">保存</a-button>
        </div>
      </a-spin>
    </a-modal>
    <RepairInfoModal ref="repairInfoFrom" @returnDataRepair="returnDataRepair"></RepairInfoModal>
    <WorkModal ref="returnWork" @returnWork="returnWork"></WorkModal>
    <faultReasonInfo-modal ref="returnReason"  @returnReason="returnReason"></faultReasonInfo-modal>

    <FaultName ref="returnFaultName" @returnFaultName="returnFaultName"  ></FaultName>
  </div>
</template>

<script>

  import { httpAction , getAction,postAction ,downFile} from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import RepairInfoModal from './RepairInfoModal.vue'
  import WorkModal from './WorkModal.vue'
  import FaultReasonInfoModal from './FaultReasonInfoModal.vue'
  import { initDictOptionsOra ,filterDictText} from '@/components/dict/JDictSelectUtil'
  import FaultName from "./FaultName"

  export default {
    name: "FaultTaskInfoModal",
    components: {
      JDictSelectTag,
      JMultiSelectTag,
      JDictSelectTagOra,
      RepairInfoModal,
      WorkModal,
      FaultReasonInfoModal,
      FaultName
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title: "操作",
        width: 1200,
        isModify:false,
        visible: false,
        faultNameVisible:false,
        checked: true,
        defaultChecked: true,
        trigger: true,
        typeName: '',
        faultName: '',
        faultType: '',
        parentId:'',
        checkedValue:'',
        value:'',
        selectFaultNamedata:[],
        lineTeamList:[],
        checkedList:[],
        checkedValueNew:[],
        equipNo:'',
        model: {},
        dataFormList: [],
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        validatorRules: {
          faultName: {
            rules: [
              {required: true, message: '请输入修复方法!'}
            ]
          },
          faultType: {
            rules: []
          },
          lineTeam: {
            rules: []
          },
          faultLabe: {
            rules: []
          },
          isStatus: {
            rules: []
          },
          typeName: {
            rules: []
          },
          shiftId: {
            rules: []
          },
          systemName: {
            rules: [{required: true, message: '请输入系统名称'}]
          },
          faultNameId: {
            rules: []
          },
        },
        refKeys: ['faultRepairInfo', 'faultSopInfo2','faultReason'],
        tableKeys: ['faultRepairInfo', 'faultSopInfo2','faultReason'],
        activeKey: 'faultRepairInfo',
        // 修复方法
        faultRepairInfoTable: {
          loading: false,
          deleteData:true,
          dataSource: [{sequence:'',}],
          totalDataSource:[],
          ipagination: {
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            sizeList: ['5', '10', '15'], //一页能显示条数
            pageSize: 5, //当前页显示多少条
            current: 1, //当前页
            total: 0,
            showSizeChanger: true
          },
          columns: [
            {
              title: '显示顺序',
              dataIndex: 'sequence',
              placeholder: '请输入${title}',
              align: "center",
            },
            {
              title: '方法名称',
              dataIndex: 'repairName',
              align: "center",
            },
            {
              title: '修复类型',
              dataIndex: 'execMode',
              align: "center",
              customRender: function (t, r, index) {
                if (t == 'self_healing') {
                  return "系统自愈"
                } else if (t == 'artificial') {
                  return "人工处理"
                } else if (t == 'remote') {
                  return "远程处理"
                }
              }
            },
            {
              title: '设备更换',
              dataIndex: 'isReplace',
              placeholder: '请输入${title}',
              align: "center",
              customRender: function (t, r, index) {
                if (t == 'Y') {
                  return "是"
                } else if (t == 'N') {
                  return "否"
                }
              }
            },
            {
              title: '说明',
              dataIndex: 'remark',
              width: "200px",
              align: "center",
            },
            {
              title: '操作',
              dataIndex: 'action',
              align:"center",
              scopedSlots: { customRender: 'action' }
            },
          ]
        },
        workTable: {
          loading: false,
          dataSource: [{index:'',}],
          totalDataSource:[],
          deleteData:true,
          ipagination: {
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            sizeList: ['5', '10', '15'], //一页能显示条数
            pageSize: 5, //当前页显示多少条
            current: 1, //当前页
            total: 0,
            showSizeChanger: true
          },
          columns: [
            // {
            //   title: '序号',
            //   dataIndex: 'rowIndex',
            //   placeholder: '请输入${title}',
            //   align: "center",
            //   customRender:function (t,r,index) {
            //     return parseInt(index)+1;
            //   }
            // },
           {
             title: '文件名称',
             width: '24%',
             dataIndex: 'workName',
             align: "center",
           },
            {
              title: '文件地址',
              dataIndex: 'sop',
              width: '38%',
              align: "center",
            },
           {
             title: '操作',
             key: 'action',
             width: '8%',
             align: "center",
             scopedSlots: { customRender: 'action' }
           }
          ]
        },
        faultReason: {
          loading: false,
          deleteData:true,
          dataSource: [{sequence:'',}],
          totalDataSource:[],
          ipagination: {
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            sizeList: ['5', '10', '15'], //一页能显示条数
            pageSize: 5, //当前页显示多少条
            current: 1, //当前页
            total: 0,
            showSizeChanger: true
          },
          columns: [
            {
              title: '顺序号',
              dataIndex: 'sequence',
              placeholder: '请输入${title}',
              align: "center",
            },
            {
              title: '故障原因',
              dataIndex: 'reasonName',
              align: "center",
            },
            {
              title: '原因分类',
              dataIndex: 'reasonMode',
              align: "center",
              customRender: function (t, r, index) {
                if (t == 'self_healing') {
                  return "系统自愈"
                } else if (t == 'artificial') {
                  return "人工处理"
                } else if (t == 'remote') {
                  return "远程处理"
                }
              }
            },
            {
              title: '原因描述',
              dataIndex: 'remark',
              width: "200px",
              align: "center",
            },
            {
              title: '操作',
              dataIndex: 'faultReasonAction',
              align:"center",
              scopedSlots: { customRender: 'faultReasonAction' }
            },
          ]
        },
        url: {
          add: "/alarmType/faultTaskInfo/add",
          edit: "/alarmType/faultTaskInfo/edit",
          queryById: "/alarmType/faultTaskInfo/queryById",
          queryFaultRepairInfoByMainId: "/alarmType/faultTaskInfo/queryFaultRepairInfoByMainId",
          queryFaultSopInfoByMainId: "/alarmType/faultTaskInfo/queryFaultSopInfoByMainId",
          queryFaultReasonInfoListByMainId: "/alarmType/faultTaskInfo/queryFaultReasonInfoListByMainId",
          queryFaulNameByEquipType: "/alarmType/faultTaskInfo/queryFaulNameByEquipType",
          downMinio: "/sys/upload/downMinio"
        }
      }
    },
    created () {
    },
    computed: {
      whereDictCode: function() {
          console.log("equipNo",this.equipNo)
        return "MIDHD.MT_EQUIP_ALARM_INFO,ALARM_REASON,ID,EQUIP_NO='"+this.equipNo+"'";
      },
    },
    methods: {
      actionModify(index,record){
          console.log('修改了')
          if ('faultRepairInfo' == this.activeKey) {
              //修复方法
              this.$refs.repairInfoFrom.edit(index,record);
          }else if('faultReason'== this.activeKey){
              //故障原因
              this.$refs.returnReason.edit(index,record);
          }else {
              //指导作业
              // this.$refs.returnWork.showModal();
          }
      },
      actionDelete( record, index){
          if ('faultRepairInfo' == this.activeKey) {
              //修复方法
              let resultArray = []
              this.faultRepairInfoTable.loading = true;
              for(var i = 0 ;i<this.faultRepairInfoTable.dataSource.length;i++){
                  if(i !== index){
                      resultArray.push(this.faultRepairInfoTable.dataSource[i])
                  }
              }
              this.faultRepairInfoTable.dataSource = resultArray
              this.faultRepairInfoTable.loading = false;

              // this.$refs.repairInfoFrom.add();
          }else if('faultReason'== this.activeKey){
              //故障原因
              let resultArray = []
              this.faultReason.loading = true;
              for(var i = 0 ;i<this.faultReason.dataSource.length;i++){
                  if(i !== index){
                      resultArray.push(this.faultReason.dataSource[i])
                  }
              }
              this.faultReason.dataSource = resultArray
              this.faultReason.loading = false;
          }else {
              //指导作业
              // this.$refs.returnWork.showModal();
              let resultArray = []
              this.workTable.loading = true;
              for(var i = 0 ;i<this.workTable.dataSource.length;i++){
                  if(i !== index){
                      resultArray.push(this.workTable.dataSource[i])
                  }
              }
              this.workTable.dataSource = resultArray
              this.workTable.loading = false;
          }
      },
      initDictConfig(){
      },
      add (recondTree,dataFormList,recondTable) {
        this.checkedValue = []
        this.checkedValueNew = []
        // 修复方法
        this.faultRepairInfoTable.dataSource = []
        // 作业指导
        this.workTable.dataSource = []
        // 故障原因
        this.faultReason.dataSource = []
        this.isModify = false;
        this.form.resetFields();
        // 报警名称
        this.faultName = ""
        this.visible = true;
        this.dataFormList = dataFormList;
        this.typeName = recondTree.title
        this.faultType = recondTree.value;
        // this.equipId = recondTree.equipId;
        this.parentId = recondTree.parentId;
        // this.equipNo= recondTree.equipNo;
        let str="O_SHIFT,SHIFT_NAME,SHIFT_ID,equip_categ='"+this.parentId+"'";
        initDictOptionsOra(str).then(res => {
          this.lineTeamList = res.result
          if(this._.has(recondTable,'lineTeam')){
            this.editcheckeds=this._.split(recondTable.lineTeam,',');
            // this.checkedValueNew= this.editcheckeds;
            console.log(" this.editcheckeds----", this.editcheckeds);
            for(var i=0;i<this.editcheckeds.length;i++){
              let value=this.editcheckeds[i];
              if(value!=""){
                let indexs=this._.findIndex(this.lineTeamList,function (o) {
                  return o.value == value;
                });
                this.checkedList.push(this.lineTeamList[indexs])
                let evens = this._.remove(this.lineTeamList, function(n) {
                  return n.value == value;
                });
              }
            };
          }
        });
        getAction(this.url.queryFaulNameByEquipType,{id:recondTree.value}).then((res)=>{
          if(res.success){
            this.selectFaultNamedata=res.result;
          }
        });

        // this.edit(recondTable);
      },

      edit (recondTree,record) {
        this.isModify = true
        this.parentId = recondTree.parentId;
        let str="O_SHIFT,SHIFT_NAME,SHIFT_ID,equip_categ='"+this.parentId+"'";
        initDictOptionsOra(str).then(res => {
            this.lineTeamList = res.result
            if(this._.has(record,'lineTeam')){
                this.editcheckeds=this._.split(record.lineTeam,',');
                this.checkedValueNew= this.editcheckeds;
                console.log(" this.editcheckeds----", this.editcheckeds);
                for(var i=0;i<this.editcheckeds.length;i++){
                    let value=this.editcheckeds[i];
                    if(value!=""){
                        let indexs=this._.findIndex(this.lineTeamList,function (o) {
                            return o.value == value;
                        });
                        this.checkedList.push(this.lineTeamList[indexs])
                        let evens = this._.remove(this.lineTeamList, function(n) {
                            return n.value == value;
                        });
                    }
                };
            }
        });
        this.form.resetFields();
        this.typeName = recondTree.title
        record.typeName = recondTree.title
        // 报警名称
        this.faultName = record.faultName
        let temp = this._.cloneDeep(record)
        this.model = Object.assign({}, temp);
        console.log("----------------------",this.model)
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'faultName', 'faultNameId','faultType', 'lineTeam', 'faultLabe', 'isStatus','systemName','typeName'))
          this.form.setFieldsValue({faultNameId:this.model.faultName});
        });

        getAction(this.url.queryFaultRepairInfoByMainId,{id:record.id}).then((res)=>{
          if(res.success){
            this.faultRepairInfoTable.dataSource=res.result;
          }
        });
        getAction(this.url.queryFaultSopInfoByMainId,{id:record.id}).then((res)=>{
          if(res.success){
            if(!res.result.length > 0){
                this.workTable.dataSource=[{index:'',}]
            }else{
                let resultArray = []
                for(var i = 0 ;i<res.result.length;i++){
                    let temp = {}
                    let splitArray = res.result[i].sop.split(".")
                    // 获取文件夹名
                    let fileName = splitArray[splitArray.length - 2]
                    // 获取文件后缀名
                    let afterFile = splitArray[splitArray.length - 1]
                    let realFileArray = fileName.split("_")
                    let realFileName = realFileArray[0].split("/")
                    temp.sop = res.result[i].sop
                    temp.workName = realFileName[realFileName.length - 1]+"."+afterFile
                    resultArray.push(temp)
                }
                this.workTable.dataSource=resultArray;
            }
          }
        });
        getAction(this.url.queryFaultReasonInfoListByMainId,{id:record.id}).then((res)=>{
          if(res.success){
            this.faultReason.dataSource=res.result;
          }
        });
      },

      close () {
        this.$emit('close');
        this.visible = false;
        this.typeName = '';
        this.faultType = '';
        this.equipId = '';
        this.checkedValue='';
        this.checkedValueNew='';
        this.lineTeamList=[];
        this.faultRepairInfoTable.dataSource=[{index:''}];
        this.workTable.dataSource=[{index:''}];
        this.faultReason.dataSource=[{index:''}];
        this.checkedList=[];
        this.equipNo='';
      },
      handleOk () {
        const that = this;
        if(that.dataFormList != null && !this.isModify){
          for(let item of that.dataFormList){
            if(item.faultName == that.faultName){
              that.$message.warning("报警名称已经存在,请重新选择");
              return
            }
          }
        }
        let repairData = that.faultRepairInfoTable.dataSource;
        let workData = that.workTable.dataSource;
        let faultReason = that.faultReason.dataSource;
        that.checkedValueNew;
        for(var i = 0 ; i<that.checkedValueNew.length;i++){
            if(i == 0 ){
                that.checkedValue = that.checkedValueNew[i]
            }else{
                that.checkedValue = that.checkedValue + "," +that.checkedValueNew[i]
            }
        }
        let formCheckValue = that.checkedValue;

        console.log("formCheckValue----",formCheckValue)
        // 触发表单验证
        this.form.validateFields((err, values) => {
            console.log("values---",values)
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            let obj = {
              faultRepairInfoList: repairData,
              faultSopInfoList: workData,
              faultReasonInfoList: faultReason,
              faultType:that.faultType,
              equipId:that.equipId,
              equipNo:that.equipNo,
              faultName:that.faultName,
              lineTeam:formCheckValue
            };
            let modelEdit = [];
            let formData = [];
            console.log("表单提交数据obj", obj)
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
              formData = Object.assign(obj, values);
            } else {
              httpurl += this.url.edit;
              method = 'put';
              this.model.faultName=that.faultName;
              modelEdit = Object.assign(this.model, values);
              modelEdit.lineTeam=formCheckValue;
              formData = Object.assign(obj, modelEdit);
            }
//            let formData = Object.assign(this.model, values);
            console.log("表单提交数据", formData)
            console.log("表单提交数据httpurl", httpurl)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row, 'faultName', 'faultType', '', 'faultLabe', 'isStatus'))
      },
      /** ATab 选项卡切换事件 */
      handleChangeTabs(key) {
        console.log("key-----", key)

      },
      inputTextShiftId(val) {
        this.form.setFieldsValue({shiftName: val})
      },
      handleAdd(){
        if ('faultRepairInfo' == this.activeKey) {
          //修复方法
          this.$refs.repairInfoFrom.add();
        }else if('faultReason'== this.activeKey){
          //故障原因
          this.$refs.returnReason.add();
        }else {
          //指导作业
          this.$refs.returnWork.showModal();
        }
      },
      returnDataRepair(repairInfo,index){
          if(index !== ""){
              let resultArray = []
              this.faultRepairInfoTable.loading = true;
              for(var i = 0 ;i<this.faultRepairInfoTable.dataSource.length;i++){
                  if(i !== index){
                      resultArray.push(this.faultRepairInfoTable.dataSource[i])
                  }else{
                      resultArray.push(repairInfo)
                  }
              }
              this.faultRepairInfoTable.dataSource = resultArray
              this.faultRepairInfoTable.loading = false;

          }else{
              if(this.faultRepairInfoTable.deleteData){
                  this.faultRepairInfoTable.dataSource= []
              }
              if(repairInfo!=null){
                  this.faultRepairInfoTable.deleteData=false;
              }
              let dataArry = this.faultRepairInfoTable.dataSource;
              const newArry = dataArry.concat(repairInfo)
              this.faultRepairInfoTable.dataSource = newArry;
              this.faultRepairInfoTable.totalDataSource = newArry;
              this.faultRepairInfoTable.ipagination.total = this.faultRepairInfoTable.dataSource.length
          }

      },
      //实现分页效果
      getShowSource(pagination) {
         pagination= this._.cloneDeep(pagination);
//        var keyValue = 0;
//        var data = this.faultRepairInfoTable.totalDataSource;//后端返回的全部数据
//        for (var i = 0; i < data.length; i++) {
//          keyValue = keyValue + 1;
//          let key = {key: keyValue};
//          data[i] = Object.assign(data[i], key);
//        }
//        var start =pagination.pageSize * pagination.current - pagination.pageSize;
//        var end =pagination.pageSize * pagination.current;
//        this.faultRepairInfoTable.dataSource = data.slice(start, end);
        this.faultRepairInfoTable.ipagination.current=pagination.current;

      },
      lineTeamChange(v){
          console.log(v)
          var checkedValue=v.target.value;
          console.log(checkedValue)
          // 若勾选了则添加
          if(v.target.checked){
            this.checkedValueNew =this._.concat(this.checkedValueNew, checkedValue);
          }else {
            this.checkedValueNew=this._.pull(this.checkedValueNew,v.target.value);
          }
          console.log("this.checkedValueNew----",this.checkedValueNew)
          // if(this.checkedValueNew.length > 0){
          //     console.log("this.checkedValue----",this.checkedValue)
          //     let vul=this.checkedValueNew.join(',');
          //     this.checkedValue=vul;
          // }
          // this.checkedValue=this.checkedValueNew;
          // console.log("this.checkedValue----",this.checkedValue)
      },
      returnWork(data){
          console.log("workTable",data)
          if(this.workTable.deleteData){
            this.workTable.dataSource= []
          }
        if(data!=null){
          this.workTable.deleteData=false;
        }
        let dataArry = this.workTable.dataSource;
        const newArry = dataArry.concat(data)
        this.workTable.dataSource = newArry;
        this.workTable.totalDataSource = newArry;
        this.workTable.ipagination.total = this.workTable.dataSource.length
      },
      returnReason(data,index){
        if(index !== ""){
            let resultArray = []
            this.faultReason.loading = true;
            for(var i = 0 ;i<this.faultReason.dataSource.length;i++){
                if(i !== index){
                    resultArray.push(this.faultReason.dataSource[i])
                }else{
                    resultArray.push(data)
                }
            }
            this.faultReason.dataSource = resultArray
            this.faultReason.loading = false;

        }else{
            if(this.faultReason.deleteData){
                this.faultReason.dataSource= []
            }
            if(data!=null){
                this.faultReason.deleteData=false;
            }
            let dataArry = this.faultReason.dataSource;
            const newArry = dataArry.concat(data)
            this.faultReason.dataSource = newArry;
            this.faultReason.totalDataSource = newArry;
            this.faultReason.ipagination.total = this.faultReason.dataSource.length
        }
      },
      //实现分页效果
      getWorkTableShowSource(pagination) {
        pagination= this._.cloneDeep(pagination);
        this.workTable.ipagination.current=pagination.current;
      },
      /* 文件下载 */
      uploadFile(row){
        console.log("row-------",row)
        let text=row.sop;
        if(!text){
          this.$message.warning("未知的文件")
          return;
        }
        text = text.substring(text.lastIndexOf("/")+1);
        window.open(window._CONFIG['downMinio']+"?params="+text);
      },
      changeFaultName(value){
        let str="MIDHD.MT_EQUIP_ALARM_INFO,ALARM_REASON,ID,ID='"+value+"'";
        initDictOptionsOra(str).then(res => {
            console.log("res.result----", res.result)
          this.faultName = res.result[0].text
        });
      },

      handleSearch (val) {
        this.value = val;
        fetch(val, data => (this.data = data));
      },
      handleChange(val) {
        this.value = val;
        this.faultName = val;
        fetch(val, data => (this.data = data));
      },
      handleBlur(val) {
        this.handleChange(val);
      },
      handleAddFaultName(){
        this.$refs.returnFaultName.show(this.faultType);
      },
      returnFaultName(v){
        this.faultName=v.realReason;
      }
    }
  }
</script>