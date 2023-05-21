<template>
  <div class="page-shops">
    <div class="columns">
      <div class="column is-10">
        <h1 class="title">我的商店</h1>
      </div>
      <div class="column">
        <button class="button is-link" @click="push_open">新增店铺</button>
      </div>
    </div>
    <form @submit.prevent="getShops">
      <div class="column is-2">
        <div class="field">
          <label>页码/共{{ total_pages }}页</label>
          <!-- <div class="column is-7"> -->
          <div class="control">
            <input type="text" class="input" v-model="page_num" />
          </div>
          <!-- </div> -->
        </div>
        <div class="field">
          <div class="control">
            <button class="button is-dark">搜索</button>
          </div>
        </div>
      </div>
    </form>
    <div class="columns is-multiline">
      <ShopBox v-for="shop in shops" v-bind:key="shop.id" v-bind:shop="shop" v-bind:type="type" />
      <!-- 此处type表示此刻是商家在他自己的界面查看自己的商店，为了复用shopbox，故传参数来指明具体使用情况 -->
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import ShopBox from "@/components/ShopBox.vue";
  // import { toast } from "bulma-toast";
  // import { title } from "process";
  export default {
    name: "MyShops",
    data() {
      return {
        page_num: 1,
        shops: [],
        type: "1",
        total_pages: 0,
        errors: [],
      };
    },
    mounted() {
      document.title = "我的商店";
      this.page_num = 1;
      // localStorage.setItem("authorize", JSON.stringify({ username: "wylker", id: 47 }));
      // this.$store.commit("initializeStore");
      // console.log(this.$store.getters.getToken);
      this.getShops();
    },
    components: {
      ShopBox,
    },
    methods: {
      getShops() {
        this.errors = [];
        if (this.page_num <= 0) {
          this.errors.push("页码不可小于0，请重新输入");
          return;
        }
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          page_num: this.page_num,
          review: JSON.parse(localStorage.getItem("authorize") || "{}").username == "admin",
        };
        axios
          .post("/api/shop/vendor", formData)
          .then((response) => {
            console.log(response);
            this.shops = JSON.parse(JSON.stringify(response.data.shops));
            this.total_pages = response.data.total_pages;
            console.log(this.shops);
          })
          .catch((error) => {
            console.log(error);
          });
      },
      push_open() {
        this.$router.push("/open");
      },
    },
  };
</script>
