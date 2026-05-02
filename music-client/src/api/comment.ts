import { api, type ApiResponse } from "@/utils/request";
import { jsonRequestConfig } from "@/api/types";

export async function fetchAllComment(type: number, id: string | number): Promise<ApiResponse> {
  const url = type === 1 ? `comment/songList/detail?songListId=${id}` : `comment/song/detail?songId=${id}`;
  return await api({ url });
}

export async function fetchSetComment(payload: {
  userId: string | number;
  content: string;
  songId: string | null;
  songListId: string | null;
  nowType: number | null;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "comment/add",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchDeleteComment(id: string | number): Promise<ApiResponse> {
  return await api({ url: `comment/delete?id=${id}` });
}

export async function fetchSetSupport(payload: { id: string | number; likeCount: number }): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "comment/like",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchTestAlreadySupport(payload: {
  commentId: string | number;
  userId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "userSupport/test",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchDeleteUserSupport(payload: {
  commentId: string | number;
  userId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "userSupport/delete",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchInsertUserSupport(payload: {
  commentId: string | number;
  userId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "userSupport/insert",
    data: payload,
    config: jsonRequestConfig,
  });
}
