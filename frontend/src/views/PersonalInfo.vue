<template>
  <div class="page-personal-info">
    <div class="columns is-mobile">
      <div class="column is-5 is-offset-1">
        <form v-if="!changed" @submit.prevent="changed = true">
          <h1 class="title">个人信息</h1>
          <div class="field">
            <label>用户名</label>
            <div class="control">
              <input type="text" class="input" v-model="username" readonly />
            </div>
          </div>

          <div class="field">
            <label>手机号</label>
            <div class="control">
              <input type="text" class="input" v-model="phone_number" readonly />
            </div>
          </div>

          <div class="field">
            <label>邮箱</label>
            <div class="control">
              <input type="text" class="input" v-model="email" readonly />
            </div>
          </div>

          <div class="field">
            <label>身份证</label>
            <div class="control">
              <input type="text" class="input" v-model="id_card" readonly />
            </div>
          </div>

          <div class="field">
            <div class="control">
              <button class="button is-dark">修改信息</button>
            </div>
          </div>
        </form>

        <form v-if="changed" @submit.prevent="submitForm">
          <h1 class="title">信息修改</h1>
          <div class="field">
            <label>用户名</label>
            <div class="control">
              <input type="text" class="input is-info" v-model="username" />
            </div>
          </div>

          <div class="field">
            <label>手机号</label>
            <div class="control">
              <input type="text" class="input is-info" v-model="phone_number" />
            </div>
          </div>

          <div class="field">
            <label>邮箱</label>
            <div class="control">
              <input type="text" class="input is-info" v-model="email" />
            </div>
          </div>

          <div class="field">
            <label>身份证(禁止修改)</label>
            <div class="control">
              <input type="text" class="input" v-model="id_card" readonly />
            </div>
          </div>

          <div class="field">
            <label>当前密码</label>
            <div class="control">
              <input type="password" class="input is-primary" v-model="password" />
            </div>
          </div>

          <div class="field">
            <label>新密码(若无需修改请勿输入内容)</label>
            <div class="control">
              <input type="password" class="input is-info" v-model="new_password" />
            </div>
          </div>

          <div class="field">
            <label>重复输入新密码</label>
            <div class="control">
              <input type="password" class="input is-info" v-model="new_password2" />
            </div>
          </div>

          <div class="field">
            <div class="control">
              <button class="button is-dark">确认修改</button>
            </div>
          </div>
          <div class="notification is-danger" v-if="errors.length">
            <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
          </div>
        </form>
        <hr />
        <div class="field">
          <div class="control">
            <button class="button is-dark" v-if="changed" @click="getInfo(), (changed = false)">取消修改</button>
          </div>
        </div>
      </div>
      <div class="column is-4 is-offset-1">
        <h2 class="title is-3">地址管理</h2>
        <div class="address">
          <Address v-for="address in addresses" :key="address.id" :address="address"></Address>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  import Address from "../components/address.vue";
  export default {
    name: "PersonalInfo",
    components: {
      Address,
    },
    data() {
      return {
        changed: false,
        id_card: "",
        username: "",
        phone_number: "",
        email: "",
        password: "",
        new_password: "",
        new_password2: "",
        errors: [],
        addresses: [],
        answer: "",
        type: "0",
      };
    },
    mounted() {
      document.title = "个人资料";
      //在进入页面时获取个人资料
      this.getInfo();
    },
    methods: {
      show() {
        console.log(this.answer);
      },
      getInfo() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
        };
        axios.post("/api/info", formData).then((response) => {
          console.log(response);
          this.id_card = response.data.idCard;
          this.username = response.data.username;
          this.phone_number = response.data.phone;
          this.email = response.data.email;
        });
        axios.post("/api/address", formData).then((response) => {
          console.log(response.data.items);
          this.addresses = response.data.items;
          console.log(this.addresses);
        });
      },
      changeProfile() {
        this.changed = true;
      },
      cancelChange() {
        this.changed = false;
      },
      submitForm() {
        //前端合规性判断
        this.errors = [];
        if (this.username.length === 0) {
          this.errors.push("用户名不得为空");
        } else if (!/^\w+$/.test(this.username) || this.username.length > 10 || this.username.length < 3) {
          this.errors.push("用户名格式不正确(英文、数字、下划线，长度为3-10个字符)");
        }
        if (this.phone_number.length === 0) {
          this.errors.push("手机号不得为空");
        } else if (!/^1[3-9]\d{9}$/.test(this.phone_number)) {
          this.errors.push("请输入中国大陆的手机号(11位，第一位为1，第二位为3-9其中之一)");
        }
        if (this.email.length === 0) {
          this.errors.push("E-mail不得为空");
        } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.email)) {
          this.errors.push("请输入正确的E-mail(用户名+@+域名)");
        }
        if (this.password.length === 0) {
          this.errors.push("请输入密码以更改");
        }
        if (this.new_password !== this.new_password2) {
          this.errors.push("两次输入的新密码不一致");
        } else if (this.new_password.length === 0) {
        } else if (
          !/^(?=.*[a-zA-Z])(?=.*\d)|(?=.*\d)(?=.*[-_])|(?=.*[a-zA-Z])(?=.*[-_])[a-zA-Z\d-_]{6,32}$/.test(
            this.new_password
          )
        ) {
          this.errors.push("请输入满足要求的新密码(6-32个字符，至少包含字母、数字或者（-_）中的至少两种)");
        }
        if (this.errors.length === 0) {
          //接口
          if (this.new_password.length) {
            var formData = {
              authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
              username: this.username,
              email: this.email,
              phone_number: this.phone_number,
              current_password: this.password,
              password: this.new_password,
            };
          } else {
            var formData = {
              authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
              username: this.username,
              email: this.email,
              phone_number: this.phone_number,
              current_password: this.password,
            };
          }
          console.log(formData);
          axios
            .post("/api/modify", formData)
            .then((response) => {
              console.log(response);
              if (response.status == 200) {
                toast({
                  message: "更新成功！",
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
              if (error.response) {
                this.errors.push(error.response.data);
                console.log(formData);
              } else if (error.message) {
                this.errors.push(error.message);
              }
            });
        }
      },
    },
  };
</script>
