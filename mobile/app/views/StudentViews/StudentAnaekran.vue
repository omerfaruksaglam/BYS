<template>
  <Page class="page">
    <ActionBar class="action-bar" title="" flat />
    <ScrollView class="home-panel">
      <FlexboxLayout class="home-panel" flexDirection="column" justifyContent="space-around" alignItems="center">
        <Image src="~/assets/baskent.png" class="logo" />
        <StackLayout class="profile-container">
          <Image src="~/assets/icon.png" class="profile-icon" @tap="goToProfile" />
          <Label :text="'Hoşgeldiniz, ' + this.$store.state.user.name + ' ' + $store.state.user.surname + '!'"
            class="welcome-label" />
          <Label text="Lütfen işlem yapmak istediğiniz sınıfı seçin." class="action-label" />
        </StackLayout>
        <GridLayout
            v-for="row in Math.ceil(bolumInfo.yilSayisi / 2)"
            :key="row"
            columns="*,*"
            rows="auto"
            class="button-grid"
          >
            <Button
              v-for="col in 2"
              :key="col"
              :text="row * 2 - 2 + col <= bolumInfo.yilSayisi ? row * 2 - 2 + col : ''"
              :col="col - 1"
              class="number-button"
              @tap="eylemeGit(row * 2 - 2 + col)"
              v-if="row * 2 - 2 + col <= bolumInfo.yilSayisi"
            />
          </GridLayout>
        <Button text="Çıkış yap" @tap="cikisYap" class="exit-button" horizontalAlignment="center"
          verticalAlignment="middle" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>
  
<script scoped>
import Home from '~/Home.vue'
import Profile from '~/views/StudentViews/StudentProfil.vue'
import StudentSkills from './StudentSkills.vue';
export default {
  props: ['bolumInfo'],

  data() {
    return {};
  },
  methods: {
    eylemeGit(sinif) {
        if (sinif <= this.bolumInfo.yilSayisi) {
          this.$navigateTo(StudentSkills, {
            props: { student: this.$store.state.user, fromStatistic: false, sinif: sinif, notVer: this.bolumInfo.ogrenciNotVerebilirmi ? true : false, bolumInfo: this.bolumInfo },
          });
        }
      },
    goToProfile() {
      console.log("Image tıklandı, profile gidiliyor!");
      this.$navigateTo(Profile,
        {
          props: { bolumInfo: this.bolumInfo }
        });
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
  text-align: center;
  color: #ffffff;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
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
  background-color: red;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}

















.ns-light .action-bar {
  background-color: #dddddd;
  color: #000000;
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
  text-align: center;
  color: #000000;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
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
  border-color: #eb4c4c;
  border-width: 8em;
  color: #000000;
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
  background-color: #ff5050;
  color: #000000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}
</style>