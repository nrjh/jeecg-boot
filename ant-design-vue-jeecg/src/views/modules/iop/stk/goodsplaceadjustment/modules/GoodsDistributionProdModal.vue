<template>
  <div>
    <a-modal
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :footer="null"
      @ok="handleAddOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.partnerName}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="保管员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.createName}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="物料" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.productName}}</span>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.cate}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.attr}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="待入库件数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.productActualQty}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="价值金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <span>{{orderInfo.priceTotal}}</span>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 这里加上添加物料弹出框的自定义表单-->

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
        >
          <template slot="warehouse" slot-scope="text,record,index">
            <a-cascader v-if="editable"
                        :field-names="{ label: 'name', value: 'id', children: 'children' }"
                        :options="location"
                        placeholder="选择仓库位置"
                        width="100%"
                        v-model="dataSource[index].warehouse"
            />
            <span width="100%" v-else>{{dataSource[index].whInputStockLotName}}</span>
          </template>

          <template slot="productActualQty" slot-scope="text,record,index">
            <a-input-number v-if="editable" v-model="dataSource[index].productActualQty"/>
            <span v-else>{{dataSource[index].productActualQty}}</span>
          </template>

          <template slot="action" slot-scope="text,record,index">
            <a-popconfirm v-if="editable" title="确定删除吗?" @confirm="() => handleDelete(index)">
              <a>删除</a>
            </a-popconfirm>
          </template>

        </a-table>

        <div class="table-operator">
           <span style="overflow: hidden; padding-left: 40%;" class="table-page-search-submitButtons">
             <template v-if="editable">
             <a-button @click="handleCleanAll" type="primary">清空货位列表</a-button>
             <a-button @click="handleAddLoc" type="primary">继续添加货位</a-button>
             <a-button @click="handleAddOk" type="primary">保存</a-button>
             </template>
             <a-button @click="handleCancel" type="primary">关闭</a-button>
           </span>
        </div>

      </a-spin>
    </a-modal>
  </div>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  import { getUserDep } from '@/api/api'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { translateDataToTree, getIdPath } from '../util/tree'
  import JDate from '@/components/jeecg/JDate'
  import ATextarea from 'ant-design-vue/es/input/TextArea'
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JMultiSelectTagIop from '@/components/dict/JMultiSelectTagIop'
  import { initDictOptions, initDictOptionsIop, filterDictText } from '@/components/dict/JDictSelectUtil'
  import {initDictOptionsOra} from "../../../../../../components/dict/JDictSelectUtil";

  export default {
    name: 'goodsDistributionProdModal',
    components: {
      ATextarea, JDate, JDictSelectTagIop, JDictSelectTag, JMultiSelectTagIop
    },
    data() {
      return {
        title: '操作',
        width: 800,
        visible: false,
        isHavingValue: true,
        editable: true,
        dataSource: [],
        labelCol: {
          span: 8
        },
        wrapperCol: {
          span: 16
        },
        orderInfo: {},
        test: 12,
        columns: [
          {
            title: '仓库和大区',
            key: 'warehouse',
            width: '70%',
            scopedSlots: { customRender: 'warehouse' }
          },
          {
            title: '件数',
            key: 'productActualQty',
            scopedSlots: { customRender: 'productActualQty' }
          },
          {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' }
          }

        ],
        location: [],
        locationOld: [],
        confirmLoading: false,
        url: {
          list: '/iop/order/orderPlan/wlList',
          locList: '/stk/in/stockInOrder/locList'
        }
      }
    },
    beforeCreate() {
      this.form = this.$form.createForm(this, { name: 'dynamic_form_item' })
      this.form.getFieldDecorator('keys', { initialValue: [], preserve: true })
    },
    created() {
      getAction(this.url.locList).then(res => {
        this.locationOld = res.result
        this.location = translateDataToTree(res.result)
      })
    },
    methods: {
      add(orderInfo, editable) {
        console.log("是否可编辑:",editable)
        console.log("orderInfo:",orderInfo)
        this.visible = true
        this.editable = editable ? true : false
        this.orderInfo = Object.assign({cate:"",attr:"",partnerName:""}, orderInfo);
        //查询数据字典
        // 规格
        initDictOptionsIop('prd_attribute_category,name,id,id="' + orderInfo.attributeCategoryId + '"').then(res => {
          if (res.result.length != 0) {
            this.orderInfo.attr = res.result[0].title
          }
        })
        //查询数据字典
        initDictOptions('vendorCode').then(res => {
          if(! res.result === null ){
              for (var i = 0; i < res.result.length; i++){
                  if (orderInfo.partnerid == res.result[i].value){
                      this.orderInfo.partnerName = res.result[i].title
                      break;
                  }
              }
          }
        })
        //查询字典
        initDictOptions('CATEGORY_TYPE').then(res => {
          for (var i = 0; i < res.result.length; i++){
            if (orderInfo.categoryType == res.result[i].value){
              this.orderInfo.cate = res.result[i].title
              break;
            }
          }
        })
        if(orderInfo.whInputStockLotName){
            console.log(orderInfo.whInputStockLotName)
            console.log(orderInfo.whInputStockLotId)
            let result = []
            let temp = orderInfo.whInputStockLotName.split(",")
            let tempOfId = orderInfo.whInputStockLotId.split(",")
            for(var i = 0;i<temp.length;i++){
                let tempList = {}
                let nameAndCount = temp[i].split(":");
                tempList.whInputStockLotName = nameAndCount[0]
                tempList.productActualQty = nameAndCount[1]
                let tempWarehouse = tempOfId[i].split(":")
                tempList.warehouse = tempWarehouse[0].split("-").map(Number)
                result.push(tempList)
            }
            console.log(result)
            this.dataSource = result ? this._.cloneDeep(result) : []
        }else {
            this.dataSource = []
        }
        // this.initDataSource()
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
      },
      handleCleanAll() {
        this.dataSource = []
        this.isHavingValue = false
      },
      handleAddLoc() {
        this.dataSource.push({ warehouse: [], productActualQty: 0 })
      },
      handleDelete(index) {
        this.dataSource.splice(index)
      },
      handleAddOk() {
        let total = 0
        for (let i = 0; i < this.dataSource.length; i++) {
          let whInputStockLotId = this.dataSource[i].warehouse[this.dataSource[i].warehouse.length - 1]
          let tempObj = this._.find(this.locationOld, { 'id': whInputStockLotId })
          this.dataSource[i].whInputStockLotId = whInputStockLotId
          this.dataSource[i].whInputStockLotName = tempObj.completeName
          total = total+parseInt(this.dataSource[i].productActualQty)
        }
        let number = parseInt(this.orderInfo.productActualQty)
        if (total > number) {
          this.$message.warning('各库房分配分配数量大于总数量！！！'+":"+total)
        } else {

          console.log(this.dataSource)
          this.$emit('returnData', this.dataSource, total < this.orderInfo.productActualQty ? 'normal' : 'freeze')
          this.close()
        }
      },
      // initDataSource() {
      //   for (let i = 0; i < this.dataSource.length; i++) {
      //     this.$set(this.dataSource[i], 'warehouse', this._.cloneDeepWith(getIdPath(this.locationOld, this.dataSource[i].whInputStockLotId)))
      //   }
      // }

    }
  }


</script>

<style scoped>
  .ant-cascader-picker {
    width: 100%;
  }
</style>