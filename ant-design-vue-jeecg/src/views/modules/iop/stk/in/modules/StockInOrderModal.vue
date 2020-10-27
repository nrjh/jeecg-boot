<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <template slot="footer">
      <template v-if="editable">
        <a-button type="primary" @click="handleOkSave('draft')">保存</a-button>
        <a-button type="primary" @click="handleOkSave('approve')">提交审核</a-button>
      </template>
      <template v-if="stockInable">
        <a-button type="primary" @click="handleOkSave('submit')">保存</a-button>
        <a-button type="primary" @click="handleOkSave('done')">提交</a-button>
      </template>
      <template v-if="checkable">
        <a-button type="primary" @click="handleSaveCheck()">审批</a-button>
      </template>
      <a-button type="primary" @click="handleCancel">返回</a-button>
    </template>

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>
          <a-col :span="8">
            <a-form-item label="采购入库单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input v-decorator="[ 'orderInId', validatorRules.orderInId]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="入库日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-date placeholder="请选择入库日期" v-decorator="[ 'orderInDate', validatorRules.orderInDate]"
                      :trigger-change="true" style="width: 100%" :disabled="!stockInable"/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="入库人员" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input v-decorator="[ 'orderInName', validatorRules.orderInName]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-item label="单据类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-radio-group v-decorator="['orderType',validatorRules.orderType]" :disabled="!editable">
                <a-radio value="normal">
                  正常单
                </a-radio>
                <a-radio value="urgency">
                  紧急单
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同单号" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-input v-decorator="[ 'contract', validatorRules.contract]" placeholder="请输入合同单号"
                       :disabled="!editable"></a-input>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="{span:3}" :wrapperCol="{span:21}">
              <a-textarea v-decorator="[ 'note', validatorRules.note]" placeholder="请输入备注"
                          :disabled="!editable"></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAddProd" type="primary" icon="plus" v-if="editable">添加物料</a-button>
      </div>

      <a-table
        ref="editableTable"
        size="middle"
        bordered
        :loading='stockInOrderLineTable.loading'
        :visible="true"
        :columns="stockInOrderLineTable.columns"
        :dataSource="stockInOrderLineTable.dataSource"
        :pagination="false"
      >
        <template slot="vendorCode" slot-scope="text,record,index">
          <j-dict-select-tag-iop :disabled="!editable" type="list"  v-model="stockInOrderLineTable.dataSource[index].partnerId"
                                 :trigger="true" dictCode="prd_brand,manufactor_name,id,is_del='0'" placeholder="请选择供应商"/>
        </template>
        <template slot="productActualQty" slot-scope="text,record,index">
          <a-input-number v-if="editable" v-model="stockInOrderLineTable.dataSource[index].productActualQty"/>
          <span v-else>{{stockInOrderLineTable.dataSource[index].productActualQty}}</span>
        </template>
        <template slot="priceUnit" slot-scope="text,record,index">
          <a-input-number v-if="editable" v-model="stockInOrderLineTable.dataSource[index].priceUnit"/>
          <span v-else>{{stockInOrderLineTable.dataSource[index].priceUnit}}</span>
        </template>
        <template slot="completeTime" slot-scope="text,record,index">
          <j-date  v-if="editable"  v-model="stockInOrderLineTable.dataSource[index].completeTime"/>
          <span v-else>{{stockInOrderLineTable.dataSource[index].completeTime}}</span>
        </template>
        <template slot="action" slot-scope="text,record,index">
          <a v-if="!checkShow" @click="handleDistribution(index)">分配货位</a>
          <a-popconfirm v-if="editable" title="确定删除吗?" @confirm="() => handleDelete(index)">
            <a>删除</a>
          </a-popconfirm>
<!--
          <a v-if="!editable && !stockInable" @click="handleDistribution(index)">查看货位</a>
