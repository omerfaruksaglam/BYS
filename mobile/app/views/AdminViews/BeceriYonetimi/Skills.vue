<template>
  <Page @navigatedTo = "getSkills">
    <ActionBar title="Beceriler" />
    <ScrollView>
    
      <StackLayout>
        <Label v-for="skill in skills" :key="skill.skillId" textWrap="true" :text="skill.namee"
          @tap="editSubSkills(skill)" @longPress="openSkillOptions(skill)" />
          <Button text="Yeni Beceri Ekle" @tap="showAddSkillInput = true" v-if="!showAddSkillInput" />
        <StackLayout v-if="showAddSkillInput">
          <TextField v-model="newSkillName" hint="Yeni beceri ismi" />
          <Button text="Ekle" @tap="addSkill" />
          <Button text="İptal" @tap="showAddSkillInput = false" />
        </StackLayout>
      </StackLayout>
    </ScrollView>
 
    <Modal v-if="showModal">
      <StackLayout>
        <Label text="Beceri İsmini Güncelle" />
        <TextField v-model="newSkillName" hint="Yeni beceri ismi" />
        <Button text="Kaydet" @tap="updateSkillName" />
        <Button text="İptal" @tap="showModal = false" />
      </StackLayout>
    </Modal>
  </Page>
</template>

<script>
import SkillOptions from '~/views/AdminViews/BeceriYonetimi/SkillOptions.vue';
import Subskills from '~/views/AdminViews/BeceriYonetimi/Subskills.vue';
import Report from '~/views/Report/Report.vue';

export default {
  data() {
    return {
      skills: [],
      showAddSkillInput: false,
      newSkillName: '',
      showModal: false, 
      updatingSkillId: null,
      showOptionsModal: false,
      selectedSkill: null,
      navigating: false,

    };
  },
  created() {
    this.$on('skillDeleted', (skillId) => {
    this.skills = this.skills.filter(skill => skill.skillId !== skillId);
  });
  },
 
  methods: {
    goReport() {
      console.log('goReport');
      this.$navigateTo(Report);
    },

    openSkillOptions(skill) {

    this.$showModal(SkillOptions, {
      props: { skill: skill },
      fullscreen: true,
      animated: true,
    });
  },
   
    editSubSkills(skill) {
      this.$navigateTo(Subskills, {
        props: { skillName: skill.namee, skillId: skill.skillId },
      });
    },
   
    async getSkills() {
      const bolumId = this.$store.state.user.bolumId;
      const token = this.$store.state.user.token;
      console.log('bolumId:', bolumId);
      console.log('token:', token);
      await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/getskills/${bolumId}`, {
        method: 'GET',
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
      .then(response => response.json())
      .then(data => {
        this.skills = data.sort((a, b) => a.step_no - b.step_no);
        console.log('Skills:', this.skills);
      })
      .catch(error => {
        console.error('Failed to fetch skills:', error);
      });
    },
    
    addSkill() {
      const bolumId = this.$store.state.user.bolumId;
      const token = this.$store.state.user.token;
      console.log("skills length:", this.skills.length);
      fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/addskill/${this.$store.state.user.userId}`, {
        method: 'POST',
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
          namee: this.newSkillName,
          stepNo: this.skills.length + 1, 
          bolumId : bolumId
        })
      })
      .then(response => response.json())
      .then(data => {
        alert('Yeni beceri başarıyla eklendi.');
        this.skills.push(data);
        this.showAddSkillInput = false;
        this.newSkillName = '';
      })
      .catch(error => {
        let errorMessage = 'Beceri eklenirken bir hata oluştu. Lütfen tekrar deneyin.';
        alert(errorMessage);
        console.error('Beceri eklenirken hata oluştu:', error);
      });
    },
    
  }
};
</script>

