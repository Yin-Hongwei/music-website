<template>
  <div>
    <div class='comment'>
      <h2>
        <span>评论</span>
        <span class='part__tit_desc'>共 {{commentList.length}} 条评论</span>
      </h2>
      <div class='comment-msg'>
        <el-input
          class='comment-input'
          type='textarea'
          placeholder='期待您的精彩评论...'
          :rows='2'
          v-model='textarea'>
        </el-input>
      </div>
      <el-button type='primary' class='sub-btn' @click='submitComment()'>发表评论</el-button>
    </div>
    <ul class='popular' v-for='(item, index) in commentList' :key='index'>
      <li>
        <div class='popular-img'>
          <img :src='attachImageUrl(userPicList[index])' alt=''>
        </div>
        <div class='popular-msg'>
          <ul>
            <li class='name'>{{userNameList[index]}}</li>
            <li class='time'>{{item.createTime}}</li>
            <li class='content'>{{item.content}}</li>
          </ul>
        </div>
        <div class='up' ref='up' @click='setSupport(item.id, item.up, index)'>
          <yin-icon :icon="iconList.ZAN"></yin-icon>
          {{item.up}}
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  getCurrentInstance,
  ref,
  toRefs,
  computed,
  watch,
  reactive,
  onMounted,
} from "vue";
import { useStore } from "vuex";
import YinIcon from '@/components/layouts/YinIcon.vue'
import mixin from '@/mixins/mixin'
import { HttpManager } from '@/api'
import { ICON } from '@/enums'

export default defineComponent({
  components: {
    YinIcon
  },
  props: {
    playId: Number || String, // 歌曲ID或歌单ID
    type: Number // 歌单（1）/歌曲（0）
  },
  setup(props) {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { checkStatus, attachImageUrl } = mixin();

    const { playId, type } = toRefs(props)
    const commentList = ref([]); // 存放评论内容
    const userPicList = ref([]); // 保存评论用户头像
    const userNameList = ref([]); // 保存评论用户名字
    const textarea = ref(''); // 存放输入内容
    const iconList = reactive({
      ZAN: ICON.ZAN
    });
    const userId = computed(() => store.getters.userId);
    const songId = computed(() => store.getters.songId);
    watch(songId, () => {
      getComment();
    });

    // 获取所有评论
    async function getComment () {
      try {
        const result = await HttpManager.getAllComment(type.value, playId.value) as any[]
        commentList.value = result
        for (const item of result) {
          // 获取评论用户的昵称和头像
          const resultUser = await HttpManager.getUserOfId(item.userId)
          userPicList.value.push(resultUser[0].avator)
          userNameList.value.push(resultUser[0].username)
        }
      } catch (error) {
        console.error(error)
      }
    }

    // 提交评论
    async function submitComment () {
      if (!checkStatus()) return

      // 0 代表歌曲， 1 代表歌单
      const params = new URLSearchParams()
      if (type.value === 1) {
        params.append('songListId', `${playId.value}`)
      } else if (type.value === 0) {
        params.append('songId', `${playId.value}`)
      }
      params.append('userId', userId.value)
      params.append('type', `${type.value}`)
      params.append('content', textarea.value)

      const result = await HttpManager.setComment(params) as { code : number }
      if (result.code === 1) {
        textarea.value = "";
        (proxy as any).$notify({
          title: '评论成功',
          type: 'success'
        })
        await getComment()
      } else {
        (proxy as any).$notify({
          title: '评论失败',
          type: 'error'
        })
      }
    }

    // 点赞
    async function setSupport (id, up, index) {
      if (!checkStatus()) return

      const params = new URLSearchParams()
      params.append('id', id)
      params.append('up', up + 1)
      
      const result = await HttpManager.setSupport(params) as { code : number }
      if (result.code === 1) {
        proxy.$refs.up[index].children[0].style.color = '#2796dd'
        await getComment()
      }
    }

    onMounted(() => {
      getComment()
    });

    return {
      commentList,
      userPicList,
      userNameList,
      textarea,
      iconList,
      attachImageUrl,
      submitComment,
      setSupport,
    }
  },
})
</script>

<style lang='scss' scoped>
@import '@/assets/css/comment.scss';
</style>
