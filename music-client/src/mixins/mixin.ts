import { getCurrentInstance, computed } from "vue";
import { useStore } from "vuex";
import { LocationQueryRaw } from "vue-router";
import {
  HOME,
  SONG_SHEET,
  SINGER,
  PERSONAL,
  SETTING,
  SIGN_IN,
  SIGN_UP,
  SIGN_OUT,
  SEARCH,
  LYRIC,
  SONG_SHEET_DETAIL,
  SINGER_DETAIL,
  ERROR,
  PERSONAL_DATA,
} from "@/enums";

interface routerOptions {
  path?: string;
  query?: LocationQueryRaw;
}

export default function () {
  const { proxy } = getCurrentInstance();

  const store = useStore();
  const BASE_URL = computed(() => store.getters.BASE_URL);
  const token = computed(() => store.getters.token);

  // 获取图片信息
  function attachImageUrl(srcUrl) {
    return BASE_URL.value + srcUrl || "../assets/images/user.jpg";
  }

  function getBirth (val) {
    const birth = String(val).match(/[0-9-]+(?=\s)/)
    return Array.isArray(birth) ? birth[0] : birth
  }

  function getUserSex (sex) {
    if (sex === 0) {
      return '女'
    } else if (sex === 1) {
      return '男'
    }
  }

  // 获取歌曲名
  function getSongTitle(str) {
    return str.split("-")[1];
  }

  // 获取歌手名
  function getSingerName(str) {
    return str.split("-")[0];
  }

  function checkStatus() {
    if (!token.value) {
      (proxy as any).$notify({
        title: "请先登录",
        type: "warning",
      });
      return false;
    }
    return true;
  }

  // 播放
  function playMusic({ id, url, pic, index, name, lyric, currentSongList }) {
    const songTitle = getSongTitle(name);
    const singerName = getSingerName(name);
    proxy.$store.dispatch("playMusic", {
      id,
      url,
      pic,
      index,
      songTitle,
      singerName,
      lyric,
      currentSongList,
    });
  }

  function changeIndex(value) {
    proxy.$store.commit("setActiveNavName", value);
  }
  // 路由管理
  function routerManager(routerName: string | number, options: routerOptions) {
    switch (routerName) {
      case SEARCH:
        proxy.$router.push({ path: options.path, query: options.query });
        break;
      case HOME:
      case SIGN_OUT:
      case SONG_SHEET:
      case SONG_SHEET_DETAIL:
      case SINGER:
      case SINGER_DETAIL:
      case PERSONAL:
      case SETTING:
      case PERSONAL_DATA:
      case SIGN_IN:
      case SIGN_UP:
      case LYRIC:
      case ERROR:
      default:
        proxy.$router.push({ path: options.path });
        break;
    }
  }

  function goBack(step = -1) {
    proxy.$router.go(step);
  }

  return {
    attachImageUrl,
    getBirth,
    getUserSex,
    getSongTitle,
    getSingerName,
    changeIndex,
    checkStatus,
    playMusic,
    routerManager,
    goBack,
  };
}
