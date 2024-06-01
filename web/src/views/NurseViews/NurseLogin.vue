<template>
    <div class="login-box">
        <img src="../../assets/baskent.png" alt="Logo" width="200" height="200">
        <div class="login-container">
            <h1>İkincil Puanlandırıcı Girişi</h1>
            <form @submit.prevent="onLogin">
                <div class="form-group">
                    <label for="nurseNumber">İkincil Puanlandırıcı Numarası</label>
                    <input type="text" id="nurseNumber" v-model="nurseNumber" required @blur="onNurseNumberBlur">
                </div>
                <div class="form-group">
                    <label for="sifre">Şifre</label>
                    <input type="password" id="sifre" v-model="sifre" required>
                </div>
                <div class="forgot" @click="forgotAlert"> 
                    Şifremi Unuttum
                </div>
                 <div class="form-group" v-if="nurseNumber">
    <label for="university">Üniversite</label>
    <select id="university" v-model="selectedUniversityId" @change="onUniversityChange">
      <option v-for="university in universityData" :value="university.uniId" :key="university.uniId">
        {{ university.uniName }}
      </option>
    </select>
  </div>
                <div class="form-group" v-if="selectedUniversityId">
                    <label for="faculty">Fakülte</label>
                    <select id="faculty" v-model="selectedFacultyId" @change="onFacultyChange">
                        <option v-for="faculty in facultyData" :value="faculty.facId" :key="faculty.facId">
                            {{ faculty.facName }}
                        </option>
                    </select>
                </div>
                <div class="form-group" v-if="selectedFacultyId">
                    <label for="bolum">Bölüm</label>
                    <select id="bolum" v-model="selectedBolumId">
                        <option v-for="bolum in bolumData" :value="bolum.bolumId" :key="bolum.bolumId">
                            {{ bolum.bolumName }}
                        </option>
                    </select>
                </div>
                <button type="submit" class="login-button">Giriş Yap</button>
            </form>
        </div>
    </div>
</template>
  
<script>
import Swal from 'sweetalert2';
import cryptoJs from 'crypto-js';

export default {
    data() {
        return {
            key: 'amutrloisdhjtruymzapoewqmjfhthew',
            sifre: '',
            nurseNumber: '',
            universityData: [],
            selectedUniversityId: null,
            facultyData: [],
            selectedFacultyId: null,
            bolumData: [],
            selectedBolumId: null,
        };
    },
    methods: {
        encryptData(data) {
  return cryptoJs.AES.encrypt(JSON.stringify(data), this.key).toString()
},

 decryptData(encryptedData) {
  const bytes = cryptoJs.AES.decrypt(encryptedData, this.key)
  return JSON.parse(bytes.toString(cryptoJs.enc.Utf8))
},
        forgotAlert() {
            Swal.fire({
                text: "Lütfen bölüm yöneticinizle iletişime geçiniz",
                icon: 'question',
                confirmButtonText: 'Tamam',
            });
    },
        onNurseNumberBlur() {
            if (this.nurseNumber) {
                this.fetchData();
            }
        },
        async fetchData() {
            try {
                const response = await fetch(`http://localhost:8082/nursapp/auth/getbolmfacuni`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        "userId": this.nurseNumber,
                        "userRole": 2
                    })
                });
                const data = await response.json();
                this.universityData = data.unis;
                this.facultyData = data.facs;
                this.bolumData = data.bolums;
                // Automatically select if there is only one option
                if (this.universityData.length === 1) {
                    this.selectedUniversityId = this.universityData[0].uniId;
                    this.onUniversityChange();
                }
            } catch (error) {
                console.error("There was an error fetching the data:", error);
            }
        },
        onUniversityChange() {
            // Filter faculties based on selected university
            this.selectedFacultyId = null; // Reset faculty selection
            this.selectedBolumId = null; // Reset department selection
            this.facultyData = this.facultyData.filter(faculty => faculty.uniId === this.selectedUniversityId);
            if (this.facultyData.length === 1) {
                this.selectedFacultyId = this.facultyData[0].facId;
                this.onFacultyChange();
            }
        },
        onFacultyChange() {
            // Filter departments based on selected faculty
            this.selectedBolumId = null; // Reset department selection
            this.bolumData = this.bolumData.filter(bolum => bolum.facId === this.selectedFacultyId);
            if (this.bolumData.length === 1) {
                this.selectedBolumId = this.bolumData[0].bolumId;
            }
        },
        async onLogin() {
          if(this.nurseNumber == null ||  this.selectedBolumId == null|| this.sifre == null){
            alert("Lütfen öğrenci numaranızı ve diğer bilgilerinizi doğru girin veya doğru giriş ekranında olduğunuzdan emin olun!");
            return;
          }else{
            await fetch(`http://localhost:8082/nursapp/auth/authenticate`,
      {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
          "userId": this.nurseNumber,
          "password": this.sifre,
          "bolumId": this.selectedBolumId
        })
      }).then(response => {
        if(response.status == 200){
          return response.json();
        }else{
          return null;        }
      })
      .then(data => {
        if(data != null){
          console.log(data);
          //data.password = this.sifre;
          this.$store.commit('LOGIN', data.authenticationResponse);
          this.$router.push({name: 'teacherhome', query: { bolumInfo: this.encryptData(data.bolumInfo) }});
        }else{
          alert("Hatalı giriş yaptınız. Lütfen bilgilerinizi kontrol edip tekrar deneyiniz!");
        }
      })
    }
            //this.$navigateTo(StudentAnaekran,);
        }
    },
}
</script>

<style scoped>
.img {
    display: flex;
    margin-left: auto;
    margin-right: auto;
    margin-bottom: 7rem;
    width: 50%;
}

.login-box {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    margin-top: 10%;
}

.login-container {
    min-width: 30%;
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.form-group {
    margin: 20px ; /* Yukarıdan ve aşağıdan 20px, yanlardan 0px boşluk bırak */
}

label {
    display: block;
    font-weight: bold;
}

input[type="text"],
input[type="password"] {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

button {
    width: 100%;
    padding: 10px 20px;
    background-color: #96B0BE; /* yeşil */
    border: none;
    border-radius: 5%;
    color: white;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    margin-top: 10px; /* Giriş yap butonunu yukarıda 10px boşluk bırak */
}
.forgot{
    text-decoration: underline;
    color: blue;
    cursor: pointer;
}
</style>
