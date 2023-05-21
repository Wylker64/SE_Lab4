<template>
  <div class="colums">
    <div class="column is-4 is-offset-4">
      <h1 class="title">商店名称：{{ shop.name }}</h1>
    </div>
  </div>
  <form @submit.prevent="getProducts">
    <div class="column is-2">
      <div class="field">
        <label>页码/共{{ product_total_pages }}页</label>
        <div class="control">
          <input type="text" class="input" v-model="product_page_num" />
        </div>
      </div>
      <div class="field">
        <div class="control">
          <button class="button is-dark">搜索</button>
        </div>
      </div>
    </div>
  </form>
  <div class="columns is-multiline">
    <ProductBox v-for="product in products" :key="product.id" :product="product" :type="type"></ProductBox>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  import ProductBox from "@/components/ProductBox.vue";
  export default {
    name: "Shop",
    data() {
      return {
        shop: {},
        account: {},
        products: [],
        product_total_pages: 0,
        product_page_num: 1,
        type: "2",
        id: "",
      };
    },
    components: {
      ProductBox,
    },
    mounted() {
      document.title = "商店详情页";
      this.id = this.$route.params.id;
      this.getProducts();
    },
    methods: {
      getProducts() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          product_page_num: this.product_page_num,
        };
        var url = "/api/shop/" + this.id;
        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            this.shop = response.data.shop;
            this.account = response.data.account;
            this.products = response.data.products;
            this.product_total_pages = response.data.product_total_pages;
          })
          .catch((error) => {
            console.log(error);
          });
      },

      //充值
      refund() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          account_id: this.account.id,
          amount: this.added,
        };
        axios
          .post("/api/account/charge", formData)
          .then((response) => {
            console.log(formData);
            if (response.status == 200) {
              toast({
                message: "充值成功！",
                type: "is-success",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "bottom-right",
              });
              //成功后重置页面
              this.getInfo();
              this.added = 0;
            } else {
              toast({
                message: response.data.message,
                type: "warning",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "bottom-right",
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
      },
    },
  };
</script>
