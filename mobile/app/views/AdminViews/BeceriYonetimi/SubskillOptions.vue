<template>
    <Page>
      <ActionBar title="Alt Beceri İşlemleri" />
      <StackLayout>
        <TextField class="skill" v-model="subskill.name" />
        <Button text="Alt Beceri Düzenle" @tap="updateSubskill()" />
        <Button text="Alt Beceri Sil" @tap="confirmDeleteSubskill(subskill)" />
        <Button text="Kapat" @tap="goBack()" />
      </StackLayout>
    </Page>
  </template>
  
  <script>
  export default {
    props: ['subskill'],
    methods: {
      confirmDeleteSubskill(subskill) {
        // Confirm kullanmak isterseniz, Vue NativeScript için bir modal/popup çözümü bulunmalı.
        this.deleteSubskill(this.subskill.subSkillId);
        console.log("subskill  " + subskill.subSkillId);
      },
      deleteSubskill(subSkillId) {
        const token = this.$store.state.user.token;
        fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/deletesubskill/${subSkillId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
          method: 'DELETE',
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          }
        })
        .then(response => {
          if (!response.ok) {
            throw new Error('Response not OK');
          }
          alert('Alt Beceri başarıyla silindi.');
          this.goBack();
        })
        .catch(error => {
          alert('Alt Beceri silinirken bir hata oluştu. Lütfen tekrar deneyin.');
          console.error('Alt Beceri silinirken hata oluştu:', error);
        });
      },
      updateSubskill() {
        const token = this.$store.state.user.token;
        console.log("subskill id " + this.subskill.subSkillId);
        console.log("subskill name : " + this.subskill.name);
        fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/updatesubskill/${this.subskill.subSkillId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': "Bearer " + token
          },
          body: JSON.stringify({name: this.subskill.name})
        })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.text().then(text => text ? JSON.parse(text) : {});
        })
        .then(data => {
          console.log('Update successful', data);
          alert('Alt Beceri başarıyla güncellendi.');
        })
        .catch((error) => {
          console.error('Error:', error);
          alert('Alt Beceri güncellenirken bir hata oluştu. Lütfen tekrar deneyin.');
        });
      },
  
      goBack() {
        this.$modal.close();
      },
    }
  }
  </script>
  
  <style>
  page {
    background-image: url('~/assets/backgroundd.jpeg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    display: flex;
  }
  
  action-bar {
    background-color: #19191b;
    color: #fff;
  }
  
  .skill {
    color: #fff;
  }
  </style>
  