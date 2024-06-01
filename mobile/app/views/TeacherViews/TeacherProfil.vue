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

            <!--<Switch :checked="currentTheme" @checkedChange="toggleTheme"  />-->

        </StackLayout>
                

        <Button text="Şifre Değiştir" @tap="sifreDegistireGit" class="exit-button" horizontalAlignment="center"
          verticalAlignment="middle" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>
  
<script scoped>
import SifreDegistir from '~/views/TeacherViews/TeacherSifreDegistir.vue';
import { ApplicationSettings } from '@nativescript/core';
import { Theme } from '@nativescript/theme';

export default {
  data() {
    return {
      currentTheme: ApplicationSettings.getString('theme') === 'dark' ? true : false,
      girisYili: this.$store.state.user.kayit_tarihi.substring(0, 10)
    };
  },
  methods: {
    toggleTheme(event) {
     // const currentTheme = ApplicationSettings.getString('theme', 'light');
      const newTheme = event.value ? 'dark' : 'light';
      Theme.setMode(newTheme === 'dark' ? Theme.Dark : Theme.Light);
      ApplicationSettings.setString('theme', newTheme);
      this.currentTheme = event.value;

    },
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

.ns-dark .action-bar {
  background-color: #19191b;
  color: #fff;
}

.ns-dark .home-panel {
  padding: 20;
}

.ns-dark .logo {
  width: 70em;
  height: 70em;
  margin-left: 100em auto;
}

.ns-dark .profile-container {
  align-items: center;
}

.ns-dark .action-label {
  display: flex;
  text-align: left;
  justify-content: flex-start;
  color: #ffffff;
  font-size: 16em;
  text-wrap: false;
  margin-bottom: 10em;
  margin-right: 50em;
}

.ns-dark .welcome-label {
  color: #fff;
  text-align: center;
  font-size: 16em;
}

.ns-dark .profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 7em 0;
}

.ns-dark .button-grid {
  margin: 10px 0;
}

.ns-dark .number-button {


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

.ns-dark .exit-button {
  margin-top: 10em;
  background-color: green;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}









.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}

.ns-light .home-panel {
  padding: 20;
}

.ns-light .logo {
  width: 70em;
  height: 70em;
  margin-left: 100em auto;
}

.ns-light .profile-container {
  align-items: center;
}

.ns-light .action-label {
  display: flex;
  text-align: left;
  justify-content: flex-start;
  color: #000000;
  font-size: 16em;
  text-wrap: false;
  margin-bottom: 10em;
  margin-right: 50em;
}

.ns-light .welcome-label {
  color: #000000;
  text-align: center;
  font-size: 16em;
}

.ns-light .profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 7em 0;
}

.ns-light .button-grid {
  margin: 10px 0;
}

.ns-light .number-button {


  margin-top: 30em;
  background-color: transparent;
  border-color: #fd6c6c;
  border-width: 8em;
  color: #000;
  border-radius: 50%;
  font-size: 24em;
  height: 100em;
  width: 100em;
  vertical-align: middle;
  text-align: center;
  line-height: 100em;
}

.ns-light .exit-button {
  margin-top: 10em;
  background-color: rgb(104, 197, 104);
  color: #000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}
</style>