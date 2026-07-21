<template>
  <n-modal
    v-model:show="visible"
    preset="card"
    :title="title"
    style="width: 560px"
    :mask-closable="false"
    @after-enter="initCropper"
    @after-leave="destroyCropper"
  >
    <div class="crop-stage">
      <img ref="imgRef" class="crop-stage__img" :src="imageSrc" alt="裁剪预览" />
    </div>
    <n-space class="crop-tools" align="center" :wrap="false">
      <n-button quaternary size="small" @click="rotate(-90)">左转</n-button>
      <n-button quaternary size="small" @click="rotate(90)">右转</n-button>
      <n-button quaternary size="small" @click="zoom(-0.1)">缩小</n-button>
      <n-button quaternary size="small" @click="zoom(0.1)">放大</n-button>
      <n-button quaternary size="small" @click="resetCropper">重置</n-button>
    </n-space>
    <template #footer>
      <n-space justify="end">
        <n-button @click="close">取 消</n-button>
        <n-button type="primary" :loading="confirming" @click="confirm"
          >确 定</n-button
        >
      </n-space>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { nextTick, ref, watch } from "vue";
import Cropper from "cropperjs";
import "cropperjs/dist/cropper.css";
import { message } from "@/utils/message";

const props = withDefaults(
  defineProps<{
    show: boolean;
    imageSrc: string;
    /** 输出文件名（含扩展名） */
    fileName?: string;
    title?: string;
    /** 裁剪宽高比，歌手头像默认 1:1 */
    aspectRatio?: number;
    /** 输出最大边长，按原图裁剪区域导出，超过此值才等比缩小 */
    maxOutputSize?: number;
  }>(),
  {
    fileName: "avatar.jpg",
    title: "裁剪图片",
    aspectRatio: 1,
    maxOutputSize: 2048,
  },
);

const emit = defineEmits<{
  (e: "update:show", value: boolean): void;
  (e: "confirm", file: File): void;
  (e: "cancel"): void;
}>();

const imgRef = ref<HTMLImageElement | null>(null);
const confirming = ref(false);
/** 确认成功关闭时不触发 cancel，避免父级误清草稿 */
let closingByConfirm = false;
let cropper: Cropper | null = null;

const visible = ref(props.show);

watch(
  () => props.show,
  (v) => {
    visible.value = v;
  },
);

watch(visible, (v) => {
  if (v !== props.show) emit("update:show", v);
  if (!v) {
    if (closingByConfirm) {
      closingByConfirm = false;
      return;
    }
    emit("cancel");
  }
});

function destroyCropper() {
  cropper?.destroy();
  cropper = null;
}

async function initCropper() {
  destroyCropper();
  await nextTick();
  const img = imgRef.value;
  if (!img || !props.imageSrc) return;

  // 图片可能尚未 decode，等 load 再初始化更稳
  if (!img.complete) {
    await new Promise<void>((resolve, reject) => {
      img.onload = () => resolve();
      img.onerror = () => reject(new Error("图片加载失败"));
    }).catch(() => {
      message.error("图片加载失败，请重新选择");
    });
  }

  cropper = new Cropper(img, {
    aspectRatio: props.aspectRatio,
    viewMode: 1,
    dragMode: "move",
    autoCropArea: 1,
    responsive: true,
    background: false,
    movable: true,
    zoomable: true,
    rotatable: true,
    scalable: false,
    guides: true,
    center: true,
    highlight: false,
    cropBoxMovable: true,
    cropBoxResizable: true,
    checkOrientation: true,
  });
}

function rotate(degree: number) {
  cropper?.rotate(degree);
}

function zoom(ratio: number) {
  cropper?.zoom(ratio);
}

function resetCropper() {
  cropper?.reset();
}

function close() {
  visible.value = false;
}

function canvasToFile(canvas: HTMLCanvasElement, fileName: string): Promise<File> {
  const lower = fileName.toLowerCase();
  const isPng = lower.endsWith(".png");
  const mime = isPng ? "image/png" : "image/jpeg";
  const quality = isPng ? undefined : 0.95;

  return new Promise((resolve, reject) => {
    canvas.toBlob(
      (blob) => {
        if (!blob) {
          reject(new Error("裁剪失败"));
          return;
        }
        resolve(new File([blob], fileName, { type: mime }));
      },
      mime,
      quality,
    );
  });
}

async function confirm() {
  if (!cropper) {
    message.error("裁剪器未就绪，请稍候再试");
    return;
  }
  confirming.value = true;
  try {
    const isPng = props.fileName.toLowerCase().endsWith(".png");
    const canvas = cropper.getCroppedCanvas({
      maxWidth: props.maxOutputSize,
      maxHeight: props.maxOutputSize,
      fillColor: isPng ? "transparent" : "#ffffff",
      imageSmoothingEnabled: true,
      imageSmoothingQuality: "high",
      rounded: true,
    });
    if (!canvas) {
      message.error("裁剪失败，请重试");
      return;
    }
    const file = await canvasToFile(canvas, props.fileName);
    closingByConfirm = true;
    emit("confirm", file);
    visible.value = false;
  } catch {
    message.error("裁剪失败，请重试");
  } finally {
    confirming.value = false;
  }
}
</script>

<style scoped>
.crop-stage {
  width: 100%;
  height: 360px;
  background: #1a1a1a;
  border-radius: 8px;
  overflow: hidden;
}

.crop-stage__img {
  display: block;
  max-width: 100%;
}

.crop-tools {
  margin-top: 12px;
}
</style>
