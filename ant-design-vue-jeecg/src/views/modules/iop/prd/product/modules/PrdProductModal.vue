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
            <a-form-item label="内部编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'defaultCode', validatorRules.defaultCode]" placeholder="请输入内部编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop type="list" v-decorator="['categoryId', validatorRules.categoryId]"
                                     :trigger-change="true" dictCode="PRD_CATEGORY,COMPLETE_NAME,ID"
                                     placeholder="请选择所属品类"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="条形码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'barcode', validatorRules.barcode]" placeholder="请输入条形码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop type="list" v-decorator="[ 'brandId', validatorRules.brandId]" placeholder="请选择品牌"
                                     :trigger-change="true" dict-code="PRD_BRAND,name,id"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="所属生产设施" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="[ 'belongEquip', validatorRules.belongEquip]"
                                 placeholder="请选择所属生产设施" :trigger-change="true"
                                 dict-code="IOP_PUB_CATEGORY"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="制造商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'manufactorId', validatorRules.manufactorId]" placeholder="请输入制造商"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop v-decorator="[ 'uomId', validatorRules.uomId]" placeholder="请选择计量单位"
                                     :trigger-change="true" dict-code="UOM_UOM,name,id"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'vendorId', validatorRules.vendorId]" placeholder="请输入供应商"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="默认库存位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'locationId', validatorRules.locationId]" placeholder="请输入默认库存位置"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
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
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="是否可被替代" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="[ 'replaced', validatorRules.replaced]"
                                 placeholder="请选择是否可被替代"
                                 :trigger-change="true" dict-code="yn"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="[ 'active', validatorRules.active]" placeholder="请选择状态"
                                 :trigger-change="true" dict-code="IOP_PUB_ACTION"/>
            </a-form-item>
          </a-col>
        </a-row>
        <!--  上传图片 -->
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="图片上传">
              <j-image-upload v-decorator="[ 'imageList', validatorRules.imageList]"></j-image-upload>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="内部说明" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['description', validatorRules.description]" rows="4" placeholder="请输入内部说明"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="属性设置" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="prdProductAttrValueRelTable.loading"
            :columns="prdProductAttrValueRelTable.columns"
            :dataSource="prdProductAttrValueRelTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"
            @valueChange="handleValueChange"/>
        </a-tab-pane>

        <a-tab-pane tab="规格设置" :key="refKeys[1]" >
          <j-editable-table
            :ref="refKeys[1]"
            :loading="prdProductAttrCategoryRelTable.loading"
            :columns="prdProductAttrCategoryRelTable.columns"
            :dataSource="prdProductAttrCategoryRelTable.dataSource"
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
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import { httpAction, getAction } from '@/api/manage'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import {ajaxGetDictItems,ajaxGetDictItemsIop} from '@/api/api'

  export default {
    name: 'PrdProductModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTagIop, JDictSelectTag, JImageUpload,JEditableTable
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
          defaultCode: {
            rules: [{ required: true, message: '请输入内部编码' }]
          },
          barcode: {
            rules: []
          },
          categoryId: {
            rules: [
              { required: true, message: '请输入品类!' }
            ]
          },
          brandId: {
            rules: [
              { required: true, message: '请输入品牌!' }
            ]
          },
          manufactorId: {
            rules: []
          },
          vendorId: {
            rules: [
              { required: true, message: '请输入供应商!' }
            ]
          },
          belongEquip: {
            rules: [
              { required: true, message: '请输入所属生产设施!' }
            ]
          },
          uomId: {
            rules: [
              { required: true, message: '请输入计量单位!' }
            ]
          },
          replaced: {
            rules: [
              { required: true, message: '请输入被替代!' }
            ]
          },
          description: {
            rules: [
              { required: true, message: '请输入内部说明!' }
            ]
          },
          weight: {
            rules: [
              { required: true, message: '请输入重量!' }
            ]
          },
          volumn: {
            rules: [
              { required: true, message: '请输入体积!' }
            ]
          },
          active: {
            rules: []
          }
        },
        refKeys: ['prdProductAttrValueRel', 'prdProductAttrCategoryRel'],
        tableKeys: ['prdProductAttrValueRel', 'prdProductAttrCategoryRel'],
        activeKey: 'prdProductAttrValueRel',
        // 物品属性值关系
        prdProductAttrValueRelTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '产品属性',
              key: 'attributeId',
              type: FormTypes.sel_search,
              dataFrom: 'iop',
              dictCode: 'PRD_ATTRIBUTE,NAME,ID',
              width: '200px',
              options:[],
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '属性值',
              key: 'attributeValueId',
              type: FormTypes.sel_search,
              dataFrom: 'iop',
              dictCode: 'PRD_ATTRIBUTE_VALUE,NAME,ID',
              width: '200px',
              options:[],
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '启用',
              key: 'active',
              type: FormTypes.sel_search,
              dictCode: 'IOP_PUB_ACTION',
              width: '200px',
              options:[],
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            }
          ]
        },
        prdProductAttrCategoryRelTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '名称',
              key: 'name',
              type: FormTypes.text,
              width: '200px'
            },
            {
              title: '编码',
              key: 'code',
              type: FormTypes.text,
              width: '200px'
            },
            {
              title: '相关属性',
              key: 'attributeContent',
              type: FormTypes.text,
              width: '200px'
            }
          ]
        },
        url: {
          add: '/iop/prd/product/add',
          edit: '/iop/prd/product/edit',
          prdProductAttrValueRel: {
            // 获取属性信息
            list: '/iop/prd/product/queryPrdProductAttrValueRelByMainId'
          },
          prdProductAttrCategoryRel: {
            // 获取规格信息
            list: ''
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
        let fieldval = pick(this.model, 'active', 'name', 'defaultCode', 'barcode', 'categoryId',
          'brandId', 'manufactorId', 'vendorId', 'belongEquip', 'uomId', 'replaced', 'description', 'weight', 'volumn')
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
          prdProductAttrValueRelList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'active', 'name', 'defaultCode', 'barcode', 'categoryId',
          'brandId', 'manufactorId', 'vendorId', 'belongEquip', 'uomId', 'replaced', 'description', 'weight', 'volumn'))
      },
      // 重写
      request(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        // 清楚子表ID
        for (var i = 0; i < formData.prdProductAttrValueRelList.length; i++) {
          formData.prdProductAttrValueRelList[i].id = ''
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
      },
      /** 当选项被改变时，联动其他组件 */
      handleValueChange(event) {
        const { type, row, column, value, target } = event
        if (type === FormTypes.sel_search) {
          // 第一列 属性
          if (column.key === 'attributeId') {
            var _dictCode = 'PRD_ATTRIBUTE_VALUE,NAME,ID';
            if(value&&value!=undefined){
              _dictCode +=',ATTRIBUTE_ID='+value;
            }

            //根据字典Code, 初始化字典数组
            ajaxGetDictItemsIop(_dictCode, null).then((res) => {
              if (res.success) {
                // 设置属性值的 options
                this.prdProductAttrValueRelTable.columns[1].options = res.result;
                // 清空属性值的数据   更新JEditableTable.vue中setValues方法
                target.setValues([{
                  rowKey: row.id,
                  values: { attributeValueId: '',active:''}
                }])
              }
            })
          }
        }

      },

    }
  }
</script>

<style scoped>
</style>