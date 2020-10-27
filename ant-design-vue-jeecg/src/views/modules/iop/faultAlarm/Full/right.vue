<template>
  <div :class="{ 'full-wrap': true, fullscreen: isFull }" ref="full">
    <div class="back">
      <img width="100%" height="100%" src="@/assets/right/bg.png" />
    </div>
    <h1 class="main-title">福建智能运维管理平台</h1>
    <span class="full" @click="toggleFull">
      <img width="100%" height="100%" src="@/assets/right/full.png" />
    </span>
    <section class="time">
      <img src="@/assets/right/time.png" />
      <span class="date">{{nowTime}}</span>
    </section>
    <header class="fake-head"></header>
    <section class="content-wrap">
      <section class="content-block left">
        <header class="nav">
          <ul class="btn-list">
            <li class="btn" @click="getSelectData('0')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.all "/>
              <p class="btm-content">全部</p>
            </li>
            <li class="btn" @click="getSelectData('FJ00AREA0000000000001101001')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.D1" />
              <p class="btm-content">单相Ⅰ三晖</p>
            </li>
            <li class="btn" @click="getSelectData('FJ00AREA0000000000001101002')">
              <span class="state">
<!--
                <img width="100%" height="100%" :src="btnStateImg.test" />
-->
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.D2" />
              <p class="btm-content">单相Ⅱ科陆</p>
            </li>
            <li class="btn" @click="getSelectData('FJ00AREA0000000000001201001')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.S1" />
              <p class="btm-content">三相Ⅰ三晖</p>
            </li>
            <li class="btn" @click="getSelectData('28ee70730f8ed101730f95eca10002')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.S2" />
              <p class="btm-content">三相Ⅱ科陆</p>
            </li>
            <li class="btn" @click="getSelectData('285e0c6e6799a3016e67a808cd0002')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.C1" />
              <p class="btm-content">采集Ⅰ</p>
            </li>
            <li class="btn" @click="getSelectData('285e0c6e6799a3016e67a897ef0003')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.C2" />
              <p class="btm-content">采集Ⅱ</p>
            </li>
            <li class="btn" @click="getSelectData('FJ00AREA0000000000001301001')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.H" />
              <p class="btm-content">互感器</p>
            </li>
            <li class="btn" @click="getSelectData('FJ00AREA0000000000001501001')">
              <span class="state">
                <img width="100%" height="100%" :src="btnState" />
              </span>
              <img :src="checked.CC" />
              <p class="btm-content">智能仓储</p>
            </li>
          </ul>
          <ul class="state-list">
            <li class="state">
              <span class="icon">
                <img width="100%" height="100%" src="@/assets/right/test.png" />
              </span>
              在检测
            </li>
            <li class="state">
              <span class="icon">
                <img width="100%" height="100%" src="@/assets/right/fault.png" />
              </span>
              故障
            </li>
            <li class="state">
              <span class="icon">
                <img width="100%" height="100%" src="@/assets/right/free.png" />
              </span>
              空闲
            </li>
          </ul>
        </header>
        <section class="summary">
          <span class="angle"></span>
          <h2 class="title">检定任务统计信息</h2>
          <section class="item">
            <Summary v-bind:dataList="countList1" />
          </section>
          <section class="item">
            <Summary v-bind:dataList="countList2" />
          </section>
        </section>
        <section class="info">
          <aside class="task">
            <h2 class="title">
            <span v-if="state == '1' || state == '2'">
              检定任务信息
            </span>
            <span v-else-if="state == '3'">
              库存数据
            </span>
            </h2>
            <span class="angle angle-ld"></span>
            <span class="angle angle-rd"></span>
            <section class="pie">
              <Pie v-bind:dataList="countD" />
              <Pie v-bind:dataList="countS"/>
              <Pie v-bind:dataList="countC"/>
              <Pie v-bind:dataList="countH"/>
            </section>
          </aside>
          <section class="charts">
            <section class="trend">
              <Trend ref="trendForm" />
            </section>
            <section class="fault">
              <Fault ref="faultForm" />
            </section>
          </section>
        </section>
      </section>
      <section class="content-block right">
