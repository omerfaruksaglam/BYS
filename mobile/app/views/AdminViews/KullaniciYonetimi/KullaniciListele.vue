<template>
  <Page class="page" @navigatedTo="onNavigatedTo">
    <ActionBar title="Kullanıcı Yönetimi" class="action-bar" />
    <ScrollView>
      <StackLayout> 
        <FlexboxLayout justifyContent="space-between" alignItems="center" class="search-container">
          <TextField v-model="searchQuery" hint="Kullanıcı ara" @textChange="filterUsers" class="search-input" />
          <Button text="+" @tap="goToAddUser" class="add-btn" />
        </FlexboxLayout>

        <Label v-if="filteredUsers.students && filteredUsers.students.length > 0" text="Öğrenciler" class="list-header" />
        <StackLayout v-for="student in filteredUsers.students" :key="student.userId" :class="{'passive' : !student.active}" @tap="onStudentTap(student)">
          <Label :text="`${student.userId} - ${student.name} ${student.surname}`" class="list-item" />
        </StackLayout>

        <Label v-if="filteredUsers.teachers && filteredUsers.teachers.length > 0" text="Öğretim Elemanları" class="list-header" />
        <StackLayout v-for="hoca in filteredUsers.teachers" :key="hoca.userId" :class="{'passive' : !hoca.active}" @tap="onHocaTap(hoca)">
          <Label :text="`${hoca.userId} - ${hoca.name} ${hoca.surname}`" class="list-item" />
        </StackLayout>

        <Label v-if="filteredUsers.nurses && filteredUsers.nurses.length>0" text="Hemşireler" class="list-header" />
        <StackLayout v-for="hemsire in filteredUsers.nurses" :key="hemsire.userId" :class="{'passive' : !hemsire.active}" @tap="onHemsireTap(hemsire)">
          <Label :text="`${hemsire.userId} - ${hemsire.name} ${hemsire.surname}`" class="list-item" />
        </StackLayout>

        <Label v-if="filteredUsers.admins && filteredUsers.admins.length > 0 " text="Yöneticiler" class="list-header" />
        <StackLayout v-for="admin in filteredUsers.admins" :key="admin.userId" :class="{'passive' : !admin.active}" @tap="onAdminTap(admin)">
          <Label :text="`${admin.userId} - ${admin.name} ${admin.surname}`" class="list-item" />
        </StackLayout>
      </StackLayout>
    </ScrollView>
  </Page>
</template>
<script scoped>
import Home from '~/Home.vue';
import KullaniciEkle from '~/views/AdminViews/KullaniciYonetimi/KullaniciEkle.vue';
import KullaniciDuzenle from '~/views/AdminViews/KullaniciYonetimi/KullaniciDuzenle.vue';


export default {
  data() {
    return {
      admins: null,
      teachers: null,
      students: null,
      nurses: null,
      searchQuery: '',
      filteredUsers: {
        admins: null,
        teachers: null,
        students: null,
        nurses: null,
      },
    };
  },
  methods: {
    goToAddUser() {
      this.$navigateTo(KullaniciEkle);
    },
    onHocaTap(hoca) {
      this.$navigateTo(KullaniciDuzenle, {
        props: {
          user: hoca
        }
      });
    },
    onHemsireTap(hemsire) {
      this.$navigateTo(KullaniciDuzenle, {
        props: {
          user: hemsire
        }
      });
    },
    onStudentTap(student) {
      
      this.$navigateTo(KullaniciDuzenle, {
        props: {
          user: student
        }
      });
      
    },
    onAdminTap(admin) {
      this.$navigateTo(KullaniciDuzenle, {
        props: {
          user: admin
        }
      });
    },
    async kullanicilarigetir() {
      await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/getusers/${this.$store.state.user.bolumId}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + this.$store.state.user.token
          },

        }).then(res => res.json())
        .then(res => {
          this.admins = res.admins;
          this.teachers = res.teachers;
          this.students = res.students;
          this.nurses = res.nurses;
          console.log(this.admins);
          console.log(this.teachers);
          console.log(this.students);
          console.log(this.nurses);
        })
        this.filterUsers(); 

    },

    filterUsers(){
      const searchText = this.searchQuery.trim().toLowerCase();
      if (!searchText) {
        this.filteredUsers.admins = this.admins;
        this.filteredUsers.teachers = this.teachers;
        this.filteredUsers.students = this.students;
        this.filteredUsers.nurses = this.nurses;
      } else {
        ['admins', 'teachers', 'students', 'nurses'].forEach(role => {
          this.filteredUsers[role] = this[role].filter(user => {
            const userIdString = String(user.userId).toLowerCase();
            const nameLower = user.name.toLowerCase();
            const surnameLower = user.surname.toLowerCase();
            return isNaN(parseInt(searchText[0]))
              ? nameLower.includes(searchText) || surnameLower.includes(searchText)
              : userIdString.includes(searchText);
          });
        });
      }
    },

    onNavigatedTo() {
      this.kullanicilarigetir();
    }
  },
  watch: {
    searchQuery() {
      this.filterUsers();
    }
  },
  mounted() {
    this.kullanicilarigetir();
  }

};
</script>
  
