<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="所属货位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag-iop v-decorator="[ 'storeId', validatorRules.storeId]" :triggerChange="true"
                                 placeholder="请选择所属货位"
                                 dictCode="iop_o_reserve_store,store_name,id,store_level=4"/>
        </a-form-item>
        <a-form-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletNo', validatorRules.palletNo]" placeholder="请输入编号"></a-input>
        </a-form-item>
        <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'palletOrder', validatorRules.palletOrder]" placeholder="请输入序号"
                          style="width: 100%"/>
        </a-form-item>
        <a-form-item label="尺寸" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletSize', validatorRules.palletSize]" placeholder="请输入尺寸"></a-input>
        </a-form-item>
        <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletColor', validatorRules.palletColor]" placeholder="请输入颜色"></a-input>
        </a-form-item>
        <a-form-item label="制作材质" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'palletMaterial', validatorRules.palletMaterial]" placeholder="请输入制作材质"></a-input>
        </a-form-item>
        <a-form-item label="是否虚拟" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="是" unCheckedChildren="否" v-model="storeIsvirtual"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue, iopValidateDuplicateValue } from '@/utils/util'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'

  export default {
    name: 'IopOReservePalletModal',
    components: {
      JDictSelectTagIop
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
        storeIsvirtual: false, //是否虚拟
        validatorRules: {
          storeId: {
            rules: [{ required: true, message: '请输入所属货位' }]
          },
          palletNo: {
            rules: [
              { required: true, message: '请输入托盘编号' },
              { validator: (rule, value, callback) => iopValidateDuplicateValue('iop_o_reserve_pallet', 'pallet_no', value, this.model.id, callback) }
            ]
          },
          palletOrder: {
            rules: [{ required: true, message: '请输入序号，用于排序' }]
          },
          palletSize: {
            rules: [{ required: true, message: '请输入尺寸 如600*600' }]
          },
          palletColor: {
            rules: [{ required: true, message: '请输入颜色' }]
          },
          palletMaterial: {
            rules: [{ required: true, message: '请输入材质' }]
          }
        },
        url: {
          add: '/iop/reserve/iopOReservePallet/add',
          edit: '/iop/reserve/iopOReservePallet/edit'
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        if (record.storeIsvirtual == 1) {
          this.storeIsvirtual = true
        } else {
          this.storeIsvirtual = false // 升级兼容 如果没有（后台没有传过来、或者是新建）默认为false
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'palletSize', 'palletColor', 'palletMaterial', 'palletNo', 'palletOrder', 'storeId', 'storeIsvirtual'))
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
            this.model.storeIsvirtual = this.storeIsvirtual == true ? 1 : 0  //将true改为1，false改为0
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
        this.form.setFieldsValue(pick(row, 'palletSize', 'palletColor', 'palletMaterial', 'palletNo', 'palletOrder', 'storeId', 'storeIsvirtual'))
      },
      onChange(checked) {
        console.log(`a-switch to ${checked}`)
        if (checked) {
          validatorRules.storeIsvirtual = 1
        } else {
          validatorRules.storeIsvirtual = 0
        }
      }
    }
  }
</script>