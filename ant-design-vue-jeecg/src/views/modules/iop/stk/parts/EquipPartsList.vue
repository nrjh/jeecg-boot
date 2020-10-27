<template>
  <a-card :bordered="false">
    <a-row :gutter="24">
      <a-col :span="4">
        <EquipPartsTree ref="treeForm" @select="onSelect"></EquipPartsTree>
      </a-col>
      <a-form :form="form" v-show="clickFlag">
      <a-col :span="20">
        <a-row :gutter="24">
          <a-tabs type="card">
            <a-tab-pane key="1" tab="部件信息">
            <a-col :span="16">
              <a-col :span="12">
                    <a-form-item label="设备类别" :labelCol="{span:5}" :wrapperCol="{span:15}">
                      <j-dict-select-tag-ora v-if="PartStatus == 2" type="list" v-model="equipPart.equipCategId"
                                             disabled
                                             dictCode="p_comm_code,param_name,param_id,SORT_ID = 'EQUIP_CATEG' and STATUS = '1'"
                                             placeholder="请选择设备类别"/>
                      <span v-else>{{equipPart.equipCategName}}</span>
                    </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="所属区域" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <j-dict-select-tag-ora v-if="PartStatus == 2" type="list" v-model="equipPart.areaId"
                                         disabled
                                         dictCode="o_area,area_name,area_id,new_flag = '1'"
                                         placeholder="请选择所属区域"/>
                  <span v-else>{{equipPart.areaName}}</span>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="设备名称" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input disabled v-if="PartStatus == 2" placeholder="请输入设备名称" v-model="equipPart.equipName"/>
                  <span v-else>{{equipPart.equipName}}</span>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="设备编码" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input disabled v-if="PartStatus == 2" placeholder="请输入设备编码" v-model="equipPart.equipId"/>
                  <span v-else>{{equipPart.equipId}}</span>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="部件名称" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input v-decorator="[ 'partsName', validatorRules.partsName]" v-if="PartStatus == 2" placeholder="请输入部件名称" />
                  <span v-else>{{equipPart.partsName}}</span>
                </a-form-item>
              </a-col>
              <a-col :span="12" style="float: left">
                <a-form-item label="部件类别" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <JDictSelectTag :trigger-change="true" v-decorator="[ 'partsSort', validatorRules.partsSort]" dictCode="Parts_Sort" v-if="PartStatus == 2" />
                  <span v-else>{{equipPart.partsSort_dictText}}</span>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="部件编号" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input disabled v-if="PartStatus == 2" placeholder="请输入部件编号" v-model="equipPart.partsNo"/>
                  <span v-else>{{equipPart.partsNo}}</span>
                </a-form-item>
              </a-col>
<!--              <a-col :span="12">
                <a-form-item label="部件类型" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <JDictSelectTag dictCode="Parts_Type"  v-if="PartStatus == 2" v-model="equipPart.partsType"/>
                  <span v-else>{{equipPart.partsType_dictText}}</span>
                </a-form-item>
              </a-col>-->
              </a-col>
              <a-col :span="8">

              </a-col>
            </a-tab-pane>
          </a-tabs>
        </a-row>
        <a-tabs type="card">
          <a-tab-pane key="2" tab="备品备件信息">
            <add-part-modal ref="addPartFrom" @select="onPush"></add-part-modal>
            <a-table v-if="PartStatus == 2"
              size="middle"
              bordered
              rowKey="id"
              :loading="loading"
              :columns="columns"
              :dataSource="equipPart.stkSparePartsList"
              :pagination="ipagination"
              :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
              @change="handleTableChange">

            </a-table>

            <a-table v-else
              size="middle"
              bordered
              rowKey="id"
              :loading="loading"
              :columns="columns"
              :dataSource="equipPart.stkSparePartsList"
              :pagination="ipagination"
              @change="handleTableChange">

            </a-table>
          </a-tab-pane>
        </a-tabs>
              <div style="margin-top: 10px;" v-if="PartStatus == 2">
                <a-button type="primary" @click="addPart()" :size="size" style="margin-right: 20px;">添加行</a-button>
                <a-button type="primary" @click="delStk()" style="margin-right: 20px;" :size="size">删除行</a-button>
                <a-button type="primary" @click="handleOkSave()" :size="size" style="margin-right: 20px;">保存</a-button>
                <a-button type="primary"  :size="size">取消</a-button>
              </div>
      </a-col>
      </a-form>
    </a-row>
  </a-card>
</template>

