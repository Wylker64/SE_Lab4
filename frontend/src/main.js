import { createApp } from "vue";
import App from "./App.vue";
// import router from './router'
import router from "@/router";
import store from "./store";
import axios from "axios";
// import tool from "../utils/tool.js";

// axios.defaults.baseURL = 'http://40f3d3d.nat123.fun'
// axios.defaults.baseURL = 'https://43.249.192.204:48724'
// axios.defaults.baseURL = 'http://192.168.31.206:8000'
createApp(App)
  .use(store)
  .use(router, axios)
  // .use(tool)
  .mount("#app");
