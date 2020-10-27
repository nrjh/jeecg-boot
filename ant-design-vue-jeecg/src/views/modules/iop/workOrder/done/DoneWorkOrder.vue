<template>
  <!--  https://blog.csdn.net/yshusencsdn/article/details/102718007-->
  <a-layout id="components-layout-demo-fixed">
    <a-layout-header :style="{zIndex: 1, width: '100%',background:'white' }">
      <a-menu
        theme="light"
        mode="horizontal"
        :default-selected-keys="defaultKey"
      >
        <a-menu-item key="1" @click="toggleTab('unfileOrdWorkOrderInfoList')">
          待归档
        </a-menu-item>
        <a-menu-item key="2" @click="toggleTab('hadSetterOrdWorkOrder')">
          已结单
        </a-menu-item>
      </a-menu>
    </a-layout-header>

    <a-layout-content>
      <component v-bind:is="cut"></component>
    </a-layout-content>

  </a-layout>
</template>

<script>
    import hadSetterOrdWorkOrder from './HadSetterOrdWorkOrder'
    import unfileOrdWorkOrderInfoList from './UnfileOrdWorkOrderInfoList'

    export default {
        name:'DoneWorkOrder',
        components:{
            hadSetterOrdWorkOrder,
            unfileOrdWorkOrderInfoList
        },
        data(){
            return {
                content:"",
                cut:'unfileOrdWorkOrderInfoList',
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