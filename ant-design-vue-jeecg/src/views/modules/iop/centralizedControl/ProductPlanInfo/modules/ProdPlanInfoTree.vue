<template>
  <a-card :bordered="false">
    <a-row :gutter="12">
      <a-col :span="4">
        <div class="area-equip-tree">
          <a-dropdown :trigger="[this.dropTrigger]" @visibleChange="dropStatus">
            <a-tree :tree-data="treeData"
                    @rightClick="rightHandle"
                    :replaceFields="replaceFields"
                    :expandedKeys.sync="expandedKeys"
                    @select="selectNode">
            </a-tree>
            <!--新增右键点击事件,和增加添加和删除功能-->

            <a-menu slot="overlay">
              <a-menu-item v-if="state == '2' || state == '1'" @click="handleAdd" key="1">添加{{name}}</a-menu-item>
              <a-menu-item v-if="state == '2' || state == '3'" @click="handleDelete" key="2">删除{{name2}}</a-menu-item>
              <a-menu-item @click="closeDrop" key="3">取消</a-menu-item>
            </a-menu>

          </a-dropdown>
          <ProdPlanInfoModel ref="addoreditForm" @ok="modalFormOk"></ProdPlanInfoModel>
          <DelProdPlanInfoModal ref="delForm" @ok="modalFormOk"></DelProdPlanInfoModal>
        </div>
      </a-col>
      <a-col :span="20">
        <SimplexList ref="simpleList" @loadDateAdd="loadAdd" @ok="modalFormOk" v-show="visible"></SimplexList>
        <AddModel ref="addModelForm" @ok="modalFormOk"></AddModel>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
  import { getAction } from '@/api/manage'
  import { translateDataToTree } from '@/views/modules/iop/stk/in/util/tree'
  import ProdPlanInfoModel from "./ProdPlanInfoModel";
  import DelProdPlanInfoModal from "./DelProdPlanInfoModal";
  import SimplexList from "../SimplexList";
  import AddModel from "./AddModel";
  export default {
    name: 'ProdPlanInfoTree',
    components: {
      ProdPlanInfoModel,
      DelProdPlanInfoModal,
      SimplexList,
      AddModel
    },
    data() {
      return {
        id:"",
        pid:"",
        //删除状态判断
        state:"1",
        status:"",
        //标识
        name:"",
        //标识
        name2:"",
        dropTrigger: '',
        treeData: [],
        visible:true,
        disTransData: [],
        expandedKeys: [],
        replaceFields: {
          title: 'title', key: 'id'
        },
        searchVal: '',
        url: {
          treeList: '/prod/devProduceCycle/menu'
        }
      }
    },
    created() {
      this.get();
    },
    methods: {
      loadAdd(){
        this.get();
      },
      get(){
        getAction(this.url.treeList).then(res => {
          this.disTransData = this._.cloneDeep(res.result)
          this.treeData = this._.cloneDeep(translateDataToTree(res.result))
        })
      },
      handleEdit(){
        this.$refs.addoreditForm.show(this.id);
      },
      handleDelete() {
        this.$refs.delForm.edit(this.pid);
      },
      // 右键下拉关闭下拉框
      closeDrop() {
        this.dropTrigger = ''
      },
      modalFormOk(){
        getAction(this.url.treeList).then(res => {
          this.disTransData = this._.cloneDeep(res.result)
          this.treeData = this._.cloneDeep(translateDataToTree(res.result))
        })
        //this.searchQuery();
        //this.onClearSelected();
      },
      handleAdd() {
        this.$refs.addModelForm.add(this.pid,this.status);
      },
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },
      rightHandle(node) {
        this.dropTrigger = ''
        this.pid = node.node.dataRef.id;
        console.log(node.node.dataRef);
        if (node.node.dataRef.isYear == "1"){
          this.name = "年份";
          this.dropTrigger = 'contextmenu'
          this.pid = node.node.dataRef.id
          this.status = "1";
          this.state = "1";
        }
        if (node.node.dataRef.isMonth == "1"){
          this.name = "月份";
          this.status = "2";
          this.state = '2';
          this.name2 = "年份"
          this.dropTrigger = 'contextmenu'
          this.pid = node.node.dataRef.id
        }
        if (node.node.dataRef.isMonth == "0" && node.node.dataRef.isYear == "0"){
          this.state = '3';
          this.name2 = "月份"
          this.dropTrigger = 'contextmenu'
          this.pid = node.node.dataRef.id
        }
        //console.log(this.id)
        console.log(this.pid)
      },
      searchExpandedKeys(val) {
        if ('' == val) {
          this.treeData = translateDataToTree(this._.cloneDeep(this.disTransData))
          return false
        }
        let disTransData = this._.cloneDeep(this.disTransData)
        let res = this._.filter(disTransData, o => {
          if (o['isEnd'] == '1' && o['title'].indexOf(val) != -1) {
            return true
          }
        })
        let keyObj = this._.keyBy(disTransData, 'id')
        for (let i = 0; i < res.length; i++) {
          let currentObj = res[i]
          let pid = currentObj['pid']
          while (pid != '0') {
            currentObj = keyObj[pid]
            res.push(currentObj)
            pid = currentObj['pid']
          }
        }
        let temp = this._.uniqBy(res, 'id')
        this.treeData = translateDataToTree(temp)
      },
      selectNode(selectedKeys, e) {
        let obj = e.node.dataRef
        if (obj) {
          if (obj['isEnd'] == '1') {
            //this.$emit('select', obj['id'], obj)
            if (obj['id'].indexOf('_D') != -1){
              this.$refs.simpleList.show1(obj.pid,'D');
            }else if (obj['id'].indexOf('_S') != -1){
              this.$refs.simpleList.show1(obj.pid,'S');
            }else if (obj['id'].indexOf('_H') != -1){
              this.$refs.simpleList.show1(obj.pid,'H');
            }else if (obj['id'].indexOf('_C') != -1){
              this.$refs.simpleList.show1(obj.pid,'C');
            }
            console.log(obj.pid)
            this.visible = true;
          }
        }
      }
    },
    watch: {
      searchVal(val) {
        this.searchExpandedKeys(val)
      }
    }
  }
</script>

<style scoped>
  .area-equip-tree {
    height: 740px;
    overflow-x: hidden;
    overflow-y: scroll;
  }
</style>