<template>
    <div class="student-home">
      <button class="profile-button" >
        <img srcSet="../src/assets/icon.png" class="img-3"/>
    </button>
        <div class="div-7">Hoş Geldiniz; {{ $store.state.user.name }} {{ $store.state.user.surname }}</div>
        <button class="custom-button" @click="profile">Kişisel Bilgiler</button>
        <button class="custom-button" @click="sinifSec">Not Girişi</button>
        <button class="custom-button" @click="grafikselGosterim">Grafiksel Gösterimler</button>
        <button class="custom-button" @click="karne">Beceri Karnesi</button>

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
        logout() {
            this.$router.push('/');
            this.$store.commit('logout');
        },
        profile() {
            this.$router.push('/student-profile');
        },
        grafikselGosterim(){
            this.$router.push({name: 'studentstatistics' , query: {bolumInfo: this.encryptData(this.bolumInfo), student: this.encryptData(this.$store.state.user)}}); 
        },
        sinifSec() {
            this.$router.push({name:'studentselectgrade',query:{bolumInfo:this.encryptData(this.bolumInfo)}});
        },
        karne(bolumId, studentId) {
            bolumId = this.$store.state.user.bolumId;
            studentId = this.$store.state.user.userId;
            const url = this.$router.resolve( { name:"karne" , params: { studentId, bolumId }} ).href;
            window.open(url, '_blank');
        }
    }
    // Diğer kodlar
}

</script>
  
<style scoped>
.student-home {
    background-color: var(--Color, #fff);
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 2rem; /* Üst boşluğu azaltır */
    box-sizing: border-box;
    overflow: auto;
}


.profile-button { /* Profil resmi butonu */
    width: 100px; /* Profil resmi boyutunu küçültür */
    height: 100px; /* Yükseklik eklendi */
    border-radius: 50%; /* Yuvarlak profil resmi */
    margin: 2rem 0; /* Üst ve alt boşluğu ayarlar */
    overflow: hidden; /* Taşan kısımları keser */
}

.img-3 { /* Profil resmi */
    width: 100%; /* Profil resmi butonuna sığdırır */
    height: auto;
}

.div-7 { /* Hoş geldiniz metni */
    font-size: 1.2rem;
    margin-bottom: 2rem;
}

.custom-button { /* Genelleştirilmiş butonlar */
    background-color: #f0f0f0;
    border: none;
    color: black;
    padding: 0.8rem 1.6rem;
    font-size: 1rem;
    margin: 0.5rem;
    border-radius: 4px;
    transition: background-color 0.3s;
    max-width: 200px; /* Buton genişliği sınırlandırılır */
    width: 100%; /* Genişlik ekran genişliğine göre ayarlanır */
}

.custom-button:hover {
    background-color: #ddd;
}

@media (max-width: 991px) {

    .custom-button {
        width: auto; /* Mobil görünümde buton genişliği ayarlanır */
    }
}
</style>