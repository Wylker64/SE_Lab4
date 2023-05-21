<!-- 展示账户流水 -->
<template>
  <div class="fundsflow">
    <div class="columns">
      <div class="column is-2">
        <div class="button is-link" @click="go_back()">返回</div>
      </div>
      <div class="column is-5 is-offset-3">
        <h3 class="title">流水展示</h3>
      </div>
      <div class="column is-2">
        <div class="select is-primary">
          <select v-model="select">
            <option value="0">全部流水</option>
            <option value="1">近一个月流水</option>
            <option value="2">近一周流水</option>
          </select>
        </div>
      </div>
    </div>
    <div class="columns">
      <div class="column is-6 is-offset-3">
        <table class="table is-fullwidth">
          <thead>
            <tr>
              <th>流入</th>
              <th>流出</th>
              <th>金额</th>
              <th>类别</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in items">
              <td>item.roll_in</td>
              <td>item.roll_out</td>
              <td>item.amount</td>
              <td v-if="item.type == 0">交易</td>
              <td v-if="item.type == 1">佣金</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";

  export default {
    data() {
      return {
        select: 0,
        account_id: 0,
        items: [],
      };
    },
    mounted() {
      this.account_id = this.$route.params.id;
    },
    methods: {
      show() {
        console.log(this.select);
      },
      getInfo() {
        var formData = {
          authorize: JSON.parse(localStorage.getItem("authorize") || "{}"),
          select: this.select,
        };
        axios.post("/api/flow", formData).then((response) => {
          this.items = response.items;
        });
      },
      go_back() {
        this.$router.back();
      },
    },
    watch: {
      select: {
        handler() {
          this.getInfo();
          console.log(this.select);
        },
      },
    },
  };
</script>
