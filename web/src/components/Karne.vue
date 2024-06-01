<template>
  <div id="app">
    <div class="pdf-container">
      <iframe v-if="pdfUrl" :src="pdfUrl"></iframe>
    </div>
  </div>
</template>


<script>
import axios from 'axios';

export default {
props: ['studentId', 'bolumId'], // Komponente gelen prop'lar
data() {
  return {
    pdfUrl: '',
  };
},
mounted() {
  this.fetchPdf();
},
methods: {
  async fetchPdf() {
    const url = `http://localhost:8082/nursapp/app/report/download?studentId=${this.studentId}&bolumId=${this.bolumId}`;
    console.log(url);
    await axios({
      url: url, // URL'i dinamik olarak oluşturma
      method: 'GET',
      headers: {
        "Authorization": "Bearer " + this.$store.state.user.token
      },
    })
    .then((response) => {
      const binaryString = atob(response.data);
        const len = binaryString.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
          bytes[i] = binaryString.charCodeAt(i);
        }
        const blob = new Blob([bytes], { type: 'application/pdf' });
        this.pdfUrl = URL.createObjectURL(blob);
    })
    .catch((error) => {
      console.error('PDF yüklenirken bir hata oluştu:', error);
    });
  },
},
};
</script>
<style>
/* Ekstra margin ve padding'leri kaldır */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

/* Vue.js uygulaması için ana container */
#app {
  height: 100%;
}

/* PDF'i gösteren container */
.pdf-container {
  height: 100%; /* Tam ekran yüksekliği */
}

/* iframe stilini güncelle */
.pdf-container iframe {
  height: 100%;
  width: 100%;
  border: none; /* Çerçeveyi kaldır */
  display: block; /* Blok seviyesinde görüntüle */
}
</style>
