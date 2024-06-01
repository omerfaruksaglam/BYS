<template>
  <div class="update-user">
    <div v-if="selectedUser">
      <div class="overlay" @click="closeModal"></div>
      <div class="modal">
        <!-- Formun submit olayını updateUser metoduna bağlayın -->
        <form @submit.prevent="updateUser">
          <label for="name">Ad:</label>
          <input v-model="selectedUser.name" placeholder="Name" required />

          <label for="surname">Soyad:</label>
          <input
            v-model="selectedUser.surname"
            placeholder="Surname"
            required
          />

          
<div class="toggle-container">
  <label class="switch">
    <input
      type="checkbox"
      v-model="selectedUser.isActive"
      @change="toggleActive"
    />
    <div class="slider round"></div>
  </label>
  <span class="active-status"> Kullanıcı : {{ selectedUser.isActive ? 'Aktif' : 'Aktif değil' }}</span>
</div>
          <!--
          <label for="role">Rol:</label>
          <select v-model="selectedUser.userRole" required>
            <option value="student">Öğrenci</option>
            <option value="teacher">Öğretim Görevlisi</option>
            <option value="admin">Yönetici</option>
            <option value="nurse">Hemşire</option>
          </select>
 -->
          <button type="submit">Güncelle</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    user: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      selectedUser: null, // selectedUser başlangıçta null olarak ayarlanır.
    };
  },
  mounted() {
    this.selectedUser = { ...this.user, isActive: this.user.active }; // `isActive` doğru başlangıç değeriyle başlatılır.
  },
  watch: {
    user(newVal) {
      this.selectedUser = { ...newVal, isActive: newVal.active }; // parent'tan `user` güncellendiğinde `selectedUser` da güncellenir.
    },
  },
  methods: {
    async toggleActive() {
      //this.selectedUser.isActive = !this.selectedUser.isActive; // Switch toggle durumu değiştirildiğinde `isActive` güncellenir.
      const isActiveValue = this.selectedUser.isActive;

      const url = `http://localhost:8082/nursapp/app/admin/isactive/${this.selectedUser.bolumId}-${this.selectedUser.userId}-${this.$store.state.user.userId}?isActive=${isActiveValue}`;
      console.log(
        this.selectedUser.isActive,
        this.selectedUser.bolumId,
        this.selectedUser.userId,
        url
      );
      await axios
        .put(
          url,
          {
            //userId: this.selectedUser.userId,
            isActive: isActiveValue,
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + this.$store.state.user.token,
            },
          }
        )
        .then((response) => {
          // Handle response
        })
        .catch((error) => {
          // Handle error
          console.error(error);
          this.selectedUser.isActive = !this.selectedUser.isActive; // Revert the change on error
        });
    },

    async updateUser() {
      // const roleMapping = {'student': 1,'teacher': 0,'nurse': 2,'admin': 3,};

      const payload = {
        name: this.selectedUser.name,
        surname: this.selectedUser.surname,
        //userRole: roleMapping[this.selectedUser.userRole],
      };

      await axios
        .put(
          `http://localhost:8082/nursapp/app/admin/updateuser/${this.selectedUser.bolumId}-${this.selectedUser.userId}-${this.$store.state.user.userId}`,
          payload,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + this.$store.state.user.token,
            },
          }
        )
        .then((response) => {
          alert("Kullanıcı başarıyla güncellendi.");
          this.$emit("close"); // Güncelleme sonrası modalı kapat
          location.reload(); // Sayfayı yenile
        })
        .catch((error) => {
          console.error("Güncelleme işlemi sırasında bir hata oluştu:", error);
          alert("Kullanıcı güncellenirken bir hata oluştu.");
        });
        location.reload(); // Sayfayı yenile
    },
    closeModal() {
      this.$emit("close");
    },
  },
};
</script>



<style scoped>
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

label {
  font-weight: bold;
  margin-top: 15px;
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
  background-color: #96b0be;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #608295;
}

.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input {
  display: none;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: 0.4s;
  transition: 0.4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: 0.4s;
  transition: 0.4s;
}

input:checked + .slider {
  background-color: #101010;
}

input:focus + .slider {
  box-shadow: 0 0 1px #101010;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

.toggle-container {
  display: flex;
  align-items: center; /* Slider ve metni dikey olarak hizalar */
  gap: 10px; /* Slider ve metin arasında boşluk bırakır */
}
.active-status{
  margin-top: 10px;
}

</style>
