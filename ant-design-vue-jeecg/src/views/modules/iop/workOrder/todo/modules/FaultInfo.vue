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
        <a-form :form="form">
          <a-form-item label="报警类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="typeName" disabled></a-input>
            <a-input v-model="faultType" hidden="hidden"></a-input>
          </a-form-item>
          <a-form-item label="报警名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'faultName', validatorRules.faultName]" placeholder="请输入报警名称" disabled></a-input>
          </a-form-item>
          <a-form-item label="转义代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'faultCode', validatorRules.faultCode]" placeholder="" disabled></a-input>
          </a-form-item>
          <a-form-item label="故障运维班组" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
              <!--<a-checkbox-group @change="lineTeamChange" style="width: 100%" v-model="checkedList" :options="lineTeamList">-->
              <a-row>
                <a-col :span="24" v-for="(i,cindex) in checkedList" :key="cindex">
                  <a-checkbox :value="i.value"  checked>{{ i.text }}</a-checkbox>
                </a-col>
              </a-row>
              <!--</a-checkbox-group>-->
            </a-col>
          </a-form-item>
          <a-form-item label="故障类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <JDictSelectTag v-decorator="['faultLabe', validatorRules.faultLabe]" :trigger-change="true" disabled
                            dictCode="FAULT_LABE" placeholder="请选择故障类型"/>
          </a-form-item>
          <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-radio-group v-decorator="['isStatus', validatorRules.isStatus]" disabled>
              <a-radio value="1">
                启用

              </a-radio>
              <a-radio value="2">
                禁用

              </a-radio>
            </a-radio-group>
          </a-form-item>

        </a-form>
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
  import { initDictOptionsOra } from '@/components/dict/JDictSelectUtil'

  export default {
    name: "FaultInfo",
    components: {
      JDictSelectTag,
      JMultiSelectTag,
      JDictSelectTagOra,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title: "查看",
        width: 800,
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
          faultCode: {
            rules: []
          },
        },
        refKeys: ['faultRepairInfo', 'faultSopInfo2',],
        tableKeys: ['faultRepairInfo', 'faultSopInfo2',],
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
        url: {
          add: "/alarmType/faultTaskInfo/add",
          edit: "/alarmType/faultTaskInfo/edit",
          queryById: "/alarmType/faultTaskInfo/queryById",
          queryFaultRepairInfoByMainId: "/alarmType/faultTaskInfo/queryFaultRepairInfoByMainId",
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
      add () {

        this.edit();
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'faultName', 'faultType', 'lineTeam', 'faultLabe', 'isStatus','faultCode'))
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