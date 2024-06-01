import Vuex from 'vuex';
import Vue from 'nativescript-vue';
Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        user: {
            hostIp: '',
            userId: -1,
            name: '',
            surname: '',
            kayit_tarihi: '',
            password: '',
            userRole: -2,
            bolumId: '',
            token: '',
        }
    },
    mutations: {
        LOGIN(state, payload) {
            state.user = {
                ...state.user,
                hostIp: payload.hostIp,
                userId: payload.userId,
                name: payload.name,
                surname: payload.surname,
                kayit_tarihi: payload.kayit_tarihi,
                password: payload.password,
                userRole: payload.userRole,
                bolumId: payload.bolumId,
                token: payload.token,
            };
            console.log("Store:");
            console.log(state.user);
        },
        LOGOUT(state) {
            state.user = {
                ...state.user,
                hostIp: '',
                userId: -1,
                name: '',
                surname: '',
                kayit_tarihi: '',
                password: '',
                userRole: -2,
                bolumId: '',
                token: '',
            };
        },
    },
});

export default store;