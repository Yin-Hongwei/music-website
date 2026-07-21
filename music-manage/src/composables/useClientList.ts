import { computed, ref, watch, type Ref } from "vue";

/**
 * 客户端列表：全量缓存 + 关键词筛选 + 分页切片 + 多选。
 * 各页列差异大，不抽表格组件；只抽重复的列表状态。
 */
export function useClientList<T extends { id: number | string }>(options?: {
  pageSize?: number;
  match?: (row: T, keyword: string) => boolean;
}) {
  const allRows = ref<T[]>([]) as Ref<T[]>;
  const filteredRows = ref<T[]>([]) as Ref<T[]>;
  const pageSize = ref(options?.pageSize ?? 10);
  const currentPage = ref(1);
  const searchWord = ref("");
  const checkedRowKeys = ref<Array<string | number>>([]);
  /** 从路由回填时避免 searchWord watch 把页码重置为 1 */
  let suppressSearchReset = false;

  const pageRows = computed(() =>
    filteredRows.value.slice(
      (currentPage.value - 1) * pageSize.value,
      currentPage.value * pageSize.value,
    ),
  );

  const defaultMatch = (row: T, keyword: string) =>
    !keyword ||
    String((row as Record<string, unknown>).name ?? "").includes(keyword);

  function filterRows() {
    const q = searchWord.value.trim();
    const match = options?.match ?? defaultMatch;
    filteredRows.value = allRows.value.filter((row) => match(row, q));
  }

  /** 搜索关键词变化时回到第一页 */
  function applySearch() {
    filterRows();
    currentPage.value = 1;
  }

  /** 编辑/删除后刷新：保留当前页；若当前页已空则回退到最后一页 */
  function clampPage() {
    const maxPage = Math.max(
      1,
      Math.ceil(filteredRows.value.length / pageSize.value) || 1,
    );
    if (currentPage.value > maxPage) {
      currentPage.value = maxPage;
    }
  }

  watch(searchWord, () => {
    if (suppressSearchReset) {
      filterRows();
      return;
    }
    applySearch();
  });

  function setRows(rows: T[]) {
    allRows.value = [...rows];
    filterRows();
    clampPage();
    checkedRowKeys.value = [];
  }

  /** 从路由等外部状态回填，不重置页码 */
  function hydrate(state: {
    searchWord?: string;
    page?: number;
    pageSize?: number;
  }) {
    suppressSearchReset = true;
    if (state.pageSize != null && state.pageSize > 0) {
      pageSize.value = state.pageSize;
    }
    if (state.searchWord != null) {
      searchWord.value = state.searchWord;
    }
    filterRows();
    if (state.page != null && state.page > 0) {
      currentPage.value = state.page;
    }
    suppressSearchReset = false;
  }

  function handlePageChange(page: number) {
    currentPage.value = page;
  }

  function handlePageSizeChange(size: number) {
    pageSize.value = size;
    currentPage.value = 1;
  }

  return {
    allRows,
    filteredRows,
    pageRows,
    pageSize,
    currentPage,
    searchWord,
    checkedRowKeys,
    setRows,
    filterRows,
    applySearch,
    hydrate,
    handlePageChange,
    handlePageSizeChange,
  };
}
