<template>
    <div class="page">
        <div class="div-7">
            <img loading="lazy" srcSet="../src/assets/sinifSec.png" class="img-3" />
            <h2 >{{student.name}} {{ student.surname }} {{textt}}</h2>
        </div>
        <div class="button-container">
            <Button v-for="(item, index) in bolumInfo.yilSayisi" :key="index" @click="onYearSelected(index+1)" class="custom-button">{{ index + 1 }}</Button>
        </div>
    </div>
</template>
  
<script>
import cryptoJs from 'crypto-js';

export default {
    mounted() {
        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.notVer = this.decryptData(this.$route.query.notVer);
        this.student = this.decryptData(this.$route.query.student);
        this.textt = this.notVer
          ? ' adlı öğrenciye not vermek için lütfen sınıf seçiniz'
          : ' adlı öğrenciye ait notları görmek için lütfen sınıf seçiniz';
    },
    data() {
        return {
            key: 'amutrloisdhjtruymzapoewqmjfhthew',
            bolumInfo: {},
            notVer: {},
            student: {},
            textt: '',
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
        onYearSelected(index) {
            

            this.$router.push({ name: 'becerisec', query: { bolumInfo: this.encryptData(this.bolumInfo), notVer: this.encryptData(this.notVer), student: this.encryptData(this.student), year: this.encryptData(index) } });

        }

        },


};
</script>
  
<style scoped>
.page {
    display: flex;
    flex-direction: column;
    width: 100%;
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

.div-7 {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 0.5rem;
  margin-top: 3em;
}
.search-box {
    padding: 0.625rem;
    /* 10px */
    border-radius: 0.3125rem;
    /* 5px */
    border: 1px solid #ccc;
    font-size: 1rem;
    /* 16px */
    width: 80%;
    margin-right: 1rem;
}

.list-item {
    margin: 0.3125rem;
    /* 5px */
    padding: 0.625rem;
    /* 10px */
    background-color: grey;
    border-radius: 0.3125rem;
    /* 5px */
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
    display: flex;
    justify-content: space-between;
}

.list-item:hover {
    background-color: #505050;
}
.teacher-home {
    background-color: var(--Color, #fff);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    overflow: auto;
}
.img-3 {
    /* Profil resmi */
    width: 4%;
    /* Profil resmi butonuna sığdırır */
    height: auto;
}
.delete-button {
    background-color: #f44336;
    /* Red */
    border: none;
    color: white;
    padding: 5px 15px;
    text-align: center;
    font-size: 14px;
    cursor: pointer;
}

.add-user-button {
    padding: 10px 20px;
    background-color: #96B0BE;
    /* yeşil */
    border: none;
    border-radius: 5%;
    color: white;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.marginr {
    margin-right: 1rem;
}

.add-user-button:hover {
    background-color: #608295;
}



.custom-button:hover {
    background-color: #ddd;
}

@media (max-width: 991px) {

    .custom-button {
        width: auto;
        /* Mobil görünümde buton genişliği ayarlanır */
    }
}
</style>
  