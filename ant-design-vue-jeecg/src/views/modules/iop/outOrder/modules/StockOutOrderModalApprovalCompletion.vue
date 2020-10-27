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
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="物料类型" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <a-radio-group v-decorator="['prdCategory', validatorRules.prdCategory]"
                               v-bind:disabled="diasabledInput">
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
              <a-form-item label="预计需求时间" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <j-date placeholder="预计需求时间" v-decorator="[ 'datePlanned', validatorRules.datePlanned]"
                        :trigger-change="true" style="width: 100%"
                        v-bind:disabled="diasabledInput"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :span="12">
              <a-form-item label="申请类型" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <a-radio-group v-decorator="['orderType', validatorRules.orderType]"
                               v-bind:disabled="diasabledInput">
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
            <a-col :span="12">
              <a-form-item label="班组" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['team', validatorRules.team]"
                                       :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"
                                       v-bind:disabled="diasabledInput"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="检定线" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['lineId', validatorRules.lineId]"
                                       @inputText="inputTextAreaId"
                                       :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                       @change="changeLine"
                                       v-bind:disabled="diasabledInput"/>
                <a-input v-show="false" v-decorator="['lineName']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="{span:6}" :wrapperCol="{span:15}">
                <a-select  v-decorator="[ 'equipName', validatorRules.equipName]" v-bind:disabled="diasabledInput">
                  <a-select-option v-for="item in equipTypeLists" :key="item.equipNo" :value="item.equipId" >
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item
                :labelCol="{span:3}"
                :wrapperCol="{span:18}"
                label="领料说明"
                hasFeedback>
                <a-textarea :rows="3" placeholder="..." v-decorator="[ 'note', { rules: [] } ]"
                            v-bind:disabled="diasabledInput"/>
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
          v-bind:disabled="diasabledInput"
          style="text-align: center"
        >
          <span slot="action" slot-scope="record">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                      <a>删除</a>
                </a-popconfirm>
        </span>
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

        <a-form :form="form">
          <a-row>
