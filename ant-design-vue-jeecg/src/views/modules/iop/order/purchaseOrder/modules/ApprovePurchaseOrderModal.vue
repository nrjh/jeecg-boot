<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :maskClosable="false"
      :confirmLoading="confirmLoading"
      :footer="null"
      @ok="handleOk"
      @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row>
            <a-col :span="8">
              <a-form-item label="订单编号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'pickingNo', validatorRules.pickingNo]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>

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

          <a-row>
            <a-col :span="24">
              <a-form-item label="采购单号" :labelCol="{span:3}" :wrapperCol="{span:21}">
                <a-input v-decorator="[ 'orign', validatorRules.orign]" placeholder="" disabled="disabled" ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="采购合同号" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'contract', validatorRules.contract]"  disabled="disabled" ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="下单日期" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <j-date v-decorator="[ 'dateOrder', validatorRules.dateOrder]" placeholder="" disabled="disabled" ></j-date>
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
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd"
        >
        </a-table>
        <div class="dy-box" style="border:1px solid #000;padding:20px 10px;border-radius: 5px;margin:15px 0;position:relative;" title="审批信息">
        <a-form>
          <a-row style="margin-left:75px">
            <a-col :span="24">
              <a-form-item :labelCol="{span:3}" :wrapperCol="{span:15}">
                <span>车间： -----{{createTime}} {{createName}}提交-----</span>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row v-if="checkList.length>0" v-for="item in checkList" style="margin-left:75px" :key="item.createTime">
            <a-col :span="24">
              <a-form-item :labelCol="{span:3}" :wrapperCol="{span:15}">
                <span>车间： -----{{item.createTime}} {{item.createBy}} {{item.result==='pass'?'审核通过':'审核失败'}}-----</span>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="24">
              <a-form-item label="审核结果" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-radio-group v-model="result" >
                  <a-radio value="pass">
                    通过
                  </a-radio>
                  <a-radio value="reject">
                    不同意
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="24">
              <a-form-item label="审核意见"  :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-textarea v-model="approveInfo" rows="5" placeholder=""></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        </div>

        <!-- 操作按钮区域 -->
        <div class="table-operator" style="text-align: center;">
          <a-button @click="handleApprove" type="primary" icon="plus">提交</a-button>
