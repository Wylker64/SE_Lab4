import { createStore } from "vuex";

export default createStore({
    state: {
        cart: {
            items: [],
        },
        isAuthenticated: false,
        isSeller: false,
        isBuyer: false,
        isAdmin: false,
        token: '',
        shop: '',
        isLoading: false
    },
    getters: {
        getToken(state) {
            return state.token;
        },
    },
    mutations: {
        initializeStore(state) {
            if (localStorage.getItem('cart')) {
                state.cart = JSON.parse(localStorage.getItem('cart'))
            } else {
                localStorage.setItem('cart', JSON.stringify(state.cart))
            }

            if (localStorage.getItem('token')) {
                state.token = localStorage.getItem('token')
                state.isAuthenticated = true
            } else {
                state.token = ''
                state.isAuthenticated = false
            }
        },
        setIsLoading(state, status) {
            state.isLoading = status
        },
        setShop(state, shop) {
            state.shop = shop

        },
        setToken(state, token) {
            state.token = token
            state.isAuthenticated = true
        },
        setBuyer(state, token) {
            state.token = token
            state.isBuyer = true
        },
        setSeller(state, token) {
            state.token = token
            state.isSeller = true
        },
        setAdmin(state, token) {
            state.token = token
            state.isAdmin = true
        },
        removeToken(state) {
            state.token = ''
            state.isAuthenticated = false
            state.isBuyer = false
            state.isSeller = false
            state.isAdmin = false
        },
    },
});