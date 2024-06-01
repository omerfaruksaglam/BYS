<template>
    <Page>
      <ActionBar title="Ekipman İşlemleri" />
      <StackLayout>
        <TextField class="equipment" v-model="equipment.name" />
        <Button text="Ekipmanı Güncelle" @tap="updateEquipment()" />
        <Button text="Ekipmanı Sil" @tap="confirmDeleteEquipment(equipment)" />
        <Button text="Kapat" @tap="goBack()" />
      </StackLayout>
    </Page>
  </template>
  
  <script>
export default {
  props: ['equipment'],
  methods: {
    confirmDeleteEquipment(equipment) {
      // Silme işlemini onaylamak için bir modal veya uyarı kutusu kullanabilirsiniz.
      this.deleteEquipment(equipment.equipmentId);
    },
    async deleteEquipment(equipmentId) {
      const token = this.$store.state.user.token;
      await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/deleteequipment/${equipmentId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        method: 'DELETE',
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
      .then(response => {
        alert('Ekipman başarıyla silindi.');
        this.goBack();
      })
      .catch(error => {
        alert('Ekipman silinirken bir hata oluştu. Lütfen tekrar deneyin.');
        console.error('Ekipman silinirken hata oluştu:', error);
      });
    },
    async updateEquipment() {
      const token = this.$store.state.user.token;
      await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/updateequipment/${this.equipment.equipmentId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': "Bearer " + token
        },
        body: JSON.stringify({ name: this.equipment.name })
      })
      .then(response => {
        return response.text().then(text => {
          const data = text ? JSON.parse(text) : {};
          console.log('Update successful', data);
          alert('Ekipman başarıyla güncellendi.');
        });
      })
      .catch((error) => {
        console.error('Error:', error);
        alert('Ekipman güncellenirken bir hata oluştu. Lütfen tekrar deneyin.');
      });
    },

    goBack() {
      this.$modal.close();
    },
  }
}
</script>

  
  .action-bar {
    background-color: #19191b;
    color: #fff;
  }
  
  .ns-light .equipment {
    color: #000;
  }

  .ns-dark .equipment {
    color: #fff;
  }
  </style>
  