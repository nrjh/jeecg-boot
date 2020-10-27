<template>
  <a-card :bordered="true">
    <a-row :gutter="24">
      <a-col :span="4">
        <AreaEquipTree ref="AreaEquipTree" @select="onSelect" @add="addEquip" @delete="deleteEquip" @edit="handEdit"></AreaEquipTree>
      </a-col>
      <a-form :form="form"  v-show="isShowForm">
      <a-col :span="20">
        <a-row :gutter="24">
            <a-col :span="16">
              <a-col :span="12">
                    <a-form-item label="设备类别" :labelCol="{span:5}" :wrapperCol="{span:15}">
                      <a-input  disabled="disabled"  v-model="equipCateName"/>
                    </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="设备类型" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <j-dict-select-tag-ora type="list"
                                         dictCode="P_COMM_CODE,param_name,param_id, SORT_ID = 'EQUIP_TYPE' AND STATUS = '1'"
                                         :trigger-change="true"
                                         v-decorator="[ 'equipType', validatorRules.equipType]"
                                         v-if="!isAdd"
                                         disabled="disabled"
                                         placeholder="请选择设备类型"/>
                  <j-dict-select-tag-ora type="list"
                                         dictCode="P_COMM_CODE,param_name,param_id, SORT_ID = 'EQUIP_TYPE' AND STATUS = '1'"
                                         :trigger-change="true"
                                         v-decorator="[ 'equipType', validatorRules.equipType]"
                                         v-else
                                         placeholder="请选择设备类型"/>

                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="所属区域" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input placeholder=""  disabled="disabled" v-model="areaText"/>
                  <a-input  hidden v-model="areaId"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="品牌" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <j-dict-select-tag-iop  v-if="!isAdd" disabled="disabled"  v-decorator="[ 'brandId', validatorRules.brandId]" placeholder="请选择品牌"  dictCode="PRD_BRAND,NAME,ID" :trigger-change="true"/>
                  <j-dict-select-tag-iop  type="list" v-else placeholder="请选择品牌"  v-decorator="[ 'brandId', validatorRules.brandId]"  dictCode="PRD_BRAND,NAME,ID" :trigger-change="true"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="设备名称" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input placeholder="请输入设备名称"  v-if="!isAdd" disabled="disabled" v-decorator="[ 'name', validatorRules.name]"  />
                  <a-input placeholder="请输入设备名称"  autocomplete ='off' v-else v-decorator="[ 'name', validatorRules.name]"  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="设备编码" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input placeholder="请输入设备编码"  v-decorator="[ 'equipNo', validatorRules.equipNo]" disabled="disabled" />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="硬件标识" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input placeholder="请输入硬件标识"  v-if="!isAdd" disabled="disabled"  v-decorator="[ 'hwEquipId', validatorRules.hwEquipId]" />
                  <a-input placeholder="请输入硬件标识"  v-else   v-decorator="[ 'hwEquipId', validatorRules.hwEquipId]" />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="购置日期" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <j-date placeholder="请选择购置日期"   v-if="!isAdd" disabled="disabled" :trigger-change="true" style="width: 100%" v-decorator="[ 'purchaseDate', validatorRules.purchaseDate]" />
                  <j-date placeholder="请选择购置日期"   v-else :trigger-change="true" style="width: 100%" v-decorator="[ 'purchaseDate', validatorRules.purchaseDate]" />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="设备别名" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input placeholder="请输入设备别名"  v-if="!isAdd" disabled="disabled"  v-decorator="[ 'equipLabe', validatorRules.equipLabe]" />
                  <a-input placeholder="请输入设备别名"  v-else   v-decorator="[ 'equipLabe', validatorRules.equipLabe]" />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="设备现场编号" :labelCol="{span:5}" :wrapperCol="{span:15}">
                  <a-input placeholder="请输入设备现场编号"  v-if="!isAdd" disabled="disabled"  v-decorator="[ 'equipFno', validatorRules.equipFno]" />
                  <a-input placeholder="请输入设备现场编号"  v-else   v-decorator="[ 'equipFno', validatorRules.equipFno]" />
                </a-form-item>
              </a-col>

              </a-col>
              <a-col :span="8" align="center">
                 <div style="width: 200px;height: 200px;">
                   <img :src="QRData" alt="二维码">
                 </div>
<!--                <a-button type="primary">打印二维码</a-button>-->
              </a-col>
            <a-col :span="24" style="padding-left: 0;">
              <a-form-item label="说明"  :labelCol="{span:2}" :wrapperCol="{span:12}">
                  <a-textarea  v-if="!isAdd" disabled="disabled"   rows="4" placeholder="请输入说明"  v-decorator="[ 'hwEquipDesc', validatorRules.hwEquipDesc]"/>
                  <a-textarea  v-else   rows="4" placeholder="请输入说明"  v-decorator="[ 'hwEquipDesc', validatorRules.hwEquipDesc]"/>
              </a-form-item>
            </a-col>
            <a-col :span="12" style="padding-right: 12px;" >
              <a-form-item label="上传图片" :labelCol="{span:4}" :wrapperCol="{span:18}">
                <j-image-upload   v-if="!isAdd"  disabled="disabled"  v-model="fileList" :isMultiple="true" > </j-image-upload>
                <j-image-upload   v-else  v-model="fileList"  :isMultiple="true"> </j-image-upload>
              </a-form-item>
            </a-col>
