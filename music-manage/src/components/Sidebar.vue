<template>
  <div class="sidebar">
    <el-menu
      class="sidebar-el-menu"
      :default-active="onRoutes"
      :collapse="collapse"
      background-color="#324157"
      text-color="#bfcbd9"
      active-text-color="#20a0ff"
      unique-opened
      router
    >
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.index">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{ item.title }}</span>
            </template>
            <el-menu-item
              v-for="(subItem,i) in item.subs"
              :key="i"
              :index="subItem.index"
            >{{ subItem.title }}</el-menu-item>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import bus from '../assets/js/bus'

export default {
  data () {
    return {
      collapse: false,
      items: [
        {
          icon: 'el-icon-document',
          index: 'info',
          title: '系统首页',
          subs: [
            {
              index: 'info',
              title: '系统首页'
            }
          ]
        },
        {
          icon: 'el-icon-document',
          index: 'consumer',
          title: '用户管理',
          subs: [
            {
              index: 'consumer',
              title: '用户信息'
            }
          ]
        },
        {
          icon: 'el-icon-document',
          index: 'song',
          title: '歌曲管理',
          subs: [
            {
              index: 'song',
              title: '歌曲信息'
            }
          ]
        },
        {
          icon: 'el-icon-document',
          index: 'singer',
          title: '歌手管理',
          subs: [
            {
              index: 'singer',
              title: '歌手信息'
            }
          ]
        },
        {
          icon: 'el-icon-document',
          index: 'songList',
          title: '歌单管理',
          subs: [
            {
              index: 'songList',
              title: '歌单信息'
            }
          ]
        }
      ]
    }
  },
  computed: {
    onRoutes () {
      return this.$route.path.replace('/', '')
    }
  },
  created () {
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    bus.$on('collapse', msg => {
      this.collapse = msg
    })
  }
}
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  background-color: #334256;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
  width: 150px;
}
.sidebar > ul {
  height: 100%;
}
</style>
