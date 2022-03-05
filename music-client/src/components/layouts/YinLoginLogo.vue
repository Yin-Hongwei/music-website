<template>
  <div class="login-logo">
    <yin-icon :icon="Iicon"></yin-icon>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import YinIcon from "./YinIcon.vue";
import { ICON } from "@/enums";

export default defineComponent({
  components: {
    YinIcon,
  },
  data() {
    return {
      icon: ICON.ERJI,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.login-logo {
  background-color: $color-blue-light;
  height: 100vh;
  width: 50vw;
  min-width: 50vw;
  overflow: hidden;
  @include layout(center, center);
  .icon {
    @include icon(40rem, $color-blue-dark);
    transform: rotate(-30deg);
  }
}
</style>
