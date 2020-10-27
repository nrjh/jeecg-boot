<template>

    <iframe  :id="id" :src="url" frameborder="0" width="100%" height="800px" scrolling="auto"></iframe>

</template>

<script>
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import PageLayout from '../page/PageLayout'
  import RouteView from './RouteView'

  export default {
    name: "IframeView",
    inject:['closeCurrent'],
    data () {
      return {
        url: "",
        id:""
      }
    },
    // created () {
    //   this.goUrl()
    // },
    // updated () {
    //   this.goUrl()
    // },
    // watch: {
    //   $route(to, from) {
    //     this.goUrl(to);
    //   }
    // },
    methods: {
      goUrl (to) {
        console.log("to------",to)
        let url = to
        let id = this.$route.path
        this.id = id
        url = "http://www.baidu.com"
        // url = "http://192.168.110.186:9000/vnc.html"
        console.log("------url------"+url)
        if (url !== null && url !== undefined) {
          this.url = url;
          /*update_begin author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */
          if(this.$route.meta.internalOrExternal != undefined && this.$route.meta.internalOrExternal==true){
            this.closeCurrent();
            //外部url加入token
            let tokenStr = "${token}";
            if(url.indexOf(tokenStr)!=-1){
              let token = Vue.ls.get(ACCESS_TOKEN);
               this.url = url.replace(tokenStr,token);
            }
            window.open(this.url);
          }
          /*update_end author:wuxianquan date:20190908 for:判断打开方式，新窗口打开时this.$route.meta.internalOrExternal==true */

        }
      }
    }
  }
</script>

<style>
</style>