<template>
  <div class="page-warehouse">
    <div class="title">{{ id }}</div>
    <div class="columns">
      <div class="column is-6">
        <h1 class="title">商店名称：{{ this.shop.name }}</h1>
        <h1 class="title is-5">商店编号：{{ this.shop.id }}</h1>
        <h1 class="title is-5">商店类别：{{ this.shop.type }}</h1>
        <p>{{ this.shop.description }}</p>
      </div>
      <div class="column is-2 offset-4">
        <div class="field">
          <label>我的余额</label>
          <div class="control">
            <input type="text" class="input" v-model="account.balance" readonly />
          </div>
        </div>
        <form @submit.prevent="refund">
          <div class="field">
            <label>充值金额</label>
            <div class="control">
              <input type="text" class="input is-primary" v-model="added" />
            </div>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-primary">确认充值</button>
            </div>
          </div>
        </form>
      </div>
      <div class="column is-2">
        <br />
        <div class="button is-link" @click="push_fundsflow()">账户流水</div>
        <hr />
        <div class="button is-primary" @click="push_shoporders()">商店订单</div>
      </div>
      <div class="column is-offset-1">
        <button class="button is-danger" @click="deleteS">删除商店</button>
      </div>
    </div>

    <!-- 现存商品和记录展示部分 -->
    <div class="columns">
      <div class="column is-6">
        <h2 class="title is-4">商品列表</h2>
        <form @submit.prevent="getInfo">
          <div class="column is-2">
            <div class="field">
              <label>页码/共{{ product_total_pages }}页</label>
              <div class="control">
                <input type="text" class="input" v-model="product_page_num" />
              </div>
            </div>
            <div class="field">
              <div class="control">
                <button class="button is-dark">搜索</button>
              </div>
            </div>
          </div>
        </form>
        <table class="table is-fullwidth">
          <thead>
            <tr>
              <th>商品名称</th>
              <th>商品id</th>
              <th>商品价格</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="product in products" :key="product.id">
              <td>{{ product.name }}</td>
              <td>{{ product.id }}</td>
              <td>{{ product.price }}</td>
              <td v-if="product.createStatus === 'APPROVED'">有效</td>
              <td v-if="product.createStatus === 'WAITING'">审核中</td>
              <td>
                <button class="button is-danger is-small" @click="deleteP(product.id)">删除</button>
                <button class="button is-primary is-small" @click="prod_id = product.id">修改</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="column is-6">
        <h2 class="title is-4">申请列表</h2>
        <form @submit.prevent="getInfo">
          <div class="column is-2">
            <div class="field">
              <label>页码/共{{ record_total_pages }}页</label>
              <div class="control">
                <input type="text" class="input" v-model="record_page_num" />
              </div>
            </div>
            <div class="field">
              <div class="control">
                <button class="button is-dark">搜索</button>
              </div>
            </div>
          </div>
        </form>
        <table class="table is-fullwidth">
          <thead>
            <tr>
              <th>商品名称</th>
              <th>申请类别</th>
              <th>申请状态</th>
            </tr>
          </thead>

          <tbody>
            <tr v-for="record in records" :key="record.id">
              <td>{{ record.name }}</td>
              <td v-if="record.createStatus != null">新增申请</td>
              <td v-if="record.modifyStatus != null">修改申请</td>
              <td v-if="record.createStatus != null">{{ record.createStatus }}</td>
              <td v-if="record.modifyStatus != null">{{ record.modifyStatus }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <br />
    <br />
    <div class="columns">
      <div class="column">
        <h1 class="title">添加商品</h1>

        <form @submit.prevent="add">
          <div class="field">
            <label>商品名</label>
            <div class="control">
              <input type="text" class="input" v-model="name1" />
            </div>
          </div>

          <div class="field">
            <label>商品描述</label>
            <div class="control">
              <input type="text" class="input" v-model="description1" />
            </div>
          </div>

          <div class="field">
            <label>价格</label>
            <div class="control">
              <input type="text" class="input" v-model="price1" />
            </div>
          </div>

          <!-- <div class="field">
            <label>添加图片</label>
            <div class="control">
              <input type="file" class="input" @change="onFileChange($event, 1)" />
            </div>
          </div> -->
          <div class="field">
            <label>添加图片</label>
            <div class="control">
              <input type="file" class="input" multiple @change="onFileChange1" />
            </div>
          </div>
          <br />
          <div class="notification is-danger" v-if="errors1.length">
            <p v-for="error in errors1" v-bind:key="error">{{ error }}</p>
          </div>
          <br />
          <div class="field">
            <div class="control">
              <button class="button is-dark">添加商品</button>
            </div>
          </div>
        </form>
      </div>
      <hr />
      <div class="column">
        <h1 class="title">更新商品</h1>

        <form @submit.prevent="update">
          <div class="field">
            <label>商品id</label>
            <div class="control">
              <input type="text" class="input" v-model="prod_id" readonly />
            </div>
          </div>
          <div class="field">
            <label>商品名</label>
            <div class="control">
              <input type="text" class="input" v-model="name2" />
            </div>
          </div>

          <div class="field">
            <label>商品描述</label>
            <div class="control">
              <input type="text" class="input" v-model="description2" />
            </div>
          </div>

          <div class="field">
            <label>价格</label>
            <div class="control">
              <input type="text" class="input" v-model="price2" />
            </div>
          </div>

          <div class="field">
            <label>添加图片</label>
            <div class="control">
              <input type="file" class="input" multiple @change="onFileChange2" />
            </div>
          </div>

          <div class="notification is-danger" v-if="errors2.length">
            <p v-for="error in errors2" v-bind:key="error">{{ error }}</p>
          </div>
          <br />
          <div class="field">
            <div class="control">
              <button class="button is-dark">更新商品</button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- <div class="column is-4">
      <h1 class="title">删除商品</h1>
      <form @submit.prevent="deleteP">
        <div class="field">
          <label>商品id</label>
          <div class="control">
            <input type="text" class="input" v-model="prod_id" />
          </div>
        </div>

        <div class="notification is-danger" v-if="errors.length">
          <p v-for="error in errors2" v-bind:key="error">{{ error }}</p>
        </div>

        <div class="field">
          <div class="control">
            <button class="button is-dark">删除商品</button>
          </div>
        </div>
      </form>
    </div> -->
  </div>
</template>

<script>
  import axios from "axios";
  import { toast } from "bulma-toast";
  export default {
    name: "WareHouse",
    data() {
      return {
        id: "0", //从上一页面传来的商店账户id
        prod_id: "", //修改商品信息时提供的商品id
        image1: [],
        name1: "",
        description1: "",
        price1: 0,
        name2: "",
        description2: "",
        price2: 0,
        image2: [],
        added: 0, //记录增加的金额
        errors1: [],
        errors2: [],
        flag: 0,
        shop: {}, //商店信息，和商店账户并不完全等同，商店账户和个人账户是相同的类
        account: {}, //商店账户
        products: [], //从后端接受的已经成功通过申请的商品
        records: [], //申请记录
        product_page_num: 1,
        record_page_num: 1,
        product_total_pages: 0,
        record_total_pages: 0,
      };
    },
    mounted() {
      document.title = "商店管理";
      this.id = this.$route.params.id;
      this.getInfo();
      console.log(this.products);
    },
    methods: {
      onFileChange1(event) {
        this.image1 = event.target.files;
        console.log(this.image1.length);
        // let reader = new FileReader();
        // reader.readAsArrayBuffer(this.image1[0]);
        // reader.onload = function(e) {
        //   var ints = new Uint8Array(e.target.result); //要使用读取的内容，所以将读取内容转化成Uint8Array
        //   ints = ints.slice(0, 5000); //截取一段读取的内容
        //   console.log("读取的内容如下：");
        //   console.log(ints);
        // };
      },

      onFileChange2(event) {
        this.image2 = event.target.files;
      },

      //从后端获取页面的全部信息
      getInfo() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          product_page_num: this.product_page_num,
          record_page_num: this.record_page_num,
        };
        var url = "/api/shop/" + this.id + "/vendor";
        axios
          .post(url, formData)
          .then((response) => {
            console.log(response);
            this.shop = response.data.shop;
            this.account = response.data.account;
            this.products = response.data.products;
            this.records = response.data.records;
            this.product_total_pages = response.data.product_total_pages;
            this.record_total_pages = response.data.record_total_pages;
          })
          .catch((error) => {
            console.log(error);
          });
      },
      push_fundsflow() {
        var url = "/funds-flow/" + this.id;
        this.$router.push({ path: url });
      },
      push_shoporders() {
        var url = "/shop-orders/" + this.id;
        this.$router.push({ path: url });
      },
      //充值
      refund() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          account_id: this.account.id,
          amount: this.added,
        };
        axios
          .post("/api/account/charge", formData)
          .then((response) => {
            console.log(formData);
            if (response.status == 200) {
              toast({
                message: "充值成功！",
                type: "is-success",
                dismissible: true,
                pauseOnHover: true,
                duration: 2000,
                position: "bottom-right",
              });
              //成功后重置页面
              this.getInfo();
              this.added = 0;
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

      //新增商品
      async add() {
        this.errors1 = [];
        if (this.name1.length === 0) {
          this.errors1.push("店名不可为空");
        }
        if (!/^.{1,120}$/.test(this.description1)) {
          this.errors1.push("简介不可为空，不可超过120字符");
        }
        if (!/^\d+(\.\d+)?$/.test(this.price1) || this.price1 == 0) {
          this.errors1.push("价格不符合规范");
        }
        if (!this.image1.length) {
          this.errors1.push("未上传图片");
        }
        if (!this.errors1.length) {
          let data = {
            username: JSON.parse(localStorage.getItem("authorize") || "{}").username,
            user_id: JSON.parse(localStorage.getItem("authorize") || "{}").id,
            shop_id: this.id,
            name: this.name1,
            description: this.description1,
            price: this.price1,
          };
          let formData = new FormData();
          for (let i in data) {
            formData.append(i, data[i]);
          }

          const config = {
            headers: {
              "Content-Type": "multipart/form-data", //将请求头设置为multipart/form-data
            },
          };
          console.log(this.image1.length);
          for (let i = 0; i < this.image1.length; i++) {
            formData.append("images", this.image1[i]);
          }

          for (let [a, b] of formData.entries()) {
            console.log(a, b, "--------------");
          }
          axios
            .post("/api/product/create", formData, config)
            .then((response) => {
              if (response.status == 200) {
                toast({
                  message: "提交添加申请成功!",
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
              if (error.response) {
                this.errors1.push(error.response.data);
                console.log(error.response.data);
              } else if (error.message) {
                this.errors1.push("Something went wrong. Please try again");
                console.log(error);
              }
            });
        }
      },

      //更改商品
      update() {
        this.errors2 = [];
        if (this.name2.length === 0) {
          this.errors2.push("店名不可为空");
        }
        if (!/^.{1,120}$/.test(this.description2)) {
          this.errors2.push("简介不可为空，不可超过120字符");
        }
        if (!/^\d+(\.\d+)?$/.test(this.price2) || this.price2 == 0) {
          this.errors2.push("价格不符合规范");
        }
        if (!this.image2.length) {
          this.errors2.push("未上传图片");
        }
        if (!this.errors2.length) {
          let data = {
            username: JSON.parse(localStorage.getItem("authorize") || "{}").username,
            user_id: JSON.parse(localStorage.getItem("authorize") || "{}").id,
            product_id: this.prod_id,
            name: this.name2,
            description: this.description2,
            price: this.price2,
          };
          let formData = new FormData();
          for (let i in data) {
            formData.append(i, data[i]);
          }

          const config = {
            headers: {
              "Content-Type": "multipart/form-data", //将请求头设置为multipart/form-data
            },
          };
          console.log(this.image2.length);
          for (let i = 0; i < this.image2.length; i++) {
            formData.append("images", this.image2[i]);
          }

          for (let [a, b] of formData.entries()) {
            console.log(a, b, "--------------");
          }

          axios
            .post("/api/product/modify", formData, config)
            .then((response) => {
              if (response.status === 200) {
                toast({
                  message: "提交修改申请成功!",
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
                for (const property in error.response.data) {
                  this.errors.push(`${property}: ${error.response.data[property]}`);
                }

                console.log(JSON.stringify(error.response.data));
              } else if (error.message) {
                this.errors.push("Something went wrong. Please try again");

                console.log(JSON.stringify(error));
              }
            });
        }
      },
      deleteP(prod_id) {
        this.errors = [];
        if (!this.errors.length) {
          const formData = {
            authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
            product_id: prod_id,
          };

          axios
            .post("/api/product/delete", formData)
            .then((response) => {
              this.getInfo();
              console.log(response);
              if (response.status == 200) {
                this.prod_id = "";
                toast({
                  message: "商品已删除!",
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
                console.log(JSON.stringify(error.response.data));
              } else if (error.message) {
                this.errors.push("Something went wrong. Please try again");

                console.log(JSON.stringify(error));
              }
            });
        }
      },
      deleteS() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          shop_id: this.id,
        };
        console.log("shopdelete");
        axios
          .post("/api/shop/delete", formData)
          .then((response) => {
            console.log("nihao");
            console.log(response.data);
            if (response.status == 200) {
              toast({
                message: "商店删除已提交，等待审核!",
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
            console.log(error.response);
            if (error.response) {
              this.errors.push(error.response.data);
              console.log(JSON.stringify(error.response.data));
            } else if (error.message) {
              this.errors.push("Something went wrong. Please try again");
              console.log(JSON.stringify(error));
            }
          });
      },
    },
  };
</script>
