<template>
  <div class="page-sign-up">
    <div class="columns">
      <div class="column is-4 is-offset-4">
        <h1 class="title">注册</h1>

        <form @submit.prevent="submitForm">
          <div class="field">
            <label>选择你的角色</label>
            <div class="control">
              <div class="select is-primary">
                <select id="identity">
                  <option value="0">买家</option>
                  <option value="1">卖家</option>
                </select>
              </div>
            </div>
          </div>

          <div class="field">
            <label>用户名</label>
            <div class="control">
              <input type="text" class="input" v-model="username" />
            </div>
          </div>

          <div class="field">
            <label>手机号</label>
            <div class="control">
              <input type="text" class="input" v-model="phone_number" />
            </div>
          </div>

          <div class="field">
            <label>身份证号</label>
            <div class="control">
              <input type="text" class="input" v-model="id_card" />
            </div>
          </div>

          <div class="field">
            <label>邮箱</label>
            <div class="control">
              <input type="text" class="input" v-model="email" />
            </div>
          </div>

          <div class="field">
            <label>密码</label>
            <div class="control">
              <input type="password" class="input" v-model="password" />
            </div>
          </div>

          <div class="field">
            <label>重复输入密码</label>
            <div class="control">
              <input type="password" class="input" v-model="password2" />
            </div>
          </div>

          <div class="notification is-danger" v-if="errors.length">
            <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
          </div>

          <div class="field">
            <div class="control">
              <button class="button is-dark">注册！</button>
            </div>
          </div>

          <hr />

          <router-link to="/log-in">点击这里</router-link> 登陆!
        </form>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  export default {
    name: "SignUp",
    data() {
      return {
        username: "",
        phone_number: "",
        id_card: "",
        email: "",
        password: "",
        password2: "",
        errors: [],
      };
    },
    mounted() {
      document.title = "Sign Up";
    },
    methods: {
      submitForm() {
        this.errors = [];
        if (this.username.length === 0) {
          this.errors.push("用户名不得为空");
        } else if (
          !/^\w+$/.test(this.username) ||
          this.username.length > 10 ||
          this.username.length < 3
        ) {
          this.errors.push("用户名格式不正确(英文、数字、下划线，长度为3-10个字符)");
        }
        if (this.phone_number.length === 0) {
          this.errors.push("手机号不得为空");
        } else if (!/^1[3-9]\d{9}$/.test(this.phone_number)) {
          this.errors.push("请输入中国大陆的手机号(11位，第一位为1，第二位为3-9其中之一)");
        }
        if (this.id_card.length === 0) {
          this.errors.push("身份证号不得为空");
        } else if (!/^[1-9]\d{16}(X|\d)$/.test(this.id_card)) {
          this.errors.push("请输入正确的中国身份证号，一共18位");
        }
        if (this.email.length === 0) {
          this.errors.push("E-mail不得为空");
        } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.email)) {
          this.errors.push("请输入正确的E-mail(用户名+@+域名)");
        }
        if (this.password.length === 0 || this.password2.length === 0) {
          this.errors.push("密码不得为空");
        } else if (this.password !== this.password2) {
          this.errors.push("两次输入的密码不一样");
        } else if (
          !/^(?=.*[a-zA-Z])(?=.*\d)|(?=.*\d)(?=.*[-_])|(?=.*[a-zA-Z])(?=.*[-_])[a-zA-Z\d-_]{6,32}$/.test(
            this.password
          )
        ) {
          this.errors.push(
            "请输入满足要求的密码(6-32个字符，至少包含字母、数字或者（-_）中的至少两种)"
          );
        }

        console.log(this.errors);

        if (!this.errors.length) {
          var formData = {
            username: this.username,
            password: this.password,
            phone_number: this.phone_number,
            email: this.email,
            id_card: this.id_card,
            identity: parseInt(document.getElementById("identity").value),
          };

          axios
            .post("/api/register", formData)
            .then((response) => {
              console.log(response);
              if (response.status == 200) {
                toast({
                  message: "账号成功注册，可以登陆了！",
                  type: "is-success",
                  dismissible: true,
                  pauseOnHover: true,
                  duration: 2000,
                  position: "bottom-right",
                });
                this.$router.push("/log-in");
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
              if (error.response) {
                // for (const property in error.response.data) {
                // this.errors.push(`${property}: ${error.response.data[property]}`)
                this.errors.push(JSON.stringify(error.response.data));
                // }

                console.log(JSON.stringify(error.response));
              } else if (error.message) {
                this.errors.push(error.message);

                console.log(response);
              }
            });
        }
      },
    },
  };
</script>
