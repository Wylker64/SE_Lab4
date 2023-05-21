import { createRouter, createWebHistory } from "vue-router";

import SignUp from "../views/SignUp.vue";
import LogIn from "../views/LogIn.vue";
import Warehouse from "../views/Warehouse.vue";
import Review from "../views/Review.vue";
import Home from "../views/Home.vue";
import Cart from "../views/Cart.vue";
import Open from "../views/Open.vue";
import PersonalInfo from "../views/PersonalInfo.vue";
import Recharge from "../views/Recharge.vue";
import MyShops from "../views/MyShops.vue";
import Shop from "../views/Shop.vue";
import ReviewProduct from "../views/ReviewProduct.vue";
import Admin from "../views/Admin.vue";
import FundsFlow from "../views/FundsFlow";
import MyOrders from "../views/MyOrders";
import ShopOrders from "../views/ShopOrders";
import Order from "../views/Order";
import Pay from "../views/Pay";
import Promotion from "../views/Promotion";
import CreatePromotion from "../views/CreatePromotion";

const routes = [
  //以下为lab4新增界面
  {
    path: "/promotion",
    name: "promotion",
    component: Promotion,
  },
  {
    path: "/create-promotion",
    name: "createpromotion",
    component: CreatePromotion,
  },
  {
    path: "/funds-flow/:id",
    name: "fundsflow",
    component: FundsFlow,
  },
  {
    path: "/my-orders",
    name: "myorders",
    component: MyOrders,
  },
  {
    path: "/shop-orders/:id",
    name: "shoporders",
    component: ShopOrders,
  },
  {
    path: "/order",
    name: "order",
    component: Order,
  },
  {
    path: "/pay",
    name: "pay",
    component: Pay,
  },
  //以下为lab3及以前界面
  {
    path: "/warehouse/:id",
    name: "Warehouse",
    component: Warehouse,
    meta: {
      // requireLogin: true
    },
  },
  {
    path: "/cart",
    name: "Cart",
    component: Cart,
    meta: {
      // requireLogin: true
    },
  },
  {
    path: "/review",
    name: "Review",
    component: Review,
    meta: {
      // requireLogin: true
    },
  },
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      // requireLogin: true
    },
  },
  {
    path: "/sign-up",
    name: "SignUp",
    component: SignUp,
  },
  {
    path: "/log-in",
    name: "LogIn",
    component: LogIn,
  },
  {
    path: "/personal-info",
    name: "PersonalInfo",
    component: PersonalInfo,
  },
  {
    path: "/recharge",
    name: "Recharge",
    component: Recharge,
  },
  {
    path: "/my-shops",
    name: "MyShops",
    component: MyShops,
  },
  {
    path: "/open",
    name: "Open",
    component: Open,
  },
  {
    path: "/shop/:id",
    name: "Shop",
    component: Shop,
  },
  {
    path: "/review-product",
    name: "ReviewProduct",
    component: ReviewProduct,
  },
  {
    path: "/admin",
    name: "Admin",
    component: Admin,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, from, next) => {
//   if (to.matched.some((record) => record.meta.requireLogin) && !store.state.isAuthenticated) {
//     next({ name: "LogIn", query: { to: to.path } });
//   } else {
//     next();
//   }
// });

export default router;
