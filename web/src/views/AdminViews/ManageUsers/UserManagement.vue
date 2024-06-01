<template>
  <div class="page">
    <div class="button-container">
      <input v-model="search" type="text" placeholder="Kullanıcı ara" class="search-box" />
      <button @click="$router.push('/addwithexcel')" class="add-user-button marginr">Excel'den Toplu Ekle</button>
      <button @click="$router.push('/adduser')" class="add-user-button">Kullanıcı Ekle</button>
    </div>
    <div class="isactive-dropdown" style="margin-left: 2%;">
      Filtrele: 
      <select v-model="isActive" @change="filterUsers">
        <option value="all">Tümü</option>
        <option value="active">Aktif Olanlar</option>
        <option value="inactive">Aktif Olmayanlar</option>
      </select>
    </div>
    <div class="content">
      <h2 v-if="filteredStudents.length">Öğrenciler</h2>
      <div v-for="student in filteredStudents" :key="student.userId" @click="openUpdateModal(student)" :class="['list-item', student.active ? 'active' : 'inactive']">
        {{ student.userId }} - {{ student.name }} {{ student.surname }}
        <div>
          <button @click.stop="openPasswordModal(student, $event)" class="marginr change-password-button">Şifre degistir</button>
          <button @click="deleteUser(student.userId, student.bolumId, $event)" class="delete-button">Sil</button>
          
        </div>
        </div>

      <h2 v-if="filteredTeachers.length">Öğretim Elemanları</h2>
      <div v-for="teacher in filteredTeachers" :key="teacher.userId" @click="openUpdateModal(teacher)" :class="['list-item', teacher.active ? 'active' : 'inactive']">
        
        {{ teacher.userId }} - {{ teacher.name }} {{ teacher.surname }}
        <div>
          <button @click.stop="openPasswordModal(teacher, $event)" class="change-password-button marginr">Şifre Değiştir</button>
          <button @click="deleteUser(teacher.userId, teacher.bolumId, $event)" class="delete-button">sil</button>
        </div>
        
      </div>

      <h2 v-if="filteredNurses.length">Hemşireler</h2>
      <div v-for="nurse in filteredNurses" :key="nurse.userId" @click="openUpdateModal(nurse)" :class="['list-item', nurse.active ? 'active' : 'inactive']">
        {{ nurse.userId }} - {{ nurse.name }} {{ nurse.surname }}
        <div>
          <button @click="openPasswordModal(nurse, $event)" class="change-password-button marginr">Şifre Değiştir</button>
          <button @click="deleteUser(nurse.userId, nurse.bolumId, $event)" class="delete-button">Sil</button>
        </div>
      </div>

      <h2 v-if="filteredAdmins.length">Yöneticiler</h2>
      <div v-for="admin in filteredAdmins" :key="admin.userId" @click="openUpdateModal(admin)" :class="['list-item', admin.active ? 'active' : 'inactive']">
        {{ admin.userId }} - {{ admin.name }} {{ admin.surname }}
        <div>
          <button @click="openPasswordModal(admin, $event)" class="change-password-button marginr">Şifre Değiştir</button>
          <button @click="deleteUser(admin.userId, admin.bolumId, $event)" class="delete-button">Sil</button>
        </div>
      </div>
    </div>
    <UserUpdateModal v-if="selectedUser" :user="selectedUser" @close="selectedUser = null" @update="updateUser" />
    <PasswordChangeModal 
    v-if="showPasswordChangeModal" 
    :user="passwordChangeUserId"
    @close="showPasswordChangeModal = false" 
    />
  </div>
</template>
<script>
import axios from 'axios';
import UserUpdateModal from './UpdateUser.vue';
import PasswordChangeModal from './ChangePasswordModal.vue';

