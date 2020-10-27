<template>
  <a-card :bordered="false">
    <a-row :gutter="12">
      <a-col :span="4">
        <area-equip-tree @select="onSelect"></area-equip-tree>
      </a-col>
      <a-col :span="20">
        <!--        <div class="equip-bind-title">故障设备绑定</div>-->
        <a-row>
          <a-col :span="6">检定线：{{areaName}}</a-col>
          <a-col :span="6">设备类型：{{equipTypeName}}</a-col>
          <a-col :span="6">检定仓：{{selectObj.name}}</a-col>
          <a-col :span="6">
            按状态：
            <a-radio-group v-model="status" style="float: right;margin-right: -30px;">
              <a-radio value="">
                全部
              </a-radio>
              <a-radio value="1">
                启用
              </a-radio>
              <a-radio value="2">
                禁用
              </a-radio>
            </a-radio-group>
          </a-col>
        </a-row>
        <br>
        <a-table
          ref="AlarmBindTable"
          size="middle"
          bordered
          rowKey="taskId"
          :loading="loading"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          @change="handleTableChange"
        >
          <template slot="faultGrade" slot-scope="text,record,index" >
            <a-select  style="width: 120px" @change="selectChange" @select="changeFault(record,index)"  v-model="record.faultGrade">
              <a-select-option value="A" >
                A级
              </a-select-option>
              <a-select-option value="B">
                B级
              </a-select-option>
              <a-select-option value="C">
                C级
              </a-select-option>
              <a-select-option value="D">
                隔离
              </a-select-option>
            </a-select>
<!--            <j-dict-select-tag type="select" v-model="record.faultGrade" dictCode="FAULT_GRADE"-->
<!--                               style="width: 90%" placeholder="请选择等级" :select="selectChange" />-->
          </template>
        </a-table>
        <br>
        <div>
          <a-button type="primary" @click="handleOkSave()">保存</a-button>
          <a-button style="margin-left: 8px" type="default">取消</a-button>
        </div>
      </a-col>

    </a-row>
  </a-card>
</template>