<script>
  import { httpAction,getAction, postAction } from '@/api/manage'
  import JDictSelectTagIop from "../../../../../components/dict/JDictSelectTagIop";
  import EquipPartsTree from './modules/EquipPartsTree'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import addPartModal from "./modules/addPartModal";
  import {initDictOptionsOra,initDictOptions,initDictOptionsIop} from "../../../../../components/dict/JDictSelectUtil";
  import pick from "lodash.pick";
  export default {
    name: 'AlarmBindEquip',
    mixins:[JeecgListMixin],
    components: {
      EquipPartsTree,
      JDictSelectTag,
      JDictSelectTagOra,
      JDictSelectTagIop,
      addPartModal
    },
    created() {

    },
    data() {
      let vm = this
      return {
        clickFlag:false,
        //1表示查看，2表示修改/新增
        PartStatus:"1",
        attributeCategoryList:[],
        form: this.$form.createForm(this),
        size: 'large',
        equipPart:{
          equipCategId:"",
          equipCategName:"",
          areaName:"",
          areaId:"",
          equipName:"",
          equipId:"",
          partsName:"",
          partsNo:"",
          partsSort:"",
          partsType:"",
          partsType_dictText:"",
          partsSort_dictText:"",
          partsId:"",
          stkSparePartsList:[]
        },
        validatorRules: {
          partsSort: {rules: [
              {required: true, message: '请选择部件类别'},
            ]},
          partsName: {rules: [
              {required: true, message: '请输入部件名称'},
            ]}
        },
        selectedRowKeys: [],
        selectedRows: [],
        supplierList:[],
        loading: false,
        //dataSource: [],
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '备品备件名称',
            align: 'center',
            dataIndex: 'productName'
          },
          {
            title: '备品备件编号',
            align: 'center',
            dataIndex: 'productNo'
          },
          {
            title: '备品备件规格',
            align: 'center',
            dataIndex: 'attributeCategoryId',
            customRender:function (text) {
              return vm.getAttributeCategory(text);
            }
          },
          {
            title: '供应商',
            align: 'center',
            dataIndex: 'supplier',
            customRender:function (text) {
              return vm.getSupplierValue(text);
            }
          }
        ],
        ipagination: {
          current: 1,
          pageSize: 8,
          pageSizeOptions: ['8', '10', '15'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        url: {
          get:"/parts/equipParts/queryById",
          list:"/",
          save:"/parts/equipParts/add",
          edit:"/parts/equipParts/edit",
        }
      }
    },
    computed: {

    },
    mounted() {

    },
    methods: {
      onPush(data){
        this.equipPart.stkSparePartsList = [];
        if (data != '' && data != null && data != undefined) {
          for (var i = 0; i < data.length; i++) {
            var d = {
              attributeCategoryId: data[i].attributeCategoryId,
              supplier: data[i].vendorId,
              id: data[i].productId,
              productNo: data[i].productNo,
              productName: data[i].rpoductName
            };
            this.equipPart.stkSparePartsList.push(d);
          }
        }
      },
      initDictConfig() {
        initDictOptions('vendorCode').then(res => {
          console.log("-----------------")
          console.log(res.result);
          this.supplierList = res.result;
        })
        initDictOptionsIop('prd_attribute_category,name,id').then(res => {
          console.log("-----------------")
          //console.log(res.result);
          this.attributeCategoryList = res.result;
        })
      },
      getSupplierValue(num){
        for(let i = 0;i<this.supplierList.length;i++){
          if(this.supplierList[i].value == num){
            return this.supplierList[i].title;
          }
        }
      },
      getAttributeCategory(num){
        for(let i = 0;i<this.attributeCategoryList.length;i++){
          if(this.attributeCategoryList[i].value == num){
            return this.attributeCategoryList[i].title;
          }
        }
      },
      delStk(){
      if (this.selectedRowKeys.length == 0) {
         this.$message.warning("请选择删除的数据！！！")
       }else if (this.selectedRowKeys.length > 0){
         let selectData = this.selectionRows;
         let data = this.equipPart.stkSparePartsList;
         for (var i = 0; i < data.length; i++){
           for (var j = 0; j < selectData.length; j++){
             if (data[i].id == selectData[j].id){
               data.splice(i,1);
             }
           }
         }
        this.selectedRowKeys = [];
      }
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      addPart(){
        console.log(this.equipPart.stkSparePartsList)
        this.$refs.addPartFrom.add('spare',this.equipPart.stkSparePartsList);
      },
      queryData(id,parentId){
        getAction(this.url.get, { id: id, parentId:parentId}).then(res => {
          this.equipPart = res.result
          this.model = Object.assign({}, res.result);
          if ((this.equipPart.partsNo == '' || this.equipPart.partsNo == null || this.equipPart.partsNo == undefined) && this.equipPart != null){
            //自动生成
            this.equipPart.partsNo = 'BJ' + this.getDate();
          }
          console.log(this.model)
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'partsName','partsSort'))
          });
        })
      },
      getDate(){
          let yy = new Date().getFullYear();
          let mm = new Date().getMonth()+1;
          let dd = new Date().getDate();
          let hh = new Date().getHours();
          let mf = new Date().getMinutes()<10 ? '0'+new Date().getMinutes() : new Date().getMinutes();
          let ss = new Date().getSeconds()<10 ? '0'+new Date().getSeconds() : new Date().getSeconds();
          return yy+""+mm+""+dd+""+hh+""+mf+""+ss;
      },
      onSelect(id, parentId,state) {
        this.clickFlag = true;
        this.PartStatus = state;
        this.queryData(id,parentId);
        this.initDictConfig();
      },
      handleTableChange(pagination) {
        console.log("pagination---",pagination)
        this.ipagination.current = pagination.current
      },
      handleOkSave() {
        let postData = this._.cloneDeep(this.equipPart)
        console.log(postData);
        this.form.validateFields((err, values) => {
          if (!err) {
            postData.partsSort = values.partsSort;
            postData.partsName = values.partsName;
            if (this.equipPart.partsId != "") {
              httpAction(this.url.edit, postData, 'put').then(res => {
                if (res.success) {
                  this.$message.success(res.message)
                } else {
                  this.$message.warning(res.message)
                }
              })
            } else {
              postAction(this.url.save, postData).then(res => {
                if (res.success) {
                  this.$message.success(res.message)
                } else {
                  this.$message.warning(res.message)
                }
              })
            }
            this.PartStatus = "1"
            initDictOptions('Parts_Sort').then(res => {
              for (var i = 0; i < res.result.length; i++) {
                if (res.result[i].value == postData.partsSort) {
                  postData.partsSort_dictText = res.result[i].title;
                }
              }
            })
            this.equipPart = postData
            this.$refs.treeForm.loads();
          }
        });
      },
    },
    watch: {

    }
  }
</script>

<style>

</style>