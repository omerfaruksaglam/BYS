<template>
  <Page class="page">
    <ActionBar class="action-bar" flat />
    <ScrollView class="home-panel">
      <FlexboxLayout class="home-panel" flexDirection="column" justifyContent="space-around" alignItems="center">
        <Image src="~/assets/baskent.png" class="profile-icon" />
        <Label :text="'Hoşgeldiniz, ' + this.$store.state.user.name + ' ' + $store.state.user.surname + '!'"
          class="action-label" />
        <Button text="Beceri Yönetimi" @tap="goSkills" class="btn" horizontalAlignment="center" verticalAlignment="middle" />
        <Button text="Kullanıcı Yönetimi" @tap="goToKullaniciEkle" class="btn" horizontalAlignment="center"
          verticalAlignment="middle" />

        <Button text="Giriş Aktiviteleri" @tap="goToLog" class="btn" horizontalAlignment="center" verticalAlignment="middle" />
        <Button text="Sistem Aktiviteleri" @tap="goToAct" class="btn" horizontalAlignment="center" verticalAlignment="middle" />

        <Button text="Çıkış yap" @tap="cikisYap" class="exit-button" horizontalAlignment="center"
          verticalAlignment="middle" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>

<script scoped>
import Home from '~/Home.vue';
import KullaniciListele from '~/views/AdminViews/KullaniciYonetimi/KullaniciListele.vue';
import Skills from '~/views/AdminViews/BeceriYonetimi/Skills.vue';
import Report from '~/views/Report/Report.vue';
import LoginLog from '~/views/AdminViews/Loglar/LoginLog.vue';
import ActivitiesLog from '~/views/AdminViews/Loglar/ActivitiesLog.vue';
export default {
  data() {
    return {};
  },
  methods: {
    goToKarne() {
      this.$navigateTo(Report);
    },
    goToProfile() {
      console.log("Image tıklandı");
    },
    goToKullaniciEkle() {
      this.$navigateTo(KullaniciListele);
    },
    goSkills() {
      this.$navigateTo(Skills);
    },

    goToLog() {
      this.$navigateTo(LoginLog);
    },
    goToAct() {
      this.$navigateTo(ActivitiesLog);
    },
    cikisYap() {
      this.$store.commit('LOGOUT');
      this.$navigateTo(Home, {
        clearHistory: true,

      });
    },
  }

}
</script>

<style scoped>

.ns-dark .Label {
  color: #fff;
}

.ns-dark .action-bar {
  background-color: #19191b;
  color: #fff;
}

.ns-dark .home-panel {
  padding: 20;
}


.ns-dark .action-label {
  display: flex;
  text-align: center;
  color: #ffffff;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
}

.ns-dark .profile-icon {
  width: 200em;
  height: 200em;
  border-radius: 20em;
  margin: 0em 0;
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

.ns-dark .btn {
  margin-top: 10em;
  background-color: #08F7FE80;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}








.ns-light .action-bar {
  background-color: #dddddd;
  color: #fff;
}

.ns-light .home-panel {
  padding: 20;
}


.ns-light .action-label {
  display: flex;
  text-align: center;
  color: #000;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
}

.ns-light .profile-icon {
  width: 200em;
  height: 200em;
  border-radius: 20em;
  margin: 0em 0;
}


.ns-light .exit-button {
  margin-top: 10em;
  background-color: red;
  color: #000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}

.ns-light .btn {
  margin-top: 10em;
  background-color: #33c5c9;
  color: #000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}
</style>