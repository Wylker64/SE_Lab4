<template>
  <h3 class="title">活动页面</h3>
  <div class="columns">
    <div class="column is-10"></div>
    <div class="column is-offset-1">
      <button v-if="identity == 2" class="button is-link" @click="push_create">创建活动</button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "promotion",
    data() {
      return {
        identity: -1,
      };
    },
    mounted() {
      this.identity = localStorage.getItem("identity");
    },
    methods: {
      push_create() {
        this.$router.push({ path: "/create-promotion" });
      },
    },
  };
</script>
