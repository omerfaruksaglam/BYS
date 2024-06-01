<template>
    <div class="user-form-container">
      <h1>Yeni Kullanıcı</h1>
      <form @submit.prevent="addNewUser">
        <div class="form-group">
          <label for="firstName">Ad:</label>
          <input type="text" id="firstName" v-model="newUser.firstName" required>
        </div>
        
        <div class="form-group">
          <label for="lastName">Soyad:</label>
          <input type="text" id="lastName" v-model="newUser.lastName" required>
        </div>
  
        <div class="form-group">
          <label for="userId">Numara:</label>
          <input type="number" id="username" v-model="newUser.userId" required>
        </div>
  
        <div class="form-group">
            <label for="role">Rol:</label>
            <select id="role" v-model="newUser.role" required>
                <option value="student">Öğrenci</option>
                <option value="teacher">Öğretim Görevlisi</option>
                <option value="admin">Yönetici</option>
                <option value="nurse">Hemşire</option>
            </select>
        </div>
  
        <div class="form-group">
          <label for="password">Şifre:</label>
          <input type="password" id="password" v-model="newUser.password" required>
        </div>
  
        <div class="form-group">
          <label for="registrationDate">Kayıt tarihi:</label>
          <input type="date" id="registrationDate" v-model="newUser.registrationDate" required>
        </div>
  
        <button type="submit" class="submit-btn">Yeni Kullanıcı Ekle</button>
      </form>
    </div>
  </template>
  
  <script>
import axios from 'axios';

export default {
  data() {
    return {
      newUser: {
        firstName: '',
        lastName: '',
        userId: '', 
        role: '',
        password: '',
        registrationDate: '', // Kayıt tarihi için yeni alan
      }
    };
  },
  methods: {
    addNewUser() {
      // Giriş bilgilerini kontrol 
      if (!this.newUser.firstName || !this.newUser.lastName || !this.newUser.userId ||
          !this.newUser.role || !this.newUser.password || !this.newUser.registrationDate) {
        console.log("Tüm alanları doldurunuz.");
        return;
      }
    
      const roleMapping = {
        'student': 1,
        'teacher': 0,
        'nurse': 2,
        'admin': 3,
      };

      const payload = {
        userId: this.newUser.userId, 
        name: this.newUser.firstName,
        surname: this.newUser.lastName,
        password: this.newUser.password,
        userRole: roleMapping[this.newUser.role],
        kayit_tarihi: this.newUser.registrationDate,
        bolumId : this.$store.state.user.bolumId,
        isActive: true,
        islem_yapan_admin: this.$store.state.user.userId
      };

      axios.post('http://localhost:8082/nursapp/app/admin/register', payload, {
        headers: {
          "Content-Type": "application/json",
           "Authorization": "Bearer " + this.$store.state.user.token
        }
      })
      .then(response => {
        console.log("Kullanıcı başarıyla kaydedildi:", response.data);
        console.log("Kayıt tarihi:", this.newUser.registrationDate);
        this.$router.push('/usermanagement');
      })
      .catch(error => {
        console.error("Kullanıcı kaydedilirken hata oluştu:", error);
      });
    }
  }
};
</script>
  
  <style scoped>
  .user-form-container {
    max-width: 400px;
    margin: 0 auto;
    padding: 1rem;
    background: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }
  
  h1 {
    text-align: center;
    margin-bottom: 2rem;
  }
  
  .form-group {
    margin-bottom: 1rem;
  }
  
  label {
    display: block;
    margin-bottom: .5rem;
  }
  
  input[type="text"],
  input[type="password"] {
    width: 90%;
    padding: .5rem;
    border: 1px solid #ccc;
    border-radius: .25rem;
  }
  
  .submit-btn {
    width: 100%;
    margin-top: 1rem;
    padding: 10px 20px;
    background-color: #96B0BE; /* yeşil */
    border: none;
    border-radius: 5%;
    color: white;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
  }
  
  .submit-btn:hover {
    background-color: #608295;
  }
  </style>
  