<template>
  <div class="search">
    <nav class="search-nav">
      <span
        v-for="(item, index) in searchNavList"
        :key="index"
        :class="{ 'is-active': currentView === item.value }"
        @click="currentView = item.value"
      >
        {{ item.name }}
      </span>
    </nav>
    <component :is="currentView"></component>
  </div>
</template>

<script>
import mixin from "@/mixins";
import SearchSong from "./SearchSong.vue";
import SearchSongList from "./SearchSongList.vue";

export default {
  mixins: [mixin],
  components: {
    SearchSong,
    SearchSongList,
  },
  data() {
    return {
      searchNavList: [
        {
          name: "歌曲",
          value: "SearchSong",
        },
        {
          name: "歌单",
          value: "SearchSongList",
        },
      ],
      currentView: "SearchSong",
    };
  }
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.search{
  margin: auto;
  margin-top: 0;
  padding-top: $header-height;
  background-color: $color-white;
  border-radius: 12px;
  width: 900px;
  position: relative;
}

.search-nav {
  @include layout(space-around);
  margin-top: 20px;
  font-size: 1.5rem;
  color: $color-black;
  span {
    line-height: 50px;
    cursor: pointer;
  }
}

.is-active {
  font-weight: 600;
  border-bottom:5px solid $color-black;
}
</style>
