<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleOkSubmit"
      @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row >
            <a-col :span="8">
              <a-form-item label="领料单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'orderOutId', validatorRules.orderOutId]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="领料人" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="申请时间" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-time placeholder="申请日期" v-decorator="[ 'createTime', validatorRules.createTime]"
                        :trigger-change="true" style="width: 100%" disabled/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="8">
              <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['lineId', validatorRules.lineId]"
                                       @inputText="inputTextAreaId"
                                       :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                       @change="changeLine"/>
                <a-input v-show="false" v-decorator="['lineName']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="班组" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['team', validatorRules.team]"
                                       :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"/>
<!--                <a-input v-show="false" v-decorator="['team']"></a-input>-->
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="设备名称" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" placeholder="请选择">
                  <a-select-option v-for="item in equipTypeLists" :key="item.equipNo" :value="item.equipId">
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="12">
              <a-form-item label="物料类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-radio-group v-decorator="['prdCategory', validatorRules.prdCategory]">
                  <a-radio value="equip">
                    生产设施
                  </a-radio>
                  <a-radio value="spare">
                    备品备件
                  </a-radio>
                  <a-radio value="office_supplies">
                    办公用品
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="需求时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="预计需求时间" v-decorator="[ 'datePlanned', validatorRules.datePlanned]"
                        :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col>
              <a-form-item label="申请类型" :labelCol="{span:3}" :wrapperCol="{span:9}">
                <a-radio-group v-decorator="['orderType', validatorRules.orderType]" >
                  <a-radio value="normal">
                    正常
                  </a-radio>
                  <a-radio value="urgency">
                    紧急
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
              <a-form-item label="领料说明" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-textarea :rows="3" placeholder="..." v-decorator="['note', validatorRules.note]"
                            v-bind:disabled="diasabledInput"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>

        <!-- 操作按钮区域 -->
        <div class="table-operator">
          <a-button @click="handleAddProd" type="primary" icon="plus">添加物料</a-button>
        </div>

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
          <template slot="productPlyQty" slot-scope="text,record,index">
            <a-input-number v-if="editable" v-model="dataSourceProd[index].productPlyQty"/>
            <span v-else>{{dataSourceProd[index].productPlyQty}}</span>
          </template>
          <template slot="action" slot-scope="text,record,index">
            <a-popconfirm v-if="editable" title="确定删除吗?" @confirm="() => handleDelete(index)">
              <a>删除</a>
            </a-popconfirm>
          </template>
        </a-table>
      </a-spin>
      <template slot="footer">
        <a-button key="submitApproval" type="primary" @click="handleOkSubmit('submit')">
          提交
        </a-button>
        <a-button key="submit" type="primary" @click="handleOkSubmit('draft')">
          保存
        </a-button>
        <a-button key="back" @click="handleCancel">
          取消
        </a-button>
      </template>
    </a-modal>
    <addProdModal ref="addProdFrom" @returnData="returnData"></addProdModal>

  </div>
</template>