<!--            <a-col span="8">-->
<!--              <a-form-item :labelCol="{span:9}"-->
<!--                           :wrapperCol="{span:15}"-->
<!--                           label="车间">-->
<!--                <j-date v-decorator="[ 'createTime', validatorRules.createTime]"-->
<!--                        :trigger-change="true" style="width: 100%"/>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col span="6">-->
<!--              <a-form-item :labelCol="{span:4}"-->
<!--                           :wrapperCol="{span:20}"-->
<!--              >-->
<!--                <a-input v-decorator="['createOutName', validatorRules.createOutName]"/>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col span="10">-->
<!--              <a-form-item :labelCol="{span:3}"-->
<!--                           :wrapperCol="{span:16}"-->
<!--              >-->
<!--                <span>提交</span>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item
                :labelCol="{span:3}"
                :wrapperCol="{span:21}"
                label="审批意见"
                hasFeedback>
                <a-textarea :rows="3" placeholder="..." v-decorator="['sugg', validatorRules.sugg]"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">

              <a-form-item
                :labelCol="{span:6}"
                :wrapperCol="{span:18}"
                label="审核人"
              >
                <j-dict-select-tag type="list"
                                   placeholder="请选择接收人"
                                   v-decorator="['userName', validatorRules.userName]"
                                   :trigger-change="true"
                                   dict-code="sys_user,realname,id"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item :labelCol="{span:6}"
                           :wrapperCol="{span:18}"
                           label="审核时间">
                <j-date v-decorator="[ 'approvalTime', validatorRules.approvalTime]"
                        :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <!--        <template>-->
        <!--          车间:<a-input :form ="form" v-decorator="['createTime', validatorRules.createTime]" ></a-input>-->
        <!--        </template>-->
      </a-spin>
      <template slot="footer">
        <a-button key="complete" type="primary" @click="completeApproval('pass')">
          通过
        </a-button>
        <a-button key="back" @click="completeApproval('reject')">
          不通过
        </a-button>
        <a-button key="cancel" @click="handleCancel">
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
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import AFormItem from "ant-design-vue/es/form/FormItem";
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";
  import store from "@/store"
  import moment from 'moment'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'

  export default {
    name: 'StockOutOrderModal',
    mixins: [JEditableTableMixin],
    components: {
      ACol,
      ARow,
      AFormItem,
      JDictSelectTag,
      addProdModal,
      JDate,
      ATextarea,
      store,
      JDictSelectTagOra
    },
    data() {
      return {
        // form: [],
        title: "审批",
        visible: false,
        visibleProd: false,
        diasabledInput: true,
        model: {},
        dataSource: [],
        dataSourceProd: [],
        checkList: [],
        confirmLoading: false,
        dataForm: [],
        equipTypeLists:[],
        columns: [
          // {
          //   title: '物料id',
          //   align: "center",
          //   dataIndex: 'productId',
          //   type: FormTypes.hidden,
          // },
          {
            title: '物料名称',
            align: "center",
            dataIndex: 'name'
          },
          {
            title: '物料编号',
            align: "center",
            dataIndex: 'productNo'
          },
          {
            title: '类别',
            align: "center",
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
            title: '规格',
            align: "center",
            dataIndex: 'attributeCategoryId',
          },
          {
            title: '库存件数',
            align: "center",
            dataIndex: 'virtualQty',
          },
          {
            title: '单位',
            align: "center",
            dataIndex: 'productUomId',
            type: FormTypes.sel_search,
            dictCode: "UOM_UOM",
            disabled: true,
          },
          {
            title: '出库数量',
            align: "center",
            dataIndex: 'productPlyQty',
            type: FormTypes.inputNumber,
            allowInput: true,
            defaultValue: '0',
            placeholder: '请输入${title}',
            disabled: false
          }
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
          createOutName: {
            rules: []
          },
          createTime: {
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
          userName: {
            rules: []
          },
          sugg: {
            rules: [{required: true, message: '请输入审核意见!'}]
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
          add: "/outOrder/stockOutOrderApproval/add",
          edit: "/outOrder/stockOutOrderApproval/edit",
          queryById: "/outOrder/stockOutOrderApproval/queryById",
          queryStockOutOrderLineByMainId: "/outOrder/stockOutOrderApproval/queryStockOutOrderLineByMainId",
          completeStockApproval: '/outOrder/stockOutOrderApproval/completeStockApproval',
          check:"/outOrder/stockOutOrder/check",
          stockOutOrderLine: {
            list: '/outOrder/stockOutOrderApproval/queryStockOutOrderLineByMainId'
          },
        }
      }
    },
    beforeCreate() {
      this.form = this.$form.createForm(this, {name: 'dynamic_form_item'});
      this.form.getFieldDecorator('keys', {initialValue: [], preserve: true});
    },
    methods: {
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
      inputTextAreaId(val){
        this.form.setFieldsValue({ lineName: val })
      },
      inputTextShiftId(val) {
        this.form.setFieldsValue({ team: val })
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
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

      handleOkSubmit() {
        let pordData = this.$refs.editableTable.getValuesSync().values;
        // console.log("物料数据",pordData);
        this.dataForm = [];
        for (let i = 0; i < pordData.length; i++) {
          let temp = {}
          temp.productId = pordData[i].productId
          temp.productName = pordData[i].name
          temp.categoryType = pordData[i].categoryId
          temp.defaultCode = pordData[i].productNo
          temp.attributeCategoryId = pordData[i].attributeCategoryId
          temp.productQty = pordData[i].product_qty
          temp.productUomId = pordData[i].productUomId
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
              stockOutOrderLineList: this.dataForm
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

      edit(v) {
        this.visible = true;
        this.form.resetFields();
        console.log(v, 222222222222222222222)
        getAction(this.url.queryById, {id: v}).then((res) => {
          if (res.success) {
            this.model = Object.assign({}, res.result);
            this.model.userName = this.getUserName
            var now = moment().format();
            this.model.approvalTime = now
            console.log(this.model, '77777777777')
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model, 'prdCategory', 'datePlanned', 'orderType',
                'note','team','equipName','lineId','userName','approvalTime'))
            })
            this.checkList = []
            // 加载子表数据
            if (this.model.id) {
              getAction(this.url.check, { id: this.model.orderOutId }).then(res => {
                this.checkList = res.result
                console.log(this.checkList,"22222222222")
              })
            }
          }
        });
        getAction(this.url.queryStockOutOrderLineByMainId, {id: v}).then((res) => {
          if (res.success) {
            this.dataSourceProd = []
            console.log(res.result, "11111111111111")
            for (let i = 0; i < res.result.length; i++) {
              let temp = {}
              temp.id = res.result[i].id
              temp.productId = res.result[i].productId
              temp.name = res.result[i].productName
              temp.categoryType = res.result[i].categoryType
              temp.productNo = res.result[i].defaultCode
              temp.attributeCategoryId = res.result[i].attributeCategoryId
              temp.productPlyQty = res.result[i].productPlyQty
              temp.virtualQty = res.result[i].virtualQty
              temp.productUomId = res.result[i].productUomId
              temp.status = res.result[i].status
              this.dataSourceProd.push(temp);
              console.log(this.dataSourceProd, "33333333333")
            }
          }
        });
      },
      completeApproval(result) {
        this.form.validateFieldsAndScroll((err, values) => {
          if (!err) {
            let abb = {}
            console.log(values,"值33333333333")
            abb.orderNo = this.model.orderOutId
            abb.sugg = values.sugg
            abb.createBy = values.userName
            abb.createTime = values.approvalTime
            abb.result = result
            this.$nextTick(() => {
              httpAction(this.url.completeStockApproval, abb, 'post').then((res) => {
                if (res.success) {
                  this.$message.success(res.message);
                } else {
                  this.$message.warning(res.message)
                }
              }).finally(() => {
                this.close()
                this.$emit("ok")
              })
            })
          }
        });
      },
    },
    computed: {
      getUserName() {
        let userName = store.getters.userInfo.username;
        let userId = store.getters.userInfo.id;
        // console.log("用户信息",store.getters.userInfo)
        // console.log(userName, "名字");
        return userId;
      }
    }
  }
</script>

<style scoped>
</style>