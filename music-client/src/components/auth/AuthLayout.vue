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
  padding: 32px 40px;
  border-radius: 24px;
  background: var(--surface);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  box-sizing: border-box;

  .sign-head {
    margin: 0 0 1rem;
    text-align: center;
    font-family: $font-display;
    font-size: 1.5rem;
    font-weight: 600;
    letter-spacing: -0.02em;
    color: var(--foreground);
    line-height: 1.2;
  }

  .sign-btn:deep(.el-form-item__content) {
    @include layout(space-between);
    gap: 12px;

    button {
      flex: 1;
    }
  }
}

.login-logo {
  background-color: $color-blue-light;
  height: calc(100vh - var(--header-height) - var(--player-height));
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

  .sign {
    width: 84vw;
    max-width: 28rem;
    position: relative;
    margin: auto;
    margin-top: -78vh;
    padding: 24px 20px;
    border-radius: 20px;
  }
}

@media screen and (min-width: $sm) {
  .sign {
    width: min(400px, 36vw);
    position: absolute;
    left: 60vw;
    top: calc(var(--header-height) + 60px);
  }

  .forgot-password-page .sign {
    width: min(440px, 40vw);
  }
}
</style>
