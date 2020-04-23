<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">

      <!-- 操作-->
      <div class="table-operator" style="margin-bottom: 20px; display:none">
        <a-button @click="edit(record,'品牌/编辑')" type="primary">编辑</a-button>&nbsp;&nbsp;
        <a-button @click="add('品牌/新建')" type="primary">新建</a-button>&nbsp;&nbsp;
        <a-button @click="handleDelete(record.id)" type="primary">删除</a-button>&nbsp;&nbsp;
        <a-button @click="edit(record,'品牌/新建',true)" type="primary">复制</a-button>
      </div>
      <!-- 信息展示-->
      <a-form :form="form">
        <a-row>
          <a-form-item label="品牌名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入品牌名称" disabled="disabled"></a-input>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item label="首字母" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'firstLetter', validatorRules.firstLetter]" placeholder="请输入首字母"
                     disabled="disabled"></a-input>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item label="制造商" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag-iop v-decorator="[ 'manufactorId', validatorRules.manufactorId]" :triggerChange="true"
                                   placeholder="请选择制造商"
                                   dictCode="RES_PARTNER,NAME,ID" disabled="disabled"/>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-switch checkedChildren="启用" unCheckedChildren="禁用" v-decorator="['active', validatorRules.active]"
                      disabled="disabled"/>
          </a-form-item>
        </a-row>
        <a-row>
          <a-form-item label="简介" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-textarea v-decorator="['description', validatorRules.description]" rows="4" placeholder="请输入简介"
                        disabled="disabled"/>
          </a-form-item>
        </a-row>
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
        <!--
        <a-tab-pane tab="相关物品" :key="refKeys[1]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[1]"
            :loading="prdProduceValueTable.loading"
            :columns="prdProduceValueTable.columns"
            :dataSource="prdProduceValueTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="false"
            :actionButton="false"/>
        </a-tab-pane>
        -->
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue, iopValidateDuplicateValue } from '@/utils/util'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'

  export default {
    name: 'PrdBrandModalView',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTagIop
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
              { required: true, message: '请输入名称!' },
              { validator: (rule, value, callback) => iopValidateDuplicateValue('prd_brand', 'name', value, this.model.id, callback) }
            ]
          },
          firstLetter: {
            rules: [
              { required: true, message: '请输入首字母!' }
            ]
          },
          manufactorId: {
            rules: [
              { required: true, message: '请选择制造商!' }
            ]
          },
          active: {
            valuePropName: 'checked',
            rules: [
              { required: true, message: '请选择状态!' }
            ]
          },
          description: {
            rules: []
          }
        },
        refKeys: ['prdCategoryValue', 'prdProduceValue'],
        // tableKeys: ['prdCategoryValue', 'prdProduceValue'],
        activeKey: 'prdCategoryValue',
        // 属性值
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
              validateRules: [{ required: true, message: '${title}不能为空' }]
            }
          ]
        },
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
          prdCategoryValue: {
            list: '/iop/prd/category/queryPrdCategoryListByBrandId'
          },
          prdProduceValue: {
            list: '/iop/prd/category/queryPrdCategoryListByBrandId'
          }
        }
      }
    },
    methods: {
      /** 当点击了查看按钮时调用此方法 */
      detail(record) {
        this.refKeys = ['prdCategoryValue', 'prdProduceValue']
        if (typeof this.editBefore === 'function') this.editBefore(record)
        this.visible = true
        this.activeKey = this.refKeys[0]
        this.form.resetFields()
        record.active = record.active == 1 ? true : false
        this.model = Object.assign({}, record)
        if (typeof this.editAfter === 'function') this.editAfter(this.model)
      },

      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model, 'name', 'firstLetter', 'manufactorId', 'active', 'description')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.prdCategoryValue.list, params, this.prdCategoryValueTable)
          this.requestSubTableData(this.url.prdProduceValue.list, params, this.prdProduceValueTable)
        }
      },
      handleOk() {
        this.close()
      }
    }
  }
</script>