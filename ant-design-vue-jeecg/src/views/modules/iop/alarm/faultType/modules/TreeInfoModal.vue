<template>
  <div class="tree-scroll">
    <a-tree
      @select="onSelect"
      @check="onCheck"
      :selectedKeys="selectedKeys"
      :checkedKeys="checkedKeys"
      :treeData="departTree"
      :checkStrictly="checkStrictly"
      @expand="onExpand"/>
  </div>

</template>

<script>

  import { getAction ,deleteAction,postAction} from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "TreeInfoModal",
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
          treequeryTreeList: "alarmType/faultTaskInfo/queryTreeList",
        },
        created() {
          this.loadTree();
        },
      }
    },
    methods: {
      loadTree() {
        var that = this
        that.treeData = []
        that.departTree = []
        getAction(this.url.queryTreeList).then((res)=>{
          if(res.success){
            if (res.success) {
              for (let i = 0; i < res.result.length; i++) {
                let temp = res.result[i]
                that.treeData.push(temp)
                that.departTree.push(temp)
              }
              this.loading = false
            }
          }
        });
      },
      onCheck(checkedKeys, info) {
        console.log('onCheck', checkedKeys, info)
        this.hiding = false
        //this.checkedKeys = checkedKeys.checked
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
        if(this.checkStrictly){
          this.checkedKeys = checkedKeys.checked;
        }else{
          this.checkedKeys = checkedKeys
        }
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      },
      onSelect(selectedKeys, e) {
        console.log('selected', selectedKeys, e)
        this.hiding = false
        let record = e.node.dataRef
        console.log('onSelect-record', record)
        this.currSelected = Object.assign({}, record)
        this.model = this.currSelected
        this.selectedKeys = [record.key]
        this.model.parentId = record.parentId
        this.onselectedRecond=Object.assign({}, record);

        this.refreshTableData(record);

      },
      // 触发onSelect事件时,为部门树右侧的form表单赋值
      setValuesToForm(record) {
        if(record.orgCategory == '1'){
          this.orgCategoryDisabled = true;
        }else{
          this.orgCategoryDisabled = false;
        }
        this.form.getFieldDecorator('fax', {initialValue: ''})
        this.form.setFieldsValue(pick(record, 'departName','orgCategory', 'orgCode', 'departOrder', 'mobile', 'fax', 'address', 'memo'))
      },
      // 右键点击下拉框改变事件
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },
      onExpand(expandedKeys) {
        console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        this.autoExpandParent = false
      },
      handleAdd(){
        if(this.onselectedRecond.leaf){
          this.$refs.modalForm.add(this.onselectedRecond);
        }else {
          this.$message.warning("请选择左边检定线设备！！")
        }
      },
      modalFormOk () {
        this.refreshTableData();
      },
      refreshTableData(){
        getAction(this.url.list,{equipId:this.onselectedRecond.equipId}).then((res)=>{
          console.log("res.success-------",res.success)
          console.log("res-------",res)
          if(res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        });
      }
    }
  }
</script>


<style scoped>
  @import '~@assets/less/common.less';
  .tree-scroll { height:600px;overflow-y:scroll;}
</style>