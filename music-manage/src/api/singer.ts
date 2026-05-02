import { api } from "@/utils/request";

export function getAllSinger() {
  return api({ url: "singer" });
}

export function setSinger(params: {
  name: string;
  sex: number | string;
  birth: string;
  location: string;
  introduction: string;
}) {
  return api({ method: "post", url: "singer/add", data: params });
}

export function updateSingerMsg(params: {
  id: number | string;
  name: string;
  sex: number | string;
  birth: string;
  location: string;
  introduction: string;
}) {
  return api({ method: "post", url: "singer/update", data: params });
}

export function deleteSinger(id: string | number) {
  return api({ url: `singer/delete?id=${id}` });
}