<!--            <a-col :span="8" style="width: 10%;" >-->
<!--              <a-form-item label="" :labelCol="{span:2}" :wrapperCol="{span:2}">-->
<!--                <j-image-upload   v-if="!isAdd" disabled="disabled" > </j-image-upload>-->
<!--                <j-image-upload   v-else  > </j-image-upload>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="8">-->
<!--              <a-form-item label="" :labelCol="{span:2}" :wrapperCol="{span:2}">-->
<!--                <j-image-upload   v-if="!isAdd" disabled="disabled" > </j-image-upload>-->
<!--                <j-image-upload   v-else src="temp/index1_1602404985844.jpg" > </j-image-upload>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
        </a-row>

        <a-tabs v-model="activeKey" style="border: 1px solid #e8e8e8;" v-show="!isAdd">
          <a-tab-pane tab="部件信息" :key="refKeys[2]" :forceRender="true">
            <a-table
              :ref="refKeys[0]"
              bordered
              :columns="partsInfoTable.columns"
              :dataSource="partsInfoTable.dataSource"
              :loading="partsInfoTable.loading"
              :pagination="partsInfoTable.ipagination"
              @change="getpartsInfoTableSource"
            >
            </a-table>
          </a-tab-pane>
          <a-tab-pane tab="备品备件" :key="refKeys[0]" :forceRender="true">
            <a-table
              :ref="refKeys[1]"
              bordered
              :columns="prodsInfoTable.columns"
              :dataSource="prodsInfoTable.dataSource"
              :loading="prodsInfoTable.loading"
              :pagination="prodsInfoTable.ipagination"
              @change="getprodsInfoTableSource"
              >
            </a-table>
          </a-tab-pane>
        </a-tabs>
              <div style="margin-top: 10px;margin-left: 25%;" v-show="isAdd">
                <a-button type="primary" @click="handleOkSave()">保存</a-button>
                <a-button style="margin-left: 8px" type="default" @click="cancel()">取消</a-button>
              </div>
      </a-col>
      </a-form>
    </a-row>
  </a-card>
</template>

