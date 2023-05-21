<template>
  <div class="review">
    <div class="columns">
      <div class="column is-6">
        <h1 class="title">增添商品申请</h1>
        <div class="field">
          <label>页面/共{{ total_pages_1 }}页</label>
          <div class="column is-3">
            <div class="control">
              <input type="text" class="input" v-model="page_num1" />
            </div>
          </div>
        </div>
        <div class="field">
          <div class="control">
            <button class="button is-dark" @click="getPro(1)">搜索！</button>
          </div>
        </div>
        <div class="columns is-multiline">
          <ProductBox
            v-for="product in products1"
            :key="product.id"
            :product="product"
            :type="type"
            :review="review1"
          ></ProductBox>
        </div>
      </div>
      <div class="column is-6">
        <h1 class="title">修改商品申请</h1>
        <div class="field">
          <label>页面/共{{ total_pages_2 }}页</label>
          <div class="column is-3">
            <div class="control">
              <input type="text" class="input" v-model="page_num2" />
            </div>
          </div>
        </div>
        <div class="field">
          <div class="control">
            <button class="button is-dark" @click="getPro(2)">搜索！</button>
          </div>
        </div>
        <div class="columns is-multiline">
          <ProductBox
            v-for="product in products2"
            :key="product.id"
            :product="product"
            :type="type"
            :review="review2"
          ></ProductBox>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import ProductBox from "@/components/ProductBox.vue";
  export default {
    name: "ReviewProduct",
    data() {
      return {
        page_num1: 1, //新增商品审查页
        page_num2: 1, //修改商品审查页
        total_pages_1: 0,
        total_pages_2: 0,
        products1: [],
        products2: [],
        errors: [],
        type: "0", //表示是在管理员界面调用productbox
        review1: "create",
        review2: "modify",
      };
    },
    mounted() {
      this.getPro(1);
      this.getPro(2);
    },
    components: {
      ProductBox,
    },
    methods: {
      getPro(flag) {
        var page_num;
        var url;
        if (flag === 1) {
          url = "/api/product/create/review";
          page_num = this.page_num1;
        } else if (flag === 2) {
          url = "/api/product/modify/review";
          page_num = this.page_num2;
        }
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          page_num: page_num,
          review: JSON.parse(localStorage.getItem("authorize") || "{}").username == "admin",
        };
        console.log(formData);
        var total_pages;
        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            total_pages = response.data.total_pages;
            if (flag === 1) this.products1 = response.data.products;
            else if (flag === 2) this.products2 = response.data.products;
            if (flag === 1) {
              this.total_pages_1 = total_pages;
            } else if (flag === 2) {
              this.total_pages_2 = total_pages;
            }
          })
          .catch((error) => {
            console.log(error);
          });
      },
    },
  };
</script>
