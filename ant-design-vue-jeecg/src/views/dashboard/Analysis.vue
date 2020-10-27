<template>
  <div>
    <a-row type="flex" justify="center" align="top">
        <a-col :sm="24" :md="12" :xl="16" :style="{ marginBottom: '24px' }" >
          <a-card :bordered="true" style="height: 100%">
            <p style="font-size: 30px;">
              集控管理
            </p>
            <a-row :gutter="24">
              <a-col  :sm="12" :md="24" :xl="8" >
                <a-card :bordered="true" style="height: 200px;background: rgb(250 202 20 / 46%)">
                    <a @click="clickFunction" class="a">综合展示</a>
                  <div class="content">
                    <img  src="~@/assets/综合管理.png" >
                  </div>
                </a-card>
              </a-col>
              <a-col  :sm="12" :md="12" :xl="8" >
                <a-card :bordered="true" style="height: 100px;background: #1890ff9c;">
                  <a  @click="clickFunction('/Full/left')" class="a">系统远程控制</a>
                  <div style="vertical-align: middle;margin-left: 35%;width: 30%; margin-top: -5%;">
                    <img src="~@/assets/远程协助.png">
                  </div>
                </a-card>
                <a-row :gutter="16">
                  <a-col :sm="12" :md="24" :xl="12" >
                    <a-card :bordered="true" style="height: 95px;margin-top: 5px;background: #1890ff9c;">
                     <a  @click="clickFunction('/comprehensiveDisplay/faultMonitoring')" class="a">故障报警</a>
                      <div style="vertical-align: middle;margin-left: 55%;width: 30%; margin-top: -5%;">
                        <img src="~@/assets/报警.png">
                      </div>
                    </a-card>
                  </a-col>
                  <a-col :sm="12" :md="24" :xl="12" >
                    <a-card :bordered="true" style="height: 95px;margin-top: 5px;background: #1890ff9c;">
                      <a  @click="clickFunction('/centralized/rightScreen')" class="a">生产任务监控</a>
                      <div style="vertical-align: middle;margin-left: 60%;width: 30%;">
                        <img src="~@/assets/生产任务监控.png">
                      </div>
                    </a-card>
                  </a-col>
                </a-row>

              </a-col>
              <a-col  :sm="12" :md="12" :xl="8" >
                <a-card :bordered="true" style="height: 200px;background: rgb(255 24 24 / 34%);">
                   <a  @click="clickFunction()" class="a">三维仿真</a>
                  <div style="vertical-align: middle;margin-left: 35%;width: 30%; margin-top: 8%;">
                    <img src="~@/assets/三维分析.png">
                  </div>
                </a-card>
              </a-col>
            </a-row>
          </a-card>
          <a-col :sm="24" :md="12" :xl="24" :style="{ marginBottom: '24px' }" >
              <a-row :gutter="2">
                <a-col  :sm="12" :md="24" :xl="8" >
                  <a-card :bordered="true">
                    <p style="font-size: 30px;">
                      故障管理
                    </p>
                    <a-col :sm="12" :md="24" :xl="16">
                      <a-card :bordered="true" style="height: 150px;background:rgb(24 93 255 / 37%);">
                         <a  @click="clickFunction('/workOrder/ordWorkOrderInfo')" class="a">工单管理</a>
                        <div style="vertical-align: middle;margin-left: 35%;width: 30%; margin-top: 8%;">
                          <img src="~@/assets/工单管理.png">
                        </div>
                      </a-card>
                    </a-col>
                    <a-col :sm="12" :md="24" :xl="8">
                      <a-card :bordered="true" style="height: 150px;background:rgb(24 200 255 / 64%);">
                        <a  @click="clickFunction('/alarm/record/mtEquipAlarmInfo')" class="a">报警记录</a>
                        <div style="vertical-align: middle;margin-left: 35%;width: 30%; margin-top: 8%;">
                          <img src="~@/assets/报警记录.png">
                        </div>
                      </a-card>
                    </a-col>
                  </a-card>
                </a-col>
                <a-col :sm="12" :md="12" :xl="16" >
                  <a-card :bordered="true">
                    <p style="font-size: 30px;">
                      备品备件
                    </p>
                    <a-row :gutter="6" >
                      <a-col  :sm="12" :md="24" :xl="8" >
                        <a-card :bordered="true" style="height: 150px;background:rgb(179 24 255 / 28%);">
                          <a  @click="clickFunction('/outOrder/stockOutOrder')" class="a">领用管理</a>
                          <div style="vertical-align: middle;margin-left: 35%;width: 30%; margin-top: 8%;">
                            <img src="~@/assets/领用.png">
                          </div>
                        </a-card>
                      </a-col>
                      <a-col  :sm="12" :md="12" :xl="8" >
                        <a-card :bordered="true" style="height: 75px;background:rgb(24 50 255 / 64%);">
                          <a  @click="clickFunction('/iop/order/orderPlan')" class="a">采购管理</a>
                          <div style="vertical-align: middle;margin-left: 50%;width: 30%; margin-top: -20%;">
                            <img src="~@/assets/采购管理.png">
                          </div>
                        </a-card>
                        <a-card :bordered="true" style="height: 70px; margin-top: 5px;background:rgb(24 255 214 / 65%);">
                           <a  @click="clickFunction('/stk/in/stockInOrder')" class="a">采购入库</a>
                          <div style="vertical-align: middle;margin-left: 50%;width: 30%; margin-top: -20%;">
                            <img src="~@/assets/采购入库.png">
                          </div>
                        </a-card>

                      </a-col>
                      <a-col  :sm="12" :md="12" :xl="8" >
                        <a-card :bordered="true" style="height: 150px;background:#00646673;">
                           <a  @click="clickFunction('')" class="a">基础信息管理</a>
                          <div style="vertical-align: middle;margin-left: 40%;width: 30%; margin-top: 5%;">
                            <img src="~@/assets/基础信息.png">
                          </div>
                        </a-card>
                      </a-col>
                    </a-row>
                  </a-card>
                </a-col>
              </a-row>
          </a-col>
        </a-col>


      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '24px' }" >
          <a-card :bordered="true" >
            <p style="font-size: 30px;">
              第三方系统
            </p>
             <a-row :gutter="24">
            <a-col  :sm="12" :md="24" :xl="12" >
              <a-card :bordered="true" style="height: 475px;background:#068b88ba;">
                <p style="font-size: 20px;" class="a">
                  MDS生产调度系统
                </p>
                <div style="vertical-align: middle;margin-left: 33%;width: 30%; margin-top: 45%;">
                  <img src="~@/assets/调度.png">
                </div>
              </a-card>
            </a-col>
            <a-col  :sm="12" :md="24" :xl="12" >
              <a-card :bordered="true" style="height: 155px;background:#52c41a59;">
                <p style="font-size: 20px;" class="a">
                  配送系统
                </p>
                <div style="vertical-align: middle;margin-left: 33%;width: 30%;">
                  <img src="~@/assets/配送.png">
                </div>
              </a-card>
              <a-card :bordered="true" style="height: 155px;margin-top: 5px;background:rgba(38,196,169,0.53);">
                <p style="font-size: 20px;" class="a">
                  仓储系统
                </p>
                <div style="vertical-align: middle;margin-left: 33%;width: 30%;">
                  <img src="~@/assets/仓储.png">
                </div>
              </a-card>
              <a-card :bordered="true" style="height: 155px;margin-top: 5px;background:rgba(196,174,47,0.35);">
                <p style="font-size: 20px;" class="a">
                  其他系统
                </p>
                <div style="vertical-align: middle;margin-left: 33%;width: 30%;">
                  <img src="~@/assets/系统.png">
                </div>
              </a-card>
            </a-col>
          </a-row>
          </a-card>
        </a-col>
    </a-row>
  </div>
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'
  import ARow from "ant-design-vue/es/grid/Row";
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import ACol from "ant-design-vue/es/grid/Col";
  import ImgTurnPage from "../jeecg/ImgTurnPage";
