<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>

          <a-col :span="12">
            <a-form-item label="采购入库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'orderInId', validatorRules.orderInId]" placeholder="请输入采购入库单号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'contract', validatorRules.contract]" placeholder="请输入合同单号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['partnerId', validatorRules.partnerId]" :trigger-change="true" dictCode="vendorCode" placeholder="请选择供应商id"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="采购员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createName', validatorRules.createName]" placeholder="请输入采购员"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="采购日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择采购日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单据级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="radio" v-decorator="['orderType', validatorRules.orderType]" :trigger-change="true" dictCode="sex" placeholder="请选择单据级别"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'note', validatorRules.note]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="入库明细" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="stockInOrderLineTable.loading"
            :columns="stockInOrderLineTable.columns"
            :dataSource="stockInOrderLineTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
        
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'StockInOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
      JDictSelectTag,
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
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          orderInId: {rules: [
            {required: true, message: '请输入采购入库单号!'},
          ]},
          contract: {rules: [
            {required: true, message: '请输入合同单号!'},
          ]},
          partnerId: {rules: [
          ]},
          createName: {rules: [
          ]},
          createTime: {rules: [
          ]},
          orderType: {rules: [
          ]},
          note: {rules: [
          ]},
        },
        refKeys: ['stockInOrderLine', ],
        tableKeys:['stockInOrderLine', ],
        activeKey: 'stockInOrderLine',
        // 入库明细
        stockInOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物料名称',
              key: 'productName',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '物品类型',
              key: 'categoryType',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '物料编号',
              key: 'defaultCode',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '规格',
              key: 'attributeCategoryId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '单价',
              key: 'priceUnit',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '应入库数量',
              key: 'productQty',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '实际入库数量',
              key: 'productActualQty',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '单位',
              key: 'productUomId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '总价',
              key: 'priceTotal',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '入货位时间',
              key: 'createTime',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '合同编号',
              key: 'contract',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '标识',
              key: 'remark',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/stk/in/stockInOrder/add",
          edit: "/stk/in/stockInOrder/edit",
          stockInOrderLine: {
            list: '/stk/in/stockInOrder/queryStockInOrderLineByMainId'
          },
        }
      }
    },
    methods: {
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'orderInId','pickingNo','contract','partnerId','partnerName','createName','createTime','orderInDate','orderType','state','note')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.stockInOrderLine.list, params, this.stockInOrderLineTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          stockInOrderLineList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'orderInId','pickingNo','contract','partnerId','partnerName','createName','createTime','orderInDate','orderType','state','note'))
     },

    }
  }
</script>

<style scoped>
</style>