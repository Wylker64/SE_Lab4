<!-- 商户看自己某一商店旗下的订单 -->
<template>
  <div class="page-merchant-orders">
    <div class="columns">
      <div class="column is-10">
        <h1 class="title">商家订单管理</h1>

        <form @submit.prevent="getMerchantOrders">
          <div class="columns">
            <div class="column is-3">
              <div class="field">
                <div class="select">
                  <select class="input" v-model="order_status">
                    <option value="">全部订单</option>
                    <option value="待发货">待发货</option>
                    <option value="退款申请">退款申请</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </form>

        <table class="table is-fullwidth">
          <thead>
            <tr>
              <th>订单编号</th>
              <th>商品名称</th>
              <th>商品单价</th>
              <th>购买数量</th>
              <th>订单总价</th>
              <th>购买时间</th>
              <th>收货地址</th>
              <th>用户名（收货人）</th>
              <th>订单状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in orders" v-bind:key="order.order_id">
              <td>{{ order.order_id }}</td>
              <td>{{ order.product_name }}</td>
              <td>{{ order.product_price }}</td>
              <td>{{ order.product_quantity }}</td>
              <td>{{ order.order_total }}</td>
              <td>{{ order.purchase_time }}</td>
              <td>{{ order.shipping_address }}</td>
              <td>{{ order.user_name }}</td>
              <td>{{ order.order_status }}</td>
              <td>
                <button
                  class="button is-info"
                  @click="shipOrder(order.order_id)"
                  v-if="order.order_status === '待发货'"
                >
                  发货
                </button>
                <button
                  class="button is-warning"
                  @click="acceptRefund(order.order_id)"
                  v-if="order.order_status === '退款申请'"
                >
                  同意退款
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="column is-offset-1">
        <div class="button is-link" @click="go_back()">返回</div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";

  export default {
    name: "MerchantOrders",
    data() {
      return {
        orders: [],
        page_num: 1,
        total_pages: 0,
        order_status: "",
      };
    },
    mounted() {
      document.title = "商家订单管理";

      this.getMerchantOrders();
    },
    methods: {
      go_back() {
        this.$router.back();
      },
      getMerchantOrders() {
        // 获取商家订单数据
      },
      shipOrder(order_id) {
        // 商家发货
      },
      acceptRefund(order_id) {
        // 商家同意退款
      },
    },
  };
</script>
