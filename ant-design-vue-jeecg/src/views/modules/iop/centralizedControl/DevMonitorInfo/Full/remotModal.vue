<template>
  <a-modal
    :visible="visible"
    :width="1200"
    :bodyStyle="bodyStyle"
    @cancel="handleCancel"
    :footer="null"
    :z-index="99999999999"
    cancelText="关闭">
<!--    <IframeView ref="iframeModal" ></IframeView>-->
    <iframe  :id="id" :src="url" frameborder="0" width="100%" height="800px" scrolling="auto"></iframe>
  </a-modal>
</template>

<script>
  import IframeView from "@/components/layouts/IframeView"
  import store from '@/store/'
  export default {
    name: "remotModal",
    components: {
      IframeView
    },
    data () {
      return {
        visible:false,
        url:'',
        id:'',
        bodyStyle:{
          padding: "0",
          height:(window.innerHeight-150)+"px"
        },
      }
    },
    created () {
    },
    mounted() {
      this.initWebSocket();
      this.heartCheckFun();
    },
    methods: {
      show () {
        this.visible=true;
        this.url="http://192.168.110.240:9010/vnc.html";
        this.websocketSend("leftDataUpdate");
        // this.$refs.iframeModal.goUrl("http://www.baidu.com");
      },
      close () {
        this.visible = false;
      },
      handleCancel () {
        this.websocketSend("leftDataUpdate");
        this.close();
        this.$emit('cancelEquip');
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
              self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
              }, self.timeout)
            }, this.timeout)
          }
        }
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
      websocketOnopen: function () {
        console.log("WebSocket连接成功");
        //心跳检测重置
        this.heartCheck.reset().start();
      },
      websocketOnerror: function (e) {
        console.log("WebSocket连接发生错误");
        this.reconnect();
      },
      websocketOnclose: function (e) {
        console.log("connection closed (" + e.code + ")");
        this.reconnect();
      },
      websocketSend(text) { // 数据发送
        try {
          console.log("数据发送---",text)
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
      // goUrl (to) {
      //   console.log("to------",to)
      //   let url = to
      //   let id = this.$route.path
      //   this.id = id
      //   url = "http://www.baidu.com"
      //   // url = "http://192.168.110.186:9000/vnc.html"
      //   console.log("------url------"+url)
      //   if (url !== null && url !== undefined) {
      //     this.url = url;
      //     /*update_begin author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
      //     if(this.$route.meta.internalOrExternal != undefined && this.$route.meta.internalOrExternal==true){
      //       this.closeCurrent();
      //       //外部url加入token
      //       let tokenStr = "${token}";
      //       if(url.indexOf(tokenStr)!=-1){
      //         let token = Vue.ls.get(ACCESS_TOKEN);
      //         this.url = url.replace(tokenStr,token);
      //       }
      //       window.open(this.url);
      //     }
      //     /*update_end author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
      //
      //   }
      // }
    }
  }
</script>