<!--        <div class="infomation">
          <p class="info-1">
            <span class="dot"></span>
            由于机械手库存复检，将合格标记的标签取下
          </p>
          <p class="info-2">
            <span class="name">李美兰</span>
            <span class="info-time">2020-09-12</span>
          </p>
        </div>-->
        <section class="test">
          <h2 class="title" v-if="state == '1' || state == '2'">在检信息</h2>
          <h2 class="title" v-else-if="state == '3'">出入库记录</h2>
          <span class="angle angle-ld"></span>
          <span class="angle angle-ru"></span>
          <div class="fake-head"></div>
          <div class="test-content">
            <info ref="info" />
          </div>
        </section>
        <section class="distribution">
          <h2 class="title">配送任务信息</h2>
          <span class="angle angle-ld"></span>
          <span class="angle angle-rd"></span>
          <div class="distribution-content">
            <table>
              <thead>
                <tr>
                  <th>序号</th>
                  <th>车牌号</th>
                  <th>设备数量</th>
                  <th>出发时间</th>
                  <th>完成时间</th>
<!--                  <th class="more-th">
                    <span class="more">
                      <img width="100%" height="100%" src="@/assets/right/icon_more.png" />
                    </span>
                  </th>-->
                </tr>
              </thead>
            </table>
            <div class="scroll">
              <table>
                <tbody v-for="(item,index) in taskDataSource">
                <tr>
                  <th>
                    {{ index + 1 <= 9 ? '0' + (index + 1) : index + 1 }}
                  </th>
                  <th>{{item.plateNum}}</th>
                  <th>{{item.equipQty}}</th>
                  <th>{{item.departureTime|moment("MM/DD")}}</th>
                  <th>{{item.completeTime|moment("MM/DD")}}</th>
<!--
                  <th></th>
-->
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>
      </section>
    </section>
  </div>
</template>

