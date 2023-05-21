<template>
  <div class="column is-4">
    <div class="card">
      <div class="card-image">
        <figure class="image is-4by3">
          <img :src="path" alt="Placeholder image" />
        </figure>
        <div class="button" id="left" v-show="index > 0" @click="btnLeft">&lt;</div>
        <div class="button" id="right" v-show="index < product.image_num - 1" @click="btnRight">
          &gt;
        </div>
      </div>
      <div class="card-content">
        <div class="media">
          <div class="media-content">
            <p class="title is-4">名称:{{ product.name }}</p>
            <p v-if="is_admin" class="title is-6">ID:{{ product.id }}</p>
          </div>
        </div>

        <div class="content">描述：{{ product.description }}</div>
      </div>
      <footer v-if="is_admin" class="card-footer">
        <button class="button is-primary" @click="release(true)">通过</button>
        <button class="button is-danger" @click="release(false)">拒绝</button>
        <div class="notification is-danger" v-if="errors.length">
          <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
        </div>
      </footer>
      <footer v-if="is_user" class="card-footer">
        <div class="control">
          <button class="button is-dark" @click="to_cart">加入购物车</button>
        </div>
        <div class="control">
          <button class="button is-dark" @click="to_order">立即购买</button>
        </div>
      </footer>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  export default {
    data() {
      return {
        path: "",
        index: 0,
        is_admin: false,
        is_user: false,
        errors: [],
      };
    },
    props: {
      product: { type: Object },
      type: { type: String },
      review: { type: String },
    },
    methods: {
      getImg() {
        this.path = "http://119.3.184.138:80/file/product/img/" + this.product.id + "-" + this.index + ".png";
      },
      btnLeft() {
        this.index--;
        this.getImg();
      },
      btnRight() {
        this.index++;
        this.getImg();
      },
      to_order() {
        var item = {
          id: this.product.id,
          name: this.product.name,
          count: 1,
          shop_name: this.product.shop_name,
          price: this.product.price,
        };
        var items = [];
        items.push(item);
        this.$router.push({ name: "order", params: { items: JSON.stringify(items), type: 1 } });
      },
      //上传审批结果
      release(approved) {
        this.errors = [];
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          info_id: this.product.id,
          approved: approved,
          review: JSON.parse(localStorage.getItem("authorize") || "{}").username == "admin",
        };
        console.log(formData);
        var url;
        if (this.review === "create") url = "/api/product/create/approval";
        else if (this.review === "modify") url = "/api/product/modify/approval";
        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            if (response.status == 200) {
              toast({
                message: "完成商品审核！",
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
            console.log(error);
            if (error.response) {
              this.errors.push(error.response.data);
              console.log(JSON.stringify(error.response));
            } else if (error.message) {
              // this.errors.push("Something went wrong. Please try again");
              console.log(error.response);
            }
          });
      },
      //将商品添加进入购物车
      to_cart() {
        this.errors = [];
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          product_id: this.product.id,
        };
        console.log(formData);
        var url = "/api/cart/create";
        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            if (response.status == 200) {
              toast({
                message: "添加购物车成功！",
                type: "is-success",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "bottom-right",
              });
            } else {
              toast({
                message: response.status,
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
            if (error.response) {
              this.errors.push(error.response.data);
              console.log(JSON.stringify(error.response));
            } else if (error.message) {
              // this.errors.push("Something went wrong. Please try again");
              console.log(error.response);
            }
          });
      },
    },
    mounted() {
      if (this.type === "2") {
        this.is_user = true;
      } else if (this.type === "0") {
        this.is_admin = true;
      }
    },
    created() {
      this.getImg();
    },
  };
</script>

<style>
  .turning_button {
    position: absolute;
    width: 30px;
    height: 50px;
    text-align: center;
    line-height: 50px;
    top: 50%;
    margin-top: -25px;
    border: 1px solid #fff;
    opacity: 0.3;
    cursor: pointer;
    color: #fff;
    background: black;
  }
  #right {
    left: 100%;
    margin-left: -30px;
  }
</style>
