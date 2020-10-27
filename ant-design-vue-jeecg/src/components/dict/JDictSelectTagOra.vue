<template>
  <a-radio-group v-if="tagType=='radio'" @change="handleInput" :value="getValueSting" :disabled="disabled">
    <a-radio v-for="(item, key) in dictOptions" :key="key" :value="item.value">{{ item.text }}</a-radio>
  </a-radio-group>

  <a-select v-else-if="tagType=='select'" :getPopupContainer="(target) => target.parentNode" :placeholder="placeholder"
            :disabled="disabled" :value="getValueSting" @change="handleInput">
<!--    <a-select-option :value="undefined">请选择</a-select-option>-->
    <a-select-option v-for="(item, key) in dictOptions" :key="key" :value="item.value">
      <span style="display: inline-block;width: 100%" :title=" item.text || item.label ">
        {{ item.text || item.label }}
      </span>
    </a-select-option>
  </a-select>
</template>

<script>
  import { ajaxGetDictItemsOra } from '@/api/api'

  export default {
    name: 'JDictSelectTagOra',
    props: {
      dictCode: String,
      placeholder: String,
      triggerChange: Boolean,
      disabled: Boolean,
      value: [String, Number],
      type: String
    },
    data() {
      return {
        dictOptions: [],
        tagType: ''
      }
    },
    watch: {
      dictCode: {
        immediate: true,
        handler() {
          this.initDictData()
        }
      }
    },
    created() {
      // console.log(this.dictCode);
      if (!this.type || this.type === 'list') {
        this.tagType = 'select'
      } else {
        this.tagType = this.type
      }
      //获取字典数据
      // this.initDictData();
    },
    computed: {
      getValueSting() {
        return this.value ? this.value.toString() : "请选择"
      }
    },
    methods: {
      initDictData() {
        //根据字典Code, 初始化字典数组
        // 查询IOP数据库
        ajaxGetDictItemsOra(this.dictCode, null).then((res) => {
          if (res.success) {
//                console.log(res.result);
            this.dictOptions = res.result
          }
        })
      },
      handleInput(e) {
        let val
        if (this.tagType == 'radio') {
          val = e.target.value
        } else {
          val = e
        }
        if (this.triggerChange) {
          this.$emit('change', val)
        } else {
          this.$emit('input', val)
        }
        let temp = this._.find(this.dictOptions, { value: val })
        this.$emit('inputText', temp.text)
      },
      setCurrentDictOptions(dictOptions) {
        this.dictOptions = dictOptions
      },
      getCurrentDictOptions() {
        return this.dictOptions
      }
    }
  }
</script>

<style scoped>
</style>