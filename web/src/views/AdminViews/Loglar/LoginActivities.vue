<template>
    <div>
        <button class="button" @click="exportLogs">Excel ile dışa aktar</button>
        <table class="full-width-table">
            <thead>
                <tr>

                    <th>Giriş Zamanı</th>
                    <th>IP Address</th>
                    <th>Kullanıcı Numarası</th>
                    <th>Bolum Numarası</th>
                    <th>Giriş Başarılı mı?</th>
                    <th>Başarısızlık Sebebi</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="log in log" :key="log.logId">

                    <td>{{ datefunction(log.actionTime) }}</td>
                    <td>{{ log.ipAddress }}</td>
                    <td>{{ log.requestedUserId }}</td>
                    <td>{{ log.requestedBolumId }}</td>
                    <td>{{ log.isSuccess ? "evet" : "hayır" }}</td>
                    <td>{{ log.failedReason }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
            log: [], // Array to store the logs retrieved from the endpoint
        };
    },
    mounted() {
        // Call the endpoint to fetch the logs and update the logs array
        this.fetchLogs();
    },
    methods: {
        fetchLogs() {
            const bolumId = this.$store.state.user.bolumId;
            const token = this.$store.state.user.token;
      console.log('bolumId:', bolumId);
      console.log('token:', token);
      axios.get(`http://localhost:8082/nursapp/app/admin/getloginrecords/${bolumId}`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })
        .then(response => {
          this.log = response.data;
          console.log('logs:', this.log);
        })
        .catch(error => {
          console.error('Failed to fetch skills:', error);
        });
    },
    datefunction(date) {
        var localDateTimeString = new Date(date).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            return day + '-' + month + '-' + year + ' ' + hours + ':' + minutes;
    },
    
    exportLogs() {
    const bolumId = this.$store.state.user.bolumId;
    const token = this.$store.state.user.token;
        console.log('bolumId:', bolumId);
        console.log('token:', token);
    axios({
        url: `http://localhost:8082/nursapp/app/admin/exportloginrecords/${bolumId}`,
        method: 'GET',
        responseType: 'blob',
        headers: {
            "Authorization": "Bearer " + token
        }
    }).then(response => {
        const fileURL = window.URL.createObjectURL(new Blob([response.data]));
        const fileLink = document.createElement('a');
        fileLink.href = fileURL;
        fileLink.setAttribute('download', 'login_records.xlsx');
        document.body.appendChild(fileLink);
        fileLink.click();
        document.body.removeChild(fileLink);
    }).catch(error => {
        console.error('Failed to download excel:', error);
        console.log('Request Headers:', error.config.headers);
        console.log('Status Code:', error.response ? error.response.status : 'No response status');
        console.log('Response:', error.response);
    });
}

    }
};
</script>
<style scoped>
.full-width-table {
  width: 100%;
  border-collapse: collapse;
}

.full-width-table th, .full-width-table td {
  padding: 0.5rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.full-width-table thead th {
  background-color: #004a99;
  color: white;
  position: sticky;
  top: 0;
  z-index: 10;
}

.full-width-table tbody tr:nth-child(even) {
  background-color: #f2f2f2;
}

.full-width-table tbody tr:hover {
  background-color: #ddd;
}

.button {
  background-color: #007bff;
  border: 1px solid #007bff;
  border-radius: 0.25rem;
  color: white;
  padding: 0.375rem 0.75rem;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1rem;
  transition: background-color 0.15s ease-in-out;
  margin: 1rem;
  cursor: pointer;
}

.button:hover, .button:focus {
  background-color: #0056b3;
  border-color: #0056b3;
  outline: none;
}

/* Additional styling for specific columns if needed */
.full-width-table .success-column {
  text-align: center;
}

.full-width-table .failure-reason-column {
  max-width: 250px; /* To ensure the content does not stretch too far horizontally */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis; /* Adds '...' at the end if the content is too long */
}

/* Tooltip for showing the full text on hover for failure reasons */
.full-width-table .failure-reason-column:hover {
  overflow: visible;
  white-space: normal;
  height: auto; /* Adjust height to fit the content */
  background-color: #f8f9fa; /* Light background color */
}

/* Scrollbar styles */
.full-width-table::-webkit-scrollbar {
  height: 0.75rem;
}

.full-width-table::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.full-width-table::-webkit-scrollbar-thumb {
  background: #888;
}

.full-width-table::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
