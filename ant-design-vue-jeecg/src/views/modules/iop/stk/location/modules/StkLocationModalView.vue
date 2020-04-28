<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="父位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select-iop
            ref="treeSelect"
            placeholder="请选择父位置"
            v-decorator="['pid', validatorRules.pid]"
            dict="stk_location,name,id"
            pidField="pid"
            pidValue="1"
            hasChildField="has_child" disabled="disabled">
          </j-tree-select-iop>
        </a-form-item>
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称" disabled="disabled"></a-input>
        </a-form-item>
        <a-form-item label="位置类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <JDictSelectTag v-decorator="[ 'usageType', validatorRules.usageType]" :triggerChange="true"
                          placeholder="请选择位置类型"
                          dictCode="IOP_STK_USAGE_TYPE" disabled="disabled"/>
        </a-form-item>
        <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-decorator="['active', validatorRules.active]"
                    disabled="disabled"/>
        </a-form-item>
        <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['comment', validatorRules.comment]" rows="4" placeholder="请输入说明"
                      disabled="disabled"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JTreeSelectIop from '@/components/jeecg/JTreeSelectIop'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'

  export default {
    name: 'StkLocationModal',
    components: {
      JTreeSelectIop, JDictSelectTag
    },
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

          pid: {
            rules: [
              { required: true, message: '请输入父位置!' }
            ]
          },
          name: {
            rules: [
              { required: true, message: '请输入名称!' }
            ]
          },
          usageType: {
            rules: [
              { required: true, message: '请输入位置类型!' }
            ]
          },
          active: {
            valuePropName: 'checked', rules: [
              { required: true, message: '请输入启用!' }
            ]
          },
          comment: {
            rules: []
          }
        },
        url: {
          add: '/iop/stk/location/add',
          edit: '/iop/stk/location/edit'
        },
        expandedRowKeys: [],
        pidField: 'pid'

      }
    },
    created() {
    },
    methods: {
      handleOk() {
        this.close()
      },
      /** 当点击了查看按钮时调用此方法 */
      detail(record) {
        if (typeof this.editBefore === 'function') this.editBefore(record)
        this.visible = true
        this.form.resetFields()
        record.active = record.active == 1 ? true : false
        this.model = Object.assign({}, record)
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'active', 'pid', 'name', 'usageType', 'comment'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'active', 'pid', 'name', 'usageType', 'comment'))
      },


    }
  }
</script>