import { computed, watch } from "vue";
import { useSearchStore } from "@/store/search";

function parseKeyword(value: unknown): string {
  if (Array.isArray(value)) return parseKeyword(value[0]);
  if (typeof value !== "string") return "";
  return value.trim();
}

export function useSearchKeyword(onKeywordChange: (keyword: string) => void) {
  const searchStore = useSearchStore();

  const keyword = computed(() => {
    return parseKeyword(searchStore.keyword);
  });

  watch(
    keyword,
    (value) => {
      onKeywordChange(value);
    },
    { immediate: true },
  );
}
