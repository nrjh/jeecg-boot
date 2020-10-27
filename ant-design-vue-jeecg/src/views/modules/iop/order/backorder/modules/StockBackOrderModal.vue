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
            <a-form-item label="退回入库单" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input v-decorator="[ 'orderBackId', validatorRules.orderBackId]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="申请日期" v-decorator="[ 'createTime', validatorRules.createTime]"
                      :trigger-change="true" style="width: 100%" disabled/>
            </a-form-item>
          </a-col>
<!--          <a-col :span="12">-->
<!--            <a-form-item label="检定线" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              &lt;!&ndash;<a-input placeholder="" v-model="queryParam.orderBackId"></a-input>&ndash;&gt;-->
<!--              <j-dict-select-tag type="list"-->
<!--                                 v-decorator="[ 'lineNo', validatorRules.lineNo]"-->
<!--                                 v-model="type"-->
<!--                                 dict-code="IOP_PUB_CATEGORY"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-item label="班组" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <JDictSelectTag v-model="team"-->
<!--                              placeholder=""-->
<!--                              dictCode="sys_user,realname,realname" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="12">
            <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<JDictSelectTag   v-decorator="['orderType', validatorRules.orderType]" :trigger-change="true" dicCode="orderType"  placeholder="请选择退还类型"></JDictSelectTag>-->
              <JDictSelectTag v-decorator="[ 'orderType', validatorRules.orderType]" :triggerChange="true" placeholder="请选择退还类型" dictCode="order_type" ></JDictSelectTag>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="领料单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <!--<j-search-select-tag-iop v-decorator="['orderOutId', validatorRules.orderOutId]" dictCode="stock_out_order,order_out_id,id"  />-->

<!--              <JDictSelectTagIop v-decorator="['orderOutId', validatorRules.orderOutId]"-->
<!--                              :triggerChange="true"-->
<!--                              placeholder=""-->
<!--                              dictCode="stock_out_order,order_out_id,id,state='done'"-->
<!--                              @change="handleChange"/>-->
              <a-input  v-model="chooseOrderOutId" placeholder="请输入领料单号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="2" style="margin: 3px auto;margin-left: 5px;">
            <a-button @click="handleAddPrd" type="primary" icon="plus">选择</a-button>
          </a-col>
<!--          <a-col :span="12">-->
<!--            <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date v-decorator="[ 'createTime', validatorRules.createTime]" placeholder=""></j-date>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="说明" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-textarea v-decorator="['note', validatorRules.note]" rows="4" placeholder="请输入说明"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <!--<a-tabs v-model="activeKey" @change="handleChangeTabs">-->
        <!--<a-tab-pane tab="退回入库明细" :key="refKeys[0]" :forceRender="true">-->
          <!--<stock-back-order-line-form ref="stockBackOrderLineForm" @validateError="validateError"></stock-back-order-line-form>-->
        <!--</a-tab-pane>-->
      <!---->
      <!--</a-tabs>-->

      <!-- 操作按钮区域 -->
<!--      <div class="table-operator">-->
<!--        <a-button @click="handleAddProd" type="primary" icon="plus">添加物料</a-button>-->
<!--      </div>-->

      <!-- 这里加上添加物料弹出框的自定义表单-->
      <a-table
        ref="editableTable"
        size="middle"
        bordered
        rowKey="id"
        :visible="visibleProd"
        :columns="columns"
        :dataSource="dataSourceProd"
      >
          <template slot="returnCount" slot-scope="text,record,index">
