<template>
<div class="label">
  <div :class="['content', fillMode, `text-dot-${colorMode}`]">
    <div :class="{'left': true, 'bg': !fillMode}">
      <span v-if="!fillMode" class="dot"></span>
      <img src="@/assets/fullscreen/icon_biancheng.svg" />
    </div>
    <p class="text">{{ labelData.title }}</p>
  </div>
</div>
</template>

<script>
export default {
  name: 'ChartLabel',
  props: {
    fillMode: {
      type: [Boolean, String],
      validator(val) {
        return ['red', 'yellow', false].includes(val)
      }
    },
    colorMode: {
      type: [Boolean, String],
      validator(val) {
        return ['red', 'yellow', 'blue', false].includes(val)
      }
    },
    labelData: {
      type: Object,
      required: true
    }
  }
}
</script>

<style lang="less" scoped>
@blue: #00fff1;
@red: #ff224c;
@yellow: #ffa10d;

.label {
  margin-bottom: 0.45rem;
  box-sizing: border-box;
  display: inline-block;
  .content {
    max-width: 100%;
    height: 0.75rem;
    box-sizing: border-box;
    display: inline-block;
    padding: 0 0.35rem 0 0.2rem;
    text-align: left;
    border-radius: 0.35rem;
    cursor: pointer;
    &.red {
      background-color: @red;
      .text {
        font-size: 0.4rem;
        color: #fff;
      }
    }
    &.yellow {
      background-color: @yellow;
      .text {
        font-size: 0.4rem;
        color: #fff;
      }
    }
    &.text-dot-red {
      .dot {
        background-color: @red;
      }
      .text {
        color: @red;
      }
    }
    &.text-dot-yellow {
      .dot {
        background-color: @yellow;
      }
      .text {
        color: @yellow;
      }
    }
  }
  .left {
    vertical-align: text-bottom;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    position: relative;
    width: 0.75rem;
    height: 0.75rem;
    border-radius: 50%;
    &.bg {
      background-color:#0E2535;
    }
    img {
      width: 0.4rem;
    }
    .dot {
      position: absolute;
      left: 0.07rem;
      top: 0.07rem;
      display: block;
      width: 0.13rem;
      height: 0.13rem;
      border-radius: 50%;
      background-color: @blue;
    }
  }
  .text {
    max-width: 70%;
    display: inline-block;
    vertical-align: text-bottom;
    margin: 0 0 0 0.1rem;
    font-size: 0.35rem;
    line-height: 0.75rem;
    font-family: PingFang SC;
    font-weight: 400;
    color: #EEFAFF;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>
