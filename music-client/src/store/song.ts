import { defineStore } from "pinia";

export const useSongStore = defineStore("song", {
  state: () => ({
    songId: "" as string | number,
    songTitle: "",
    songUrl: "",
    songPic: "/img/songPic/tubiao.jpg",
    singerName: "",
    lyric: [] as any[],
    isPlay: false,
    volume: 0,
    duration: 0,
    curTime: 0,
    changeTime: 0,
    autoNext: true,
    currentPlayList: [] as any[],
    songDetails: null as any,
    currentPlayIndex: -1,
  }),
  actions: {
    setSongId(songId: string | number) {
      this.songId = songId;
    },
    setSongTitle(songTitle: string) {
      this.songTitle = songTitle;
    },
    setSongUrl(songUrl: string) {
      this.songUrl = songUrl;
    },
    setSongPic(songPic: string) {
      this.songPic = songPic;
    },
    setSingerName(singerName: string) {
      this.singerName = singerName;
    },
    setAutoNext(autoNext: boolean) {
      this.autoNext = autoNext;
    },
    setLyric(lyric: any[]) {
      this.lyric = lyric;
    },
    setIsPlay(isPlay: boolean) {
      this.isPlay = isPlay;
    },
    setVolume(volume: number) {
      this.volume = volume;
    },
    setDuration(duration: number) {
      this.duration = duration;
    },
    setCurTime(curTime: number) {
      this.curTime = curTime;
    },
    setChangeTime(changeTime: number) {
      this.changeTime = changeTime;
    },
    setCurrentPlayList(currentPlayList: any[]) {
      this.currentPlayList = currentPlayList;
    },
    setSongDetails(songDetails: any) {
      this.songDetails = songDetails;
    },
    setCurrentPlayIndex(currentPlayIndex: number) {
      this.currentPlayIndex = currentPlayIndex;
    },
    playMusic({ id, url, pic, index, songTitle, singerName, lyric, currentSongList }) {
      this.setSongId(id);
      this.setSongUrl(url);
      this.setSongPic(pic);
      this.setCurrentPlayIndex(index);
      this.setSongTitle(songTitle);
      this.setSingerName(singerName);
      this.setLyric(lyric);
      this.setCurrentPlayList(currentSongList);
    },
  },
});
