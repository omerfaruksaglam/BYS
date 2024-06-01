<template>
  <Page class="page" >
    <ActionBar class="thome-action-bar" flat>
    </ActionBar> 
    <ScrollView class="thome-home-panel">
      <FlexboxLayout class="thome-home-panel" flexDirection="column" justifyContent="space-around" alignItems="center">
        <Image src="~/assets/baskent.png" class="thome-logo" />
        <Image src="~/assets/icon.png" class="thome-profile-icon" @tap="goToProfile" />
        <Label :text="'Hoşgeldiniz, ' + this.$store.state.user.name + ' ' + $store.state.user.surname + '!'"
          class="thome-action-label" />
        <Button text="Not Girişi" @tap="goToOgrenciSecNotVer" class="thome-btn" horizontalAlignment="center"
          verticalAlignment="middle" />
        <Button text="Ogrenci Durumunu Gör" @tap="goToOgrenciSecDurumGor" class="thome-btn" horizontalAlignment="center"
          verticalAlignment="middle" />
        <Button text="Çıkış yap" @tap="cikisYap" class="thome-exit-button" horizontalAlignment="center"
          verticalAlignment="middle" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>

<script scoped>
/**
 * 1) Karşilama ekrani
 * 2) Login Ekranlari
 * 3) Teacher Durum gor sayfasi
 * 4)Beceri sec sayfasi
 */

import Home from '~/Home.vue';
import Profile from '~/views/TeacherViews/TeacherProfil.vue';
import OgrenciSec from '~/views/TeacherViews/OgrenciSec.vue';
import { ApplicationSettings } from '@nativescript/core';
import { Theme } from '@nativescript/theme';
export default {
  props: ['bolumInfo'],
  
  data() {

    return {
      theme: ApplicationSettings.getString('theme', 'light'),
    };
  },
  mounted() {
    this.applyTheme();
  },
  methods: {
    applyTheme() {
      // Cihazın ya da uygulama ayarlarının şu anki temasını al
      const currentTheme = ApplicationSettings.getString('theme', 'light');
      // Eğer şu anki tema, state'imizdeki temadan farklıysa güncelle
      if (this.theme !== currentTheme) {
        // Temayı NativeScript Theme modülünde güncelle
        Theme.setMode(currentTheme === 'dark' ? Theme.Dark : Theme.Light);
        // State'deki tema bilgisini güncelle
        this.theme = currentTheme;
        // Vue'nun reaktif sistemine dayanarak stil değişikliklerini zorla
        this.$forceUpdate();
      }
    },
    goToProfile() {

      console.log("Image tıklandı!");
      this.$navigateTo(Profile);

    },
    goToOgrenciSecDurumGor() {
      
      this.$navigateTo(OgrenciSec, {
        props: { notVer: false, bolumInfo: this.bolumInfo},
        // ... other navigation options
      });

    },
    goToOgrenciSecNotVer() {

      this.$navigateTo(OgrenciSec, {
        props: { notVer: true, bolumInfo: this.bolumInfo},
        // ... other navigation options
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
.ns-dark .thome-action-bar {
  background-color:  #19191b;
  color: #ffffff;
}

.ns-dark .thome-home-panel {
  padding: 20;
}

.ns-dark .thome-logo {
  width: 70em;
  height: 70em;
  margin-left: 120em auto;
}

.ns-dark .thome-action-label {
  display: flex;
  text-align: center;
  color: #ffffff;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
}

.ns-dark .thome-profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 0em 0;
}


.ns-dark .thome-exit-button {
  margin-top: 10em;
  background-color: red;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}

.ns-dark .thome-btn {
  margin-top: 10em;
  background-color: #08F7FE80;
  color: #ffffff;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}







.ns-light .thome-action-bar {
  background-color:  #dddddd;
  color: #000000;
}

.ns-light .thome-home-panel {
  padding: 20;
}

.ns-light .thome-logo {
  width: 70em;
  height: 70em;
  margin-left: 120em auto;
}

.ns-light .thome-action-label {
  display: flex;
  text-align: center;
  color: #000000;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
}

.ns-light .thome-profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 0em 0;
}


.ns-light .thome-exit-button {
  margin-top: 10em;
  background-color: #ff5050;
  color: #000000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}

.ns-light .thome-btn {
  margin-top: 10em;
  background-color: #33c5c9;
  color: #000000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}
</style>