<!--          <a-button @click="handleApprove(false)" type="primary" icon="plus">退回</a-button>-->
          <a-button @click="handleCancel" type="primary" icon="plus">关闭</a-button>
        </div>

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
  import { httpAction , getAction ,postAction} from '@/api/manage'
  import JInput from "../../../../../../components/jeecg/JInput";
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import AFormItem from "ant-design-vue/es/form/FormItem";

  export default {
    name: 'LookPurchaseOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      AFormItem,
      ACol,
      ARow, ATextarea, JInput, JDate, PcsOrderLineForm,JDictSelectTag,addProdModal,
    },
    data() {
      return {
        createName:'',
        createTime:'',
        checkList: [],
        result: '',
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
        approveInfo:"",
        pickingNo:"",
        id:"",
        dataSourceProd:[],
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          vendorId: {rules: [

          ]},
          datePlanned: {rules: [
          ]},
          contract: {rules: [
          ]},
          pickingNo: {rules: [
          ]},
          orign: {rules: [
          ]},
          dateOrder: {rules: [

          ]},
          createName: {rules: [
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
        // columns: [
        //   {
        //     title:'物料id',
        //     align:"center",
        //     key: 'productId',
        //     type: FormTypes.hidden,
        //   },
        //   {
        //     title:'物料名称',
        //     align:"center",
        //     key: 'name'
        //   },
        //   {
        //     title:'物料编号',
        //     align:"center",
        //     key: 'productNo'
        //   },
        //   {
        //     title:'类别',
        //     align:"center",
        //     key: 'categoryId'
        //   },
        //   {
        //     title:'规格',
        //     align:"center",
        //     key: 'attributeCategoryId',
        //   },
        //   {
        //     title:'单位',
        //     align:"center",
        //     key: 'productUomId',
        //     type: FormTypes.sel_search,
        //     dictCode:"UOM_UOM",
        //     disabled:true,
        //   },
        //   {
        //     title:'采购数量',
        //     align:"center",
        //     key:'productUomQty',
        //     type: FormTypes.inputNumber,
        //     allowInput: true,
        //     defaultValue: '0',
        //     placeholder: '请输入${title}',
        //     disabled: true
        //   },
        //   {
        //     title:'单价',
        //     align:"center",
        //     key:'priceUnit',
        //     type: FormTypes.inputNumber,
        //     allowInput: true,
        //     defaultValue: '0',
        //     placeholder: '请输入${title}',
        //     disabled: true
        //   },
        //   {
        //     title:'金额',
        //     align:"center",
        //     key:'price',
        //     type: FormTypes.inputNumber,
        //     allowInput: true,
        //     defaultValue: '0',
        //     placeholder: '请输入${title}',
        //     disabled: true
        //   },
        //   {
        //     title:'备注',
        //     align:"center",
        //     key:'remark',
        //     type: FormTypes.input,
        //     allowInput: true,
        //     defaultValue: '0',
        //     placeholder: '请输入${title}',
        //     disabled: true
        //   },
        // ],
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
          {
            title:'物料名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'物料编号',
            align:"center",
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
            dataIndex: 'acName',
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'uuName',
          },
          {
            title:'采购数量',
            align:"center",
            dataIndex:'productUomQty',
          },
          {
            title:'单价',
            align:"center",
            dataIndex:'priceUnit',
          },
          {
            title:'金额',
            align:"center",
            dataIndex:'price',
          },
          {
            title:'备注',
            align:"center",
            dataIndex:'remark',
          },
        ],
        url: {
          add: "/order/pcsPurchaseOrder/add",
          edit: "/order/pcsPurchaseOrder/edit",
          queryById: "/order/pcsPurchaseOrder/queryById",
          approve: "/order/pcsPurchaseOrder/approve",
          queryPcsOrderLineByMainId: "/order/pcsPurchaseOrder/queryPcsOrderLineByMainId",
          check:"/outOrder/stockOutOrder/check",
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
      returnData(data){
        this.visibleProd=true;
        this.dataSourceProd=data;
      },
      handleAddProd(){
        this.$refs.addProdFrom.add();
      },
      handleOk(){
      },
      handleCancel(){
        this.close();
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.dataSourceProd=[];
        this.approveInfo="";
        this.pickingNo='';
        this.$emit('onClear');
        this.$emit('ok');
      },
      handleDelete(data){
      },
      editOrder (v) {
        this.id=v;
        console.log("v------",v)
        this.form.resetFields();
        getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.pickingNo=this.model.pickingNo;
            this.createName=this.model.createName;
            this.createTime=this.model.createTime;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'pickingNo','orign','dateOrder','createName','partnerId','datePlanned','contract'))
            })
            //加载审核信息
            this.checkList = []
            if (this.model.id) {
              getAction(this.url.check, { id: this.model.pickingNo }).then(res => {
                this.checkList = res.result
                console.log(this.checkList,"22222222222")
              })
            }
          }
        });
        getAction(this.url.queryPcsOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.dataSourceProd=res.result;
            console.log( this.dataSourceProd,"33333333")
          }
        });


      },
      handleApprove(){
          console.log("pickingNo",this.pickingNo)
          console.log("approveInfo",this.approveInfo)
          getAction(this.url.approve,{pickingNo:this.pickingNo,approveInfo:this.approveInfo,id:this.id,result:this.result}).then((res)=>{
          if(res.success){
            if(res.success){
              this.$message.success("审核提交成功！");
              this.close();
            }
          }else {
            this.$message.error("该订单已经审核，请勿重复提交！");
            this.close();
          }
        });

      },



    }
  }
</script>
<style scoped>
</style>