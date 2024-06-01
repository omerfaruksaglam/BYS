<template>
  <div class="upload-container">
    <h1 class="upload-title">Toplu Kullanıcı Ekle</h1>
    <div class="upload-content">
      <div class="file-upload">
        <input type="file" @change="handleFileUpload" accept=".xls" class="file-input" />
        <button @click="uploadFile" class="upload-button">Yükle</button>
      </div>
      <div class="instructions">
        <img src="@/assets/ExcelTemplate.png" alt="Excel Şablonu" class="excel-image"/>
        <ul class="instructions-list">
          <li>Lütfen belirtilen formatta excel tablonuzu hazırladığınızdan emin olun.</li>
          <li>Dosyanızın '.xls' formatında olduğundan emin olun.</li>
          <li>Kayıt okuma 2. satırdan başlayacaktır.</li>
          <li>*Şifre en az 1 harf içermelidir.</li>
          <li>Rol Tablosu</li>
          <li>0 : Öğretim Elemanı</li>
          <li>1 : Öğrenci</li>
          <li>2 : Hemşire</li>
          <li>3 : Yönetici</li>
        </ul>
      </div>
    </div>
  </div>
</template>


  
<script>
import axios from 'axios';
import { useRouter } from 'vue-router'; // Vue Router'ı import edin

export default {
    data() {
        return {
            selectedFile: null,
            isComponentAlive: true,
        };
    },
    setup() {
        const router = useRouter(); // Vue Router'ı kullanmak için setup fonksiyonunu kullanın

        return {
            router, // router'ı döndürün, böylece methods içinde kullanılabilir
        };
    },
    methods: {
        handleFileUpload(event) {
            this.selectedFile = event.target.files[0];
        },
        async uploadFile() {
            if (!this.selectedFile) {
                console.log('Dosya seçilmedi.');
                return;
            }

        try {
                const formData = new FormData();
                formData.append('file', this.selectedFile);
                console.log('uploading', this.selectedFile);
                const url = `http://localhost:8082/nursapp/app/admin/excel-upload/${this.$store.state.user.userId}`;
                const response = await axios.post(
        url,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${this.$store.state.user.token}`,
          },
        }
      );
      // Check if the response indicates an error
      if (response.data.error) {
        alert('Hata: ' + response.data.message);
        return;
      }

      console.log('File uploaded successfully', response.data);

      // İşlem başarılı olduğunda, bir önceki sayfaya dönün ve bir alert gösterin
      this.router.back();
      alert('Excelden toplu ekleme işlemi başarılı');
    } catch (error) {
      if (!this.isComponentAlive) return;

      // Check if the error response from the server is available
      if (error.response) {
        alert('ERROR: ' + error.response.data.message);
      } else {
        console.error('Error uploading file:', error);
        alert('ERROR');
      }
    }
  },
    },
    unmounted() {
        this.isComponentAlive = false;
    },
};
</script>
  

<style scoped>
.upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin: 3%;
  margin-top: 5%;
}

.upload-title {
  margin-bottom: 30px;
  color: #0056b3;
  font-weight: 600;
}

.upload-content {
  width: 100%;
}

.file-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.file-input {
  margin-bottom: 20px;
  padding: 15px;
  border: 2px dashed #ddd;
  border-radius: 5px;
  cursor: pointer;
}

.upload-button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #0056b3;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.upload-button:hover {
  background-color: #003d80;
}

.instructions {
  text-align: center;
}

.instructions-list {
  list-style: none;
  padding: 0;
  margin-top: 20px;
  text-align: left;
  font-size: 16px;
}

.instructions-list li {
  margin-bottom: 10px;
}

.excel-image {
  max-width: 100%;
  height: auto;
  margin-bottom: 20px;
}

</style>