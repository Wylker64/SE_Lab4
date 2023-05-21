<template>
  <div class="home">
    <section class="hero is-medium is-dark mb-6">
      <div class="hero-body has-text-centered">
        <p class="title mb-6">
          欢迎！
        </p>
        <p class="subtitle">
          我们当前有这些商店：
        </p>
      </div>
    </section>
    <div class="columns">
      <div class="column">
        <form @submit.prevent="getPro">
          <div class="column is-4">
            <div class="field">
              <label>页面/总页数{{ total_pages }}页</label>
              <div class="control">
                <input type="text" class="input" v-model="page_num" />
              </div>
            </div>
            <div class="field">
              <div class="control">
                <button class="button is-dark" @click="getPro">搜索！</button>
              </div>
              <div class="notification is-danger" v-if="errors.length">
                <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="column"></div>
    </div>
    <div class="columns is-multiline">
      <ShopBox v-for="shop in shops" v-bind:key="shop.id" v-bind:shop="shop" :type="type" />
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import ShopBox from "@/components/ShopBox";
  export default {
    name: "Review",
    data() {
      return {
        shops: [],
        page_num: 1,
        total_pages: 0,
        errors: [],
        type: "2",
        identity: -1,
      };
    },
    components: {
      ShopBox,
    },
    mounted() {
      document.title = "Home";
      this.way = this.$route.params.way;
      console.log("login");
      console.log(this.$route.params);
      console.log(this.way);
      if (this.way == "login") location.reload();
      this.page_num = 1;
      this.identity = localStorage.getItem("identity");
      console.log(this.identity);
      this.getPro();
    },
    methods: {
      getPro() {
        this.errors = [];
        if (this.page_num <= 0) {
          this.errors.push("页码不可小于0，请重新输入");
          return;
        }
        this.$store.commit("setIsLoading", true);
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          page_num: this.page_num,
        };
        axios
          .post("/api/shop", formData)
          .then((response) => {
            this.shops = response.data.shops;
            this.total_pages = response.data.total_pages;
            console.log(this.shops);
          })
          .catch((error) => {
            console.log(error);
          });

        this.$store.commit("setIsLoading", false);
      },
    },
  };
</script>
