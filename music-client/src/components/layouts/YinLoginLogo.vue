<template>
  <div class='login-logo'>
    <svg class='icon' aria-hidden='true'>
      <use :xlink:href='ERJI'></use>
    </svg>
  </div>
</template>

<script>
import { ICON } from '../../enums'

export default {
  name: 'YinLoginLogo',
  data () {
    return {
      ERJI: ICON.ERJI
    }
  }
}
</script>

<style lang='scss' scoped>
@import '@/assets/css/yin-login-logo.scss';
</style>
