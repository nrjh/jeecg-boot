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
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="计量类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['measureType', validatorRules.measureType]"
                                 :trigger-change="true" dictCode="IOP_UOM_MEASURE_TYPE" placeholder="请选择计量类型"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="计量单位" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="uomUomTable.loading"
            :columns="uomUomTable.columns"
            :dataSource="uomUomTable.dataSource"
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
    name: 'UomCategoryModal',
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
              { required: true, message: '请输入名称!' }
            ]
          },
          measureType: {
            rules: [
              { required: true, message: '请输入计量类型!' }
            ]
          }
        },
        refKeys: ['uomUom'],
        tableKeys: ['uomUom'],
        activeKey: 'uomUom',
        // 计量单位
        uomUomTable: {
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
              title: '单位类型',
              key: 'uomType',
              type: FormTypes.select,
              dictCode: 'IOP_UOM_UOM_TYPE',
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '比例',
              key: 'factor',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '精度',
              key: 'rounding',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '状态',
              key: 'active',
              type: FormTypes.list_multi,
              dictCode: 'IOP_PUB_ACTION',
              width: '250px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            }
          ]
        },
        url: {
          add: '/iop/uom/category/add',
          edit: '/iop/uom/category/edit',
          uomUom: {
            list: '/iop/uom/category/queryUomUomByMainId'
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
        let fieldval = pick(this.model, 'name', 'measureType')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.uomUom.list, params, this.uomUomTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          uomUomList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'name', 'measureType'))
      },
      // 重写
      request(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        //属性管理  属性值字表主键
        for (var i = 0; i < formData.uomUomList.length; i++) {
          formData.uomUomList[i].id = '';
          formData.uomUomList[i].active = formData.uomUomList[i].active == true ? 1 : 0;
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