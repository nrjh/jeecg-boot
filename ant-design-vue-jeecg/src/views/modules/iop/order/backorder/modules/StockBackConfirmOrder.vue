<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :maskClosable="false"
      :confirmLoading="confirmLoading"
      :footer="null"
      @cancel="handleCancel"
    >
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row>
            <a-col :span="8">
              <a-form-item label="退回入库单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'orderBackId', validatorRules.orderBackId]"  disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="退回人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]"  disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date v-decorator="[ 'createTime', validatorRules.createTime]" placeholder="" disabled="disabled" style="width: 100%"></j-date>
              </a-form-item>
            </a-col>
<!--            <a-col :span="8">-->
<!--              <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <JDictSelectTag v-decorator="['team', validatorRules.team]"-->
<!--                                :triggerChange="true"-->
<!--                                placeholder=""-->
<!--                                disabled="disabled"-->
<!--                                dictCode="sys_user,username,id" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--            <a-col :span="8">-->
<!--              <a-form-item label="班组" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <JDictSelectTag v-decorator="['team', validatorRules.team]"-->
<!--                                :triggerChange="true"-->
<!--                                placeholder=""-->
<!--                                disabled="disabled"-->
<!--                                dictCode="sys_user,username,id" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <!--<JDictSelectTag   v-decorator="['orderType', validatorRules.orderType]" :trigger-change="true" dicCode="orderType"  placeholder="请选择退还类型"></JDictSelectTag>-->
                <JDictSelectTag v-decorator="[ 'orderType', validatorRules.orderType]" :triggerChange="true" placeholder="请选择退还类型" dictCode="order_type" disabled="disabled" ></JDictSelectTag>

              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="领料单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <!--<j-search-select-tag-iop v-decorator="['orderOutId', validatorRules.orderOutId]" dictCode="stock_out_order,order_out_id,id"  />-->

                <JDictSelectTagIop v-decorator="['orderOutId', validatorRules.orderOutId]"
                                   :triggerChange="true"
                                   placeholder=""
                                   disabled="disabled"
                                   dictCode="stock_out_order,order_out_id,id" />
              </a-form-item>
            </a-col>

