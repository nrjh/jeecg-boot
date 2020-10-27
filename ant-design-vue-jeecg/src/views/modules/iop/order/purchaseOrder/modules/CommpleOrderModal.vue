<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :maskClosable="false"
      :confirmLoading="confirmLoading"
      @ok="handleOkCommple"
      @cancel="handleCancelCommple">
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row>
            <a-col :span="8">
              <a-form-item label="订单编号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'pickingNo', validatorRules.pickingNo]" placeholder="订单号" v-bind:disabled="true"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="到货日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-date v-decorator="[ 'datePlanned', validatorRules.datePlanned]" placeholder="请输入预计到货时间"></j-date>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="采购员" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'createName', validatorRules.createName]" placeholder="请输入" v-bind:disabled="true" ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="24">
              <a-form-item label="采购单号" :labelCol="{span:3}" :wrapperCol="{span:21}">
                <a-input v-decorator="[ 'orign', validatorRules.orign]" placeholder="请输入编号" v-bind:disabled="true"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="采购合同号" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-input  placeholder="请输入合同号" v-decorator="[ 'contract', validatorRules.contract]"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="下单日期" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <j-date v-decorator="[ 'dateOrder', validatorRules.dateOrder]" placeholder="请输入预计到货时间"></j-date>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

        <!-- 子表单区域 -->
        <!--<a-tabs v-model="activeKey" @change="handleChangeTabs">-->
        <!--<a-tab-pane tab="采购明细" :key="refKeys[0]" :forceRender="true">-->
        <!--<pcs-order-line-form ref="pcsOrderLineForm" @validateError="validateError"></pcs-order-line-form>-->
        <!--</a-tab-pane>-->
        <!---->
        <!--</a-tabs>-->


        <!-- 操作按钮区域 -->
<!--        <div class="table-operator">-->
<!--          <a-button @click="handleAddProd" type="primary" icon="plus">添加物料</a-button>-->
<!--        </div>-->

        <!-- 这里加上添加物料弹出框的自定义表单-->
        <a-table
          ref="editableTable"
          size="middle"
          bordered
          rowKey="id"
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd">
          <template slot="vendorCode" slot-scope="text,record,index">
            <j-dict-select-tag-iop type="list"  v-model="dataSourceProd[index].partnerId"
                                   :trigger="true" dictCode="prd_brand,manufactor_name,id,is_del='0'" placeholder="请选择供应商"
            />
          </template>
          <template slot="productUomQty" slot-scope="text,record,index">
            <a-input-number v-if="editable" v-model="dataSourceProd[index].productPlyQty"/>
            <span v-else>{{dataSourceProd[index].productPlyQty}}</span>
          </template>
          <template slot="priceUnit" slot-scope="text,record,index">
            <a-input-number v-if="editable" v-model="dataSourceProd[index].priceUnit"/>
            <span v-else>{{dataSourceProd[index].priceUnit}}</span>
          </template>
          <template slot="action" slot-scope="text,record,index">
            <a-popconfirm v-if="editable" title="确定删除吗?" @confirm="() => handleDelete(index)">
              <a>删除</a>
            </a-popconfirm>
          </template>
          <template slot="remark" slot-scope="text,record,index">
            <a-input v-if="editable" v-model="dataSourceProd[index].remark" />
          </template>
        </a-table>
      </a-spin>
    </a-modal>
<!--    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>-->
  </div>
