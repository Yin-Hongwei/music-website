<template>
  <main :class="pageClass">
    <div class="login-logo">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#icon-erji"></use>
      </svg>
    </div>
    <section class="sign">
      <h2 class="sign-head">{{ title }}</h2>
      <slot />
    </section>
  </main>
</template>

<script lang="ts" setup>
withDefaults(
  defineProps<{
    title: string;
    pageClass?: string;
  }>(),
  {
    pageClass: "sign-page",
  },
);
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.sign {
  padding: 30px 50px;
  border-radius: 10px;
  background-color: $color-white;

  .sign-head {
    text-align: center;
    margin-bottom: 10px;
    font-size: 20px;
    font-weight: 600;
  }

  .sign-btn:deep(.el-form-item__content) {
    @include layout(space-between);
    button {
      width: 50%;
    }
  }
}

.login-logo {
  background-color: $color-blue-light;
  height: calc(100vh - $header-height - $footer-height);
  width: 50vw;
  overflow: hidden;
  @include layout(center, center);

  .icon {
    @include icon(36rem, $color-blue-dark);
    transform: rotate(-30deg);
  }
}

@media screen and (max-width: $sm) {
  .login-logo {
    width: 100vw;
  }
}

@media screen and (min-width: $sm) {
  .sign {
    width: 300px;
    position: absolute;
    left: 60vw;
    top: $header-height + 60px;
  }
}

@media screen and (max-width: $sm) {
  .sign {
    width: 60vw;
    position: relative;
    margin: auto;
    margin-top: -78vh;
  }
}
</style>
