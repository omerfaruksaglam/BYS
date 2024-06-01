<template>
  <Page class="page">
    <ActionBar class="action-bar" title="Profil" flat />
    <ScrollView class="home-panel">
      <FlexboxLayout class="home-panel" flexDirection="column" justifyContent="space-around" alignItems="center">
        <StackLayout class="profile-container">
          <Image src="~/assets/icon.png" class="profile-icon" @tap="goToProfile" />

          <Label :text="'Isim: ' + '\t\t\t' + this.$store.state.user.name" class="action-label" />


          <Label :text="'Soyisim: ' + '\t\t' + this.$store.state.user.surname" class="action-label" />


          <Label :text="'Numara: ' + '\t\t' + this.$store.state.user.userId" class="action-label" />


          <Label :text="'Giriş Yılı: ' + '\t\t' + this.$store.state.user.kayit_tarihi.substring(0, 10)"
            class="action-label" justify-content="flex-start" />




        </StackLayout>
        <Button text="Şifre Değiştir" @tap="sifreDegistireGit" class="exit-button" horizontalAlignment="center"
          verticalAlignment="middle" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>
  
<script scoped>
import SifreDegistir from '~/views/NurseViews/NurseSifreDegistir.vue';
export default {
  data() {
    return {
      girisYili: this.$store.state.user.kayit_tarihi.substring(0, 10)
    };
  },
  methods: {
    goToProfile() {
      console.log("Image tıklandı!");
    },
    sifreDegistireGit() {
      this.$navigateTo(SifreDegistir);
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
  margin-left: 100em auto;
}

.profile-container {
  align-items: center;
}

.action-label {
  display: flex;
  text-align: left;
  justify-content: flex-start;
  color: #ffffff;
  font-size: 16em;
  text-wrap: false;
  margin-bottom: 10em;
  margin-right: 50em;
}

.welcome-label {
  color: #fff;
  text-align: center;
  font-size: 16em;
}

.profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 7em 0;
}

.button-grid {
  margin: 10px 0;
}

.number-button {


  margin-top: 30em;
  background-color: transparent;
  border-color: #D32F2F;
  border-width: 8em;
  color: #fff;
  border-radius: 50%;
  font-size: 24em;
  height: 100em;
  width: 100em;
  vertical-align: middle;
  text-align: center;
  line-height: 100em;
}

.exit-button {
  margin-top: 10em;
  background-color: green;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}</style>