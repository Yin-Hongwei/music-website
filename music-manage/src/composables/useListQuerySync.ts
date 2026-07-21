import { watch, type WatchSource } from "vue";
import {
  useRoute,
  useRouter,
  type LocationQuery,
  type LocationQueryRaw,
} from "vue-router";

export function queryStr(v: unknown): string {
  if (v === undefined || v === null || v === "") return "";
  return String(Array.isArray(v) ? v[0] : v);
}

export function queryInt(v: unknown, fallback: number): number {
  const n = Number(queryStr(v));
  return Number.isFinite(n) && n > 0 ? n : fallback;
}

export function queryOptionalInt(v: unknown): number | null {
  const s = queryStr(v);
  if (!s) return null;
  const n = Number(s);
  return Number.isFinite(n) ? n : null;
}

function normalizeQuery(
  q: LocationQuery | LocationQueryRaw,
): Record<string, string> {
  const out: Record<string, string> = {};
  for (const [k, v] of Object.entries(q)) {
    if (v === undefined || v === null || v === "") continue;
    out[k] = String(Array.isArray(v) ? v[0] : v);
  }
  return out;
}

function queryEqual(a: LocationQuery, b: LocationQueryRaw): boolean {
  const na = normalizeQuery(a);
  const nb = normalizeQuery(b);
  const keys = new Set([...Object.keys(na), ...Object.keys(nb)]);
  for (const k of keys) {
    if (na[k] !== nb[k]) return false;
  }
  return true;
}

/**
 * 将列表页码 / 筛选同步到当前路由 query（replace，不堆历史栈）。
 * 返回时通过 query 或面包屑携带的 query 即可还原定位。
 */
export function useListQuerySync(
  buildQuery: () => LocationQueryRaw,
  sources: WatchSource[],
) {
  const route = useRoute();
  const router = useRouter();

  function syncListQuery() {
    const next = buildQuery();
    if (queryEqual(route.query, next)) return;
    void router.replace({ query: next });
  }

  watch(sources, syncListQuery);

  return {
    buildListQuery: buildQuery,
    syncListQuery,
  };
}