//  import PageLayout from '../page/PageLayout'
//  import RouteView from './RouteView'
//  import RouteView from '@/components/layouts/RouteView'

  export default {
    name: "Analysis",
    components: {
      ImgTurnPage,
      ACol, ARow, IndexChart,
      IndexTask,
      IndexBdc
    },
    data() {
      return {
        indexStyle:1
      }
    },
    created() {

    },
    methods: {
      clickFunction(url){
//        let url='/faultType/faultTaskInfo';
        console.log("url----",url)
        this.$router.push({ path: url })
        if (url !== null && url !== undefined) {
//          this.url = url;
//          this.$route.meta.internalOrExternal=true;
//          /*update_begin author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
//          if(this.$route.meta.internalOrExternal != undefined && this.$route.meta.internalOrExternal==true){
//            console.log("------2------"+2)
////            this.closeCurrent();
//            //外部url加入token
//            let tokenStr = "${token}";
//            if(url.indexOf(tokenStr)!=-1){
//              let token = Vue.ls.get(ACCESS_TOKEN);
//              this.url = url.replace(tokenStr,token);
//            }
//            console.log("------3------"+this.url)
//            window.open(this.url);
//          }else {
//            window.open(this.url);
//          }
//          let route = this.searchMenuOptions.filter(item => item.id === value)[0]

          /*update_end author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */

        }
      }
    }
  }
</script>
<style>
  .content{
    vertical-align: middle;
    margin-left: 35%;
    width: 30%;
    margin-top: 10%;
  }
  .a{
    color: #fafafa;
  }
</style>