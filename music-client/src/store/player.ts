import { defineStore } from "pinia";
import { useSongStore } from "@/store/song";
import { resolveSingerName, resolveSongTitle } from "@/utils";

type SongId = string | number;

export interface PlaySongItem {
  id: SongId;
  url: string;
  pic: string;
  lyric: unknown;
  name: string;
  singerName?: string;
  songName?: string;
  duration?: number | null;
}

export interface PlaySongPayload {
  id: SongId;
  url: string;
  pic: string;
  lyric: unknown;
  index: number;
  name: string;
  singerName?: string;
  currentSongList: PlaySongItem[];
  duration?: number | null;
}

export const usePlayerStore = defineStore("player", {
  state: () => ({
    lastPlayedId: "" as SongId,
  }),
  actions: {
    setLastPlayedId(id: SongId) {
      this.lastPlayedId = id;
    },
    playSong(payload: PlaySongPayload) {
      const {
        id,
        url,
        pic,
        lyric,
        index,
        name,
        singerName,
        currentSongList,
        duration,
      } = payload;
      const songStore = useSongStore();
      this.setLastPlayedId(id);
      songStore.playMusic({
        id,
        url,
        pic,
        index,
        songTitle: resolveSongTitle(name),
        singerName: resolveSingerName(singerName),
        lyric,
        currentSongList,
        duration,
      });
    },
  },
});