-->
          <a v-if="!editable && !stockInable" @click="handleDistribution(index)">查看货位</a>
        </template>
      </a-table>

      <br>
      <template v-if="checkList.length>0">
        <template v-for="item in checkList">
          <a-row>
            <a-col :span="8">
              <a-form-item label="审批结果" :labelCol="{span:9}" :wrapperCol="{span:14}">
                {{item.result == 'pass' ? '审批通过':'审批失败'}}
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="审批人" :labelCol="{span:9}" :wrapperCol="{span:14}">
                {{item.createBy}}
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="审批时间" :labelCol="{span:9}" :wrapperCol="{span:14}">
                {{item.createTime}}
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="审批意见" :labelCol="{span:3}" :wrapperCol="{span:21}">
                {{item.sugg}}
              </a-form-item>
            </a-col>
          </a-row>
        </template>
      </template>
      <a-form :form="checkForm" v-if="checkable">
        <a-row>
          <a-col :span="12">
            <a-form-item label="审批结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group v-decorator="['result',validatorRules.result]" :disabled="!checkable">
                <a-radio value="pass">
                  审批通过
                </a-radio>
                <a-radio value="reject">
                  审批驳回
                </a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="审批内容" :labelCol="{span:3}" :wrapperCol="{span:21}">
              <a-textarea v-decorator="[ 'sugg', validatorRules.sugg]" placeholder="请输入审批内容"
                          :disabled="!checkable"></a-textarea>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <add-prod-modal ref="addProdFrom" @returnData="returnData"></add-prod-modal>
    <distribution-prod-modal ref="distributionProdFrom" @returnData="distributionReturnData"></distribution-prod-modal>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import AddProdModal from '../../../order/orderplan/modules/addProdModal.vue'
  import DistributionProdModal from './DistributionProdModal.vue'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import ATextarea from 'ant-design-vue/es/input/TextArea'
  import JDictSelectTagIop from '../../../../../../components/dict/JDictSelectTagIop'
  import store from '@/store'
  import ACol from 'ant-design-vue/es/grid/Col'
  import moment from 'moment'

  export default {
    name: 'StockInOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      ACol,
      JDictSelectTagIop,
      ATextarea,
      JDate,
      JDictSelectTag,
      AddProdModal,
      DistributionProdModal,
    },
    data() {
      return {
        seeanable:false,
        checkForm: this.$form.createForm(this),
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
          orderInId: {
            rules: []
          },
          contract: {
            rules: [
              { required: true, message: '请输入合同单号!' }
            ]
          },
          partnerId: {
            rules: [
              { required: true, message: '请选择供应商!' }
            ]
          },
          createName: {
            rules: []
          },
          createTime: {
            rules: [
              { required: true, message: '请选择采购日期!' }
            ]
          },
          orderType: {
            rules: [
              { required: true, message: '请选择单据类型!' }
            ]
          },
          note: {
            rules: []
          }
        },
        refKeys: ['stockInOrderLine'],
        tableKeys: ['stockInOrderLine'],
        activeKey: 'stockInOrderLine',
        activeIndex: 0,
        editable: true, // 可 新增
        checkable: false, // 可 审批
        checkShow: false,
        stockInable: false, // 可入库
        // 入库明细
        stockInOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物料名称',
              align:"center",
              dataIndex: 'rpoductName',
              width: '6%'
            },
            {
              title: '物料编号',
              align:"center",
              dataIndex: 'productNo',
              width: '6%'
            },
            {
              title: '物料类别',
              align:"center",
              dataIndex: 'categoryType',
              width: '6%',
              customRender:function (text) {
                if(text === 'equip'){
                  return "生产设施"
                }else if(text === 'spare'){
                  return "备品备件"
                }else {
                  return "办公用品"
                }
              }
            },
            {
              title: '规格',
              align:"center",
              dataIndex: 'acName',
              width: '6%'
            },
            // {
            //   title: '标识',
            //   align:"center",
            //   dataIndex: 'remark',
            //   width: '6%'
            // },
            {
              title: '单位',
              align:"center",
              dataIndex: 'uuName',
              width: '6%'
            },