<script>
import Summary from './summary'
import Pie from './pie'
import Trend from './trend'
import Fault from './fault'
import info from './info'
import {httpAction, getAction} from '@/api/manage'
import moment from 'moment'
import { initDictOptions } from '@/components/dict/JDictSelectUtil'
export default {
  name: 'FullRight',
  components: {
    Summary,
    Pie,
    Trend,
    Fault,
    info
  },
  provide() {
    return {
      getRootSize: () => this.rootSize
    }
  },
  data() {
    return {
      //标记选中的状态 1代表全部，2代表其他的线（除仓储）3.仓储
      state:"1",
      //产能比列表
      capacityRatioList:[],
      //按钮状态
      btnState:"",
      //存放选择的检定线
      checkedLineNO:["0"],
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
        flagH:false,
        flagCC:false,
        all:"",
        D1:"",
        D2:"",
        S1:"",
        S2:"",
        C1:"",
        C2:"",
        H:"",
        CC:""
      },
      status:"",
      nowTime: "",
      isFull: true,
      rootSize: 40,
      taskDataSource:[],
      countList1:[],
      countList2:[],
      countD:[],
      countS:[],
      countC:[],
      countH:[],
      url: {
        taskList: '/centralized/rightScreen/taskList',
        detectCountList:"/centralized/rightScreen/detectCountList",
        switchMode:"/centralized/rightScreen/switchMode"
      },
      btnStateImg: {
        fault: require('@/assets/right/b_state_fault.png'),
        free: require('@/assets/right/b_state_free.png'),
        test: require('@/assets/right/b_state_test.png')
      },
      btnImg: {
        default: require('@/assets/right/btn.png'),
        selected: require('@/assets/right/btn_selected.png')
      }
    }
  },
  mounted() {
    this.initData();
    this.initDictConfig();
    this.setFont();
    this.nowTimes();
    this.getTaskList();
    //1表示全部
    this.getCountList("1");
    this.getSwitchMode();
  },
  methods: {
    selectDataById(areaId){
      var data = areaId.split(",");
      //判断是否是仓储
      var flagCC = false;
      for (var i = 0; i < data.length; i++) {
        if (data[i] == 'FJ00AREA0000000000001501001') {
          flagCC = true;
        }
      }
      if(flagCC){
        this.state = "3";
        this.getCountList(this.state);
        this.$refs.info.show('FJ00AREA0000000000001501001',1);
      }else{
        if (areaId == '0'){
          this.state = "1";
          this.getCountList(this.state);
          this.$refs.info.show("0",1);
        }else{
          this.state = "2"
          this.getCountList(this.state);
          this.$refs.info.show("0",0);
        }
      }
      this.$refs.faultForm.show(areaId);
      this.$refs.trendForm.show(areaId);
    },
    initData(){
        this.checked.all = this.btnImg.selected,
        this.checked.D1 = this.btnImg.default,
        this.checked.D2 = this.btnImg.default,
        this.checked.S1 = this.btnImg.default,
        this.checked.S2 = this.btnImg.default,
        this.checked.C1 = this.btnImg.default,
        this.checked.C2 = this.btnImg.default,
        this.checked.H = this.btnImg.default,
        this.checked.CC = this.btnImg.default
    },
    getSelectData(id){

      if (id == "0"){
        this.checked.flagA = !this.checked.flagA;
        if (this.checked.flagA){
          this.checkedLineNO = [];
          this.checkedLineNO.push(id);
          //将所有的效果还原（与全部按钮互斥）
          this.checked.all = this.btnImg.selected;
          this.checked.D1 = this.btnImg.default;
          this.checked.D2 = this.btnImg.default;
          this.checked.S1 = this.btnImg.default;
          this.checked.S2 = this.btnImg.default;
          this.checked.C1 = this.btnImg.default;
          this.checked.C2 = this.btnImg.default;
          this.checked.CC = this.btnImg.default;
          this.checked.H = this.btnImg.default;
          this.checked.flagD1 = false;
          this.checked.flagD2 = false;
          this.checked.flagS1 = false;
          this.checked.flagS2 = false;
          this.checked.flagC1 = false;
          this.checked.flagC2 = false;
          this.checked.flagH = false;
          this.checked.flagCC = false;
        }else{
          this.removeId(id)
          this.checked.all = this.btnImg.default;
        }
      }
      //单相1
      if (id == 'FJ00AREA0000000000001101001'){
        this.checked.flagD1 = !this.checked.flagD1;
        if (this.checked.flagD1){
          this.removeId("0")
          this.checkedLineNO.push(id)
          this.checked.flagA = false
          this.checked.all = this.btnImg.default;
          this.checked.D1 = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.D1 = this.btnImg.default;
        }
      }
      if (id == 'FJ00AREA0000000000001101002'){
        this.checked.flagD2 = !this.checked.flagD2;
        if (this.checked.flagD2){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.D2 = this.btnImg.selected;
          this.checked.all = this.btnImg.default;
        }else{
          this.removeId(id);
          this.checked.D2 = this.btnImg.default;
        }
      }
      if (id == 'FJ00AREA0000000000001201001'){
        this.checked.flagS1 = !this.checked.flagS1;
        if (this.checked.flagS1){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.all = this.btnImg.default;
          this.checked.S1 = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.S1 = this.btnImg.default;
        }
      }
      if (id == '28ee70730f8ed101730f95eca10002'){
        this.checked.flagS2 = !this.checked.flagS2;
        if (this.checked.flagS2){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.all = this.btnImg.default;
          this.checked.S2 = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.S2 = this.btnImg.default;
        }
      }
      if (id == '285e0c6e6799a3016e67a808cd0002'){
        this.checked.flagC1 = !this.checked.flagC1;
        if (this.checked.flagC1){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.all = this.btnImg.default;
          this.checked.C1 = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.C1 = this.btnImg.default;
        }
      }
      if (id == '285e0c6e6799a3016e67a897ef0003'){
        this.checked.flagC2 = !this.checked.flagC2;
        if (this.checked.flagC2){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.all = this.btnImg.default;
          this.checked.C2 = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.C2 = this.btnImg.default;
        }
      }
      if (id == 'FJ00AREA0000000000001301001'){
        this.checked.flagH = !this.checked.flagH;
        if (this.checked.flagH){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.all = this.btnImg.default;
          this.checked.H = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.H = this.btnImg.default;
        }
      }
      if (id == 'FJ00AREA0000000000001501001'){
        this.checked.flagCC = !this.checked.flagCC;
        if (this.checked.flagCC){
          this.removeId("0")
          this.checked.flagA = false
          this.checkedLineNO.push(id)
          this.checked.all = this.btnImg.default;
          this.checked.CC = this.btnImg.selected;
        }else{
          this.removeId(id);
          this.checked.CC = this.btnImg.default;
        }
      }
      console.log(this.checkedLineNO)
      if (this.checked.flagA){
        this.selectDataById("0");
        return;
      }
      if (this.checkedLineNO.length == 0) {
        this.$message.warning("请选择一条记录")
        return;
      }
      this.selectDataById(this.checkedLineNO.join(","));
    },
    getSwitchMode(){
      getAction(this.url.switchMode, {}).then((res) => {
        if (res.success) {
          this.btnState = res.result.status;
          if (this.btnState == 1){
            this.btnState = this.btnStateImg.free;
          }else if(this.btnState == 2){
            this.btnState = this.btnStateImg.test;
          }else if(this.btnState == 3){
            this.btnState = this.btnStateImg.fault;
          }
        }
      })
    },
    removeId(id){
      for (var i = 0; i < this.checkedLineNO.length; i++){
        if (id == this.checkedLineNO[i]){
          this.checkedLineNO.splice(i,1);
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
      this.nowTime = year + "/" + month + "/" + date +"  "+"   "+hh+"  :  "+mm+'  :  '+ss + "（" +getWeek  + "）";
    },
    nowTimes(){
      this.timeFormate(new Date());
      setInterval(this.nowTimes,1000);
      this.clear()
    },
    clear(){
      clearInterval(this.nowTimes)
      this.nowTimes = null;
    },
    moment,
    setFont() {
      let cw = this.$refs.full.clientWidth
      const fontSize = (cw * 40) / 1920
      this.rootSize = fontSize
      document.documentElement.style.fontSize = `${fontSize}px`
    },
    toggleFull() {
      this.isFull = !this.isFull
      this.$nextTick(this.setFont)
    },
    getTaskList(){
      getAction(this.url.taskList, {}).then((res) => {
        if (res.success) {
          this.taskDataSource = res.result;
          console.log(res.result)
        }
      })
    },
    initDictConfig(){
      initDictOptions('AREA_PRODUCT_PLAN_NUM').then(res => {
        console.log("--------------------------------------------------------------------------------------------")
        console.log(res.result);
        this.capacityRatioList = res.result;
      })
    },
    getCountList(state){
      getAction(this.url.detectCountList, {}).then((res) => {
        if (res.success) {
          console.log("111",res.result)
          this.countList1 = this.getData(res.result.pendMonthTotal,res.result.completeVerificeMonthCount,res.result.completeMonth,'本月');
          this.countList2 = this.getData(res.result.pendDayTotal,res.result.completeVerificeDayCount,res.result.completeDay,'今日');
          //1代表全部 2代表
          if (state == '1') {
            this.countD = this.getDataDSCH(res.result.planD, res.result.completeD, '单相', state,0);
            this.countS = this.getDataDSCH(res.result.planS, res.result.completeS, '三相', state,0);
            this.countC = this.getDataDSCH(res.result.planC, res.result.completeC, '采集', state,0);
            this.countH = this.getDataDSCH(res.result.planH, res.result.completeH, '互感器', state,0);
          }else if(state == '2'){
            let value = 0;
            if (this.checkedLineNO.length > 0 && this.checkedLineNO != null) {
              for (var i = 0; i < this.checkedLineNO.length; i++) {
                for (var j = 0; j < this.capacityRatioList.length; j++) {
                  if (this.checkedLineNO[i] == this.capacityRatioList[j].text) {
                    console.log("333333333333333333333333333");
                    value += parseInt(this.capacityRatioList[j].value);
                  }
                }
              }
            }
            console.log("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrttttttttttttttttttttttttttttttttttttttttttttttttttttttt",value)
            this.countD = this.getDataDSCH(res.result.completeD,value, '单相', state,0);
            this.countS = this.getDataDSCH(res.result.completeS,value,'三相', state,0);
            this.countC = this.getDataDSCH(res.result.completeC,value, '采集', state,0);
            this.countH = this.getDataDSCH(res.result.completeH,value, '互感器', state,0);
          }else if(state == '3'){
            this.countD = this.getDataDSCH(0,0, '单相', state,0);
            this.countS = this.getDataDSCH(0,0,'三相', state,0);
            this.countC = this.getDataDSCH(0,0, '采集', state,0);
            this.countH = this.getDataDSCH(0,0, '互感器', state,0);
          }
        }
      })
    },
    getDataDSCH(num1,num2,title,state,num){
      var num3 = [];
      num3.push({value:num2});
      if (num1 - num2 > 0) {
        num3.push({value: num1 - num2})
      }else{
        num3 = [];
        num3.push({value:num1});
        num3.push({value: 0})
      }
      var data = {
        title:title,
        num1:num1,
        num2:num2,
        data:num3,
        num3:num,
        state:state
      };
      return data;
    },
    getData(num1,num2,cur,title){
      var data = {
        num1: "",
        num2:"",
        cur: "",
        title:""
      }
      data.title = title
      data.num1 = this.getNum(num1);
      data.num2 = this.getNum(num2);
      data.cur = cur;
      console.log(data.num1)
      return data;
    },
    getNum(num){
      //补位其余的位数为0;
      var d = "" + num;
      var l = "";
      if (d.length < 6){
        for (var i = d.length; i < 6; i++){
          l += '0'
        }
      }
      return l + d;
    }
  }
}
</script>

<style lang="less" scoped>
;

::-webkit-scrollbar {
  width: 4px;
  height: 1px;
}
/*滚动条滑块*/
::-webkit-scrollbar-thumb {
  box-sizing: border-box;
  width: 0.2rem;
  height: 1.2rem;
  background: #02464f;
  border: 1px solid #1786c8;
  opacity: 0.92;
  border-radius: 0rem;
}
/*滚动条轨道*/
::-webkit-scrollbar-track {
  right: 0.13rem;
  box-sizing: border-box;
  width: 0.2rem;
  background: #021726;
  border: 1px solid #1786c8;
  border-radius: 0;
}

.back {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  height: 100%;
}
.full-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden auto;
  &.fullscreen {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 1000;
  }
  .main-title {
    position: absolute;
    top: 0.28rem;
    left: 50%;
    transform: translateX(-50%);
    font-size: 0.8rem;
    line-height: 0.8rem;
    font-family: YouSheBiaoTiHei;
    font-weight: 400;
    color: #eefaff;
  }
  .full {
    position: absolute;
    top: 0.53rem;
    right: 0.6rem;
    display: block;
    width: 0.85rem;
    height: 0.85rem;
    cursor: pointer;
    z-index: 1004;
  }
  .time {
    position: absolute;
    left: 0.6rem;
    top: 0.5rem;
    img {
      vertical-align: bottom;
      width: 0.85rem;
      height: 0.85rem;
    }
    .date {
      margin: 0 0 0 0.15rem;
      vertical-align: bottom;
      font-size: 0.53rem;
      // line-height: 0.53rem;
      font-family: Source Han Sans CN;
      font-weight: 400;
      color: #01fafe;
    }
  }
  .fake-head {
    width: 100%;
    height: 1.4rem;
  }
  .content-wrap {
    box-sizing: border-box;
    width: 100%;
    height: calc(100% - 1.4rem);
    padding: 0;
    overflow: hidden auto;
  }
  .content-block {
    display: inline-block;
    vertical-align: top;
    margin-bottom: 0.48rem;
    h2 {
      font-size: 0.45rem;
      font-family: Source Han Sans CN;
      font-weight: bold;
      color: #00fff1;
      line-height: 0.6rem;
    }
  }
  .left {
    position: relative;
    z-index: 1001;
    width: 75%;
    padding: 0 0 0 0.45rem;
    .nav {
      display: flex;
      align-items: flex-end;
      padding: 0.93rem 0 0.3rem 0.75rem;
      .btn-list {
        padding: 0;
        margin: 0;
        list-style: none;
        height: 1.48rem;
        .btn {
          position: relative;
          display: inline-block;
          width: 2.68rem;
          height: 1.48rem;
          transform-origin: center bottom;
          transition: all 0.1s linear;
          cursor: pointer;
          & + .btn {
            margin: 0 0 0 15px;
          }
          .state {
            position: absolute;
            left: -0.9rem;
            top: -0.75rem;
            width: 2.15rem;
            height: 2.13rem;
          }
          img {
            width: 100%;
            height: 100%;
          }
          .btm-content {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            font-size: 0.5rem;
            line-height: 1.48rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #ffffff;
            text-align: center;
          }
/*          &:hover {
            transform: translateY(-0.3rem) scale(1.2);
          }*/
        }
      }
      .state-list {
        padding: 0;
        margin: 0 0 0 0.4rem;
        .state {
          display: inline-flex;
          align-items: center;
          height: 0.95rem;
          & + .state {
            margin: 0 0 0 0.13rem;
          }
          .icon {
            display: inline-block;
            width: 0.95rem;
            height: 0.93rem;
          }
          font-size: 0.45rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #ffffff;
        }
      }
    }
    .summary {
      margin: 0 0 0.33rem 0;
      box-sizing: border-box;
      position: relative;
      padding: 1.35rem 0 0.57rem 0.88rem;
      width: 100%;
      height: 4.88rem;
      background: url('~@/assets/right/left_1.png') no-repeat;
      background-size: contain;
      .angle {
        position: absolute;
        left: 0.14rem;
        top: 0.14rem;
        width: 1.1rem;
        height: 1rem;
        background: url('~@/assets/right/left_up.png') no-repeat;
        background-size: contain;
      }
      .title {
        position: absolute;
        top: 0.33rem;
        left: 4.85rem;
      }
      .item {
        display: inline-block;
        height: 100%;
        width: 50%;
      }
    }
    .info {
      display: flex;
      align-items: flex-start;
      width: 100%;
      .task {
        box-sizing: border-box;
        position: relative;
        // width: 7.43rem;
        width: 21%;
        height: 17.27rem;
        background: url('~@/assets/right/bg_task.png') no-repeat;
        background-size: contain;
        .angle {
          display: block;
          position: absolute;
          bottom: 0;
          width: 1rem;
          height: 1rem;
        }
        .angle-ld {
          left: 0.15rem;
          background: url('~@/assets/right/left_down.png') no-repeat;
          background-size: contain;
        }
        .angle-rd {
          right: 0.15rem;
          background: url('~@/assets/right/right_down.png') no-repeat;
          background-size: contain;
        }
        .title {
          position: absolute;
          top: 0.33rem;
          left: 2.13rem;
        }
        .pie {
          box-sizing: border-box;
          height: 100%;
          width: 100%;
          padding: 2.13rem 0.8rem 1.4rem 0.8rem;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
        }
      }
      .charts {
        box-sizing: border-box;
        width: 79%;
        padding: 0 0 0 0.33rem;
        .trend {
          width: 100%;
          height: 6.88rem;
          margin: 0 0 0.33rem 0;
        }
        .fault {
          width: 100%;
          height: 10.07rem;
        }
      }
    }
  }
  .right {
    box-sizing: border-box;
    width: 25%;
    padding: 0 0.33rem;
    .infomation {
      position: relative;
      z-index: 1003;
      box-sizing: border-box;
      // height: 2.7rem;
      padding: 0 1.33rem 0.25rem 0;
      .info-1 {
        margin: 0;
        text-align: right;
        .dot {
          display: inline-block;
          vertical-align: middle;
          width: 0.13rem;
          height: 0.13rem;
          background: #2bfff3;
          border-radius: 50%;
        }
        font-size: 0.4rem;
        line-height: 0.4rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #e4e9ff;
        opacity: 0.73;
      }
      .info-2 {
        margin: 0.38rem 0 0 0;
        height: 0.35rem;
        line-height: 0.35rem;
        text-align: right;
        .name {
          margin: 0 0.55rem 0 0;
          font-size: 0.35rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #8490c0;
        }
        .info-time {
          font-size: 0.35rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #00b0ae;
        }
      }
    }
    .test {
      box-sizing: border-box;
      position: relative;
      margin: 0 0 0.33rem 0;
      width: 100%;
      height: 16.63rem;
      padding: 0 0.12rem 0 0;
      background: url('~@/assets/right/bg_test.png') no-repeat;
      background-size: contain;
      h2 {
        position: absolute;
        top: 0.33rem;
        left: 4.68rem;
      }
      .angle {
        display: block;
        position: absolute;
        width: 1rem;
        height: 1rem;
      }
      .angle-ld {
        bottom: 0.05rem;
        left: 0.13rem;
        background: url('~@/assets/right/left_down.png') no-repeat;
        background-size: contain;
      }
      .angle-ru {
        top: 0.15rem;
        right: 0.13rem;
        background: url('~@/assets/right/right_up.png') no-repeat;
        background-size: contain;
      }
      .fake-head {
        width: 100%;
        height: 1.13rem;
      }
      .test-content {
        box-sizing: border-box;
        width: 100%;
        height: calc(100% - 1.13rem);
        padding: 0 0.36rem 0.35rem 0.48rem;
        overflow: hidden auto;
        .test-info {
          & + .test-info {
            margin: 1rem 0 0 0;
          }
        }
      }
    }
    .distribution {
      box-sizing: border-box;
      position: relative;
      width: 100%;
      height: 6.88rem;
      padding: 0;
      background: url('~@/assets/right/bg_distribution.png') no-repeat;
      background-size: contain;
      h2 {
        position: absolute;
        top: 0.33rem;
        left: 4.2rem;
      }
      .angle {
        display: block;
        position: absolute;
        bottom: 0.07rem;
        width: 1rem;
        height: 1rem;
      }
      .angle-ld {
        left: 0.15rem;
        background: url('~@/assets/right/left_down.png') no-repeat;
        background-size: contain;
      }
      .angle-rd {
        right: 0.1rem;
        background: url('~@/assets/right/right_down.png') no-repeat;
        background-size: contain;
      }
      .distribution-content {
        box-sizing: border-box;
        width: 100%;
        height: 100%;
        padding: 1.3rem 0.25rem 0.45rem 0.25rem;
        .scroll {
          height: 4rem;
          overflow: hidden auto;
        }
        table {
          width: 100%;
          tr {
            height: 1rem;
            font-size: 0.35rem;
            line-height: 1rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #ffffff;
            th {
              padding: 0;
              width: 20%;
              text-align: center;
              &:first-child {
                padding: 0 0 0 1.23rem;
              }
              &:last-child{
                padding: 0 0.5rem 0 0;
              }
              &.more-th {
                padding: 0 0.1rem 0 0.1rem;
                .more {
                  display: inline-block;
                  width: 0.48rem;
                  height: 1rem;
                  cursor: pointer;
                }
              }
            }
          }
          thead tr {
            background: rgba(23, 134, 200, 0.1);
          }
          tbody tr {
            &:nth-child(even) {
              background: rgba(23, 134, 200, 0.16);
            }
            &:nth-child(odd) {
              background: rgba(0, 0, 0, 0.16);
            }
          }
        }
      }
    }
  }
}
</style>
