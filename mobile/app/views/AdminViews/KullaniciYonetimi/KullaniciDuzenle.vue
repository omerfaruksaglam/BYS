<template>
    <Page class="page">
      <ActionBar title="Kullanıcı Güncelle" class="action-bar" />
      <ScrollView>
        <StackLayout class="form">
          <TextField v-model="selectedUser.name" hint="Ad" class="input" />
          <TextField v-model="selectedUser.surname" hint="Soyad" class="input" />
          <StackLayout orientation="horizontal" alignItems="center" class="switch-container">
            <Label class="label" text="Aktiflik Durumu" />
            <Switch v-model="selectedUser.active" class="switch" />
          </StackLayout>
          <Button text="Güncelle" @tap="updateUser" class="btn" />
          <button text="Şifre Değiştir" @tap="changePassword" class="btn-cp" />
          <Button text="Kullanıcıyı Sil" @tap="deleteUser" class="btn delete" />

        </StackLayout>
      </ScrollView>
    </Page>
  </template>
  
  <script>
  import { Dialogs } from "@nativescript/core";
  
  export default {
    props: ["user"],
    data() {
      return {
        selectedUser: this.user ? {...this.user} : {
          userId: "",
          bolumId: "",
          name: "",
          surname: "",
          isActive: false,
          password: "",
        },
      };
    },
    methods: {
      async updateUser() {
        try {
          await this.toggleActive();
          await this.updateUserDetails();
          this.$navigateBack();
          Dialogs.alert("Kullanıcı başarıyla güncellendi.");
          
        } catch (error) {
          Dialogs.alert(`Güncelleme işlemi sırasında bir hata oluştu: ${error.message}`);
        }
      },
      async toggleActive() {
        const isActiveUrl = `http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/isactive/${this.selectedUser.bolumId}-${this.selectedUser.userId}-${this.$store.state.user.userId}?isActive=${this.selectedUser.active}`;
        const response = await fetch(isActiveUrl, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + this.$store.state.user.token,
          },
          body: JSON.stringify({ isActive: this.selectedUser.isActive }),
        });
  
        if (!response.ok) {
            console.log(response);
          const errorText = await response.text();
          throw new Error(errorText || 'Aktiflik durumu güncellenemedi');
        }
      },
    async deleteUser() {
  const confirmed = await Dialogs.confirm({
    title: "Kullanıcı Sil",
    message: "Kullanıcı silinecek. Onaylıyor musunuz?",
    okButtonText: "Evet",
    cancelButtonText: "Hayır",
  });

  if (confirmed) {
    try {
      const response = await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/deleteuser/${this.selectedUser.bolumId}-${this.selectedUser.userId}-${this.$store.state.user.userId}`, {
        method: 'DELETE',
        headers: {
          "Content-Type": "application/json",  
          'Authorization': "Bearer " + this.$store.state.user.token,
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to delete user with ID ${this.selectedUser.userId}`);
      }

      await Dialogs.alert('Kullanıcı başarıyla silindi');
      this.$navigateBack();
    } catch (error) {
      console.error(error);
      await Dialogs.alert('Kullanıcı silinirken bir hata oluştu');
    }
  }
},

      async updateUserDetails() {
        const userDetailsUrl = `http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/updateuser/${this.selectedUser.bolumId}-${this.selectedUser.userId}-${this.$store.state.user.userId}`;
        const response = await fetch(userDetailsUrl, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + this.$store.state.user.token,
          },
          body: JSON.stringify({
            name: this.selectedUser.name,
            surname: this.selectedUser.surname,
          }),
        });
  
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || 'Kullanıcı detayları güncellenemedi');
        }
      },
      async changePassword() {
  // İlk şifre girişi
  const newPassword = await Dialogs.prompt({
    title: "Yeni Şifre",
    message: "Yeni şifreyi giriniz:",
    okButtonText: "Tamam",
    cancelButtonText: "İptal",
    //inputType: Dialogs.inputType.password,
  });

  if (!newPassword.result) {
    return; // Kullanıcı iptal ettiyse işlemi durdur
  }

  // İkinci şifre girişi (teyit için)
  const confirmPassword = await Dialogs.prompt({
    title: "Şifreyi Onayla",
    message: "Yeni şifreyi tekrar giriniz:",
    okButtonText: "Tamam",
    cancelButtonText: "İptal",
    //inputType: Dialogs.inputType.password,
  });

  if (!confirmPassword.result) {
    return; // Kullanıcı iptal ettiyse işlemi durdur
  }

  // İki girişin eşleşip eşleşmediğini kontrol et
  if (newPassword.text !== confirmPassword.text) {
    await Dialogs.alert("Girilen şifreler eşleşmiyor. Lütfen tekrar deneyiniz.");
    return;
  }
  console.log("pass" + newPassword.text);

  // Şifreleri backend'e gönder
  try {
    const response = await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/changepassword`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + this.$store.state.user.token,
      },
      body: JSON.stringify({
        userId: this.selectedUser.userId,
        bolumId: this.selectedUser.bolumId,
        password: newPassword.text, // Kullanıcıdan alınan yeni şifre
        changerId: this.$store.state.user.userId,
      }),
    });

    if (!response.ok) {
      throw new Error('Şifre değiştirme işlemi başarısız.');
    }

    await Dialogs.alert("Şifreniz başarıyla değiştirildi.");
    this.$navigateBack(); // İşlem başarılı, bir önceki sayfaya dön
  } catch (error) {
    console.error('Şifre değiştirme işlemi sırasında bir hata oluştu', error);
    await Dialogs.alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz.");
  }
}

    },
    
  };
  </script>

  
<style scoped>
* {
  color : black;
}

.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}
.ns-light .form {
  margin: 20;
}
.ns-light .input {
  margin: 10;
}
.ns-light .switch-container {
  margin: 10;
  justify-content: space-between;
}
.ns-light .switch {
  margin-left: 10;
  background-color: #33c5c9;
}
.ns-light .btn {
  margin-top: 10em;
    background-color: #33c5c9;
    color: #000;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;
}
.ns-light .btn.delete {
  background-color: #D81F1F;
  color: #000;
  margin-top: 10em;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;
}
.ns-light .btn-cp {
  margin: 10;
  background-color: #aa5050;
  color: #000;
  margin-top: 10em;
    color: #000;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;
}





.ns-dark .action-bar {
  background-color: #19191b;
  color: #fff;
}
.ns-dark .form {
  margin: 20;
}
.ns-dark .input {
  margin: 10;
}
.ns-dark .label {
  color: #ffffff;
}
.ns-dark .switch-container {
  margin: 10;
  justify-content: space-between;
}
.ns-dark .switch {
  margin-left: 10;
  background-color: #08F7FE80;
}
.ns-dark .btn {
  margin-top: 10em;
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;
}
.ns-dark .btn.delete {
  background-color: #D81F1F;
  color: #ffffff;
  margin-top: 10em;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;
}
.ns-dark .btn-cp {
  margin: 10;
  background-color: #aa5050;
  color: #ffffff;
  margin-top: 10em;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;
}
</style>
