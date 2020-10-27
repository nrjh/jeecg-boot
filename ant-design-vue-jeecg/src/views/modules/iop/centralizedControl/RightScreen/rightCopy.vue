<template>
  <div class="body">
    <a-row>
      <a-col :span="12" align="center">
        <span id="header-title">福建智能运维管理平台</span>
      </a-col>
      <a-col :span="12" align="center">
        <span id="header-time">{{nowTime}}</span>
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="4">
        <div class="inspection-header">
          在检信息
        </div>
        {{msg1}}
        <div class="inspection-body" v-for="(item,i) in checkList" >
          <div class="inspection-body-icon">
            <img src="http://47.98.176.54/group1/M00/00/00/rBADMV9iw0SASxoZAAASONcMeNw108.png" width="70"/>
          </div>
          <div class="inspection-body-content">
            <p class="inspection-body-content-text">任务单号：{{item.taskId}}</p>
            <p class="inspection-body-content-text">批次：{{item.batchId}}</p>
            <p class="inspection-body-content-text">型号：{{item.deviceModel}}</p>
            <p class="inspection-body-content-text">供应商：{{item.supplier}}</p>
            <p class="inspection-body-content-text">任务类型：{{item.taskType}}</p>
            <p class="inspection-body-content-text">创建时间：{{item.createTime|moment("YYYY/MM/DD HH:mm:ss")}}</p>
            <p class="inspection-body-content-text">任务总量：{{item.totalTask}}</p>
            <p class="inspection-body-content-text">当天完成量：{{item.amountCompleted}}</p>
            <p class="inspection-body-content-text">累计完成量：{{item.cumulativeCompletion}}</p>
          </div>
        </div>
      </a-col>
      <a-col :span="16">
        <a-col :span="24">
          <div class="global">
            <a-col :span="1">

            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(-1)">
              <div :class="monitorClass.all"></div>
              <div class="monitor-body">全部</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(0)">
              <div :class="monitorClass.D1"></div>
              <div class="monitor-body">单相Ⅰ</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(1)">
              <div :class="monitorClass.D2"></div>
              <div class="monitor-body">单相Ⅱ</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(2)">
              <div :class="monitorClass.S1"></div>
              <div class="monitor-body">三相Ⅰ</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(3)">
              <div :class="monitorClass.S2"></div>
              <div class="monitor-body">三相Ⅱ</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(4)">
              <div :class="monitorClass.C1"></div>
              <div class="monitor-body">采集Ⅰ</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(5)">
              <div :class="monitorClass.C2"></div>
              <div class="monitor-body">采集Ⅱ</div>
            </a-col>
            <a-col :span="2" align="center" class="monitor" @click="clickData(6)">
              <div :class="monitorClass.H"></div>
              <div class="monitor-body">互感器</div>
            </a-col>
            <a-col :span="7" align="center">
              <div :class="statusClass"></div>
              <div class="monitor-body">{{status}}</div>
            </a-col>
          </div>
        </a-col>
        <a-col :span="24">
          <div class="task-info">
            <a-col :span="3">
              <div class="task-info-body">
                本月待检
              </div>
            </a-col>
            <a-col :span="2">
              <div class="task-info-body-other">
                本月待检总数<br/>
                <span class="task-info-content-body">{{taskInfo.pendMonthTotal}}</span>
              </div>
            </a-col>
            <a-col :span="2">
              <div class="task-info-body-other">
                <div class="task-info-body-split">/</div>
              </div>
            </a-col>
            <a-col :span="2">
              <div class="task-info-body-other">
                完成检定数<br/>
                {{taskInfo.completeVerificeMonthCount}}
              </div>
            </a-col>
            <a-col :span="3">
              <div class="task-info-body-other">
                <br/>
                本月完成率：<span class="task-info-body-complate">{{taskInfo.completeMonth}}%</span>
              </div>
            </a-col>
            <a-col :span="3">
              <div class="task-info-body">
                今日待检总数
              </div>
            </a-col>
            <a-col :span="2">
              <div class="task-info-body-other">
                今日待检总数<br/>
                <span class="task-info-content-body">{{taskInfo.pendDayTotal}}</span>
              </div>
            </a-col>
            <a-col :span="2">
              <div class="task-info-body-other">
                <span class="task-info-body-split">/</span>
              </div>
            </a-col>
            <a-col :span="2">
              <div class="task-info-body-other">
                完成检定数<br/>
                {{taskInfo.completeVerificeDayCount}}
              </div>
            </a-col>
            <a-col :span="3">
              <div class="task-info-body-other">
                <br/>
                今日完成率：<strong class="task-info-body-complate">{{taskInfo.completeDay}}%</strong>
              </div>
            </a-col>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="task-info-count">
            <span class="task-info-count-title">单相</span>
            <div class="task-info-count-content">
              <div class="task-info-count-content-body">
                计划数<br/>
                <span class="task-info-count-content-count">{{taskInfo.planD}}</span>
              </div>
              <div class="task-info-count-content-body-split">
                /
              </div>
              <div class="task-info-count-content-body">
                完成数<br/>
                {{taskInfo.completeD}}
              </div>
            </div>
            <a-progress class="task-info-count-progress" :percent="taskInfo.completeRateD" status="active" />
          </div>
        </a-col>
        <a-col :span="6">
          <div class="task-info-count">
            <span class="task-info-count-title">三相</span>
            <div class="task-info-count-content">
              <div class="task-info-count-content-body">
                计划数<br/>
                <span class="task-info-count-content-count">{{taskInfo.planS}}</span>
              </div>
              <div class="task-info-count-content-body-split">
                /
              </div>
              <div class="task-info-count-content-body">
                完成数<br/>
                {{taskInfo.completeS}}
              </div>
            </div>
            <a-progress class="task-info-count-progress" :percent="taskInfo.completeRateS" status="active" />
          </div>
        </a-col>
        <a-col :span="6">
          <div class="task-info-count">
            <span class="task-info-count-title">采集</span>
            <div class="task-info-count-content">
              <div class="task-info-count-content-body">
                计划数<br/>
                <span class="task-info-count-content-count">{{taskInfo.planC}}</span>
              </div>
              <div class="task-info-count-content-body-split">
                /
              </div>
              <div class="task-info-count-content-body">
                完成数<br/>
                {{taskInfo.completeC}}
              </div>
            </div>
            <a-progress class="task-info-count-progress" :percent="taskInfo.completeRateC" status="active" />
          </div>
        </a-col>
        <a-col :span="6">
          <div class="task-info-count">
            <span class="task-info-count-title">互感器</span>
            <div class="task-info-count-content">
              <div class="task-info-count-content-body">
                计划数<br/>
                <span class="task-info-count-content-count">{{taskInfo.planH}}</span>
              </div>
              <div class="task-info-count-content-body-split">
                /
              </div>
              <div class="task-info-count-content-body">
                完成数<br/>
                {{taskInfo.completeH}}
              </div>
            </div>
            <a-progress class="task-info-count-progress" :percent="taskInfo.completeRateH" status="active" />
          </div>
        </a-col>
        <a-col :span="24">
          <div class="detection-trend">
            <div class="detection-trend-header">
              当日检测走势图
            </div>
            <div class="detection-trend-body">
              <Bar ref="barForm"></Bar>
            </div>
          </div>
        </a-col>
        <a-col :span="24">
          <div class="fault">
            <div class="fault-header">
              故障统计
            </div>
            <div class="fault-body">
              <div class="fault-body-content-1">
                <a-col :span="8">
                  <div class="fault-body-content-1-body">
                    <a-col :span="24" class="fault-body-content-1-body-title">流水线故障占比</a-col>
                    <a-col :span="24" class="fault-body-content-1-body-pie">
                      <LineFaultPie ref="lineFaultPieForm" title="饼图" :height="height"/>
                    </a-col>
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="fault-body-content-1-body">
                    <a-col :span="24" class="fault-body-content-1-body-title">故障区域占比</a-col>
                    <a-col :span="24" class="fault-body-content-1-body-pie">
                      <FaultAreaPie ref="faultAreaPieForm" title="饼图" :height="height"/>
                    </a-col>
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="fault-body-content-1-body">
                    <a-col :span="24" class="fault-body-content-1-body-title">故障类型</a-col>
                    <a-col :span="24" class="fault-body-content-1-body-pie">
                      <FaultTypePie ref="faultTypePieForm" title="饼图" :height="height"/>
                    </a-col>
                  </div>
                </a-col>
              </div>
              <div class="fault-body-content-2">
                <div class="fault-body-content-2-alarm">
                  <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="alarmColumns"
                    :pagination="false"
                    :dataSource="alarmDataSource"
                    :loading="loading"
                    :scroll="tableScroll">
                  </a-table>
                </div>
                <div class="fault-body-content-2-equip">
                  <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="equipAlarmColumns"
                    :pagination="false"
                    :dataSource="equipAlarmDataSource"
                    :loading="loading"
                    :scroll="tableScroll">
                  </a-table>
                </div>
              </div>
            </div>
          </div>
        </a-col>
      </a-col>
      <a-col :span="4">
        <div class="task">
          <div class="task-header">
            配送任务信息
            <a href="#" class="task-header-link">更多</a>
          </div>
          <div class="task-body">
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="id"
              :columns="taskColumns"
              :pagination="false"
              :dataSource="taskDataSource"
              :loading="loading"
              :scroll="tableScroll">
            </a-table>
          </div>
        </div>
        <div class="msg">
          <div class="msg-header">
            通知公告
            <a href="#" class="msg-header-link">更多</a>
          </div>
          <div class="msg-body">
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="id"
              :columns="msgColumns"
              :pagination="false"
              :dataSource="msgDataSource"
              :loading="loading"
              :scroll="tableScroll">
            </a-table>
          </div>
        </div>
      </a-col>
    </a-row>
    <a-row>
      <a-col :span="24">
        <div class="footer">
          111
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  import moment from 'moment'
  import {httpAction, getAction} from '@/api/manage'
  import FaultTypePie from "./modules/FaultTypePie";
  import Bar from './modules/Bar'
  import LineFaultPie from "./modules/LineFaultPie";
  import FaultAreaPie from "./modules/FaultAreaPie";
  export default {
    name: "RightScreenList",
    components: {
      FaultTypePie,
      Bar,
      LineFaultPie,
      FaultAreaPie
    },
    created() {

    },
    mounted() {
      this.nowTimes();
      this.getCheckList();
      this.getTaskList();
      this.getAnnouncementList();
      this.getAlarmList();
      this.getEquipAlarmList();
      this.getDetectCountList();
      this.getSwitchMode();
    },
    data() {
      return {
        msg1:"暂无数据",
        height:400,
        //选择id
        checkId:[],
        //检定任务统计
        taskInfo:{},
        //现在时间
        nowTime: "",
        statusClass:"",
        status:"",
        //样式
        monitorClass:{
          all:"monitor-title",
          D1:"monitor-title-2",
          D2:"monitor-title-2",
          S1:"monitor-title-2",
          S2:"monitor-title-2",
          C1:"monitor-title-2",
          C2:"monitor-title-2",
          H:"monitor-title-2"
        },
        //用于判断是否被选中
        checked:{
          //单相I
          flagA:true,
          flagD1:false,
          flagD2:false,
          flagS1:false,
          flagS2:false,
          flagC1:false,
          flagC2:false,
          flagH:false
        },
        tableScroll:{y:300},
        taskDataSource:[],
        alarmDataSource:[],
        equipAlarmDataSource:[],
        loading:false,
        //通知公告列表
        msgDataSource:[],
        taskColumns:[
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
            title:'车牌号',
            align:"center",
            dataIndex: 'plateNum'
          },
          {
            title:'接收单位',
            align:"center",
            dataIndex: 'acceptUnit'
          },
          {
            title:'设备数量',
            align:"center",
            dataIndex: 'equipQty'
          },
          {
            title:'出发时间',
            align:"center",
            dataIndex: 'departureTime'
          },
          {
            title:'完成时间',
            align:"center",
            dataIndex: 'completeTime'
          },
        ],
        //通知公告字段
        msgColumns:[
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
            title:'公告名称',
            align:"center",
            dataIndex: 'announcementName'
          },
          {
            title:'发布时间',
            align:"center",
            dataIndex: 'releaseTime'
          },
          {
            title:'管控人',
            align:"center",
            dataIndex: 'controller'
          },
        ],
        alarmColumns:[
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
            title:'报警时间',
            align:"center",
            dataIndex: 'alarmTime'
          },
          {
            title:'报警名称',
            align:"center",
            dataIndex: 'alarmName'
          },
          {
            title:'检定线',
            align:"center",
            dataIndex: 'lineNo'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName'
          },
          {
            title:'故障等级',
            align:"center",
            dataIndex: 'failureLevel'
          },
          {
            title:'故障状态',
            align:"center",
            dataIndex: 'faultStatus',
            customRender: function(t, r, index){
              if(t=='1'){
                return '启用'
              }else if (t=='2'){
                return '禁用'
              }
            }
          },
          {
            title: '处理时长',
            align: "center",
            dataIndex: 'handleTime',
            customRender: function(text, r, index) {
              if (text == null || text == ''){
                return text;
              }
              var theTime = parseInt(text);// 秒
              var middle = 0;// 分
              var hour = 0;// 小时
              if (theTime > 60) {
                middle = parseInt(theTime / 60);
                theTime = parseInt(theTime % 60);
                if (middle > 60) {
                  hour = parseInt(middle / 60);
                  middle = parseInt(middle % 60);
                }
              }
              var result = "" + parseInt(theTime) + "秒";
              if (middle > 0) {
                result = "" + parseInt(middle) + "分" + result;
              }
              if (hour > 0) {
                result = "" + parseInt(hour) + "小时" + result;
              }
              return result;
            }
          }
        ],
        equipAlarmColumns:[
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
            title:'检定线',
            align:"center",
            dataIndex: 'lineNo'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipName'
          },
          {
            title:'故障频率',
            align:"center",
            dataIndex: 'frequency'
          },
          {
            title:'处理总时长',
            align:"center",
            dataIndex: 'handleTimeTotal',
            customRender: function(text, r, index) {
              if (text == null || text == ''){
                return text;
              }
              var theTime = parseInt(text);// 秒
              var middle = 0;// 分
              var hour = 0;// 小时
              if (theTime > 60) {
                middle = parseInt(theTime / 60);
                theTime = parseInt(theTime % 60);
                if (middle > 60) {
                  hour = parseInt(middle / 60);
                  middle = parseInt(middle % 60);
                }
              }
              var result = "" + parseInt(theTime) + "秒";
              if (middle > 0) {
                result = "" + parseInt(middle) + "分" + result;
              }
              if (hour > 0) {
                result = "" + parseInt(hour) + "小时" + result;
              }
              return result;
            }
          },
        ],
        checkList: [],
        url: {
          checkList: "/centralized/rightScreen/checkList",
          taskList: '/centralized/rightScreen/taskList',
          announcementList:"/centralized/rightScreen/announcementList",
          alarmList:"/centralized/rightScreen/alarmList",
          equipAlarmList:"/centralized/rightScreen/equipAlarmList",
          detectCountList:"/centralized/rightScreen/detectCountList",
          switchMode:"/centralized/rightScreen/switchMode"
        }
      }
    },
    methods: {
      clickData(id){

        if (id == -1){
          this.check.flag = !this.check.flag;
          if (this.check.flag){
            this.checkId.push(-1)
            this.monitorClass.all = "monitor-title";
          }else{
            this.removeId(-1);
            this.monitorClass.all = "monitor-title-2";
          }
        }
        //单相1
        if (id == 0){
          this.check.flagD1 = !this.check.flagD1;
          if (this.check.flagD1){
            this.checkId.push(0)
            this.monitorClass.D1 = "monitor-title";
          }else{
            this.removeId(0);
            this.monitorClass.D1 = "monitor-title-2";
          }
        }
        if (id == 1){
          this.check.flagD2 = !this.check.flagD2;
          if (this.check.flagD2){
            this.checkId.push(1)
            this.monitorClass.D2 = "monitor-title";
          }else{
            this.removeId(1);
            this.monitorClass.D2 = "monitor-title-2";
          }
        }
        if (id == 2){
          this.check.flagS1 = !this.check.flagS1;
          if (this.check.flagS1){
            this.checkId.push(2)
            this.monitorClass.S1 = "monitor-title";
          }else{
            this.removeId(2);
            this.monitorClass.S1 = "monitor-title-2";
          }
        }
        if (id == 3){
          this.check.flagS2 = !this.check.flagS2;
          if (this.check.flagS2){
            this.checkId.push(3)
            this.monitorClass.S2 = "monitor-title";
          }else{
            this.removeId(3);
            this.monitorClass.S2 = "monitor-title-2";
          }
        }
        if (id == 4){
          this.check.flagC1 = !this.check.flagC1;
          if (this.check.flagC1){
            this.checkId.push(4)
            this.monitorClass.C1 = "monitor-title";
          }else{
            this.removeId(4);
            this.monitorClass.C1 = "monitor-title-2";
          }
        }
        if (id == 5){
          this.check.flagC2 = !this.check.flagC2;
          if (this.check.flagC2){
            this.checkId.push(5)
            this.monitorClass.C2 = "monitor-title";
          }else{
            this.removeId(5);
            this.monitorClass.C2 = "monitor-title-2";
          }
        }
        if (id == 6){
          this.check.flagH = !this.check.flagH;
          if (this.check.flagH){
            this.checkId.push(6)
            this.monitorClass.H = "monitor-title";
          }else{
            this.removeId(6);
            this.monitorClass.H = "monitor-title-2";
          }
        }
        if (this.check.flag){
          this.$refs.lineFaultPieForm.show(-1);
          this.$refs.faultAreaPieForm.show(-1);
          this.$refs.faultTypePieForm.show(-1);
          this.$refs.barForm.show(-1);
          return;
        }
        if (this.checkId.length == 0) {
          this.$message.warning("请选择一条记录")
          return;
        }
        this.$refs.lineFaultPieForm.show(this.checkId.join(","));
        this.$refs.faultAreaPieForm.show(this.checkId.join(","));
        this.$refs.faultTypePieForm.show(this.checkId.join(","));
        this.$refs.barForm.show(this.checkId.join(","));
      },
      removeId(id){
        for (var i = 0; i < this.checkId.length; i++){
          if (id == this.checkId[i]){
            this.checkId.splice(i,1);
          }
        }
      },
      //显示当前时间（年月日时分秒）
      timeFormate(timeStamp) {
        let year = new Date(timeStamp).getFullYear();
        let month =new Date(timeStamp).getMonth() + 1 < 10? "0" + (new Date(timeStamp).getMonth() + 1): new Date(timeStamp).getMonth() + 1;
        let date =new Date(timeStamp).getDate() < 10? "0" + new Date(timeStamp).getDate(): new Date(timeStamp).getDate();
        let hh =new Date(timeStamp).getHours() < 10? "0" + new Date(timeStamp).getHours(): new Date(timeStamp).getHours();
        let mm =new Date(timeStamp).getMinutes() < 10? "0" + new Date(timeStamp).getMinutes(): new Date(timeStamp).getMinutes();
        let ss =new Date(timeStamp).getSeconds() < 10? "0" + new Date(timeStamp).getSeconds(): new Date(timeStamp).getSeconds();
        let week = new Date(timeStamp).getDay();
        let weeks = ["日","一","二","三","四","五","六"];
        let getWeek = "星期" + weeks[week];
        this.nowTime = year + "年" + month + "月" + date +"日"+"   "+hh+"  :  "+mm+'  :  '+ss + "（" +getWeek  + "）";
      },
      moment,
      nowTimes(){
        this.timeFormate(new Date());
        setInterval(this.nowTimes,1000);
        this.clear()
      },
      clear(){
        clearInterval(this.nowTimes)
        this.nowTimes = null;
      },
      getCheckList(){
        getAction(this.url.checkList, {}).then((res) => {
          if (res.success) {
            this.checkList = res.result;
            if (this.checkList == null){
              this.msg1 = "暂无数据";
            }else{
              this.msg1 = "";
            }
          }
        })
      },
      getTaskList(){
        getAction(this.url.taskList, {}).then((res) => {
          if (res.success) {
            this.taskDataSource = res.result;
          }
        })
      },
      getAnnouncementList(){
        getAction(this.url.announcementList, {}).then((res) => {
          if (res.success) {
            this.msgDataSource = res.result;
          }
        })
      },
      getAlarmList(){
        getAction(this.url.alarmList, {}).then((res) => {
          if (res.success) {
            this.alarmDataSource = res.result;
          }
        })
      },
      getEquipAlarmList(){
        getAction(this.url.equipAlarmList, {}).then((res) => {
          if (res.success) {
            this.equipAlarmDataSource = res.result;
          }
        })
      },
      getDetectCountList(){
        getAction(this.url.detectCountList, {}).then((res) => {
          if (res.success) {
            this.taskInfo = res.result;
          }
        })
      },
      getSwitchMode(){
        getAction(this.url.switchMode, {}).then((res) => {
          if (res.success) {
            if(res.result.status == 1){
              this.statusClass = "monitor-title-status-1";
              this.status = "空闲"
            }else if (res.result.status == 2){
              this.statusClass = "monitor-title-status-2";
              this.status = "在检"
            }else if (res.result.s == 3){
              this.statusClass = "monitor-title-status-3";
              this.status = "故障"
            }
          }
        })
      }
    }
  }
