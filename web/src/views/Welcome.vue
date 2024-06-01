<template>
    <div class="welcome">
        <img src="../assets/baskent.png" alt="Logo" width="200" height="200">
        <h1>BECERİ YÖNETİM SİSTEMİ</h1>

        <div class="login-buttons">
            <button @click="selectLoginType('student')">Öğrenci</button>
            <button @click="selectLoginType('teacher')">Öğretim Elemanı</button>
            <button @click="selectLoginType('nurse')">İkincil Puanlandırıcı</button>
            <button @click="selectLoginType('admin')">Yönetici</button>
        </div>
        <div @click="Hakkında" class="Hakkında" style="position: absolute; top: 10px; right: 10px; cursor: pointer; color: blue; text-decoration: underline;">Hakkında</div>
    </div>
</template>

<script>
export default {
    name: 'Welcome',
    methods: {
        selectLoginType(role) {
            this.$router.push(`/${role}-login`);
        },
        Hakkında(){
            this.$router.push('/hakkinda');
        }
    }
}
</script>

<style scoped>
.welcome {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    margin-top: 10%; /* Üst boşluğu azaltır */
}

.login-buttons {
    display: flex;
    flex-direction: column;
    gap: 10px; /* Butonlar arasında boşluk oluşturur */
}

.login-buttons button {
    background-color: #f0f0f0; /* Gri arka plan */
    border: none; /* Kenarlıkları kaldırır */
    color: black; /* Yazı rengini siyah yapar */
    padding: 15px 32px; /* İç boşluk ekler */
    text-align: center; /* Yazıyı ortalar */
    text-decoration: none; /* Altı çizili metni kaldırır */
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer; /* İmleci bir el şekline dönüştürür */
    border-radius: 4px; /* Yuvarlatılmış köşeler */
    transition: background-color 0.3s; /* Arka plan rengi geçişi */
}

.login-buttons button:hover {
    background-color: #938e8e; /* Fare üzerindeyken arka plan rengini değiştirir */
}
</style>