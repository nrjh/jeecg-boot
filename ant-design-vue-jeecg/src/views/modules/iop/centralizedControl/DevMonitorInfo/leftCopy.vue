<!--<template>-->
<!--  <div>-->
<!--    <a-card  style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/2.jpg" >-->
<!--        &lt;!&ndash;      单相线一区&ndash;&gt;-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper" >-->
<!--        <div class="sc-time-line">-->
<!--          <div @click="handelFault(index,node.equipNo)" class="node" v-for="(node,index) in listSimplexOne" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--    <a-card  style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/3.jpg" >-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper" >-->
<!--        <div class="sc-time-line">-->
<!--          <div class="node" @click="handelFault(index,node.equipNo)"  v-for="(node,index) in listSimplexTwo" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--    <a-card  style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/4.jpg" >-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper">-->
<!--        <div class="sc-time-line">-->
<!--          <div class="node" @click="handelFault(index,node.equipNo)"  v-for="(node,index) in listThreephaseOne" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--    <a-card  style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/5.jpg" >-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper">-->
<!--        <div class="sc-time-line">-->
<!--          <div class="node" @click="handelFault(index,node.equipNo)"  v-for="(node,index) in listThreephaseTwo" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--    <a-card style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/6.jpg" >-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper">-->
<!--        <div class="sc-time-line">-->
<!--          <div class="node" @click="handelFault(index,node.equipNo)" v-for="(node,index) in listCollectionOne" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--    <a-card style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/7.jpg" >-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper">-->
<!--        <div class="sc-time-line">-->
<!--          <div class="node" @click="handelFault(index,node.equipNo)" v-for="(node,index) in listCollectionTwo" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--    <a-card  style="width: 100%; font-size:1px;" :bordered="false" >-->
<!--      <div style="float: left">-->
<!--        <img  src="~@/assets/8.jpg" >-->
<!--      </div>-->
<!--      <div class="sc-time-line-wrapper">-->
<!--        <div class="sc-time-line">-->
<!--          <div class="node" @click="handelFault(index,node.equipNo)" v-for="(node,index) in listTransformer" :key="node.equipNo" :class="`node-direction__${node.direction} node-direction__${node.isHandled}`" >-->
<!--            {{ node.equipName }}-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </a-card>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--  import { JeecgListMixin } from '@/mixins/JeecgListMixin'-->
<!--  import { getAction} from '@/api/manage'-->
<!--  import store from '@/store/'-->


<!--  export default {-->
<!--    name: "Advanced",-->
<!--    components: {-->
<!--    },-->
<!--    mixins: [JeecgListMixin],-->
<!--    data () {-->
<!--      return {-->
<!--        listSimplexOne:[],-->
<!--        listSimplexTwo:[],-->
<!--        listCollectionOne:[],-->
<!--        listCollectionTwo:[],-->
<!--        listThreephaseOne:[],-->
<!--        listThreephaseTwo:[],-->
<!--        listTransformer:[],-->
<!--        activeTabKey: '1',-->
<!--        stopTimer:false,-->
<!--        websock: null,-->
<!--        lockReconnect:false,-->
<!--        heartCheck:null,-->
<!--        url:{-->
<!--          list: "/DevMonitorInfo/devMonitorInfo/queryDevList",-->
<!--        },-->
<!--        nodes: [-->
<!--        ],-->
<!--      }-->
<!--    },-->
<!--    filters: {-->
<!--    },-->
<!--    mounted() {-->
<!--      this.loadData();-->
<!--      //this.timerFun();-->
<!--      this.initWebSocket();-->
<!--      this.heartCheckFun();-->
<!--    },-->
<!--    destroyed: function () { // 离开页面生命周期函数-->
<!--      this.websocketclose();-->
<!--    },-->
<!--    methods:{-->
<!--      loadData(){-->
<!--        getAction(this.url.list).then((res) => {-->
<!--          console.log("res.result",res)-->
<!--          this.listSimplexOne=res.result.listSimplexOne;-->
<!--          this.listSimplexTwo=res.result.listSimplexTwo;-->
<!--          this.listCollectionOne=res.result.listCollectionOne;-->
<!--          this.listCollectionTwo=res.result.listCollectionTwo;-->
<!--          this.listThreephaseOne=res.result.listThreephaseOne;-->
<!--          this.listThreephaseTwo=res.result.listThreephaseTwo;-->
<!--          this.listTransformer=res.result.listTransformer;-->
<!--        })-->
<!--      },-->
<!--      handelFault(i,value){-->
<!--        console.log("this.$router&#45;&#45;&#45;&#45;",this.$router)-->
<!--        console.log("value-&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;",value)-->
<!--        // this.$router.push({path:'http://192.168.110.186:9000/vnc.html'})-->
<!--        // .push({ name: "dashboard" })-->
<!--        // this.websocketSend(value);-->
<!--        // window.open('http://192.168.110.186:9000/vnc.html', '_blank')-->
<!--      },-->
<!--      initWebSocket: function () {-->
<!--        // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https-->
<!--        var userId = store.getters.userInfo.id;-->
<!--        var url = window._CONFIG['domianURL'].replace("https://","wss://").replace("http://","ws://")+"/websocket/"+userId;-->
<!--        console.log("url&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;&#45;",url);-->
<!--        this.websock = new WebSocket(url);-->
<!--        this.websock.onopen = this.websocketOnopen;-->
<!--        this.websock.onerror = this.websocketOnerror;-->
<!--        this.websock.onmessage = this.websocketOnmessage;-->
<!--        this.websock.onclose = this.websocketOnclose;-->
<!--      },-->
<!--      heartCheckFun(){-->
<!--        var that = this;-->
<!--        //心跳检测,每20s心跳一次-->
<!--        that.heartCheck = {-->
<!--          timeout: 20000,-->
<!--          timeoutObj: null,-->
<!--          serverTimeoutObj: null,-->
<!--          reset: function(){-->
<!--            clearTimeout(this.timeoutObj);-->
<!--            //clearTimeout(this.serverTimeoutObj);-->
<!--            return this;-->
<!--          },-->
<!--          start: function(){-->
<!--            var self = this;-->
<!--            this.timeoutObj = setTimeout(function(){-->
<!--              //这里发送一个心跳，后端收到后，返回一个心跳消息，-->
<!--              //onmessage拿到返回的心跳就说明连接正常-->
<!--              // that.websocketSend("HeartBeat");-->
<!--              console.info("客户端发送心跳11");-->
<!--              // that.loadData();-->
<!--              // console.info("客户端发送心跳");-->
<!--              self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了-->
<!--                that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次-->
<!--              }, self.timeout)-->
<!--            }, this.timeout)-->
<!--          }-->
<!--        }-->
<!--      },-->
<!--      websocketOnopen: function () {-->
<!--        console.log("WebSocket连接成功");-->
<!--        //心跳检测重置-->
<!--        this.heartCheck.reset().start();-->
<!--      },-->
<!--      websocketOnerror: function (e) {-->
<!--        console.log("WebSocket连接发生错误");-->
<!--        this.reconnect();-->
<!--      },-->
<!--      websocketOnmessage: function (e) {-->
<!--        console.log("-&#45;&#45;&#45;&#45;接收消息-&#45;&#45;&#45;&#45;&#45;&#45;",e.data);-->
<!--        var data = eval("(" + e.data + ")"); //解析对象-->
<!--        if(data.cmd == "topic"){-->
<!--          //系统通知-->
<!--          this.loadData();-->
<!--        }else if(data.cmd == "user"){-->
<!--          //用户消息-->
<!--          this.loadData();-->
<!--        }-->
<!--        //心跳检测重置-->
<!--        this.heartCheck.reset().start();-->
<!--      },-->
<!--      websocketOnclose: function (e) {-->
<!--        console.log("connection closed (" + e.code + ")");-->
<!--        this.reconnect();-->
<!--      },-->
<!--      websocketSend(text) { // 数据发送-->
<!--        try {-->
<!--          this.websock.send(text);-->
<!--        } catch (err) {-->
<!--          console.log("send failed (" + err.code + ")");-->
<!--        }-->
<!--      },-->
<!--      reconnect() {-->
<!--        var that = this;-->
<!--        if(that.lockReconnect) return;-->
<!--        that.lockReconnect = true;-->
<!--        //没连接上会一直重连，设置延迟避免请求过多-->
<!--        setTimeout(function () {-->
<!--          console.info("尝试重连...");-->
<!--          that.initWebSocket();-->
<!--          that.lockReconnect = false;-->
<!--        }, 5000);-->
<!--      },-->
<!--    }-->
<!--  }-->
<!--</script>-->

