<template>
  <div class="chart-card">
    <h2 class="sub-title"  v-if="dataList.length>0">
      {{ dataList[0].areaName }}
    </h2>
    <span class="angle angle-ld"></span>
    <span class="angle angle-rd"></span>
    <div class="top">
      <section class="left">
        <div class="icon"></div>
        <p class="info">
          <span class="desc">终端设备数量</span>
          <span class="num">{{ dataList.length }}</span>
        </p>
      </section>
      <span class="line"></span>
      <section  v-if="dataList.length>0" :class="{ right: true, error: dataList[0].faultNum> 0 }">
        <div class="icon bg-fault"></div>
        <p class="info">
          <span class="desc">故障数</span>
          <span class="num">{{ dataList[0].faultNum>0 ? dataList[0].faultNum:0}}</span>
        </p>
      </section>
      <section  v-else  :class="{ right: true, error: false }">
        <div class="icon bg-fault"></div>
        <p class="info">
          <span class="desc">故障数</span>
          <span class="num">0</span>
        </p>
      </section>
    </div>
    <div class="bottom" >
      <chart-label v-for="(node,index) in dataList"   :label-data="{index:index, title:  node.equipName,node:node  }"  :key="node.equipNo"   v-if="node.isHandled==='0'" :color-mode="'red'"  :fill-mode="'red'" />
      <chart-label v-else-if ="node.isHandled==='2'"  :label-data="{index:index, title:  node.equipName ,node:node }"     :color-mode="'yellow'" :fill-mode="'yellow'"  />
      <chart-label v-else   :label-data="{ index:index, title:  node.equipName,node:node  }"     :color-mode="'blue'"    />
      <div class="label add-label">
        <span class="add" @click="add">
          <img src="@/assets/fullscreen/icon_add.png" />
        </span>
      </div>
    </div>
    <devMonitorInfo-modal ref="modalForm"  @searchQuery="searchQuery" ></devMonitorInfo-modal>
  </div>
</template>

<script>
  import ChartLabel from './ChartLabel'
  import DevMonitorInfoModal from '../modules/DevMonitorInfoModal'

  export default {
    name: 'ChartData',
    props:{
      dataList:{
        type:Array,
        required:true
      }
    },
    components: {
      ChartLabel,
      DevMonitorInfoModal
    },
    data() {
      return {
        fl: {
          normal: 50,
          error: 10,
          operation: 20
        }
      }
    },
    methods: {
      add() {
        // this.$router.push({path:'/DevMonitorInfo/devMonitorInfo'})
        this.$refs.modalForm.edit();
      },
      searchQuery(){
        console.log("loadData-------------",this.$emit('loadData'))
        this.$emit('loadData');
      }
    }
  }
</script>

<style lang="less" scoped>
  @blue: #00fff1;
  @red: #ff224c;
  @yellow: #ffa10d;

  .chart-card {
    position: relative;
    box-sizing: border-box;
    margin-bottom: 0.3rem;
    box-sizing: border-box;
    width: 100%;
    padding: 1.55rem 0.5rem 0.3rem 0.7rem;
    background: url('~@/assets/fullscreen/card_bg.png') no-repeat;
    background-size: cover;
    .sub-title {
      margin: 0;
      position: absolute;
      top: 0.3rem;
      left: 50%;
      transform: translateX(-50%);
      font-size: 0.45rem;
      font-family: Source Han Sans CN;
      font-weight: bold;
      color: #00fff1;
      line-height: 0.6rem;
    }
    .angle {
      display: block;
      position: absolute;
      bottom: 0;
      width: 1rem;
      height: 1rem;
    }
    .angle-ld {
      left: 0.13rem;
      background: url('~@/assets/right/left_down.png') no-repeat;
      background-size: contain;
    }
    .angle-rd {
      right: 0.13rem;
      background: url('~@/assets/right/right_down.png') no-repeat;
      background-size: contain;
    }
    .top {
      section {
        display: inline-flex;
        vertical-align: top;
        align-items: flex-end;
        .icon {
          width: 2.22rem;
          height: 2.22rem;
          background: url('~@/assets/fullscreen/number_eq.png') no-repeat;
          background-size: contain;
          &.bg-fault {
            background: url('~@/assets/fullscreen/number_fault_n.png') no-repeat;
            background-size: contain;
          }
        }
        .info {
          margin: 0 0 0 0.4rem;
          .desc {
            margin: 0 0 0.5rem 0;
            display: block;
            font-size: 0.4rem;
            line-height: 0.4rem;
            font-family: Source Han Sans CN;
            font-weight: 400;
            color: #ffffff;
          }
          .num {
            display: block;
            font-size: 1.05rem;
            line-height: 1.05rem;
            font-family: Source Han Sans CN;
            font-weight: bold;
            color: #23b5af;
          }
        }
        &.error {
          .info {
            .num {
              color: #ff224c;
            }
          }
          .icon {
            background: url('~@/assets/fullscreen/number_error.png') no-repeat;
            background-size: contain;
          }
        }
      }
      .line {
        display: inline-block;
        vertical-align: top;
        height: 2.17rem;
        width: 0.03rem;
        margin: 0 0.63rem;
        background: url('~@/assets/fullscreen/line.png') no-repeat;
        background-size: contain;
      }
    }
    .bottom {
      padding-top: 1.1rem;
      .label {
        width: 33.33%;
        vertical-align: top;
      }
      .add-label {
        display: inline-block;
        vertical-align: top;
        padding-left: 0.1rem;
      }
      .add {
        display: inline-flex;
        justify-content: center;
        align-items: center;
        width: 1rem;
        height: 1rem;
        border-radius: 50%;
        background-color: #000000;
        cursor: pointer;
        img {
          width: 0.5rem;
        }
      }
    }
  }
</style>
