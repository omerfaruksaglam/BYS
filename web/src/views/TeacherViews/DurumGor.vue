<template>
    <div class="divv">
        <div class="teacher-home">
        
        <div class="div-7"> Seçilen Öğrenci: {{ student.name }} {{ student.surname }}</div>
        <button class="custom-button" @click="goToStatistics">Grafiksel Gösterimler Gör</button>
        <button class="custom-button" @click="goToSinifSec">Notları Gör</button>
        <button class="custom-button" @click="karne">Beceri Karnesi</button>
        

    </div>
    </div>
</template>
  
<script>
import cryptoJs from 'crypto-js';

export default {
    created() {
        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.student = this.decryptData(this.$route.query.student);
        this.notVer = this.decryptData(this.$route.query.notVer);
    },
    data() {
        return {
            key: 'amutrloisdhjtruymzapoewqmjfhthew',
            bolumInfo: null,
            notVer: null,
            student: null
        }
    },
    // Diğer kodlar 
    methods: {
        encryptData(data) {
  return cryptoJs.AES.encrypt(JSON.stringify(data), this.key).toString()
},

 decryptData(encryptedData) {
  const bytes = cryptoJs.AES.decrypt(encryptedData, this.key)
  return JSON.parse(bytes.toString(cryptoJs.enc.Utf8))
},
        goToStatistics(){
        this.$router.push({name: 'teacherstatistics' , query: {bolumInfo: this.encryptData(this.bolumInfo), student: this.encryptData(this.student)}}); 
        },
        goToSinifSec(){
            this.$router.push({name: 'sinifsec' , query: {bolumInfo: this.encryptData(this.bolumInfo), student: this.encryptData(this.student), notVer: this.encryptData(this.notVer)}}); 

        },
        karne(bolumId, studentId) {
            bolumId = this.$store.state.user.bolumId;
            studentId = this.student.userId;
            const url = this.$router.resolve( { name:"karne" , params: { studentId, bolumId }} ).href;
            window.open(url, '_blank');
        }
    }
    // Diğer kodlar
}
</script>
  
<style scoped>
.teacher-home {
    background-color: var(--Color, #fff);
    display: flex;
    
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding-top: 2rem;
}


.divv{
        padding: 10%;
}
.div-7 {
    /* Hoş geldiniz metni */
    font-size: 1.2rem;
    margin-bottom: 2rem;
}

.custom-button {
    /* Genelleştirilmiş butonlar */
    background-color: #f0f0f0;
    border: none;
    color: black;
    padding: 0.8rem 1.6rem;
    font-size: 1rem;
    margin: 0.5rem;
    border-radius: 4px;
    transition: background-color 0.3s;
    max-width: 200px;
    /* Buton genişliği sınırlandırılır */
    width: 100%;
    /* Genişlik ekran genişliğine göre ayarlanır */
}

.custom-button:hover {
    background-color: #ddd;
}

@media (max-width: 991px) {

    .custom-button {
        width: auto;
        /* Mobil görünümde buton genişliği ayarlanır */
    }
}</style>