<template>
  <svg class="icon" aria-hidden="true">
    <use :xlink:href="icon"></use>
  </svg>
</template>

<script>
export default {
  props: {
    icon: String,
  },
};
</script>

<style></style>
