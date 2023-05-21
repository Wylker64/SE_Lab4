<template>
  <div class="page-payment">
    <div class="columns">
      <div class="column is-6">
        <h1 class="title">支付订单</h1>
        <div class="payment-details">
          <div class="box">
            <div class="field">
              <h2 class="subtitle">订单详情</h2>
            </div>
            <div class="field">
              <div class="control">
                <div>订单编号：{{ order.order_id }}</div>
              </div>
            </div>
            <div class="field">
              <div class="control">
                <div>订单金额：{{ order.total_amount }}</div>
              </div>
            </div>
            <div class="field">
              <div class="control">
                <div>账户余额：{{ user.balance }}</div>
              </div>
            </div>
          </div>
        </div>
        <hr />
        <div class="payment-actions">
          <button class="button is-dark" :disabled="user.balance < order.total_amount" @click="payOrder">
            确认支付
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";

  export default {
    name: "Payment",
    data() {
      return {
        order: {},
        user: {},
      };
    },
    mounted() {
      document.title = "支付订单";
      // 获取订单和用户信息...
    },
    methods: {
      payOrder() {
        if (this.user.balance >= this.order.total_amount) {
          // 执行支付逻辑...
          // 更新订单状态为“已支付”
          // 将订单金额从用户账户中扣除并转移到商城的暂存账户中
          toast({
            message: "支付成功！",
            type: "is-success",
            dismissible: true,
            pauseOnHover: true,
            duration: 2000,
            position: "bottom-right",
          });
          this.$router.push("/order-management");
        } else {
          toast({
            message: "支付失败！账户余额不足。",
            type: "is-danger",
            dismissible: true,
            pauseOnHover: true,
            duration: 2000,
            position: "bottom-right",
          });
          this.$router.push("/order-management");
        }
      },
    },
  };
</script>