<!--<style>-->
<!--  .sc-time-line-wrapper {-->
<!--    overflow-x: auto;-->
<!--  }-->
<!--  .sc-time-line {-->
<!--    height: 120px;-->
<!--    display: flex;-->
<!--    flex-direction: row;-->
<!--  }-->

<!--  .sc-time-line .node {-->
<!--    height: 30px;-->
<!--    width: 90px;-->
<!--    position: relative;-->
<!--    line-height: 32px;-->

<!--    border: 1px solid teal;-->
<!--    border-radius: 7px;-->
<!--    margin: 0 4px;-->
<!--    cursor: pointer;-->
<!--  }-->

<!--  .sc-time-line .node.node-direction__down {-->
<!--    top: 48px;-->
<!--  }-->
<!--  .sc-time-line .node.node-direction__1 {-->
<!--    background: #52c41a;-->
<!--  }-->
<!--  .sc-time-line .node.node-direction__0 {-->
<!--    background: #cf1322;-->
<!--  }-->

<!--  .sc-time-line:before {-->
<!--    content: "";-->
<!--    position: absolute;-->
<!--    width: 100%;-->
<!--    height: 2px;-->
<!--    background: teal;-->
<!--    top: 60px;-->
<!--  }-->

<!--  .sc-time-line .node.node-direction__down:before {-->
<!--    content: "";-->
<!--    position: absolute;-->
<!--    left: 50%;-->
<!--    height: 12px;-->
<!--    width: 2px;-->
<!--    background: teal;-->
<!--    top: -12px;-->
<!--  }-->

<!--  .sc-time-line .node.node-direction__up:before {-->
<!--    content: "";-->
<!--    position: absolute;-->
<!--    left: 50%;-->
<!--    height: 12px;-->
<!--    width: 2px;-->
<!--    background: teal;-->
<!--    bottom: -12px;-->
<!--  }-->

<!--  .sc-time-line .node.node-direction__down:hover:after {-->
<!--    position: absolute;-->
<!--    left: calc(50% - 4px);-->
<!--    top: -16px;-->
<!--    width: 9px;-->
<!--    border-radius: 150%;-->
<!--    height: 9px;-->
<!--    background: teal;-->
<!--    content: "";-->
<!--  }-->

<!--  .sc-time-line .node.node-direction__up:hover:after {-->
<!--    position: absolute;-->
<!--    left: calc(50% - 3px);-->
<!--    bottom: -17px;-->
<!--    width: 9px;-->
<!--    border-radius: 150%;-->
<!--    height: 9px;-->
<!--    background: teal;-->
<!--    content: "";-->
<!--  }-->
<!--</style>-->