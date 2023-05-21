<template>
  <div class="page-admin">
    <div class="columns is-mobile">
      <div class="column is-half is-offset-one-quarter">
        <div class="field">
          <h1 class="title">商城中间账户</h1>
          <label>我的余额</label>
          <div class="control">
            <input type="text" class="input" v-model="account1.balance" readonly />
          </div>
        </div>
        <br />
        <div class="field">
          <h1 class="title">商城流水账户</h1>
          <label>我的余额</label>
          <div class="control">
            <input type="text" class="input" v-model="account2.balance" readonly />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  export default {
    name: "Admin",
    data() {
      return {
        account1: [], //中间账户
        account2: [], //流水账户
      };
    },
    mounted() {
      document.title = "用户充值";
      this.getBalance();
    },
    methods: {
      //获取余额
      getBalance() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
        };
        axios.post("/api/account/profit", formData).then((response) => {
          this.account2 = response.data.account;
        });
        axios.post("/api/account/intermediate", formData).then((response) => {
          this.account1 = response.data.account;
        });
      },
    },
  };
</script>
