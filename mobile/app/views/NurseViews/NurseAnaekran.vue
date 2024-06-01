<template>
  <Page class="page">
    <ActionBar class="action-bar" flat />
    <ScrollView class="home-panel">
      <FlexboxLayout flexDirection="column" justifyContent="space-around" alignItems="center">
        <Image src="~/assets/baskent.png" class="logo" />
        <Image src="~/assets/icon.png" class="profile-icon" @tap="goToProfile" />
        <Label :text="'Hoşgeldiniz, ' + this.$store.state.user.name + ' ' + $store.state.user.surname + '!'"
          class="action-label" />
        <Button text="Not Girişi" @tap="goX" class="btn" horizontalAlignment="center" verticalAlignment="middle" />
        <Button text="Ogrenci Durumunu Gör" @tap="goX" class="btn" horizontalAlignment="center"
          verticalAlignment="middle" />
        <Button text="Çıkış yap" @tap="cikisYap" class="exit-button" horizontalAlignment="center"
          verticalAlignment="middle" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>
  
<script scoped>
import Home from '~/Home.vue';
import Profile from '~/views/NurseViews/NurseProfil.vue';
export default {
  data() {
    return {};
  },
  methods: {
    goToProfile() {
      console.log("Image tıklandı!");
      this.$navigateTo(Profile);
    },
    cikisYap() {
      this.$store.commit('LOGOUT');
      this.$navigateTo(Home, {
        clearHistory: true,
      });
    }
  },

};
</script>
  
<style scoped>
.page {
  background-image: url('~/assets/backgroundd.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
}

.action-bar {
  background-color: #19191b;
  color: #fff;
}

.home-panel {
  padding: 20;
}

.logo {
  width: 70em;
  height: 70em;
  margin-left: 120em auto;
}

.action-label {
  display: flex;
  text-align: center;
  color: #ffffff;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
}

.profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 0em 0;
}


.exit-button {
  margin-top: 10em;
  background-color: red;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}

.btn {
  margin-top: 10em;
  background-color: #08F7FE80;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}</style>