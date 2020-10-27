<template>
  <div :class="{ 'full-wrap': true, fullscreen: isFull }" ref="full">
    <div class="back">
      <img width="100%" height="100%" src="@/assets/right/bg.png" />
    </div>
    <h1 class="title">AIOPS_故障综合管控</h1>
    <header class="head">
      <aside class="time">
        <img src="@/assets/right/time.png" />
        <span class="date">{{ nowTime }}</span>
      </aside>
      <aside class="full" @click="toggleFull">
        <img width="100%" height="100%" src="@/assets/right/full.png" />
      </aside>
      <ul class="state-list">
        <li class="state">
          <span class="icon">
            <img width="100%" height="100%" src="@/assets/right/test.png" />
          </span>
          正常
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
          操作中
        </li>
      </ul>
    </header>
    <section class="main-box">
      <ul class="main-col">
        <chart-data v-bind:dataList="listCollectionOne"  @loadData="loadData"/>
        <chart-data v-bind:dataList="listThreephaseOne"  @loadData="loadData"/>
      </ul>
      <ul class="main-col">
        <chart-data v-bind:dataList="listCollectionTwo"  @loadData="loadData"/>
        <chart-data v-bind:dataList="listThreephaseTwo"  @loadData="loadData" />
      </ul>
      <ul class="main-col">
        <chart-data v-bind:dataList="listSimplexOne"  @loadData="loadData"/>
        <chart-data v-bind:dataList="listTransformer" @loadData="loadData"/>
      </ul>
      <ul class="main-col">
        <chart-data v-bind:dataList="listSimplexTwo"  @loadData="loadData"/>
        <chart-data v-bind:dataList="listcangchu" @loadData="loadData"/>
        <!--      <chart-data />-->
      </ul>
    </section>
  </div>
</template>

