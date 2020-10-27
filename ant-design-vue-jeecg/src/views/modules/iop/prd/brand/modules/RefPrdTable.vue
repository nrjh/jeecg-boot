<template>
  <a-table
    ref="refPrdTable"
    size="middle"
    bordered
    rowKey="id"
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :pagination="ipagination"
    @change="handleTableChange"
  >
  </a-table>
</template>

<script>
  import { getAction } from '@/api/manage'

  export default {
    name: 'RefPrdTable',
    props: ['queryBy', 'queryValue'],
    data() {
      return {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '内部货号',
            align: 'center',
            dataIndex: 'defaultCode'
          },
          {
            title: '条形码',
            align: 'center',
            dataIndex: 'barcode'
          },
          {
            title: '内部说明',
            align: 'center',
            dataIndex: 'description'
          }
        ],
        ipagination: {
          current: 1,
          pageSize: 5,
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: false,
          showSizeChanger: false,
          total: 0
        },
        url: {
          list: '/iop/prd/product/ref/list'
        }
      }
    },
    methods: {
      loadData() {
        this.loading = true
        let temp = {}
        temp.pageNo = this.ipagination.current
        temp.pageSize = this.ipagination.pageSize
        temp[this.queryBy] = this.queryValue
        getAction(this.url.list, temp).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      handleTableChange(pagination) {
        this.ipagination.current = pagination.current
        this.loadData()
      }
    },
    created() {
      this.loadData()
    }
  }
</script>

<style scoped>

</style>