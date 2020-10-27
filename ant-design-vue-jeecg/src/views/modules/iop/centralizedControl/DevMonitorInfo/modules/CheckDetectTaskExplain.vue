<template>
  <a-modal
    :title="title"
    :width="600"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="异常原因:" :labelCol="{span:4}" :wrapperCol="{span:19}">
              <a-textarea :rows="3"
                          v-decorator="[ 'reasonName', validatorRules.reasonName]"
                          placeholder="..."/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="处理说明:" :labelCol="{span:4}" :wrapperCol="{span:19}">
              <a-textarea :rows="3"
                          v-decorator="[ 'remark', validatorRules.remark]"
                          placeholder="..."/>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">

          <a-col :span="12">
            <a-form-item label="填写人:" :labelCol="{span:8}" :wrapperCol="{span:14}">
              <a-input placeholder=""
                       v-decorator="[ 'createName', validatorRules.createName]"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="填写时间" :labelCol="{span:8}" :wrapperCol="{span:14}">
              <j-date placeholder="填写时间"
                      v-decorator="[ 'createTime', validatorRules.createTime]"
                      style="width: 100%" :trigger-change="true"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="submit" type="primary" @click="save">
        确定
      </a-button>
      <a-button key="submitAndDispatch" type="primary" @click="handleCancel">
        取消
      </a-button>
    </template>
  </a-modal>
</template>

<script>
    import pick from 'lodash.pick'
    import JDate from '@/components/jeecg/JDate'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'
    import JDictSelectTagOra from '@/components/dict/JDictSelectTagOra'
    import JDictSelectTagIop from '@/components/dict/JDictSelectTagIop'
    import ATextarea from 'ant-design-vue/es/input/TextArea'
    import { getAction } from '@/api/manage'
    import Vue from 'vue'
    import { ACCESS_TOKEN } from '@/store/mutation-types'
    import { httpAction } from '../../../../../../api/manage'

    export default {
    name: "checkTaskExplain",
    components: {
      JDate,
      JDictSelectTag,
      JDictSelectTagOra,
      JDictSelectTagIop,
      ATextarea
    },
    data () {
      return {
        form: this.$form.createForm(this),
        //token header
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        title:"核对异常处理说明",
        width:800,
        visible: false,
        confirmLoading: false,
        validatorRules: {
            reasonName:{rules:[{
                required: true,
                message: '不能为空'
                }
              ]},
            remark:{rules:[{
                    required: true,
                    message: '不能为空'
                }
                ]},
            createName:{rules:[{
                    required: true,
                    message: '不能为空'
                }
                ]},
            createTime:{rules:[{
                    required: true,
                    message: '不能为空'
                }
                ]}
        },
        url: {
          add: "/checkExplain/add",
          queryTaskNo: "/checkExplain/queryByTaskNo",
        },
        responseResult:{}
      }
    },
    created () {
    },
    computed: {
    },
    methods: {
      initialData(record){
          this.visible=true;
          console.log(record.detectTaskNo)
          getAction(this.url.queryTaskNo,{detectTaskNo:record.detectTaskNo}).then((res) => {
              if(res.success){
                  this.model = Object.assign({}, res.result);
                  this.$nextTick(() => {
                      this.form.setFieldsValue(pick(this.model,'reasonName','remark','createName','createTime'))
                  })
              }
          })
      },
      save () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
            if (!err) {
                that.confirmLoading = true;
                let formData = Object.assign(this.model, values);
                console.log("--=-=-=-=")
                console.log("表单提交的数据")
                console.log(formData)
                console.log("--=-=-=-=")
                httpAction(this.url.add,formData,"post").then((res) => {
                    if(res.success){
                        that.$message.success(res.message);
                    }else{
                        that.$message.warning(res.message);
                    }
                }).finally(() => {
                    that.confirmLoading = false;
                    that.close();
                })
            }
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
    }
  }
</script>