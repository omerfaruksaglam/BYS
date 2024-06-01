<template>
  <Page class="page">
    <ActionBar class="thome-action-bar" flat>
    </ActionBar>
    
    <ScrollView>
      
      <FlexboxLayout class="home-form" flexDirection="column" justifyContent="space-around" alignItems="center">
        <Label class="hakkinda" @tap="onHakkinda()">Hakkında</Label>
        <!--<Switch :checked="currentTheme" @checkedChange="toggleTheme"  />-->
        <Image src="~/assets/study.png" stretch="aspectFit" class="home-logo" />
        <Label class="home-header">Beceri Yönetim Sistemi</Label>
        <Button text="Öğrenci" @tap="onStudentLogin" class="home-btn" />
        <Button text="Öğretim Elemanı" @tap="onTeacherLogin" class="home-btn" />
        <Button text="İkincil Puanlandırıcı" @tap="onNurseLogin" class="home-btn " />
        <Button text="Yönetici" @tap="onAdminLogin" class="home-btn" />
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>
  
<script>
import { ActionBar } from '@nativescript/core';
import AdminLogin from '~/views/AdminViews/AdminLogin.vue';
import StudentLogin from '~/views/StudentViews/StudentLogin.vue';
import NurseLogin from '~/views/NurseViews/NurseLogin.vue';
import TeacherLogin from '~/views/TeacherViews/TeacherLogin.vue';
import { ApplicationSettings } from '@nativescript/core';
import { Theme } from '@nativescript/theme';
import Hakkinda from '~/Hakkinda.vue';

export default {
  data() {
        return {
            currentTheme: ApplicationSettings.getString('theme') === 'dark' ? true : false,
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
    onStudentLogin() {
      this.$navigateTo(StudentLogin, {
        clearHistory: false,
      });

    },
    onHakkinda() {
      this.$navigateTo(Hakkinda, {
        clearHistory: false,
      });
    },
    onTeacherLogin() {
      this.$navigateTo(TeacherLogin, {
        clearHistory: false,
      });
    },
    onNurseLogin() {
      this.$navigateTo(NurseLogin, {
        clearHistory: false,
      });
    },
    onAdminLogin() {
      this.$navigateTo(AdminLogin, {
        clearHistory: false,
      });
    }
  },

}
</script>
  
<style scoped>
.ns-dark .home-action-bar {
  background-color: #19191B;
  color: #ffffff;
}

.ns-dark .home-logo {
  height: 30%;
  width: 30%;
  margin-bottom: 20em;
}


.ns-dark .home-form {
  margin: 20em;
  padding: 20em;

}

.ns-dark .home-header {
  display: flex;
  text-align: center;
  color: #ffffff;
  font-size: 18em;
  text-wrap: true;
}

.ns-dark .home-btn {
  margin-bottom: 5em;
  border-radius: 11em;
  background-color: #08F7FE80;
  width: 90%;
  height: 50em;
  color: #fff;
}

.ns-dark .hakkinda{
  color: #33c5c9;
  width: 100%;
  font-size: 12em;
  text-align: right;
  text-decoration: underline;

}









.ns-light .thome-action-bar {
  background-color: #dddddd;
  color: #000000;
}

.ns-light .home-logo {
  height: 30%;
  width: 30%;
  margin-bottom: 20em;
}


.ns-light .home-form {
  margin: 20em;
  padding: 20em;

}

.ns-light .home-header {
  display: flex;
  text-align: center;
  color: #000000;
  font-size: 18em;
  text-wrap: true;
}

.ns-light .home-btn {
  margin-bottom: 5em;
  border-radius: 11em;
  background-color: #33c5c9;
  width: 90%;
  height: 50em;
  color: #000000;
}
.ns-light .hakkinda{
  color: blue;
  width: 100%;
  font-size: 12em;
  text-align: right;
  text-decoration: underline;
}
</style>