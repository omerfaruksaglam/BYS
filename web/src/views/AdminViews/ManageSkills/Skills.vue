<template>
  <div class="page">
    <h2>Beceriler </h2>
    <button @click="showBulkAddModal = true" class="bulk-add-button">Toplu Beceri Ekle</button>
    <div class="content">
      <draggable v-model="skills" @end="onEnd" class="drag-area">
        <div v-for="skill in skills" :key="skill.step_no" class="list-item">
          <div class="skill-name">{{ skill.namee }}</div>
          <div>
            <button @click.stop="openUpdateModal(skill)" class="update-button">Beceri ismini değiştir</button>
            <button @click.stop="editSubSkills(skill)" class="subskill-button">Alt Becerilerini düzenle</button>
            <button @click.stop="deleteSkill(skill.skillId, $event)" class="delete-button">Sil</button>
          </div>
        </div>
      </draggable>
    </div>
  <!-- Toplu Ekle Modal -->
  <div v-if="showBulkAddModal" class="modal">
      <div class="modal-content">
        <h3>Toplu Beceri Ekle</h3>
        <textarea v-model="bulkSkills" placeholder="Becerileri satır satır giriniz"></textarea>
        <div class="modal-footer">
          <button @click="submitBulkAdd">Kaydet</button>
          <button @click="showBulkAddModal = false">İptal</button>
        </div>
      </div>
    </div>

    <button v-if="!showAddSkillInput" @click="showAddSkillInput = true">Yeni Beceri Ekle</button>
    <div v-if="showAddSkillInput">
      <input v-model="newSkillName" placeholder="Yeni beceri ismi" />
      <button class='addbutton' @click="addSkill">Ekle</button>
      <button @click="showAddSkillInput = false">iptal</button>
    </div>
    <div v-if="showModal" class="modal">
  <div class="modal-content">
    <h3>Beceri İsmini Güncelle</h3>
    <input v-model="newSkillName" placeholder="Yeni beceri ismi" />
    <button @click="updateSkillName">Kaydet</button>
    <button @click="showModal = false">İptal</button>
  </div>
</div>
  </div>
</template>
  
