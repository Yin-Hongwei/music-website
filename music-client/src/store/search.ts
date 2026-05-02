import { defineStore } from "pinia";

interface SearchState {
  keyword: string;
}

export const useSearchStore = defineStore("search", {
  state: (): SearchState => ({
    keyword: "",
  }),
  actions: {
    setKeyword(keyword: string) {
      this.keyword = keyword;
    },
    clearKeyword() {
      this.keyword = "";
    },
  },
});
