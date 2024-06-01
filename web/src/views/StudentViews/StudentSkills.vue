<template>
  <div class="student-skills">
    <!--<div class="div-2">
      <div class="div-3">
        <img loading="lazy" srcSet="../src/assets/baskent.png" class="img" />
        <div class="div-4">BYS</div>
      </div>
      <button class="logout-button" @click="logout">
        <img loading="lazy" srcSet="../src/assets/logout.png" class="img-2" />
        <div class="div-5">Çıkış Yap</div>
      </button>
    </div>-->

    <div class="button-container">
      <button v-for="skill in Skills" :key="skill.skillId" @click="goToSubskills(skill)" class="custom-button">
        {{ skill.namee }}
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import cryptoJs from 'crypto-js';

export default {
  mounted() {
    this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
    this.year=this.decryptData(this.$route.query.year);
    this.fetchSkills();
  },
  data() {
    return {
      key: 'mitughjazbvrtpoyqjewlirtunmxshfr',
      bolumInfo: null,
      year: null,
      secondGrader: {},
      secondGraderNotVerebilirmi: {},
      Skills: [],
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
    goToSubskills(skill){
      this.$router.push({name:'studentsubskills',query:{notVer: this.encryptData(this.bolumInfo.ogrenciNotVerebilirmi ? true : false), student: this.encryptData(this.$store.state.user), bolumInfo:this.encryptData(this.bolumInfo),year:this.encryptData(this.year),skill:this.encryptData(skill),secondGrader:this.encryptData(this.secondGrader),secondGraderNotVerebilirmi:this.encryptData(this.secondGraderNotVerebilirmi)}});
    },
    async fetchSkills() {
      try {
        console.log(this.bolumInfo);
        console.log(this.year);
        const response = await axios.get(`http://localhost:8082/nursapp/app/student/getskills/${this.$store.state.user.bolumId}/${this.year}/${this.bolumInfo.secondGraderId}`, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + this.$store.state.user.token
          }
        });
        console.log(response.data);
        this.Skills = response.data.skills; 
        this.secondGrader = response.data.secondGrader;
        this.secondGraderNotVerebilirmi = response.data.secondGraderNotVerebilirmi;
      } catch (error) {
        console.error('Hata:', error);
      }
    },

   
    logout() {
      this.$router.push('/');
      this.$store.commit('logout');
    }
  },
};
</script>

<style scoped>
.student-skills {
  background-color: var(--Color, #fff);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 2rem;
  box-sizing: border-box;
  overflow: auto;
}

.logout-button {
  position: absolute;
  top: 1.5rem;
  right: 1.5rem;
  background-color: white;
  display: flex;
  align-items: center;
  border: none;
  padding: 1.2rem 1.8rem;
  font-size: 1.4rem;
  cursor: pointer;
}

.div-2 {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 0 1.5rem;
  box-sizing: border-box;
}

.div-3 {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.img {
  width: 50px;
  height: auto;
}

.div-4 {
  font-size: 1.5rem;
}

.div-5 {
  font-size: 1rem;
  margin-left: 1rem;
}

.img-2 {
  width: 25px;
  height: auto;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 2rem;
}

.custom-button {
  background-color: #d81f1f;
  border: none;
  color: white;
  padding: 0.8rem 1.6rem;
  font-size: 1rem;
  margin: 1.5rem;
  border-radius: 4px;
  transition: background-color 0.3s;
  max-width: 250px;
  width: 100%;
}

.custom-button:hover {
  background-color: #bd1d1d;
}

@media (max-width: 991px) {
  .div-2 {
    flex-direction: column;
    align-items: center;
  }

  .logout-button,
  .custom-button {
    width: 100%;
  }

  .custom-button {
    margin: 1.5rem;
  }
}
</style>