<script>
import axios from 'axios';
import { VueDraggableNext } from 'vue-draggable-next'
export default {
  components: {
    draggable: VueDraggableNext,
  },
  data() {
    return {
      skills: [],
      selectedSkill: null,
      showAddSkillInput: false,
      newSkillName: '',
      showModal: false, 
      updatingSkillId: null,
      showBulkAddModal: false,
      bulkSkills: '',    
    };
  },
  created() {
    this.getSkills();
  },
  methods: {




    
   async submitBulkAdd() {
    const token = this.$store.state.user.token;
      const skillLines = this.bulkSkills.split('\n').filter(line => line.trim() !== '');
// Her bir satırı Skills nesnesi haline getiriyoruz
    const skillsToSubmit = skillLines.map(skillName => ({ 
    namee: skillName.trim(),
    stepNo: this.skills.length + 1, // Mevcut beceri sayısına göre sıra numarası belirleyin
    bolumId: this.$store.state.user.bolumId // Kullanıcının bölüm ID'sini gönderin
}));
console.log('Eklenecek beceriler:', skillsToSubmit);
// Admin ID'nizi uygun bir şekilde ayarlayın
const adminId = this.$store.state.user.userId; // Örneğin Vuex store'dan alınıyor
await axios.post(`http://localhost:8082/nursapp/app/admin/addskills/${this.$store.state.user.userId}`, skillsToSubmit, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      }).then(response => {
        // İstek başarılı olduğunda kullanıcıyı bilgilendir ve modalı kapat
        alert('Beceriler başarıyla eklendi.');
        this.showBulkAddModal = false;
        this.bulkSkills = '';
        this.getSkills(); // Becerileri yeniden yüklemek için gerekli olan methodları burada çağırın
      })
      .catch(error => {
        // Bir hata oluşursa kullanıcıyı bilgilendir
        console.error('Beceriler eklenirken hata oluştu:', error);
        alert('Beceriler eklenirken bir hata oluştu, lütfen tekrar deneyiniz.');
      });

      this.showBulkAddModal = false;
      this.bulkSkills = '';
      // Becerileri yeniden yüklemek için gerekli olan methodları burada çağırın
    },
    onEnd(event) {
      console.log('Dragged skill:', event);
      const token = this.$store.state.user.token;
      const skills = this.skills.map(skill => skill.skillId);
      console.log('Sıralama:', skills);
      axios.put(`http://localhost:8082/nursapp/app/admin/update-skill-order`, 
        skills
      , {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          console.log('Sıralama başarıyla güncellendi:', response);
        })
        .catch(error => {
          console.error('Sıralama güncellenirken hata oluştu:', error);
        });
    },
    editSubSkills(skill) {
      this.$router.push({
        name: 'SubSkills',
        params: { 
          skillName: skill.namee,
          skillId: skill.skillId, // skillName parametresi olarak skill'in adını da gönder
    }
  });
},
    getSkills() {
      const bolumId = this.$store.state.user.bolumId;
      const token = this.$store.state.user.token;
      console.log('bolumId:', bolumId);
      console.log('token:', token);
      axios.get(`http://localhost:8082/nursapp/app/admin/getskills/${bolumId}`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          this.skills = response.data.sort((a, b) => a.step_no - b.step_no);
          console.log('Skills:', this.skills);
        })
        .catch(error => {
          console.error('Failed to fetch skills:', error);
        });
    },
    deleteSkill(skillId, event) {
      event.stopPropagation();
      const confirmed = confirm('Bu beceriyi silmek istediğinizden emin misiniz?');
      if (confirmed) {
        console.log("Beceri siliniyor, ID:", skillId);
        axios.delete(`http://localhost:8082/nursapp/app/admin/deleteskill/${skillId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + this.$store.state.user.token
          }
        })
          .then(response => {
            // Silme işlemi başarıyla tamamlandığında kullanıcıyı bilgilendir
            alert('Beceri başarıyla silindi.');
            this.skills = this.skills.filter(skill => skill.skillId !== skillId);
          })
          .catch(error => {
            // Backend'den gelen hata mesajını kullanıcıya göster.
            let errorMessage = 'Beceri silinirken bir hata oluştu. Lütfen tekrar deneyin.';
            if (error.response && error.response.data && error.response.data.message) {
              errorMessage = error.response.data.message;
            }
            alert(errorMessage);
            console.error('Beceri silinirken hata oluştu:', error);
          });
      }
    },
    addSkill() {
      const bolumId = this.$store.state.user.bolumId;
      const token = this.$store.state.user.token;
      console.log("skills length:", this.skills.length);
      axios.post(`http://localhost:8082/nursapp/app/admin/addskill/${this.$store.state.user.userId}`, {
        namee: this.newSkillName,
        stepNo: this.skills.length + 1, 
        bolumId : bolumId
      }, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          // Ekleme işlemi başarıyla tamamlandığında kullanıcıyı bilgilendir
          alert('Yeni beceri başarıyla eklendi.');
          // Eklenen beceriyi UI'ya eklemek için gerekli işlemler
          this.skills.push(response.data);
          this.showAddSkillInput = false;
          this.newSkillName = '';
        })
        .catch(error => {
          // Backend'den gelen hata mesajını kullanıcıya göster.
          let errorMessage = 'Beceri eklenirken bir hata oluştu. Lütfen tekrar deneyin.';
          if (error.response && error.response.data && error.response.data.message) {
            errorMessage = error.response.data.message;
          }
          alert(errorMessage);
          console.error('Beceri eklenirken hata oluştu:', error);
        });
    },
    openUpdateModal(skill) {
    this.updatingSkillId = skill.skillId; // Seçilen becerinin ID'sini saklayın
    this.newSkillName = skill.namee; // Mevcut beceri ismini önceden doldurun
    this.showModal = true; // Modalı göster
  },
  updateSkillName() {
  if (!this.newSkillName.trim()) return; // Boş isim girilmemesini kontrol edin

  const token = this.$store.state.user.token;
  console.log('Güncellenen beceri ID:', this.updatingSkillId);
  axios.put(`http://localhost:8082/nursapp/app/admin/updateskill/${this.updatingSkillId}-${this.$store.state.user.userId}`, {
    name: this.newSkillName, // Yeni ismi gönderin
  }, {
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    }
  })
  .then(response => {
    alert('Beceri ismi başarıyla güncellendi.');
    this.getSkills(); // Beceriler listesini yeniden çekin
    this.showModal = false; // Modalı kapatın
  })
  .catch(error => {
    alert('Beceri ismi güncellenirken bir hata oluştu.');
    console.error('Güncelleme hatası:', error);
  });
},
  }
};
</script>
  
