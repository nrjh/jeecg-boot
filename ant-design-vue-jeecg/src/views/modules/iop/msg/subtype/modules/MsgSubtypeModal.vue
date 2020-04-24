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

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-textarea v-decorator="['description', validatorRules.description]" rows="4" placeholder="请输入描述"/>
        </a-form-item>

        <a-form-item label="父级" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select-iop
            ref="treeSelect"
            placeholder="请选择父级"
            v-decorator="['pid', validatorRules.pid]"
            dict="msg_subtype,name,id"
            pidField="pid"
            pidValue="0"
            hasChildField="has_child">
          </j-tree-select-iop>
        </a-form-item>
        <a-form-item label="顺序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'sequence', validatorRules.sequence]" placeholder="请输入顺序" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="仅内部" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="是" unCheckedChildren="否" v-decorator="['internal', validatorRules.internal]"/>
        </a-form-item>
        <a-form-item label="默认" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="是" unCheckedChildren="否" v-decorator="['izDefault', validatorRules.izDefault]"/>
        </a-form-item>
        <a-form-item label="隐藏" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="是" unCheckedChildren="否" v-decorator="['hidden', validatorRules.hidden]"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JTreeSelectIop from '@/components/jeecg/JTreeSelectIop'

  export default {
    name: 'MsgSubtypeModal',
    components: {
      JDictSelectTag,
      JTreeSelectIop
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
          name: {
            rules: [
              { required: true, message: '请输入名称!' },
              { validator: (rule, value, callback) => validateDuplicateValue('msg_subtype', 'name', value, this.model.id, callback) }
            ]
          },
          description: {
            rules: []
          },
          pid: { rules: [] },
          sequence: {
            rules: [
              { required: true, message: '请输入顺序!' },
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
            ]
          },
          internal: {
            valuePropName: 'checked', rules: [
              { required: true, message: '请输入仅内部!' }
            ]
          },

          izDefault: {
            valuePropName: 'checked', rules: [
              { required: true, message: '请输入默认!' }
            ]
          },
          hidden: {
            valuePropName: 'checked', rules: [
              { required: true, message: '请输入隐藏!' }
            ]
          }
        },
        url: {
          add: '/iop/msg/subtype/add',
          edit: '/iop/msg/subtype/edit'
        },
        expandedRowKeys: [],
        pidField: 'pid'

      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({internal:1,izDefault:1})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'name', 'description', 'internal', 'pid', 'izDefault', 'sequence', 'hidden'))
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
        this.form.setFieldsValue(pick(row, 'name', 'description', 'internal', 'pid', 'izDefault', 'sequence', 'hidden'))
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