<script>
  import { getAction, postAction ,deleteAction,httpAction } from '@/api/manage'
  import { initDictOptionsOra, filterDictText } from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop";
  import AreaEquipTree from './modules/AreaEquipTree'
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  import JDate from '@/components/jeecg/JDate'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import pick from 'lodash.pick'
  import moment from 'moment'
  export default {
    name: 'EquipsList',
    components: {
      AreaEquipTree,
      JDictSelectTag,
      JDictSelectTagOra,
      JDictSelectTagIop,
      JDate,
      JImageUpload
    },
    created() {
      this.initDictConfig()
    },
    data() {
      let vm = this
      return {
        form: this.$form.createForm(this),
        activeKey:'partsInfoTable',
        refKeys: ['partsInfoTable', 'prodsInfoTable'],
        loading: false,
        areaText:'',
        areaId:'',
        QRData:'',
        model :{},
        fileList:[],
        isShowForm:false,
        isAdd:false,
        confirmLoading:false,
        dictOptions: {
          equipCateDict: {},
        },
        equipCate:'',
        validatorRules: {
          equipType: {rules: [
              {required: true, message: '请选择设备类别'},
            ]},
          name: {rules: [
              {required: true, message: '请输入设备名称'},
            ]},
          hwEquipId: {rules: [
            ]},
          equipNo: {rules: [
            ]},
          purchaseDate: {rules: [
            ]},
          hwEquipDesc: {rules: [
            ]},
          brandId: {rules: [
            ]},
          equipLabe: {rules: [
            ]},
          equipFno: {rules: [
            ]},
        },
        // 部件信息
        partsInfoTable: {
          loading: false,
          deleteData:true,
          dataSource: [],
          totalDataSource:[],
          ipagination: {
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            sizeList: ['5', '10', '15'], //一页能显示条数
            pageSize: 5, //当前页显示多少条
            current: 1, //当前页
            total: 0,
            showSizeChanger: true
          },
          columns: [
            {
              title: '名称',
              dataIndex: 'partsName',
              align: "center",
            },
            {
              title: '编码',
              dataIndex: 'partsNo',
              align: "center",
            },
            {
              title: '相关属性',
              dataIndex: '',
              align: "center",
            },
            // {
            //   title: '操作',
            //   dataIndex: 'action',
            //   align:"center",
            //   scopedSlots: { customRender: 'action' }
            // },
          ]
        },
        // 备品备件
        prodsInfoTable: {
          loading: false,
          deleteData:true,
          dataSource: [],
          totalDataSource:[],
          ipagination: {
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            sizeList: ['5', '10', '15'], //一页能显示条数
            pageSize: 5, //当前页显示多少条
            current: 1, //当前页
            total: 0,
            showSizeChanger: true
          },
          columns: [
            {
              title: '备品备件编号',
              dataIndex: 'productNo',
              align: "center",
            },
            {
              title: '备品备件名称',
              dataIndex: 'productName',
              align: "center",
            },
            {
              title: '配件条形码',
              dataIndex: '',
              align: "center",
            },
            {
              title: '型号',
              dataIndex: '',
              align: "center",
            },
          ]
        },
        url: {
          list: '/alarm/bind/faultPrdProductInfo/list',
          add: '/OEquip/oEquip/add',
          edit: '/OEquip/oEquip/edit',
          delete: "/OEquip/oEquip/delete",
          equip: '/OEquip/oEquip/queryById',
          img: "/DevMonitorInfo/devMonitorInfo/qrimage",
        },
        selectObj: {},
        status: ''
      }

    },
    computed: {
      equipCateName: function() {
        return filterDictText(this.dictOptions.equipCateDict, this.equipCate)
      }
    },
    methods: {
      initDictConfig() {
        initDictOptionsOra('P_COMM_CODE,param_name,param_id,SORT_ID = \'EQUIP_CATEG\' AND STATUS = \'1\'').then(res => {
          this.dictOptions.equipCateDict = res.result
        })

      },
      onSelect(id, obj) {
        this.isShowForm=true;
        this.isAdd=false;
        getAction(this.url.equip, { id: id }).then(res => {
          console.log("res----",res)
          this.selectObj = res.result;
          console.log("selectObj----",this.selectObj)
          this.model = Object.assign({}, res.result);
          this.areaId=this.model.areaId;
          this.areaText=this.model.areaId_dictText;
          this.visible = true;
          this.fileList=this.model.equipImgs;
          this.partsInfoTable.dataSource=this.model.equipParts;
          this.prodsInfoTable.dataSource=this.model.stkSpareParts;
          this.QRData='data:image/png;base64,'+this.model.qrstr
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.model,'equipType','name','hwEquipDesc','purchaseDate','equipNo','hwEquipId','brandId','equipLabe','equipFno'))
          });
          let str='o_area,area_name,equip_categ,area_id =\''+this.model.areaId+'\''
          initDictOptionsOra(str).then(res => {
             this.equipCate= res.result[0].value
          })
        })
      },

      handleTableChange(pagination) {
        console.log("pagination---",pagination)
        this.ipagination.current = pagination.current
      },
      handleOkSave() {
        const that = this
        console.log(that.fileList)
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            console.log("this.model----",this.model)
            if (!this.model.equipId) {
              httpurl += this.url.add
              method = 'post'
              this.model.areaId=this.areaId;
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            formData.equipImgs=that.fileList
            console.log('表单提交数据', formData)
            console.log('表单提交数据method', method)
            console.log('httpurl', httpurl)
            let id='';
            httpAction(httpurl, formData, method).then((res) => {
              console.log('res.result.equipId---',res.result)
              id=res.result.equipId;
              if (res.success) {
                this.onSelect(id,null)
                that.$message.success(res.message)
              } else {
                that.$message.warning(res.message)
              }
              that.confirmLoading = false
            })
            //   .finally(() => {
            //   that.confirmLoading = false
            //   this.onSelect(id, obj)
            //   // that.cancel()
            // })
          }
        })
      },
      selectChange(value){
      },
      addEquip(id, obj){
          this.form.resetFields()
          this.isAdd=true;
          this.isShowForm=true;
          this.model={};
          var equipNo = "EQ" + moment().format("YYYYMMDDHHmmss");
          this.form.setFieldsValue({ equipNo: equipNo })
          this.areaId=obj.id;
          this.areaText=obj.title;
          this.equipCate=obj.pid;
          getAction(this.url.img,{equipNo:equipNo}).then(res=>{
            this.QRData='data:image/png;base64,'+res.message;
          })
          console.log('id-----',obj);
      },
      cancel(){
        this.form.resetFields()
        this.isAdd=false;
        this.isShowForm=false;
        this.$refs.AreaEquipTree.onloadData();
      },
      deleteEquip(id, obj){
        deleteAction(this.url.delete,{id:id}).then((res)=>{
          if(res.success){
           this.$refs.AreaEquipTree.onloadData();
           this.cancel();
          }else{
            console.log(res.message);
          }
        })
      },
      handEdit(id, obj){
        this.onSelect(id, obj);
        this.isAdd=true;
      },
      //实现分页效果
      getpartsInfoTableSource(pagination) {
        pagination= this._.cloneDeep(pagination);
        this.partsInfoTable.ipagination.current=pagination.current;

      },
      //实现分页效果
      getprodsInfoTableSource(pagination) {
        pagination= this._.cloneDeep(pagination);
        this.prodsInfoTable.ipagination.current=pagination.current;

      },
    },
    watch: {
      status(val) {
        // this.loadData()
      }
    }
  }
</script>

<style>

</style>