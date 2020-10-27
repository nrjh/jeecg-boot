<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
<!--        <span>基础信息</span>-->
        <div class="dy-box" style="border:1px solid #dadada;padding:20px 10px;border-radius: 5px;margin:15px 0;position:relative;" title="基础信息">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="物料编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'productNo', validatorRules.productNo]" placeholder="请输入物料编号"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="物料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'productName', validatorRules.productName]" placeholder="请输入物料名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="缩写" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="[ 'productNameAbb', validatorRules.productNameAbb]" placeholder="请输入物料名称缩写"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="8">
                <a-form-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <JDictSelectTagIop v-decorator="['brandId', validatorRules.brandId]"
                                                :triggerChange="true"
                                                placeholder="请选择品牌"
                                                dictCode="prd_brand,name,id,is_del='0'"/>
                </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JDictSelectTagIop v-decorator="['categoryId', validatorRules.categoryId]"
                                   :triggerChange="true"
                                   placeholder="请选择品类"
                                   dictCode="prd_category,name,id,is_del='0'"
                                   @change="changeLine"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
                <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--                    <JDictSelectTagIop v-decorator="['attributeCategoryId', validatorRules.attributeCategoryId]"-->
<!--                                                :triggerChange="true"-->
<!--                                                placeholder="请选择规格"-->
<!--                                                dictCode="prd_attribute_category,name,id"/>-->
                  <a-select  v-decorator="[ 'attributeCategoryId', validatorRules.attributeCategoryId]">
                    <a-select-option v-for="item in attributeCategoryList" :key="item.value" :value="item.value">
                      {{ item.text }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <JDictSelectTagIop v-decorator="['uomId', validatorRules.uomId]"
                                                :triggerChange="true"
                                                placeholder="请选择单位"
                                                dictCode="uom_uom,name,id"/>
                </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item label="货位" :labelCol="{span:1}" :wrapperCol="wrapperCol">
                <!--                    <JDictSelectTagIop v-decorator="['locationId', validatorRules.locationId]"-->
                <!--                                                :triggerChange="true"-->
                <!--                                                placeholder="请选择货位"-->
                <!--                                                dictCode="stk_location,name,id"/>-->
                <a-cascader
                  v-decorator="['locationId', validatorRules.locationId]"
                  :field-names="{ label: 'name', value: 'id', children: 'children' }"
                  :options="location"
                  placeholder="请选择货位"
                  width="100%"
                />
              </a-form-item>
            </a-col>
          </a-row>

        </div>
<!--        <span>扩展信息</span>-->
        <div  class="dy-box" style="border:1px solid #dadada;padding:20px 10px;border-radius: 5px;margin:25px 0;position:relative;" title="扩展信息">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="初始数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="[ 'initQty', validatorRules.initQty]" placeholder="请输入初始数量" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="初始总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="[ 'initAmount', validatorRules.initAmount]" placeholder="请输入初始总金额" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-upload v-decorator="['photo', validatorRules.photo]" :trigger-change="true" rows="4"></j-upload>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="最低库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="[ 'stockMin', validatorRules.stockMin]" placeholder="请输入最低库存" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="最高库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="[ 'stockMax', validatorRules.stockMax]" placeholder="请输入最高库存" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="警戒库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="[ 'stockWarn', validatorRules.stockWarn]" placeholder="请输入警戒库存" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <span>备注</span>
        <div style="margin:15px 0;">
          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="{span:1}" :wrapperCol="{span:24}">
                <a-textarea v-decorator="[ 'remark', validatorRules.remark]" placeholder="请输入备注" rows="4" ></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
        </div>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { translateDataToTree, getIdPath } from '../util/tree'
  import { validateDuplicateValue } from '@/utils/util'
  import JUpload from '@/components/jeecg/JUpload'
  import JDate from '@/components/jeecg/JDate'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JMultiSelectTagIop from '@/components/dict/JMultiSelectTagIop'
  import { initDictOptionsIop ,filterDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "StkOfficeSuppliesModal",
    components: {
      JUpload,ATextarea, JDate,JDictSelectTagIop,JDictSelectTag,initDictOptionsIop
    },
    data () {
      return {
        location: [],
        form: this.$form.createForm(this),
        title:"操作",
        width:1200,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          productNo: {rules: [
          ]},
          productName: {rules: [
          ]},
          productNameAbb: {rules: [
          ]},
          categoryId: {rules: [
          ]},
          brandId: {rules: [
          ]},
          attributeCategoryId: {rules: [
          ]},
          uomId: {rules: [
          ]},
          locationId: {rules: [
          ]},
          stockMin: {rules: [
          ]},
          stockMax: {rules: [
          ]},
          joinParts: {rules: [
          ]},
          initQty: {rules: [
          ]},
          initAmount: {rules: [
          ]},
          stockWarn: {rules: [
          ]},
          remark: {rules: [
          ]},
          photo: {rules: [
          ]},
        },
        attributeCategoryList:[],
        url: {
          add: "/officesupplies/stkOfficeSupplies/add",
          edit: "/officesupplies/stkOfficeSupplies/edit",
          locList: '/stk/in/stockInOrder/locList',
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'productNo','productName','productNameAbb','categoryId','attributeCategoryId','uomId','statusPro','locationId','stockMin','stockMax','joinParts','initQty','initAmount','stockWarn','remark','photoString','priceAvg'))
        })

        getAction(this.url.locList).then(res => {
            // this.locationOld = res.result
            console.log("res.result----",res.result)
            this.location = translateDataToTree(res.result)
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
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
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'productNo','productName','productNameAbb','categoryId','attributeCategoryId','uomId','statusPro','locationId','stockMin','stockMax','joinParts','initQty','initAmount','stockWarn','remark','photoString','priceAvg'))
      },

      changeLine(value){
        this.attributeCategoryList = [];
        this.form.setFieldsValue({attributeCategoryId:''})
        let str="prd_attribute_category,name,id,category_id='"+value+"' and is_del='0'";
        initDictOptionsIop(str).then(res => {
          console.log("res.result----------",res.result)
          this.attributeCategoryList=res.result;
        })
      }


    }
  }
</script>