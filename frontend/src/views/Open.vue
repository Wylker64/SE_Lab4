<template>
  <div class="page-open">
    <div class="columns">
      <div class="column is-4 is-offset-4">
        <h1 class="title">开店</h1>

        <form @submit.prevent="submitForm">
          <div class="field">
            <label>店名</label>
            <div class="control">
              <input class="input" v-model="shopname" />
            </div>
          </div>

          <div class="field">
            <label>售卖商品类别</label>
            <div class="control">
              <input class="input" v-model="type" />
            </div>
          </div>

          <div class="field">
            <label>身份证号</label>
            <div class="control">
              <input class="input" v-model="id_card" />
            </div>
          </div>

          <div class="field">
            <label>商店简介</label>
            <div class="control">
              <input class="input" v-model="description" />
            </div>
          </div>

          <div class="field">
            <label>备案地址</label>
            <div class="control">
              <input class="input" v-model="address" />
            </div>
          </div>

          <div class="field">
            <label>注册资金</label>
            <div class="control">
              <input class="input" v-model="capital" />
            </div>
          </div>

          <div class="field">
            <label>注册时间</label>
            <div class="control">
              <input class="input" v-model="date" />
            </div>
          </div>

          <div class="notification is-danger" v-if="errors.length">
            <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
          </div>

          <div class="field">
            <div class="control">
              <button class="button is-dark">开店！</button>
            </div>
          </div>

          <hr />
        </form>
      </div>
      <div class="column is-offset-2">
        <button class="button is-link" @click="push_warehouse">返回</button>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  export default {
    name: "Open",
    data() {
      return {
        shopname: "",
        type: "",
        id_card: "",
        description: "",
        address: "",
        capital: "",
        date: "",
        errors: [],
      };
    },
    mounted() {
      document.title = "申请开店！";
    },
    methods: {
      valid_date(value) {
        let days = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        let YMD = value.split("-");
        let y = parseInt(YMD[0], 10);
        let m = parseInt(YMD[1], 10);
        let d = parseInt(YMD[2], 10);
        // console.log(y)
        // console.log(m)
        // console.log(d)
        //判断月份是否合法
        if (m > 12 || m < 1) {
          return false;
        }
        //判断是否是闰年
        if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
          days[2] = 29;
        }
        //判断当月天数是否合法
        if (d > days[m]) {
          return false;
        }
        return true;
      },
      submitForm() {
        this.errors = [];
        if (this.shopname.length === 0) {
          this.errors.push("店名不可为空");
        } else if (this.shopname.length > 12) {
          this.errors.push("店名要在12个字符以内");
        }

        if (this.type === "") {
          this.errors.push("请提供正确的商品类别");
        }
        if (this.id_card.length === 0) {
          this.errors.push("身份证号不得为空");
        } else if (!/^[1-9]\d{16}(X|\d)$/.test(this.id_card)) {
          this.errors.push("请输入正确的中国身份证号，一共18位");
        }
        if (this.description.length === 0) {
          this.errors.push("简介不得为空");
        } else if (this.description.length > 128) {
          this.errors.push("简介要在12个字符以内");
        }
        if (this.address.length === 0) {
          this.errors.push("备案地址不得为空");
        } else if (this.address.length > 32) {
          this.errors.push("备案地址要在32个字符以内");
        }

        if (!/^([1-9]\d{3,}|[1-9]\d{0,2}(,\d{3})+)(\.\d+)?$/.test(this.capital)) {
          console.log();
          this.errors.push("注册资金至少需要1000元");
        }

        if (!/^\d{4}-\d{2}-\d{2}$/.test(this.date)) {
          console.log();
          this.errors.push("日期不合规，请按照YYYY-MM-DD输入");
        } else if (!this.valid_date(this.date)) {
          console.log(this.valid_date(this.date));
          console.log();
          this.errors.push("输入日期非法");
        }

        if (!this.errors.length) {
          var formData = {
            authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
            shop_name: this.shopname,
            type: this.type,
            description: this.description,
            capital: this.capital,
            id_card: this.id_card,
            address: this.address,
            date: this.date,
          };

          axios
            .post("/api/shop/application", formData)
            .then((response) => {
              console.log(formData);
              if (response.status == 200) {
                toast({
                  message: "开店成功！",
                  class: "is-success",
                  dismissible: true,
                  pauseOnHover: true,
                  duration: 2000,
                  position: "bottom-right",
                });
              } else {
                toast({
                  message: response.data.message,
                  class: "warning",
                  dismissible: true,
                  pauseOnHover: true,
                  duration: 2000,
                  position: "bottom-right",
                });
              }
            })
            .catch((error) => {
              console.log(error.response);
              if (error.response) {
                this.errors.push("您的余额不足，仅有");
                this.errors.push(error.response.data);
                console.log(formData);
              } else if (error.message) {
                this.errors.push(error.message);
              }
            });
        }
      },
      push_warehouse() {
        this.$router.push("/my-shops");
      },
    },
  };
</script>
