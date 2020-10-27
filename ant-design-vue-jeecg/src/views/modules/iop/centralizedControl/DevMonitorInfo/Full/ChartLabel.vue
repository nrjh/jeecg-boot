<template>
  <div class="label">
    <div :class="['content-lable', fillMode, `text-dot-${colorMode}`]" @click="openRemot(labelData)">
      <div :class="{ left: true, bg: !fillMode }">
        <span v-if="!fillMode" class="dot"></span>

        <img v-if=" labelData.title.substring(0,2)==='编程' " src="@/assets/fullscreen/icon_biancheng.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='雕刻' " src="@/assets/fullscreen/icon_diaoke.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='红外' " src="@/assets/fullscreen/icon_hongwai.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='检定' " src="@/assets/fullscreen/icon_jianding.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='检定仓' " src="@/assets/fullscreen/icon_jiandingcang.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='耐压' " src="@/assets/fullscreen/icon_naiya.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='上位' " src="@/assets/fullscreen/icon_shangwei.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='视频' " src="@/assets/fullscreen/icon_shipin.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='贴标' " src="@/assets/fullscreen/icon_tiebiao.svg" />
        <img v-else-if=" labelData.title.substring(0,2)==='误差' " src="@/assets/fullscreen/icon_wucha.svg" />
        <img v-else  src="@/assets/fullscreen/icon_jianding.svg" />
      </div>
      <p class="text">{{ labelData.title }}</p>
    </div>

    <remotModal ref="modalForm" @cancelEquip="cancelEquip"></remotModal>
  </div>
</template>
<script>
  import  remotModal from './remotModal'
  import { httpAction ,getAction } from '@/api/manage'
  export default {
    name: 'ChartLabel',
    components: {
      remotModal
    },

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
    },
    data() {
      return {
        tempData:{},
        data:{},
        imgData:{
          // 0:'/img/icon_biancheng.ed70b219.svg',
          // 1:'@/assets/fullscreen/icon_diaoke.svg',
          // 2:'@assets/fullscreen/icon_hongwai.svg',
          // 3:'/assets/fullscreen/icon_jianding.svg',
          // 4:'/assets/fullscreen/icon_jiangdingcang.svg',
          // 5:'/assets/fullscreen/icon_naiya.svg',
          // 6:'/assets/fullscreen/icon_shangwei.svg',
          // 7:'/assets/fullscreen/icon_shipin.svg',
          // 8:'/assets/fullscreen/icon_tiebiao.svg',
          // 9:'/assets/fullscreen/icon_waiguan.svg',
          // 10:'/assets/fullscreen/icon_wucha.svg',
        },
      }
    },
    computed: {
      // imgUrl: function(){
      //   return `@/assets/fullscreen`+this.imgData[this.labelData.index];
      // }
    },
    methods: {
      openRemot(node){
        console.log("11111111",node.node);
        let data={
          areaId:node.node.lineNo,
          equipNo:node.node.equipNo
        }
        this.tempData=data;
        this.updDateEquip(data,'updateEquiopStatue');
        this.$refs.modalForm.show();
      },
      updDateEquip(data,url){
        if(data!=null){
          let edit={
            areaId:data.areaId,
            equipNo:data.equipNo
          }
          let httpurl = '/DevMonitorInfo/devMonitorInfo/'+url;
          let method = 'put';
          let formData;
          formData = Object.assign(edit);
          console.log("表单提交数据",formData)
          httpAction(httpurl,formData,method).then((res)=>{
            console.log("res.success----",res.success)
          })
        }
      },
      cancelEquip(){
        this.updDateEquip(this.tempData,'cancleEquiopStatue');
      }
    }
  }
</script>
<style lang="less" scoped>
  @blue: #00fff1;
  @red: #ff224c;
  @yellow: #ffa10d;

  .label {
    padding: 0 0.6rem 0.1rem 0;
    margin-bottom: 0.15rem;
    box-sizing: border-box;
    display: inline-block;
    transform-origin: center bottom;
    transition: all 0.1s linear;
    &:hover {
      transform: scale(1.2);
    }
    .content-lable {
      max-width: 100%;
      /*width: 100%;*/
      height: 1rem;
      box-sizing: border-box;
      display: inline-flex;
      padding: 0;
      text-align: left;
      border-radius: 0.4rem;
      cursor: pointer;
      &.red {
        height: 0.8rem;
        padding: 0 0.1rem 0 0.2rem;
        background-color: @red;
        .left {
          height: 0.8rem;
          width: 0.8rem;
          img {
            width: 0.4rem;
          }
        }
        .text {
          font-size:0.1rem;
          line-height: 0.8rem;
          color: #fff;
        }
      }
      &.yellow {
        height: 0.8rem;
        padding: 0 0.1rem 0 0.2rem;
        background-color: @yellow;
        .left {
          height: 0.8rem;
          width: 0.8rem;
          img {
            width: 0.4rem;
          }
        }
        .text {
          font-size:0.1rem;
          line-height: 0.8rem;
          color: #fff;
        }
      }
    }
    .left {
      vertical-align: text-bottom;
      display: inline-flex;
      justify-content: center;
      align-items: center;
      position: relative;
      width: 1rem;
      height: 1rem;
      border-radius: 50%;
      &.bg {
        background-color: #000;
      }
      img {
        width: 0.5rem;
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
      flex: 1;
      display: inline-block;
      vertical-align: text-bottom;
      margin: 0 0 0 0.1rem;
      font-size: 0.1rem;
      line-height: 1rem;
      font-family: PingFang SC;
      font-weight: 400;
      color: #eefaff;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
</style>