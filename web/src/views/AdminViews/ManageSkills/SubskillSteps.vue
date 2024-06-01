<template>
  <div class="page">
    <div class="container">


      <div class="skills-section">
        <h2>Alt Beceri Basamakları : {{ subskillName }}</h2>
        <!-- Toplu Adım Ekleme Butonu ve Modal -->
        <button @click="showBulkAddStepModal = true" class="bulk-add-button">Toplu Adım Ekle</button>
        <div v-if="showBulkAddStepModal" class="modal">
          <div class="modal-content">
            <h3>Toplu Adım Ekle</h3>
            <textarea v-model="bulkSteps" placeholder="Adımları satır satır giriniz"></textarea>
            <div class="modal-footer">
              <button @click="submitBulkAddSteps">Kaydet</button>
              <button @click="showBulkAddStepModal = false">İptal</button>
            </div>
          </div>
        </div>
        <div class="content">

          <draggable v-model="subSkillSteps" @end="onEnd" group="steps" class="drag-area">
            <div v-for="step in subSkillSteps" :key="step.stepNo" class="list-item">
              <div class="step-name">{{ step.name }}</div>
              <div class="step-controls">

                <div style="display: flex ; align-items: center;">
                  <input type="checkbox" :id="'header' + step.stepNo" v-model="step.head" @change="updateHeader(step)">
                  <label :for="'header' + step.stepNo">Başlık</label>
                </div>
                <button @click="openUpdateModalStep(step)" class="update-button">Adımı Düzenle</button>
                <button @click.stop="confirmDeleteStep(step.subskillStepsId)" class="delete-button">Sil</button>
              </div>
            </div>
          </draggable>
          <div v-if="showAddStepInput" class="add-input">
            <input v-model="newStepName" placeholder="Adım ismi" />
            <button @click="addStep" class='addbutton'>Ekle</button>
            <button @click="showAddStepInput = false" class='cancel-button'>İptal</button>
          </div>
          <button v-else @click="showAddStepInput = true" class="add-step-button">Yeni Adım Ekle</button>
        </div>
      </div>

      <div class="materials-section">
        <h2>Malzemeler</h2>
        <!-- Toplu Malzeme Ekleme Butonu ve Modal -->
        <button @click="showBulkAddMaterialModal = true" class="bulk-add-button">Toplu Malzeme Ekle</button>
        <div v-if="showBulkAddMaterialModal" class="modal">
          <div class="modal-content">
            <h3>Toplu Malzeme Ekle</h3>
            <textarea v-model="bulkMaterials" placeholder="Malzemeleri satır satır giriniz"></textarea>
            <div class="modal-footer">
              <button @click="submitBulkAddMaterials">Kaydet</button>
              <button @click="showBulkAddMaterialModal = false">İptal</button>
            </div>
          </div>
        </div>
        <div class="content">
          <div v-for="equ in equipments" :key="equ.equipmentId" class="list-item">
            <div class="step-name">{{ equ.name }}</div>
            <div class="materaials">
              <button @click="openUpdateModalEqu(equ)" class="update-button">Düzenle</button>
              <button @click.stop="confirmDeleteEqu(equ.equipmentId)" class="delete-button">Sil</button>
            </div>
          </div>
          <div v-if="showAddEquInput" class="add-input">
            <input v-model="newEquName" placeholder="Malzeme ismi" />
            <button @click="addEquipment" class='addbutton'>Ekle</button>
            <button @click="showAddEquInput = false" class='cancel-button'>İptal</button>
          </div>
          <button v-else @click="showAddEquInput = true" class="add-step-button">Yeni Malzeme Ekle</button>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>{{ modalTitle }}</h3>
        <div class="grup">
          <p>Adı </p>
          <input v-model="modalInput" placeholder="Giriniz" />
        </div>
        <div class="grup" v-if="malzemeden">
          <p>Ağırlığı </p>
          <input type="number" v-model="modalWeight" placeholder="Weight" min="1" />
        </div>

        <button @click="submitModalAction"    :class="{ 'disabled-button': modalWeight < 1 }"
   :disabled="modalWeight < 1">{{ modalAction }}</button>
        <button @click="closeModal" class='cancel-button'>İptal</button>
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import { vModelRadio } from 'vue';
import { VueDraggableNext } from 'vue-draggable-next'

