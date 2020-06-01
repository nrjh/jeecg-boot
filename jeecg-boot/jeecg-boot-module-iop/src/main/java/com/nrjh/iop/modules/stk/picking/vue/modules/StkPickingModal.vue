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
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="源单据" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'orign', validatorRules.orign]" placeholder="请输入源单据"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['pickingTypeId', validatorRules.pickingTypeId]" :trigger-change="true" dictCode="stk_picking_type,name,id" placeholder="请选择作业类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="优先级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['priority', validatorRules.priority]" :trigger-change="true" dictCode="" placeholder="请选择优先级"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="移动类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['moveType', validatorRules.moveType]" :trigger-change="true" dictCode="IOP_STK_MOVE_TYPE" placeholder="请选择移动类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="计划执行时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择计划执行时间" v-decorator="[ 'scheduledDate', validatorRules.scheduledDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="实际完成时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择实际完成时间" v-decorator="[ 'dateDone', validatorRules.dateDone]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="伙伴" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'partnerId', validatorRules.partnerId]" placeholder="请输入伙伴" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="已打印" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'printed', validatorRules.printed]" placeholder="请输入已打印" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="被锁定" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'izLocked', validatorRules.izLocked]" placeholder="请输入被锁定" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="立即转移 " :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'immediateTranster', validatorRules.immediateTranster]" placeholder="请输入立即转移 " style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['state', validatorRules.state]" :trigger-change="true" dictCode="IOP_STK_PICKING_STATE" placeholder="请选择状态"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="欠单" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'backorderId', validatorRules.backorderId]" placeholder="请输入欠单" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="源位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag v-decorator="['locationSrcId', validatorRules.locationSrcId]" dict="" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="目标位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag v-decorator="['locationDestId', validatorRules.locationDestId]" dict="" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'userId', validatorRules.userId]" placeholder="请输入负责人" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['note', validatorRules.note]" rows="4" placeholder="请输入备注"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['companyId', validatorRules.companyId]" :trigger-change="true" dictCode="" placeholder="请选择单位"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="移库单" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="stkMoveTable.loading"
            :columns="stkMoveTable.columns"
            :dataSource="stkMoveTable.dataSource"
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
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

  export default {
    name: 'StkPickingModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
      JDictSelectTag,
      JSearchSelectTag,
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
          name: {rules: [
            {required: true, message: '请输入名称!'},
          ]},
          orign: {rules: [
          ]},
          pickingTypeId: {rules: [
            {required: true, message: '请输入作业类型!'},
          ]},
          priority: {rules: [
            {required: true, message: '请输入优先级!'},
          ]},
          moveType: {rules: [
            {required: true, message: '请输入移动类型!'},
          ]},
          scheduledDate: {rules: [
          ]},
          dateDone: {rules: [
          ]},
          partnerId: {rules: [
          ]},
          printed: {rules: [
            {required: true, message: '请输入已打印!'},
          ]},
          izLocked: {rules: [
            {required: true, message: '请输入被锁定!'},
          ]},
          immediateTranster: {rules: [
            {required: true, message: '请输入立即转移 !'},
          ]},
          state: {rules: [
            {required: true, message: '请输入状态!'},
          ]},
          backorderId: {rules: [
          ]},
          locationSrcId: {rules: [
            {required: true, message: '请输入源位置!'},
          ]},
          locationDestId: {rules: [
            {required: true, message: '请输入目标位置!'},
          ]},
          userId: {rules: [
            {required: true, message: '请输入负责人!'},
          ]},
          note: {rules: [
          ]},
          companyId: {rules: [
          ]},
        },
        refKeys: ['stkMove', ],
        tableKeys:['stkMove', ],
        activeKey: 'stkMove',
        // 移库单
        stkMoveTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '名称',
              key: 'name',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '物品',
              key: 'productId',
              type: FormTypes.sel_search,
              dictCode:"PRD_PRODUCT,name,id",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '物品计量单位',
              key: 'productUomId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '批次/序列号',
              key: 'productLotId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '实际物品数量',
              key: 'productQty',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '需求物品数量',
              key: 'productUomQty',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '状态',
              key: 'state',
              type: FormTypes.select,
              dictCode:"",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '源单据',
              key: 'origin',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '作业类型',
              key: 'pickingTypeId',
              type: FormTypes.list_multi,
              dictCode:"",
              width:"250px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '调拨单',
              key: 'pickingId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '盘点单',
              key: 'inventoryId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '是否退回移库',
              key: 'toRefund',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '关联源移库单',
              key: 'originReturnedMoveId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '关联单据说明',
              key: 'reference',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '仓库',
              key: 'warehouseId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '源位置',
              key: 'locationSrcId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '目标位置',
              key: 'locationDestId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '实际执行时间',
              key: 'dateDone',
              type: FormTypes.datetime,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '预计执行时间',
              key: 'dateExpected',
              type: FormTypes.datetime,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '说明',
              key: 'note',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '单位',
              key: 'companyId',
              type: FormTypes.sel_search,
              dictCode:"",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/stk.picking/stkPicking/add",
          edit: "/stk.picking/stkPicking/edit",
          stkMove: {
            list: '/stk.picking/stkPicking/queryStkMoveByMainId'
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
        let fieldval = pick(this.model,'name','orign','pickingTypeId','priority','moveType','scheduledDate','dateDone','partnerId','printed','izLocked','immediateTranster','state','backorderId','locationSrcId','locationDestId','userId','note','companyId')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.stkMove.list, params, this.stkMoveTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          stkMoveList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'name','orign','pickingTypeId','priority','moveType','scheduledDate','dateDone','partnerId','printed','izLocked','immediateTranster','state','backorderId','locationSrcId','locationDestId','userId','note','companyId'))
     },

    }
  }
</script>

<style scoped>
</style>