<template>
  <div class='setting'>
    <div class='leftCol'>
      <div class='settingsMainHeader'>设置</div>
      <ul class='setting-aside'>
        <li v-for='(item, index) in settingList' :key='index' :class='{activeColor: activeName === item.name}' @click='handleClick(item)'>
          {{item.name}}
        </li>
      </ul>
    </div>
    <div class='contentCol'>
      <component :is='componentSrc'></component>
    </div>
  </div>
</template>

<script>
import PersonalData from './PersonalData'
import Upload from './Upload'

export default {
  name: 'Setting',
  components: {
    PersonalData,
    Upload
  },
  data () {
    return {
      activeName: '个人信息',
      componentSrc: 'PersonalData',
      settingList: [{
        icon: '',
        name: '个人信息',
        path: 'PersonalData'
      }, {
        icon: '',
        name: '修改头像',
        path: 'Upload'
      }]
    }
  },
  methods: {
    handleClick (item) {
      this.activeName = item.name
      this.componentSrc = item.path
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/setting.scss';
</style>