export default {
  components: {
    draggable: VueDraggableNext,
    vModelRadio
  },
  props: ['subSkillId', 'subskillName'],
  data() {
    return {
      subSkillSteps: [],
      equipments: [],
      showAddStepInput: false,
      showAddEquInput: false,
      newStepName: '',
      newEquName: '',
      updatingStepId: null,
      updatingEquId: null,
      showModal: false,
      head: false,
      modalWeight: '',
      malzemeden: true,
      showBulkAddStepModal: false,
      bulkSteps: '',
      showBulkAddMaterialModal: false,
      bulkMaterials: '',
    };
  },
  created() {
    this.getSubSkillSteps();
    this.getEquipments();
  },
  methods: {
    
    onEnd(event) {
      event.stopPropagation();
      console.log('Dragged step:', event);
      const token = this.$store.state.user.token;
      // Make sure that the property name here matches the property in your objects
      const stepOrder = this.subSkillSteps.map(step => step.subskillStepsId);
      console.log('Sıralama:', stepOrder);

      if (stepOrder.includes(undefined)) {
        console.error('One or more step IDs are undefined:', stepOrder);
        return;
      }
      axios.put(`http://localhost:8082/nursapp/app/admin/update-subskill-step-order`, stepOrder, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          console.log('Step order updated successfully:', response);
        })
        .catch(error => {
          console.error('Error updating step order:', error);
          alert('Adımların sırası güncellenirken bir hata oluştu.');
        });
    },
    updateHeader(step) {
      try {
        const token = this.$store.state.user.token;
        const stepid = step.subskillStepsId;
        console.log('stepid:', stepid);
        axios.put(`http://localhost:8082/nursapp/app/admin/${stepid}/update-header-status?head=${step.head}`, {
          head: step.head
        }, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          }
        })
          .then(response => {
            console.log('Başlık durumu güncellendi:', response);
          })
          .catch(error => {
            console.error('Başlık durumu güncellenirken hata oluştu:', error);
          });
      } catch (error) {
        console.error('updateHeader metodunda hata:', error);
      }
    },
    getSubSkillSteps() {
      const token = this.$store.state.user.token;
      axios.get(`http://localhost:8082/nursapp/app/admin/getsubskillssteps/${this.subSkillId}`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          this.subSkillSteps = response.data;
          console.log("SubSkillSteps: ", this.subSkillSteps);
        })
        .catch(error => console.error("SubSkillSteps çekilirken hata oluştu:", error));
    },
    getEquipments() {
      const token = this.$store.state.user.token;
      axios.get(`http://localhost:8082/nursapp/app/admin/equipments/${this.subSkillId}`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          this.equipments = response.data;
          console.log("Equipments: ", this.equipments);
        })
        .catch(error => console.error("Equipments çekilirken hata oluştu:", error));
    },

    submitBulkAddSteps() {
      const stepsToAdd = this.bulkSteps.split('\n').filter(stepName => stepName.trim() !== '');
      const stepsData = stepsToAdd.map(stepName => ({
        name: stepName.trim(),
        subSkillId: this.subSkillId,
        stepNo: this.subSkillSteps.length + 1,
        head: false,
        weight: 1
      }));

      // Burada API'nize uygun POST isteği gönderin
      axios.post(`http://localhost:8082/nursapp/app/admin/addsubskillsteps/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, stepsData, {
        headers: {
          'Authorization': `Bearer ${this.$store.state.user.token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        console.log('Steps added successfully', response);
        // Adımları güncelleyin
        this.getSubSkillSteps();
      })
      .catch(error => {
        console.error('Error adding steps', error);
      });

      // Modal'ı kapat ve formu temizle
      this.showBulkAddStepModal = false;
      this.bulkSteps = '';
    },

    submitBulkAddMaterials() {
      const materialsToAdd = this.bulkMaterials.split('\n').filter(materialName => materialName.trim() !== '');
      const materialsData = materialsToAdd.map(materialName => ({
        name: materialName.trim(),
        subskillId: this.subSkillId,
      }));

      // Burada API'nize uygun POST isteği gönderin
      axios.post(`http://localhost:8082/nursapp/app/admin/addequipments/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, materialsData, {
        headers: {
          'Authorization': `Bearer ${this.$store.state.user.token}`,
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        console.log('Materials added successfully', response);
        // Malzemeleri güncelleyin
        this.getEquipments();
      })
      .catch(error => {
        console.error('Error adding materials', error);
      });

      // Modal'ı kapat ve formu temizle
      this.showBulkAddMaterialModal = false;
      this.bulkMaterials = '';
    },
    
    addStep() {
      if (!this.newStepName) return;
      const token = this.$store.state.user.token;
      axios.post(`http://localhost:8082/nursapp/app/admin/addsubskillstep/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        name: this.newStepName,
        subSkillId: this.subSkillId,
        step_no: this.subSkillSteps.length + 1,
        head: false,
        weight: 1
      }, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(() => {
          this.closeModal();
          this.getSubSkillSteps();
        })
        .catch(error => console.error("Adım eklenirken hata oluştu:", error));
    },
    addEquipment() {
      if (!this.newEquName) return;
      const token = this.$store.state.user.token;
      console.log("new equ name: " + this.newEquName);
      axios.post(`http://localhost:8082/nursapp/app/admin/addequipment/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        name: this.newEquName,
        subskillId: this.subSkillId,
      }, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(() => {
          this.closeModal();
          this.getEquipments();
        })
        .catch(error => console.error("Malzeme eklenirken hata oluştu:", error));
      this.newEquName = '';
      this.showAddEquInput = false;
    },
    openUpdateModalStep(step) {
      this.malzemeden = true;
      this.modalInput = step.name; // Modal input alanına mevcut ismi yerleştir
      this.updatingStepId = step.subskillStepsId;
      console.log("open update model ssid: " + this.updatingStepId);
      this.newStepName = this.modalInput;
      console.log("new step name: " + this.newStepName);
      console.log("modal input: " + this.modalInput);
      this.modalTitle = 'Alt Beceri Basamaklarını Düzenle'; // Modal başlığını ayarla
      this.modalAction = 'Güncelle'; // Modal buton etiketini ayarla
      this.submitModalAction = () => this.updateStep(this.updatingStepId); // Modal kaydetme aksiyonunu ayarla
      this.modalWeight = step.weight;
      this.showModal = true; // Modal'ı göster
    },
    updateStep() {
      if (!this.modalInput.trim()) return;
      const token = this.$store.state.user.token;
      console.log("lee: " + this.updatingStepId);
      console.log("new step name: " + this.newStepName);

      axios.put(`http://localhost:8082/nursapp/app/admin/updatesubskillstep/${this.updatingStepId}/${this.modalWeight}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        name: this.modalInput,
      }, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(() => {
          alert('Adım başarıyla güncellendi.');
          this.getSubSkillSteps();
          this.showModal = false;
        })
        .catch(error => {
          alert('Adım güncellenirken bir hata oluştu.');
          console.error('Adım güncellenirken hata:', error);
        });
      this.closeModal(); // İşlem bittikten sonra modal'ı kapat
    },

    openUpdateModalEqu(equ) {
      this.malzemeden = false;
      this.modalInput = equ.name; // Modal input alanına mevcut ismi yerleştir
      console.log("equid: " + equ.equipmentId);
      this.updatingEquId = equ.equipmentId;
      console.log("open update model equid: " + this.updatingEquId);
      this.newEquName = this.modalInput;
      console.log("new equ name: " + this.newEquName);
      console.log("modal input: " + this.modalInput);
      this.modalTitle = 'Malzeme Düzenle'; // Modal başlığını ayarla
      this.modalAction = 'Güncelle'; // Modal buton etiketini ayarla
      this.submitModalAction = () => this.updateEquipment(this.updatingEquId); // Modal kaydetme aksiyonunu ayarla
      this.showModal = true; // Modal'ı göster
    },

    updateEquipment(id) {
      if (!this.modalInput.trim()) return;
      const token = this.$store.state.user.token;
      console.log("updating equ id : " + id);
      console.log("new equipment name: " + this.newEquName);
      axios.put(`http://localhost:8082/nursapp/app/admin/updateequipment/${id}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        name: this.modalInput,
      }, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(() => {
          alert('Malzeme başarıyla güncellendi.');
          this.getEquipments();
          this.showModal = false;
        })
        .catch(error => {
          alert('Malzeme güncellenirken bir hata oluştu.');
          console.error('Malzeme güncellenirken hata:', error);
        });
      this.closeModal(); // İşlem bittikten sonra modal'ı kapat
    },

    closeModal() {
      // Modal kapatma işlemleri
      this.showModal = false;
      this.updatingEquId = null;
      this.newEquName = '';
      this.updatingStepId = null;
      this.newStepName = '';
    },

    confirmDeleteStep(stepId) {
      if (confirm("Bu adımı silmek istediğinizden emin misiniz?")) {
        this.deleteStep(stepId);
      }
    },
    deleteStep(stepId) {
      const token = this.$store.state.user.token;
      console.log("delete step id: " + stepId);
      axios
        .delete(`http://localhost:8082/nursapp/app/admin/deletesubskillstep/${stepId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          }
        })
        .then(() => {
          this.getSubSkillSteps();
        })
        .catch(error => console.error("Adım silinirken hata oluştu:", error));
    },

    confirmDeleteEqu(equipmentId) {
      if (confirm("Bu adımı silmek istediğinizden emin misiniz?")) {
        this.deleteEqu(equipmentId);
      }
    },
    deleteEqu(equipmentId) {
      const token = this.$store.state.user.token;
      console.log("delete equ id: " + equipmentId);
      axios.delete(`http://localhost:8082/nursapp/app/admin/deleteequipment/${equipmentId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(() => {
          this.getEquipments();
        })
        .catch(error => console.error("malzeme silinirken hata oluştu:", error));
    },
    updateWeight(step) {
      const token = this.$store.state.user.token;
      console.log("subskillstep :", step);
      console.log("step.weight:", step.weight);
      axios
        .put(
          `http://localhost:8082/nursapp/app/admin/updatesubskillstepweight/${step.subskillStepsId}`,
          step.weight, // Ağırlık değerini bir nesne olarak gönderin
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        )
        .then(() => {
          alert("Alt beceri ağırlığı başarıyla güncellendi.");
        })
        .catch((error) => {
          alert("Alt beceri ağırlığı güncellenirken bir hata oluştu.");
          console.error("Alt beceri ağırlığı güncellenirken hata:", error);
        });
    },
  }
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.page {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 100%;
  /* Sayfanın genişliğini sınırla */
  padding: 20px;
  /* Sayfanın etrafındaki boşluğu ayarla */
}