<script>
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, getAction} from '@/api/manage'
  import addProdModal from '../../order/orderplan/modules/addProdModal.vue'
  import pick from 'lodash.pick'
  import {FormTypes, getRefPromise} from '@/utils/JEditableTableUtil'
  import {JEditableTableMixin} from '@/mixins/JEditableTableMixin'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import moment from 'moment'
  import store from '@/store'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import { initDictOptionsOra,initDictOptionsIop } from '@/components/dict/JDictSelectUtil'
  import JTime from '@/components/jeecg/JTime'


  export default {
    name: 'StockOutOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      addProdModal,
      JDate,
      ATextarea,
      JDictSelectTagOra,
      JTime
    },
    data() {
      const vm = this
      return {
        // form: this.$form.createForm(this),
        team:'',
        type:'',
        title: "新增",
        defaultSelectValue:"请选择",
        editable: true, // 可新增
        visible: false,
        visibleProd: false,
        diasabledInput: false,
        equipLists:[],
        model: {},
        dataSource: [],
        dataSourceProd: [],
        confirmLoading: false,
        dataForm: [],
        equipTypeLists: [],
        columns: [
          {
            title: '物料名称',
            align: "center",
            dataIndex: 'rpoductName'
          },
          {
            title: '物料编号',
            align: "center",
            dataIndex: 'productNo'
          },
          {
            title: '类别',
            align: "center",
            dataIndex:'categoryType',
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
            align: "center",
            dataIndex:'acName',
            // customRender:function (text) {
            //   return vm.getAttributeCategory(text)
            // }
          },
          {
            title: '库存件数',
            align: "center",
            dataIndex: 'virtualQty',
          },
          {
            title: '单位',
            align: "center",
            dataIndex: 'uuName',
          },
          {
            title: '出库数量',
            align: "center",
            dataIndex: 'productPlyQty',
            scopedSlots: { customRender: 'productPlyQty' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            width: '100px',
            scopedSlots: { customRender: 'action' }
          },
        ],
        labelCol: {
          span: 9
        },
        wrapperCol: {
          span: 15
        },
        visibleProd: false,
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          lineId: {
            rules: [{required: true, message: '请选择检定线!'}]
          },
          shiftId: {
            rules: [{required: true, message: '请选择班组!'}]
          },
          prdCategory: {
            rules: [{required: true, message: '请选择物料类型!'}]
          },
          orderType: {
            rules: [{required: true, message: '请选择申请类型!'}]
          },
          equipId: {
            rules: [{required: true, message: '请选择设备!'}]
          },

        },
        refKeys: ['stockOutOrderLine',],
        tableKeys: ['stockOutOrderLine',],
        activeKey: 'stockOutOrderLine',
        // 出库明细表
        stockOutOrderLineTable: {
          loading: false,
          dataSource: [],

        },
        url: {
          add: "/outOrder/stockOutOrder/add",
          edit: "/outOrder/stockOutOrder/edit",
          queryById: "/outOrder/stockOutOrder/queryById",
          queryStockOutOrderLineByMainId: "/outOrder/stockOutOrder/queryStockOutOrderLineByMainId",
          submitStockApproval: '/outOrder/stockOutOrder/submitApproval',
          queryEquip: "/alarm/record/mtEquipAlarmInfo/queryEquip",
          stockOutOrderLine: {
            list: '/outOrder/stockOutOrder/queryStockOutOrderLineByMainId'
          },
        }
      }
    },
    beforeCreate() {
      this.form = this.$form.createForm(this, {name: 'dynamic_form_item'});
      this.form.getFieldDecorator('keys', {initialValue: [], preserve: true});
    },
    methods: {
      add() {
        this.form.resetFields();
        this.dataSourceProd = [];
        var now = moment().format('YYYY-MM-DD HH:mm:ss')
        var outid = "LLCK" + moment().format("YYYYMMDDHHmmss")
        let userName = store.getters.userInfo.realname
        this.model.createTime = now
        this.model.datePlanned = moment().format("YYYY-MM-DD")
        this.model.orderOutId = outid
        this.model.createOutName = userName
        this.model.orderType = "normal"
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'createTime','createOutName','orderOutId','orderType','datePlanned'))
        })
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      handleAddProd() {
          let t = this.form.getFieldValue('prdCategory')
          this.$refs.addProdFrom.add(t);
      },
      returnData(data) {
        this.visibleProd = true;
        for(var i = 0;i<data.length;i++){
          if(data[i].productQty ==0){
            data.splice(i,1);
          }
        }
        console.log("查看返回数据111111",data)
        this.dataSourceProd = this.dataSourceProd.concat(data);
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {

      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {

      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'orderOutId', 'createOutName', 'createOutTime', 'prdCategory', 'belongEquip', 'team', 'orderType', 'datePlanned', 'note', 'state', 'updateInBy', 'updateInName', 'updateInTime', 'outState'))
      },
      inputTextShiftId(val) {
        this.form.setFieldsValue({ team: val })
      },
      inputTextAreaId(val){
        this.form.setFieldsValue({ lineName: val })
      },
      handleOkSubmit(state) {
        // let pordData = this.$refs.editableTable.getValuesSync().values;
        let pordData = this.dataSourceProd
        console.log("物料数据",pordData);
        this.dataForm = [];
        for (let i = 0; i < pordData.length; i++) {
          if(pordData[i].productPlyQty>pordData[i].virtualQty){
            this.$message.warning("物品出库数量大于库存数量，请修改");
            return
          }
          let temp = {}
          // 规格  字段:acName
          temp.attributeCategoryId = pordData[i].attributeCategoryID

          temp.productId = pordData[i].productId
          // 物料名称
          temp.productName = pordData[i].rpoductName
          // 类别   stock_out_order_line.stock_out_order_line  物品类型
          temp.categoryType = pordData[i].categoryType
          // 物料编号  stock_out_order_line.defaultCode  内部货号
          temp.defaultCode = pordData[i].productNo
          temp.attributeCategoryId = pordData[i].attributeCategoryId
          temp.virtualQty = pordData[i].virtualQty
          temp.productUomId = pordData[i].productUomId
          temp.productPlyQty = pordData[i].productPlyQty
          temp.id = pordData[i].id
          temp.status = 'freeze'

          this.dataForm.push(temp);
        }
        // console.log("物料数据2222",this.dataForm);
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            let obj = {
              stockOutOrderLineList: this.dataForm,
              state: state
            };
            let modelEdit = [];
            let formData = [];
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
              formData = Object.assign(obj, values);
            } else {
              // console.log(this.model.id,"88888888")
              httpurl += this.url.edit;
              method = 'put';
              if(this.model.state == "back"){
                this.model.state = "approve"
              }else{
                this.model.state = state
              }
              modelEdit = Object.assign(this.model, values);
              formData = Object.assign(obj, modelEdit);
            }
            console.log("表单提交数据", formData);
            console.log("表单提交数据httpurl", httpurl);
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
                that.$emit('searchQuery');
                that.model={};
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel() {
        this.close()
      },

      edit(v, opt) {
        this.form.resetFields();
        // console.log(v,222222222222222222222)
        getAction(this.url.queryById, {id: v}).then((res) => {
          if (res.success) {
            this.model = Object.assign({}, res.result);
            console.log(this.model, '77777777777')
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'createTime','createOutName','orderOutId', 'prdCategory', 'datePlanned', 'orderType',
                'note','lineId','equipName','team'))
            })
          }
        });
        getAction(this.url.queryStockOutOrderLineByMainId, {id: v}).then((res) => {
          if (res.success) {
            this.dataSourceProd = []
             console.log(res.result, "11111111111111")
            for (let i = 0; i < res.result.length; i++) {
              let temp = {}
              console.log("拼凑数据前--")
              console.log(res.result[i])
              temp.id = res.result[i].id
              temp.productId = res.result[i].productId
              temp.rpoductName = res.result[i].productName
              temp.categoryType = res.result[i].categoryType
              temp.productNo = res.result[i].defaultCode
              temp.acName = res.result[i].attributeCategoryId
              temp.virtualQty = res.result[i].virtualQty
              temp.productUomId = res.result[i].productUomId
              temp.status = res.result[i].status
              temp.uuName = res.result[i].productUomId_dictText
              temp.productPlyQty = res.result[i].productPlyQty
              // temp.acName = res.result[i].attributeCategoryId
              this.dataSourceProd.push(temp);
            }
            console.log(this.dataSourceProd, "33333333333")
          }
        });
        if (opt == 1) {
          this.diasabledInput = true;
          this.getAllTable().then(editableTables => {
            editableTables[0].columns[6].disabled = true;
          })
        }
      },
      submitApproval() {
        if (this.model.id) {
          var v = this.model.id
          var j = parseInt(v)
          console.log("id111111111111111111", j)
          getAction(this.url.submitStockApproval, {id:j}).then((res) => {
            if (res.success) {
              this.$message.success(res.message);
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.visible = false;
          })
        } else {
          alert("请先提交数据")
        }
      },
      // changeLine(value){
      //   this.equipLists=[];
      //   getAction(this.url.queryEquip,{paramId:value}).then(res=>{
      //     console.log("res",res)
      //     console.log("result",res.result)
      //     this.equipLists=res.result;
      //   })
      // },
      changeLine(value){
        this.equipTypeLists=[];
        this.form.setFieldsValue({equipType:''})
        this.form.setFieldsValue({equipName:''})
        this.form.setFieldsValue({equipId:'请选择'})
        let equipVaule='';
        let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
          console.log("值1111111",res.result)
          equipVaule=res.result[0].value;
          if(equipVaule != null){
              getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
                  this.equipTypeLists=res.result;
              })
          }
        })

        // 若是放在这里拿到的equipVaule = ""
        // getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
        //   this.equipTypeLists=res.result;
        //   console.log(this.equipTypeLists.length)
        // })
      },
      handleDelete(index){
        this.dataSourceProd.splice(index,1);
      },
    }

  }
</script>

<style scoped>
</style>