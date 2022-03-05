<template>
  <div class="upload">
    <el-upload
      drag
      :action="uploadUrl()"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处或点击上传</div>
      <template #tip>
        <p class="el-upload__tip">
          只能上传 {{ uploadTypes.join("、") }} 文件, 且不超过10M
        </p>
      </template>
    </el-upload>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  computed,
  getCurrentInstance,
} from "vue";
import { useStore } from "vuex";
import { UploadFilled } from "@element-plus/icons-vue";

export default defineComponent({
  components: {
    UploadFilled,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();

    const imageUrl = ref("")
    const uploadTypes = ref(["jpg", "jpeg", "png", "gif", "bmp", "x-png", "icon"]);
    const BASE_URL = computed(() => store.getters.BASE_URL)
    const userId = computed(() => store.getters.userId)

    function uploadUrl() {
      return `${BASE_URL.value}/user/avatar/update?id=${userId.value}`;
    }

    function beforeAvatarUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        (proxy as any).$message.error("图片大小不可以超过 10MB!");
      } else if (!uploadTypes.value.includes(file.type.replace(/image\//, ""))) {
        (proxy as any).$message.error(`图片只支持 ${uploadTypes.value.join("、")} 格式!`);
      }
    }

    function handleAvatarSuccess(res, file) {
      if (res.code === 1) {
        imageUrl.value = URL.createObjectURL(file.raw);
        proxy.$store.commit("setUserPic", res.avator);
        (proxy as any).$message({
          message: "修改成功",
          type: "success",
        });
      } else {
        (proxy as any).$notify({
          title: "修改失败",
          type: "error",
        });
      }
    }

    return {
      uploadTypes,
      uploadUrl,
      beforeAvatarUpload,
      handleAvatarSuccess,
    }
  },
});
</script>

<style scoped>
.upload {
  width: 100%;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
