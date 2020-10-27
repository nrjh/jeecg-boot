<template>
  <div class="area-equip-tree">
    <a-dropdown :trigger="[this.dropTrigger]" @visibleChange="dropStatus">
      <a-tree :tree-data="treeData"
              :replaceFields="replaceFields"
              @rightClick="rightHandle"
              :expandedKeys.sync="expandedKeys"
              @select="selectNode">
      </a-tree>
      <!--新增右键点击事件,和增加添加和删除功能-->

      <a-menu slot="overlay">
        <a-menu-item v-if="isEquip == 1" @click="AddOrEdit()" key="1">添加部件</a-menu-item>
        <a-menu-item v-if="isEquip == 2" @click="deletePart()" key="2">删除部件</a-menu-item>
        <a-menu-item v-if="isEquip == 2" @click="AddOrEdit()" key="3">修改部件</a-menu-item>
        <a-menu-item v-if="isEquip == 2 || isEquip == 1" @click="closeDrop()" key="4">取消</a-menu-item>
      </a-menu>
    </a-dropdown>

  <a-modal
        title=""
        :visible="visibleModel"
        @ok="handleOkModel"
        @cancel="handleCancelModel">
        <p>是否删除此部件？</p>
      </a-modal>
  </div>
</template>

<script>
  import { getAction,httpAction } from '@/api/manage'
  import { translateDataToTree } from '@/views/modules/iop/stk/in/util/tree'

  export default {
    name: 'EquipPartsTree',
    data() {
      return {
        visibleModel:false,
        dropTrigger: '',
        isEquip:"1",
        parentId:"",
        id:"",
        treeData: [],
        disTransData: [],
        expandedKeys: [],
        replaceFields: {
          title: 'title', key: 'id'
        },
        url: {
          treeList: '/parts/equipParts/tree',
          del:"/parts/equipParts/delete"
        }
      }
    },
    created() {
      getAction(this.url.treeList).then(res => {
        this.disTransData = this._.cloneDeep(res.result)
        this.treeData = this._.cloneDeep(translateDataToTree(res.result))
      })
    },
    methods: {
      loadData(){
        getAction(this.url.treeList).then(res => {
          this.disTransData = this._.cloneDeep(res.result)
          this.treeData = this._.cloneDeep(translateDataToTree(res.result))
        })
      },
      loads(){
        this.loadData();
      },
      handleOkModel(){
        httpAction(this.url.del + "?id=" + this.id,{},"delete").then(res => {
          if (res.success){
            this.$message.success(res.message)
          }else {
            this.$message.warning(res.message)
          }
          this.visibleModel = false;
          this.loads();
        })
      },
      handleCancelModel(){

      },
      // 右键店家下拉关闭下拉框
      closeDrop() {
        this.dropTrigger = ''
      },
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },
      AddOrEdit(){
        this.$emit('select', this.id, this.parentId,'2')
      },
      deletePart(){
        this.visibleModel = true;
      },
      rightHandle(node) {
        this.isEquip = "3";
        this.dropTrigger = 'contextmenu';
        if (node.node.dataRef.isEquip == '1'){
          this.parentId = node.node.dataRef.id;
          this.id = '0'
          this.isEquip = "1";
        }
        if (node.node.dataRef.isPart == '1'){
          this.parentId = node.node.dataRef.pid;
          this.id = node.node.dataRef.id;
          this.isEquip = "2";
        }
      },
      searchExpandedKeys(val) {
        if ('' == val) {
          this.treeData = translateDataToTree(this._.cloneDeep(this.disTransData))
          return false
        }
        let disTransData = this._.cloneDeep(this.disTransData)
        let res = this._.filter(disTransData, o => {
          if (o['isPart'] == '1' && o['title'].indexOf(val) != -1) {
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
          if (obj['isPart'] == '1') {
            this.$emit('select', obj['id'], obj.pid,'1')
          }
        }
      }
    },
    watch: {

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