<!-- 下单界面 -->
<template>
  <div class="page-checkout">
    <div class="columns">
      <div class="column is-8">
        <h1 class="title">提交订单</h1>
        <div>{{ this.type }}</div>
        <div class="checkout-items">
          <!-- <h2 class="subtitle">购物清单</h2> -->
          <div class="box">
            <table class="table is-fullwidth">
              <thead>
                <tr>
                  <th>商品名称</th>
                  <th>购买件数</th>
                  <th>店铺名称</th>
                  <th>单价</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in items" v-bind:key="item.product_id">
                  <td>{{ item.name }}</td>
                  <td>{{ item.count }}</td>
                  <td>{{ item.shop_name }}</td>
                  <td>{{ item.price }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="shipping-address">
          <!-- <h2 class="subtitle">收货地址</h2> -->
          <!-- 收货地址选择组件 -->
          <!-- <div class="dropdown is-hoverable">
            <div class="dropdown-trigger">
              <button class="button" aria-haspopup="true" aria-controls="dropdown-menu">
                <span>地址选择</span>
                <span class="icon is-small">
                  <i class="fas fa-angle-down" aria-hidden="true"></i>
                </span>
              </button>
            </div>
            <div class="dropdown-menu" id="dropdown-menu" role="menu">
              <div class="dropdown-content">
                <div class="dropdown-item">
                  <label class="radio">
                    <input type="radio" name="answer" v-model="answer" value="1" />
                    <Address></Address>
                  </label>
                </div>
                <hr class="dropdown-divider" />
                <div class="dropdown-item">
                  <label class="radio">
                    <input type="radio" name="answer" v-model="answer" value="1" />
                    <Address></Address>
                  </label>
                </div>
                <hr class="dropdown-divider" />
                <div class="dropdown-item">
                  <label class="radio">
                    <input type="radio" name="answer" v-model="answer" value="1" />
                    <Address></Address>
                  </label>
                </div>
              </div>
            </div>
          </div> -->
        </div>

        <div class="payment-summary">
          <!-- <h2 class="subtitle">结算金额及优惠明细</h2> -->
          <!-- 结算金额及优惠明细组件 -->
        </div>

        <div class="submit-order">
          <button class="button is-dark" @click="submitOrder">
            提交订单
          </button>
        </div>
      </div>
      <div class="column is-3 is-offset-1">
        <h2 class="title is-3">选择地址</h2>
        <!-- <label class="radio">
          <input type="radio" name="answer" v-model="address_id" value="1" />
          <Address></Address>
        </label> -->
        <div class="address" v-for="address in addresses" :key="address.id">
          <label class="radio">
            <input type="radio" name="answer" v-model="address_id" :value="address.id" />
            <Address :key="address.id" :address="address"></Address>
          </label>
        </div>
      </div>
    </div>
    <button @click="show">show</button>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  import Address from "../components/address.vue";
  export default {
    name: "Checkout",
    components: {
      Address,
    },
    data() {
      return {
        items: [],
        type: -1,
        order_id: -1,
        addresses: [],
        address_id: -1,
        // 其他所需数据
      };
    },
    mounted() {
      document.title = "提交订单";
      // 获取购物清单数据
      // console.log(this.$route.params);
      this.type = this.$route.params.type;
      this.items = JSON.parse(this.$route.params.items);
      // console.log(this.type);
      // console.log(this.items);
      this.getAddress();
    },
    methods: {
      submitOrder() {
        // 提交订单
        this.$router.push({ name: "pay", params: { id: this.order_id } });
      },
      show() {
        console.log(this.address_id);
      },
      getAddress() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
        };
        axios.post("/api/address", formData).then((response) => {
          console.log(response.data.items);
          this.addresses = response.data.items;
          console.log(this.addresses);
        });
      },
      // 其他方法，如收货地址选择、优惠券选择等
    },
  };
</script>
