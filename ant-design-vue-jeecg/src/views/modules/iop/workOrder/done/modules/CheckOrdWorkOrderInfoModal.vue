<template>
  <a-modal
    title="结单"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
      <a-form :form="form">
        <a-row >
          <a-col >
            您确定要对 {{data}} 单据进行结单处理吗？
          </a-col>
        </a-row>
      </a-form>
  </a-modal>
</template>

<script>

  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
  import JDictSelectTagIop from "@/components/dict/JDictSelectTagIop"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import { httpAction ,getAction } from '@/api/manage'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"

  export default {
    name: "OrdWorkOrderInfoModal",
    components: {
      JDate,
      JDictSelectTag,
      JDictSelectTagOra,
      JDictSelectTagIop,
      ATextarea
    },
    data () {
      return {
        data:"",
        confirmLoading:false,
        form: this.$form.createForm(this),
        //token header
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        width:800,
        visible: false,
        model: {},
        workData: {},
      }
    },
    created () {
    },
    methods: {
      edit(data) {
        this.data = data.id;
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        let httpurl = '/workOrder/ordWorkOrderInfo/checkInfo?id=' + this.data;
        let method = 'post';
        httpAction(httpurl, {}, method).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.$emit('ok');
          } else {
            that.$message.warning(res.message);
          }
        }).finally(() => {
          that.confirmLoading = false;
          that.close();
        })
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>