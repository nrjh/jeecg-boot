<template>
  <div class="area-equip-tree">
    <a-input-search style="padding-right: 8px" placeholder="请输入设备名称进行查找" v-model="searchVal"/>
    <a-tree :tree-data="treeData"
            :replaceFields="replaceFields"
            :expandedKeys.sync="expandedKeys"
            @select="selectNode"
    >
    </a-tree>
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