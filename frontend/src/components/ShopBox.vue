<template>
  <div class="column is-3">
    <div class="box">
      <h3 v-if="is_admin" class="is-size-4">ID: {{ shop.id }}</h3>
      <h3 class="is-size-4">名称：{{ shop.name }}</h3>
      <p class="is-size-6 has-text-grey">类型：{{ shop.type }}</p>
      <p v-if="is_user || is_admin" class="is-size-6 has-text-grey">描述：{{ shop.description }}</p>
      <p v-if="is_admin" class="is-size-6 has-text-grey">地址：{{ shop.address }}</p>
      <!-- 若从/my-shops点击则跳转到商店管理界面/warehouse -->
      <button v-if="is_seller && shop.approved" @click="push_warehouse" class="button is-dark mt-4">
        前往
      </button>
      <button v-if="is_seller && !shop.approved" @click="push_warehouse" class="button is-dark mt-4" disabled>
        前往
      </button>
      <!-- 若从/home点击则跳转到商店展示界面/shop -->
      <button v-if="is_user" @click="push_shop" class="button is-dark mt-4">前往</button>
      <button v-if="is_admin" @click="release(true)" class="button mt-4">
        通过申请
      </button>
      <button v-if="is_admin" @click="release(false)" class="button mt-4">
        拒绝申请
      </button>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  // import { numberLiteralTypeAnnotation } from "@babel/types";
  export default {
    name: "ShopBox",
    data() {
      return {
        // movie: this.$store.state.shop,
        errors: [],
        approved: false,
      };
    },
    methods: {
      push_shop() {
        var url = "/shop/" + this.shop.id;
        this.$router.push({ path: url });
        console.log(this.shop);
      },
      push_warehouse() {
        var url = "/warehouse/" + this.shop.id;
        this.$router.push({ path: url });
        console.log(this.shop);
      },
      release(approved) {
        this.errors = [];
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          shop_id: this.shop.id,
          approved: approved,
        };
        console.log(this.review);
        var url;
        if (this.review === "open") url = "/api/shop/approval";
        else if (this.review === "delete") url = "api/shop/delete/approval";
        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            if (response.status == 200) {
              toast({
                message: "商店审核通过！",
                type: "is-success",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "center",
              });
            } else {
              toast({
                message: response.status,
                type: "warning",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "center",
              });
            }
          })
          .catch((error) => {
            console.log(error.response);
            if (error.response) {
              this.errors.push(error.response.data);
              console.log(JSON.stringify(error.response));
            } else if (error.message) {
              this.errors.push("Something went wrong. Please try again");
              console.log(JSON.stringify(error.response));
            }
          });
      },
    },
    created() {
      if (this.type === "1") {
        this.is_seller = true;
      } else if (this.type === "2") {
        this.is_user = true;
      } else if (this.type === "0") {
        this.is_admin = true;
      }
    },
    props: {
      shop: { type: Object },
      type: { type: String },
      review: { type: String },
    },
  };
</script>

<style scoped>
  .image {
    margin-top: -1.25rem;
    margin-left: -1.25rem;
    margin-right: -1.25rem;
  }
</style>
