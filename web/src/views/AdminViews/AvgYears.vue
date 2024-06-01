<template>
    <div class="page">
        <div>
            <select v-model="sid" @change="fetchAvgYears">
              <option  disabled value="0">Lütfen bir Beceri Seçiniz</option>
          <option v-for="skill in skillsList" :key="skill.skillId" :value="skill.skillId">{{ skill.namee }}</option>
          </select>
             </div>
        <div class="chart-container">
             <Line :data="data" :options="options" :key="chartKey" />
        </div>
    </div>
    
        

  </template>
  
  <script lang="ts">
  import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
  } 
  
  from 'chart.js'
  import { Line } from 'vue-chartjs'
    import axios from 'axios'
  
  ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
  )
  
  export default {
    name: 'App',
    components: {
      Line
    },
    data() {
        return {
          chartKey: 0,
            sid: 0,
            skillsList: [],
            data: {
                labels: [],
                datasets: [
                {
                label: 'Yıllara göre notların ortalaması',
                data: [],
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 2,
                fill: false
                }
            ]
            },
            options: {
            scales: {
                y: {
                beginAtZero: true
                }
            }
            }
        }
    },

    mounted() {
      this.fetchSkills();
    },

  methods: {
    async fetchSkills() {
      try {
        const bolumId = this.$store.state.user.bolumId;
      const token = this.$store.state.user.token;
      console.log('bolumId:', bolumId);
      console.log('token:', token);
      axios.get(`http://localhost:8082/nursapp/app/admin/getskills/${bolumId}`, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": "Bearer " + token
        }
      })       
        .then(response => {
            this.skillsList = response.data;
            console.log('skillsList:', this.skillsList);
        })
      } catch (error) {
        console.error(error);
      }
    },

    async fetchAvgYears() {
      try {
        const token = this.$store.state.user.token;
        console.log('selectedSkillId:', this.sid);
        await axios.get(`http://localhost:8082/nursapp/app/admin/getavgwithyears/${this.sid}`, {
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          }
        })       
          .then(response => {
            this.data.labels = [...response.data.map(item => item.academicYear)];
            this.data.datasets[0].data = [...response.data.map(item => item.averageGrade)];

            console.log('API Response:', response);
console.log('Mapped labels:', response.data.map(item => item.academicYear));
console.log('Mapped data:', response.data.map(item => item.averageGrade));

          })
        } catch (error) {
          console.error(error);
        }
        this.chartKey++;
      }
    },
  }
  </script>
  
  <style scoped>
  .chart-container {
  width: 100%;
  }
  
  .page {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  
  /* Flex child'larınızın (bu durumda chart-container) yeterli boyuta sahip olduğundan emin olun */
  .select-container {
    width: 100%; /* veya belirli bir genişlik */
    margin-bottom: 20px; /* Dropdown ile grafik arasındaki boşluğu ayarla */
  }
  </style>
  