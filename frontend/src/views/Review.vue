<template>
  <div class="review">
    <!-- <div class="columns">
      <div class="column">
        <form @submit.prevent="getPro">
          <div class="column is-4 is-offset-4">
            <div class="field">
              <label>页面</label>
              <div class="control">
                <input type="text" class="input" v-model="page_num" />
              </div>
            </div>
            <div class="field">
              <div class="control">
                <button class="button is-dark" @click="getPro">搜索！</button>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="column">
        <form @submit.prevent="release">
          <div class="column is-4 is-offset-4">
            <div class="field">
              <label>Shop ID</label>
              <div class="control">
                <input type="text" class="input" v-model="shop_id" />
              </div>
            </div>
            <div class="field">
              <div class="control">
                <button :style="{ marginRight: '10px' }" @click="approved = true">
                  批准开店！
                </button>
                <button @click="approved = false">拒绝请求</button>
              </div>
            </div>
            <div class="notification is-danger" v-if="errors.length">
              <p v-for="error in errors" v-bind:key="error">{{ error }}</p>
            </div>
          </div>
        </form>
      </div>
    </div> -->
    <div class="columns">
      <div class="column is-12">
        <h1 class="title">开店申请</h1>
        <div class="field">
          <label>页面/共{{ total_pages1 }}页</label>
          <div class="column is-3">
            <div class="control">
              <input type="text" class="input" v-model="page_num1" />
            </div>
          </div>
        </div>
        <div class="field">
          <div class="control">
            <button class="button is-dark" @click="getPro(1)">搜索！</button>
          </div>
        </div>
        <div class="columns is-multiline">
          <ShopBox
            v-for="shop in shops1"
            v-bind:key="shop.id"
            v-bind:shop="shop"
            v-bind:type="type"
            v-bind:review="review1"
          />
        </div>
        <div class="column is-12">
          <h1 class="title">删除商店申请</h1>
          <div class="field">
            <label>页面/共{{ total_pages2 }}页</label>
            <div class="column is-3">
              <div class="control">
                <input type="text" class="input" v-model="page_num2" />
              </div>
            </div>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-dark" @click="getPro(2)">搜索！</button>
            </div>
          </div>
          <div class="columns is-multiline">
            <ShopBox
              v-for="shop in shops2"
              v-bind:key="shop.id"
              v-bind:shop="shop"
              v-bind:type="type"
              v-bind:review="review2"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";
  // import { toast } from "bulma-toast";
  import ShopBox from "@/components/ShopBox";
  export default {
    name: "Review",
    data() {
      return {
        shops1: [], //存放申请新建商店
        shops2: [], //存放申请删除商店
        shop_id: "",
        approved: "",
        page_num1: 1, //
        page_num2: 1,
        total_pages1: 0,
        total_pages2: 0,
        errors: [],
        type: "0", //表示是管理员界面调用shopbox
        review1: "open",
        review2: "delete",
      };
    },
    components: {
      ShopBox,
    },
    mounted() {
      document.title = "审查商店申请";
      this.getPro(1); //代表获取申请新建商店
      this.getPro(2); //代表获取申请删除商店
    },
    methods: {
      async getPro(flag) {
        var url;
        var page_num;
        if (flag === 1) {
          page_num = this.page_num1;
          url = "/api/shop";
        } else if (flag === 2) {
          page_num = this.page_num2;
          url = "/api/shop/delete/review";
        }
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          page_num: page_num,
          review: JSON.parse(localStorage.getItem("authorize") || "{}").username == "admin",
        };

        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            if (flag === 1) {
              this.shops1 = JSON.parse(JSON.stringify(response.data.shops));
              this.total_pages1 = response.data.total_pages;
            } else if (flag === 2) {
              this.shops2 = JSON.parse(JSON.stringify(response.data.shops));
              this.total_pages2 = response.data.total_pages;
            }
          })
          .catch((error) => {
            console.log(error);
          });
      },

      // release() {
      //   this.errors = [];
      //   var formData = {
      //     authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
      //     shop_id: this.shop_id,
      //     approved: this.approved,
      //   };
      //   axios
      //     .post("/api/shop/approval", formData)
      //     .then((response) => {
      //       if (response.status == 200) {
      //         toast({
      //           message: "商店审核通过！",
      //           type: "is-success",
      //           dismissible: true,
      //           pauseOnHover: true,
      //           duration: 2000,
      //           position: "bottom-right",
      //         });
      //       } else {
      //         toast({
      //           message: response.status,
      //           type: "warning",
      //           dismissible: true,
      //           pauseOnHover: true,
      //           duration: 2000,
      //           position: "bottom-right",
      //         });
      //       }
      //     })
      //     .catch((error) => {
      //       if (error.response) {
      //         // for (const property in error.response.data) {
      //         // this.errors.push(`${property}: ${error.response.data[property]}`)
      //         this.errors.push(error.response.data);
      //         // }

      //         console.log(JSON.stringify(error.response));
      //       } else if (error.message) {
      //         this.errors.push("Something went wrong. Please try again");
      //         console.log(JSON.stringify(error.response));
      //       }
      //     });
      // },
    },
  };
</script>
