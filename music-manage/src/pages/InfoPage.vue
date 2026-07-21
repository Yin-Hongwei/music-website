<template>
  <div class="info-page">
    <n-grid cols="2 m:4" :x-gap="16" :y-gap="16" responsive="screen">
      <n-gi v-for="card in statCards" :key="card.label">
        <n-card size="small" :bordered="false" class="stat-card">
          <div class="card-content">
            <div class="card-icon-wrap">
              <component :is="card.icon" class="card-icon" />
            </div>
            <div class="card-text">
              <div class="card-num">{{ card.value }}</div>
              <div class="card-label">{{ card.label }}</div>
            </div>
          </div>
        </n-card>
      </n-gi>
    </n-grid>

    <n-grid class="chart-row" cols="1 m:2" :x-gap="16" :y-gap="16" responsive="screen">
      <n-gi>
        <n-card title="用户性别比例" size="small" :bordered="false">
          <div ref="userSexRef" class="chart-el" />
        </n-card>
      </n-gi>
      <n-gi>
        <n-card title="歌单风格" size="small" :bordered="false">
          <div ref="songStyleRef" class="chart-el" />
        </n-card>
      </n-gi>
      <n-gi>
        <n-card title="歌手类型比例" size="small" :bordered="false">
          <div ref="singerKindRef" class="chart-el" />
        </n-card>
      </n-gi>
      <n-gi>
        <n-card title="歌手地区" size="small" :bordered="false">
          <div ref="countryRef" class="chart-el" />
        </n-card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from "vue";
import * as echarts from "echarts";
import { FileText, Headphones, Mic2, User } from "lucide-vue-next";
import { getAllSinger, getAllSong, getAllUser, getSongList } from "@/api";

interface SexItem {
  sex?: number | string;
}

interface SingerKindItem {
  kind?: number | string;
}

interface StyleItem {
  style?: string;
  styles?: Array<{ id?: number; name?: string }>;
}

interface LocationItem {
  location?: string;
}

const SEX_LABEL: Record<string, string> = {
  "0": "女",
  "1": "男",
  "2": "保密",
};

const SINGER_KIND_LABEL: Record<string, string> = {
  "0": "个人",
  "1": "组合",
  "2": "其他",
};

const STYLE_TAGS = ["华语", "粤语", "欧美", "日韩", "轻音乐", "BGM", "乐器", "流行"];

const userCount = ref(0);
const songCount = ref(0);
const singerCount = ref(0);
const songListCount = ref(0);

const userSexRef = ref<HTMLElement | null>(null);
const songStyleRef = ref<HTMLElement | null>(null);
const singerKindRef = ref<HTMLElement | null>(null);
const countryRef = ref<HTMLElement | null>(null);

const charts: echarts.ECharts[] = [];

const statCards = computed(() => [
  { icon: User, value: userCount.value, label: "用户总数" },
  { icon: Headphones, value: songCount.value, label: "歌曲总数" },
  { icon: Mic2, value: singerCount.value, label: "歌手数量" },
  { icon: FileText, value: songListCount.value, label: "歌单数量" },
]);

function asList<T>(data: unknown): T[] {
  return Array.isArray(data) ? (data as T[]) : [];
}

function aggregateSex(list: SexItem[]) {
  const counts = new Map<string, number>();
  for (const item of list) {
    const key = SEX_LABEL[String(item.sex)] || "未知";
    counts.set(key, (counts.get(key) || 0) + 1);
  }
  return Array.from(counts.entries()).map(([name, value]) => ({ name, value }));
}

function aggregateSingerKind(list: SingerKindItem[]) {
  const counts = new Map<string, number>();
  for (const item of list) {
    const key = SINGER_KIND_LABEL[String(item.kind)] || "未知";
    counts.set(key, (counts.get(key) || 0) + 1);
  }
  return Array.from(counts.entries()).map(([name, value]) => ({ name, value }));
}

function styleNamesOf(item: StyleItem): string[] {
  if (Array.isArray(item.styles) && item.styles.length) {
    return item.styles.map((s) => (s?.name || "").trim()).filter(Boolean);
  }
  const raw = (item.style || "").trim();
  if (!raw) return [];
  return raw.split("-").map((t) => t.trim()).filter(Boolean);
}

