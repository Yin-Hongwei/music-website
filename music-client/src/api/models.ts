/**
 * 客户端列表/卡片展示用领域模型。
 */

export interface HomeCardItem {
  id: number;
  pic: string;
  [key: string]: unknown;
}

export interface BannerItem {
  id: number;
  pic: string;
  title?: string;
  url?: string;
  [key: string]: unknown;
}

export interface SongStyleItem {
  id: number;
  name: string;
}

export interface SingerStyleItem {
  id: number;
  name: string;
  type: number;
}
