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
            hasChildField="has_child">
          </j-tree-select-iop>
        </a-form-item>
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="位置类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <JDictSelectTag v-decorator="[ 'usageType', validatorRules.usageType]" :triggerChange="true"
                          placeholder="请选择位置类型"
                          dictCode="IOP_STK_USAGE_TYPE"/>
        </a-form-item>
        <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-decorator="['active', validatorRules.active]"/>
        </a-form-item>
        <a-form-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['comment', validatorRules.comment]" rows="4" placeholder="请输入说明"/>
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
      add() {
        this.edit({ active: 1 })
      },
      edit(record) {
        debugger;
        this.form.resetFields()
        record.active = record.active == 1 ? true : false
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'active', 'pid', 'name', 'usageType', 'comment'))
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
            values.active = values.active == true ? 1 : 0
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
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values)
            let new_pid = this.model[this.pidField]
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.submitSuccess(formData, old_pid == new_pid)
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
        this.form.setFieldsValue(pick(row, 'active', 'pid', 'name', 'usageType', 'comment'))
      },
      submitSuccess(formData, flag) {
        if (!formData.id) {
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys = []
          this.getExpandKeysByPid(formData[this.pidField], treeData, treeData)
          this.$emit('ok', formData, this.expandedRowKeys.reverse())
        } else {
          this.$emit('ok', formData, flag)
        }
      },
      getExpandKeysByPid(pid, arr, all) {
        if (pid && arr && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].key == pid) {
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'], all, all)
            } else {
              this.getExpandKeysByPid(pid, arr[i].children, all)
            }
          }
        }
      }


    }
  }
</script>