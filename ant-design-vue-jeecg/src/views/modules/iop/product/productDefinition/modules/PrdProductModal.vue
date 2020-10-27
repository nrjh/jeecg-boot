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
            <a-form-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'active', validatorRules.active]" placeholder="请输入启用" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="内部货号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'defaultCode', validatorRules.defaultCode]" placeholder="请输入内部货号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="条形码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'barcode', validatorRules.barcode]" placeholder="请输入条形码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'categoryId', validatorRules.categoryId]" placeholder="请输入品类" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'brandId', validatorRules.brandId]" placeholder="请输入品牌" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="制造商  选择品牌时从品牌信息带入" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'manufactorId', validatorRules.manufactorId]" placeholder="请输入制造商  选择品牌时从品牌信息带入" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'vendorId', validatorRules.vendorId]" placeholder="请输入供应商" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="所属生产设施  备品备件时，标明该备件可用于哪个生产设施（四线一库）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'belongEquip', validatorRules.belongEquip]" placeholder="请输入所属生产设施  备品备件时，标明该备件可用于哪个生产设施（四线一库）" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="计量单位 与 计量单位[UOM_UOM]表关联" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'uomId', validatorRules.uomId]" placeholder="请输入计量单位 与 计量单位[UOM_UOM]表关联" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="被替代 当该物品（设备/备件/办公用品等）被其他设备替代时，该字段为true，默认为false" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'replaced', validatorRules.replaced]" placeholder="请输入被替代 当该物品（设备/备件/办公用品等）被其他设备替代时，该字段为true，默认为false" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="内部说明" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['description', validatorRules.description]" rows="4" placeholder="请输入内部说明"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="重量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'weight', validatorRules.weight]" placeholder="请输入重量" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="体积" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'volumn', validatorRules.volumn]" placeholder="请输入体积" style="width: 100%"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="物品属性值关系" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="prdProductAttrValueRelTable.loading"
            :columns="prdProductAttrValueRelTable.columns"
            :dataSource="prdProductAttrValueRelTable.dataSource"
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
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'PrdProductModal',
    mixins: [JEditableTableMixin],
    components: {
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
          active: {rules: [
          ]},
          name: {rules: [
            {required: true, message: '请输入名称!'},
          ]},
          defaultCode: {rules: [
          ]},
          barcode: {rules: [
          ]},
          categoryId: {rules: [
            {required: true, message: '请输入品类!'},
          ]},
          brandId: {rules: [
            {required: true, message: '请输入品牌!'},
          ]},
          manufactorId: {rules: [
          ]},
          vendorId: {rules: [
            {required: true, message: '请输入供应商!'},
          ]},
          belongEquip: {rules: [
            {required: true, message: '请输入所属生产设施  备品备件时，标明该备件可用于哪个生产设施（四线一库）!'},
          ]},
          uomId: {rules: [
            {required: true, message: '请输入计量单位 与 计量单位[UOM_UOM]表关联!'},
          ]},
          replaced: {rules: [
            {required: true, message: '请输入被替代 当该物品（设备/备件/办公用品等）被其他设备替代时，该字段为true，默认为false!'},
          ]},
          description: {rules: [
            {required: true, message: '请输入内部说明!'},
          ]},
          weight: {rules: [
            {required: true, message: '请输入重量!'},
          ]},
          volumn: {rules: [
            {required: true, message: '请输入体积!'},
          ]},
        },
        refKeys: ['prdProductAttrValueRel', ],
        tableKeys:['prdProductAttrValueRel', ],
        activeKey: 'prdProductAttrValueRel',
        // 物品属性值关系
        prdProductAttrValueRelTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '启用',
              key: 'active',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '产品属性',
              key: 'attributeId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '属性值',
              key: 'attributeValueId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
          ]
        },
        url: {
          add: "/product/product/add",
          edit: "/product/product/edit",
          prdProductAttrValueRel: {
            list: '/product/product/queryPrdProductAttrValueRelByMainId'
          },
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
        let fieldval = pick(this.model,'active','name','defaultCode','barcode','categoryId','brandId','manufactorId','vendorId','belongEquip','uomId','replaced','description','weight','volumn')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.prdProductAttrValueRel.list, params, this.prdProductAttrValueRelTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          prdProductAttrValueRelList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'active','name','defaultCode','barcode','categoryId','brandId','manufactorId','vendorId','belongEquip','uomId','replaced','description','weight','volumn'))
     },

    }
  }
</script>

<style scoped>
</style>