<template>
    <div class="header-container">
        <div class="logo-container">
            <img src="../assets/baskent.png" class="logo-image" />
            <div class="logo-text">BYS</div>
        </div>
        <button class="logout-button" @click="logout">
            <img src="../assets/logout.png" class="logout-icon"/>
            Çıkış Yap
        </button>
    </div>
</template>

<script>
export default { 
    methods: {
        logout() {
            this.$store.commit('LOGOUT');
            this.$router.push('/');
        }
    }
}
</script>

<style>
.header-container {
    display: flex;
    justify-content: space-between;
    padding: 0 1rem;
    box-sizing: border-box;
}

.logo-container {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.logo-image {
    width: 80px;
    height: auto;
}

.logout-button {
    position: absolute;
    top: 1rem;
    right: 1rem;
    background-color: white;
    display: flex;
    align-items: center;
    border: none;
    padding: 0.5rem 1rem;
    font-size: 0.9rem;
    cursor: pointer;
}

.logout-icon {
    width: 20px;
    height: 20px;
}
</style>