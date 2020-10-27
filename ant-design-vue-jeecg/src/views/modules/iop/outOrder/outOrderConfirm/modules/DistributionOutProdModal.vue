<template>
  <div>
    <a-modal
      :width="800"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :footer="null"
      @ok="handleAddOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <!--主表单区域-->
        <a-form :form="form">
          <a-row >
            <a-col :span="12">
              <a-form-item label="物料名称" :labelCol="{span:6}" :wrapperCol="{span:16}">
                <a-input v-decorator="[ 'productName', validatorRules.productName]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="物料编号" :labelCol="{span:6}" :wrapperCol="{span:16}">
                <a-input v-decorator="[ 'defaultCode', validatorRules.defaultCode]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="12">
              <a-form-item label="规格" :labelCol="{span:6}" :wrapperCol="{span:16}">
                <a-input v-decorator="[ 'attributeCategoryId', validatorRules.attributeCategoryId]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="数量" :labelCol="{span:6}" :wrapperCol="{span:16}">
                <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

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
            <span v-else>{{dataSource[index].whInputStockLotName}}</span>
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


  export default {
    name: 'distributionProdModal',
    components: {
      ATextarea, JDate, JDictSelectTagIop, JDictSelectTag, JMultiSelectTagIop
    },
    data() {
      return {
        title: '操作',
        width: 800,
        selectedRowKeys:[],
        selectedRows:[],
        visible: false,
        editable: true,
        dataSource: [],
        validatorRules: {
          orderOutId: {rules: [
            ]},
          defaultCode: {rules: [
            ]},
          team: {rules: [
            ]},
          orderType: {rules: [
            ]},
          orderOutId: {rules: [
            ]},
        },
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 18
        },
        orderInfo: {},
        test: 12,
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'productName'
          },
          {
            title: '物料编号',
            align: 'center',
            key: 'defaultCode',
            customRender:function (text) {
                return text == '' ? '' : text.defaultCode
            }
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
          {
            title: '货位',
            align: 'center',
            dataIndex: 'orderStockInLists',
            customRender:function (text){
                var result = "";
                // for (let i = 0; i < text.length; i++) {
                //     var localName = text[i].whInputStockLotName;
                //     result += localName+"     ";
                // }
                // return result+"";
              if(text.length>0){
                if(text.length > 1){
                  return text[0].whInputStockLotName+"..."
                }else {
                  return text[0].whInputStockLotName
                }
              }else{
                return  text
              }
            }
          },
          {
            title: '规格',
            align: 'center',
            dataIndex: 'attributeCategoryId'
          },
          {
            title: '合同编号',
            align: 'center',
            dataIndex: 'contract'
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'productUomId_dictText'
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
              return r.virtualQty - r.productActualQty
            }

          },
          {
            title: '实际出库件数',
            align: 'center',
            key: 'productActualQty',
            dataIndex: 'productActualQty',
            scopedSlots: {customRender:'productActualQty'}
          },

          {
            title: '操作',
            key: 'action',
            width: '7%',
            scopedSlots: { customRender: 'action' }
          }
        ],
        location: [],
        locationOld: [],
        confirmLoading: false,
        url: {
          list: '/iop/order/orderPlan/wlList',
          locList: '/stk/in/stockInOrder/locList',
          queryGoodsList:'/outOrder/stockOutOrder/queryStockOutOrderGoods'
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
        //  清空数据
        this.dataSource = [];
        this.visible = true
        this.editable = editable ? true : false
        this.orderInfo = orderInfo
        // this.dataSource = orderInfo.orderStockInLists ? this._.cloneDeep(orderInfo.orderStockInLists) : []
        getAction(this.url.queryGoodsList,{outOrderId:orderInfo.orderId,productId:orderInfo.productId,productCode:orderInfo.defaultCode}).then((res)=>{
          if(res.success){
              for(let outOrderVO of res.result){
                  this.dataSource.push(outOrderVO)
              }
            }
        })
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
      },
      handleAddLoc() {
        this.dataSource.push({ warehouse: [], productActualQty: 0 })
      },
      handleDelete(index) {
        this.dataSource.splice(index)
      },
      handleAddOk() {
        // let total = 0
        // for (let i = 0; i < this.dataSource.length; i++) {
        //   let whInputStockLotId = this.dataSource[i].warehouse[this.dataSource[i].warehouse.length - 1]
        //   let tempObj = this._.find(this.locationOld, { 'id': whInputStockLotId })
        //   this.dataSource[i].whInputStockLotId = whInputStockLotId
        //   this.dataSource[i].whInputStockLotName = tempObj.completeName
        //   total += this.dataSource[i].productActualQty
        // }
        // if (total > this.orderInfo.productActualQty) {
        //   this.$message.warning('各库房分配分配数量大于总数量！！！')
        // } else {
        //   this.$emit('returnData', this.dataSource, total < this.orderInfo.productActualQty ? 'freeze' : 'normal')
        //   this.close()
        // }

          // 封装获取选中行的值
          // let selectedData=[];
        // console.log(this.selectionRows);
        // for (let item of this.selectionRows) {
        //     // this.dataSource.forEach(function(record,index){
        //     //     if(index==item){
        //     //         selectedData.push(record);
        //     //     }
        //     // })
        //     selectedData.push(item);
        // }
          this.close();
          console.log(this.dataSource)
          this.$emit("distributionReturnData",this.dataSource);
      },
      // initDataSource() {
      //   for (let i = 0; i < this.dataSource.length; i++) {
      //     this.$set(this.dataSource[i], 'warehouse', this._.cloneDeepWith(getIdPath(this.locationOld, this.dataSource[i].whInputStockLotId)))
      //   }
      // },
      handleChange(value) {
        console.log(`selected ${value}`);
        getAction(this.url.queryStockOutOrderLineByMainId,{id:value}).then((res)=>{
          if(res.success){
            // console.log("物料信息1111111111111",res.result)
            this.dataSourceProd=res.result;
          }
        });
      },
      onSelectChange(selectedRowKeys,selectionRows) {
          console.log('selectedRowKeys changed: ', selectionRows);
          this.selectedRowKeys = selectedRowKeys;
          this.selectionRows = selectionRows;
      }

    }
  }


</script>

<style scoped>
  .ant-cascader-picker {
    width: 100%;
  }
</style>