</template>
<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import PcsOrderLineForm from './PcsOrderLineForm.vue'
  import JDate from "../../../../../../components/jeecg/JDate";
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import addProdModal from '../../orderplan/modules/addProdModal.vue'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { httpAction , getAction } from '@/api/manage'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import PcsPurchaseOrderList from "../PcsPurchaseOrderList";
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import store from '@/store'
  import moment from 'moment'

  export default {
    name: 'CommpleOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate, PcsOrderLineForm,JDictSelectTag,addProdModal,validateDuplicateValue,JEditableTable,
      initDictOptions,filterDictText,PcsPurchaseOrderList,JDictSelectTagIop
    },
    data() {
      return {
        contract:"",
        selectedRowKeys:[],
        orderType:"",
        editable: true, // 可 新增
        model: {},
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
        visibleProd:true,
        dataSourceProd:[],
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          partnerId: {rules: [

            ]},
          datePlanned: {rules: [
              {required: true, message: '请输入预计到货时间!'},
            ]},
          contract: {rules: [
              {required: true, message: '请输入合同号!'},
            ]},
          orderType: {rules: [
              {required: true, message: '选择单据类型!'},
            ]},
        },
        refKeys: ['pcsOrderLine', ],
        tableKeys:[],
        activeKey: 'pcsOrderLine',
        // 采购明细
        pcsOrderLineTable: {
          loading: false,
          dataSource: [],
          columns: [
          ]
        },
        columns: [
          {
            title:'物料id',
            align:"center",
            key: 'productId',
            dataIndex: 'productId',
            type: FormTypes.hidden,
          },
          {
            title:'物料名称',
            align:"center",
            key: 'name',
            dataIndex: 'name',
            customRender: function(t, r, index) {
              if (r.name == null){
                return "暂无信息"
              }
              return r.name
            }
          },
          {
            title:'物料编号',
            align:"center",
            key: 'productNo',
            dataIndex: 'productNo'
          },
          {
            title:'类别',
            align:"center",
            key: 'categoryType',
            dataIndex: 'categoryType',
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
            key: 'acName',
            dataIndex: 'acName'
          },
          {
            title:'单位',
            align:"center",
            key: 'uuName',
            dataIndex: 'uuName',
          },
          {
            title: '供应商',
            align: "center",
            dataIndex: 'vendorId',
            scopedSlots: {customRender: 'vendorCode'}
          },
          {
            title:'采购数量',
            align:"center",
            key:'productPlyQty',
            dataIndex: 'productPlyQty',
            scopedSlots: { customRender: 'productPlyQty' }
          },
          {
            title:'单价',
            align:"center",
            key:'priceUnit',
            dataIndex: 'priceUnit',
            scopedSlots: { customRender: 'priceUnit' }
          },
          {
            title:'金额',
            align:"center",
            key:'price',
            customRender: function(t, r, index) {
              r.price = r.priceUnit * r.productUomQty
              return r.price
            }
          },
          // {
          //   title:'备注',
          //   align:"center",
          //   key:'remark',
          //   dataIndex: 'remark',
          //   scopedSlots: { customRender: 'remark' }
          // },

          // {
          //   title: '操作',
          //   key: 'action',
          //   scopedSlots: { customRender: 'action' }
          // },
        ],
        url: {
          add: "/order/pcsPurchaseOrder/add",
          edit: "/order/pcsPurchaseOrder/edit",
          queryById: "/order/pcsPurchaseOrder/queryById",
          queryPcsOrderLineByMainId: "/order/pcsPurchaseOrder/queryPcsOrderLineByMainId",
          queryPcsOrderLineByIds:"/order/pcsPurchaseOrder/queryPcsOrderLineByIds",
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
//        let fieldval = pick(this.model,'createBy','createTime','pickingNo','orign','partnerId','partnerName','datePlanned','contract','dateOrder','createName','updateName','orderType','state','numberTotal','amountTotal','dateApprove')
//        this.$nextTick(() => {
//          this.form.setFieldsValue(fieldval)
//          this.$refs.pcsOrderLineForm.initFormData(this.url.pcsOrderLine.list,this.model.id)
//        })
//        // 加载子表数据
//        if (this.model.id) {
//          let params = { id: this.model.id }
//        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pcsOrderLineList: this.$refs.pcsOrderLineForm.getFormData(),
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'createBy','createTime','pickingNo','orign','partnerId','partnerName','datePlanned','contract','dateOrder','createName','updateName','orderType','state','numberTotal','amountTotal','dateApprove'))
      },
      handleOkCommple(){
        let ids=[];
          this.selectedRowKeys.forEach(item => {
            ids.push(item)
          })
        // console.log(ids)
        //   console.log("ids", ids.join(';'));
        //   getAction("order/pcsPurchaseOrder/commple", {ids: ids.join(';'),contract:this.contract}).then((res) => {
        //     if (res.success) {
        //       this.visible = false;
        //       this.$emit('ok');
        //     }
        //   });
        let pordData=this.dataSourceProd;
        console.log('dataSourceProd-----',pordData);
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let obj={
              pcsOrderLineList :pordData,
              ids:ids.join(',')
            };
            let formData = Object.assign(obj, values);
            console.log("表单提交数据",formData);
            httpAction('order/pcsPurchaseOrder/commple',formData,'post').then((res)=>{
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
      handleCancelCommple(){
        this.visible=false;
        this.close()
      },
      returnData(data){
        this.visibleProd=true;
        this.dataSourceProd=this.dataSourceProd.concat(data);
      },
      handleAddProd(){
        this.$refs.addProdFrom.add();
      },
      handleOk(){
        let pordData=this.dataSourceProd;
        console.log(pordData);
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          this.$set(values, "orderType", this.orderType)
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            let obj={
              pcsOrderLineList :pordData
            };
            let modelEdit=[];
            let formData=[];
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
      handleCancel(){
        this.close();
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.dataSourceProd=[];
        this.$emit('onClear');
      },
      handleDelete(index){
        this.dataSourceProd.splice(index,1);
      },
      editOrder (v,records) {
        this.selectedRowKeys = v;
        console.log("111111111111",records)
        //计划单号
        var orign = ''
        records.forEach(item =>{
          orign += (item.orign + ",")
        })
        this.model.orign = orign;
        //到货时间
        var maxDate = records[0].datePlanned;
        for(var i = 0; i<records.length; i++){
          var date1 = new Date(records[i].datePlanned)
          var date2 = new Date(maxDate)
          if(date1.getTime()>= date2.getTime()){
            maxDate = records[i].datePlanned
          }
        }
        this.model.datePlanned = maxDate;
        this.visible = true;
        console.log(">>>>>>>>>>>>>>><<<<<<<<<<<<<<<<",v)
        this.form.resetFields();
        //生成订单
        var now = moment().format("YYYYMMDDHHmmss")
        var ddId = "CGDD" + now
        this.model.pickingNo = ddId
        let userName = store.getters.userInfo.realname
        this.model.createName=userName;
        // getAction(this.url.queryById,{id:v[0]}).then((res)=>{
        //   if(res.success){
        //     this.model = Object.assign({}, res.result);
        //     console.log(" this.model------", this.model)
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'orign','pickingNo','partnerId','datePlanned','createName','dateOrder','contract'))
            })
        //   }
        // });
        var jsonStr = JSON.parse(JSON.stringify(v))
        console.log("json数据",jsonStr)
        httpAction(this.url.queryPcsOrderLineByIds,jsonStr,'post').then((res)=>{
          if(res.success){
            this.dataSourceProd=res.result;
            console.log("物料数据11111",this.dataSourceProd)
          }
        });

      },


    }
  }
</script>

<style scoped>
</style>