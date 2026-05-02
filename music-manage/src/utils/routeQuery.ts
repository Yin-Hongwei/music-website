/** Vue Router `query` 可能是 `string | string[]`，拼进 URL 时取单值 */
export function routeQueryValue(value: unknown): string {
  if (value == null) return "";
  if (Array.isArray(value)) return routeQueryValue(value[0]);
  return String(value);
}
