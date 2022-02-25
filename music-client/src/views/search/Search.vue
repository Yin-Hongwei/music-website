<template>
  <div class="container">
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
  },
};
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.container {
  padding-top: $header-height + 30px;
}

.search {
  margin: auto;
  background-color: $color-white;
  border-radius: 12px;
  width: 900px;
  position: relative;
}

.search-nav {
  @include layout(space-around);
  font-size: 1.5rem;
  color: $color-black;
  span {
    line-height: 50px;
    cursor: pointer;
  }
}

.is-active {
  font-weight: 600;
}
</style>
