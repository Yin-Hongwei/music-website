import { defineStore } from "pinia";

interface UserState {
  userId: string | number;
  username: string;
  userPic: string;
}

export const useUserStore = defineStore("user", {
  state: (): UserState => ({
    userId: "",
    username: "",
    userPic: "",
  }),
  actions: {
    setUserId(userId: string | number) {
      this.userId = userId;
    },
    setUsername(username: string) {
      this.username = username;
    },
    setUserPic(userPic: string) {
      this.userPic = userPic;
    },
  },
});
