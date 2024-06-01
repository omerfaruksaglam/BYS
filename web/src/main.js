import { createApp } from 'vue'
import App from './App.vue'
import router from './router.js'
import store from './store.js'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faFilter } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'


library.add(faFilter)


createApp(App)
.use(store)
.component('font-awesome-icon', FontAwesomeIcon)
.use(router)
.mount('#app')
