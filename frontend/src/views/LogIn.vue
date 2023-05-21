<template>
  <div class="page-log-in">
    <div class="columns">
      <div class="column is-4 is-offset-4">
        <h1 class="title">登陆</h1>

        <form @submit.prevent="submitForm">
          <div class="field">
            <label>用户名</label>
            <div class="control">
              <input type="text" class="input" v-model="username" />
            </div>
          </div>

          <div class="field">
            <label>密码</label>
            <div class="control">
              <input type="password" class="input" v-model="password" />
            </div>
          </div>

          <div class="notification is-danger" v-if="errors.length">
            <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
          </div>

          <div class="field">
            <div class="control">
              <button class="button is-dark">登陆</button>
            </div>
          </div>

          <hr />

          <router-link to="/sign-up">点击这里</router-link> 去注册!
        </form>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  export default {
    name: "LogIn",
    data() {
      return {
        username: "",
        password: "",
        errors: [],
        userid: "",
        identity: "",
      };
    },
    mounted() {
      document.title = "登陆";
    },
    methods: {
      async submitForm() {
        this.errors = [];
        axios.defaults.headers.common["Authorization"] = "";
        localStorage.removeItem("token");
        this.errors.splice(0);
        if (this.username.length === 0 || this.password === 0) {
          this.errors.push("用户名或密码不得为空");
          return;
        }
        const formData = {
          username: this.username,
          password: this.password,
        };
        console.log(formData);
        await axios
          .post("/api/login", formData)
          .then((response) => {
            this.userid = response.data.userid;
            this.identity = response.data.identity;
            console.log(response);

            localStorage.setItem("authorize", JSON.stringify({ username: this.username, id: this.userid }));
            localStorage.setItem("identity", this.identity);
            // const token = document.getElementById("url").value

            // this.$store.commit('setToken', token)

            // axios.defaults.headers.common["Authorization"] = "Token " + token

            // localStorage.setItem("token", token)
            if (response.status == 200) {
              // toast({
              //   message: "登陆成功！",
              //   type: "is-success",
              //   dismissible: true,
              //   pauseOnHover: true,
              //   duration: 2000,
              //   position: "bottom-right",
              // });

              // if (String(this.identity) == "0") {
              //   var toPath = this.$route.query.to || "/";
              //   // this.$store.commit('setBuyer', token)
              // }
              // if (String(this.identity) == "1") {
              //   var toPath = this.$route.query.to || "/";
              //   console.log(toPath);
              //   // this.$store.commit('setSeller', token)
              // }
              // if (String(this.identity) == "2") {
              //   var toPath = this.$route.query.to || "/";
              //   // this.$store.commit('setAdmin', token)
              // }
              this.$router.push({ name: "Home", params: { way: "login" } });
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
              console.log(error.response.data);
              this.errors.push(error.response.data);
            } else {
              this.errors.push("Something went wrong. Please try again");
              console.log(error.response);
            }
          });
      },
    },
  };
</script>
