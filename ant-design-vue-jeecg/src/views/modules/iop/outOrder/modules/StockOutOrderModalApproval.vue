<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @cancel="handleCancel">
      <a-spin :spinning="confirmLoading">
        <!-- 主表单区域 -->
        <a-form :form="form">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="物料类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-radio-group v-decorator="['prdCategory', validatorRules.prdCategory]" disabled="disabled">
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
              <a-form-item label="预计需求时间" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <j-date placeholder="预计需求时间" v-decorator="[ 'datePlanned', validatorRules.datePlanned]" disabled="disabled"
                        :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['lineId', validatorRules.lineId]"
                                       @inputText="inputTextAreaId"
                                       :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                       @change="changeLine"
                                      disabled/>
                <a-input v-show="false" v-decorator="['lineName']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="班组" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['team', validatorRules.team]"
                                       :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"
                                       disabled/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="设备名称" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" disabled>
                  <a-select-option v-for="item in equipTypeLists" :key="item.equipNo" :value="item.equipId">
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col>
              <a-form-item label="申请类型" :labelCol="{span:3}" :wrapperCol="{span:18}">
                <a-radio-group v-decorator="['orderType', validatorRules.orderType]" disabled="disabled">
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
              <a-form-item label="领料说明" :labelCol="{span:3}" :wrapperCol="{span:18}">
                <a-input v-decorator="[ 'note', validatorRules.note]" placeholder="请输入说明" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
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
          :dataSource="dataSourceProd"
          disabled="disabled"
        >
          <span slot="action" slot-scope="record">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                      <a>删除</a>
                </a-popconfirm>
        </span>
        </a-table>
      </a-spin>
      <template slot="footer">
        <a-button key="submitApproval" type="primary" @click="submitApproval">
          提交
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
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  export default {
    name: 'StockOutOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      addProdModal,
      JDate,
      JDictSelectTagOra
    },
    data() {
      return {
        // form: this.$form.createForm(this),
        title: "新增",
        visible: false,
        visibleProd: false,
        diasabledInput: false,
        model: {},
        dataSource: [],
        dataSourceProd: [],
        confirmLoading: false,
        dataForm: [],
        equipTypeLists:[],
        // columns: [
        //   {
        //     title: '物料id',
        //     align: "center",
        //     key: 'productId',
        //     type: FormTypes.hidden,
        //   },
        //   {
        //     title: '物料名称',
        //     align: "center",
        //     key: 'name'
        //   },
        //   {
        //     title: '物料编号',
        //     align: "center",
        //     key: 'productNo'
        //   },
        //   {
        //     title: '类别',
        //     align: "center",
        //     key: 'categoryId'
        //   },
        //   {
        //     title: '规格',
        //     align: "center",
        //     key: 'attributeCategoryId',
        //   },
        //   {
        //     title: '库存件数',
        //     align: "center",
        //     key: 'productQty',
        //   },
        //   {
        //     title: '单位',
        //     align: "center",
        //     key: 'productUomId',
        //     type: FormTypes.sel_search,
        //     dictCode: "UOM_UOM",
        //     disabled: true,
        //   },
        //   {
        //     title: '出库数量',
        //     align: "center",
        //     key: 'product_qty',
        //     type: FormTypes.inputNumber,
        //     allowInput: true,
        //     defaultValue: '0',
        //     placeholder: '请输入${title}',
        //     disabled: false
        //   },
        //   {
        //     title: '操作',
        //     key: 'action',
        //     // width: '8%',
        //     width: '100px',
        //     type: FormTypes.slot,
        //     slotName: 'action',
        //     defaultValue: '删除'
        //   },
        // ],
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
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   width: '100px',
          //   scopedSlots: { customRender: 'action' }
          // },
        ],
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
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          prdCategory: {
            rules: []
          },
          datePlanned: {
            rules: []
          },
          belongEquip: {
            rules: []
          },
          team: {
            rules: []
          },
          orderType: {
            rules: []
          },
          note: {
            rules: []
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
      inputTextAreaId(val){
        this.form.setFieldsValue({ lineName: val })
      },
      changeLine(value){
        this.equipTypeLists=[];
        this.form.setFieldsValue({equipType:''})
        this.form.setFieldsValue({equipName:''})
        let equipVaule='';
        let str="o_area,area_id,EQUIP_CATEG,area_id='"+value+"'";
        initDictOptionsOra(str).then(res => {
          equipVaule=res.result[0].value;
        })
        getAction(this.url.queryEquip,{paramId:equipVaule}).then(res=>{
          this.equipTypeLists=res.result;
        })
      },
      add() {
        this.visible = true;
        this.form.resetFields();
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      handleAddProd() {
        this.$refs.addProdFrom.add();
      },
      returnData(data) {
        this.visibleProd = true;
        this.dataSourceProd = data;
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

      handleCancel() {
        this.close()
      },

      edit(v) {
        this.form.resetFields();
        console.log(v,22222222222)
        getAction(this.url.queryById, {id: v}).then((res) => {
          if (res.success) {
            this.model = Object.assign({}, res.result);
            console.log(this.model, '77777777777')
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'prdCategory', 'datePlanned', 'orderType',
                'note','lineId','team','equipName'))
            })
          }
        });
        getAction(this.url.queryStockOutOrderLineByMainId, {id: v}).then((res) => {
          if (res.success) {
            this.dataSourceProd = []
            console.log(res.result, "11111111111111")
            for (let i = 0; i < res.result.length; i++) {
              let temp = {}
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
              this.dataSourceProd.push(temp);
              console.log(this.dataSourceProd, "33333333333")
            }
          }
        });
      },
      submitApproval() {
        if(this.model.state != 'draft'){
          this.$message.warning("此出库单已提交审批");
        }else {
          if (this.model.id) {
            var v = this.model.id
            var j = parseInt(v)
            console.log("id111111111111111111", j)
            getAction(this.url.submitStockApproval, {id:j}).then((res) => {
              if (res.success) {
                this.$message.success(res.message);
                this.$emit('ok');
              } else {
                this.$message.warning(res.message)
              }
            }).finally(() => {
              this.visible = false;
            })
          } else {
            alert("请先提交数据")
          }
        }
      },
    }
  }
</script>

<style scoped>
</style>