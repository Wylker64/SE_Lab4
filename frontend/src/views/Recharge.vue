<template>
  <div class="page-recharge">
    <div class="columns is-mobile">
      <div class="column is-half is-offset-one-quarter">
        <div class="field">
          <h1 class="title">账户充值</h1>
          <label>我的余额</label>
          <div class="control">
            <input type="text" class="input" v-model="account.balance" readonly />
          </div>
        </div>
        <form @submit.prevent="submitForm">
          <div class="field">
            <label>充值金额</label>
            <div class="control">
              <input type="text" class="input is-primary" v-model="added" />
            </div>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-dark">确认充值</button>
            </div>
          </div>
        </form>
        <hr />
        <div class="button is-link" @click="push_fundsflow()">账户流水</div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  export default {
    name: "Recharge",
    data() {
      return {
        added: 0,
        account: {},
      };
    },
    mounted() {
      document.title = "用户充值";
      this.getBalance();
    },
    methods: {
      push_fundsflow() {
        var url = "/funds-flow/" + this.id;
        this.$router.push({ path: url });
      },
      //获取余额
      getBalance() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
        };
        axios.post("/api/account", formData).then((response) => {
          this.account = response.data.account;
        });
        console.log(this.account);
      },
      //提交新增金额
      submitForm() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          account_id: this.account.id,
          amount: this.added,
        };
        axios
          .post("/api/account/charge", formData)
          .then((response) => {
            console.log(formData);
            this.getBalance();
            if (response.status == 200) {
              toast({
                message: "充值成功！",
                type: "is-success",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "bottom-right",
              });
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
