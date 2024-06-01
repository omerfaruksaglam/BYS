<template>
    <div class="modal-mask" v-show="isVisible">
        <div class="modal-container">
            <div class="change-password-modal">
                <h2>Şifre Değiştir</h2>
                <form @submit.prevent="confirmChange">
                    <label for="new-password">Yeni şifre :</label>
                    <input type="password" id="new-password" v-model="newPassword" required>
                    <label for="confirm-password">Yeni Şifre Tekrar:</label>
                    <input type="password" id="confirm-password" v-model="confirmPassword" required>
                    <div class="actions">
                        <button type="submit">Şifreyi Değiştir</button>
                        <button type="button" @click="closeModal">İptal</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props:{
        user: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            isVisible: false,
            newPassword: '',
            confirmPassword: ''
        };
    },
    methods: {
        closeModal() {
            this.isVisible = false;
            this.$emit('close');
        },
        confirmChange() {
            if (this.newPassword !== this.confirmPassword) {
                alert("Şifreler farklı, Düzeltip tekrar deneyiniz!");
                return;
            }
            if (window.confirm('Kullanıcının şifresi değiştirilecektir onaylıyor musunuz?')) {
                this.changePassword();
            }
        },
        changePassword() {
            fetch('http://localhost:8082/nursapp/app/admin/changepassword', {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + this.$store.state.user.token
                },
                body: JSON.stringify({
                    "userId": this.user.userId,
                    "bolumId": this.user.bolumId,
                    "password": this.newPassword,
                    "changerId": this.$store.state.user.userId,
                })
            }).then(response => {
                if (response.status === 200) {
                    alert("Şifre başarıyla değiştirildi.");
                    this.closeModal();
                } else {
                    alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                    console.log(this.user.userId, this.user.bolumId, this.newPassword);
                }
            });
        }
    },
    mounted() {
        this.isVisible = true;
    },
};
</script>

<style scoped>
.modal-mask {
    position: fixed;
    z-index: 9998;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: table;
    transition: opacity 0.3s ease;
}

.modal-container {
    display: table-cell;
    vertical-align: middle;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    /* Add a semi-transparent background */
}

.change-password-modal {
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
    transition: all 0.3s ease;
    width: 300px;
    max-width: 90%;
    /* Ensure the modal doesn't exceed the screen width */
}

.actions {
    text-align: right;
    padding-top: 10px;
    /* Add some space above the actions */
}

/* Add styles for buttons */
.actions button {
    padding: 10px 20px;
    margin-left: 10px;
    border: none;
    border-radius: 5px;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s ease;
}


.modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    z-index: 1000;
}

.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
}

.update-user {
    margin-top: 10%;
}

form {
    display: flex;
    flex-direction: column;
    width: 300px;
    margin: auto;
}


input,
select {
    margin-top: 5px;
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
}

button {
    margin-top: 15px;
    padding: 10px;
    background-color: #96B0BE;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

button:hover {
    background-color: #608295;
}
</style>