<script>
  import screenfull from 'screenfull'
  import ChartData from './ChartData'
  import { getAction} from '@/api/manage'
  import store from '@/store/'

  export default {
    name: 'FullLeft',
    components: {
      ChartData
    },
    data() {
      return {
        nowTime:'',
        listSimplexOne:[],
        listSimplexTwo:[],
        listCollectionOne:[],
        listCollectionTwo:[],
        listThreephaseOne:[],
        listThreephaseTwo:[],
        listTransformer:[],
        listcangchu:[],
        isFull:true,
        url:{
          list: "/DevMonitorInfo/devMonitorInfo/queryDevList",
        },
      }
    },
    mounted() {
      this.setFont();
      this.nowTimes();

      this.initWebSocket();
      this.heartCheckFun();
    },
    beforeMount() {
      this.loadData();
    },
    methods: {
      setFont() {
        let cw = this.$refs.full.clientWidth
        const fontSize = (cw * 40) / 1920
        document.documentElement.style.fontSize = `${fontSize}px`
      },
      toggleFull() {
        this.isFull = !this.isFull
        this.$nextTick(this.setFont)
      },
      loadData(){
        getAction(this.url.list).then((res) => {
          console.log("res.result",res)
          this.listSimplexOne=res.result.listSimplexOne;
          this.listSimplexTwo=res.result.listSimplexTwo;
          this.listCollectionOne=res.result.listCollectionOne;
          this.listCollectionTwo=res.result.listCollectionTwo;
          this.listThreephaseOne=res.result.listThreephaseOne;
          this.listThreephaseTwo=res.result.listThreephaseTwo;
          this.listTransformer=res.result.listTransformer;
          this.listcangchu=res.result.listcangchu;
        })
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
      nowTimes(){
        this.timeFormate(new Date());
        setInterval(this.nowTimes,1000);
        this.clear()
      },
      clear(){
        clearInterval(this.nowTimes)
        this.nowTimes = null;
      },
      initWebSocket: function () {
        // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
        var userId = store.getters.userInfo.id;
        var url = window._CONFIG['domianURL'].replace("https://","wss://").replace("http://","ws://")+"/websocket/"+userId;
        console.log("url--------------",url);
        this.websock = new WebSocket(url);
        this.websock.onopen = this.websocketOnopen;
        this.websock.onerror = this.websocketOnerror;
        this.websock.onmessage = this.websocketOnmessage;
        this.websock.onclose = this.websocketOnclose;
      },
      heartCheckFun(){
        var that = this;
        //心跳检测,每20s心跳一次
        that.heartCheck = {
          timeout: 20000,
          timeoutObj: null,
          serverTimeoutObj: null,
          reset: function(){
            clearTimeout(this.timeoutObj);
            //clearTimeout(this.serverTimeoutObj);
            return this;
          },
          start: function(){
            var self = this;
            this.timeoutObj = setTimeout(function(){
              //这里发送一个心跳，后端收到后，返回一个心跳消息，
              //onmessage拿到返回的心跳就说明连接正常
              that.websocketSend("leftDataUpdate");
              that.loadData();
              // console.info("客户端发送心跳");
              self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
              }, self.timeout)
            }, this.timeout)
          }
        }
      },
      websocketOnopen: function () {
        console.log("WebSocket连接成功");
        //心跳检测重置
        this.heartCheck.reset().start();
      },
      websocketOnerror: function (e) {
        console.log("WebSocket连接发生错误");
        this.reconnect();
      },
      websocketOnmessage: function (e) {
        console.log("-----接收消息-------",e.data);
        var data = eval("(" + e.data + ")"); //解析对象
        if(data.msgTxt == "leftDataUpdate"){
          //系统通知
          this.loadData();
        }
        // else if(data.cmd == "leftDataUpdate"){
        //   //用户消息
        //   this.loadData();
        // }
        //心跳检测重置
        this.heartCheck.reset().start();
      },
      websocketOnclose: function (e) {
        console.log("connection closed (" + e.code + ")");
        this.reconnect();
      },
      websocketSend(text) { // 数据发送
        try {
          this.websock.send(text);
        } catch (err) {
          console.log("send failed (" + err.code + ")");
        }
      },
      reconnect() {
        var that = this;
        if(that.lockReconnect) return;
        that.lockReconnect = true;
        //没连接上会一直重连，设置延迟避免请求过多
        setTimeout(function () {
          console.info("尝试重连...");
          that.initWebSocket();
          that.lockReconnect = false;
        }, 5000);
      },

    }
  }
</script>

<style lang="less" scoped>
  @import '~@/assets/fonts/fonts/font.css';

  .back {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-origin: content-box;
    z-index: 999;
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
    .title {
      position: absolute;
      top: 0.28rem;
      left: 50%;
      transform: translateX(-50%);
      font-size: 0.8rem;
      line-height: 0.8rem;
      font-family: YouSheBiaoTiHei;
      font-weight: 400;
      color: #eefaff;
      z-index: 1010;
    }
    .head {
      position: relative;
      box-sizing: border-box;
      width: 100%;
      height: 2.05rem;
      padding: 0 3.33rem 0 0;
      z-index: 1003;
      display: flex;
      align-items: flex-end;
      justify-content: flex-end;
      .time {
        position: absolute;
        top: 0.5rem;
        left: 0.6rem;
        z-index: 1003;
        img {
          vertical-align: bottom;
          width: 0.85rem;
          height: 0.85rem;
        }
        .date {
          margin: 0 0 0 0.15rem;
          vertical-align: bottom;
          font-size: 0.53rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #01fafe;
        }
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
      .state-list {
        display: inline-block;
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
            width: 0.85rem;
            height: 0.83rem;
          }
          font-size: 0.35rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #c7dfef;
        }
      }
    }
    .main-box {
      position: relative;
      z-index: 1002;
      box-sizing: border-box;
      width: 100%;
      height: calc(100% - 2.05rem);
      padding: 0 0.22rem 0.15rem 0.22rem;
      overflow: hidden auto;
      .main-col {
        display: inline-block;
        vertical-align: top;
        box-sizing: border-box;
        width: 25%;
        height: 100%;
        padding: 0 0.225rem;
        margin: 0;
      }
    }
  }
</style>
