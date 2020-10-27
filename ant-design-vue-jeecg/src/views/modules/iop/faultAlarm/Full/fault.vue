<template>
  <div class="fault-pie">
    <h2>故障统计
    </h2>
    <span class="angle angle-ld"></span>
    <span class="angle angle-rd"></span>
    <div class="content-wrap">
      <div class="pie-wrap">
        <FaultPie v-bind:dataList="lineFaultListDataSource"/>
        <FaultPie v-bind:dataList="faultTypeListDataSource" :needPadding="true"/>
        <FaultPie v-bind:dataList="faultAreaListDataSource" :needPadding="true" />
      </div>
      <div class="pie-table">
        <section class="left">
          <table class="table table-1">
            <thead>
              <tr>
                <th>序号</th>
                <th>报警时间</th>
                <th>报警名称</th>
                <th>检定线</th>
                <th>设备名称</th>
                <th>故障等级</th>
                <th>故障状态</th>
                <th>
                  处理时长
                </th>
<!--                <th>
                  <span class="more">
                    <img width="100%" height="100%" src="@/assets/right/icon_more.png" />
                  </span>
                </th>-->
              </tr>
            </thead>
          </table>
          <div class="scroll">
            <table class="table table-1">
              <tbody v-for="(item,index) in alarmDataSource">
              <tr class="tr1">
                <th>{{ index + 1 <= 9 ? '0' + (index + 1) : index + 1 }}</th>
                <th>{{item.alarmTime|moment("YYYY-MM-DD")}}</th>
                <th>{{item.alarmName.length > 4 ?  item.alarmName.substring(0,4) + '...' : item.alarmName }}</th>
                <th>{{item.areaName}}</th>
                <th>{{item.equipName.length >= 7 ? item.equipName.substring(0,3) + '...' : item.equipName}}</th>
                <th :class="levelClass[item.failureLevel]" >{{ level[item.failureLevel] }}</th>
                <th>{{item.faultStatus == '1' ? '是':'否'}}</th>
                <th v-if="">{{(item.handleTime != '' && item.handleTime != null) ? item.handleTime + '小时' : ''}}</th>
<!--                <th></th>-->
              </tr>
              </tbody>
            </table>
          </div>
        </section>
        <section class="right">
          <table class="table table-2">
            <thead>
              <tr>
                <th>序号</th>
                <th>检定线</th>
                <th>设备名称</th>
                <th>故障频率</th>
                <!--<th>处理时长</th>-->
<!--                <th>
                  <span class="more">
                    <img width="100%" height="100%" src="@/assets/right/icon_more.png" />
                  </span>
                </th>-->
              </tr>
            </thead>
          </table>
          <div class="scroll">
            <table class="table table-2">
              <tbody v-for="(item,index) in equipAlarmDataSource">
              <tr>
                <th>{{ index + 1 <= 9 ? '0' + (index + 1) : index + 1 }}</th>
                <th>{{item.areaName}}</th>
                <th>{{item.equipName}}</th>
                <th>{{item.frequency}}</th>
                <!--<th v-if="item.handleTimeTotal !='' && item.handleTimeTotal != null">
                  {{item.handleTimeTotal}}小时</th>-->
