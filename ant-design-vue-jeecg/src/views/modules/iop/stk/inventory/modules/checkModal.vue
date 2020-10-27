<template>
  <div>
    <a-modal
      title=""
      :width='1000'
      :visible="visibleModelCheck"
      @ok="handleOkCheck"
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
      <div class="table-operator" v-if="!checkMess">
        <a-button type="primary" icon="download" @click="handleExportXls('盘点清单')">导出</a-button>
        <a-button v-print="'#printContent'" ghost  type="primary">打印</a-button>
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
        :columns="columnsCheck"
        :dataSource="dataSourceCheck"
        :pagination="false"
        >

        <template v-if="checkMess" slot="productQty" slot-scope="text, record">
          <editable-cell :text="Number" @change="onCellChange(record.key, 'productQty', $event)" />
        </template>

      </a-table>


    </a-modal>
  </div>
</template>
<script>
  import { httpAction , getAction ,downFile} from '@/api/manage'

  import { FormTypes } from '@/utils/JEditableTableUtil'

  const EditableCell = {
    template: `
      <div class="editable-cell">
        <div v-if="editable" class="editable-cell-input-wrapper">
          <a-input :value="value" @change="handleChange" @pressEnter="check" /><a-icon
            type="check"
            class="editable-cell-icon-check"
            @click="check"
          />
        </div>
        <div v-else class="editable-cell-text-wrapper">
          {{ value || ' ' }}
          <a-icon type="edit" class="editable-cell-icon" @click="edit" />
        </div>
      </div>
    `,
    props: {
      type: Number,
    },
    data() {
      return {
        value: this.text,
        editable: false,
      };
    },
    methods: {
      handleChange(e) {
        console.log('----------处理值-------')
        const value = e.target.value;
        this.value = value;
      },
      check() {
        this.editable = false;
        console.log('----------处理值完成-------')
        this.$emit('change', this.value);
      },
      edit() {
        this.editable = true;
      },
    },
  };
export default {
  components: {
    name: "checkModal",
    EditableCell,
  },
  data() {
    return {
      dataSourceCheck: [],
      dataSourceTotal: [],
      taskNoCheck: '',
      nameCheck: '',
      execByCheck: '',
      visibleModelCheck: false,
      checkMess: false,
      inventoryId: '',
      printShow: false,
      labelCol: {
        span: 6
      },
      wrapperCol: {
        span: 16
      },
      columnsCheck: [
        {
          title: '单位',
          dataIndex:'taskNo',
          align:"center",
          type: 'hidden'
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
          scopedSlots: { customRender: 'productQty' },
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
          title: '#',
          align: 'center',
          dataIndex: 'rowIndex',
          customRender: function (text, r, index) {
            return (text !== '合计') ? (parseInt(index) + 1) : text
          }
        },
        {
          title:'单位',
          align:"center",
          dataIndex: 'status'
        },
        {
          title:'库房',
          align:"center",
          dataIndex: 'houseName'
        },
        {
          title:'材料类型',
          align:"center",
          dataIndex: ''
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
        getTotalByInventoryId: "StkInventory/stkInventory/getTotalByInventoryId",
      },
    };
  },
  methods: {
    handleOkCheck(){
      console.log('---this.dataSourceCheck--'+this.dataSourceCheck);
      let formData = Object.assign(this.dataSourceCheck);
      console.log("表单提交数据",formData)
      httpAction(this.url.inventoryResult,formData,'put').then((res)=>{
        if(res.success){
          this.companyName='';
          this.visibleModelCheck=false;
          this.dataSourceCheck = [];
          this.inventoryId = '';
          this.printShow = false;
          this.dataSourceTotal = [];
        }else{
          this.companyName='';
          this.visibleModelCheck=false;
          this.dataSourceCheck = [];
          this.inventoryId = '';
          this.printShow = false;
          this.dataSourceTotal = [];
        }
      }).finally(() => {
      })
    },
    handleCancelCheck(){
      this.visibleModelCheck=false;
      this.dataSourceCheck = [];
      this.dataSourceTotal = [];
      this.printShow = false;
    },
   showCheck(inventoryId,v){
     this.visibleModelCheck=true;
     this.inventoryId=inventoryId;
     console.log('---this.v--',v);
     this.printShow=true;
     if(v==2){
         this.checkMess=true;
     }
     getAction(this.url.getInventoryProd,{inventoryId:inventoryId,mark:v}).then((res)=>{
       if(res.success){
         this.dataSourceCheck=res.result;
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
         this.tableAddTotalRow(this.columnsTotal, res.result);
       }else{
         console.log(res.message);
       }
     });
    },
    onCellChange(key, dataIndex, value) {
      const dataSourceCheck = [...this.dataSourceCheck];
      const target = dataSourceCheck.find(item => item.key === key);
      if (target) {
        target[dataIndex] = value;
        this.dataSourceCheck = dataSourceCheck;
      }
    },

    handleExportXls(){
      let formData = Object.assign(this.dataSourceCheck);
      console.log("导出参数",formData)
//      let paramsStr = JSON.stringify(filterObj(param));
       downFile(this.url.exportXls,{inventoryId:this.inventoryId}).then((data)=>{
        if (!data) {
          this.$message.warning("文件下载失败")
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data]), this.description+'.xls')
        }else{
          let url = window.URL.createObjectURL(new Blob([data]))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', this.description+'.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        }
      })
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
    },
  },
};
</script>
<style>
.editable-cell {
  position: relative;
}

.editable-cell-input-wrapper,
.editable-cell-text-wrapper {
  padding-right: 24px;
}

.editable-cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}

.editable-cell-icon,
.editable-cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}

.editable-cell-icon {
  line-height: 18px;
  display: none;
}

.editable-cell-icon-check {
  line-height: 28px;
}

.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}

.editable-cell-icon:hover,
.editable-cell-icon-check:hover {
  color: #108ee9;
}

.editable-add-btn {
  margin-bottom: 8px;
}
</style>
