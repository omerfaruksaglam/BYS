<template>
    <Page>
      <ActionBar title="Beceri İşlemleri" />
      <StackLayout>
        <TextField class="skill" v-model="skill.namee" />
        <Button text="Beceri Düzenle" @tap="updateSkill()" />
        <Button text="Beceri Sil" @tap="confirmDeleteSkill(skill)" />
        <Button text="Kapat" @tap="goBack()" />
      </StackLayout>
    </Page>
  </template>
  
  <script>
  export default {
    
    props: ['skill'],
    methods: {
      confirmDeleteSkill(skill) {
        /*
        Bunu koyunca bozulma oluyor modalın içinde her işlem çift yapıldığı için.. 
      const confirmed = confirm('Bu beceriyi silmek istediğinizden emin misiniz?');
      if (confirmed) {}
        */
        this.deleteSkill(this.skill.skillId);
        console.log("skill  " + skill.skillId);
        this.$modal.close();
    },
    async deleteSkill(skillId) {
      const token = this.$store.state.user.token;
      await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/deleteskill/${skillId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
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
        alert('Beceri başarıyla silindi.');
        this.$emit('skillDeleted', skillId);
        this.goBack();

      })
      .catch(error => {
        alert('Beceri silinirken bir hata oluştu. Lütfen tekrar deneyin.');
        console.error('Beceri silinirken hata oluştu:', error);
      });
    },
    updateSkill() {
  const token = this.$store.state.user.token;
  console.log("skill id " + this.skill.skillId);
  console.log("skill name : " + this.skill.namee);
  fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/updateskill/${this.skill.skillId}-${this.$store.state.user.userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': "Bearer " + token
    },
    body: JSON.stringify({name: this.skill.namee})
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.text().then(text => text ? JSON.parse(text) : {});
  })
  .then(data => {
    console.log('Update successful', data);
    this.goBack();
    alert('Beceri başarıyla güncellendi.');
  })
  .catch((error) => {
    console.error('Error:', error);
    alert('Beceri güncellenirken bir hata oluştu. Lütfen tekrar deneyin.');
  });
},

      goBack() {
        this.$modal.close();
      },
    }
    
  }
  </script>
  
  <style>

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

.ns-dark .skill {
  color: #fff;
}




.ns-light page {
  background-image: url('~/assets/background.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
}

.ns-light action-bar {
  background-color: #dddddd;
  color: #000;
}

.ns-light .skill {
  color: #000;
}
</style>