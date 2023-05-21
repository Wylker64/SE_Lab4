<template>
  <div id="wrapper">
    <nav class="navbar is-dark" role="navigation" aria-label="main navigation">
      <div class="navbar-brand">
        <router-link to="/" class="navbar-item"><strong>E-Commerce</strong></router-link>
      </div>
      <div id="navbarBasicExample" class="navbar-menu">
        <div v-if="identity == 2" class="navbar-start">
          <a class="navbar-item" href="/admin">
            管理员账户
          </a>
          <a class="navbar-item" href="/review">
            审查店铺
          </a>
          <a class="navbar-item" href="/review-product">
            审查商品
          </a>
          <a class="navbar-item" href="/promotion">
            活动列表
          </a>
        </div>

        <div v-if="identity == 0" class="navbar-start">
          <a class="navbar-item" href="/personal-info">
            个人资料
          </a>
          <a class="navbar-item" href="/cart">
            购物车
          </a>
          <a class="navbar-item" href="/recharge">
            账户充值
          </a>
          <a class="navbar-item" href="/my-orders">
            我的订单
          </a>
        </div>

        <div v-if="identity == 1" class="navbar-start">
          <a class="navbar-item" href="/personal-info">
            个人资料
          </a>
          <a class="navbar-item" href="/cart">
            购物车
          </a>
          <a class="navbar-item" href="/recharge">
            账户充值
          </a>
          <a class="navbar-item" href="/my-shops">
            我的商店
          </a>
          <a class="navbar-item" href="/promotion">
            活动列表
          </a>
          <a class="navbar-item" href="/my-orders">
            我的订单
          </a>
        </div>
      </div>

      <div class="navbar-end">
        <div class="navbar-end">
          <div class="navbar-item">{{ name }}</div>
        </div>
        <div v-if="name != ''" class="navbar-item">
          <div class="button is-danger" @click="logOut">
            退出登录
          </div>
        </div>
        <div class="navbar-item">
          <div class="buttons">
            <a class="button is-primary" href="/sign-up">
              <strong>注册</strong>
            </a>
            <a class="button is-light" href="/log-in">
              登陆
            </a>
          </div>
        </div>
      </div>
    </nav>
    <div class="is-loading-bar has-text-centered" v-bind:class="{ 'is-loading': $store.state.isLoading }">
      <div class="lds-dual-ring"></div>
    </div>

    <section class="section">
      <router-view />
    </section>
  </div>
</template>

<script>
  import axios from "axios";
  window.addEventListener("storage", () => {
    identity = localStorage.getItem("identity");
  });
  export default {
    data() {
      return {
        showMobileMenu: false,
        identity: "-1",
        name: "",
        way: "",
      };
    },
    methods: {
      logOut() {
        localStorage.clear();
        location.reload();
      },
    },
    beforeCreate() {
      this.$store.commit("initializeStore");
      const token = this.$store.state.token;

      if (token) {
        axios.defaults.headers.common["Authorization"] = "Token " + token;
      } else {
        axios.defaults.headers.common["Authorization"] = "";
      }
    },
    mounted() {
      this.identity = localStorage.getItem("identity");
      if (localStorage.getItem("authorize")) this.name = JSON.parse(localStorage.getItem("authorize")).username;

      // let _this = this;
      // window.addEventListener("setItemEvent", function(e) {
      //   if (e.key === "identity") {
      //     console.log(e.newValue);
      //     _this.identity = e.newValue;
      //   }
      //   if (e.key === "authorized") {
      //     console.log(e.newValue);
      //     _this.name = JSON.parse(localStorage.getItem("authorize")).username;
      //   }
      // });
    },
    computed: {
      cartTotalLength() {
        let totalLength = 0;

        for (let i = 0; i < this.cart.items.length; i++) {
          totalLength += this.cart.items[i].quantity;
        }

        return totalLength;
      },
    },
  };
</script>

<style lang="scss">
  @import "../node_modules/bulma";

  .lds-dual-ring {
    display: inline-block;
    width: 80px;
    height: 80px;
  }

  .lds-dual-ring:after {
    content: " ";
    display: block;
    width: 64px;
    height: 64px;
    margin: 8px;
    border-radius: 50%;
    border: 6px solid #ccc;
    border-color: #ccc transparent #ccc transparent;
    animation: lds-dual-ring 1.2s linear infinite;
  }

  @keyframes lds-dual-ring {
    0% {
      transform: rotate(0deg);
    }

    100% {
      transform: rotate(360deg);
    }
  }

  .is-loading-bar {
    height: 0;
    overflow: hidden;

    -webkit-transition: all 0.3s;
    transition: all 0.3s;

    &.is-loading {
      height: 80px;
    }
  }
</style>
