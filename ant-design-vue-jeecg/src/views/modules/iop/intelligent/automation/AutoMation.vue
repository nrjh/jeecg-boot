<template>
  <a-card :bordered="true">
    <a-row :gutter="24">
      <a-col :xxl="4" :lg="4" :md="4">
        <AreaEquipTree ref="AreaEquipTree" @select="onSelect" @add="addEquip" @delete="deleteEquip" @edit="handEdit"></AreaEquipTree>
      </a-col>
      <a-col :xxl="20" :lg="20" :md="20">
        <a-layout>
          <a-layout-header :style="{zIndex: 1, width: '100%',background:'white' }">
            <a-menu
              theme="light"
              mode="horizontal"
              :default-selected-keys="defaultKey"
            >
              <a-menu-item key="1" @click="toggleTab('FailuresCount')">
                故障次数
              </a-menu-item>
            </a-menu>
          </a-layout-header>
          <a-layout-content>
            <component v-bind:is="cut" ref="sendEquipInfo"></component>
          </a-layout-content>

        </a-layout>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
    import LookFailuresCount from './modules/LookFailuresCount'
    import AreaEquipTree from './modules/AreaEquipTree'
    export default {
        name:'AutoMation',
        components:{
            LookFailuresCount,
            AreaEquipTree
        },
        data(){
            return {
                content:"",
                cut:'LookFailuresCount',
                defaultKey:['1']
            }
        },
        methods:{
            toggleTab(tab){
                this.cut = tab
            },
            onSelect(id, obj) {
                this.$refs.sendEquipInfo.equipInfo(id,obj);
            },
            addEquip(id, obj){
                this.$refs.sendEquipInfo.add(id, obj);
            },
            deleteEquip(id, obj){
            },
            handEdit(id, obj){
                this.$refs.sendEquipInfo.handEdit(id, obj);
            }
        },
        created:function() {
        }
    }
</script>
<style>

</style>