import { computed } from "@vue/reactivity";
import type { LocationQueryRaw } from "vue-router";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useConfigureStore } from "@/store/configure";
import { useSongStore } from "@/store/song";
import { RouterName } from "@/enums";
import { fetchDownloadMusic } from "@/api/song";
import { attachImageUrl, getSingerName, getSongTitle } from "@/utils";

interface RouterOptions {
  path?: string;
  query?: LocationQueryRaw;
}

export function useAppActions() {
  const router = useRouter();
  const configureStore = useConfigureStore();
  const songStore = useSongStore();

  const token = computed(() => configureStore.token);

  function checkStatus(status?: boolean) {
    if (!token.value) {
      if (status !== false) {
        ElMessage({
          message: "请先登录",
          type: "warning",
        });
      }
      return false;
    }
    return true;
  }

  function routerManager(routerName: string | number, options: RouterOptions) {
    switch (routerName) {
      case RouterName.Search:
      case RouterName.ForgotPassword:
        router.push({ path: options.path, query: options.query });
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
        router.push({ path: options.path });
        break;
    }
  }

  function goBack(step = -1) {
    router.go(step);
  }

  function playMusic({ id, url, pic, index, name, lyric, currentSongList }: any) {
    songStore.playMusic({
      id,
      url,
      pic,
      index,
      songTitle: getSongTitle(name),
      singerName: getSingerName(name),
      lyric,
      currentSongList,
    });
  }

  async function downloadMusic({ songUrl, songName }: { songUrl: string; songName: string }) {
    if (!songUrl) {
      ElMessage({
        message: "下载链接为空！",
        type: "error",
      });
      return;
    }

    const eleLink = document.createElement("a");
    eleLink.download = `${songName}.mp3`;
    eleLink.style.display = "none";

    try {
      const blob = await fetchDownloadMusic(songUrl);
      eleLink.href = URL.createObjectURL(blob);
      document.body.appendChild(eleLink);
      eleLink.click();
      document.body.removeChild(eleLink);
    } catch {
      ElMessage({
        message: "下载失败！",
        type: "error",
      });
    }
  }

  return {
    getSongTitle,
    getSingerName,
    attachImageUrl,
    checkStatus,
    routerManager,
    goBack,
    playMusic,
    downloadMusic,
  };
}
