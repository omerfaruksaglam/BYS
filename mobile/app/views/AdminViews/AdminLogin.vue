<template>
  <Page class="page" title="" ref="login">
    <ActionBar class="action-bar" flat>
      <NavigationButton android.systemIcon="ic_menu_back" @tap="goBack" />
    </ActionBar>

    <ScrollView>
      <FlexboxLayout class="form" flexDirection="column" justifyContent="center" alignItems="center">

        <Image src="~/assets/baskent.png" stretch="aspectFit" class="logo" />

        <label class="label">Yonetici Numarası</label>
        <TextField v-model="adminNumber" @blur="onAdminNumberBlur" class="input-field" />
        <label class="label">Sifre</label>
        <TextField v-model="sifre" secure="true" class="input-field" />
        <label class="label">Üniversite</label>
        <Button :text="selectedUniversity" @tap="showUniversities" class="input-field" />
        <label v-if="selectedUniversityId" class="label">Fakülte</label>
        <Button v-if="selectedUniversityId" :text="selectedFaculty" @tap="showFaculties" class="input-field" />
        <label v-if="selectedFacultyId" class="label">Bölüm</label>
        <Button v-if="selectedFacultyId" :text="selectedBolum" @tap="showBolums" class="input-field" />
        <label  class="labell" @tap="sifremiUnuttumAlert">Şifremi unuttum</label>

        <Button text="Giriş yap" @tap="onLogin" class="btn btn-primary" />


      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>

<script>
import { action } from '@nativescript/core';
import AdminAnaekran from '~/views/AdminViews/AdminAnaekran.vue' 
export default {
  data() {
    return {
      hostIp: '192.168.1.80',
      sifre: '',
      adminNumber: null,
      universityData: [
      ],
      selectedUniversity: "Üniversitenizi Seçin",
      selectedUniversityId: null,
      facultyData: [
      ],
      selectedFaculty: "Fakültenizi Seçin",
      selectedFacultyId: null,
      bolumData: [
      ],
      selectedBolum: "Bölümünüzu Seçin",
      selectedBolumId: null
    };
  },
  methods: {
    sifremiUnuttumAlert(){
      alert("Lütfen bölümünüzün yöneticisiyle iletişime geçin.");
    },
    onAdminNumberBlur() {
      if (this.adminNumber) {
        // TextField'dan odak kaybolduğunda veri alma işlemini başlat
        this.fetchData();
      }
    },
    async fetchData() {
      const userRole = 3;
      console.log(this.adminNumber);
      await fetch(`http://${this.hostIp}:8082/nursapp/auth/getbolmfacuni`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            "userId": this.adminNumber,
            "userRole": userRole
          })
        })
        .then(response => response.json()) // JSON formatına dönüştürme
        .then(data => {
          // data objesi içindeki verileri işleme
          this.universityData = data.unis;
          console.log(this.universityData.length + ' length');
          this.facultyData = data.facs;
          console.log(this.facultyData);
          this.bolumData = data.bolums;
          console.log(this.bolumData);
          if (data.unis.length == 1) {
            this.selectedUniversity = data.unis[0].uniName;
            this.selectedUniversityId = data.unis[0].uniId;
            console.log(data.unis[0]);
          } if (data.facs.length == 1) {
            this.selectedFaculty = data.facs[0].facName;
            this.selectedFacultyId = data.facs[0].facId;
          }
          if (data.bolums.length == 1) {
            this.selectedBolum = data.bolums[0].bolumName;
            this.selectedBolumId = data.bolums[0].bolumId;
          }

        })
        .catch(error => {
          console.error("Hata oluştu:", error);
        })
    },
    showUniversities() {
      const options = {
        title: "Üniversitenizi Seçin",
        message: "Lütfen üniversitenizi seçin:",
        cancelButtonText: "İptal",
        actions: this.universityData.map(u => u.uniName),
      };

      action(options).then((result) => {
        if (result !== "İptal") {

          this.selectedUniversity = result;
          const selectedUni = this.universityData.find(u => u.uniName == result);
          this.selectedUniversityId = selectedUni ? selectedUni.uniId : null;
          console.log(this.selectedUniversityId);
        }
      });
    },
    showFaculties() {
      const options = {
        title: "Fakültenizi Seçin",
        message: "Lütfen fakültenizi seçin:",
        cancelButtonText: "İptal",
        actions: this.facultyData.map(u => u.uniId == this.selectedUniversityId ? u.facName : null),
      };
      console.log(`${this.facultyData.map(u => u.uniId == this.selectedUniversityId ? u.facName : null)[0].facName}`);


      action(options).then((result) => {
        if (result !== "İptal") {
          this.selectedFaculty = result;
          const selectedFac = this.facultyData.find(u => u.facName == result);
          this.selectedFacultyId = selectedFac ? selectedFac.facId : null;
          console.log("fak id: " + this.selectedFacultyId);
        }
      });
    },
    showBolums() {
      const options = {
        title: "Bölümünüzü Seçin",
        message: "Lütfen bölümünüzü seçin:",
        cancelButtonText: "İptal",
        actions: this.bolumData.map(u => u.facId == this.selectedFacultyId ? u.bolumName : null),
      };

      action(options).then((result) => {
        if (result !== "İptal") {
          this.selectedBolum = result;
          const selectedBolm = this.bolumData.find(u => u.bolumName == result);
          this.selectedBolumId = selectedBolm ? selectedBolm.bolumId : null;
          console.log("bolm id: " + this.selectedBolumId);
        }
      });
    },
    goBack() {
      this.$navigateBack();
    },
    async onLogin() {
      if (this.adminNumber == null || this.selectedBolumId == null || this.sifre == null) {
        alert("Lütfen yönetici numaranızı ve diğer bilgilerinizi doğru girin veya doğru giriş ekranında olduğunuzdan emin olun!");
        return;
      } else {
        await fetch(`http://${this.hostIp}:8082/nursapp/auth/authenticate`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              "userId": this.adminNumber,
              "password": this.sifre,
              "bolumId": this.selectedBolumId
            })
          }).then(response => {
            if (response.status == 200) {
              return response.json();
            } else {
              return null;
            }
          })

          .then(data => {
            if (data != null) {
              console.log(data);
              //data.password = this.sifre;
              data.authenticationResponse.hostIp = this.hostIp;
              this.$store.commit('LOGIN', data.authenticationResponse);
              this.$navigateTo(AdminAnaekran,{
                clearHistory: true,
              });
            } else {
              alert("Hatalı giriş yaptınız. Lütfen bilgilerinizi kontrol edip tekrar deneyiniz!");
            }
          })
      }
      //this.$navigateTo(StudentAnaekran,);
    }
  },
  mounted() {

  }

}
</script>
  
