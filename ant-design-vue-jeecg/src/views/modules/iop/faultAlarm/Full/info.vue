<template>
  <div class="test-info" v-show="state">
    <div class="test-wrap" v-for="(item,index) in inOutDataSource">
     <div class="test-top">
        <div class="online">
          {{item.sortCode}}
        </div>
<!--        <p>
          <span class="num">{{item.totalTask}}</span>
          <span class="label">任务总量</span>
        </p>
        <p>
          <span class="num">{{item.amountCompleted}}</span>
          <span class="label">当天完成量</span>
        </p>
        <p>
          <span class="num">{{item.cumulativeCompletion}}</span>
          <span class="label">累计完成量</span>
        </p>-->
      </div>
      <div class="test-bottom">
        <p>
          <span class="label">任务单号：</span>
          <span class="info">{{item.taskId.length > 12 ? item.taskId.substring(0,9) + '...' : item.taskId}}</span>
        </p>
        <p>
          <span class="label">批次：</span>
          <span class="info">{{item.batchId.length > 16 ? item.batchId.substring(0,13) + '...' : item.batchId}}</span>
        </p>
        <p>
          <span class="label">箱条码：</span>
          <span class="info">{{item.boxBarCode}}</span>
        </p>
        <p>
          <span class="label">设备类别：</span>
          <span class="info">{{item.equipCateg.length > 6 ? item.equipCateg.substring(0,3) + '...' : item.equipCateg}}</span>
        </p>
      </div>
      <div class="test-bottom" style="margin-top: 20px;">
        <p>
          <span class="label">垛号：</span>
          <span class="info"></span>
          <span class="info">{{item.pileNo.length > 20 ? item.pileNo.substring(0,12) + '...' : item.pileNo}}</span>
        </p>
        <p>
          <span class="label">托盘号：</span>
          <span class="info">{{item.palletNo}}</span>
        </p>
        <p>
          <span class="label">系统编号：</span>
          <span class="info">{{item.sysName == '' ? item.sysNo : item.sysName}}</span>
        </p>
        <p>
          <span class="label">处理时间：</span>
          <span class="info">{{item.writeDate|moment("YYYY/MM/DD")}}</span>
        </p>
      </div>
    </div>
  </div>
</template>
<script>
  import {httpAction, getAction} from '@/api/manage'
  import moment from 'moment'
  export default {
  name: 'TestInfo',
  data() {
    return {
      //状态1 显示 0不显示
      state:true,
      inOutDataSource:[],
      url: {
        inOutEquipList: "/centralized/rightScreen/inOutEquipList",
      }
    }
  },
  mounted() {
    this.getCheckList("0");
  },
  methods:{
    moment,
    show(areaId,state){
      console.log("23333333333333333333333333333333")
      console.log(areaId)
      if (state == 1) {
        this.state = true;
      }else{
        this.state = false;
      }
      this.getCheckList(areaId);
    },
    getCheckList(areaId){
      getAction(this.url.inOutEquipList, {areaId:areaId}).then((res) => {
        if (res.success) {
          this.inOutDataSource = res.result;
          console.log(",,,,,,,,,,,", res.result)
        }
      })
    },
  }
}
</script>
<style lang="less" scoped>
.test-info {
  width: 100%;
  .test-top {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    padding: 0 1rem 0 0;
    margin: 0 0 0.23rem 0;
    .online {
      display: inline-block;
      width: 1.73rem;
      height: 1.73rem;
      background: url('~@/assets/right/icon_online.png') no-repeat;
      background-size: contain;
      font-size: 0.35rem;
      line-height: 1.73rem;
      font-family: Source Han Sans CN;
      font-weight: 400;
      color: #ffffff;
      text-align: center;
    }
    p {
      display: inline-block;
      vertical-align: top;
      margin: 0;
      span {
        display: block;
      }
      .num {
        margin: 0 0 0.25rem 0;
        font-size: 0.4rem;
        line-height: 0.4rem;
        font-family: Source Han Sans CN;
        font-weight: bold;
        color: #e4e9ff;
      }
      .label {
        font-size: 0.35rem;
        line-height: 0.35rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #00cfcd;
      }
    }
  }
  .test-bottom {
    padding: 0 0 0 0.35rem;
    display: flex;
    flex-wrap: wrap;
    p {
      margin: 0;
      vertical-align: top;
      width: 50%;
      line-height: 0.35rem;
      span {
        display: inline-block;
        font-size: 0.35rem;
        font-family: Source Han Sans CN;
        font-weight: 400;
        color: #e4e9ff;
      }
      .label {
        min-width: 1.53rem;
      }
      &:nth-child(1),
      &:nth-child(2) {
        margin: 0 0 0.33rem 0;
      }
    }
  }
  .test-wrap + .test-wrap {
    margin: 1rem 0 0 0;
  }
}
</style>
