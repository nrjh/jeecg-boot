<template>
  <ProdPlanInfoTree @select="onSelect"></ProdPlanInfoTree>
</template>

<script>
  import { getAction, postAction } from '@/api/manage'
  import ProdPlanInfoTree from "./modules/ProdPlanInfoTree";
  import JDictSelectTag from '@/components/dict/JDictSelectTag'
  export default {
    name: 'ProdPlanInfoList',
    components: {
      JDictSelectTag,
      ProdPlanInfoTree
    },
    created() {

    },
    data() {
      let vm = this
      return {
        visible1:false,
        loading: false,
        url: {
          detail: '/prod/devProduceCycle/queryById',
        },
        selectObj: {},
      }
    },
    computed: {

    },
    methods: {
      onSelect(id, obj) {
        console.log(id)
        getAction(this.url.detail, { id: obj.pid }).then(res => {
          this.selectObj = res.result
          this.loadData()
        })
      },
      loadData() {
        //this.loading = true
        let temp = {}
        console.log(this.selectObj);
/*        console.log(this.selectObj);
        temp.faultType = this.selectObj.id;
        temp.productId = this.selectObj.equipId
        if (this.status && this.status != '') {
          temp.isStatus = this.status
        }*/
/*        getAction(this.url.list, temp).then((res) => {
          if (res.success) {
            this.dataSource = res.result
            this.ipagination.current = 1
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })*/
      },
      handleTableChange(pagination) {
        this.ipagination.current = pagination.current
      },
    },
    watch: {
      status(val) {
        this.loadData()
      }
    }
  }
</script>

<style scoped>
  .equip-bind-title {

  }
  .ant-alert-info {
    border: 1px solid #068b881f;
    background-color: #068b881f;
  }
</style>