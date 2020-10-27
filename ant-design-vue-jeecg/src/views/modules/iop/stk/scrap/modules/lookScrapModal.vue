<template>
<!-- 查看按钮弹出表单-->
  <a-modal
    :title="title1"
    :width="width"
    :visible="visibleLookScrapModel" 
    @cancel="handleCancelCheck"
    @ok="handleOKCheck"
    cancelText="关闭">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="报废盘点单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="scrapInventoryOrder" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="申请人" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input v-model="applicationWorker" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申请日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="applicationDate" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
         <a-row :gutter="24">
        <a-col :span="24">
            <a-form-item label="申请说明" :labelCol="{span:3}" :wrapperCol="{span:20}">
              <a-input v-model="applicationAbout" rows="4" placeholder="" disabled="disabled"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      <!-- 这里加上添加物料信息的table数据   查看页面需要增加合计行-->      
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columnsLook"
        :dataSource="dataSourceLook"
        :pagination="false"
      >
      </a-table>
      <div><span>车间: ---------{{updateTime+updateBy}}提交---------</span></div>
      <div><span>车间: ---------{{sureTime+sureBy}}确认---------</span></div>  
      <div><span>车间: ---------{{shenheTime+shenheBy}}审核---------</span></div>    
  </a-modal>
  
</template>

<script>
import { httpAction , getAction } from '@/api/manage'
export default {
  components: {
    name: "lookScrapModal",
  },
  data() {
    return {
      title1:"查看",
      width:1200,
      hight:800,
      scrapId: '',
      dataSourceLook: [],
      scrapInventoryOrder: '',
      applicationWorker: '',
      applicationDate: '',
      applicationAbout: '',
      updateTime:'',
      updateBy:'',
      sureTime:'',
      sureBy:'',
      shenheTime:'',
      shenheBy:'',
      visibleLookScrapModel: false,
      labelCol: {
        span: 6
      },
      wrapperCol: {
        span: 16
      },
     
      columnsLook: [
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
                  dataIndex: 'rpoductName'
                },
                {
                  title:'物料编号',
                  align:"center",
                  dataIndex: 'productNo'
                },
                {
                  title:'规格',
                  align:"center",
                  dataIndex: 'attributeCategoryID'
                },
                {
                  title:'单位',
                  align:"center",
                  dataIndex: 'productUomID'
                },
                {
                  title:'供应商',
                  align:"center",
                  dataIndex: 'vendorID'
                },
                {
                  title:'库存状况',
                  align:"center",
                  dataIndex: 'locationID'
                },
                {
                  title:'库存件数',
                  align:"center",
                  dataIndex: 'productQty'
                },
                {
                  title:'报废件数',
                  align:"center",
                  dataIndex: 'scrapQty'
                },
                 {
                  title:'金额',
                  align:"center",
                  dataIndex: 'price'
                }               
              ],
      url: {
        find:"/scrap/stkScrap/look",//主表 表单回显数据
        look:"/scrap/stkScrap/lookList",//查看物料列表
      },
    };
  },
  methods: {
    handleCancelCheck(){
      this.visibleLookScrapModel=false;
      this.dataSourceLook = [];
    },
    handleOKCheck(){
      this.visibleLookScrapModel=false;
      this.dataSourceLook = [];
    },
   showCheck(scrapId,v){
     this.visibleLookScrapModel=true;
     let InventoryOrder="";
     console.log('---this.v--',v);
     //根据报废单ID查询主表信息回显到表单
     getAction(this.url.find,{id:scrapId}).then((res)=>{
       if(res.success){
         this.scrapInventoryOrder=res.result.scrapInventoryOrder;
         InventoryOrder=res.result.scrapInventoryOrder
         this.applicationWorker=res.result.applicationWorker;
         this.applicationDate=res.result.applicationDate;
         this.applicationAbout=res.result.applicationAbout;
         this.updateTime=res.result.updateTime;
         this.updateBy=res.result.updateBy;
         this.sureTime=res.result.sureTime;
         this.sureBy=res.result.sureBy;
         this.shenheTime=res.result.shenheTime;
         this.shenheBy=res.result.shenheBy;
        //根据报废单号查询物料信息列表
        console.log("报废单号:",InventoryOrder);
        getAction(this.url.look,{scrapInventoryOrder:InventoryOrder}).then((res)=>{
          if(res.success){
            this.dataSourceLook=res.result;
          }else{
            console.log(res.message);
          }
        });
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