<script>
  import { getAction, postAction } from '@/api/manage'
  import { initDictOptionsOra, filterDictText } from '@/components/dict/JDictSelectUtil'

  import AreaEquipTree from './modules/AreaEquipTree'
  import JDictSelectTag from '@/components/dict/JDictSelectTag'

  export default {
    name: 'AlarmBindEquip',
    components: {
      AreaEquipTree,
      JDictSelectTag
    },
    created() {
      this.initDictConfig()
      this.initFaultGrade()
    },
    data() {
      let vm = this
      return {
        dictOptions: {
          areaDict: {},
          equipTypeDict: {},
          shiftDict: {}
        },
        faultGradeObj: {},
        loading: false,
        faultGrade: '',
        dataSource: [],
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '故障名称',
            align: 'center',
            dataIndex: 'faultName'
          },
          {
            title: '等级',
            align: 'center',
            dataIndex: 'faultGrade',
            width: '150px',
            scopedSlots: { customRender: 'faultGrade' }
          },
          {
            title: '运维班组',
            align: 'center',
            dataIndex: 'lineTeam',
            customRender: function(t, r, index) {
              let codeArr = vm._.split(t, ',')
              let nameArr = []
              console.log(codeArr, nameArr)
              for (let i = 0; i < codeArr.length; i++) {
                let str = filterDictText(vm.dictOptions.shiftDict, codeArr[i])
                nameArr.push(str)
              }
              return vm._.join(nameArr, ',')
            }
          },
          {
            title: '工单生成时长',
            align: 'center',
            dataIndex: 'orderCreTime',
            customRender: function(t, r, index) {
              console.log("r--",r)
              if (r.faultGrade) {
                return  vm.trancsoleGrade(r.faultGrade)
              } else {
                return ''
              }
            }
          },
          {
              title: '系统名称',
              align: 'center',
              dataIndex: 'systemName'
          },
          {
            title: '工单催办时长',
            align: 'center',
            dataIndex: 'orderCheTime',
            customRender: function(t, r, index) {
              if (r.faultGrade) {
                return vm.trancsoleGrade(r.faultGrade)
              } else {
                return ''
              }
            }
          },
          {
            title: '工单消缺',
            align: 'center',
            dataIndex: 'orderClearTime',
            customRender: function(t, r, index) {
              if (r.faultGrade) {
                return vm.trancsoleGrade(r.faultGrade)
              } else {
                return ''
              }
            }
          },
          {
            title: '故障类型',
            align: 'center',
            dataIndex: 'faultLabe',
            customRender: function(t, r, index) {
              if(t=='01'){
                return '机械故障'
              }else if (t=='02'){
                return '电控故障'
              }
            }
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'isStatus',
            customRender: function(t, r, index) {
              if (t == '1') {
                return '启用'
              } else {
                return '停用'
              }
            }
          }
        ],
        ipagination: {
          current: 1,
          pageSize: 15,
          pageSizeOptions: ['15', '30', '45'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        url: {
          list: '/alarm/bind/faultPrdProductInfo/list',
          save: '/alarm/bind/faultPrdProductInfo/save',
          equip: '/alarm/bind/faultPrdProductInfo/equip',
          faultGrade: '/alarm/bind/faultPrdProductInfo/faultGrade'
        },
        selectObj: {},
        status: ''
      }
    },
    computed: {
      areaName: function() {
        return filterDictText(this.dictOptions.areaDict, this.selectObj.areaId)
      },
      equipTypeName: function() {
        return filterDictText(this.dictOptions.equipTypeDict, this.selectObj.equipType)
      }
    },
    methods: {
      initDictConfig() {
        initDictOptionsOra('o_area,area_name,area_id').then(res => {
          this.dictOptions.areaDict = res.result
        })
        initDictOptionsOra('P_COMM_CODE,param_name,param_id,SORT_ID = \'EQUIP_TYPE\' AND STATUS = \'1\'').then(res => {
          this.dictOptions.equipTypeDict = res.result
        })
        initDictOptionsOra('O_SHIFT,SHIFT_NAME,SHIFT_ID').then(res => {
          this.dictOptions.shiftDict = res.result
        })

      },
      initFaultGrade() {
        getAction(this.url.faultGrade).then(res => {
          this.faultGradeObj = this._.keyBy(res.result, 'faultGrade')
        })
      },
      onSelect(id, obj) {
        this.faultGrade=''
        getAction(this.url.equip, { id: id }).then(res => {
          this.selectObj = res.result
          this.loadData()
        })
      },
      loadData() {
        this.loading = true
        let temp = {}
        temp.faultType = this.selectObj.equipType
        temp.productId = this.selectObj.equipId
        if (this.status && this.status != '') {
          temp.isStatus = this.status
        }
        getAction(this.url.list, temp).then((res) => {
          if (res.success) {
            this.dataSource = res.result
            this.ipagination.current = 1
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      handleTableChange(pagination) {
        console.log("pagination---",pagination)
        this.ipagination.current = pagination.current
      },
      handleOkSave() {
        let postData = this._.cloneDeep(this.selectObj)
        postData.faultPrdProductInfos = this.dataSource
        postAction(this.url.save, postData).then(res => {
          if (res.success) {
            this.$message.success(res.message)
            this.loadData()
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
        })
      },
      selectChange(value){
        this.faultGrade=value;
      },
      changeFault(record){
        record.faultGrade= this.faultGrade;
        record.orderClearTime= this.faultGrade;
        console.log("record------",record)
      },
      trancsoleGrade(v){
        if(v=='A' || v=='B'){
            return '立即生效'
        }else  if(v=='C'){
            return '24小时'
        }else {
          return '从不'
        }
      }
    },
    watch: {
      status(val) {
        this.loadData()
      }
    }
  }
</script>

<style scoped>
  .equip-bind-title {

  }
  .ant-alert-info {
    border: 1px solid #068b881f;
    background-color: #068b881f;
  }
</style>