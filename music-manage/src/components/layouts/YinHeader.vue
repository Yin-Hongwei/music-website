<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChage">
      <el-icon><expand /></el-icon>
    </div>
    <div class="logo">{{nusicName}}</div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 用户头像 -->
        <div class="user-avator">
          <img :src="userPic" />
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            {{username}}
            <i class="el-icon-caret-bottom"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="loginout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import { Expand } from '@element-plus/icons-vue'
import emitter from '../../utils/emitter'
import { SIGN_IN, MUSICNAME } from '../../enums'
import { mixin } from '../../mixins'

export default {
  mixins: [mixin],
  components: {
    Expand
  },
  data () {
    return {
      collapse: true,
      username: 'admin',
      userPic: require('@/assets/images/user.jpg'),
      nusicName: MUSICNAME
    }
  },
  mounted () {
    if (document.body.clientWidth < 1500) {
      this.collapseChage()
    }
  },
  methods: {
    // 用户名下拉菜单选择事件
    handleCommand (command) {
      if (command === 'loginout') {
        this.routerManager(SIGN_IN, { path: SIGN_IN })
      }
    },
    // 侧边栏折叠
    collapseChage () {
      this.collapse = !this.collapse
      emitter.emit('collapse', this.collapse)
    }
  }
}
</script>
<style scoped>
.header {
  position: absolute;
  z-index: 100;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #B0B3B2;
  background: #1A1B1C;
  box-shadow: 0px 0px 8px 2px rgba(0, 0, 0, 0.3);
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}

.header .logo {
  float: left;
  width: 250px;
  font-family: 26px;
  line-height: 70px;
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}

.user-name {
  margin-left: 10px;
}

.user-avator {
  margin-left: 20px;
}

.user-avator img {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.el-dropdown-link {
  color: #B0B3B2;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}
</style>
