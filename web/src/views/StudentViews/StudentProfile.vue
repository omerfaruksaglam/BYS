<template>
    <div class="student-home">
        <div class="profile-icon">
            <img srcSet="../src/assets/icon.png" class="img-3" />
        </div>
        <div>
            <p class="div-7">İsim: {{ $store.state.user.name }} </p>
            <p class="div-7">Soyisim: {{ $store.state.user.surname }}</p>
            <p class="div-7">Numara: {{ $store.state.user.userId }}</p>
            <p class="div-7">Giriş Tarihi: {{ formatGirisYili($store.state.user.kayit_tarihi) }}</p>
        </div>
        <button class="custom-button" @click="changePassword">Şifre Değiştir</button>

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
        changePassword() {
            this.$router.push('/student-change-password');
        },
        formatGirisYili(dateString) {
            const date = new Date(dateString);
            const formattedDate = date.toISOString().split('T')[0];
            return formattedDate;
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
    padding-top: 2rem;
    box-sizing: border-box;
    overflow: auto;
}


.profile-icon {
    /* Profil resmi butonu */
    width: 100px;
    /* Profil resmi boyutunu küçültür */
    height: 100px;
    /* Yükseklik eklendi */
    border-radius: 50%;
    /* Yuvarlak profil resmi */
    margin: 2rem 0;
    /* Üst ve alt boşluğu ayarlar */
    overflow: hidden;
    /* Taşan kısımları keser */
}

.img-3 {
    /* Profil resmi */
    width: 100%;
    /* Profil resmi butonuna sığdırır */
    height: auto;
}

.div-7 {
    /* Hoş geldiniz metni */
    font-size: 1.2rem;
    margin-bottom: 2rem;
}

.custom-button {
    /* Genelleştirilmiş butonlar */
    background-color: #119094;
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
    background-color: #0e6e7c;
}

@media (max-width: 991px) {
    .custom-button {
        width: auto;
        /* Mobil görünümde buton genişliği ayarlanır */
    }
}</style>