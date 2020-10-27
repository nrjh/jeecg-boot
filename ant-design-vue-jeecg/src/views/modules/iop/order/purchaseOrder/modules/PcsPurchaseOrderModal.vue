<template>
  <div>
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
          <a-col :span="8">
            <a-form-item label="采购定单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input v-decorator="[ 'pickingNo', validatorRules.pickingNo]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :span="8">-->
<!--            <a-form-item label="供应商名称" :labelCol="{span:9}" :wrapperCol="{span:15}">-->
<!--              <JDictSelectTag v-decorator="[ 'partnerId', validatorRules.partnerId]" :triggerChange="true" placeholder="请输入供应商名称" dictCode="vendorCode" ></JDictSelectTag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="8">
            <a-form-item label="到货日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-date v-decorator="[ 'datePlanned', validatorRules.datePlanned]" placeholder="请输入预计到货时间"></j-date>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="采购员："  :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-dict-select-tag type="list"
                                 placeholder="请选择接收人"
                                 v-decorator="['createName', validatorRules.createName]"
                                 :trigger-change="true"
                                 dict-code="sys_user,realname,id"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row  v-if="showNo">
          <a-col :span="24">
            <a-form-item label="采购单号" :labelCol="{span:3}" :wrapperCol="{span:21}">
              <a-input v-decorator="[ 'orign', validatorRules.orign]" placeholder="请输入编号" v-bind:disabled="true"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
<!--          <a-col :span="12">-->
<!--            <a-form-item label="单据类型" :labelCol="{span:6}" :wrapperCol="{span:15}">-->
<!--              <j-dict-select-tag :trigger-change="true"  placeholder="单据类型" v-decorator="[ 'orderType', validatorRules.orderType]"  dictCode="orderType"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="8">
            <a-form-item label="合同号" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input v-decorator="[ 'contract', validatorRules.contract]" placeholder="请输入合同号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="下单日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-date v-decorator="[ 'dateOrder', validatorRules.dateOrder]" placeholder="请输入下单"></j-date>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="单据类型" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-radio-group v-decorator="['orderType',validatorRules.orderType]" >
                <a-radio value="normal">
                  正常单
                </a-radio>
                <a-radio value="urgency">
                  紧急单
                </a-radio>
              </a-radio-group>
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
      <div class="table-operator">
        <a-button @click="handleAddProd" type="primary" icon="plus">添加物料</a-button>
      </div>

      <!-- 这里加上添加物料弹出框的自定义表单-->
      <a-table
        ref="editableTable"
        size="middle"
        bordered
        :visible="visibleProd"
        :columns="columns"
        :dataSource="dataSourceProd">
        <template slot="vendorCode" slot-scope="text,record,index">
          <j-dict-select-tag-iop type="list"  v-model="dataSourceProd[index].partnerId"
                                 :trigger="true" dictCode="prd_brand,manufactor_name,id,is_del='0'" placeholder="请选择供应商"
          />
        </template>
        <template slot="productPlyQty" slot-scope="text,record,index">
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
    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>
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
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import ARow from "ant-design-vue/es/grid/Row";
  import moment from 'moment'
  import store from '@/store'
  export default {
    name: 'PcsPurchaseOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      ARow,JDictSelectTagIop,
      JDate, PcsOrderLineForm,JDictSelectTag,addProdModal,validateDuplicateValue,
      JEditableTable,initDictOptions,filterDictText
    },
    data() {
      return {
        orderType:"",
        editable: true, // 可 新增
        showNo: false,
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
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          // {
          //   title:'物料id',
          //   align:"center",
          //   key: 'productId',
          //   dataIndex: 'productId',
          //   type: FormTypes.hidden,
          // },
          {
            title:'物料名称',
            align:"center",
            dataIndex: 'rpoductName'
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
            dataIndex: 'acName'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'uuName',
          },
          {
            title:'供应商',
            align:"center",
            key: 'vendorId',
            width: '12%',
            scopedSlots: { customRender: 'vendorCode' }
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
              r.price = r.priceUnit * r.productPlyQty
              return r.price
            }
          },
          {
            title:'备注',
            align:"center",
            key:'remark',
            dataIndex: 'remark',
            scopedSlots: { customRender: 'remark' }
          },

          {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' }
          },
        ],
        url: {
          add: "/order/pcsPurchaseOrder/add",
          edit: "/order/pcsPurchaseOrder/edit",
          queryById: "/order/pcsPurchaseOrder/queryById",
          queryPcsOrderLineByMainId: "/order/pcsPurchaseOrder/queryPcsOrderLineByMainId",
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
        var now = moment().format("YYYYMMDDHHmmss")
        var pkId = "CGDD" + now
        this.model.pickingNo = pkId
        this.model.orderType = "normal"
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'createTime','createOutName','pickingNo','orderType'))
        })
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
      returnData(data){
        this.visibleProd=true;
        this.dataSourceProd=this.dataSourceProd.concat(data);
      },
      handleAddProd(){
        this.$refs.addProdFrom.add();
      },
      handleOk(){
        let pordData=this.dataSourceProd;
        console.log("pordData----",pordData);
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
//          this.$set(values, "orderType", this.orderType)
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
      handleDelete(data){
        this.dataSourceProd.splice(data,1);
      },
      editOrder (v) {
        this.form.resetFields();
        this.showNo = true;
        getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            console.log(" this.model------", this.model)
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'orign','pickingNo','datePlanned','contract','orderType','dateOrder','createName'))
            })
          }
        });
        getAction(this.url.queryPcsOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.dataSourceProd=res.result;
            console.log("2222222",this.dataSourceProd)
          }
        });

      },


    }
  }
</script>

<style scoped>
</style>