<!--            <a-col :span="8">-->
<!--              <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="8">-->
<!--              <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="24">
              <a-form-item label="说明" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                <a-textarea v-decorator="['note', validatorRules.note]" rows="4" disabled="disabled" placeholder="请输入说明"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <!-- 这里加上添加物料弹出框的自定义表单-->
        <a-table
          ref="editableTable"
          size="middle"
          bordered
          rowKey="id"
          :loading='stockBackOrderLineTable.loading'
          :visible="true"
          :columns="stockBackOrderLineTable.columns"
          :dataSource="stockBackOrderLineTable.dataSource"
        >
          <template slot="partnerId" slot-scope="text,record,index">
            <j-dict-select-tag style="width: 100%" v-if="editable" placeholder="请选择供应商" v-model="stockBackOrderLineTable.dataSource[index].partnerId" dictCode="vendorCode"/>
            <span v-else>{{stockBackOrderLineTable.dataSource[index].partnerId}}</span>
          </template>
          <template slot="action" slot-scope="text,record,index">
            <template v-if="editable">
              <a @click="handleDistribution(index)">分配货位</a>
            </template>
            <a v-else @click="handleDistribution(index)">查看货位</a>
          </template>
        </a-table>

        <br>
        <a-form :form="checkForm" v-if="checkShow">
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
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="text-align: center;">
          <a-button @click="handleOkCommit(1)" type="primary" icon="plus">提交</a-button>
          <a-button @click="handleCancel" type="primary" icon="plus">关闭</a-button>
        </div>
      </a-spin>
    </a-modal>
    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>
    <distribution-prod-modal ref="distributionProdFrom" @returnData="distributionReturnData"></distribution-prod-modal>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JSearchSelectTagIop from '@/components/dict/JSearchSelectTagIop'
  import addProdModal from '../../orderplan/modules/addProdModal.vue'
  import JDate from "../../../../../../components/jeecg/JDate";
  import { httpAction , getAction } from '@/api/manage'
  import DistributionProdModal from '../../../stk/in/modules/DistributionProdModal.vue'
  import AInput from "ant-design-vue/es/input/Input";
  import AInputSearch from "ant-design-vue/es/input/Search";



  export default {
    name: 'StockBackConfirmOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      AInputSearch, AInput, JDictSelectTag,
      JSearchSelectTag,
      addProdModal,
      JDate,
      JSearchSelectTagIop,
      JDictSelectTagIop,
      DistributionProdModal
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
        visibleProd:false,
        dataSourceProd:[],
        editable: true,
        checkable: false,
        checkShow: false,
        isStockIn: false,
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          updateInName: {rules: [
          ]},
          orderBackId: {rules: [
          ]},
          team: {rules: [
          ]},
          orderType: {rules: [
          ]},
          orderOutId: {rules: [
          ]},
          note: {rules: [
          ]},
          createTime: {rules: [
          ]},
        },

        refKeys: ['stockBackOrderLine', ],
        tableKeys:[],
        activeKey: 'stockBackOrderLine',
        // 退回入库明细
        stockBackOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title:'物料名称',
              align:"center",
              key: 'name',
              dataIndex: 'name',
            },
            {
              title:'物料编号',
              align:"center",
              key: 'productNo',
              dataIndex: 'productNo',
            },
            {
              title:'类别',
              align:"center",
              key: 'categoryId',
              dataIndex: 'categoryId',
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
              title:'物品供应商',
              align:"center",
              key: 'partnerId',
              dataIndex: 'partnerId',
              scopedSlots: { customRender: 'partnerId' },
            },
            {
              title:'规格',
              align:"center",
              key: 'attributeCategoryId',
              dataIndex: 'attributeCategoryId',
            },
            {
              title:'单位',
              align:"center",
              // key: 'productUomId',
              dataIndex: 'productUomId',
              dictCode:"UOM_UOM",
              disabled:true,
            },
            {
              title:'入库件数',
              align:"center",
              // key:'productUomQty',
              dataIndex:'productActualQty',
              allowInput: true
            },
            {
              title:'退回件数',
              align:"center",
              dataIndex:'returnCount',
              allowInput: true,
              defaultValue: '0',
            },
            {
              title:'入库详情',
              align:"center",
              key:'status',
              dataIndex: 'status',
              customRender: function(text, r, index) {
                  console.log("r",r)
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
              width: '7%',
              scopedSlots: { customRender: 'action' }
            },
          ],
        },
        url: {
          add: "/backorder/stockBackConfirmOrder/add",
          edit: "/backorder/stockBackOrder/edit",
          queryById: "/backorder/stockBackOrder/queryById",
          queryStockBackOrderLineByMainId: "/backorder/stockBackOrder/queryStockBackOrderLineByMainId",
        }
      }
    },
    methods: {
      handleAddProd(){
        this.$refs.addProdFrom.add();
      },
      returnData(data){
        this.visibleProd=true;
        this.dataSourceProd=data;
      },
      handleOkCommit(v){
        let pordData=this.stockBackOrderLineTable.dataSource
        console.log("---pordData----",pordData)
        const that = this;
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = this.url.add
            let method = 'post';
            let formData=[];
//            let obj={
//              stockInOrderLineList :pordData
//            };
            formData= Object.assign(formData,pordData);
            console.log("表单提交数据",formData);
            console.log("表单提交数据httpurl",httpurl);
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
                that.$emit('searchQuery');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      close(){
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.dataSourceProd=[];
        this.$emit('onClear');
      },
      handleCancel(){
        this.close();
      },
      editOrder (v) {
        console.log("v------",v);
        this.form.resetFields();
        getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            console.log("this.model",this.model)
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'createOutName','orderBackId','createTime','team','createTime','orderOutId','orderType','note'))
            })
          }
        });
        getAction(this.url.queryStockBackOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.stockBackOrderLineTable.dataSource=res.result;
          }
        });
      },

      handleDistribution(index) {
        console.log("index",index)
        let pordData=this.stockBackOrderLineTable.dataSource[index]
        console.log("pordData",pordData)
        let proInfo = this.stockBackOrderLineTable.dataSource[index]
        let orderInfo = {}
        orderInfo.partnerid = pordData.partnerId
        orderInfo.createName = this.userName
        orderInfo.productName = pordData.name
        orderInfo.categoryType = pordData.categoryId
        orderInfo.attributeCategoryId = pordData.attributeCategoryId
        orderInfo.productActualQty = pordData.productUomQty
        orderInfo.orderStockInLists = this.stockBackOrderLineTable.dataSource[index].orderStockInLists
        this.activeIndex = index
        this.$refs.distributionProdFrom.add(orderInfo, this.editable)
      },
      distributionReturnData(data, status) {
        this.stockBackOrderLineTable.dataSource[this.activeIndex].orderStockInLists = data
        this.stockBackOrderLineTable.dataSource[this.activeIndex].status = ""
      },
    }
  }
</script>

<style scoped>
</style>