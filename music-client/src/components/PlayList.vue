<template>
  <div class="play-list">
    <div class="section-title" v-if="title">{{ title }}</div>
    <ul class="section-content">
      <li class="content-item" v-for="(item, index) in playList" :key="index">
        <div class="kuo" @click="goAblum(item)">
          <img class="item-img" :src="attachImageUrl(item.pic)" alt="" />
          <div class="mask" @click="goAblum(item)">
            <yin-icon :icon="BOFANG"></yin-icon>
          </div>
        </div>
        <p class="item-name">{{ item.name || item.title }}</p>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, ref, toRefs } from "vue";
import YinIcon from "@/components/layouts/YinIcon.vue";
import mixin from "@/mixins/mixin";
import { Icon } from "@/enums";
import { HttpManager } from '@/api'

export default defineComponent({
  components: {
    YinIcon,
  },
  props: {
    title: String,
    playList: Array,
    path: String,
  },
  setup(props) {
    const { proxy } = getCurrentInstance();
    const { routerManager } = mixin();

    const { path } = toRefs(props);
    const BOFANG = ref(Icon.BOFANG);

    function goAblum(item) {
      proxy.$store.commit("setSongDetails", item);
      routerManager(path.value, { path: `/${path.value}/${item.id}` });
    }

    return {
      BOFANG,
      goAblum,
      attachImageUrl: HttpManager.attachImageUrl,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/play-list.scss";
</style>
