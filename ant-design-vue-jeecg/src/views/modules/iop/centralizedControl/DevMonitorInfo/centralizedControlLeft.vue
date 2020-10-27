<template>
  <div class="card">
  <img src="~@/assets/left1.png">
  </div>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction} from '@/api/manage'
  import store from '@/store/'


  export default {
    name: "Advanced",
    components: {
    },
    mixins: [JeecgListMixin],
    data () {
      return {
        listSimplexOne:[],
        listSimplexTwo:[],
        listCollectionOne:[],
        listCollectionTwo:[],
        listThreephaseOne:[],
        listThreephaseTwo:[],
        listTransformer:[],
        activeTabKey: '1',
        stopTimer:false,
        websock: null,
        lockReconnect:false,
        heartCheck:null,
        url:{
          list: "/DevMonitorInfo/devMonitorInfo/queryDevList",
        },
        nodes: [
        ],
      }
    },
    filters: {
    },
    mounted() {
      this.loadData();
      //this.timerFun();
      this.initWebSocket();
      this.heartCheckFun();
    },
    destroyed: function () { // 离开页面生命周期函数
      this.websocketclose();
    },
    methods:{
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
        })
      },
      handelFault(i,value){
        console.log("this.$router----",this.$router)
        console.log("value---------",value)
         // this.$router.push({path:'http://192.168.110.186:9000/vnc.html'})
        // .push({ name: "dashboard" })
        // this.websocketSend(value);
        // window.open('http://192.168.110.186:9000/vnc.html', '_blank')
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
              // that.websocketSend("HeartBeat");
              console.info("客户端发送心跳11");
              // that.loadData();
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
        if(data.cmd == "topic"){
          //系统通知
          this.loadData();
        }else if(data.cmd == "user"){
          //用户消息
          this.loadData();
        }
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

<style>
  #components-layout-demo-responsive {
    height: 100vh;
  }

  .card .card-cal {
    height: 100px;
  }

  .card {
    border: 1px solid #BDBDBD;
    margin: 10px;
    border-radius: 4px;
    box-shadow: 0px 6px 12px 0 #d9d9d9;
  }
</style>