<style scoped>
.ns-light page {
  background-image: url('~/assets/background.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
}



.ns-dark page {
  background-image: url('~/assets/backgroundd.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
}
.ns-dark action-bar {
  background-color: #19191b;
  color: #fff;
}

/* Etiket ve düğmeler için ortak stil ayarlamaları */
.ns-dark Label, Button {
  margin: 10 20;
  padding: 30px;
  border-radius: 22px; /* Kenar yuvarlaklığı büyüklüğünü ayarlayın */
  background-color: #08F7FE80;
  font-size: 15px;
  text-align: center;

}

/* Beceri etiketleri için özel stil */
.ns-dark .Label {
  background-color: #08F7FE80; /* Etiket arka plan rengi kodunuzu buraya ekleyin */
  color: #FFFFFF;
  box-shadow: 2px 5px 15px rgba(0, 0, 0, 0.2); /* Gölge etkisi */
}

/* Butonlar için ortak stil */
.ns-dark .Button {
  background-color: var(--buton-arka-plan-rengi); /* Buton arka plan rengi kodunuzu buraya ekleyin */
  color: #FFFFFF;
  box-shadow: 2px 5px 15px rgba(0, 0, 0, 0.2); /* Gölge etkisi */
}

/* Metin giriş alanları için stil */
.ns-dark .TextField {
  margin: 30px;
  padding: 20px;
  background-color: #ffffffad;
  border: 2px solid var(--textfield-kenarlik-rengi); /* Textfield kenarlık rengi kodunuzu buraya ekleyin */
  border-radius: 22px;
}

/* Modal içeriği için stil */
.ns-dark .Modal {
  padding: 20px;
  background-color: #FFFFFF;
  border-radius: 22px;
  box-shadow: 2px 5px 15px rgba(0, 0, 0, 0.5);
}

/* Sil butonu için özel stil */
.ns-dark .Button[text='İptal'] {
  background-color: #FF3B30; /* Sil butonu arka plan rengi */
}

/* Ekle butonu için özel stil */
.ns-dark Button[text='Ekle'] {
  background-color: #26893f; /* Ekle butonu arka plan rengi */
}










.ns-light action-bar {
  background-color: #dddddd;
  color: #000;
}

/* Etiket ve düğmeler için ortak stil ayarlamaları */
.ns-light Label, Button {
  margin: 10 20;
  padding: 30px;
  border-radius: 22px; /* Kenar yuvarlaklığı büyüklüğünü ayarlayın */
  font-size: 15px;
  text-align: center;
}

/* Beceri etiketleri için özel stil */
.ns-light Label {
  background-color: #33c5c9; /* Etiket arka plan rengi kodunuzu buraya ekleyin */
  color: #000;
  box-shadow: 2px 5px 15px rgba(0, 0, 0, 0.2); /* Gölge etkisi */
}

/* Butonlar için ortak stil */
.ns-light Button {
  background-color: var(--buton-arka-plan-rengi); /* Buton arka plan rengi kodunuzu buraya ekleyin */
  color: #000;
  box-shadow: 2px 5px 15px rgba(0, 0, 0, 0.2); /* Gölge etkisi */
}


/* Metin giriş alanları için stil */
.ns-light TextField {
  margin: 30px;
  padding: 20px;
  background-color: #fff;
  border: 2px solid var(--textfield-kenarlik-rengi); /* Textfield kenarlık rengi kodunuzu buraya ekleyin */
  border-radius: 22px;
}

/* Modal içeriği için stil */
.ns-light Modal {
  padding: 20px;
  background-color: #000;
  border-radius: 22px;
  box-shadow: 2px 5px 15px rgba(0, 0, 0, 0.5);
}

/* Sil butonu için özel stil */
.ns-light Button[text='Sil'] {
  background-color: #FF3B30; /* Sil butonu arka plan rengi */
}

/* Ekle butonu için özel stil */
.ns-light Button[text='Ekle'] {
  background-color: #26893f; /* Ekle butonu arka plan rengi */
}
</style>
