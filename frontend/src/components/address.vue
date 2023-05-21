<template>
  <div class="address">
    <div v-if="!is_edit" class="box">
      <div class="field">
        <p>
          <strong>{{ address.name }}</strong
          >{{ address.phone }}
          <!-- <strong>毕云天</strong>
          17721369528 -->
          <button @click="edit()">
            <span class="icon is-small">
              <i class="fas fa-pen"></i>
            </span>
          </button>

          <button @click="deleteA()">
            <span class="icon is-small">
              <i class="fas fa-ban"></i>
            </span>
          </button>
        </p>
        <ul>
          {{
            address.address
          }}
          <!-- 松花江路2500号 -->
        </ul>
      </div>
    </div>
    <div v-if="is_edit" class="box">
      <div class="column is-8">
        <form>
          <div class="field">
            <div class="control">
              <input type="text" class="input is-small" v-model="name" />
            </div>
          </div>
          <div class="field">
            <div class="control">
              <input type="text" class="input is-small" v-model="phone" />
            </div>
          </div>
          <div class="field">
            <div class="control">
              <input type="text" class="input is-small" v-model="location" />
            </div>
          </div>
        </form>
        <button @click="back()">
          <i class="fas fa-arrow-left"></i>
        </button>
        <button @click="back()">
          <i class="fas fa-check"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        is_edit: false,
        name: "",
        phone: "",
        location: "",
      };
    },
    props: {
      address: { type: Object },
    },
    methods: {
      edit() {
        this.is_edit = true;
      },
      back() {
        this.is_edit = false;
      },
      deleteA() {},
    },
    mounted() {
      this.name = this.address.name;
      this.phone = this.address.phone;
      this.location = this.address.address;
    },
  };
</script>
