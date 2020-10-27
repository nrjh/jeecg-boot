<template>
  <div>
    <a-modal
      title=""
      :width="1000"
      :visible="visibleModelLook"
      :footer="null"
      @cancel="handleCancelCheck">
      <a-row :gutter="24">
        <a-col :span="8">
          <a-form-item label="任务编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="taskNoCheck" placeholder="" disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="任务名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="nameCheck" placeholder="" disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="盘点人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input  v-model="execByCheck"  placeholder="" disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
      </a-row>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button type="primary" icon="download" @click="handleExportXls('盘点清单')">导出</a-button>
        <a-button v-print="'#printContentLook'" ghost  type="primary">打印</a-button>
      </div>

      <a-table
        size="middle"
        bordered
        rowKey="id"
        style="padding: 20px"
        :columns="columnsTotal"
        :dataSource="dataSourceTotal"
        :pagination="false"
      >
      </a-table>

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

      <section ref="print" id="printContentLook" class="abcdefg" v-show="printShow">
        <div style="text-align: center">
          <p style="font-size: 24px;font-weight: 800">盘点单数据打印</p>
        </div>
        <a-col :md="24" :sm="24">
          <div class="sign" style="text-align: left;height: inherit">
            <a-col :span="8" style="margin-top: 20px">
              <span>任务编码:</span>
              <a-input style="width: 60%" v-model="taskNoCheck"/>
            </a-col>
            <a-col :span="8" style="margin-top: 20px">
              <span>任务名称:</span>
              <a-input style="width: 60%" v-model="nameCheck"/>
            </a-col>
            <a-col :span="8" style="margin-top: 20px">
              <span>盘点人:</span>
              <a-input style="width: 60%" v-model="execByCheck"/>
            </a-col>
          </div>
        </a-col>

        <a-table
          :columns="columnsLook"
          :dataSource="dataSourceLook"
          :pagination="false"/>
      </section>
    </a-modal>

  </div>
</template>
<script>
  import { httpAction , getAction ,downFile} from '@/api/manage'
  import { parseTime } from "@/utils/setMethods"

