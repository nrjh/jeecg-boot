<template>
<!-- 查看按钮弹出表单 办公用品查看页面-->
  <a-modal
    :title="title1"
    :width="width"
    :visible="visiblelookOfficeSuppliesModal"
    @cancel="handleCancelCheck"
    @ok="handleOKCheck"
    cancelText="关闭">
<!--      <span>基础信息</span>-->
      <div class="dy-box" style="border:1px solid #dadada;padding:20px 10px;border-radius: 5px;margin:15px 0;position:relative;" title="基础信息">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="物料编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="productNo" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="物料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="productName" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="缩写" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input v-model="productNameAbb" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
              <a-form-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop
                                              placeholder=""
                                              disabled="disabled"
                                              dictCode="prd_brand,name,id" v-model='brandId' style="width: 100%"/>
              </a-form-item>
          </a-col>
          <a-col :span="8">
              <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop
                                              placeholder=""
                                              disabled="disabled"
                                              dictCode="prd_attribute_category,name,id" v-model='attributeCategoryId' style="width: 100%"/>
              </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="品类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop
                                              placeholder=""
                                              disabled="disabled"
                                              dictCode="prd_category,name,id" v-model='categoryId' style="width: 100%"/>
              </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop
                                              placeholder=""
                                              disabled="disabled"
                                              dictCode="uom_uom,name,id" v-model='uomId' style="width: 100%"/>
              </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="货位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <JDictSelectTagIop
                                              placeholder=""
                                              disabled="disabled"
                                              dictCode="stk_location,name,id" v-model='locationId' style="width: 100%"/>
              </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="关联设备" :labelCol="{span:2}" :wrapperCol="wrapperCol">
            <a-input v-model="joinParts" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </div>
<!--      <span>扩展信息</span>-->
      <div  class="dy-box" style="border:1px solid #dadada;padding:20px 10px;border-radius: 5px;margin:25px 0;position:relative;" title="扩展信息">
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="初始数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="initQty" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="初始总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="initAmount" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-upload  rows="4" v-model='photo' disabled="disabled"></j-upload>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="最低库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input v-model="stockMin" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="最高库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input v-model="stockMax" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="警戒库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input v-model="stockWarn" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </div>
      <span>备注</span>
      <div style="margin:15px 0;">
        <a-row :gutter="24">
        <a-col :span="24">
          <a-form-item :labelCol="{span:1}" :wrapperCol="{span:24}">
             <a-textarea v-model="remark" placeholder="" rows="4" disabled="disabled"></a-textarea>
          </a-form-item>
        </a-col>
      </a-row>
      </div>

  </a-modal>

</template>

<script>
import { httpAction , getAction } from '@/api/manage'
import JUpload from '@/components/jeecg/JUpload'
import ATextarea from "ant-design-vue/es/input/TextArea";
import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
import JDictSelectTag from '@/components/dict/JDictSelectTag'
export default {
  components: {
    name: "lookOfficeSuppliesModal",
    JUpload,ATextarea, JDictSelectTagIop,JDictSelectTag
  },
  data() {
    return {
      title1:"查看",
      width:1200,
      hight:800,
      sparePartsId: '',
      //dataSourceLook: [],
      productNo: '',
      productName: '',
      productNameAbb: '',
      brandId: '',
      attributeCategoryId: '',
      categoryId: '',
      uomId: '',
      locationId: '',
      joinParts: '',
      initQty: '',
      initAmount: '',
      photo: '',
      stockMin: '',
      stockMax: '',
      stockWarn: '',
      remark: '',
      visiblelookOfficeSuppliesModal: false,
      labelCol: {
        span: 6
      },
      wrapperCol: {
        span: 16
      },

      url: {
        find:"/officesupplies/stkOfficeSupplies/queryById",//主表 表单回显数据
      },
    };
  },
  methods: {
    handleCancelCheck(){
      this.visiblelookOfficeSuppliesModal=false;
    },
    handleOKCheck(){
      this.visiblelookOfficeSuppliesModal=false;
    },
   showCheck(sparePartsId){
     this.visiblelookOfficeSuppliesModal=true;

     //根据备品备件ID查询主表信息回显到表单
     getAction(this.url.find,{id:sparePartsId}).then((res)=>{
       if(res.success){
         this.productNo=res.result.productNo;
         this.productName=res.result.productName;
         this.productNameAbb=res.result.productNameAbb;
         this.brandId=res.result.brandId;
         this.attributeCategoryId=res.result.attributeCategoryId;
         this.categoryId=res.result.categoryId;
         this.uomId=res.result.uomId;
         this.locationId=res.result.locationId;
         this.joinParts=res.result.joinParts;
         this.initQty=res.result.initQty;
         this.initAmount=res.result.initAmount;
         this.stockMin=res.result.stockMin;
         this.stockMax=res.result.stockMax;
         this.stockWarn=res.result.stockWarn;
         this.remark=res.result.remark;
         this.photo=res.result.photo;
       }else{
         console.log(res.message);
       }
     });


    },
    onCellChange(key, dataIndex, value) {
      const dataSourceLook = [...this.dataSourceLook];
      const target = dataSourceLook.find(item => item.key === key);
      if (target) {
        target[dataIndex] = value;
        this.dataSourceLook = dataSourceLook;
      }
    },
  },
};
</script>
<style>
  .dy-box::before{
    content:attr(title);
    position:absolute;
    left:5%;
    top:-0.5vh;
    transform:translateX(-50%);
    -webkit-transform:translate(-50%,-50%);
    padding:0 10px;
    background-color:#fff;
  }
</style>

