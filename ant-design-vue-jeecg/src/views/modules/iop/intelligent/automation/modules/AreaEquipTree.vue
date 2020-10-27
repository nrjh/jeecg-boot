<template>
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
        <a-menu-item @click="handleAdd" key="1" v-show="isEquipNo">添加</a-menu-item>
        <a-menu-item @click="handleEdit" key="2" v-show="isEquipNo">修改</a-menu-item>
<!--        <a-menu-item @click="handleDelete" key="3" v-show="isEquipNo">删除</a-menu-item>-->
        <a-menu-item @click="closeDrop" key="4">取消</a-menu-item>
      </a-menu>

    </a-dropdown>
  </div>
</template>

<script>
  import { getAction } from '@/api/manage'
  import { translateDataToTree } from '@/views/modules/iop/stk/in/util/tree'

  export default {
    name: 'AreaEquipTree',
    data() {
      return {
        treeData: [],
        disTransData: [],
        expandedKeys: [],
        dropTrigger: '',
        nodeData:{},
        isEquipNo:false,
        replaceFields: {
          title: 'title', key: 'id'
        },
        searchVal: '',
        url: {
          treeList: '/alarm/bind/faultPrdProductInfo/tree'
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
      searchExpandedKeys(val) {
        if ('' == val) {
          this.treeData = translateDataToTree(this._.cloneDeep(this.disTransData))
          return false
        }
        let disTransData = this._.cloneDeep(this.disTransData)
        let res = this._.filter(disTransData, o => {
          if (o['isEquip'] == '1' && o['title'].indexOf(val) != -1) {
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
          if (obj['isEquip'] == '1') {
            this.$emit('select', obj['id'], obj)
          }
        }
      },
      handleAdd() {
        let obj = this.nodeData
        if (obj) {
            this.$emit('add', obj['id'], obj)
        }
      },
      handleDelete(){
        let obj = this.nodeData
        if (obj) {
          this.$emit('delete', obj['id'], obj)
        }
      },
      handleEdit(){
        let obj = this.nodeData
        if (obj) {
          this.$emit('edit', obj['id'], obj)
        }
      },
      // 右键店家下拉关闭下拉框
      closeDrop() {
        this.dropTrigger = ''
      },
      rightHandle(node) {
        console.log('node.dataRef------',node.node.dataRef)
        this.nodeData=node.node.dataRef;
        if(node.node.dataRef.isEquip==='0' && node.node.dataRef.pid !='0'){
          this.dropTrigger = 'contextmenu'
          this.isEquipNo=false;
        }else if(node.node.dataRef.isEquip==='1'){
          this.dropTrigger = 'contextmenu'
          this.isEquipNo=true;
        }else {
          this.dropTrigger = ''
        }

      },
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },
      onloadData(){
        getAction(this.url.treeList).then(res => {
          this.disTransData = this._.cloneDeep(res.result)
          this.treeData = this._.cloneDeep(translateDataToTree(res.result))
        })
      },
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