<style scoped>

.ns-light .search-input {
    margin: 10;
    margin-left: 20;
    margin-right: 20;
    padding: 10;
    border-radius: 10;
    color: white;
    background-color: darkgray;
    placeholder-color: #000;
    /* Diğer stil tanımlamaları */
}
.ns-light .list-header {
  font-size: 16;
  font-weight: bold;
  padding: 10;
  margin: 5;
  text-align: left;
  color: #000;
}

.ns-light .list-item {
  margin-top: 5;
  margin-bottom: 5;
  margin-left: 15;
  margin-right: 15;
  padding: 10;
  background-color: darkgray;
  border-radius: 5;
  color: #000;
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
  margin-left: 120em auto;
}

.ns-light .profile-container {
  align-items: center;
}

.ns-light .action-label {
  display: flex;
  text-align: center;
  color: #000;
  font-size: 18em;
  text-wrap: true;
  margin-bottom: 10em;
}

.ns-light .welcome-label {
  color: #000;
  text-align: center;
  font-size: 16em;
}

.ns-light .profile-icon {
  width: 180em;
  height: 180pem;
  border-radius: 20em;
  margin: 0em 0;
}

.ns-light .button-grid {
  margin: 10px 0;
}

.ns-light .number-button {
  margin-top: 30em;
  background-color: transparent;
  border-color: #D32F2F;
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
  background-color: #08F7FE80;
  color: #000;
  border-radius: 7em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;

}

.ns-light .passive {
  color: #1e1c1c; /* Yazı rengini soluk gri yapar */
  background-color: #dcd9d9; /* Arka plan rengini koyu gri yapar */
  opacity: 0.5; /* Yarı saydam görünüm için şeffaflık ekler */
}

.ns-light .search-input {
  flex-grow: 1;
  margin-right: 8; /* Arama kutusu ve buton arasında boşluk */
  border-width: 0; /* Kutu çevresindeki kenarlığı kaldır */
  border-radius: 8;
}

.ns-light .add-btn {
  background-color: #33c5c9;
  border-radius: 8; /* Buton köşe yuvarlaklığı */
  width: 40; /* Buton genişliği */
  height: 40; /* Buton yüksekliği */
  font-size: 18; /* Artı işaretinin boyutu */
  vertical-align: center; /* İçerik dikey hizalama */
  color:#000
}












.ns-dark .search-input {
    margin: 10;
    margin-left: 20;
    margin-right: 20;
    padding: 10;
    border-radius: 10;
    color: white;
    background-color: grey;
    placeholder-color: #CCCCCC;
    /* Diğer stil tanımlamaları */
}
.ns-dark .list-header {
  font-size: 16;
  font-weight: bold;
  padding: 10;
  margin: 5;
  text-align: left;
  color: #ffffff;
}

.ns-dark .list-item {
  margin-top: 5;
  margin-bottom: 5;
  margin-left: 15;
  margin-right: 15;
  padding: 10;
  background-color: grey;
  border-radius: 5;
  color: #ffffff;
}

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
  margin-left: 120em auto;
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
  margin: 0em 0;
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

.ns-dark .passive {
  color: #aaa; /* Yazı rengini soluk gri yapar */
  background-color: #333; /* Arka plan rengini koyu gri yapar */
  opacity: 0.5; /* Yarı saydam görünüm için şeffaflık ekler */
}

.ns-dark .search-input {
  flex-grow: 1;
  margin-right: 8; /* Arama kutusu ve buton arasında boşluk */
  border-width: 0; /* Kutu çevresindeki kenarlığı kaldır */
  border-radius: 8;
}

.ns-dark .add-btn {
  background-color: #08F7FE80;
  border-radius: 8; /* Buton köşe yuvarlaklığı */
  width: 40; /* Buton genişliği */
  height: 40; /* Buton yüksekliği */
  font-size: 18; /* Artı işaretinin boyutu */
  vertical-align: center; /* İçerik dikey hizalama */
  color:#ffffff
}
</style>