.container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  /* Konteyner genişliğini maksimum yap */
  max-width: 1200px;
  /* Konteyner için maksimum genişlik */
  gap: 20px;
  /* Bölümler arasındaki boşluğu ayarla */
}

.skills-section {
  width: 70%;
  /* Skills bölümü genişliği */
  padding: 20px;
  /* İç boşluk */
}

.materials-section {
  width: 30%;
  /* Materials bölümü genişliği */
  padding: 20px;
  /* İç boşluk */
}


.list-item {
  background-color: #ffffff;
  border: 1px solid #ddd;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.step-controls {
  display: flex;
  align-items: center;
}

.skill-name,
.equipment-name {
  font-weight: bold;
}

.materaials {}

.modal {
  position: fixed;
  z-index: 1000;
  /* Z-index değerini artırabilirsiniz */
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  /* Merkezlemek için flex kullan */
  align-items: center;
  /* Dikey olarak merkezle */
  justify-content: center;
  /* Yatay olarak merkezle */
}

.modal-content {
  background-color: #fefefe;
  margin: 5% auto; /* Modalı daha merkezi konumlandır */
  padding: 20px;
  border: 1px solid #888;
  width: 50%; /* Modal genişliğini artır */
  max-width: 800px; /* Maksimum genişlik limiti */
}


.add-step-input,
.add-equ-input {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.input-field {
  padding: 5px;
  margin-right: 5px;
  border: 1px solid #ddd;
  border-radius: 3px;
  flex-grow: 1;
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

.grup {
  display: flex;
  align-items: center;
  gap: 2rem;
}

textarea {
  width: 100%; /* Genişliği parent elementin genişliğine ayarlar */
  height: 200px; /* Yüksekliği 200 piksel olarak ayarlar */
  max-width: 100%; /* Maksimum genişlik ayarı */
}
.disabled-button {
  background-color: grey;
  cursor: not-allowed;
}
/* Daha küçük ekran boyutları için medya sorguları */
@media (max-width: 600px) {
  .container {
    flex-direction: column;
  }

  .skills-section,
  .materials-section {
    width: 100%;
    /* Mobil cihazlarda tam genişlik kullan */
  }
}

/* Tablet ekranlar için medya sorguları */
@media (min-width: 601px) and (max-width: 1024px) {
  /* Tablet için stil ayarlamaları */
}
</style>