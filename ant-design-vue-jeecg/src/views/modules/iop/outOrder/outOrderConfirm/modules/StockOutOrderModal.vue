<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :maskClosable="false"
      :confirmLoading="confirmLoading"
      @ok="handleOkSave"
      @cancel="handleCancel">

      <template slot="footer">
        <a-button type="primary" @click="handleOkSave()">保存</a-button>
        <a-button type="primary" @click="handleOkSave('done')">提交</a-button>
        <a-button type="primary" @click="handleCancel">返回</a-button>
      </template>

      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="物料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-radio-group  v-decorator="['prdCategory', validatorRules.prdCategory]" disabled="disabled">
                  <a-radio value="equip">
                    备品/易耗品
                  </a-radio>
                  <a-radio value="spare">
                    办公用品
                  </a-radio>
                  <a-radio value="office_supplies">
                    工器具
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预计需求时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="预计需求时间" v-decorator="['datePlanned', validatorRules.datePlanned]"
                        disabled
                        :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list"   v-decorator="['lineId', validatorRules.lineId]"
                                       @inputText="inputTextAreaId"
                                       disabled
                                       :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                       @change="changeLine"/>
                <a-input v-show="false" v-decorator="['lineName']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="班组" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list"  v-decorator="['team', validatorRules.team]"
                                       disabled
                                       :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"/>
                <!--                <a-input v-show="false" v-decorator="['team']"></a-input>-->
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="设备名称" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-select   v-decorator="[ 'equipName', validatorRules.equipName]" placeholder="请选择" disabled>
                  <a-select-option v-for="item in equipTypeLists" :key="item.equipNo" :value="item.equipId">
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col>
              <a-form-item label="申请类型" :labelCol="{span:3}" :wrapperCol="{span:21}">
                <a-radio-group v-decorator="[ 'orderType', validatorRules.orderType]" disabled="disabled">
                  <a-radio value="urgency">
                    紧急
                  </a-radio>
                  <a-radio value="normal">
                    正常
                  </a-radio>
                  <a-radio value="order">
                    检修工单
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col>
              <a-form-item label="领料说明" :labelCol="{span:3}" :wrapperCol="{span:21}">
                <a-input v-decorator="[ 'note', validatorRules.note]"  placeholder="请输入说明" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

        <a-table
          ref="editableTable"
          size="middle"
          bordered
          rowKey="id"
          :loading='stockOutOrderLineTable.loading'
          :visible="true"
          :columns="stockOutOrderLineTable.columns"
          :dataSource="stockOutOrderLineTable.dataSource"
          :pagination="false"
        >
          <template slot="action" slot-scope="text,record,index">
            <a v-if="editable" @click="handleDistribution(index)">分配货位</a>
            <a v-else @click="handleDistribution(index)">查看货位</a>
          </template>
        </a-table>
      </a-spin>
      <DistributionOutProdModal ref="distributionProdFrom"
                               @distributionReturnData="distributionReturnData"></DistributionOutProdModal>
    </a-modal>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDate from '@/components/jeecg/JDate.vue'
  import DistributionOutProdModal from './DistributionOutProdModal'
  import { httpAction, getAction } from '@/api/manage'
  import { initDictOptionsOra,initDictOptionsIop } from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'

  export default {
    name: 'StockOutOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      DistributionOutProdModal,
      JDate,
      JDictSelectTagOra
    },
    data() {
      return {
        activeIndex: 0,
        editable: true,
        dataSourceProd: [],
        equipTypeLists: [],
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
        visibleProd: false,
        validatorRules: {},
        refKeys: ['stockOutOrderLine'],
        tableKeys: ['stockOutOrderLine'],
        activeKey: 'stockOutOrderLine',
        // 出库明细表
        stockOutOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物料名称',
              align: 'center',
              dataIndex: 'productName'
            },
            {
              title: '物料编号',
              align: 'center',
              dataIndex: 'defaultCode'
            },
            {
              title: '物料类别',
              align: 'center',
              dataIndex: 'categoryType',
              customRender: function(text) {
                if(text=='spare'){
                  return  '备品备件';
                }else if(text=='office_supplies'){
                  return '办公用品'
                }else if(text=='equip'){
                  return '生产设施'
                }
              }
            },
            // {
            //   title: '货位',
            //   align: 'center',
            //   dataIndex: 'orderStockInLists',
            //   customRender:function (text){
            //       var result = "";
            //       if(text.length>0){
            //         if(text.length > 1){
            //           return text[0].whInputStockLotName+"..."
            //         }else {
            //           return text[0].whInputStockLotName
            //         }
            //       }else {
            //         return  text
            //       }
            //   }
            // },
            {
              title: '规格',
              align: 'center',
              dataIndex: 'attributeCategoryId'
            },
            // {
            //   title: '合同编号',
            //   align: 'center',
            //   dataIndex: 'contract'
            // },
            {
              title: '单位',
              align: 'center',
              dataIndex: 'productUomId_dictText'
            },
            {
              title: '实际库存数量',
              align: 'center',
              dataIndex: 'productQty'
            },
            {
              title: '计划出库件数',
              align: 'center',
              dataIndex: 'productPlyQty'
            },
            {
              title: '剩余出库件数',
              align: 'center',
              dataIndex: 'remainQty',
              customRender: function(text, r, index) {
                return r.productPlyQty - r.productActualQty
              }

            },
            {
              title: '实际出库件数',
              align: 'center',
              dataIndex: 'productActualQty'
            },

            {
              title: '操作',
              key: 'action',
              width: '7%',
              scopedSlots: { customRender: 'action' }
            }
          ]
        },
        url: {
          add: '/outOrder/stockOutOrder/add',
          edit: '/outOrder/stockOutOrder/edit',
          out: '/outOrder/stockOutOrder/confirm/save',
          queryById: "/outOrder/stockOutOrder/queryById",
          stockOutOrderLine: {
            list: '/outOrder/stockOutOrder/queryStockOutOrderLineVoByMainId'
          },
          check: '/stk/in/stockInOrder/check'
        }
      }
    },
    methods: {
      inputTextAreaId(val){
        this.form.setFieldsValue({ lineName: val })
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter(v) {
        getAction(this.url.queryById, {id: v.id}).then((res) => {
          if (res.success) {
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'prdCategory', 'datePlanned', 'orderType',
                'note','lineId','equipName','team'))
            })
          }
        });
        // 加载子表数据
        this.editable = true
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.stockOutOrderLine.list, params, this.stockOutOrderLineTable)

          getAction(this.url.check, { id: this.model.id }).then(res => {
            this.checkList = res.result
            console.log(this.checkList)
          })
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          stockOutOrderLineList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'orderOutId', 'createOutName', 'createOutTime', 'prdCategory', 'belongEquip', 'team', 'orderType', 'datePlanned', 'note', 'state', 'updateInBy', 'updateInName', 'updateInTime', 'outState'))
      },
      read(record) {
        this.edit(record)
        this.editable = false
      },
      handleDistribution(index) {
        let proInfo = this.stockOutOrderLineTable.dataSource[index]
        let orderInfo = {}
        orderInfo.partnerid = this.model.partnerId
        orderInfo.createName = this.userName
        orderInfo.productName = proInfo.productName
        orderInfo.defaultCode = proInfo.defaultCode
        orderInfo.categoryType = proInfo.categoryType
        orderInfo.attributeCategoryId = proInfo.attributeCategoryId
        orderInfo.productActualQty = proInfo.productQty
        orderInfo.priceTotal = proInfo.priceUnit * proInfo.productQty
        orderInfo.orderId = proInfo.orderId
        orderInfo.productId = proInfo.productId
        orderInfo.defaultCode = proInfo.defaultCode
        orderInfo.orderStockInLists = this.stockOutOrderLineTable.dataSource[index].orderStockInLists
        this.activeIndex = index
        this.$refs.distributionProdFrom.add(orderInfo, this.editable)
      },
      // distributionReturnData(data, status) {
      //   this.stockOutOrderLineTable.dataSource[this.activeIndex].orderStockInLists = data
      //   if (data && data.length > 0) {
      //     let productActualQty = 0
      //     for (let i = 0; i < data.length; i++) {
      //       productActualQty += parseInt(data[i].productActualQty)
      //     }
      //     this.stockOutOrderLineTable.dataSource[this.activeIndex].productActualQty = productActualQty
      //   }
      //   this.stockOutOrderLineTable.dataSource[this.activeIndex].status = status
      // },
      distributionReturnData(data){
          this.stockOutOrderLineTable.dataSource = [];
          for(let d of data){
              this.stockOutOrderLineTable.dataSource.push(d);
          }
          // this.stockOutOrderLineTable.dataSource = data;
      },
      // 保存货位信息
      handleOkSave(state) {
        if (!this.validateDistributionData()) {
          return false
        }
        let pordData = this.stockOutOrderLineTable.dataSource
        for(let item of pordData){
          if(item.productActualQty != item.productPlyQty){
            this.$message.warning("实际出库库存和计划出库不相同，请重新填写")
            return
          }
        }
        let obj = { stockOutOrderLineList: pordData }
        if (state) {

          obj.state = state
          obj.outState = 'normal'
        }
        this.confirmLoading = true
        let httpurl = this.url.out
        let method = 'put'
        let formData = Object.assign(this.model, obj)
        httpAction(httpurl, formData, 'put').then((res) => {
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
      },
      validateDistributionData() {
        let dataSource = this.stockOutOrderLineTable.dataSource
        for (let i = 0; i < dataSource.length; i++) {
          dataSource[i].productActualQty
          // if (dataSource[i].productActualQty != dataSource[i].productQty) {
          //   this.$message.warning('请分配货位！！！')
          //   return false
          // }
          // if(dataSource[i].orderStockInLists[0].whInputStockLotName == null){
          //     this.$message.warning('请分配货位！！！')
          //     return false
          // }
          dataSource.status = 'normal'
        }
        return true
      },

      changeLine(value){
        this.equipTypeLists=[];
        this.form.setFieldsValue({equipType:''})
        this.form.setFieldsValue({equipName:''})
        this.form.setFieldsValue({equipId:'请选择'})
        let equipVaule='';
        let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
          equipVaule=res.result[0].value;
          if(equipVaule != null){
            getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
              this.equipTypeLists=res.result;
            })
          }
        })
      },

    }
  }
</script>

<style scoped>
</style>