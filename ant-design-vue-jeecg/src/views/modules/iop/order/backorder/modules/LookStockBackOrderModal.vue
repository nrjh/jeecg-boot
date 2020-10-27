<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :maskClosable="false"
      :confirmLoading="confirmLoading"
      :footer="null"
      @cancel="handleCancel"
    >
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row>
            <a-col :span="12">
              <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTag v-decorator="['team', validatorRules.team]"
                                :triggerChange="true"
                                placeholder=""
                                disabled="disabled"
                                dictCode="sys_user,username,id" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="班组" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTag v-decorator="['team', validatorRules.team]"
                                :triggerChange="true"
                                placeholder=""
                                disabled="disabled"
                                dictCode="sys_user,username,id" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTag v-decorator="[ 'orderType', validatorRules.orderType]" :triggerChange="true" disabled="disabled" placeholder="请选择退还类型" dictCode="order_type" ></JDictSelectTag>

              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="领料单号" :labelCol="labelCol" :wrapperCol="wrapperCol">

                <JDictSelectTagIop v-decorator="['orderOutId', validatorRules.orderOutId]"
                                   :triggerChange="true"
                                   placeholder=""
                                   disabled="disabled"
                                   dictCode="stock_out_order,order_out_id,id" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date v-decorator="[ 'createTime', validatorRules.createTime]" placeholder="" disabled="disabled"></j-date>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="说明" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                <a-textarea v-decorator="['note', validatorRules.note]" rows="4" disabled="disabled" placeholder="请输入说明"/>
              </a-form-item>
            </a-col>

          </a-row>
        </a-form>
        <!-- 这里加上添加物料弹出框的自定义表单-->
        <j-editable-table
          ref="editableTable"
          size="middle"
          bordered
          rowKey="id"
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd"
        >
          <span  slot="action" slot-scope="record">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                      <a>删除</a>
                </a-popconfirm>

        </span>
        </j-editable-table>
      </a-spin>
    </a-modal>
    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JSearchSelectTagIop from '@/components/dict/JSearchSelectTagIop'
  import addProdModal from '../../orderplan/modules/addProdModal.vue'
  import JDate from "../../../../../../components/jeecg/JDate";
  import { httpAction , getAction } from '@/api/manage'

  export default {
    name: 'StockBackOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      JSearchSelectTag,
      addProdModal,
      JDate,
      JSearchSelectTagIop,
      JDictSelectTagIop
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
          updateInName: {rules: [
          ]},
          orderBackId: {rules: [
          ]},
          team: {rules: [
          ]},
          orderType: {rules: [
          ]},
          orderOutId: {rules: [
          ]},
          note: {rules: [
          ]},
          createTime: {rules: [
          ]},
        },
        columns: [
          {
            title:'物料id',
            align:"center",
            key: 'productId',
            type: FormTypes.hidden,
          },
          {
            title:'物料名称',
            align:"center",
            key: 'name'
          },
          {
            title:'物料编号',
            align:"center",
            key: 'productNo'
          },
          {
            title:'类别',
            align:"center",
            key: 'categoryId'
          },
          {
            title:'规格',
            align:"center",
            key: 'attributeCategoryId',
          },
          {
            title:'单位',
            align:"center",
            key: 'productUomId',
            type: FormTypes.sel_search,
            dictCode:"UOM_UOM",
            disabled:true,
          },
          {
            title:'入库件数',
            align:"center",
            key:'productUomQty',
            type: FormTypes.inputNumber,
            allowInput: true,
            defaultValue: '0',
            placeholder: '请输入${title}',
            disabled: true
          },
        ],
        refKeys: ['stockBackOrderLine', ],
        tableKeys:[],
        activeKey: 'stockBackOrderLine',
        // 退回入库明细
        stockBackOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
          ]
        },
        url: {
          add: "/backorder/stockBackOrder/add",
          edit: "/backorder/stockBackOrder/edit",
          queryById: "/backorder/stockBackOrder/queryById",
          queryStockBackOrderLineByMainId: "/backorder/stockBackOrder/queryStockBackOrderLineByMainId",
        }
      }
    },
    methods: {
      getAllTable() {
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
      },
      validateError(msg){
      },
      popupCallback(row){
      },
      handleAddProd(){
      },
      returnData(data){
      },
      handleOkCommit(v){
      },
      close(){
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.dataSourceProd=[];
        this.$emit('onClear');
      },
      handleCancel(){
        this.close();
      },
      lookOrder (v) {
        console.log("v------",v);
        this.form.resetFields();
        getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'team','createTime','orderOutId','orderType','note'))
            })
          }
        });
        getAction(this.url.queryStockBackOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.dataSourceProd=res.result;
          }
        });
      },


    }
  }
</script>

<style scoped>
</style>