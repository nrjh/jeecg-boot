<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    @cancel="handleCancel"
    :bodyStyle="bodyStyle"
    :confirmLoading="false"
    :footer="null"
    cancelText="关闭">
  <a-card :bordered="false">
    <a-row :gutter="12">
      <a-col :span="4">
        <a-menu  mode="vertical" @click="handleClick" :defaultSelectedKeys="defaultSelectedKeys" :selectedKeys="selectedKeys">
                <a-menu-item key="1">
                  报警信息
                </a-menu-item>
                <a-menu-item key="2">
                  工单信息
                </a-menu-item>
                <a-menu-item key="3">
                  派工信息
                </a-menu-item>
                <a-menu-item key="4">
                  挂单
                </a-menu-item>
                <a-menu-item key="5">
                  处理信息
                </a-menu-item>
                <a-menu-item key="6">
                  转派
                </a-menu-item>
                <a-menu-item key="7">
                  结单
                </a-menu-item>
        </a-menu>
      </a-col>
      <a-col :span="20">
        <div v-show="faultShow">
            <a-row :gutter="24" style="margin-left: -80px">
              <a-col :span="24">
                <a-form-item label="报警时间:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="" disabled="disabled" v-model="this.returnResult.alarmTime"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="检定线:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.lineNo"></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="设备名称:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" disabled="disabled"  v-model="this.returnResult.equipName"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="故障等级:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                  <a-radio-group  v-model="this.returnResult.faultGrade" disabled="disabled">
                    <a-radio value="高">
                      A级
                    </a-radio>
                    <a-radio value="中">
                      B级
                    </a-radio>
                    <a-radio value="低">
                      C级
                    </a-radio>
                  </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="故障类型:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                 <a-input placeholder="" v-model="this.returnResult.faultType" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="报警内容:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea disabled="disabled" v-model="this.returnResult.alarmDesc"></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="处理人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" v-model="this.returnResult.processUser" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div v-show="workOrderShow">
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="工单编号:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.id"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="检定线:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.lineNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" v-model="this.returnResult.equipName"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="故障等级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.faultGrade"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="工单名称:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.alarmReason"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="描述:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea disabled="disabled" v-model="this.returnResult.alarmDesc"></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="附件:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" v-model="this.returnResult.enclosurenName" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" >
            <a-col :span="12">
              <a-form-item label="上报时间:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 8%">
                <a-input placeholder="" v-model="this.returnResult.notifyTime" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="班组:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" v-model="this.returnResult.dispatchTeam" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" >
            <a-col :span="12">
              <a-form-item label="上报人:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 8%">
                <a-input placeholder="" v-model="this.returnResult.notifyPerson" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="联系方式:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" v-model="this.returnResult.notifyContactWay" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" >
            <a-col :span="12">
              <a-form-item label="登记人:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 8%">
                <a-input placeholder="" v-model="this.returnResult.registerPerson" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="联系方式:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" v-model="this.returnResult.notifyContactWay" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div v-show="sendOrderShow">
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="报警时间:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.alarmTime"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="检定线:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.lineNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.equipName"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="故障等级:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-radio-group  v-model="this.returnResult.faultGrade" disabled="disabled">
                  <a-radio value="高">
                    A级
                  </a-radio>
                  <a-radio value="中">
                    B级
                  </a-radio>
                  <a-radio value="低">
                    C级
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="故障类型:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" v-model="this.returnResult.faultType" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="报警内容:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea disabled="disabled" v-model="this.returnResult.alarmDesc"></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="处理人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" v-model="this.returnResult.processUser" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div v-show="waitOrderShow">

          <a-row :gutter="24">


            <a-col :span="2">
              <span>挂起</span>
            </a-col>

            <a-col :span="21">

              <a-form-item label="操作类型" :labelCol="{span:3}" :wrapperCol="{span:12}">
                <a-radio-group disabled="disabled" :value="1">
                  <a-radio :value="1">
                    解除
                  </a-radio>
                  <a-radio :value="2">
                    挂起
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <a-form-item :labelCol="{span:6}" :wrapperCol="{span:18}" style="margin-left: 5%">
                <a-textarea disabled="disabled" v-model="this.returnResult.listContent" :rows="3" placeholder="..."/>
              </a-form-item>


              <a-form-item label="结束时间:" :labelCol="{span:3}" :wrapperCol="{span:12}">
                <a-input placeholder="结束时间" disabled="disabled"  v-model="this.returnResult.listTime"  :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>

          <hr>

          <a-row :gutter="24">

            <a-col :span="2">
              <span>解除</span>
            </a-col>

            <a-col :span="21">

              <a-form-item label="操作类型" :labelCol="{span:3}" :wrapperCol="{span:12}">
                <a-radio-group :value="1" disabled="disabled">
                  <a-radio :value="1">
                    解除
                  </a-radio>
                  <a-radio :value="2">
                    挂起
                  </a-radio>
                </a-radio-group>
              </a-form-item>

              <a-form-item :labelCol="{span:6}" :wrapperCol="{span:18}" style="margin-left: 5%">
                <a-textarea disabled="disabled" v-model="this.returnResult.untieContent" :rows="3" placeholder="..."/>
              </a-form-item>

              <a-form-item label="结束时间:" :labelCol="{span:3}" :wrapperCol="{span:12}">
                <a-input placeholder="结束时间" disabled="disabled"  v-model="this.returnResult.untieTime" :trigger-change="true" style="width: 100%"/>
              </a-form-item>
            </a-col>

          </a-row>

        </div>
        <div v-show="dealInfoShow">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="处理结果：" :labelCol="{span:6}" :wrapperCol="{span:18}" >
                <a-radio-group  disabled="disabled">
                  <a-radio v-if="this.returnResult.solveResult != null">
                    已处理
                  </a-radio>
                  <a-radio v-else>
                    无需处理
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="修复方法" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.repairName"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="故障等级" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-radio-group  v-model="this.returnResult.faultGrade" disabled="disabled">
                  <a-radio value="高">
                    高
                  </a-radio>
                  <a-radio value="中">
                    中
                  </a-radio>
                  <a-radio value="低">
                    低
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="领料编号：" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.orderOutId"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="备注" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-textarea :rows="3"  disabled="disabled" placeholder="..." v-model="this.returnResult.resolveReason" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col>
              <a-form-item label="附件：" :labelCol="{span:3}" :wrapperCol="{span:15}">
                <a-input placeholder='' disabled="disabled" v-model="this.returnResult.enclosurenName"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="处理人：" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-input placeholder="" disabled="disabled"  v-model="this.returnResult.processUser"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间：" :labelCol="{span:6}" :wrapperCol="{span:18}">
                <a-input placeholder="" disabled="disabled"  v-model="this.returnResult.solveTime"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div v-show="transferOrderShow">
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="报警时间:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.alarmTime"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="检定线:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.lineNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.equipName"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="故障等级:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-radio-group  v-model="this.returnResult.faultGrade" disabled="disabled">
                  <a-radio value="高">
                    A级
                  </a-radio>
                  <a-radio value="中">
                    B级
                  </a-radio>
                  <a-radio value="低">
                    C级
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="故障类型:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" v-model="this.returnResult.faultType" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="报警内容:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea disabled="disabled" v-model="this.returnResult.alarmDesc"></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="原处理人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" v-model="this.returnResult.processUser" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="处理人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" v-model="this.returnResult.turnResolvePerson" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div v-show="doneOrderShow">
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="结单时间:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.updateTime"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="结单类型:" :labelCol="{span:6}" :wrapperCol="{span:12}" style="margin-left: 9%">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.orderType"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="工单时长:" :labelCol="{span:6}" :wrapperCol="{span:12}">
                <a-input placeholder="" disabled="disabled" v-model="this.returnResult.howTime"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="报警内容:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea disabled="disabled" v-model="this.returnResult.alarmDesc"></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" style="margin-left: -80px">
            <a-col :span="24">
              <a-form-item label="处理人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="" v-model="this.returnResult.processUser" disabled="disabled"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
      </a-col>

    </a-row>
  </a-card>
  </a-modal>