<style scoped>
* {
  font-family: Arial, sans-serif;
  /* Set the font family for all elements */
  font-size: 18px;
  /* Set the font size for all elements */
}

.page {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content {
  display: flex;
  flex-direction: column;
  margin: 0 5%;
}

.skill-name {
  max-width: 65%; /* Adjust as needed */
  word-wrap: break-word;
}

.list-item {
  margin: 1rem 0;
  padding: 15px;
  background-color: #f0f0f0;
  border-radius: 5px;
  color: #333;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  justify-content: space-between;
}

.list-item:hover {
  background-color: #e0e0f0;
}

.delete-button,
.subskill-button,
.update-button,
.addbutton,
button {
  margin: 5px;
  padding: 5px 10px;
  font-size: 14px;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
}

.delete-button {
  background-color: #f44336;
}

.delete-button:hover {
  background-color: #d32f2f;
}

.subskill-button {
  background-color: #047591;
}

.subskill-button:hover {
  background-color: #005b6e;
}

.update-button {
  background-color: #0056b3;
}

.update-button:hover {
  background-color: #004080;
}

.addbutton,
button {
  background-color: #007BFF;
}

button:hover {
  background-color: #0056b3;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

div {
  margin-top: 10px;
}

.modal {
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0,0,0,0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

/* Toplu Ekle Buton Stili */
.bulk-add-button {
  /* Buton stil ayarlarını buraya ekleyin */
  margin-top: 1em;
  /* Diğer stil ayarları */
}

/* Modal Stili */
.modal {
  /* Modal stil ayarlarını buraya ekleyin */
  position: fixed;
  z-index: 1000;
  /* Diğer stil ayarları */
}

/* Modal İçerik Stili */
.modal-content {
  /* Modal içerik stil ayarlarını buraya ekleyin */
  background-color: #fff;
  /* Diğer stil ayarları */
}

/* Modal Footer Stili */
.modal-footer {
  /* Modal footer stil ayarlarını buraya ekleyin */
  display: flex;
  justify-content: flex-end;
  /* Diğer stil ayarları */
}

/* Textarea Stili */
textarea {
  /* Textarea stil ayarlarını buraya ekleyin */
  width: 100%;
  height: 200px;
  max-width: 100%;
  margin-top: 1em;
  /* Diğer stil ayarları */
}

@media (max-width: 600px) {
  .page {
    padding: 10px;
  }

  .list-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .skill-name,
  .update-button,
  .subskill-button,
  .delete-button,
  .addbutton,
  button {
    width: 100%; /* Butonları ve skill isimlerini genişlet */
    margin: 5px 0; /* Daha fazla boşluk */
  }

  .modal-content {
    width: 90%; /* Modalı biraz daha geniş yap */
  }
}

/* Tablet ekranlar için stil ayarlamaları */
@media (min-width: 601px) and (max-width: 1024px) {
  .page {
    padding: 20px;
  }

  .list-item {
    padding: 20px;
  }

  .modal-content {
    width: 70%; /* Modalı geniş ekranlar için biraz daralt */
  }
}

</style>
  