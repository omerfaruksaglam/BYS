import Vue from 'nativescript-vue';
import Home from './Home';
import store from './store.js'; // Make sure this import comes after Vue and Vuex
import { ApplicationSettings, SystemAppearanceChangedEventData } from '@nativescript/core';
import { Theme } from '@nativescript/theme';



Theme.setMode(Theme.Auto);
ApplicationSettings.setString('theme', Theme.getMode());
console.log('theme', Theme.getMode());

Vue.registerElement('BarChart', () => require('@nativescript-community/ui-chart/charts').BarChart);
Vue.registerElement('PieChart', () => require('@nativescript-community/ui-chart/charts').PieChart);
Vue.registerElement('PDFView', () => require('@nativescript/pdf').PDFView);


new Vue({
  render: (h) => h('frame', [h(Home)]),
  store
}).$start();