<style scoped>
.ns-dark .labell {
  color: grey;
  font-size: 14;
  width: 80%;
  margin-top: 5;
  text-align: left; 
}
.ns-dark .label {
  color: #ffffff;
  font-size: 16;
  width: 80%;
  margin-bottom: 5em;
  text-align: left;
}

.ns-dark .logo {
  height: 70%;
  width: 70%;
  margin-bottom: 20em;
}

.ns-dark .action-bar {
  background-color: #19191B;
  color: #ffffff;
}

.ns-dark .action-bar-title {
  color: #ffffff;
}

.ns-dark .input-field {
  width: 80%;
  margin: 5%;
  padding: 8%;
  background-color: #ffffff;
  color: #333333;
  border-radius: 3em;
  border: none;
}

.ns-dark .btn-primary {
  margin-top: 20em;
  background-color: #4CAF50;
  color: #ffffff;
  border-radius: 5em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;
}

.ns-dark .logoo {
  height: 150em;
  width: 150em;
  margin-left: 90em;
}

.ns-dark .logo {
  height: 80%;
  width: 80%;
  margin-bottom: 20em;
}

.ns-dark.form {
  margin-left: 20em;
  margin-right: 20em;
  margin-bottom: 20em;
  padding-left: 20em;
  padding-bottom: 20em;
  padding-right: 20em;

}

.ns-dark .header {
  display: flex;
  text-align: center;
  color: #ffffff;
  font-size: 18;
  text-wrap: true;
}

.ns-dark .btn {
  margin-bottom: 5em;
  border-radius: 11em;
  background-color: #08F7FE80;
  width: 90%;
  color: #fff;
}











.ns-light .labell {
  color: rgb(26, 21, 21);
  font-size: 14;
  width: 80%;
  margin-top: 5;
  text-align: left; 
}
.ns-light .label {
  color: black;
  font-size: 16;
  width: 80%;
  margin-bottom: 5em;
  text-align: left;
}

.ns-light .logo {
  height: 70%;
  width: 70%;
  margin-bottom: 20em;
}

.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}

.ns-light .action-bar-title {
  color: #000000;
}

.ns-light .input-field {
  width: 80%;
  margin: 5%;
  padding: 8%;
  background-color: darkgrey;
  color: #333333;
  border-radius: 3em;
  border-color: #000000;
  border: none;
}

.ns-light .btn-primary {
  margin-top: 20em;
  background-color: #4CAF50;
  color: #ffffff;
  border-radius: 5em;
  padding: 10px;
  width: 80%;
  height: 40em;
  text-align: center;
}

.ns-light .logoo {
  height: 150em;
  width: 150em;
  margin-left: 90em;
}

.ns-light .logo {
  height: 80%;
  width: 80%;
  margin-bottom: 20em;
}

.ns-light .form {
  margin-left: 20em;
  margin-right: 20em;
  margin-bottom: 20em;
  padding-left: 20em;
  padding-bottom: 20em;
  padding-right: 20em;

}

.ns-light .header {
  display: flex;
  text-align: center;
  color: #ffffff;
  font-size: 18;
  text-wrap: true;
}

.ns-light .btn {
  margin-bottom: 5em;
  border-radius: 11em;
  background-color: #33c5c9;
  width: 90%;
  color: #000;
}
</style>