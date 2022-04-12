import { getCurrentInstance, computed } from "vue";
import { useStore } from "vuex";
import { LocationQueryRaw } from "vue-router";
import { RouterName } from "@/enums";

interface routerOptions {
  path?: string;
  query?: LocationQueryRaw;
}

export default function () {
  const { proxy } = getCurrentInstance();

  const store = useStore();
  const token = computed(() => store.getters.token);

  function getUserSex(sex) {
    if (sex === 0) {
      return "女";
    } else if (sex === 1) {
      return "男";
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
      (proxy as any).$message({
        message: "请先登录",
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
      case RouterName.Search:
        proxy.$router.push({ path: options.path, query: options.query });
        break;
      case RouterName.Home:
      case RouterName.SongSheet:
      case RouterName.SongSheetDetail:
      case RouterName.Singer:
      case RouterName.SingerDetail:
      case RouterName.Personal:
      case RouterName.PersonalData:
      case RouterName.Setting:
      case RouterName.SignIn:
      case RouterName.SignUp:
      case RouterName.SignOut:
      case RouterName.Lyric:
      case RouterName.Error:
      default:
        proxy.$router.push({ path: options.path });
        break;
    }
  }

  function goBack(step = -1) {
    proxy.$router.go(step);
  }

  return {
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
