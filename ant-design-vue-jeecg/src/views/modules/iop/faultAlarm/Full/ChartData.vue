<template>
<div class="chart-card">
  <div class="top">
    <div class="left">
      <div class="pie-out">
        <div class="pie-inside">
          <span class="circle"></span>
          <span>19</span>
        </div>
      </div>
    </div>
    <div class="center">
      <h2 class="title">采集II线检定区</h2>
      <p class="sub">collection I line verification area</p>
      <div class="status">
        <li>
          <span class="dot normal"></span>
          正常
        </li>
        <li>
          <span class="dot error"></span>
          故障
        </li>
        <li>
          <span class="dot operation"></span>
          操作中
        </li>
      </div>
    </div>
    <div class="right">
      <span class="num">0</span>
      <p class="desc">故障数</p>
    </div>
  </div>
  <div class="bottom">
    <chart-label :label-data="{ title: '编程' }" :color-mode="'red'" />
    <chart-label :label-data="{ title: '编程' }" :color-mode="'yellow'" />
    <chart-label :label-data="{ title: '编程' }" :color-mode="'blue'" />
    <chart-label :label-data="{ title: '编程' }" :fill-mode="'red'" />
    <chart-label :label-data="{ title: '编程' }" :fill-mode="'yellow'" />
    <div class="label add-label">
      <span class="add" @click="add">
        <img src="@/assets/fullscreen/icon_add.png" />
      </span>
    </div>
  </div>
</div>
</template>

<script>
import ChartLabel from './ChartLabel'

export default {
  name: 'ChartData',
  components: {
    ChartLabel
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
    add() {}
  }
}
</script>

<style lang="less" scoped>
@blue: #00fff1;
@red: #ff224c;
@yellow: #ffa10d;

.chart-card {
  margin-bottom: .45rem;
  box-sizing: border-box;
  width: 100%;
  padding: 1.05rem .5rem .5rem .5rem;
  background: #153244;
  opacity: .7;
  border: 1px solid #0095ae;
  border-radius: 4px;
  .top {
    display: flex;
    .left {
      flex: 0 0 auto;
      width: 2.78rem;
      height: 2.78rem;
      .pie-out {
        position: relative;
        width: 100%;
        height: 100%;
        background: url('~@/assets/fullscreen/pie_background.png') no-repeat;
        background-size: contain;
        .pie-inside {
          width: 2.1rem;
          height: 2.1rem;
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate(-50%, -50%);
          background: url('~@/assets/fullscreen/pie_chart.png') no-repeat;
          background-size: contain;
          span {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            font-size: 0.6rem;
            font-family: Bahnschrift;
            font-weight: 400;
            color: #EEFAFF;
          }
          .circle {
            display: block;
            width: 1.5rem;
            height: 1.5rem;
            box-sizing: border-box;
            border-radius: 50%;
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            border: 0.1rem solid @blue;
          }
        }
      }
    }
    .center {
      box-sizing: border-box;
      padding-top: 0.5rem;
      flex: 1;
      max-width: 4.2rem;
      height: 2.78rem;
      margin: 0 1.1rem 0 0.58rem;
      font-family: PingFang SC;
      font-weight: 400;
      .title {
        margin: 0 0 0.13rem 0;
        font-size: 0.4rem;
        line-height: 0.4rem;
        color: #FFFFFF;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .sub {
        margin: 0;
        font-size: 0.2rem;
        line-height: 0.2rem;
        color: #596F99;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .status {
        margin: 0.5rem 0 0 0;
        padding: 0;
        list-style: none;
        li {
          display: inline-flex;
          align-items: center;
          font-size: 0.35rem;
          line-height: 0.35rem;
          color: #C7DFEF;
          & + li {
            margin-left: .2rem;
          }
        }
        .dot {
          display: inline-block;
          width: 4px;
          height: 4px;
          margin-right: .15rem;
          border-radius: 50%;
          &.normal {
            background-color: @blue;
          }
          &.error {
            background-color: @red;
          }
          &.operation {
            background-color: @yellow;
          }
        }
      }
    }
    .right {
      box-sizing: border-box;
      padding-top: 0.78rem;
      flex: 0 0 auto;
      height: 2.78rem;
      text-align: center;
      .num {
        margin-bottom: 0.5rem;
        display: block;
        line-height: 0.7rem;
        font-size: 0.7rem;
        font-family: Bahnschrift;
        font-weight: 400;
        color: @red;
      }
      .desc {
        margin: 0;
        font-size: 0.35rem;
        line-height: 0.35rem;
        font-family: PingFang SC;
        font-weight: 400;
        color: #C7DFEF;
      }
    }
  }
  .bottom {
    padding-top: 1.05rem;
    .label {
      width: 33.33%;
    }
    .add-label {
      display: inline-block;
      padding-left: 0.2rem;
    }
    .add {
      display: inline-flex;
      justify-content: center;
      align-items: center;
      width: 0.75rem;
      height: 0.75rem;
      border-radius: 50%;
      background-color:#0E2535;
      cursor: pointer;
      img {
        width: 0.38rem;
      }
    }
  }
}
</style>
