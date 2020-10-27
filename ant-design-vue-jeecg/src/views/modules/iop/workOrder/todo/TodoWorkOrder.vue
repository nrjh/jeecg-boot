<template>
  <!--  https://blog.csdn.net/yshusencsdn/article/details/102718007-->
  <a-layout id="components-layout-demo-fixed">
    <a-layout-header :style="{zIndex: 1, width: '100%',background:'white' }">
      <a-menu
        theme="light"
        mode="horizontal"
        :default-selected-keys="defaultKey"
      >
        <a-menu-item key="1" @click="toggleTab('ordWorkOrderInfoList')">
          待派工
        </a-menu-item>
        <a-menu-item key="2" @click="toggleTab('ordWorkOrderFeedBackList')">
          待反馈
        </a-menu-item>
      </a-menu>
    </a-layout-header>

    <a-layout-content>
      <component v-bind:is="cut"></component>
    </a-layout-content>

  </a-layout>
</template>

<script>
    import ordWorkOrderFeedBackList from './OrdWorkOrderFeedBackList'
    import ordWorkOrderInfoList from './OrdWorkOrderInfoList'

    export default {
        name:'TodoWorkOrder',
        components:{
            ordWorkOrderFeedBackList,
            ordWorkOrderInfoList
        },
        data(){
            return {
                content:"",
                cut:'ordWorkOrderInfoList',
                defaultKey:['1']
            }
        },
        methods:{
            toggleTab(tab){
                this.cut = tab
            }
        },
        created:function() {
        }
    }
</script>
<style>

</style>