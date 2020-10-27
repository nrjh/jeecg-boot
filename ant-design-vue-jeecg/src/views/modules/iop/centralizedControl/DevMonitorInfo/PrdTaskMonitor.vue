<template>
  <!--  https://blog.csdn.net/yshusencsdn/article/details/102718007-->
  <a-layout id="components-layout-demo-fixed">
    <a-layout-header :style="{zIndex: 1, width: '100%',background:'white' }">
      <a-menu
        theme="light"
        mode="horizontal"
        :default-selected-keys="defaultKey"
        :style="{ lineHeight: '64px' }"
      >
        <a-menu-item key="1" @click="toggleTab('mtDetectTaskInfoList')">
          生产任务监控
          <span v-if="this.mtDetectTaskInfoCount > 0" :style="{textAlign:'center',color:'white',padding: '2px 5px',background:'red',borderRadius:'5px'}">
              {{this.mtDetectTaskInfoCount}}
          </span>
        </a-menu-item>
        <a-menu-item   key="2" @click="toggleTab('dIoTaskList')">
          出入库任务监控
          <span v-if="this.ioStockCount > 0" :style="{textAlign:'center',color:'white',padding: '2px 5px',background:'red',borderRadius:'5px'}">
              {{this.ioStockCount}}
          </span>
        </a-menu-item>
        <a-menu-item key="3" @click="toggleTab('checkDetectTaskList')">
          核对异常检定任务
          <span v-if="this.exceptionCount > 0" :style="{textAlign:'center',color:'white',padding: '2px 5px',background:'red',borderRadius:'5px'}">
              {{this.exceptionCount}}
          </span>
        </a-menu-item>
        <a-menu-item key="4" @click="toggleTab('uDistTaskList')">
          配送任务监控
          <span v-if="this.taskMonitorCount" :style="{textAlign:'center',color:'white',padding: '2px 5px',background:'red',borderRadius:'5px'}">
              {{this.taskMonitorCount}}
          </span>
        </a-menu-item>
      </a-menu>
    </a-layout-header>

    <a-layout-content>
      <component v-bind:is="cut"></component>
    </a-layout-content>

  </a-layout>
</template>

<script>
    import uDistTaskList from './modules/UDistTaskList.vue'
    import mtDetectTaskInfoList from './modules/MtDetectTaskInfoList'
    import dIoTaskList from './modules/DIoTaskList'
    import checkDetectTaskList from './modules/CheckDetectTaskList'
    import { getAction } from '@/api/manage'

    export default {
        name:'PrdTaskMonitor',
        components:{
            uDistTaskList,
            mtDetectTaskInfoList,
            dIoTaskList,
            checkDetectTaskList
        },
        data(){
            return {
                content:"",
                cut:'mtDetectTaskInfoList',
                defaultKey:['1'],
                url:{
                    getCount:"/ptm/mtDetectTaskInfo/getCount"
                },
                exceptionCount:0,
                mtDetectTaskInfoCount:0,
                ioStockCount:0,
                taskMonitorCount:0
            }
        },
        methods:{
            toggleTab(tab){
                this.cut = tab
            }
        },
        created:function() {
            getAction(this.url.getCount).then((res) => {
                this.exceptionCount = res.result.exceptionCount
                this.mtDetectTaskInfoCount = res.result.mtDetectTaskInfoCount
                this.ioStockCount = res.result.ioStockCount
                this.taskMonitorCount = res.result.taskMonitorCount
            })
        }
    }
</script>
<style>
  /*display: inline-block;*/
  /*float: right;*/
  /*height: size(20);*/
  /*width: size(20);*/
  /*background-color: #F64D32;*/
  /*margin-right: size(5);*/
  /*margin-top: size(13);*/
  /*border-radius: 50%;*/

  /*[v-cloak] {*/
  /*  display: none;*/
  /*}*/
</style>