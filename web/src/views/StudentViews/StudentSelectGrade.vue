<template>
  <div class="student-select-grade">
    
    
    <div class="div-7">
      <img loading="lazy" srcSet="../src/assets/sinifSec.png" class="img-3" />
      Sınıf Seçiniz
    </div>

    <div class="button-container" v-if="bolumInfo && bolumInfo.yilSayisi">
      <button v-for="year in bolumInfo.yilSayisi" :key="year" class="custom-button" @click="gradeSelected(year)">
        {{ year }}. Sınıf
      </button>
    </div>
  </div>
</template>

<script>
  import cryptoJs from 'crypto-js';

export default {
  mounted() {
        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
    },
  data() {
        return {
          key: 'mitughjazbvrtpoyqjewlirtunmxshfr',
            bolumInfo: null
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
    gradeSelected(year) {
      this.$router.push({name:'studentskills',query:{bolumInfo:this.encryptData(this.bolumInfo),year:this.encryptData(year)}});
    },
    logout() {
      this.$router.push('/');
      this.$store.commit('logout');
    },
  }
};
</script>

<style scoped>
.student-select-grade {
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

.img-3 {
  width: 35px;
  height: auto;
  margin-right: 2rem;
}

.div-7 {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  margin-top: 3em;
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
