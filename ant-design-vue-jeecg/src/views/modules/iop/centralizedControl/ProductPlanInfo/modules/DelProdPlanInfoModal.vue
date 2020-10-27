<template>
  <a-modal
    title="删除"
    :width="400"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="取消">
      <a-form :form="form">
        <a-row >
          <a-col >
            您确定删除该记录？
          </a-col>
        </a-row>
      </a-form>
  </a-modal>
</template>
<script>

  import JDate from '@/components/jeecg/JDate'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import { httpAction ,getAction } from '@/api/manage'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  export default {
    name: "DelProdPlanInfoModal",
    components: {
      JDate,
      ATextarea
    },
    data () {
      return {
        confirmLoading:false,
        data:"",
        form: this.$form.createForm(this),
        //token header
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        width:800,
        visible: false,
        model: {},
        workData: {},
        url: {
          del: '/prod/devProduceCycle/delete'
        }
      }
    },
    created () {
    },
    methods: {
      edit(data) {
        this.data = data;
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        let httpurl = this.url.del + '?id=' + this.data;
        let method = 'delete';
        httpAction(httpurl, {}, method).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
          } else {
            that.$message.warning(res.message);
          }
        }).finally(() => {
          that.confirmLoading = false;
          this.$emit('ok');
          that.close();
        })
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>