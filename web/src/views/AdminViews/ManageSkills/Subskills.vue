<template>
  <div class="page">
    <h2>Alt Beceriler : {{ skillName }}</h2>
     <!-- Toplu Alt Beceri Ekleme Butonu -->
  <button @click="showBulkAddSubSkillModal = true" class="bulk-add-button">Toplu Alt Beceri Ekle</button>
<!-- Toplu Alt Beceri Ekleme Modalı -->
<div v-if="showBulkAddSubSkillModal" class="modal">
    <div class="modal-content">
      <h3>Toplu Alt Beceri Ekle</h3>
      <textarea v-model="bulkSubSkills" placeholder="Alt Becerileri satır satır giriniz"></textarea>
      <div class="modal-footer">
        <button @click="submitBulkAddSubSkills">Kaydet</button>
        <button @click="showBulkAddSubSkillModal = false">İptal</button>
      </div>
    </div>
  </div>
    <div class="content">
      <draggable v-model="subSkills" @end="onEnd" class="drag-area">
        <div
          v-for="subskill in subSkills"
          :key="subskill.stepNo"
          class="list-item"
        >
          <div class="skill-name">{{ subskill.name }}</div>
          <div>
            
            <button
              @click.stop="openUpdateModal(subskill)"
              class="update-button"
            >
              Alt Beceriyi düzenle
            </button>
            <button
              @click.stop="editSubSkillSteps(subskill)"
              class="subskillsteps-button"
            >
              Alt Beceri Basamaklarını düzenle
            </button>
            <button
              @click.stop="deleteSubSkill(subskill.subSkillId, $event)"
              class="delete-button"
            >
              Sil
            </button>
          </div>
        </div>
      </draggable>
    </div>
    <button v-if="!showAddSkillInput" @click="showAddSkillInput = true">
      Yeni Alt Beceri Ekle
    </button>
    <div v-if="showAddSkillInput">
      <input v-model="newsubSkillName" :placeholder="placeholderText" />
      <button class="addbutton" @click="addSubSkill()">Ekle</button>
      <button @click="showAddSkillInput = false">İptal</button>
    </div>
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>Beceri İsmini Güncelle</h3>
        <input
          v-model="updatingSubSkillName"
          placeholder="Yeni Alt beceri ismi"
        />
        <button @click="updateSubSkillName" type="button">Kaydet</button>

        <button @click="showModal = false">İptal</button>
      </div>
    </div>
  </div>
</template>
  