<!--                <th></th>-->
              </tr>
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>
<script>
import FaultPie from './FaultPie'
import {httpAction, getAction} from '@/api/manage'
import moment from 'moment'
export default {
  name: 'Fault',
  components: {
    FaultPie
  },
  data() {
    return {
      equipAlarmDataSource:[],
      alarmDataSource:[],
      lineFaultListDataSource:[],
      faultAreaListDataSource:[],
      faultTypeListDataSource:[],
      url:{
        equipAlarmList:"/centralized/rightScreen/equipAlarmList",
        alarmList:"/centralized/rightScreen/alarmList",
        lineFaultList:"/centralized/rightScreen/lineFaultList",
        faultAreaList:"/centralized/rightScreen/faultAreaList",
        faultTypeList:"/centralized/rightScreen/faultTypeList"
      },
      level:{
        A:'A级',
        1:'A级',
        高:'A级',
        B:'B级',
        2:'B级',
        中:'B级',
        C:'C级',
        3:'C级',
        低:'C级'
      },
      levelClass:{
        A:'level-a',
        1:'level-a',
        高:'level-a',
        B:'level-b',
        2:'level-b',
        中:'level-b',
        C:'level-c',
        3:'level-c',
        低:'level-c',
      }
    }
  },
  beforeMount() {
  },
  mounted() {
    this.getEquipAlarmList("0");
    this.getAlarmList("0");
    this.getLineFaultList("0");
    this.getFaultAreaList("0");
    this.getFaultTypeList("0");
  },
  methods: {
    show(areaId){
      this.getEquipAlarmList(areaId);
      this.getAlarmList(areaId);
      this.getLineFaultList(areaId);
      this.getFaultAreaList(areaId);
      this.getFaultTypeList(areaId);
    },
    moment,
    getAlarmList(areaId) {
      getAction(this.url.alarmList, {areaId: areaId}).then((res) => {
        if (res.success) {
          this.alarmDataSource = res.result;
          console.log(this.alarmDataSource)
        }
      })
    },
    getEquipAlarmList(areaId) {
      getAction(this.url.equipAlarmList, {areaId: areaId}).then((res) => {
        if (res.success) {
          this.equipAlarmDataSource = res.result;
          console.log(this.equipAlarmDataSource)
        }
      })
    },
    getLineFaultList(areaId) {
      getAction(this.url.lineFaultList, {areaId: areaId}).then((res) => {
        if (res.success) {
          console.log("asdddddddddddddddddddddddddddddddddd", res.result)
          this.lineFaultListDataSource = res.result;
          this.lineFaultListDataSource.title= '流水线 \n故障占比'
        }
      })
    },
    getFaultAreaList(areaId){
      getAction(this.url.faultAreaList, {areaId:areaId}).then((res) => {
        if (res.success) {
          console.log("22222222222222222",res.result)
          this.faultAreaListDataSource = res.result;
          this.faultAreaListDataSource.title = '故障区 \n域占比'
        }
      })
    },
    getFaultTypeList(areaId){
      getAction(this.url.faultTypeList, {areaId:areaId}).then((res) => {
        if (res.success) {
          console.log("2222222222222221111111111111111111111111111111111122",res.result)
          this.faultTypeListDataSource = res.result;
          this.faultTypeListDataSource.title = '故障类 \n型占比'
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
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

.fault-pie {
  position: relative;
  width: 100%;
  height: 100%;
  background: url('~@/assets/right/bg_fault.png') no-repeat;
  background-size: contain;
  h2 {
    position: absolute;
    top: 0.33rem;
    left: 12.75rem;
    font-size: 0.45rem;
    font-family: Source Han Sans CN;
    font-weight: bold;
    color: #00fff1;
    line-height: 0.6rem;
  }
  .angle {
    display: block;
    position: absolute;
    bottom: 0.05rem;
    width: 1rem;
    height: 1rem;
    opacity: 0.7;
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
  .content-wrap {
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    padding: 0 0.15rem 0 0.15rem;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    .pie-wrap {
      padding: 0.13rem 0.57rem 0 0.6rem;
      flex: 1;
      display: inline-flex;
    }
    .pie-table {
      display: inline-flex;
      justify-content: space-between;
      width: 100%;
      // height: 5rem;
      .left {
        width: 17.75rem;
      }
      .right {
        width: 9.2rem;
      }
      .scroll {
        height: 4.1rem;
        overflow: hidden auto;
      }
      table {
        // display: inline-block;
        thead {
          tr {
            .more {
              display: inline-block;
              width: 0.48rem;
              height: 1rem;
              cursor: pointer;
            }
          }
        }
        tr {
          box-sizing: border-box;
          height: 1rem;
          th {
            padding: 0;
            font-size: 0.35rem;
            line-height: 1rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #ffffff;
            text-align: center;
            &:first-child {
              padding: 0 0 0 0.4rem;
            }
          }
        }
      }
      .table-1 {
        width: 17.75rem;
        thead {
          tr {
            background: rgba(0, 0, 0, 0.2);
          }
        }
        tr {
          &.tr1 {
            background: rgba(242, 83, 75, 0.2);
          }
          &.tr2 {
            background: rgba(255, 182, 75, 0.2);
          }
          &.tr3 {
            background: rgba(240, 225, 19, 0.2);
          }
          .level-a {
            color: #f2534b;
          }
          .level-b {
            color: #ffb64b;
          }
          .level-c {
            color: #f0e113;
          }
          th {
            &:first-child{
              width: 7%;
            }
            &:nth-child(5){
              width: 12%;
            }
            &:nth-child(6),
            &:nth-child(7),
            &:nth-child(8){
              width: 10%;
            }
          }
        }
      }
      .table-2 {
        width: 9.2rem;
        tr {
          width: 9.2rem;
          margin: 0;
          padding: 0;
          th {
            &:first-child{
              width: 15%;
            }
            &:nth-child(2),
            &:nth-child(3){
              width: 24.5%;
            }
            &:nth-child(4),
            &:nth-child(5){
              width: 18%;
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
</style>
