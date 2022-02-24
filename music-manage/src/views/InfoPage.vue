<template>
  <div>
    <el-row :gutter="20" class="mgb20">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-1">
            <div class="grid-cont-right">
              <div class="grid-num">{{ userCount }}</div>
              <div>用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-2">
            <div class="grid-cont-right">
              <div class="grid-num">{{ songCount }}</div>
              <div>歌曲总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-3">
            <div class="grid-cont-right">
              <div class="grid-num">{{ singerCount }}</div>
              <div>歌手数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-4">
            <div class="grid-cont-right">
              <div class="grid-num">{{ songListCount }}</div>
              <div>歌单数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <h3 style="margin-bottom: 20px">用户性别比例</h3>
        <div class="cav-info" style="background-color: white">
          <div class="container" ref="userSex"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <h3 style="margin-bottom: 20px">歌曲类型分布</h3>
        <div class="cav-info" style="background-color: white">
          <div class="container" ref="songStyle"></div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <h3 style="margin: 20px 0">歌手性别比例</h3>
        <div class="cav-info" style="background-color: white">
          <div class="container" ref="singerSex"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <h3 style="margin: 20px 0">歌手国籍分布</h3>
        <div class="cav-info" style="background-color: white">
          <div class="container" ref="country"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, reactive, getCurrentInstance } from "vue";
import * as echarts from "echarts";
import { HttpManager } from "@/api/index";

const ctx = getCurrentInstance().ctx;
const userCount = ref(0);
const songCount = ref(0);
const singerCount = ref(0);
const songListCount = ref(0);
const userSex = reactive({
  series: [
    {
      type: "pie",
      data: [
        {
          value: 0,
          name: "男",
        },
        {
          value: 0,
          name: "女",
        },
      ],
    },
  ],
});
const songStyle = reactive({
  xAxis: {
    type: "category",
    data: ["华语", "粤语", "欧美", "日韩", "BGM", "轻音乐", "乐器"],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [0, 0, 0, 0, 0, 0, 0],
      type: "bar",
      barWidth: '20%'
    },
  ],
});
const singerSex = reactive({
  series: [
    {
      type: "pie",
      data: [
        {
          value: 0,
          name: "男",
        },
        {
          value: 0,
          name: "女",
        },
      ],
    },
  ],
});
const country = reactive({
  xAxis: {
    type: "category",
    data: [
      "中国",
      "韩国",
      "意大利",
      "新加坡",
      "美国",
      // "马来西亚",
      "西班牙",
      "日本",
    ],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [0, 0, 0, 0, 0, 0, 0, 0],
      type: "bar",
      barWidth: '20%'
    },
  ],
});

function setSex(sex, arr) {
  let value = 0;
  const name = sex === 0 ? "男" : "女";
  for (let item of arr) {
    if (sex === item.sex) {
      value++;
    }
  }
  return { value, name };
}
HttpManager.getAllUser().then((res) => {
  userCount.value = res.length;
  userSex.series[0].data.push(setSex(0, res));
  userSex.series[0].data.push(setSex(1, res));

  const userSexChart = echarts.init(ctx.$refs.userSex);
  userSexChart.setOption(userSex);
});

HttpManager.getAllSong().then((res) => {
  songCount.value = res.length;
});
HttpManager.getSongList().then((res) => {
  songListCount.value = res.length;
  for (let item of res) {
    for (let i = 0; i < songStyle.xAxis.data.length; i++) {
      if (item.style.includes(songStyle.xAxis.data[i])) {
        songStyle.series[0].data[i]++;
      }
    }
  }
  const songStyleChart = echarts.init(ctx.$refs.songStyle);
  songStyleChart.setOption(songStyle);
});

HttpManager.getAllSinger().then((res) => {
  singerCount.value = res.length;
  singerSex.series[0].data.push(setSex(0, res));
  singerSex.series[0].data.push(setSex(1, res));
  const singerSexChart = echarts.init(ctx.$refs.singerSex);
  singerSexChart.setOption(singerSex);

  for (let item of res) {
    for (let i = 0; i < country.xAxis.data.length; i++) {
      if (item.location.includes(country.xAxis.data[i])) {
        country.series[0].data[i]++;
      }
    }
  }
  const countryChart = echarts.init(ctx.$refs.country);
  countryChart.setOption(country);
});
</script>
<script>
import { mixin } from "@/mixins";
export default {
  mixins: [mixin],
};
</script>

<style scoped>
.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.cav-info {
  border-radius: 6px;
  overflow: hidden;
}
.container {
  height: 300px;
}
</style>
