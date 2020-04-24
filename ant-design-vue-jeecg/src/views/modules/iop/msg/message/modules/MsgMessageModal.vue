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
            <a-form-item label="主题" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'subject', validatorRules.subject]" placeholder="请输入主题"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="作者" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'authorId', validatorRules.authorId]" placeholder="请输入作者"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="消息类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JDictSelectTag v-decorator="['messageType', validatorRules.messageType]" placeholder="请选择消息类型" dictCode="IOP_MESSAGE_TYPE"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="子类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-tree-select-iop
                ref="treeSelect"
                placeholder="请选择消息子类型"
                v-decorator="['subtypeId', validatorRules.subtypeId]"
                dict="msg_subtype,NAME,id"
                pidField="pid"
                pidValue="0"
                hasChildField="has_child">
              </j-tree-select-iop>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="正文" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['body', validatorRules.body]" rows="4" placeholder="请输入正文"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="消息值变更记录" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="msgTrackingValueTable.loading"
            :columns="msgTrackingValueTable.columns"
            :dataSource="msgTrackingValueTable.dataSource"
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
  import JTreeSelectIop from '@/components/jeecg/JTreeSelectIop'

  export default {
    name: 'MsgMessageModal',
    mixins: [JEditableTableMixin],
    components: {JDictSelectTag,JTreeSelectIop},
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
          subject: {
            rules: [
              { required: true, message: '请输入主题!' }
            ]
          },
          authorId: {
            rules: [
              { required: true, message: '请输入作者!' }
            ]
          },
          messageType: {
            rules: [
              { required: true, message: '请输入消息类型!' }
            ]
          },
          subtypeId: {
            rules: [
              { required: true, message: '请输入子类型!' }
            ]
          },
          body: {
            rules: [
              { required: true, message: '请输入正文!' }
            ]
          }
        },
        refKeys: ['msgTrackingValue'],
        tableKeys: ['msgTrackingValue'],
        activeKey: 'msgTrackingValue',
        // 消息值变更记录
        msgTrackingValueTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '字段代码',
              key: 'field',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '字段描述',
              key: 'fieldDesc',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '字段类型',
              key: 'fieldType',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '旧文本值',
              key: 'oldValueText',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '新文本值',
              key: 'newValueText',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            }
          ]
        },
        url: {
          add: '/iop/msg/message/add',
          edit: '/iop/msg/message/edit',
          msgTrackingValue: {
            list: '/iop/msg/message/queryMsgTrackingValueByMainId'
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
        let fieldval = pick(this.model, 'subject', 'authorId', 'pubTime', 'body', 'messageType', 'subtypeId')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.msgTrackingValue.list, params, this.msgTrackingValueTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          msgTrackingValueList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'subject', 'authorId', 'pubTime', 'body', 'messageType', 'subtypeId'))
      }

    }
  }
</script>

<style scoped>
</style>