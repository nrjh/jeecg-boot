<template>
  <a-modal
    :title="title"
    :width="1000"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row>
          <a-col :span="12">
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'code', validatorRules.code]" placeholder="请输入代码" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'address', validatorRules.address]" placeholder="请输入地址"
                       disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-decorator="['active', validatorRules.active]"
                        disabled="disabled"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-tabs type="card" v-model="activeKey">
            <a-tab-pane tab="相关位置" key="tab1">
              <a-form-item label="视图位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'viewLocationId', validatorRules.viewLocationId]"
                                   :triggerChange="true"
                                   placeholder="请选择视图位置"
                                   dictCode="stk_location,complete_name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'stockLocationId', validatorRules.stockLocationId]"
                                   :triggerChange="true"
                                   placeholder="请选择库存位置"
                                   dictCode="stk_location,complete_name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="入库区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'whInputStockLotId', validatorRules.whInputStockLotId]"
                                   :triggerChange="true"
                                   placeholder="请选择入库区库存位置"
                                   dictCode="stk_location,complete_name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="质检区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'whQcStockLocId', validatorRules.whQcStockLocId]"
                                   :triggerChange="true"
                                   placeholder="请选择质检区库存位置"
                                   dictCode="stk_location,complete_name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="出库区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'whOutputStockLocId', validatorRules.whOutputStockLocId]"
                                   :triggerChange="true"
                                   placeholder="请选择出库区库存位置"
                                   dictCode="stk_location,complete_name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="打包区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'whPackStockLocId', validatorRules.whPackStockLocId]"
                                   :triggerChange="true"
                                   placeholder="请选择打包区库存位置"
                                   dictCode="stk_location,complete_name,id" disabled="disabled"/>
              </a-form-item>
            </a-tab-pane>
            <a-tab-pane tab="相关作业类型" key="tab2">
              <a-form-item label="入库作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'inTypeId', validatorRules.inTypeId]"
                                   :triggerChange="true"
                                   placeholder="请选择入库作业类型"
                                   dictCode="stk_picking_type,name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="出库作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: none">
                <JDictSelectTagIop v-decorator="[ 'outTypeId', validatorRules.outTypeId]"
                                   :triggerChange="true"
                                   placeholder="请选择出库作业类型"
                                   dictCode="stk_picking_type,name,id" disabled="disabled"/>
              </a-form-item>
              <a-form-item label="内部调拨作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="[ 'intTypeId', validatorRules.intTypeId]"
                                   :triggerChange="true"
                                   placeholder="请选择内部调拨作业类型"
                                   dictCode="stk_picking_type,name,id" disabled="disabled"/>
              </a-form-item>
            </a-tab-pane>
          </a-tabs>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'

  export default {
    name: 'StkWarehouseModal',
    components: { JDictSelectTagIop },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        activeKey:"tab1",
        confirmLoading: false,
        validatorRules: {
          name: {
            rules: [
              { required: true, message: '请输入名称!' }
            ]
          },
          code: {
            rules: [
              { required: true, message: '请输入代码!' }
            ]
          },
          address: {
            rules: [
              { required: true, message: '请输入地址!' }
            ]
          },
          active: {
            valuePropName: 'checked',
            rules: [
              { required: true, message: '请选择启用状态!' }
            ]
          }
        },
        url: {
          add: '/iop/stk/warehouse/add',
          edit: '/iop/stk/warehouse/edit'
        }
      }
    },
    created() {
    },
    methods: {
      handleOk() {
        this.close()
        this.activeKey="tab1"
      },
      /** 当点击了查看按钮时调用此方法 */
      detail(record) {
        if (typeof this.editBefore === 'function') this.editBefore(record)
        this.visible = true
        this.form.resetFields()
        record.active = record.active == 1 ? true : false
        this.model = Object.assign({}, record)
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'address', 'active', 'code', 'name', 'partnerId', 'viewLocationId', 'stockLocationId', 'receptionSteps', 'deliverySteps', 'whInputStockLotId', 'whQcStockLocId', 'whOutputStockLocId', 'whPackStockLocId', 'pickTypeId', 'packTypeId', 'inTypeId', 'outTypeId', 'intTypeId', 'companyId'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
        this.activeKey="tab1"
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'address', 'active', 'code', 'name', 'partnerId', 'viewLocationId', 'stockLocationId', 'receptionSteps', 'deliverySteps', 'whInputStockLotId', 'whQcStockLocId', 'whOutputStockLocId', 'whPackStockLocId', 'pickTypeId', 'packTypeId', 'inTypeId', 'outTypeId', 'intTypeId', 'companyId'))
      }


    }
  }
</script>