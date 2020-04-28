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
            <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'code', validatorRules.code]" placeholder="请输入代码"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'address', validatorRules.address]" placeholder="请输入地址"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-decorator="['active', validatorRules.active]"/>
          </a-form-item>
        </a-col>

        <!-- 查看时才显示 -->
        <!--
        <a-form-item label="视图位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'viewLocationId', validatorRules.viewLocationId]" placeholder="请输入视图位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'stockLocationId', validatorRules.stockLocationId]" placeholder="请输入库存位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="入库步骤" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'receptionSteps', validatorRules.receptionSteps]" placeholder="请输入入库步骤" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="出库步骤 " :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'deliverySteps', validatorRules.deliverySteps]" placeholder="请输入出库步骤 " style="width: 100%"/>
        </a-form-item>
        <a-form-item label="入库区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'whInputStockLotId', validatorRules.whInputStockLotId]" placeholder="请输入入库区库存位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="质检区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'whQcStockLocId', validatorRules.whQcStockLocId]" placeholder="请输入质检区库存位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="出库区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'whOutputStockLocId', validatorRules.whOutputStockLocId]" placeholder="请输入出库区库存位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="打包区库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'whPackStockLocId', validatorRules.whPackStockLocId]" placeholder="请输入打包区库存位置" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="分拣作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'pickTypeId', validatorRules.pickTypeId]" placeholder="请输入分拣作业类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="打包作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'packTypeId', validatorRules.packTypeId]" placeholder="请输入打包作业类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="入库作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'inTypeId', validatorRules.inTypeId]" placeholder="请输入入库作业类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="出库作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'outTypeId', validatorRules.outTypeId]" placeholder="请输入出库作业类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="内部调拨作业类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'intTypeId', validatorRules.intTypeId]" placeholder="请输入内部调拨作业类型" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'companyId', validatorRules.companyId]" placeholder="请输入单位" style="width: 100%"/>
        </a-form-item>
        -->
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'StkWarehouseModal',
    components: {},
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
      add() {
        this.edit({active:1})
      },
      edit(record) {
        this.form.resetFields()
        record.active = record.active == 1 ? true : false
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'address', 'active', 'code', 'name', 'partnerId', 'viewLocationId', 'stockLocationId', 'receptionSteps', 'deliverySteps', 'whInputStockLotId', 'whQcStockLocId', 'whOutputStockLocId', 'whPackStockLocId', 'pickTypeId', 'packTypeId', 'inTypeId', 'outTypeId', 'intTypeId', 'companyId'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            values.active = values.active == true ? 1 : 0  //将true改为1，false改为0
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }

        })
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