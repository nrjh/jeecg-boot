<template>
  <div>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row >
          <a-col :span="8">
            <a-form-item label="领料出库单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <a-input v-decorator="[ 'orderOutId', validatorRules.orderOutId]" placeholder="系统自动生成" disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="领料人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="入库时生成"  disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="申请时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-time placeholder="申请时间" v-decorator="[ 'createTime', validatorRules.createTime]"
                      :trigger-change="true" style="width: 100%" disabled/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col :span="8">
            <a-form-item label="检定线" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-dict-select-tag-ora type="list" v-decorator="['lineId', validatorRules.lineId]"
                                     :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                      disabled/>
              <a-input v-show="false" v-decorator="['lineName']"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="班组" :labelCol="{span:9}" :wrapperCol="{span:15}">
              <j-dict-select-tag-ora type="list" v-decorator="['team', validatorRules.team]"
                                     :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组" disabled/>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="设备名称" :labelCol="{span:9}" :wrapperCol="{span:15}">
<!--              <a-select  v-decorator="[ 'equipId', validatorRules.equipId]">-->
<!--                <a-select-option v-for="item in equipTypeLists" :key="item.equipNo" :value="item.equipId" disabled>-->
<!--                  {{ item.title }}-->
<!--                </a-select-option>-->
<!--              </a-select>-->
              <a-input v-decorator="[ 'equipName', validatorRules.equipName]" placeholder="设备名称" v-bind:disabled="true"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col :span="12">
            <a-form-item label="物料类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
              <a-radio-group v-decorator="['prdCategory', validatorRules.prdCategory]" disabled>
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
            <a-form-item label="预计需求时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="预计需求时间" v-decorator="[ 'datePlanned', validatorRules.datePlanned]"
                      :trigger-change="true" style="width: 100%" disabled/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col>
            <a-form-item label="申请类型" :labelCol="{span:3}" :wrapperCol="{span:9}">
              <a-radio-group v-decorator="['orderType', validatorRules.orderType]" disabled>
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
              <a-textarea :rows="3" placeholder="..." v-decorator="[ 'note', { rules: [] } ]"
                          v-bind:disabled="diasabledInput" disabled/>
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
        style="text-align: center"
      >
      </a-table>

      <template v-if="checkList.length>0">
        <template v-for="item in checkList">
          <a-row>
            <a-col :span="8">
              <a-form-item label="审批结果" :labelCol="{span:9}" :wrapperCol="{span:14}">
                {{item.result == 'pass' ? '审批通过':'审批失败'}}
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="审批人" :labelCol="{span:9}" :wrapperCol="{span:14}">
                {{item.createBy}}
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="审批时间" :labelCol="{span:9}" :wrapperCol="{span:14}">
                {{item.createTime}}
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="审批意见" :labelCol="{span:3}" :wrapperCol="{span:21}">
                {{item.sugg}}
              </a-form-item>
            </a-col>
          </a-row>
        </template>
      </template>
    </a-spin>
    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
  </div>
</template>

<script>
  import JDate from '@/components/jeecg/JDate'
  import { httpAction , getAction } from '@/api/manage'
  import addProdModal from '../../order/orderplan/modules/addProdModal.vue'
  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import moment from 'moment'
  import store from '@/store'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JTime from '@/components/jeecg/JTime'

  export default {
    name: 'StockOutOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTag,
      addProdModal,
      JDate,
      JDictSelectTagOra,
      JTime
    },
    data() {
      return {
        // form: this.$form.createForm(this),
        title:"查看",
        visible: false,
        diasabledInput:false,
        model: {},
        dataSource: [],
        dataSourceProd:[],
        confirmLoading: false,
        dataForm: [],
        checkList: [],
        equipTypeLists:[],
        columns: [
          // {
          //   title:'物料id',
          //   align:"center",
          //   dataIndex: 'productId',
          //   type: FormTypes.hidden,
          // },
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
            title:'规格',
            align:"center",
            dataIndex: 'attributeCategoryId_dictText',
          },
          {
            title:'库存件数',
            align:"center",
            dataIndex: 'virtualQty',
          },
          {
            title:'单位',
            align:"center",
/*          key: "productUomId_dictText",
            type: FormTypes.sel_search,
            dictCode:"UOM_UOM",*/
            dataIndex: 'productUomId_dictText',
           type: FormTypes.sel_search,
            disabled:true,
          },
          {
            title:'出库数量',
            align:"center",
            dataIndex:'productPlyQty',
            type: FormTypes.inputNumber,
            allowInput: true,
            defaultValue: '0',
            placeholder: '请输入${title}',
            disabled: true
          },
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
        visibleProd:false,
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          prdCategory: {rules: [
          ]},
          datePlanned: {rules: [
            ]},
          belongEquip: {rules: [
          ]},
          team: {rules: [
          ]},
          orderType: {rules: [
          ]},
          note: {rules: [
          ]},
        },
        refKeys: ['stockOutOrderLine', ],
        tableKeys:['stockOutOrderLine', ],
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
          check:"/outOrder/stockOutOrder/check",
          stockOutOrderLine: {
            list: '/outOrder/stockOutOrder/queryStockOutOrderLineByMainId'
          },
        }
      }
    },
    beforeCreate() {
      this.form = this.$form.createForm(this, { name: 'dynamic_form_item' });
      this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
    },
    methods: {
      look (v) {
        this.form.resetFields();
        getAction(this.url.queryById,{id:v}).then((res)=>{
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'createTime','createOutName','orderOutId','prdCategory','datePlanned','orderType',
                'note','lineId','equipName','team'))
              this.checkList = []
              // 加载子表数据
              if (this.model.id) {
                getAction(this.url.check, { id: this.model.orderOutId }).then(res => {
                  this.checkList = res.result
                  console.log(this.checkList,"22222222222")
                })
              }
            })
          }
        });
        getAction(this.url.queryStockOutOrderLineByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.dataSourceProd = []
            console.log(res.result,"11111111111111")
            for (let i = 0; i < res.result.length; i++) {
              let temp = {}
              temp.productId = res.result[i].productId
              temp.name = res.result[i].productName
              temp.categoryType = res.result[i].categoryType
              temp.productNo = res.result[i].defaultCode
              temp.attributeCategoryId_dictText = res.result[i].attributeCategoryId_dictText
              temp.productPlyQty = res.result[i].productPlyQty
              temp.virtualQty = res.result[i].virtualQty
              temp.productUomId_dictText = res.result[i].productUomId_dictText
              temp.status = res.result[i].status
              this.dataSourceProd.push(temp);
              // console.log(this.dataSourceProd, "33333333333")
            }
          }
        });
        // this.lookAfter()
      },
      lookAfter() {
        console.log("11122222222222")
        this.checkList = []
        // 加载子表数据
        if (this.model.id) {
          getAction(this.url.check, { id: this.model.id }).then(res => {
            this.checkList = res.result
            console.log(this.checkList,"22222222222")
          })
        }
      },
      handleCancel () {
        this.close()
      },
    }
  }
</script>

<style scoped>
</style>