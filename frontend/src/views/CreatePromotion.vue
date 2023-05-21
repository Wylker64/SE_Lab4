<template>
  <div class="create-promotion">
    <h3 class="title">创建活动</h3>
    <div class="columns">
      <div class="column is-6 is-offset-3"></div>
      <div class="column is-offset-2">
        <button class="button is-link" @click="go_back()">返回</button>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    name: "CreatePromotion",
    data() {
      return {};
    },
    methods: {
      go_back() {
        this.$router.back();
      },
    },
  };
</script>