export default {
  components: {
    UserUpdateModal,
    PasswordChangeModal
  },
  props: {
    userId: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      admins: [],
      teachers: [],
      students: [],
      nurses: [],
      search: '',
      showModal: false,
      selectedUser: null,
      showPasswordChangeModal: false, 
      passwordChangeUserId: null,     
      isActive: 'all',
    };
  },

  methods: {
    filterByStatusAndSearch(users) {
      return users.filter(user => {
        const searchInput = this.search.trim().toLowerCase();
        let matchesSearch = true; // Eğer arama kutusu boşsa, varsayılan olarak tüm kullanıcıları döndür.

        if (searchInput) {
          if (isNaN(this.search)) {
            // Harf girilmişse ad ve soyadı kontrol et
            matchesSearch = user.name.toLowerCase().includes(searchInput) || user.surname.toLowerCase().includes(searchInput);
          } else {
            // Rakam girilmişse userId'yi kontrol et
            matchesSearch = user.userId.toString().includes(this.search);
          }
        }

        // Aktiflik durumuna göre filtreleme
        let isActiveUser = true; // Eğer aktiflik filtresi "all" ise tüm kullanıcıları döndür.
        if (this.isActive === 'active') {
          isActiveUser = user.active;
        } else if (this.isActive === 'inactive') {
          isActiveUser = !user.active;
        }

        return matchesSearch && isActiveUser;
      });
    },

    // Diğer metodlarınız...
    openUpdateModal(user) {
      this.selectedUser = user;
    },
    openPasswordModal(user, event) {
      event.stopPropagation();
      this.passwordChangeUserId = user;
      this.showPasswordChangeModal = true;
    },
    updateUser(updatedUser) {
      const index = this.admins.findIndex(user => user.userId === updatedUser.userId);
      if (index !== -1) {
        this.$set(this.admins, index, updatedUser);
      }
      this.selectedUser = null;
    },
    goToAddUser() {
      this.$router.push('/add-user');
    },
    addFromExcel() {
      this.$router.push('/addfromexcel');
    },
    deleteUser(userId, bolumId, event) {
      event.stopPropagation();
      const confirmed = confirm('Kullanıcı silinecek. Onaylıyor musunuz?');
      if (confirmed) {
        axios.delete(`http://localhost:8082/nursapp/app/admin/deleteuser/${bolumId}-${userId}-${this.$store.state.user.userId}`, {
          headers: {
            'Authorization': `Bearer ${this.$store.state.user.token}`,
          },
        }).then(() => {
          this.admins = this.admins.filter(user => user.userId !== userId);
          this.teachers = this.teachers.filter(user => user.userId !== userId);
          this.students = this.students.filter(user => user.userId !== userId);
          this.nurses = this.nurses.filter(user => user.userId !== userId);
        }).catch(error => {
          console.error(`Failed to delete user with ID ${userId}:`, error);
        });
      }
    },
    async fetchUsers() {
      try {
        const bolumId = this.$store.state.user.bolumId;
        const token = this.$store.state.user.token;
        const response = await axios.get(`http://localhost:8082/nursapp/app/admin/getusers/${bolumId}`, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          }
        });
        this.admins = response.data.admins;
        this.teachers = response.data.teachers;
        this.students = response.data.students;
        this.nurses = response.data.nurses;
      } catch (error) {
        console.error('Error fetching users with axios:', error);
      }
    }
  },
  mounted() {
    if (this.$store.state.user && this.$store.state.user.token) {
      this.fetchUsers();
    } else {
      console.error('Store or user info is not ready yet.');
    }
  },
  computed: {
    filteredStudents() {
      return this.filterByStatusAndSearch(this.students);
    },
    filteredTeachers() {
      return this.filterByStatusAndSearch(this.teachers);
    },
    filteredNurses() {
      return this.filterByStatusAndSearch(this.nurses);
    },
    filteredAdmins() {
      return this.filterByStatusAndSearch(this.admins);
    }
  }
};
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.button-container {
  display: flex;
  justify-content: flex-end;
  padding: 0.625rem;
  /* 10px */
}

.active {
  /* Aktif kullanıcılar için stil */
  background-color: #DFF0D8; /* Açık yeşil */
  color: #3C763D; /* Koyu yeşil */
}

.inactive {
  /* Pasif kullanıcılar için stil */
  background-color: #F2DEDE; /* Açık kırmızı */
  color: #A94442; /* Koyu kırmızı */
  opacity: 0.6; /* Daha soluk görünüm */
}

.list-item {
  /* Mevcut stil kurallarınız */
  /* Diğer stilleriniz... */
  transition: background-color 0.3s, opacity 0.3s; /* Geçiş efekti ekleyebilirsiniz */
}

.search-box {
  padding: 0.625rem;
  /* 10px */
  border-radius: 0.3125rem;
  /* 5px */
  border: 1px solid #ccc;
  font-size: 1rem;
  /* 16px */
  width: 80%;
  margin-right: 1rem;
}

.list-item {
  margin: 0.3125rem;
  /* 5px */
  padding: 0.625rem;
  /* 10px */
  background-color: grey;
  border-radius: 0.3125rem;
  /* 5px */
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  justify-content: space-between;
}

.list-item:hover {
  background-color: #505050;
}

.delete-button {
  background-color: #f44336;
  /* Red */
  border: none;
  color: white;
  padding: 5px 15px;
  text-align: center;
  font-size: 14px;
  cursor: pointer;
}

.change-password-button {
  background-color: #4CAF50;
  /* Green */
  border: none;
  color: white;
  padding: 5px 15px;
  text-align: center;
  font-size: 14px;
  cursor: pointer;
}

.add-user-button {
  padding: 10px 20px;
  background-color: #96B0BE;
  /* yeşil */
  border: none;
  border-radius: 5%;
  color: white;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.marginr {
  margin-right: 1rem;
}

.add-user-button:hover {
  background-color: #608295;
}
</style>