/*            {
              title: '应入库数量',
              align:"center",
              dataIndex: 'productPlyQty',
              width: '6%'
            },*/
            {
              title:'供应商',
              align:"center",
              key: 'vendorId',
              width: '12%',
              scopedSlots: { customRender: 'vendorCode' }
            },
            {
              title: '实际数量',
              align:"center",
              dataIndex: 'productActualQty',
              width: '6%',
              scopedSlots: { customRender: 'productActualQty' }
            },
            {
              title: '单价',
              align:"center",
              key: 'priceUnit',
              width: '6%',
              scopedSlots: { customRender: 'priceUnit' }
            },
            {
              title: '金额',
              align:"center",
              key: 'priceTotal',
              width: '6%',
              customRender: function(t, r, index) {
                return r.priceUnit * r.productActualQty
              }
            },
            {
              title: '入货位时间',
              align:"center",
              key: 'completeTime',
              width: '12%',
              scopedSlots: { customRender: 'completeTime' }
            },
            {
              title: '入库详情',
              align:"center",
              key: 'status',
              dataIndex: 'status',
              width: '12%',
              customRender: function(text, r, index) {
                // return 'normal' == text ? '正常' : '未入库完成'
                let str = ''
                if (r.orderStockInLists)
                  for (let i = 0; i < r.orderStockInLists.length; i++) {
                    str += r.orderStockInLists[i].whInputStockLotName + ':' + r.orderStockInLists[i].productActualQty + '件'
                    if (i != r.orderStockInLists.length - 1) {
                      str += ';'
                    }
                  }
                return str
              }
            },
            {
              title: '操作',
              key: 'action',
              width: '6%',
              scopedSlots: { customRender: 'action' }
            }
          ],
          columnsDel: []
        },
        checkList: [],
        url: {
          add: '/stk/in/stockInOrder/add',
          edit: '/stk/in/stockInOrder/edit',
          stockInOrderLine: {
            list: '/stk/in/stockInOrder/queryStockInOrderLineEcho'
          },
          check: '/stk/in/stockInOrder/check'
        }
      }
    },
    methods: {
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      addAfter() {
        this.stockInOrderLineTable.dataSource = []
        if (!this.model.state) {
          this.model.state = 'draft'
        }
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        // 默认查看状态
        this.editable = true
        this.checkable = false
        this.checkShow = true;
        this.stockInable = false
        let fieldval = pick(this.model, 'orderInId', 'partnerId', 'createTime', 'createName', 'contract', 'orderType', 'note','orderInDate','orderInName')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        this.checkList = []
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.stockInOrderLine.list, params, this.stockInOrderLineTable)
          getAction(this.url.check, { id: this.model.id }).then(res => {
            this.checkList = res.result
            console.log(this.checkList)
          })
        }
      },
      look(record) {
        this.edit(record)
        this.editable = false
        this.checkShow = true
        this.seeanable = false
      },
      stockIn(record) {
        this.edit(record)
        this.editable = false
        this.stockInable = true
        this.checkShow = false;
        this.seeanable = true
      },
      check(record) {
        this.edit(record)
        console.log("33333333333333333333333333333333333333",record)
        this.editable = false
        this.checkable = true
        this.checkShow = true
        this.seeanable = false
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          stockInOrderLineList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'orderInId', 'pickingNo', 'contract', 'partnerId', 'partnerName', 'createName', 'createTime', 'orderInDate', 'orderType', 'state', 'note'))
      },
      handleAddProd() {
        this.$refs.addProdFrom.add()
      },
      handleDistribution(index) {
        console.log("index----",index)
        let proInfo = this.stockInOrderLineTable.dataSource[index]
        console.log("-----------------------------------------------------------")
        console.log(proInfo);
        let orderInfo = {}
        orderInfo.partnerid = proInfo.partnerId;//this.form.getFieldValue('partnerId');
        orderInfo.createName = this.userName
        orderInfo.productName = proInfo.rpoductName
        orderInfo.categoryType = proInfo.categoryType
        orderInfo.attributeCategoryId = proInfo.attributeCategoryId
        orderInfo.productActualQty = proInfo.productActualQty
        orderInfo.priceTotal = proInfo.priceUnit * proInfo.productActualQty
        orderInfo.orderStockInLists = this.stockInOrderLineTable.dataSource[index].orderStockInLists
        this.activeIndex = index

        console.log("................",this.seeanable)
        this.$refs.distributionProdFrom.add(orderInfo, this.seeanable)
      },
      handleDelete(index) {
        this.stockInOrderLineTable.dataSource.splice(index)
      },
      returnData(data) {
        console.log("回显数据111111",data)
        this.stockInOrderLineTable.loading = true

        let stockData = [];

        for (var i = 0; i < data.length; i++){
          let formData = Object.assign({productPlyQty:data[i].productQty}, data[i]);
          stockData.push(formData)
        }
        // for (let i = 0; i < data.length; i++) {
        //   let temp = {}
        //   temp.productId = data[i].productId
        //   temp.productName = data[i].name
        //   temp.categoryType = data[i].categoryId
        //   temp.defaultCode = data[i].productNo
        //   temp.attributeCategoryId = data[i].attributeCategoryId
        //   temp.productQty = 0
        //   temp.productUomId = data[i].productUomId
        //   temp.status = 'freeze'
        //
        //   this.stockInOrderLineTable.dataSource.push(temp)
        // }
        // this.stockInable = true
        console.log(stockData)

        this.stockInOrderLineTable.dataSource = this.stockInOrderLineTable.dataSource.concat(stockData);
        //this.stockInOrderLineTable.dataSource = this.stockInOrderLineTable.dataSource.concat(data);
        this.stockInOrderLineTable.loading = false
      },
      distributionReturnData(data, status) {
        this.stockInOrderLineTable.dataSource[this.activeIndex].orderStockInLists = data
        this.stockInOrderLineTable.dataSource[this.activeIndex].status = status
      },
      handleOkSave(state) {
        let pordData = this.stockInOrderLineTable.dataSource
        for(var i=0;i<pordData.length;i++){
          pordData[i].priceTotal = pordData[i].priceUnit * pordData[i].productActualQty;
        }
        let that = this

        if (state == 'submit') {
          if (!this.validateLineData()) {
            return false
          }
        } else if (state == 'done') {
          if (!this.validateDistributionData()) {
            return false
          }
        }
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            let obj = {
              stockInOrderLineList: pordData,
              state: state
            }
            console.log('obj', obj)
            let modelEdit = []
            let formData = []
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
              formData = Object.assign(obj, values)
            } else {
              httpurl += this.url.edit
              method = 'put'
              modelEdit = Object.assign(this.model, values)
              formData = Object.assign(modelEdit, obj)
            }
            console.log('表单提交数据', formData)
            console.log('表单提交数据httpurl', httpurl)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
                that.$emit('searchQuery')
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
      handleSaveCheck() {
        this.checkForm.validateFields((err, values) => {
          if (!err) {
            let method = 'post'
            let order = {
              orderNo: this.model.id
            }
            let formData = Object.assign(order, values)
            console.log("审批内容111111111",formData)
            if(formData.sugg != undefined ){
              httpAction(this.url.check, formData, method).then((res) => {
                if (res.success) {
                  this.$message.success(res.message)
                  this.$emit('ok')
                  this.$emit('searchQuery')
                } else {
                  this.$message.warning(res.message)
                }
              }).finally(() => {
                this.confirmLoading = false
                this.close()
              })
            }else{
              this.$message.warning('请输入审批内容')
            }
          }
        })
      },
      validateLineData() {
        let dataSource = this.stockInOrderLineTable.dataSource
        if (!dataSource || dataSource.length == 0) {
          this.$message.warning('请添加物料！！！')
          return false
        } else {
          for (let i = 0; i < dataSource.length; i++) {
            if (!dataSource[i].productActualQty || !dataSource[i].priceUnit) {
              this.$message.warning('请填写物料的数量与单价！！！')
              return false
            }
          }
        }
        return true
      },
      validateDistributionData() {
        let dataSource = this.stockInOrderLineTable.dataSource
        console.log(dataSource)
        for (let i = 0; i < dataSource.length; i++) {
          if (!dataSource[i].completeTime || dataSource[i].completeTime == '') {
            this.$message.warning('请设置入货位时间！！！')
            return false
          }
          let orderStockInLists = dataSource[i].orderStockInLists
          if (!orderStockInLists || orderStockInLists.length == 0) {
            this.$message.warning('请分配货位！！！')
            return false
          } else {
            let qty = dataSource[i].productActualQty
            let disQty = 0
            for (let j = 0; j < orderStockInLists.length; j++) {
              disQty += orderStockInLists[j].productActualQty
            }
            console.log(disQty)
            if (qty != disQty) {
              console.log("222222222")
              this.$message.warning('实际数量和入库详情数量必须相等！！！')
              return false
            }
          }
          dataSource.status = 'normal'
        }
        return true
      },
      /** 当点击新增按钮时调用此方法 */
      addInOrder() {
        this.seeanable = true
        if (typeof this.addBefore === 'function') this.addBefore()
        // 默认新增空数据
        let rowNum = this.addDefaultRowNum
        if (typeof rowNum !== 'number') {
          rowNum = 1
          console.warn('由于你没有在 data 中定义 addDefaultRowNum 或 addDefaultRowNum 不是数字，所以默认添加一条空数据，如果不想默认添加空数据，请将定义 addDefaultRowNum 为 0')
        }
        this.eachAllTable((item) => {
          item.add(rowNum)
        })
        if (typeof this.addAfter === 'function') this.addAfter(this.model)
        this.editAndAdd({})
      },
      /** 当点击了编辑（修改）按钮时调用此方法 */
      editAndAdd(record) {
        if (typeof this.editBefore === 'function') this.editBefore(record)
        this.form.resetFields()
        this.model = Object.assign({}, record)
        var now = "CGRK" + moment().format("YYYYMMDDHHmmss")
        this.model.orderInId = now
        this.visible = true
        this.activeKey = this.refKeys[0]
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'orderInId'))
        })
        if (typeof this.editAfter === 'function') this.editAfter(this.model)
      },
    },
    computed: {
      userName: function() {
        let userName = store.getters.userInfo.realname
        console.log(userName, store)
        return userName
      }
    }
  }
</script>

<style scoped>
</style>