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
                <a-input v-model="typeName" disabled="disabled"></a-input>
                <a-input v-model="faultType" hidden="hidden"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12" style="width: 37%; margin-left: 3%;">
              <a-form-item label="报警名称" :labelCol="{span:6}" :wrapperCol="{span:15}">
<!--                <a-input v-model="faultName"></a-input>-->
                <a-input v-decorator="[ 'faultName', validatorRules.faultName]" placeholder=""  disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12" style="width: 37%; margin-left: 3%;">
              <a-form-item label="系统名称" :labelCol="{span:6}" :wrapperCol="{span:15}" >
                <a-input v-decorator="[ 'systemName', validatorRules.systemName]" placeholder=""  disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <a-form-item label="故障运维班组" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <!--<a-checkbox-group @change="lineTeamChange" style="width: 100%" v-model="checkedList" :options="lineTeamList">-->
                <a-row style="border: 1px solid #e8e8e8;">
                  <a-col :span="12" v-for="(item,index) in lineTeamList" :key="5+index" style="margin-left: 12px;width: 40%;">
                    <a-checkbox :value="item.value"   disabled="disabled" >{{ item.text }}</a-checkbox>
                  </a-col>
                  <a-col :span="12" v-for="(item,index) in checkedList" :key="index" style="margin-left: 12px;width: 40%;">
                    <a-checkbox :value="item.value"  :defaultChecked="checked"  disabled="disabled">{{ item.text }}</a-checkbox>
                  </a-col>
                </a-row>
                <!--</a-checkbox-group>-->
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <a-form-item label="故障类型" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <JDictSelectTag v-decorator="['faultLabe', validatorRules.faultLabe]" :trigger-change="true"  disabled="disabled"
                                dictCode="FAULT_LABE"  placeholder="请选择故障类型"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <a-form-item label="状态" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-radio-group v-decorator="['isStatus', validatorRules.isStatus]" defaultValue="1"   disabled="disabled">
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
            <a-table
              :ref="refKeys[2]"
              bordered
              :columns="faultReason.columns"
              :dataSource="faultReason.dataSource"
              :loading="faultReason.loading"
              :pagination="faultReason.ipagination"
              @change="getShowSource"
            >
              <template slot="action" slot-scope="text,recond">
                <a
                  @click="handleAdd">
                  添加
                </a>
              </template>
            </a-table>
          </a-tab-pane>
          <a-tab-pane tab="修复方法" :key="refKeys[0]" :forceRender="true">
            <a-table
              :ref="refKeys[0]"
              bordered
              :columns="faultRepairInfoTable.columns"
              :dataSource="faultRepairInfoTable.dataSource"
              :loading="faultRepairInfoTable.loading"
              :pagination="faultRepairInfoTable.ipagination"
              @change="getShowSource"
            >
            </a-table>
          </a-tab-pane>

          <a-tab-pane tab="作业指导" :key="refKeys[1]" :forceRender="true">
            <a-table
              :ref="refKeys[1]"
              bordered
              :columns="workTable.columns"
              :dataSource="workTable.dataSource"
              :loading="workTable.loading"
              :pagination="workTable.ipagination"
              @change="getWorkTableShowSource"
            >
            </a-table>
          </a-tab-pane>

        </a-tabs>
        <!-- 操作按钮区域 -->
        <div class="ant-table-footer" style="text-align: center">
          <a-button @click="handleCancel" type="primary" style="margin: 5px" icon="delete">关闭</a-button>
        </div>
      </a-spin>
    </a-modal>
  </div>
</template>

