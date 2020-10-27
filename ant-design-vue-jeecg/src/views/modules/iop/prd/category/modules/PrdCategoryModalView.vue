<template>
  <a-modal
    :title="title"
    :width="1000"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel" :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">

      <a-form :form="form">
        <a-form-item label="品类名称222" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入品类名称" disabled="disabled"></a-input>
        </a-form-item>
        <a-form-item label="上级品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag-iop placeholder="请选择上级品类" v-decorator="[ 'pid', validatorRules.pid]"
                                 :triggerChange="true"
                                 dictCode="PRD_CATEGORY,COMPLETE_NAME,ID" disabled="disabled">
          </j-dict-select-tag-iop>
        </a-form-item>
        <a-form-item label="品类类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag v-decorator="[ 'categoryType', validatorRules.categoryType]" :triggerChange="true"
                             placeholder="请选择品类类型"
                             dictCode="IOP_PRD_CATEGORY_TYPE" disabled="disabled"/>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-decorator="['active', validatorRules.active]"
                    disabled="disabled"/>
        </a-form-item>
        <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'sequence', validatorRules.sequence]" placeholder="请输入序号"
                          style="width: 100%" disabled="disabled"/>
        </a-form-item>
        <a-form-item label="下架策略" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: none">
          <a-input v-decorator="[ 'removalStrategyId', validatorRules.removalStrategyId]"
                   placeholder="请输入下架策略" disabled="disabled"></a-input>
        </a-form-item>
        <a-form-item label="名称路径" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: none">
          <a-input v-decorator="[ 'completeName', validatorRules.completeName]" placeholder="请输入名称路径"
                   disabled="disabled"></a-input>
        </a-form-item>
      </a-form>
      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="相关分类" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="prdCategoryValueTable.loading"
            :columns="prdCategoryValueTable.columns"
            :dataSource="prdCategoryValueTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="false"
            :actionButton="false"/>
        </a-tab-pane>
        <a-tab-pane tab="相关规格" :key="refKeys[1]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="prdAttrCategoryValueTable.loading"
            :columns="prdAttrCategoryValueTable.columns"
            :dataSource="prdAttrCategoryValueTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="false"
            :actionButton="false"/>
        </a-tab-pane>
        <a-tab-pane tab="相关物品" :key="refKeys[2]" :forceRender="true">
          <ref-prd-table query-by="categoryId" :query-value="model.id"></ref-prd-table>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  // 相关物品
  import RefPrdTable from '../../brand/modules/RefPrdTable'

  export default {
    name: 'PrdCategoryModalView',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      JDictSelectTagIop,
      RefPrdTable
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
        detailStatus: false, // 是否详情页面
        validatorRules: {
          name: {
            rules: [
              { required: true, message: '请输入名称!' }
            ]
          },
          pid: {
            rules: [
              { required: true, message: '请选择上级品类!' }
            ]
          },
          categoryType: {
            rules: [
              { required: true, message: '请选择品类类型!' }
            ]
          },
          active: {
            valuePropName: 'checked',
            rules: [
              { required: true, message: '请选择状态!' }
            ]
          },
          sequence: {
            rules: [
              { required: false, message: '请输入序号!' }
            ]
          },
          removalStrategyId: { rules: [] },
          completeName: { rules: [] }
        },
        refKeys: ['prdCategoryValue', 'prdAttrCategoryValue', 'prdProduceValue'],
        activeKey: 'prdCategoryValue',
        // 相关分类
        prdCategoryValueTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '分类名称',
              key: 'completeName',
              type: FormTypes.text,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: []
            }
          ]
        },
        // 相关规格
        prdAttrCategoryValueTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '规格名称',
              key: 'name',
              type: FormTypes.text,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: []
            }
          ]
        },
        // 相关物品
        prdProduceValueTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物品名称',
              key: 'name',
              type: FormTypes.text,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            }
          ]
        },
        url: {
          // 相关分类
          prdCategoryValue: {
            list: '/iop/prd/category/queryPrdCategoryListByCategoryId'
          },
          // 相关规格
          prdAttrCategoryValue: {
            list: '/iop/prd/attrcategory/selectAttrCategoryListByCategoryId'
          },
          prdProduceValue: {
            list: '/iop/prd/category/queryPrdCategoryListByCategoryId'
          }
        }
      }
    },
    created() {
    },
    methods: {
      /** 当点击了查看按钮时调用此方法 */
      detail(record) {
       // refKeys: ['prdCategoryValue', 'prdAttrCategoryValue', 'prdProduceValue']
        if (typeof this.editBefore === 'function') this.editBefore(record)
        this.visible = true
        this.detailStatus = true
        this.activeKey = this.refKeys[0]
        this.form.resetFields()
        record.active = record.active == 1 ? true : false
        this.model = Object.assign({}, record)
        if (typeof this.editAfter === 'function') this.editAfter(this.model)
      },

      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model, 'name', 'pid', 'categoryType', 'active', 'sequence', 'completeName', 'removalStrategyId')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          // 加载相关信息
          // 加载相关物品
          this.requestSubTableData(this.url.prdProduceValue.list, params, this.prdProduceValueTable)
          // 加载相关规格
          this.requestSubTableData(this.url.prdAttrCategoryValue.list, params, this.prdAttrCategoryValueTable)
          // 加载相关分类
          this.requestSubTableData(this.url.prdCategoryValue.list, params, this.prdCategoryValueTable)
        }
      },
      handleOk() {
        this.close()
      }

    }
  }
</script>