</script>

<style scoped>
  .fault-body-content-1-body-pie{
    margin: 0;
    padding: 0;
  }
  .fault-body-content-1-body-title{
    margin-top: 10px;
    margin-left: 10px;
  }
  .fault-body-content-1-body{
    border: 1px solid #333333;
    margin: 10px 10px;
    height: 430px;
  }
  .task-info-count-content-body-split{
    margin-left: 0;
    padding-right: 20px;
    display: inline-block;
  }
  .task-info-count-content-body{
    display: inline-block;
    margin-right: 10px;
  }
  .detection-trend-body{
    display: inline-block;
    border-top: 1px solid #333333;
    border-right: 1px solid #333333;
    border-bottom: 1px solid #333333;
    border-left: 1px solid #333333;
    width: 97%;
    float: right;
    height: 400px;
  }

  .task-info-count-content {
    float: right;
    display: inline-block;
  }
  .task-info-count-progress
  {
    width: 95%;
    text-align: center;
    margin-left: 10px;
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .task-info-count{
    border: 1px solid #333333;
    margin-right: -10px;
  }
  .task-info-count{
    margin-left: 10px;
    margin-right: 10px;
    border: 1px solid #333333;
    margin-top: 10px;
  }
  .task-info-content-body{
    font-weight: bolder;
    font-size: 30px;
    color: darkorange;
    margin: 0;
    padding: 0;
  }
  .task-info-body-complate,.task-info-count-title{
    font-weight: bolder;
    font-size: 30px;
  }

  .task-info-body{
    line-height: 30px;
    height: 30px;
    text-align: center;
    display: inline-block;
    margin-left: 40px;
  }
  .task-info-body-other{
    line-height: 30px;
    height: 30px;
    text-align: center;
    display: inline-block;
  }
  .task-info-body-split{
    font-size: 30px;
    color: lightgray;
    padding-left: 30px;
  }
  .task-info-body{
  }
  .fault-body-content-2-alarm{
    width: 49%;
    border-right: 1px solid #333333;
    display: inline-block;
    height: 100%;
  }
  .fault-body-content-2-equip{
    width: 49%;
    float: right;
    border-left: 1px solid #333333;
    height: 100%;
    display: inline-block;
  }
  .fault-body-content-2{
    height: 450px;
    float: right;
    width: 100%;
    display: inline-block;
  }
  .fault-body-content-1{
    height: 450px;
    border-bottom: 1px solid #333333;
    width: 100%;
    float: right;
    display: inline-block;
  }
  .fault-header{
    height: 900px;
    writing-mode: vertical-lr;/*从左向右 从右向左是 writing-mode: vertical-rl;*/
    /*writing-mode: tb-lr;IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
    border: 1px solid #333333;
    text-align: center;
    display: inline-block;
  }
  .detection-trend-header{
    height: 400px;
    writing-mode: vertical-lr;/*从左向右 从右向左是 writing-mode: vertical-rl;*/
    /*writing-mode: tb-lr;IE浏览器的从左向右 从右向左是 writing-mode: tb-rl；*/
    border: 1px solid #333333;
    text-align: center;
    display: inline-block;
  }
  .fault{
    margin-left: 10px;
    margin-right: 10px;
    height: 800px;
    margin-top: 10px;
  }
  .global{
    margin-left: 10px;
    margin-right: 10px;
    height: 120px;
    border: 1px solid #333333;
  }
  .detection-trend{
    margin-left: 10px;
    margin-right: 10px;
    height: 400px;
    margin-top: 10px;
  }
  .task-info{
    margin-left: 10px;
    margin-right: 10px;
    height: 60px;
    border: 1px solid #333333;
    margin-top: 10px;
  }
  .fault-body{
    display: inline-block;
    border-top: 1px solid #333333;
    border-right: 1px solid #333333;
    border-bottom: 1px solid #333333;
    border-left: 1px solid #333333;
    width: 97%;
    float: right;
    height: 900px;
  }
  .task-header-link{
    text-decoration: none;
    float: right;
    margin-right: 20px;
  }
  .msg-header-link{
    text-decoration: none;
    float: right;
    margin-right: 20px;
  }
  .task-body{
    margin-top: 10px;
    height: 300px;
    border: 1px solid #333333;
  }
  .msg-body{
    margin-top: 10px;
    height: 300px;
  }
  .inspection-body-content-text{
    font-family: 'Arial Normal', 'Arial';
    font-weight: bolder;
    font-style: normal;
    font-size: 12px;
    color: #333333;
  }
  .inspection-header{
    border: 1px solid #333333;
    text-align: center;
    line-height: 50px;
    height: 50px;
  }
  .task-header{
    border: 1px solid #333333;
    width: 100%;
    text-align: center;
    line-height: 50px;
    height: 50px;
  }
  .msg-header{
    border: 1px solid #333333;
    width: 100%;
    text-align: center;
    line-height: 50px;
    height: 50px;
    margin-top: 10px;
  }
  .inspection-body{
    border: 1px solid grey;
    margin-top: 10px;
  }
  .inspection-body-icon{
    display: inline-block;
    height: 100px;
    width: 70px;
    display: inline-block;
  }
  .inspection-body-content{
    display: inline-block;
    height: 100px;
    width: 85%;
  }
  .body{
    background: #FFFFFF;
    height: 2000px;
  }
  #header-title{
    font-size: 18px;
    font-family: 'Arial Normal', 'Arial';
    font-weight: bolder;
    font-style: normal;
    color: #333333;
    height: 100px;
    line-height: 100px;
  }
  #header-time{
    font-family: 'Arial Normal', 'Arial';
    font-weight: bolder;
    font-style: normal;
    color: #333333;
    font-size: 16px;
    height: 100px;
    line-height: 100px;
  }
  .monitor-title{
    width: 60px;
    height: 60px;
    background: rgb(0 255 0);
    margin-top: 20px;
  }
  .monitor-title-2{
    width: 60px;
    height: 60px;
    background: rgb(204 204 204);
    margin-top: 20px;
  }
  .monitor-body{
    font-size: 15px;
    font-weight: bolder;
    text-align: center;
    width: 100%;
    display: inline-block;
  }
  .monitor{
    cursor: pointer;
  }
  .monitor-title-status-1{
    width: 40px;
    height: 40px;
    background: rgb(204 204 204);
    margin-top: 35px;
  }
  .monitor-title-status-2{
    width: 40px;
    height: 40px;
    background: rgb(0 255 0);
    margin-top: 35px;
  }
  .monitor-title-status-3{
    width: 40px;
    height: 40px;
    background: rgb(255 0 0);
    margin-top: 35px;
  }
</style>