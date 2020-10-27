<template>
  <div>
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
          <a-row>
            <a-form-item label="货位调整单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'orderMoveId', validatorRules.orderMoveId]" placeholder="请输入移库单号（源单据）" disabled autocomplete="off"></a-input>
            </a-form-item>
          </a-row>

          <a-row :span="24">
            <a-col :span="12">
              <a-form-item label="调整人名称" :labelCol="{span:10}" :wrapperCol="{span:12}">
                <a-input v-decorator="[ 'createOutName', validatorRules.createOutName]" placeholder="请输入调整人名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="调整日期" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <j-date placeholder="请选择调整日期" v-decorator="[ 'createOutTime', validatorRules.createOutTime]" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-textarea v-decorator="['note', validatorRules.note]" rows="4" placeholder="请输入备注"/>
            </a-form-item>
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
          :visible="visibleProd"
          :columns="columns"
          :dataSource="dataSourceProd"
        >
          <template slot="productMoveQty" slot-scope="text,record,index">
            <a-input-number v-if="editable" v-model="dataSourceProd[index].productMoveQty" :min="0" :max="dataSourceProd[index].virtualQty"/>
            <span v-else>{{dataSourceProd[index].productMoveQty}}</span>
          </template>



          <template slot="action" slot-scope="text,record,index">
            <a @click="handleDistribution(index)">分配货位</a>
            <a-divider type="vertical" />
            <a-popconfirm v-if="editable" title="确定删除吗?" @confirm="() => handleDelete(index)">
              <a>删除</a>
            </a-popconfirm>
          </template>

        </a-table>
      </a-spin>
    </a-modal>
    <goodsPlaceAddProdModal ref="goodsPlaceAddProdModal" @returnData="returnData"></goodsPlaceAddProdModal>
    <!--    分配货位 -->
    <goods-distribution-prod-modal ref="distributionProdFrom" @returnData="distributionReturnData"></goods-distribution-prod-modal>
  </div>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import goodsPlaceAddProdModal from './GoodsPlaceAddProdModal'
  import { validateDuplicateValue } from '@/utils/util'
  import GoodsDistributionProdModal from './GoodsDistributionProdModal'
  import JDate from '@/components/jeecg/JDate'
  import moment from 'moment'
  import { getAction } from '../../../../../../api/manage'
  import store from '@/store'
  export default {
    name: "StockMoveOrderModal",
    components: {
      JDate,
      goodsPlaceAddProdModal,
      GoodsDistributionProdModal,
    },
    data () {
      const vm = this
      return {
        form: this.$form.createForm(this),
        title:"操作",
        // 规格
        tempCategoryNameList:[],
        //  单位
        uomUomList:[],
        width:1500,
        visible: false,
        // model: {},
        visibleProd: false,
        dataSource:[],
        isEdit:false,
        editable:true,
        dataSourceProd: [],
        tableScroll:{x :47*30+50,y:350},
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
                title: '物料类别',
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
                dataIndex:'attributeCategoryId',
                customRender:function (text) {
                    return vm.getCategoryName(text);
                }
            },
            // {
            //     title: '标识',
            //     align: "center",
            //     dataIndex:'stockInOrderId',
            // },
            {
                title: '单位',
                align: "center",
                dataIndex: 'productUomId',
                customRender:function (text) {
                    return vm.getUomUomName(text);
                }
            },
            {
                title: '供应商',
                align: "center",
                dataIndex: 'partnerName',
            },
            {
                title: '移出货位',
                align: "center",
                dataIndex: 'unkonwColum',
            },
            {
                title: '库存件数',
                align: "center",
                dataIndex: 'virtualQty',
            },
            {
                title: '计划移出件数',
                align: "center",
                dataIndex: 'productMoveQty',
                scopedSlots: { customRender: 'productMoveQty' }
            },
            {
                title: '剩余移出件数',
                align: "center",
                dataIndex: 'productActualQty',
                customRender:function (t,r,index) {
                    return vm.surplusCompute(index)
                }
            },
            {
                title: '已分配货位详情',
                align: "center",
                dataIndex: 'whInputStockLotName',
            },
            {
                title: '状态',
                align: "center",
                dataIndex: 'state',
                customRender:function (value) {
                    if(value === 'normal'){
                        return '正常';
                    } else if(value === 'freeze') {
                        return '冻结'
                    }
                }
            },
            {
                title: '操作',
                dataIndex: 'action',
                align: "center",
                width:"150px",
                scopedSlots: { customRender: 'action' }
            },
        ],
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
          orderMoveId: {rules: [
            {required: true, message: '请输入移库单号（源单据）!'},
          ]},
          createOutBy: {rules: [
            {required: true, message: '请输入调整人ID!'},
          ]},
          createOutName: {rules: [
            {required: true, message: '请输入调整人名称!'},
          ]},
          createOutTime: {rules: [
            {required: true, message: '请输入调整日期!'},
          ]},
          note: {rules: [
          ]},
        },
        url: {
          add: "/stk/goodsplaceadjustment/add",
          queryByOrderMoveId: "/stk/goodsplaceadjustment/queryByOrderMoveId",
          edit: "/stk/goodsplaceadjustment/edit",
          attributeCategoryNameList:"/iop/prd/attrcategory/attributeCategoryNameList",
          uomUomList:"/iop/uom/category/uomUomList"
        }
      }
    },
    created () {
        this.getAttributeCategoryNameList();
        this.getUomUomList();
    },
    methods: {
      // 获取规格字典
      getAttributeCategoryNameList(){
          getAction(this.url.attributeCategoryNameList,{}).then((res) => {
              if (res.success) {
                  this.tempCategoryNameList =  res.result
              }else {
                  this.tempCategoryNameList =  []
              }
          })
      },
      // 获取单位字典
      getUomUomList(){
          getAction(this.url.uomUomList,{}).then((res) => {
              if (res.success) {
                  this.uomUomList =  res.result
              }else {
                  this.uomUomList =  []
              }
          })
      },
      // 根据规格id获取规格名称
      getCategoryName(text){
          for(var i = 0 ;i<this.tempCategoryNameList.length ; i++){
                if(text === this.tempCategoryNameList[i].id){
                    return this.tempCategoryNameList[i].name
                }
          }
      },
      // 根据单位id获取规格名称
      getUomUomName(text){
          for(var i = 0 ;i<this.uomUomList.length ; i++){
              if(text === this.uomUomList[i].id){
                  return this.uomUomList[i].name
              }
          }
      },
      // 分配货位
      handleDistribution(index) {
          console.log("index",index)
          let pordData=this.dataSourceProd[index]
          console.log("pordData",pordData)
          let proInfo = this.dataSourceProd[index]
          let orderInfo = {}
          // 供应商
          orderInfo.partnerid = pordData.partnerId
          // 保管员
          orderInfo.createName = this.userName
          // 物料名称
          orderInfo.productName = pordData.rpoductName
          // 类别
          orderInfo.categoryType = pordData.categoryType
          // 规格
          orderInfo.attributeCategoryId = pordData.attributeCategoryId
          //
          orderInfo.productActualQty = pordData.virtualQty
          // if(!orderInfo.whInputStockLotName){
          //     orderInfo.whInputStockLotName = ""
          // }
          orderInfo.whInputStockLotName = pordData.whInputStockLotName
          orderInfo.whInputStockLotId = pordData.whInputStockLotId
          //
          orderInfo.orderStockInLists = this.dataSourceProd[index].orderStockInLists
          this.activeIndex = index
          console.log("传送的货位信息:",orderInfo)
          this.$refs.distributionProdFrom.add(orderInfo, this.editable)
      },
      distributionReturnData(data, status) {
          let tempDataSourceProd = []
          tempDataSourceProd = this._.cloneDeep(this.dataSourceProd);
          tempDataSourceProd[this.activeIndex].whInputStockLotName = ""
          tempDataSourceProd[this.activeIndex].whInputStockLotId = ""
          tempDataSourceProd[this.activeIndex].productMoveQty = 0
          console.log("分配货位：",data)
          for(var j = 0 ;j<data.length;j++){
              // stk_location的name
              if(tempDataSourceProd[this.activeIndex].whInputStockLotName === ""){
                  tempDataSourceProd[this.activeIndex].whInputStockLotName = data[j].whInputStockLotName+":"+data[j].productActualQty
              }else{
                  tempDataSourceProd[this.activeIndex].whInputStockLotName = tempDataSourceProd[this.activeIndex].whInputStockLotName+","+data[j].whInputStockLotName+":"+data[j].productActualQty
              }
              // stk_location的id
              if(tempDataSourceProd[this.activeIndex].whInputStockLotId === ""){
                  tempDataSourceProd[this.activeIndex].whInputStockLotId = data[j].warehouse.join("-")+":"+data[j].whInputStockLotId
              }else{
                  tempDataSourceProd[this.activeIndex].whInputStockLotId = tempDataSourceProd[this.activeIndex].whInputStockLotId+","+data[j].warehouse.join("-")+":"+data[j].whInputStockLotId
              }
              tempDataSourceProd[this.activeIndex].productMoveQty = parseInt(tempDataSourceProd[this.activeIndex].productMoveQty) + parseInt(data[j].productActualQty)
              tempDataSourceProd[this.activeIndex].state = status
          }
          console.log("tempDataSourceProd---",tempDataSourceProd)
          this.dataSourceProd = tempDataSourceProd
          // this.surplusCompute(this.activeIndex)
      },
      handleDelete(id){
          this.dataSourceProd.splice(id,1);
      },
      surplusCompute(index){
          return (this.dataSourceProd[index].virtualQty - this.dataSourceProd[index].productMoveQty)
      },
      handleAddProd(){
        this.$refs.goodsPlaceAddProdModal.add();
      },
      returnData(data) {
          console.log("选择的物料信息:",data)
          this.visibleProd = true;
          for(var i = 0;i<data.length;i++){
              // if(data[i].productQty ==0){
              //     data.splice(i,1);
              // }
              data[i].productMoveQty = 0
          }
          this.dataSourceProd = this.dataSourceProd.concat(data);
      },
      add () {
        this.isEdit = false
        this.edit({});
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.form.resetFields();
        // 如果是新增
        if(!record.orderMoveId){
            let now = moment().format("YYYYMMDDHHmmss")
            let pkId = "HWTZ" + now
            this.model.orderMoveId = pkId
            // 调整日期
            this.model.createOutTime = moment().format("YYYY-MM-DD")
            // 调整人
            let userName = store.getters.userInfo.realname
            this.model.createOutName = userName
        }
        this.visible = true;
        // 如果是修改
        if(record.orderMoveId){
            // 根据orderMoveId获取物料列表
            this.isEdit = true
            getAction(this.url.queryByOrderMoveId,{orderMoveId:record.orderMoveId}).then((res) => {
                if(res.success){
                    let tempPrdData = []
                    for(var i = 0 ;i < res.result.stockMoveOrderLineList.length ; i++){
                        var tempList = {}
                        //物品id
                        tempList.productNo = res.result.stockMoveOrderLineList[i].productId
                        //物品名称
                        tempList.rpoductName = res.result.stockMoveOrderLineList[i].productName
                        //物品类型
                        tempList.categoryType = res.result.stockMoveOrderLineList[i].categoryType
                        //规格
                        tempList.acName = res.result.stockMoveOrderLineList[i].attributeCategoryId_dictText
                        //移出库位置名称
                        tempList.whInputStockLotName = res.result.stockMoveOrderLineList[i].whInputStockLotName
                        // 移出库位置id
                        tempList.whInputStockLotId = res.result.stockMoveOrderLineList[i].whInputStockLotId
                        //移出库前库数量
                        tempList.virtualQty = res.result.stockMoveOrderLineList[i].productBeforeActualQty
                        //计划移库数量
                        tempList.productMoveQty = res.result.stockMoveOrderLineList[i].productMoveQty
                        //供应商名称
                        tempList.partnerName = res.result.stockMoveOrderLineList[i].partnerName
                        // 单位
                        tempList.productUomId = res.result.stockMoveOrderLineList[i].productUomId
                        //状态
                        tempList.state = res.result.stockMoveOrderLineList[i].status
                        // 规格
                        tempList.attributeCategoryId =  res.result.stockMoveOrderLineList[i].attributeCategoryId
                        console.log("从后端查到的数据：",tempList)

                        tempPrdData.push(tempList);
                    }
                    this.dataSourceProd = tempPrdData
                }else{
                    that.$message.warning(res.message);
                }
            })
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'orderMoveId','createOutBy','createOutName','createOutTime','note'))
        })

      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.dataSourceProd=[];
        this.model=[];
      },
      handleOk () {
        const that = this;
        let pordData=this.dataSourceProd;
        if(pordData.length === 0){
            that.$message.warning("请选择物料");
            return
        }
        // 若是没有分配货位则不给保存
        for(var j = 0 ; j<pordData.length;j++){
            if(!pordData[j].whInputStockLotName){
                that.$message.warning("请选择货位");
                return
            }
        }

        let stockMoveOrderLineListData = []
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            // 如果不是修改
            if(!this.isEdit){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
          // 修改属性使之于后台字段名称一致
            for(var i = 0 ; i < pordData.length ;i++){
                var tempList = {}
                //移库单号
                tempList.orderId = values.orderMoveId
                //物品id
                tempList.productId = pordData[i].productNo
                //物品名称
                tempList.productName = pordData[i].rpoductName
                //物品类型
                tempList.categoryType = pordData[i].categoryType
                //规格Id
                tempList.attributeCategoryId = pordData[i].attributeCategoryId
                //移出库位置名称
                tempList.whInputStockLotName = pordData[i].whInputStockLotName
                //id
                tempList.whInputStockLotId = pordData[i].whInputStockLotId
                //移出库前库数量
                tempList.productBeforeActualQty = pordData[i].virtualQty
                //计划移库数量
                tempList.productMoveQty = pordData[i].productMoveQty
                //供应商名称
                tempList.partnerName = pordData[i].partnerName
                //物品计量单位
                tempList.productUomId = pordData[i].productUomId
                //状态
                tempList.status = pordData[i].state
                stockMoveOrderLineListData.push(tempList)
            }


            let obj={
                stockMoveOrderLineList : stockMoveOrderLineListData
            };

            let formData = []
            formData = Object.assign(obj, values);
            console.log("表单提交数据",formData)
            let tempState = "normal"
            for(var k = 0 ;k<stockMoveOrderLineListData.length;k++){
                if(stockMoveOrderLineListData[k].state!==tempState){
                    tempState = "freeze"
                    break;
                }
            }
            formData.state = tempState
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
        this.form.setFieldsValue(pick(row,'orderMoveId','createOutBy','createOutName','createOutTime','note','state','updateInBy','updateInName','updateInTime','inState','	inBy','inName','	inTime'))
      },


    }
  }
</script>