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
            <a-form-item label="属性名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入属性名称"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="显示类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['displayType', validatorRules.displayType]"
                                 :trigger-change="true" dictCode="IOP_PRD_DISPLAY_TYPE"
                                 placeholder="请选择显示类型"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'sequence', validatorRules.sequence]" placeholder="请输入序号"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="属性值" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="prdAttributeValueTable.loading"
            :columns="prdAttributeValueTable.columns"
            :dataSource="prdAttributeValueTable.dataSource"
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
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import { httpAction, getAction } from '@/api/manage'

  export default {
    name: 'PrdAttributeModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag
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
              { required: true, message: '请输入名称!' },
              { validator: (rule, value, callback) => validateDuplicateValue('prd_attribute', 'name', value, this.model.id, callback) }
            ]
          },
          sequence: {
            rules: [
              { required: true, message: '请输入序号!' },
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
            ]
          },
          displayType: {
            rules: [
              { required: true, message: '请输入显示类型!' }
            ]
          }
        },
        refKeys: ['prdAttributeValue'],
        tableKeys: ['prdAttributeValue'],
        activeKey: 'prdAttributeValue',
        // 属性值
        prdAttributeValueTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '名称',
              key: 'name',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '序号',
              key: 'sequence',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }, {
                pattern: /^-?\d+\.?\d*$/,
                message: '请输入数字!'
              }]
            }
          ]
        },
        url: {
          add: '/iop/prd/attribute/add',
          edit: '/iop/prd/attribute/edit',
          prdAttributeValue: {
            list: '/iop/prd/attribute/queryPrdAttributeValueByMainId'
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
        let fieldval = pick(this.model, 'name', 'sequence', 'displayType')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.prdAttributeValue.list, params, this.prdAttributeValueTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          prdAttributeValueList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'name', 'sequence', 'displayType'))
      },
      // 重写
      request(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        // 清楚子表ID
        for (var i = 0; i < formData.prdAttributeValueList.length; i++) {
          formData.prdAttributeValueList[i].id = '';
        }

        this.confirmLoading = true
        httpAction(url, formData, method).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok')
            this.close()
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      }

    }
  }
</script>

<style scoped>
</style>