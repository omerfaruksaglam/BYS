<template>
    <Page>
      <ActionBar class="action-bar" :title="'Giriş Aktiviteleri'"></ActionBar>
      <ScrollView>
        <StackLayout>
          <ListView for="log in logs" class="list-group">
            <v-template>
              <FlexboxLayout flexDirection="column" class="list-group-item">

                <Label :text="'Giriş Zamanı: ' + datefunction(log.actionTime)" class="list-group-item-text"></Label>
                <Label :text="'IP Address: ' + log.ipAddress" class="list-group-item-text"></Label>
                <Label :text="'Kullanıcı Numarası: ' + log.requestedUserId" class="list-group-item-text"></Label>
                <Label :text="'Bolum Numarası: ' + log.requestedBolumId" class="list-group-item-text"></Label>
                <Label :text="'Giriş Başarılı mı?: ' + (log.isSuccess ? 'Evet' : 'Hayır')" class="list-group-item-text"></Label>
                <Label :text="'Başarısızlık Sebebi: ' + (log.failedReason === null ? '-': log.failedReason)" class="list-group-item-text"></Label>
              </FlexboxLayout>
            </v-template>
          </ListView>
        </StackLayout>
      </ScrollView>
    </Page>
  </template>
  
  <script>


  export default {
    data() {
      return {
        logs: [], // This should be the array of logs you retrieve from your backend
      };
    },
    mounted() {
      this.fetchLogs();
    },
    methods: {
      async fetchLogs() {
        const bolumId = this.$store.state.user.bolumId; // Or however you get this information
        const token = this.$store.state.user.token; // Same here
  
        try {
          const response = await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/getloginrecords/${bolumId}`, {
            method: 'GET',
            headers: {
              "Content-Type": "application/json",
              "Authorization": "Bearer " + token
            }
          });
  
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          
          this.logs = await response.json();
        } catch (error) {
          console.error('Failed to fetch logs:', error);
        }
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
    },
  };
  </script>
  <style scoped>

.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}

.ns-light .btn {
  margin: 15;
  background-color: #007bff; /* Bootstrap primary blue */
}

.ns-light .btn-primary {
  background-color: #30bced;
  color: #ffffff;
}
.ns-light .list-group-item-heading {
  font-weight: bold;
  color: #004a99;
}

.ns-light .list-group-item-text {
  padding: 10;
}
  



.ns-dark .action-bar {
  background-color: #19191b;
  color: #fff;
}

.ns-dark .btn {
  margin: 15;
  background-color: #007bff; /* Bootstrap primary blue */
}

.ns-dark .btn-primary {
  background-color: #30bced;
  color: #000;
}
.ns-dark .list-group-item-heading {
  font-weight: bold;
  color: #3c7ec4;
}

.ns-dark .list-group-item-text {
  padding: 10;
}
  /* ... diğer stilleriniz ... */
  </style>
  