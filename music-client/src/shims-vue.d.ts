/* eslint-disable */
declare module "*.vue" {
  import type { DefineComponent } from "vue";
  const component: DefineComponent<{}, {}, any>;
  export default component;
}

declare module "vue/types/vue" {
  import VueRouter, { Route } from "vue-router";

  interface Vue {
    $router: VueRouter;
    $route: Route;
  }
}

declare module "@vue/runtime-core" {
  interface State {
    count: number;
  }

  interface ComponentCustomProperties {
    $store: import("vuex").Store<State>;
  }
}

// `<script setup>` compiler macros（部分 IDE/ts-plugin 无法识别编译器宏）
// 返回 any，避免仅依赖不完整 ExtractPropTypes 时 props 字段类型异常
declare function defineProps<T = any>(props?: T): any;
declare function withDefaults<T, D>(props: T, defaults: D): T;
declare function defineEmits<T = any>(): T;

// TypeScript 解析当前 `vue` 包时缺失一些命名导出（导致 TS2305）。
// 这里用最小显式声明把常用 API 补齐，确保 `<script lang="ts">` / `script setup`
// 里的写法能被正确类型检查。
declare module "vue" {
  export { computed, reactive, ref, toRefs } from "@vue/reactivity";
  export { defineComponent, nextTick, watch, onMounted, getCurrentInstance } from "@vue/runtime-core";
  export { createApp } from "@vue/runtime-dom";
}
