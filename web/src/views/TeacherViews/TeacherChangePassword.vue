<template>
  <div class="change-password-container">
    <h1>Şifre Değiştir</h1>
    <form @submit="changePassword">
      <div class="form-group">
        <label for="new-password">Yeni Şifre</label>
        <input type="password" id="new-password" v-model="newPassword" required>
      </div>
      <div class="form-group">
        <label for="confirm-password">Şifreyi Onayla</label>
        <input type="password" id="confirm-password" v-model="confirmPassword" required>
      </div>
      <button type="submit" class="submit-btn">Şifreyi Değiştir</button>
    </form>
  </div>
</template>
  
<script>

export default {
  data() {
    return {
      newPassword: '',
      confirmPassword: ''
    };
  },
  methods: {
    async changePassword(event) {
      event.preventDefault();
      if (this.newPassword != this.confirmPassword) {
                alert("Şifreler farklı, Düzeltip tekrar deneyiniz!");
            }
            else {
                if(this.$store.state.user.userRole === 0){
                  await fetch('http://localhost:8082/nursapp/app/teacher/changepassword',
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                        body: JSON.stringify({
                            "userId": this.$store.state.user.userId,
                            "password": this.$store.state.user.password,
                            "newPassword": this.newPassword,
                            "bolumId": this.$store.state.user.bolumId,
                        })

                    }).then(response => {
                        if (response.status == 200) {
                            return response.json();
                        } else {

                            alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                        }
                    })
                    
                    .then(data => {
                        if (data != null) {
                            data.token = this.$store.state.user.token;
                            console.log(data);
                            this.$store.commit('LOGIN', data);
                            console.log("Şifre değiştirildi");
                            alert("Şifre başarıyla değiştirildi !");
                            this.$router.go(-1);
                        }
                        else {
                            alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                        }
                    });
                }else if (this.$store.state.user.userRole === 2){
                  await fetch('http://localhost:8082/nursapp/app/secondgrader/changepassword',
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                        body: JSON.stringify({
                            "userId": this.$store.state.user.userId,
                            "password": this.$store.state.user.password,
                            "newPassword": this.newPassword,
                            "bolumId": this.$store.state.user.bolumId,
                        })

                    }).then(response => {
                        if (response.status == 200) {
                            return response.json();
                        } else {

                            alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                        }
                    })
                    
                    .then(data => {
                        if (data != null) {
                            data.token = this.$store.state.user.token;
                            console.log(data);
                            this.$store.commit('LOGIN', data);
                            console.log("Şifre değiştirildi");
                            alert("Şifre başarıyla değiştirildi !");
                            this.$router.go(-1);
                        }
                        else {
                            alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                        }
                    });
                }

            }
    }
  }
};
</script> 
  
<style scoped>
.change-password-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  /* Değişiklik burada: Arkaplan rengini beyaz yap */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
}

input[type="password"] {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button[type="submit"] {
  width: 100%;
  padding: 10px 20px;
  background-color: #119094;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #96B0BE;
}
</style>
  