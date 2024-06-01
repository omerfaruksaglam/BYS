<template>
  <Page @navigatedTo="getSubSkillSteps">
    <ActionBar :title="SubSkillName" class="action-bar" />
    <ScrollView>
      <StackLayout>
        <!-- Buton Grubu -->
        <StackLayout orientation="horizontal" class="tepe">
          <Button text="      Alt Beceri Basamakları      " @tap="showStepsList" :class="{ 'selected': showSteps }" />
          <Button text="         Malzemeler          " @tap="showMaterialsList" :class="{ 'selected': showMaterials }" />
        </StackLayout>

        <!-- Steps Listesi -->
        <StackLayout v-if="showSteps">
          <StackLayout v-for="subskillsteps in SubSubSkillSteps" :key="subskillsteps.subskillStepsId" class="list-item">
            <Label class="skill-name" :text="subskillsteps.name" @longPress="SubskillStepsOptions(subskillsteps)" />
            <StackLayout class="horizontal-divider" />
          </StackLayout>
          <Button text="Yeni Alt Beceri Ekle" @tap="showAddSubSkillStepsInput = true" v-if="!showAddSubSkillStepsInput" />
          <StackLayout v-if="showAddSubSkillStepsInput">
            <TextField v-model="newSubSkillStepName" hint="Yeni Alt Beceri ismi" />
            <Button text="Ekle" @tap="addSubSkillStep" />
            <Button text="İptal" @tap="closeModal" />
          </StackLayout>
        </StackLayout>

        <!-- Malzemeler Listesi -->
        <StackLayout v-if="showMaterials">
          <StackLayout v-for="equipment in equipments" :key="equipment.id" class="list-item">
            <Label class="skill-name" :text="equipment.name" @longPress="EquipmentOptions(equipment)" />
            <StackLayout class="horizontal-divider" />
          </StackLayout>
          <Button text="Yeni Malzeme Ekle" @tap="showAddEquipmentInput = true" v-if="!showAddEquipmentInput" />
          <StackLayout v-if="showAddEquipmentInput">
            <TextField v-model="newEquipmentName" hint="Yeni malzeme ismi" />
            <Button text="Ekle" @tap="addEquipment" />
            <Button text="İptal" @tap="closeModal" />
          </StackLayout>
        </StackLayout>

      </StackLayout>
    </ScrollView>

    <!-- Alt beceri eklemek için modal -->

  </Page>
</template>

<script>
import SubskillStepsOptionsModal from '~/views/AdminViews/BeceriYonetimi/SubSkillStepsOptions.vue';
import EquipmentOptionsModal from '~/views/AdminViews/BeceriYonetimi/EquipmentOptions.vue';
export default {
  props: ["SubSkillId", "SubSkillName"],
  data() {
    return {
      SubSubSkillSteps: [],
      equipments: [],
      showSteps: true,
      showMaterials: false,
      showAddSubSkillStepsInput: false,
      showAddEquipmentInput: false,
      newSubSkillStepName: '',
      newEquipmentName: '',
    };
  },
  methods: {
    SubskillStepsOptions(subskillsteps) {
      console.log("subskillsteps:", subskillsteps);
      this.$showModal(SubskillStepsOptionsModal, {
        props: {
          subskillsteps: subskillsteps
        },
        fullscreen: true,
        animated: true,
      });
    },
    EquipmentOptions(equipment) {
      console.log("equipment:", equipment);
      this.$showModal(EquipmentOptionsModal, {
        props: {
          equipment: equipment
        },
        fullscreen: true,
        animated: true,
      });
    },
    getSubSkillSteps() {
      const token = this.$store.state.user.token;
      console.log("skillId:", this.SubSkillId);
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/getsubskillssteps/${this.SubSkillId}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
      })
      .then(response => response.json())
      .then(data => {
        console.log("SubSubSkillSteps:", data);
        this.SubSubSkillSteps = data.sort((a, b) => a.stepNo - b.stepNo);
        console.log("SubSubSkillSteps:", data);
      })
      .catch(error => {
        console.error("Failed to fetch subskills:", error);
      });
    },
    getEquipments() {
      const token = this.$store.state.user.token;
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/equipments/${this.SubSkillId}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
      })
      .then(response => response.json())
      .then(data => {
        console.log("Equipments:", data);
        this.equipments = data;
      })
      .catch(error => {
        console.error("Failed to fetch equipments:", error);
      });
    },
    addSubSkillStep() {
      const token = this.$store.state.user.token;

      console.log("newsubSkillName:", this.newSubSkillStepName);
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/addsubskillstep/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify({
          name: this.newSubSkillStepName,
          step_no: this.SubSubSkillSteps.length + 1,
          subSkillId: this.SubSkillId,
          weight: 1,
        }),
      })
      .then(() => {
        alert("Yeni alt beceri başarıyla eklendi.");
        this.getSubSkillSteps(); // Listeyi yenile
        this.showAddSubSkillStepsInput = false;
        this.newSubSkillStepName = "";
      })
      .catch(error => {
        alert("Yeni alt beceri eklenirken bir hata oluştu.");
        console.error("Alt beceri eklenirken hata:", error);
      });
    },
    addEquipment() {
      const token = this.$store.state.user.token;

      console.log("newEquipmentName:", this.newEquipmentName);
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/addequipment/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify({
          name: this.newEquipmentName,
          subskillId: this.SubSkillId,
          // Gerekirse diğer özellikleri de buraya ekleyebilirsiniz
        }),
      })
      .then(() => {
        alert("Yeni malzeme başarıyla eklendi.");
        this.getEquipments(); // Malzemeleri yenile
        this.showAddEquipmentInput = false;
        this.newEquipmentName = "";
      })
      .catch(error => {
        alert("Yeni malzeme eklenirken bir hata oluştu.");
        console.error("Malzeme eklenirken hata:", error);
      });
    },
    showStepsList() {
      this.showSteps = true;
      this.showMaterials = false;
      this.getSubSkillSteps(); // Steps listesini güncelle
    },
    showMaterialsList() {
      this.showSteps = false;
      this.showMaterials = true;
      this.getEquipments(); // Malzemeler listesini güncelle
    },
    closeModal() {
      this.showAddSubSkillStepsInput = false;
      this.showAddEquipmentInput = false;
    },
  },
};
</script>

<style scoped>
.page {
  background-image: url('~/assets/backgroundd.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
  color: white;
}

.tepe {

  display: flex;
  justify-content: center;
}

.tepe button {
  display: flex;
}

.horizontal-divider {
  
  height: 2;
  background-color: #ffffff;
  width: 100%;
}

.action-bar {
  background-color: #19191b;
  color: #fff;
}

.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}

.list-container {
  margin: 0;
  padding: 0;
}

.list-item {
  border-bottom: 1px solid #b42929;
  padding: 10px 0;
}

.ns-light .skill-name {
  font-size: 13%;
  text-wrap: true;
  display: block;
  width: 100%;
  font-weight: bold;
  padding: 10px 20px;
  margin: 0;
  color: #000;
}

.skill-name {
  font-size: 13%;
  text-wrap: true;
  display: block;
  width: 100%;
  font-weight: bold;
  padding: 10px 20px;
  margin: 0;
  color: #fff;
}

.selected {
  background-color: #b42929;
  color: #fff;
}
</style>
