<template>
  <a-date-picker
    :disabled="disabled || readOnly"
    @change="handleDateChange"
    :format="dateFormat"
    :disabled-date="disabledDate"
    :value="momVal"
    :disabled-time="disabledDateTime"
    :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
  />
</template>
<script>
  import moment from 'moment'
  export default {
    name: 'JTime',
    props: {
      // placeholder:{
      //   type: String,
      //   default: '',
      //   required: false
      // },
      value:{
        type: String,
        required: false
      },
      dateFormat:{
        type: String,
        default: 'YYYY-MM-DD HH:mm:ss',
        required: false
      },
      //此属性可以被废弃了
      triggerChange:{
        type: Boolean,
        required: false,
        default: false
      },
      readOnly:{
        type: Boolean,
        required: false,
        default: false
      },
      disabled:{
        type: Boolean,
        required: false,
        default: false
      },
      showTime:{
        defaultValue: false
      },
      getCalendarContainer: {
        type: Function,
        default: () => document.body
      }
    },
    data () {
      let dateStr = this.value;
      return {
        decorator:"",
        momVal:!dateStr?null:moment(dateStr,this.dateFormat)
      }
    },
    watch: {
      value (val) {
        if(!val){
          this.momVal = null
        }else{
          this.momVal = moment(val,this.dateFormat)
        }
      }
    },
    methods: {
      moment,
      range(start, end) {
        const result = [];
        for (let i = start; i < end; i++) {
          result.push(i);
        }
        return result;
      },
      handleDateChange(mom,dateStr){
        this.$emit('change', dateStr);
      },
      disabledDate(current) {
        // Can not select days before today and today
        return current && current < moment().endOf('day');
      },
      disabledDateTime() {
        return {
          disabledHours: () => this.range(0, 24).splice(4, 20),
          disabledMinutes: () => this.range(30, 60),
          disabledSeconds: () => [55, 56],
        };
      },
    },
    //2.2新增 在组件内定义 指定父组件调用时候的传值属性和事件类型 这个牛逼
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
