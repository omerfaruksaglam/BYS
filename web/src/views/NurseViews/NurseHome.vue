<template>
    <div class="nurse-home">
      <button class="profile-button">
        <img srcSet="../src/assets/icon.png" class="img-3"/>
    </button>
        <div class="div-7">Hoş Geldiniz; {{ $store.state.user.name }} {{ $store.state.user.surname }}</div>
        <button class="custom-button" @click="profile">Kişisel Bilgiler</button>
        <button class="custom-button">Not Girişi</button>
        <button class="custom-button">Öğrencinin Durumunu Gör</button>

    </div>
  </template>
  
  <script>
export default {
    // Diğer kodlar
    methods: {
        logout() {
            this.$router.push('/');
            this.$store.commit('logout');
        },
        profile() {
            this.$router.push('/nurse-profile');
        }
    }
    // Diğer kodlar
}
</script>
  
<style scoped>
.nurse-home {
    background-color: var(--Color, #fff);
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 2rem; /* Üst boşluğu azaltır */
    box-sizing: border-box;
    overflow: auto;
}

.img-2 { /* Çıkış yap ikonu */
    width: 20px; /* İkon boyutunu küçültür */
    height: auto;
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