function aggregateStyles(list: StyleItem[]) {
  const counts = new Map<string, number>(STYLE_TAGS.map((tag) => [tag, 0]));
  let other = 0;
  for (const item of list) {
    const tags = styleNamesOf(item);
    if (!tags.length) {
      other += 1;
      continue;
    }
    let matched = false;
    for (const tag of tags) {
      if (counts.has(tag)) {
        counts.set(tag, (counts.get(tag) || 0) + 1);
        matched = true;
      }
    }
    if (!matched) other += 1;
  }
  const categories = [...STYLE_TAGS];
  const values = STYLE_TAGS.map((tag) => counts.get(tag) || 0);
  if (other > 0) {
    categories.push("其他");
    values.push(other);
  }
  return { categories, values };
}

function normalizeLocation(location?: string) {
  const raw = (location || "").trim() || "未知";
  if (raw.startsWith("中国")) return "中国";
  return raw;
}

function aggregateLocations(list: LocationItem[], topN = 8) {
  const counts = new Map<string, number>();
  for (const item of list) {
    const key = normalizeLocation(item.location);
    counts.set(key, (counts.get(key) || 0) + 1);
  }
  const sorted = Array.from(counts.entries()).sort((a, b) => b[1] - a[1]);
  const top = sorted.slice(0, topN);
  const restTotal = sorted.slice(topN).reduce((sum, item) => sum + item[1], 0);
  const categories = top.map((item) => item[0]);
  const values = top.map((item) => item[1]);
  if (restTotal > 0) {
    categories.push("其他");
    values.push(restTotal);
  }
  return { categories, values };
}

function pieOption(data: { name: string; value: number }[]): echarts.EChartsOption {
  return {
    tooltip: { trigger: "item", formatter: "{b}: {c} ({d}%)" },
    legend: { bottom: 0, left: "center" },
    series: [
      {
        type: "pie",
        radius: ["36%", "62%"],
        center: ["50%", "45%"],
        avoidLabelOverlap: true,
        itemStyle: { borderRadius: 4, borderColor: "#fff", borderWidth: 2 },
        label: { formatter: "{b}\n{d}%" },
        data,
      },
    ],
  };
}

function barOption(categories: string[], values: number[]): echarts.EChartsOption {
  return {
    tooltip: { trigger: "axis" },
    grid: { left: 40, right: 16, top: 24, bottom: 48 },
    xAxis: {
      type: "category",
      data: categories,
      axisLabel: { interval: 0, rotate: categories.length > 6 ? 30 : 0 },
    },
    yAxis: { type: "value", minInterval: 1 },
    series: [
      {
        type: "bar",
        data: values,
        barMaxWidth: 36,
        itemStyle: { color: "#18a058", borderRadius: [4, 4, 0, 0] },
      },
    ],
  };
}

function renderChart(el: HTMLElement | null, option: echarts.EChartsOption) {
  if (!el) return;
  const chart = echarts.init(el);
  chart.setOption(option);
  charts.push(chart);
}

function handleResize() {
  charts.forEach((chart) => chart.resize());
}

async function loadDashboard() {
  const [userRes, songRes, songListRes, singerRes] = await Promise.all([
    getAllUser(),
    getAllSong(),
    getSongList(),
    getAllSinger(),
  ]);

  const users = asList<SexItem>(userRes.data);
  const songs = asList(songRes.data);
  const songLists = asList<StyleItem>(songListRes.data);
  const singers = asList<SingerKindItem & LocationItem>(singerRes.data);

  userCount.value = users.length;
  songCount.value = songs.length;
  songListCount.value = songLists.length;
  singerCount.value = singers.length;

  await nextTick();

  const styleStats = aggregateStyles(songLists);
  const locationStats = aggregateLocations(singers);

  renderChart(userSexRef.value, pieOption(aggregateSex(users)));
  renderChart(songStyleRef.value, barOption(styleStats.categories, styleStats.values));
  renderChart(singerKindRef.value, pieOption(aggregateSingerKind(singers)));
  renderChart(countryRef.value, barOption(locationStats.categories, locationStats.values));

  requestAnimationFrame(() => handleResize());
}

onMounted(async () => {
  await loadDashboard();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize);
  charts.forEach((chart) => chart.dispose());
  charts.length = 0;
});
</script>

<style scoped>
.info-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-bottom: 12px;
}

.stat-card {
  background: #fff;
}

.card-content {
  display: flex;
  align-items: center;
  gap: 16px;
  min-height: 72px;
}

.card-icon-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 52px;
  height: 52px;
  border-radius: 12px;
  background: rgba(24, 160, 88, 0.1);
  color: #18a058;
  flex-shrink: 0;
}

.card-icon {
  width: 26px;
  height: 26px;
}

.card-text {
  min-width: 0;
}

.card-num {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
  color: #1f2225;
}

.card-label {
  margin-top: 4px;
  font-size: 14px;
  color: #909399;
}

.chart-el {
  width: 100%;
  height: 260px;
}
</style>
