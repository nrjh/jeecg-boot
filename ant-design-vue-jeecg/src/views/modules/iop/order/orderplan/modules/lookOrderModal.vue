<template>
  <div>
    <a-modal
      :title="title"
      :width="1200"
      :visible="visible"
      @cancel="handleCancel"
      :footer="null"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item label="申请单号" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input  v-decorator="[ 'pickingNo', validatorRules.pickingNO]" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="申请人 " :labelCol="{span:9}" :wrapperCol="{span:15}">
                <a-input v-decorator="[ 'createBy', validatorRules.createby]" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="申请日期" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-time  placeholder="" v-decorator="[ 'createTime', validatorRules.createtime]" :trigger-change="true" style="width: 100%" disabled="disabled"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="8">
              <a-form-item label="检定线 " :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['lineId', validatorRules.lineId]"
                                       :trigger-change="true" dictCode="O_AREA,AREA_NAME,AREA_ID" placeholder="请选择检定线"
                                       disabled="disabled"/>
<!--                <a-input v-show="false" v-decorator="['lineName']"></a-input>-->
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="班组" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-dict-select-tag-ora type="list" v-decorator="['groupNo', validatorRules.groupNo]"
                                       :trigger-change="true" dictCode="O_SHIFT,SHIFT_NAME,SHIFT_ID" placeholder="请选择班组"
                                       disabled="disabled"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="预计时间" :labelCol="{span:9}" :wrapperCol="{span:15}">
                <j-date  placeholder="" v-decorator="[ 'scheduledDate', validatorRules.scheduledDate]" :trigger-change="true" style="width: 100%" disabled="disabled"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="申请类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-radio-group v-decorator="['categoryType',validatorRules.categoryType]" disabled="disabled">
                  <a-radio value="equip">
                    生产设备
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
              <a-form-item label="单据类型" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-radio-group v-decorator="['orderType',validatorRules.orderType]" disabled="disabled">
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

          <a-row :gutter="16">
            <a-col :span="24">
              <a-form-item :labelCol="{span:3}" :wrapperCol="{span:18}"
                label="领料说明"
                hasFeedback>
                <a-textarea :rows="3" placeholder="..." v-decorator="[ 'note', { rules: [] } ]"  disabled="disabled" />
              </a-form-item>
            </a-col>
          </a-row>

          <!-- 这里加上添加物料弹出框的自定义表单-->
          <a-table
            ref="editableTable"
            size="middle"
            bordered
            :visible="visibleProd"
            :columns="columns"
            :dataSource="dataSourceProd"
          >
            <template slot="productUomQty" slot-scope="text,record,index">
              <a-input-number v-model="dataSourceProd[index].productUomQty" disabled="disabled"/>
            </template>
            <template slot="vendorCode" slot-scope="text,record,index">
              <j-dict-select-tag-iop type="list"  v-model="dataSourceProd[index].partnerId" disabled="disabled"
                                     :trigger="true" dictCode="prd_brand,manufactor_name,id,is_del='0'" placeholder="请选择供应商"/>
            </template>
          </a-table>
        </a-form>
      </a-spin>
    </a-modal>

  </div>
</template>

<script>
  import { httpAction , getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JMultiSelectTagIop from '@/components/dict/JMultiSelectTagIop'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import JTime from '@/components/jeecg/JTime'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'


  export default {
    name: "lookOrderModal",
    components: {
      ATextarea, JDate,JDictSelectTagIop,JDictSelectTag,JMultiSelectTagIop,JEditableTable,JTime,JDictSelectTagOra
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        dataSource: [],
        dataSourceProd: [],
        visibleProd:false,
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        formItemLayoutWithOutLabel: {
          wrapperCol: {
            xs: { span: 24, offset: 0 },
            sm: { span: 20, offset: 4 },
          },
        },
        confirmLoading: false,
        validatorRules: {
          name: {rules: [
          ]},
          dateDone: {rules: [
          ]},
          taspickingNOkNo: {rules: [
          ]},
          scheduledDate: {rules: [
          ]},
          createtime: {rules: [
          ]},
          groupNo: {rules: [
          ]},
          remark: {rules: [
          ]},
          stockLocationId: {rules: [
          ]},
          orderType: {rules: [
            {required: true, message: ''},
          ]},
        },
        columns: [
          {
            title:'物料名称',
            align:"center",
            key: 'rpoductName',
            dataIndex:'rpoductName'
          },
          {
            title:'物料编号',
            align:"center",
            key: 'productNo',
            dataIndex:'productNo'
          },
          {
            title:'类别',
            align:"center",
            key: 'cateName',
            dataIndex:'cateName'
          },
          {
            title:'规格',
            align:"center",
            key: 'acName',
            dataIndex:'acName'
          },
          {
            title:'单位',
            align:"center",
            key: 'uuName',
            dataIndex:'uuName'
          },
          {
            title:'申请件数',
            align:"center",
            key:'productUomQty',
            scopedSlots: { customRender: 'productUomQty' }
          },
          {
            title:'供应商',
            align:"center",
            key: 'vendorId',
            width: '12%',
            scopedSlots: { customRender: 'vendorCode' }
          },
        ],
        url: {
          add: "/iop/order/orderPlan/add",
          edit: "/iop/order/orderPlan/edit",
          queryById: "/iop/order/orderPlan/queryById",
          queryStkMoveByMainId: "/iop/order/orderPlan/queryStkMoveByMainId",
        }
      }
    },
    beforeCreate() {
      this.form = this.$form.createForm(this, { name: 'dynamic_form_item' });
      this.form.getFieldDecorator('keys', { initialValue: [], preserve: true });
    },
    created () {
    },
    methods: {
      look (v) {
        console.log('-----',v);
        this.form.resetFields();
        getAction(this.url.queryById,{id:v}).then((res)=>{
          console.log('-----',res);
          if(res.success){
            this.model = Object.assign({}, res.result);
            this.visible = true;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'pickingNo','createTime','createBy',
                'groupNo','scheduledDate','categoryType','orderType','note','lineId','vendorId'))
            })
          }
        });
        getAction(this.url.queryStkMoveByMainId,{id:v}).then((res)=>{
          if(res.success){
            this.dataSourceProd=res.result;
            console.log("返回值111111",this.dataSourceProd)
          }
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.visibleProd = false;
        this.dataSourceProd=[];
        this.$emit('onClear');
      },
      handleCancel () {
        this.close()
      },

    }
  }

</script>