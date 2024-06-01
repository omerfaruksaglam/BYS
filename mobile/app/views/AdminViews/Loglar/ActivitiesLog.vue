<template>
  <Page>
    <ActionBar class="action-bar" :title="'Log Kayıtları'"></ActionBar>
    <ScrollView>
      <StackLayout>
        <ListView v-if="logs.length > 0" for="log in logs" class="list-group">
          <v-template>
            <FlexboxLayout flexDirection="column" class="list-group-item">

              <Label :text="'Aksiyon Zamanı: ' + datefunction(log.actionTime)" class="list-group-item-text"></Label>
              <Label :text="'IP Address: ' + log.ipAddress" class="list-group-item-text"></Label>
              <Label :text="'Kullanıcı Numarası: ' + log.userid" class="list-group-item-text"></Label>
              <Label :text="'Bolum Numarası: ' + log.bolumId" class="list-group-item-text"></Label>
              <Label :text="'Açıklama: ' + log.actionText" class="list-group-item-text" @tap="showFullText(log.actionText)"></Label>
            </FlexboxLayout>
          </v-template>
        </ListView>
        <Label v-else text="Kayıtlar yükleniyor..." class="loading-message"></Label>
      </StackLayout>
    </ScrollView>
  </Page>
</template>

<script>
import { knownFolders, File, path as Path, Dialogs } from '@nativescript/core';

export default {
  data() {
    return {
      logs: []
    };
  },
  mounted() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      const bolumId = this.$store.state.user.bolumId;
      const token = this.$store.state.user.token;
      try {
        const response = await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/getactionrecords/${bolumId}`, {
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
      const newDate = new Date(date);
      return `${newDate.getDate().toString().padStart(2, '0')}-${(newDate.getMonth() + 1).toString().padStart(2, '0')}-${newDate.getFullYear()} ${newDate.getHours().toString().padStart(2, '0')}:${newDate.getMinutes().toString().padStart(2, '0')}`;
    },
    showFullText(text) {
      Dialogs.alert({
        title: "Açıklama",
        message: text,
        okButtonText: "Tamam"
      });
    }
  }
};
</script>

<style scoped>

.ns-dark .action-bar {
  background-color: #19191b;
  color: #fff;
}

.ns-light .action-bar {
  background-color: #dddddd;
  color: #000;
}

.btn {
  margin: 15;
  background-color: #007bff;
}

.btn-primary {
  background-color: #30bced;
  color: #ffffff;
}

.list-group-item-heading {
  font-weight: bold;
  color: #3c7ec4;
}

.list-group-item-text {
  padding: 10;
}

.loading-message {
  padding: 10;
  color: #777;
  text-align: center;
}
</style>
