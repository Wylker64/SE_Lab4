<!-- 展示自己买了的物品的订单界面 -->
<template>
  <div class="page-orders">
    <div class="columns">
      <div class="column is-10">
        <h1 class="title">我的订单</h1>

        <form @submit.prevent="getOrders">
          <div class="columns">
            <div class="column is-3">
              <div class="field">
                <div class="select">
                  <select class="input" v-model="order_status">
                    <option value="">全部订单</option>
                    <option value="待支付">待支付</option>
                    <option value="待发货">待发货</option>
                    <option value="待收货">待收货</option>
                    <option value="已完成">已完成</option>
                    <option value="已撤销">已撤销</option>
                    <option value="已退款">已退款</option>
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
              <th>店铺名称</th>
              <th>商品名称</th>
              <th>商品单价</th>
              <th>购买数量</th>
              <th>订单总价</th>
              <th>购买时间</th>
              <th>收货地址</th>
              <th>用户名</th>
              <th>订单状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in orders" v-bind:key="order.order_id">
              <td>{{ order.order_id }}</td>
              <td>{{ order.shop_name }}</td>
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
                  class="button is-primary"
                  @click="payOrder(order.order_id)"
                  v-if="order.order_status === '待支付'"
                >
                  支付
                </button>
                <button
                  class="button is-info"
                  @click="confirmReceipt(order.order_id)"
                  v-if="order.order_status === '待收货'"
                >
                  确认收货
                </button>
                <button
                  class="button is-warning"
                  @click="cancelOrder(order.order_id)"
                  v-if="order.order_status === '待支付'"
                >
                  撤销订单
                </button>
                <button
                  class="button is-danger"
                  @click="requestRefund(order.order_id)"
                  v-if="order.order_status === '待发货' || order.order_status === '待收货'"
                >
                  退货退款
                </button>
                <button
                  class="button is-dark"
                  @click="deleteOrder(order.order_id)"
                  v-if="['已完成', '已撤销', '已退款'].includes(order.order_status)"
                >
                  删除订单
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script>
  import axios from "axios";
  import { toast } from "bulma-toast";

  export default {
    name: "Orders",
    data() {
      return {
        orders: [],
        page_num: 1,
        total_pages: 0,
        order_status: "",
      };
    },
    mounted() {
      document.title = "我的订单";
      this.getOrders();
    },
    methods: {
      getOrders() {
        // 获取订单数据
      },
      payOrder(order_id) {
        // 支付订单
      },
      confirmReceipt(order_id) {
        // 确认收货
      },
      cancelOrder(order_id) {
        // 撤销订单
      },
      requestRefund(order_id) {
        // 退货退款
      },
      deleteOrder(order_id) {
        // 删除订单
      },
    },
  };
</script>
