import Vuex from 'vuex';

const sessionStoragePlugin = store => {
    // Uygulama başladığında state'i yükle
    if (sessionStorage.getItem('vuex')) {
      const storedState = JSON.parse(sessionStorage.getItem('vuex'));
      store.replaceState(storedState);
    }
  
    // Her mutasyondan sonra state'i kaydet
    store.subscribe((mutation, state) => {
      sessionStorage.setItem('vuex', JSON.stringify(state));
    });
  };

const store = new Vuex.Store({
    state: {
        user: {
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
    plugins: [sessionStoragePlugin]
});

export default store;