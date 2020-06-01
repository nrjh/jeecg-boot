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
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop type="list" v-decorator="['pickingTypeId', validatorRules.pickingTypeId]" :trigger-change="true"
                                     dictCode="stk_picking_type,name,id" placeholder="请选择作业类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="物品" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'productId', validatorRules.productId]" :triggerChange="true"
                                     placeholder="请选择物品"  dictCode="PRD_PRODUCT,NAME,ID" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'productUomId', validatorRules.productUomId]" :triggerChange="true"
                                     placeholder="请选择计量单位"  dictCode="UOM_UOM,NAME,ID" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="批次/序列号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'productLotId', validatorRules.productLotId]" placeholder="请输入批次/序列号"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="实际物品数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'productQty', validatorRules.productQty]" placeholder="请输入实际物品数量"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="需求物品数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'productUomQty', validatorRules.productUomQty]" placeholder="请输入需求物品数量"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['state', validatorRules.state]" :trigger-change="true"
                                 dictCode="IOP_STK_MOVE_STATE" placeholder="请选择状态"/>
            </a-form-item>
          </a-col>
          <!--
          <a-col :span="12">
            <a-form-item label="源单据" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'origin', validatorRules.origin]" placeholder="请输入源单据"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="调拨单" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'pickingId', validatorRules.pickingId]" placeholder="请输入调拨单"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="盘点单" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'inventoryId', validatorRules.inventoryId]" placeholder="请输入盘点单"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="是否退回移库" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'toRefund', validatorRules.toRefund]" placeholder="请输入是否退回移库"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="关联源移库单" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'originReturnedMoveId', validatorRules.originReturnedMoveId]"
                              placeholder="请输入关联源移库单" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="关联单据说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'reference', validatorRules.reference]" placeholder="请输入关联单据说明"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag v-decorator="['companyId', validatorRules.companyId]" dict=""/>
            </a-form-item>
          </a-col>
          -->
          <a-col :span="12">
            <a-form-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'warehouseId', validatorRules.warehouseId]" :triggerChange="true"
                                     placeholder="请选择所属仓库"  dictCode="stk_warehouse,NAME,ID" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="源位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'locationSrcId', validatorRules.locationSrcId]" :triggerChange="true"
                                     placeholder="请选择源位置"  dictCode="stk_location,complete_name,ID" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="目标位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'locationDestId', validatorRules.locationDestId]" :triggerChange="true"
              placeholder="请选择目标位置"  dictCode="stk_location,complete_name,ID" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="实际执行时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择实际执行时间" v-decorator="[ 'dateDone', validatorRules.dateDone]"
                      :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="预计执行时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择预计执行时间" v-decorator="[ 'dateExpected', validatorRules.dateExpected]"
                      :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="说明" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['note', validatorRules.note]" rows="4" placeholder="请输入说明"/>
            </a-form-item>
          </a-col>


        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="库存移动明细" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="stkMoveLineTable.loading"
            :columns="stkMoveLineTable.columns"
            :dataSource="stkMoveLineTable.dataSource"
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
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'


  export default {
    name: 'StkMoveModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
      JDictSelectTagIop,
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
          name: {
            rules: [
              { required: true, message: '请输入名称!' }
            ]
          },
          productId: {
            rules: [
              { required: true, message: '请输入物品!' }
            ]
          },
          productUomId: {
            rules: [
              { required: true, message: '请输入物品计量单位!' }
            ]
          },
          productLotId: {
            rules: []
          },
          productQty: {
            rules: [
              { required: true, message: '请输入实际物品数量!' },
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
            ]
          },
          productUomQty: {
            rules: [
              { required: true, message: '请输入需求物品数量!' },
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
            ]
          },
          state: {
            rules: [
              { required: true, message: '请输入状态!' }
            ]
          },
          origin: {
            rules: []
          },
          pickingTypeId: {
            rules: [
              { required: true, message: '请输入作业类型!' }
            ]
          },
          pickingId: {
            rules: []
          },
          inventoryId: {
            rules: []
          },
          toRefund: {
            rules: [
              { required: true, message: '请输入是否退回移库!' }
            ]
          },
          originReturnedMoveId: {
            rules: []
          },
          reference: {
            rules: []
          },
          warehouseId: {
            rules: [
              { required: true, message: '请输入仓库!' }
            ]
          },
          locationSrcId: {
            rules: [
              { required: true, message: '请输入源位置!' }
            ]
          },
          locationDestId: {
            rules: [
              { required: true, message: '请输入目标位置!' }
            ]
          },
          dateDone: {
            rules: []
          },
          dateExpected: {
            rules: []
          },
          note: {
            rules: []
          },
          companyId: {
            rules: []
          }
        },
        refKeys: ['stkMoveLine'],
        tableKeys: ['stkMoveLine'],
        activeKey: 'stkMoveLine',
        // 库存移动明细
        stkMoveLineTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '调拨单',
              key: 'pickingId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '移库单',
              key: 'moveId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '物品',
              key: 'productId',
              type: FormTypes.sel_search,
              dictCode: '',
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '物品计量单位',
              key: 'productUomId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '实际物品数量',
              key: 'productQty',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '已保留物品数量',
              key: 'productUomQty',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '完成物品数量',
              key: 'productDoneQty',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '批次/序列号',
              key: 'productLotId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '批次/序列名称',
              key: 'productLotName',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '实际执行时间',
              key: 'dateDone',
              type: FormTypes.datetime,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '状态',
              key: 'state',
              type: FormTypes.select,
              dictCode: '',
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '关联单据说明',
              key: 'reference',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '源位置',
              key: 'locationSrcId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '目标位置',
              key: 'locationDestId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            },
            {
              title: '单位',
              key: 'companyId',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: ''
            }
          ]
        },
        url: {
          add: '/iop/stk/move/add',
          edit: '/iop/stk/move/edit',
          stkMoveLine: {
            list: '/iop/stk/move/queryStkMoveLineByMainId'
          }
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
        let fieldval = pick(this.model, 'name', 'productId', 'productUomId', 'productLotId', 'productQty', 'productUomQty', 'state', 'origin', 'pickingTypeId', 'pickingId', 'inventoryId', 'toRefund', 'originReturnedMoveId', 'reference', 'warehouseId', 'locationSrcId', 'locationDestId', 'dateDone', 'dateExpected', 'note', 'companyId')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.stkMoveLine.list, params, this.stkMoveLineTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          stkMoveLineList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'name', 'productId', 'productUomId', 'productLotId', 'productQty', 'productUomQty', 'state', 'origin', 'pickingTypeId', 'pickingId', 'inventoryId', 'toRefund', 'originReturnedMoveId', 'reference', 'warehouseId', 'locationSrcId', 'locationDestId', 'dateDone', 'dateExpected', 'note', 'companyId'))
      }

    }
  }
</script>

<style scoped>
</style>