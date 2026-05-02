import { defineStore } from "pinia";

interface ConfigureState {
  token: boolean;
  showAside: boolean;
  showSongCommentDialog: boolean;
  activeNavName: string;
}

export const useConfigureStore = defineStore("configure", {
  state: (): ConfigureState => ({
    token: false,
    showAside: false,
    showSongCommentDialog: false,
    activeNavName: "首页",
  }),
  actions: {
    setToken(token: boolean) {
      this.token = token;
    },
    setActiveNavName(activeNavName: string) {
      this.activeNavName = activeNavName;
    },
    setShowAside(showAside: boolean) {
      this.showAside = showAside;
    },
    setShowSongCommentDialog(showSongCommentDialog: boolean) {
      this.showSongCommentDialog = showSongCommentDialog;
    },
  },
});
