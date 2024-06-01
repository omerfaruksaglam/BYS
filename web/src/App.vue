<template>
  <div id="app">
    <Header v-if="$route.meta.header"></Header>
    <router-view></router-view>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
export default {
  name: 'App',
  components: {
    Header
  }
}
</script>
