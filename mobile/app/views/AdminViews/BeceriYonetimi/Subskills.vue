<template>
  <Page @navigatedTo="getSubSkills">
    <ActionBar :title="skillName" class="action-bar" />
    <ScrollView>
      <StackLayout>
        <!-- Her bir subskill için bir StackLayout kapsayıcı kullanın -->
        <StackLayout v-for="subskill in subSkills" :key="subskill.subSkillId" class="list-item">
          <Label class="skill-name" :text="subskill.name"
                 @tap="editSubSkillSteps(subskill)"
                 @longPress="SubskillOptions(subskill)" />
          <StackLayout class="horizontal-divider" />
          
        </StackLayout>
        <Button text="Yeni Alt Beceri Ekle" @tap="showAddSubSkillInput = true" v-if="!showAddSubSkillInput" />
        <StackLayout v-if="showAddSubSkillInput">
          <TextField v-model="newSubSkillName" hint="Yeni beceri ismi" />
          <Button text="Ekle" @tap="addSubSkill" />
          <Button text="İptal" @tap="closeModal" />
        </StackLayout>
      </StackLayout>
    </ScrollView>

    <!-- Alt beceri eklemek için modal -->
    
  </Page>
</template>

<script>
import SubSkillSteps from './SubSkillSteps.vue';
import SubskillOptions from '~/views/AdminViews/BeceriYonetimi/SubskillOptions.vue';

export default {
  props: ["skillId", "skillName"],
  data() {
    return {
      subSkills: [],
      showAddSubSkillInput: false, // Yeni alt beceri eklemek için modalı göstermek için kullanılacak
      newSubSkillName: '', // Yeni alt beceri ismi
    };
  },
  methods: {
    SubskillOptions(subskill) {
      console.log("subskill:", subskill);
      this.$showModal(SubskillOptions, {
        props: {
          subskill: subskill
        },
        fullscreen: true,
        animated: true,
      });
    },
    editSubSkillSteps(subskill) {
      
      this.$navigateTo(SubSkillSteps, {
        props: {
          SubSkillName: subskill.name,
          SubSkillId: subskill.subSkillId,
        },
        fullscreen: true,
        animated: true,
      });
    },
    getSubSkills() {
      const token = this.$store.state.user.token;
      console.log("skillId:", this.skillId);
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/getsubskills/${this.skillId}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
      })
      .then(response => response.json())
      .then(data => {
        this.subSkills = data.sort((a, b) => a.stepNo - b.stepNo);
        console.log("subSkills:", data);
      })
      .catch(error => {
        console.error("Failed to fetch subskills:", error);
      });
    },
    addSubSkill() {
      const token = this.$store.state.user.token;
      console.log("skillId:", this.skillId);
      console.log("newsubSkillName:", this.newSubSkillName);
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/addsubskill/${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify({
          name: this.newSubSkillName,
          stepNo: this.subSkills.length + 1,
          skillId: this.skillId,
          weight: 1,
        }),
      })
      .then(() => {
        alert("Yeni alt beceri başarıyla eklendi.");
        this.getSubSkills(); // Listeyi yenile
        this.showAddSubSkillInput = false;
        this.newSubSkillName = "";
      })
      .catch(error => {
        alert("Yeni alt beceri eklenirken bir hata oluştu.");
        console.error("Alt beceri eklenirken hata:", error);
      });
    },
    openAddNewSubSkill() {
      this.newSubSkillName = ""; // Yeni alt beceri ismi girişini temizleyin
      this.showAddSubSkillInput = true; // Ekleme alanını gösterin
    },
    closeModal() {
      // Modalı kapatmak için showAddSubSkillInput değişkenini false yapın
      this.showAddSubSkillInput = false;
    },
  },
};
</script>

<style scoped>

.ns-dark .horizontal-divider {
  height: 2;
  background-color: #ffffff;
  width: 100%;
}

.ns-dark .action-bar {
  background-color: #19191b;
  color: #fff;
}

.ns-dark .list-container {
  margin: 0;
  padding: 0;
}

.ns-dark .list-item {
  border-bottom: 1px solid #b42929; /* Her bir liste elemanının altına çizgi ekler */
  padding: 10px 0; /* İç boşluk sağlar */
}

.ns-dark .skill-name {
  font-size: 13%;
  text-wrap: true;
  display: block;
  width: 100%; /* Tam genişlik */
  font-weight: bold;
  padding: 10px 20px; /* Üst ve alttan boşluk, yanlardan 20px */
  margin: 0; /* Dış boşluk yok */
  color: #fff; /* Metin rengi beyaz */
}

.ns-dark .Button {
  background-color: #b42929;
  color: #b42929;
}




.ns-light .horizontal-divider {
  height: 2;
  background-color: #000;
  width: 100%;
}

.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}

.ns-light .list-container {
  margin: 0;
  padding: 0;
}

.ns-light .list-item {
  border-bottom: 1px solid #b42929; /* Her bir liste elemanının altına çizgi ekler */
  padding: 10px 0; /* İç boşluk sağlar */
}

.ns-light .skill-name {
  font-size: 13%;
  text-wrap: true;
  display: block;
  width: 100%; /* Tam genişlik */
  font-weight: bold;
  padding: 10px 20px; /* Üst ve alttan boşluk, yanlardan 20px */
  margin: 0; /* Dış boşluk yok */
  color: #000; /* Metin rengi beyaz */
}
</style>
