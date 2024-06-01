<template>
    <Page>
      <ActionBar title="Alt Beceri basamakları İşlemleri" />
      <StackLayout>
        <TextField class="skill" v-model="subskillsteps.name" />
        <TextField class="skill" v-model="subskillsteps.weight" />
        <FlexboxLayout flexDirection="row" alignItems="center">
  <Label text="Başlık" class="switch-label" />
  <Switch v-model="subskillsteps.head" class="switch" />
</FlexboxLayout>
<Button text="Alt Beceri Düzenle"  @tap="updateSubskillStep(subskillsteps.subskillStepsId, subskillsteps.name, subskillsteps.weight, $store.state.user.userId, $store.state.user.bolumId)" :class="{ 'disabled-button': subskillsteps.weight < 1 }"/>
        <Button text="Alt Beceri Sil" @tap="confirmDeleteSubskillStep(subskillsteps)" />
        <Button text="Kapat" @tap="goBack()" />
      </StackLayout>
    </Page>
  </template>
  <script>
  export default {
    props: ['subskillsteps'],
    data() {
      return {
        step :" ",
        }
      },
      watch: {
    // subskillsteps.head değişkenini izle
    'subskillsteps.head'(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.updateHeader(this.subskillsteps); // Değişiklik olduğunda updateHeader fonksiyonunu çağır
      }
    }
  },
    methods: {
      confirmDeleteSubskillStep(subskillsteps) {
        this.deleteSubskill(subskillsteps.subskillStepsId);
        console.log("subskillsteps  " + subskillsteps.subskillStepsId);
      },
      deleteSubskill(subskillStepsId) {
        const token = this.$store.state.user.token;
        fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/deletesubskillstep/${subskillStepsId}-${this.$store.state.user.userId}-${this.$store.state.user.bolumId}`, {
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
          alert('Alt Beceri Basamağı başarıyla silindi.');
          this.goBack();
        })
        .catch(error => {
          alert('Alt Beceri basamağı silinirken bir hata oluştu. Lütfen tekrar deneyin.');
          console.error('Alt Beceri silinirken hata oluştu:', error);
        });
      },
      async updateSubskillStep(subskillStepId, name, weight, adminId, adminBolumId) {
        if (weight < 1) {
      alert('Lütfen ağırlık değerini 1 veya daha büyük bir sayı giriniz.');
      return;
    }
  const token = this.$store.state.user.token; // Doğru token alındığından emin olun
  try {
    // URL güncellendi: ağırlık değeri ve diğer parametreler dahil edildi
    const url = `http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/updatesubskillstep/${subskillStepId}/${weight}-${adminId}-${adminBolumId}`;
    const body = JSON.stringify({ name: name });
    const headers = {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };

    const response = await fetch(url, {
      method: 'PUT',
      headers: headers,
      body: body
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    alert('Alt Beceri Basamağı başarıyla güncellendi.');
  } catch (error) {
    console.error('Alt Beceri Basamağı güncellenirken hata oluştu:', error);
    alert('Alt Beceri Basamağı güncellenirken bir hata oluştu.');
  }
},
updateHeader(step) {
  const token = this.$store.state.user.token; // Token bilgisini al
  const stepid = step.subskillStepsId; // Adım ID'sini al
  console.log('stepid:', stepid);

  // Fetch API kullanarak başlık durumunu güncelle
  fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/${stepid}/update-header-status?head=${step.head}`, {
    method: 'PUT', // HTTP metodu
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}` // Token ile yetkilendirme
    },
    body: JSON.stringify({ head: step.head }) // Gönderilecek veri
  })
  .then(response => response.json()) // Yanıtı JSON olarak dönüştür
  .then(data => {
    console.log('Başlık durumu güncellendi:', data); // Güncelleme başarılı
  })
  .catch(error => {
    console.log("step head " + step.head);
    console.error('Başlık durumu güncellenirken hata oluştu:', error); // Hata durumunda log
  });
},

      goBack() {
        this.$modal.close();
      },
    }
  }
</script>

  <style>
  .disabled-button {
  background-color: grey;
  color: #fff;
  cursor: not-allowed;
}
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
  .switch-label {
  margin: 20px; /* Switch'ten sağa doğru biraz boşluk ekler */
  color: #fff; /* Yazı rengi */
  /* Diğer stiller */
}




  
  .ns-light  action-bar {
    background-color: #19191b;
    color: #000;
  }
  
  .ns-light .skill {
    color: #000;
  }
  .ns-light .switch-label {
  margin: 20px; /* Switch'ten sağa doğru biraz boşluk ekler */
  color: #000; /* Yazı rengi */
  /* Diğer stiller */
}
  </style>
  