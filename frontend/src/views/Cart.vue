<template>
  <div class="page-cart">
    <div class="columns">
      <div class="column is-7">
        <h1 class="title">我的购物车</h1>

        <form @submit.prevent="getOrder">
          <div class="column is-3">
            <div class="field">
              <label>页码/共{{ total_pages }}页</label>
              <div class="control">
                <input type="text" class="input" v-model="page_num" />
              </div>
            </div>
            <div class="field">
              <div class="control">
                <button class="button is-dark">搜索</button>
              </div>
            </div>
          </div>
        </form>
        <table class="table is-fullwidth">
          <thead>
            <tr>
              <th>
                <input type="checkbox" @change="toggleAll" />
              </th>
              <th>商品名称</th>
              <th>商品价格</th>
              <th>个数</th>
              <th>花费</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in items" v-bind:key="item.product_id">
              <td>
                <input type="checkbox" v-model="item.checked" />
              </td>
              <td>{{ item.name }}</td>
              <td>{{ item.price }}</td>
              <td>{{ item.count }}</td>
              <td>{{ item.price * item.count }}</td>
              <td v-if="item.valid">在售中</td>
              <td v-if="!item.valid">已下架</td>
              <td><button @click="deleteP(item.id)">删除</button></td>
            </tr>
          </tbody>
        </table>
        <div class="field is-grouped">
          <div class="control">
            <button class="button is-dark" @click="executePayment">付款</button>
          </div>
          <div class="control">
            <button class="button is-danger" @click="executeDelete">删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";

  export default {
    name: "Cart",
    data() {
      return {
        items: [],
        page_num: 1,
        total_pages: 0,
      };
    },
    mounted() {
      document.title = "购物车";
      this.getOrder();
      // this.cart = this.$store.state.cart
    },
    methods: {
      getOrder() {
        var formData = {
          page_num: this.page_num,
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
        };
        console.log(formData);
        axios
          .post("/api/cart", formData)
          .then((response) => {
            this.total_pages = response.data.total_pages;
            this.items = response.data.items;
            console.log(response);
          })
          .catch((error) => {
            console.log(error);
          });
      },
      executePayment() {
        var orderlist = [];
        for (var i = 0; i < this.items.length; i++) {
          if (this.items[i].checked == true) {
            orderlist.push(this.items[i]);
          }
        }
        console.log(orderlist);
        this.$router.push({ name: "order", params: { items: JSON.stringify(orderlist), type: 0 } });
      },
      toggleAll() {
        for (var i = 0; i < this.items.length; i++) {
          this.items[i].checked = true;
        }
      },
      deleteP(id) {
        var formData = {
          item_id: id,
          item_id_list: [],
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
        };
        axios
          .post("/api/cart/delete", formData)
          .then((response) => {
            console.log(response);
            if (response.status === 200) {
              toast({
                message: "从购物车删除成功！",
                type: "is-success",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "bottom-right",
              });
              this.getOrder();
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
          });
      },
    },
  };
</script>
