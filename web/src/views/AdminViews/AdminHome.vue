<template>
    <div class="admin-home">

      <button class="profile-button">
        <img srcSet="../src/assets/icon.png" class="img-3"/>
    </button>
        <div class="div-7">Hoş Geldiniz; {{ $store.state.user.name }} {{ $store.state.user.surname }}</div>
        <button class="custom-button" @click="skillmanagement">Becerileri Yönet</button>
        <button class="custom-button" @click="usermanagement">Kullanıcıları Yönet</button>
        <button class="custom-button" @click="toggleLogs">Loglar</button>
    <button class="custom-button" v-if="showLogs" @click="loginactivities">Giriş Aktiviteleri</button>
    <button class="custom-button" v-if="showLogs" @click="activities">Diğer Sistem Aktiviteleri</button>
        <button class="custom-button" @click="goAvgYears">Yıllara Göre Becerilerin Puan Ortalamaları</button>
    </div>
  </template>
  
  <script>
  import Header from '@/components/Header.vue';
export default {
    // Diğer kodlar
    components: {
        Header
    },
    data() {
        return {
            showLogs: false,
        }
    },
    methods: {
        toggleLogs() {
    this.showLogs = !this.showLogs;
  },
        usermanagement() {
            this.$router.push('/usermanagement');
        },
        skillmanagement(){
            this.$router.push('/skills');
        },
        loginactivities(){
            this.$router.push('/loginactivities');
        },
        activities(){
            this.$router.push('/activities');
        },
        goAvgYears(){
            this.$router.push('/yıllar');
        }
    }
    // Diğer kodlar
}
</script>
  
<style scoped>
.admin-home {
    background-color: var(--Color, #fff);
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 10%; /* Üst boşluğu azaltır */
    height: 100%;
    box-sizing: border-box;
}

.profile-button { /* Profil resmi butonu */
    width: 80px; /* Profil resmi boyutunu küçültür */
    height: 80px; /* Yükseklik eklendi */
    border-radius: 50%; /* Yuvarlak profil resmi */
    margin: 1rem 0; /* Üst ve alt boşluğu azaltır */
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
    .div-2 {
        flex-direction: column;
        align-items: center;
    }

    .logout-button, .profile-button {
        margin: 0.5rem 0;
    }

    .custom-button {
        width: auto; /* Mobil görünümde buton genişliği ayarlanır */
    }
}
</style>