</template>
<script>

  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import { initDictOptionsOra,initDictOptions } from '@/components/dict/JDictSelectUtil'
  import FaultInfo from './FaultInfo.vue'
  import { getAction } from '../../../../../../api/manage'

  export default {
    name: "OrdWorkOrderDetailList",
    components: {
      JDictSelectTag,
      FaultInfo,
    },
    data () {
      return {
        description: '工单列表详情',
        visible:false,
        faultShow:false,
        workOrderShow:false,
        sendOrderShow:false,
        waitOrderShow:false,
        dealInfoShow:false,
        transferOrderShow:false,
        doneOrderShow:false,
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight-150)+"px"
        },
        title:"工单详情",
        defaultSelectedKeys:['1'],
        selectedKeys:[],
        returnResult:{},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        typeName:'',
        url: {
          list: "/workOrder/ordWorkOrderDetail/list",
          detail: "/workOrder/ordWorkOrderDetail/detail",
          delete: "/workOrderInfo/ordWorkOrderInfo/delete",
          deleteBatch: "/workOrderInfo/ordWorkOrderInfo/deleteBatch",
          exportXlsUrl: "/workOrderInfo/ordWorkOrderInfo/exportXls",
          importExcelUrl: "workOrderInfo/ordWorkOrderInfo/importExcel",
        },
        dictOptions:{},
        tableScroll:{x :47*147+50},
        calibrationLine:[],
        // ordWokOrderStatus:[],
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
          let lineNo="P_COMM_CODE,param_name,param_id,SORT_ID = 'EQUIP_CATEG' AND STATUS = '1'";
          initDictOptionsOra(lineNo).then(res => {
              this.calibrationLine = res.result;
          });
          // initDictOptions("ord_work_order_status").then(res => {
          //     this.ordWokOrderStatus = res.result
          // });
      },
      look(v){
        getAction(this.url.detail,{workOrderId:v}).then((res) => {
            if(res.success){
                this.returnResult = res.result;
                if(this.returnResult.faultGrade == 'low'){
                    this.returnResult.faultGrade = '低'
                }else if(this.returnResult.faultGrade == 'centre'){
                    this.returnResult.faultGrade = '中'
                }else{
                    this.returnResult.faultGrade = '高'
                }

                for(let i = 0;i<this.calibrationLine.length;i++){
                    if(this.calibrationLine[i].value == this.returnResult.lineNo){
                        this.returnResult.lineNo = this.calibrationLine[i].title;
                    }
                }
                console.log(this.returnResult)
            }
        })
        this.visible=true;
        this.faultShow=true;
        this.selectedKeys=[];
        this.selectedKeys.push('1');
        this.initDictConfig()
      },
      handleCancel(){
          this.visible=false;
          this.defaultSelectedKeys=['1'];
          this.selectedKeys=[];
          this.faultShow=false;
          this.workOrderShow=false;
          this.sendOrderShow=false;
          this.waitOrderShow=false;
          this.dealInfoShow=false;
          this.transferOrderShow=false;
          this.doneOrderShow=false;
      },
      handleClick(v){
          console.log("v----",v)
          this.selectedKeys=[];
          this.selectedKeys.push(v.key);
          if(v.key=='1'){
              this.faultShow=true;
              this.workOrderShow=false;
              this.sendOrderShow=false;
              this.waitOrderShow=false;
              this.dealInfoShow=false;
              this.transferOrderShow=false;
              this.doneOrderShow=false;
          }else if (v.key=='2'){
            this.faultShow=false;
            this.workOrderShow=true;
            this.sendOrderShow=false;
            this.waitOrderShow=false;
            this.dealInfoShow=false;
            this.transferOrderShow=false;
            this.doneOrderShow=false;
          }else if (v.key=='3'){
            this.faultShow=false;
            this.workOrderShow=false;
            this.sendOrderShow=true;
            this.waitOrderShow=false;
            this.dealInfoShow=false;
            this.transferOrderShow=false;
            this.doneOrderShow=false;
          }else if (v.key=='4'){
            this.faultShow=false;
            this.workOrderShow=false;
            this.sendOrderShow=false;
            this.waitOrderShow=true;
            this.dealInfoShow=false;
            this.transferOrderShow=false;
            this.doneOrderShow=false;
          }else if (v.key=='5'){
            this.faultShow=false;
            this.workOrderShow=false;
            this.sendOrderShow=false;
            this.waitOrderShow=false;
            this.dealInfoShow=true;
            this.transferOrderShow=false;
            this.doneOrderShow=false;
          }else if (v.key=='6'){
            this.faultShow=false;
            this.workOrderShow=false;
            this.sendOrderShow=false;
            this.waitOrderShow=false;
            this.dealInfoShow=false;
            this.transferOrderShow=true;
            this.doneOrderShow=false;
          }else if (v.key=='7'){
            this.faultShow=false;
            this.workOrderShow=false;
            this.sendOrderShow=false;
            this.waitOrderShow=false;
            this.dealInfoShow=false;
            this.transferOrderShow=false;
            this.doneOrderShow=true;
          }

      },
    }
  }
</script>