<script>
import axios from "axios";
import { VueDraggableNext } from "vue-draggable-next";
export default {
  components: {
    draggable: VueDraggableNext,
  },
  props: ["skillId", "skillName"],
  data() {
    return {
      subSkills: [],
      showAddSkillInput: false,
      newSubSkillName: "",
      updatingSubSkillName: "",
      showModal: false,
      updatingSubSkillId: null,
      placeholderText: "Yeni alt beceri ismini girin", // Default placeholder metni
      showAddSkillInput: false,
      showBulkAddSubSkillModal: false,
      bulkSubSkills: '',
    };
  },
  created() {
    this.getSubSkills();
  },
  methods: {
    onEnd(event) {
      console.log("Dragged subskill:", event);
      const token = this.$store.state.user.token;
      const subSkills = this.subSkills.map((subskill) => subskill.subSkillId);
      console.log("Sıralama:", subSkills);
      axios
        .put(
          `http://localhost:8082/nursapp/app/admin/update-subskill-order`,
          subSkills,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        )
        .then((response) => {
          console.log("Subskills sıralaması başarıyla güncellendi:", response);
        })
        .catch((error) => {
          console.error(
            "Subskills sıralaması güncellenirken hata oluştu:",
            error
          );
        });
    },
    editSubSkillSteps(subskill) {
      this.$router.push({
        name: "SubSkillSteps",
        params: {
          subskillName: subskill.name,
          subSkillId: subskill.subSkillId, // skillName parametresi olarak skill'in adını da gönder
        },
      });
    },
    getSubSkills() {
      const token = this.$store.state.user.token;
      console.log("skillId:", this.skillId);
      axios
        .get(
          `http://localhost:8082/nursapp/app/admin/getsubskills/${this.skillId}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        )
        .then((response) => {
          this.subSkills = response.data.sort((a, b) => a.stepNo - b.stepNo);
          console.log("subSkills:", response.data);
        })
        .catch((error) => {
          console.error("Failed to fetch subskills:", error);
        });
    },
    deleteSubSkill(subSkillId, event) {
      event.stopPropagation();
      const confirmed = confirm(
        "Bu alt beceriyi silmek istediğinizden emin misiniz?"
      );
      console.log("subSkillId:", subSkillId);
      if (confirmed) {
        axios
          .delete(
            `http://localhost:8082/nursapp/app/admin/deletesubskill/${subSkillId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + this.$store.state.user.token,
              },
            }
          )
          .then(() => {
            alert("Alt beceri başarıyla silindi.");
            this.getSubSkills(); // Listeyi yenile
          })
          .catch((error) => {
            alert("Alt beceri silinirken bir hata oluştu.");
            console.error("Alt beceri silinirken hata:", error);
          });
      }
    },

    async submitBulkAddSubSkills() {
      // Token ve diğer gerekli verileri al
      const token = this.$store.state.user.token;
      // Kullanıcı girişini satır satır ayır ve boşlukları temizle
      const subSkillLines = this.bulkSubSkills.split('\n').filter(line => line.trim() !== '');
      // Her satır için bir obje oluştur
      const subSkillsToSubmit = subSkillLines.map(subSkillName => ({ 
        name: subSkillName.trim(),
        stepNo: this.subSkills.length + 1,
        skillId: this.skillId, // Üst becerinin ID'si
        // Burada gerekirse diğer özellikleri de ekleyebilirsiniz
      }));
      // Toplu ekleme için POST isteği gönder
      await axios.post(`http://localhost:8082/nursapp/app/admin/addsubskills/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, subSkillsToSubmit, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
      .then(response => {
        // İşlem başarılıysa bildir ve modalı kapat
        alert('Alt beceriler başarıyla eklendi.');
        this.showBulkAddSubSkillModal = false;
        this.bulkSubSkills = '';
        this.getSubSkills(); // Alt becerileri yeniden yükle
      })
      .catch(error => {
        // Hata oluşursa bildir
        console.error('Alt beceriler eklenirken hata oluştu:', error);
        alert('Alt beceriler eklenirken bir hata oluştu, lütfen tekrar deneyiniz.');
      });
    }, 

    addSubSkill() {
      const token = this.$store.state.user.token;
      console.log("skillId:", this.skillId);
      console.log("newsubSkillName:", this.newsubSkillName);
      axios
        .post(
          `http://localhost:8082/nursapp/app/admin/addsubskill/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`,
          {
            name: this.newsubSkillName,
            stepNo: this.subSkills.length + 1,
            skillId: this.skillId,
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        )
        .then(() => {
          alert("Yeni alt beceri başarıyla eklendi.");
          this.getSubSkills(); // Listeyi yenile
          this.showAddSkillInput = false;
          this.newsubSkillName = "";
        })
        .catch((error) => {
          alert("Yeni alt beceri eklenirken bir hata oluştu.");
          console.error("Alt beceri eklenirken hata:", error);
        });
    },
    openAddNewSubSkill() {
      this.isAddingNewSubSkill = true; // Yeni bir alt beceri eklerken bu değeri true yapın
      this.newsubSkillName = ""; // Yeni alt beceri ismi girişini temizleyin
      this.showAddSkillInput = true; // Ekleme alanını gösterin
    },

    openUpdateModal(subskill) {
      this.isAddingNewSubSkill = false; // Düzenleme yaparken bu değeri false yapın
      this.updatingSubSkillName = subskill.name; // Düzenlenen alt becerinin ismini alın
      this.updatingSubSkillId = subskill.subSkillId; // Düzenlenen alt becerinin ID'sini alın
      this.showModal = true; // Modalı gösterin
    },
    updateSubSkillName() {
      if (!this.updatingSubSkillName.trim()) return;
      const token = this.$store.state.user.token;
      axios
        .put(
          `http://localhost:8082/nursapp/app/admin/updatesubskill/${this.updatingSubSkillId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`,
          {
            name: this.updatingSubSkillName,
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        )
        .then(() => {
          alert("Alt beceri ismi başarıyla güncellendi.");
          this.getSubSkills();
          this.showModal = false;
        })
        .catch((error) => {
          alert("Alt beceri ismi güncellenirken bir hata oluştu.");
          console.error("Alt beceri güncellenirken hata:", error);
        });
    },
    
  },
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
  margin: 0 5% ;
}

.skill-name {
  max-width: 65%;
  /* Adjust as needed */
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
  background-color: #007bff;
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
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}
input[type="number"] {
  width: 50px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.bulk-add-button {
    /* Buton stil ayarlarını buraya ekleyin */
    margin-top: 1em;
    /* Diğer stil ayarları */
  }

  textarea {
  width: 100%; /* Genişliği parent elementin genişliğine ayarlar */
  height: 200px; /* Yüksekliği 200 piksel olarak ayarlar */
  max-width: 100%; /* Maksimum genişlik ayarı */
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
    width: 100%;
    /* Butonları ve skill isimlerini genişlet */
    margin: 5px 0;
    /* Daha fazla boşluk */
  }

  .modal-content {
    width: 90%;
    /* Modalı biraz daha geniş yap */
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
    width: 70%;
    /* Modalı geniş ekranlar için biraz daralt */
  }
}
</style>
    