<script>

  import { httpAction , getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  import {FormTypes, getRefPromise} from '@/utils/JEditableTableUtil'
  import {JEditableTableMixin} from '@/mixins/JEditableTableMixin'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import RepairInfoModal from './RepairInfoModal.vue'
  import { initDictOptionsOra } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "FaultTaskInfoModal",
    components: {
      JDictSelectTag,
      JMultiSelectTag,
      JDictSelectTagOra,
      RepairInfoModal,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title: "查看",
        width: 1200,
        visible: false,
        checked: true,
        typeName: '',
        faultType: '',
        parentId:'',
        checkedValue:'',
        lineTeamList:[],
        checkedList:[],
        checkedValueNew:[],
        editcheckeds:[],
        model: {},
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
            rules: []
          },
        },
        refKeys: ['faultRepairInfo', 'faultSopInfo2','faultReason'],
        tableKeys: ['faultRepairInfo', 'faultSopInfo2','faultReason'],
        activeKey: 'faultRepairInfo',
        // 修复方法
        faultRepairInfoTable: {
          loading: false,
          dataSource: [],
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
              defaultValue: '',
              width: "200px",
              align: "center",
            },
          ]
        },
        workTable: {
          loading: false,
          dataSource: [],
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
              title: '序号',
              dataIndex: 'rowIndex',
              placeholder: '请输入${title}',
              align: "center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
//            {
//              title: '文件名称',
//              dataIndex: 'workName',
//              align: "center",
//            },
            {
              title: '文件地址',
              dataIndex: 'sop',
              align: "center",
            },
          ]
        },
        faultReason: {
          loading: false,
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
            }
          ]
        },
        url: {
          add: "/alarmType/faultTaskInfo/add",
          edit: "/alarmType/faultTaskInfo/edit",
          queryById: "/alarmType/faultTaskInfo/queryById",
          queryFaultRepairInfoByMainId: "/alarmType/faultTaskInfo/queryFaultRepairInfoByMainId",
          queryFaultReasonInfoListByMainId: "/alarmType/faultTaskInfo/queryFaultReasonInfoListByMainId",
          queryFaultSopInfoByMainId: "/alarmType/faultTaskInfo/queryFaultSopInfoByMainId",
        }
      }
    },
    created () {
    },
    computed: {
//      whereDictCode: function() {
//        return "O_SHIFT,SHIFT_NAME,SHIFT_ID,equip_categ='"+this.parentId+"'";
//      },
    },
    methods: {
      initDictConfig(){
      },
      add (recondTree,recondTable) {
        console.log("recondTable-----",recondTable)
        this.typeName = recondTree.title
        this.faultType = recondTree.equipType;
        this.equipId = recondTree.equipId;
        this.parentId = recondTree.parentId;
        let str="O_SHIFT,SHIFT_NAME,SHIFT_ID,equip_categ='"+this.parentId+"'";
        initDictOptionsOra(str).then(res => {
          this.lineTeamList = res.result
          if(this._.has(recondTable,'lineTeam')){
            this.editcheckeds=this._.split(recondTable.lineTeam,',');
            for(var i=0;i<this.editcheckeds.length;i++){
              var value=this.editcheckeds[i];
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
        this.edit(recondTable);
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'faultName','faultNameId', 'faultType', 'lineTeam', 'faultLabe', 'isStatus','systemName'))
        });
        getAction(this.url.queryFaultRepairInfoByMainId,{id:record.id}).then((res)=>{
          if(res.success){
            this.faultRepairInfoTable.dataSource=res.result;
          }
        });
        getAction(this.url.queryFaultSopInfoByMainId,{id:record.id}).then((res)=>{
          if(res.success){
            this.workTable.dataSource=res.result;
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
        this.lineTeamList=[];
        this.faultRepairInfoTable.dataSource=[];
        this.checkedList=[];
      },
      handleOk () {
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row, 'faultName', 'faultType', 'lineTeam', 'faultLabe', 'isStatus'))
      },
      /** ATab 选项卡切换事件 */
      handleChangeTabs(key) {
        console.log("key-----", key)
      },
      inputTextShiftId(val) {
        this.form.setFieldsValue({shiftName: val})
      },
      //实现分页效果
      getShowSource(pagination) {
        pagination= this._.cloneDeep(pagination);
        this.faultRepairInfoTable.ipagination.current=pagination.current;

      },
      //实现分页效果
      getWorkTableShowSource(pagination) {
        pagination= this._.cloneDeep(pagination);
        this.workTable.ipagination.current=pagination.current;
      },
    }
  }
</script>