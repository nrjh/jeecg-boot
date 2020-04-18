<template>
  <a-list>
    <a-list-item :key="index" v-for="(record, index) in mssageList">
      <div style="margin-left: 5%;width: 80%">
        <p><a @click="showMessage(record)">{{ record.titile }}</a></p>
        <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
      </div>
      <div style="text-align: right">
        <a-tag @click="showMessage(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
        <a-tag @click="showMessage(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
        <a-tag @click="showMessage(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
      </div>
    </a-list-item>
  </a-list>
</template>

<script>  
  import store from '@/store/'

  export default {
    name: 'websocket',
    data() {
      return {
         messagaUrl : "",
         mssageList:[],
         msgCount:0,
         msgTitle:""
      }
    },
    mounted() {
      //初始化websocket
      this.initWebSocket()
      // 心跳测试
      this.heartCheckFun()
    },
    destroyed: function () { // 离开页面生命周期函数
      this.websocketclose();
    },
    methods: {
      loadData (){
        try {
          // 获取系统消息
          getAction(this.messagaUrl).then((res) => {
            if (res.success) {
              this.messageList = res.result.messageList;
              this.msgCount = res.result.messageTotal;
              this.msgTitle = "通知(" + res.result.messageTotal + ")";             
            }
          }).catch(error => {
            console.log("系统消息通知异常",error);//这行打印permissionName is undefined
            this.stopTimer = true;
            console.log("清理timer");
          });
        } catch (err) {
          this.stopTimer = true;
          console.log("通知异常",err);
        }
      },
      showMessage:function(){
         // TODO
      },
      initWebSocket: function () {
        // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
        var userId = store.getters.userInfo.id;
        var url = window._CONFIG['domianURL'].replace("https://","ws://").replace("http://","ws://")+"/iop/websocket/"+userId;
        this.websock = new WebSocket(url);
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
      },
      websocketonopen: function () {
        console.log("WebSocket连接成功");
      },
      websocketonerror: function (e) {
        console.log("WebSocket连接发生错误");
      },
      websocketonmessage: function (e) {
        var data = eval("(" + e.data + ")");
        //处理订阅信息
        if(data.cmd == "topic"){
          //TODO 系统通知
          this.loadData();
        }else if(data.cmd == "user"){
          //TODO 用户消息
          this.loadData();
        }
        //心跳检测重置
        this.heartCheck.reset().start();
      },
      websocketclose: function (e) {
        console.log("connection closed (" + e.code + ")");
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
              that.websocketSend("HeartBeat");
              console.info("客户端发送心跳");
              //self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
              //  that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
              //}, self.timeout)
            }, this.timeout)
          }
        }
      },
    }
  }
</script>

<style scoped>

</style>