export default {
  components: {
    name: "lookModal",
  },
  data() {
    return {
      dataSourceLook: [],
      dataSourceTotal: [],
      taskNoCheck: '',
      nameCheck: '',
      execByCheck: '',
      visibleModelLook: false,
      checkMess: false,
      inventoryId: '',
      printShow: false,
      labelCol: {
        span: 6
      },
      wrapperCol: {
        span: 16
      },
      columnsLook: [
        {
          title: '单位',
          dataIndex:'taskNo',
          align:"center"
        },
        {
          title:'材料类别',
          align:"center",
          dataIndex: 'categoryId_dictText'
        },
        {
          title:'材料编号',
          align:"center",
          dataIndex: 'productNo',
          customRender:function (text) {
            return !text?"":(text.length>10?text.substr(0,10):text)
          }
        },
        {
          title:'材料名称',
          align:"center",
          dataIndex: 'rpoductName',
          customRender:function (text) {
            return !text?"":(text.length>10?text.substr(0,10):text)
          }
        },
        {
          title:'货位',
          align:"center",
          dataIndex: 'locationId',
          customRender:function (text) {
            return !text?"":(text.length>10?text.substr(0,10):text)
          }
        },
        {
          title:'型号',
          align:"center",
          dataIndex: 'planStartTime'
        },
        {
          title:'规格	',
          align:"center",
          dataIndex: 'planEndTime',
          customRender:function (text) {
            return !text?"":(text.length>10?text.substr(0,10):text)
          }
        },
        {
          title:'数量',
          align:"center",
          dataIndex: 'productQty',
        },
        {
          title:'价格',
          align:"center",
          dataIndex: 'price'
        },
        {
          title:'状态',
          align:"center",
          dataIndex: 'status'
        },
        {
          title:'操作',
          align:"center",
          dataIndex: 'endTime',
          customRender:function (text) {
            return !text?"":(text.length>10?text.substr(0,10):text)
          }
        }
      ],
      columnsTotal:[
        {
          title: '序号',
          align: 'center',
          dataIndex: 'rowIndex',
          customRender: function (text, r, index) {
            return (text !== '合计') ? (parseInt(index) + 1) : text
          }
        },
        // {
        //   title:'单位',
        //   align:"center",
        //   dataIndex: 'status',
        //   customRender: function (text, r, index) {
        //     if(r != null){
        //       return text
        //     }
        //     return ""
        //   }
        // },
        // {
        //   title:'库房',
        //   align:"center",
        //   dataIndex: 'houseName'
        // },
        {
          title:'材料类型',
          align:"center",
          dataIndex: 'categoryId_dictText'
        },
        {
          title:'数量',
          align:"center",
          dataIndex: 'productTotalQty'
        },
        {
          title:'金额',
          align:"center",
          dataIndex: 'priceTotal'
        },

      ],
      url: {
        getInventoryProd: "StkInventory/stkInventory/getInventoryProd",
        queryById: "StkInventory/stkInventory/queryById",
        inventoryResult: "StkInventory/stkInventory/inventoryResult",
        exportXls: "StkInventory/stkInventory/exportXls",
        export:"StkInventory/stkInventory/export",
        getTotalByInventoryId: "StkInventory/stkInventory/getTotalByInventoryId",
      },
    };
  },
  methods: {
    handleOkCheck(){
      console.log('---this.dataSourceLook--'+this.dataSourceLook);
      let formData = Object.assign(this.dataSourceLook);
      console.log("表单提交数据",formData)
      httpAction(this.url.inventoryResult,formData,'put').then((res)=>{
        if(res.success){
          this.companyName='';
          this.visibleModelLook=false;
          this.dataSourceLook = [];
          this.inventoryId = '';
          this.printShow = false;
        }else{
          this.companyName='';
          this.visibleModelLook=false;
          this.dataSourceLook = [];
          this.inventoryId = '';
          this.printShow = false;
        }
      }).finally(() => {
      })
    },
    handleCancelCheck(){
      this.visibleModelLook=false;
      this.dataSourceLook = [];
      this.dataSourceTotal = [];
      this.printShow = false;
    },
   showCheck(inventoryId,v){
     this.visibleModelLook=true;
     this.inventoryId=inventoryId;
     console.log('---this.v--',v);
     this.printShow=true;
     getAction(this.url.getInventoryProd,{inventoryId:inventoryId,mark:v}).then((res)=>{
       if(res.success){
         this.dataSourceLook=res.result;
       }else{
         console.log(res.message);
       }
     });
     getAction(this.url.queryById,{id:inventoryId}).then((res)=>{
       if(res.success){
         this.taskNoCheck=res.result.taskNo;
         this.nameCheck=res.result.name;
         this.execByCheck=res.result.execBy;
       }else{
         console.log(res.message);
       }
     });

     getAction(this.url.getTotalByInventoryId,{inventoryId:inventoryId}).then((res)=>{
       if(res.success){
         this.dataSourceTotal=res.result;
         console.log(this.dataSourceTotal,"总计数据11111111111")
         this.tableAddTotalRow(this.columnsTotal, res.result);
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

//     handleExportXls(){
//       let formData = Object.assign(this.dataSourceLook);
//       console.log("导出参数",formData)
// //      let paramsStr = JSON.stringify(filterObj(param));
//        downFile(this.url.exportXls,{inventoryId:this.inventoryId}).then((data)=>{
//          console.log("导出数据",data)
//         if (!data) {
//           this.$message.warning("文件下载失败")
//           return
//         }
//         if (typeof window.navigator.msSaveBlob !== 'undefined') {
//           window.navigator.msSaveBlob(new Blob([data]), this.description+'.xls')
//         }else{
//           let url = window.URL.createObjectURL(new Blob([data]))
//           let link = document.createElement('a')
//           link.style.display = 'none'
//           link.href = url
//           link.setAttribute('download', this.description+'.xls')
//           document.body.appendChild(link)
//           link.click()
//           document.body.removeChild(link); //下载完成移除元素
//           window.URL.revokeObjectURL(url); //释放掉blob对象
//         }
//       })
//     },
    handleExportXls(){
      let formData = Object.assign(this.dataSourceLook);
      console.log("导出参数",formData)
//      let paramsStr = JSON.stringify(filterObj(param));
      getAction(this.url.export,{inventoryId:this.inventoryId}).then((res)=>{
        console.log("数据源",res.result)
        var excelDatas = [
          {
            tHeader: ["盘点单", "库位", "供应商", "物品id", "品牌","品类","物品名","物品编号","价格","数量","状态"], // sheet表一头部
            filterVal: ["inventoryId","locationId", "vendorId", "productId", "brandId","categoryId","rpoductName","productNo","price","productQty","status"], // 表一的数据字段
            tableDatas: res.result, // 表一的整体json数据
            sheetName: "sheet1"// 表一的sheet名字
          },
          {
            tHeader: ["盘点单", "库位", "供应商", "物品id", "品牌","品类","物品名","物品编号","价格","数量","状态"],
            filterVal: ["inventoryId","locationId", "vendorId", "productId", "brandId","categoryId","rpoductName","productNo","price","productQty","status"],
            tableDatas: res.result,
            sheetName: "sheet2"
          }
          // {
          //   tHeader: ["序号", "名字", "描述"],
          //   filterVal: ["userId", "userName", "userDescription"],
          //   tableDatas: this.rolesList,
          //   sheetName: "sheet5555"
          // }
        ]

        this.json2excel(excelDatas, "数据报表", true, "xlsx")
      })

    },
    json2excel (tableJson, filenames, autowidth, bookTypes) {
      var that = this
      //这个是引用插件
      import("@/excel/Export2Excel").then(excel => {
        var tHeader = []
        var dataArr = []
        var sheetnames = []
        for (var i in tableJson) {
          tHeader.push(tableJson[i].tHeader)
          dataArr.push(that.formatJson(tableJson[i].filterVal, tableJson[i].tableDatas))
          sheetnames.push(tableJson[i].sheetName)
        }
        excel.export_json_to_excel({
          header: tHeader,
          data: dataArr,
          sheetname: sheetnames,
          filename: filenames,
          autoWidth: autowidth,
          bookType: bookTypes
        })
      })
    },
    // 数据过滤，时间过滤
    formatJson (filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === "timestamp") {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
      /** 表格增加合计行 */
    tableAddTotalRow(columns, dataSource) {
      let numKey = 'rowIndex'
      let totalRow = { [numKey]: '合计' }
      columns.forEach(column => {
        let { key, dataIndex } = column
        if (![key, dataIndex].includes(numKey)) {

          let total = 0
          dataSource.forEach(data => {
            total += /^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN
            console.log(data[dataIndex], ':', (/^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN))
          })
          if (Number.isNaN(total)) {
            total = '-'
          }
          totalRow[dataIndex] = total
        }
      })

      dataSource.push(totalRow)
    }
  },
};
</script>

