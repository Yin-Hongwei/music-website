/**
 * 与统一响应 body 中 `data` 字段相关的纯函数整形（不含 HTTP）。
 */

export interface PagedResult<T> {
  records: T[];
  page: number;
  size: number;
  total: number;
  hasMore: boolean;
}

export function ensureArray<T>(data: unknown): T[] {
  return Array.isArray(data) ? (data as T[]) : [];
}

export function normalizePagedResult<T>(data: unknown, page: number, size: number): PagedResult<T> {
  if (data && typeof data === "object") {
    const value = data as Record<string, unknown>;
    const records = ensureArray<T>(value.records);
    const total = Number(value.total) || 0;
    const currentPage = Number(value.page) || page;
    const pageSize = Number(value.size) || size;
    return {
      records,
      page: currentPage,
      size: pageSize,
      total,
      hasMore: currentPage * pageSize < total,
    };
  }
  const records = ensureArray<T>(data);
  return {
    records,
    page,
    size,
    total: records.length,
    hasMore: records.length >= size,
  };
}

export function emptyPagedResult<T>(page: number, size: number): PagedResult<T> {
  return { records: [], page, size, total: 0, hasMore: false };
}
