<template>
  <n-modal
    v-model:show="visible"
    preset="dialog"
    title="提示"
    positive-text="确 定"
    negative-text="取 消"
    @positive-click="onConfirm"
    @negative-click="onCancel"
    @close="onCancel"
  >
    删除不可恢复，是否确定删除？
  </n-modal>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  delVisible: boolean;
}>();

const emit = defineEmits<{
  (e: "cancelRow", value: boolean): void;
  (e: "confirm"): void;
}>();

const visible = computed({
  get: () => props.delVisible,
  set: (v: boolean) => {
    if (!v) emit("cancelRow", false);
  },
});

function onCancel() {
  emit("cancelRow", false);
}

function onConfirm() {
  emit("confirm");
}
</script>
