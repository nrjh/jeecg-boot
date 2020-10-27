<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- 操作按钮区域 -->
    <a-modal
      :title="title"
      :width="width"
      :visible="visible"
      @cancel="handleCancel"
      :footer="null"
      cancelText="关闭">
        <div class="table-operator">
          <a-upload
            name="file"
            :multiple="false"
            :action="minioUploadAction"
            :headers="tokenHeader"
            :showUploadList="false"
            :beforeUpload="beforeUpload"
            @change="handleChange">
            <a-button>
              <a-icon type="upload"/>
              作业指导上传
            </a-button>
          </a-upload>
        </div>
    </a-modal>
    <!-- table区域-begin -->
    <!-- table区域-end -->
  </a-card>
</template>
<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import AModal from "ant-design-vue/es/modal/Modal";

  export default {
    components: {AModal}, name: "workModul",
    mixins: [JeecgListMixin],
    data() {
      return {
        description: '文件列表',
        title: "作业指导上传",
        width: 800,
        visible: false,
        url: {
          upload: "/oss/file/upload",
          list: "/oss/file/list",
          delete: "/oss/file/delete",
          minioUpload: "/sys/upload/uploadMinio"
        }
      }
    },
    computed: {
      uploadAction() {
        return window._CONFIG['domianURL'] + this.url.upload;
      },
      minioUploadAction() {
        return window._CONFIG['domianURL'] + this.url.minioUpload;
      },
    },
    methods: {
      beforeUpload(file) {
        var fileType = file.type;
        if (fileType === 'image') {
          if (fileType.indexOf('image') < 0) {
            this.$message.warning('请上传图片');
            return false;
          }
        } else if (fileType === 'file') {
          if (fileType.indexOf('image') >= 0) {
            this.$message.warning('请上传文件');
            return false;
          }
        }
        return true
      },
      handleChange(info) {
          console.log("info----------",info)
        if (info.file.status === 'done') {
          if (info.file.response.success) {
            let  workName=info.file.name;
            let  sop=info.file.response.message;
            let  returnData=[];
            let  workData={
              workName:workName,
              sop:sop
            };
            returnData.push(workData)
            this.$emit('returnWork',returnData)

            this.$message.success(`${info.file.name} 上传成功!`);
          } else {
            this.$message.error(`${info.file.response.message}`);
          }
        } else if (info.file.status === 'error') {
          this.$message.error(`${info.file.response.message}`);
        }
        this.handleCancel();
      },
      handlePreview(record) {
        if (record && record.url) {
          let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(record.url)
          window.open(url, '_blank')
        }
      },
      showModal(){
        this.visible=true;
      },
      handleCancel(){
        this.visible=false;
      }
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less';
</style>