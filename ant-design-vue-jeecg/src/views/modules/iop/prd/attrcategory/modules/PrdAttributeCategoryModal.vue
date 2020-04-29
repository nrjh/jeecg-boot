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
            <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'code', validatorRules.code]" placeholder="请输入编码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="所属品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-iop type="list" v-decorator="['categoryId', validatorRules.categoryId]" :trigger-change="true" dictCode="PRD_CATEGORY,COMPLETE_NAME,ID" placeholder="请选择所属品类"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['active', validatorRules.active]" :trigger-change="true" dictCode="IOP_PUB_ACTION" placeholder="请选择状态"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'sequence', validatorRules.sequence]" placeholder="请输入序号" style="width: 100%"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="相关属性" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="prdAttrCategAttrValueRelTable.loading"
            :columns="prdAttrCategAttrValueRelTable.columns"
            :dataSource="prdAttrCategAttrValueRelTable.dataSource"
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
  import { validateDuplicateValueIop } from '@/utils/util'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import { httpAction, getAction } from '@/api/manage'

  export default {
    name: 'PrdAttributeCategoryModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTagIop,
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
            {required: true, message: '请输入状态!'},
          ]},
          sequence: {rules: [
            {required: true, message: '请输入序号!'},
           // { validator: (rule, value, callback) => validateDuplicateValueIop('prd_attribute_category', 'sequence', value, this.model.id, callback)},
          ]},
          name: {rules: [
            {required: true, message: '请输入名称!'},
           // { validator: (rule, value, callback) => validateDuplicateValueIop('prd_attribute_category', 'name', value, this.model.id, callback)},
          ]},
          code: {rules: [
            {required: true, message: '请输入编码!'},
          //  { validator: (rule, value, callback) => validateDuplicateValueIop('prd_attribute_category', 'code', value, this.model.id, callback)},
          ]},
          categoryId: {rules: [
            {required: true, message: '请选择所属品类!'},
          ]},
        },
        refKeys: ['prdAttrCategAttrValueRel', ],
        tableKeys:['prdAttrCategAttrValueRel', ],
        activeKey: 'prdAttrCategAttrValueRel',
        // 物品规格与产品属性值关系
        prdAttrCategAttrValueRelTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '属性',
              key: 'attributeId',
              type: FormTypes.sel_search,
              dataFrom: "iop",
              dictCode:"PRD_ATTRIBUTE,NAME,ID",
              width:"200px",
              placeholder: '请选择${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '属性值',
              key: 'attributeValueId',
              type: FormTypes.sel_search,
              dataFrom: "iop",
              dictCode:"PRD_ATTRIBUTE_VALUE,NAME,ID",
              width:"200px",
              placeholder: '请选择${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
          ]
        },
        url: {
          add: "/iop/prd/attrcategory/add",
          edit: "/iop/prd/attrcategory/edit",
          prdAttrCategAttrValueRel: {
            list: '/iop/prd/attrcategory/queryPrdAttrCategAttrValueRelByMainId'
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
        let fieldval = pick(this.model,'active','sequence','name','code','categoryId')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.prdAttrCategAttrValueRel.list, params, this.prdAttrCategAttrValueRelTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          prdAttrCategAttrValueRelList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'active','sequence','name','code','categoryId'))
     },
      // 重写
      request(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        // 清楚子表ID
        for (var i = 0; i < formData.prdAttrCategAttrValueRelList.length; i++) {
          formData.prdAttrCategAttrValueRelList[i].id = '';
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