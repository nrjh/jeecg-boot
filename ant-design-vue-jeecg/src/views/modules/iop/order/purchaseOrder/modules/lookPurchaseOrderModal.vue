<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :maskClosable="false"
      :confirmLoading="confirmLoading"
      :footer="null"
      @ok="handleOk"
      @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row>
            <a-col :span="8">
              <a-form-item label="采购定单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'pickingNo', validatorRules.pickingNo]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="到货日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-date v-decorator="[ 'datePlanned', validatorRules.datePlanned]" placeholder="请输入预计到货时间" disabled></j-date>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="采购员："  :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag type="list"
                                   placeholder="请选择接收人"
                                   v-decorator="['createName', validatorRules.createName]"
                                   :trigger-change="true"
                                   dict-code="sys_user,realname,id"
                                   disabled/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="24">
              <a-form-item label="采购单号" :labelCol="{span:3}" :wrapperCol="{span:21}">
                <a-input v-decorator="[ 'orign', validatorRules.orign]" placeholder="请输入编号" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8">
              <a-form-item label="合同号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'contract', validatorRules.contract]" placeholder="请输入合同号" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="下单日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-date v-decorator="[ 'dateOrder', validatorRules.dateOrder]" placeholder="请输入下单" disabled></j-date>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="单据类型" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-radio-group v-decorator="['orderType',validatorRules.orderType]" disabled>
                  <a-radio value="normal">
                    正常单
                  </a-radio>
                  <a-radio value="urgency">
                    紧急单
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

        <!-- 这里加上添加物料弹出框的自定义表单-->
        <a-table
          ref="editableTable"
          size="middle"
          bordered
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd"
        >
          <template slot="vendorCode" slot-scope="text,record,index">
            <j-dict-select-tag-iop type="list"  v-model="dataSourceProd[index].partnerId"
                                   :trigger="true" dictCode="prd_brand,manufactor_name,id,is_del='0'" placeholder="请选择供应商"
            />
          </template>
        </a-table>

      </a-spin>
    </a-modal>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import PcsOrderLineForm from './PcsOrderLineForm.vue'
  import JDate from "../../../../../../components/jeecg/JDate";
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import addProdModal from '../../orderplan/modules/addProdModal.vue'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { httpAction , getAction } from '@/api/manage'

  export default {
    name: 'LookPurchaseOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate, PcsOrderLineForm,JDictSelectTag,addProdModal,JDictSelectTagIop
    },
    data() {
      return {
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        labelCol2: {
          span: 3
        },
        wrapperCol2: {
          span: 20
        },
        visibleProd:false,
        dataSourceProd:[],
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          vendorId: {rules: [

          ]},
          datePlanned: {rules: [
            {required: true, message: '请输入预计到货时间!'},
          ]},
          contract: {rules: [
            {required: true, message: '请输入合同号!'},
          ]},
        },
        refKeys: ['pcsOrderLine', ],
        tableKeys:[],
        activeKey: 'pcsOrderLine',
        // 采购明细
        pcsOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
          ]
        },
        columns: [
          // {
          //   title:'物料id',
          //   align:"center",
          //   dataIndex: 'productId',
          // },
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
            title:'物料名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'物料编号',
            align:"center",
            dataIndex: 'productNo'
          },
          {
            title:'类别',
            align:"center",
            dataIndex: 'categoryType',
            customRender:function (text) {
              if(text === 'equip'){
                return "生产设施"
              }else if(text === 'spare'){
                return "备品备件"
              }else {
                return "办公用品"
              }
            }
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'acName',
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'uuName',
          },
          {
            title:'供应商',
            align:"center",
            key: 'vendorId',
            width: '12%',
            scopedSlots: { customRender: 'vendorCode' }
          },
          {
            title:'采购数量',
            align:"center",
            dataIndex:'productPlyQty',
          },
          {
            title:'单价',
            align:"center",
            dataIndex:'priceUnit',
          },
          {
            title:'金额',
            align:"center",
            dataIndex:'price',
          },
          {
            title:'备注',
            align:"center",
            dataIndex:'remark',
          }
        ],
        url: {
          add: "/order/pcsPurchaseOrder/add",
          edit: "/order/pcsPurchaseOrder/edit",
          queryById: "/order/pcsPurchaseOrder/queryById",
          queryPcsOrderLineByMainId: "/order/pcsPurchaseOrder/queryPcsOrderLineByMainId",
        }
      }
    },
    methods: {
      handleCancel(){
        this.close();
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.dataSourceProd=[];
        this.$emit('onClear');
      },
      editOrder (v) {
        console.log("v------",v)
        this.form.resetFields();
        getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'orign','pickingNo','datePlanned','contract','orderType','dateOrder','createName'))
            })
          }
        });
        getAction(this.url.queryPcsOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
              console.log("-----",res.result)
            this.dataSourceProd=res.result;
          }
        });
      },
    }
  }
</script>
<style scoped>
</style>