import { api, getBaseURL, type ApiResponse } from "@/utils/request";
import { jsonRequestConfig } from "@/api/types";

export async function fetchUserById(id: string | number): Promise<ApiResponse> {
  return await api({ url: `user/detail?id=${id}` });
}

export async function fetchCollectionByUserId(userId: string | number): Promise<ApiResponse> {
  return await api({ url: `collection/detail?userId=${userId}` });
}

export async function fetchDeleteCollection(
  userId: string | number,
  songId: string | number,
): Promise<ApiResponse> {
  return await api({
    method: "delete",
    url: `collection/delete?userId=${userId}&songId=${songId}`,
  });
}

export async function fetchSetCollection(payload: {
  userId: string | number;
  type: string;
  songId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/add",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchCollectionStatus(payload: {
  userId: string | number;
  type: string;
  songId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/status",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchDeleteSongListCollection(
  userId: string | number,
  songListId: string | number,
): Promise<ApiResponse> {
  return await api({
    method: "delete",
    url: `collection/songList/delete?userId=${userId}&songListId=${songListId}`,
  });
}

export async function fetchSongListCollectionStatus(payload: {
  userId: string | number;
  songListId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/songList/status",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchSetSongListCollection(payload: {
  userId: string | number;
  songListId: string | number;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "collection/songList/add",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchSongListCollectors(songListId: string | number): Promise<ApiResponse> {
  return await api({ url: `collection/songList/collectors?songListId=${songListId}` });
}

export async function fetchDeleteUser(id: string | number): Promise<ApiResponse> {
  return await api({ url: `user/delete?id=${id}` });
}

export async function fetchUpdateUserMsg(payload: {
  id: string | number;
  username: string;
  sex: string | number;
  phoneNum: string;
  email: string;
  birth: string | Date;
  introduction: string;
  location: string;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "user/update",
    data: payload,
    config: jsonRequestConfig,
  });
}

export async function fetchUpdateUserPassword(payload: {
  id: string | number;
  username: string;
  oldPassword: string;
  password: string;
}): Promise<ApiResponse> {
  return await api({
    method: "post",
    url: "user/updatePassword",
    data: payload,
    config: jsonRequestConfig,
  });
}

export function fetchUploadUrl(userId: string | number): string {
  return `${getBaseURL()}/user/avatar/update?id=${userId}`;
}