<!--            <a-input-number v-model="dataSourceProd[index].returnCount" @change="changeInputNum(index)" :min="0" :max="dataSourceProd[index].productActualQty"/>-->
            <a-input-number v-model="dataSourceProd[index].returnCount" :min="0" :max="dataSourceProd[index].productActualQty"/>
          </template>

          <span  slot="action" slot-scope="record">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                      <a>删除</a>
                </a-popconfirm>

          </span>
      </a-table>


      <!-- 操作按钮区域 -->
      <div class="table-operator" style="text-align: center;">
        <a-button @click="handleOkCommit(1)" type="primary" icon="plus">保存</a-button>
        <a-button @click="handleOkCommit(2)" type="primary" icon="plus">提交</a-button>
        <a-button @click="handleCancel" type="primary" icon="plus">关闭</a-button>
      </div>

    </a-spin>
  </a-modal>
    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>
    <related-prd ref="relatedPrdRef" @relatedPrd="relatedPrd"></related-prd>
  </div>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  import JSearchSelectTagIop from '@/components/dict/JSearchSelectTagIop'
  import addProdModal from '../../orderplan/modules/addProdModal.vue'
  import RelatedPrd from './RelatedPrd'
  import JDate from "../../../../../../components/jeecg/JDate";
  import { httpAction , getAction } from '@/api/manage'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import moment from 'moment'
  import store from '@/store'

  export default {
    name: 'StockBackCoOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      JSearchSelectTag,
      addProdModal,
      JDate,
      RelatedPrd,
      JSearchSelectTagIop,
      JDictSelectTagIop,
      JDictSelectTagOra,
      ATextarea
    },
    data() {
      return {
        chooseOrderOutId:"",
        type:'',
        team:'',
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
          lineNo: {rules: [
          ]},
        },
        columns: [
          {
            title:'物料名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'物料编号',
            align:"center",
            dataIndex: 'defaultCode'
          },
          {
            title:'类别',
            align:"center",
            dataIndex: 'office_supplies',
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
            title:'规格',
            align:"center",
            dataIndex: 'attributeCategoryId_dictText',
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'productUomId_dictText',
          },
          {
            title:'入库件数',
            align:"center",
            dataIndex:'productActualQty',
          },
          {
              title:'退回件数',
              align:"center",
              dataIndex:'returnCount',
              scopedSlots: { customRender: 'returnCount' }
          },
          {
            title: '操作',
            key: 'action',
            width: '6%',
            scopedSlots: { customRender: 'action' }
          }
        ],
        refKeys: ['stockBackOrderLine', ],
        tableKeys:[],
        activeKey: 'stockBackOrderLine',
        // 退回入库明细
        stockBackOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
          ]
        },
        url: {
          add: "/backorder/stockBackOrder/add",
          edit: "/backorder/stockBackOrder/edit",
          queryById: "/backorder/stockBackOrder/queryById",
          queryStockBackOrderLineByMainId: "/backorder/stockBackOrder/queryStockBackOrderLineByMainId",
          queryStockOutOrderLineByMainId: "/outOrder/stockOutOrder/queryStockOutOrderLineByMainId",
        }
      }
    },
    methods: {
      // changeInputNum(index){
      //     console.log(this.dataSourceProd[index].productActualQty)
      //     console.log(this.dataSourceProd[index].returnCount)
      //     this.dataSourceProd[index].productActualQty = this.dataSourceProd[index].productActualQty - this.dataSourceProd[index].returnCount
      // },
      relatedPrd(t){
          this.handleChange(t.id)
          this.chooseOrderOutId = t.orderOutId
          console.log("点击的数据---",t)
      },
      handleAddPrd(){
          this.$refs.relatedPrdRef.show();
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          stockBackOrderLineList: this.$refs.stockBackOrderLineForm.getFormData(),
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'createOutName','updateInName','orderBackId','team','orderType','orderOutId','note'))
     },
     handleAddProd(){
        this.$refs.addProdFrom.add();
      },
      returnData(data){
        this.visibleProd=true;
        this.dataSourceProd=data;
      },
      handleOkCommit(v){
        // let pordData=this.$refs.editableTable.getValuesSync().values;
        // let pordData = this.dataSourceProd
        let pordData = []
        for(var i = 0 ;i < this.dataSourceProd.length;i++){
            let tempList = {}
            // 规格id
            tempList.attributeCategoryId = this.dataSourceProd[i].attributeCategoryId
            // 物料编码
            tempList.productNo = this.dataSourceProd[i].defaultCode
            // 物料id
            tempList.productId = this.dataSourceProd[i].productId

            // 物料名称
            tempList.name = this.dataSourceProd[i].productName

            // 物品类型
            tempList.categoryType = this.dataSourceProd[i].categoryType
            // 单位id
            tempList.productUomId_dictText = this.dataSourceProd[i].productUomId
            tempList.productUomId = this.dataSourceProd[i].productUomId
            // 实际需求量
            tempList.productActualQty = this.dataSourceProd[i].productActualQty
            tempList.returnCount = this.dataSourceProd[i].returnCount
            pordData.push(tempList)
        }
        const that = this;
        // 触发表单验证
        let saveFlag={
          saveFlag:v
        }
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            let obj={
              prodAndStockVoList :pordData
            };
            let modelEdit=[];
            let formData=[];
            obj= Object.assign(obj,saveFlag);
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
              formData = Object.assign(obj, values);
            }else{
              httpurl+=this.url.edit;
              method = 'put';
              modelEdit = Object.assign(this.model, values);
              formData = Object.assign(obj, modelEdit);
            }
            formData= Object.assign(formData,saveFlag);
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
            this.visible = true;
            this.chooseOrderOutId = res.result.orderBackId
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'team','createTime','orderOutId','orderType','note','returnCount'))
            })
          }
        });
        getAction(this.url.queryStockBackOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.dataSourceProd = []
                // = res.result;
            for(var i = 0 ;i < res.result.length;i++){
                let tempList = {}
                // 规格id
                tempList.attributeCategoryId = res.result[i].attributeCategoryId
                // 物料编码
                tempList.defaultCode = res.result[i].productNo
                // 物料id
                tempList.productId = res.result[i].productId

                // 物料名称
                tempList.productName = res.result[i].name

                // 物品类型
                tempList.categoryType = res.result[i].categoryType
                // 单位id
                tempList.productUomId = res.result[i].productUomId
                // 实际需求量
                tempList.productActualQty = res.result[i].productActualQty

                // 退回数量
                tempList.returnCount = res.result[i].returnCount
                this.dataSourceProd.push(tempList)
            }

          }
        });
      },
      add() {
        this.form.resetFields();
        this.chooseOrderOutId = "";
        var now = moment().format("YYYY-MM-DD")
        var backId = "THRK" + moment().format("YYYYMMDDHHmmss")
        let userName = store.getters.userInfo.realname
        this.model.createTime = now
        console.log(this.model.createTime)
        this.model.orderBackId = backId
        this.model.createOutName = userName
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'createTime','createOutName','orderBackId',))
        })
      },
      handleChange(value) {
        console.log(`selected ${value}`);
        getAction(this.url.queryStockOutOrderLineByMainId,{id:value}).then((res)=>{
          if(res.success){
            console.log("物料信息1111111111111",res.result)
            this.dataSourceProd=res.result;
          }
        });
      },
      handleDelete(recond){
        console.log("recond------",recond)
        var newData =this._.remove(this.dataSourceProd, function(n) {
          return recond.id==n.id;
        });
        this.dataSourceProd=this._.cloneDeep(this.dataSourceProd)
        console.log("this.dataSourceProd------",this.dataSourceProd)
        // this.dataSourceProd=dataTemp;
      }
    }
  }
</script>

<style scoped>
</style>