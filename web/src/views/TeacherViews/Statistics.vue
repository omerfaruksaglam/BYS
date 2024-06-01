<template>
    <div class="page">
        <div class="header">
            <h1> Grafiksel Gösterimler </h1>
        </div>
        <div class="headerrr">
            <label :style="{ margin: '0 5 0 0' }"> Sınıf Seç: <br></label>
            <select v-model="sinif" @change="verileriAyarla">
                <option v-for="(item, index) in bolumInfo.yilSayisi" :key="index + 1" :value="index + 1">{{ index + 1 }}
                </option>
            </select>
        </div>
        <div class="headerr">
            <label> {{ sinif }}. Sınıf</label>
        </div>

        <div class="chart-container" v-for="index in  Math.ceil(bolumInfo.skillCount / 2) " :key="index">
            <div class="chart-row">
                <div class="chart-item" v-if="skills" v-for="n in  2 " :key="`chart-${index}-${n}`">
                    <div v-if="n === 2 ? index * n <= bolumInfo.skillCount ? true : false : true" class="asdf">
                        <Label class="label">{{ skills[(index - 1) * 2 + n - 1].namee }}</Label>

                        <Pie :key="`${(index - 1) * 2 + n - 1}`"
                            :data="dataa[(index - 1) * 2 + n - 1] ? dataa[(index - 1) * 2 + n - 1] : dataa[0]"
                            :options="optionss" />
                    </div>
                </div>
            </div>
        </div>


    </div>
</template>

<script>
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Pie } from 'vue-chartjs'
import cryptoJs from 'crypto-js';


ChartJS.register(ArcElement, Tooltip, Legend)
export default {
    created() {
        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.student = this.decryptData(this.$route.query.student);
        console.log(this.bolumInfo);
        this.statisticleriGetir();
    },
    components: {
        Pie
    },
    data() {
        return {
            key: 'amutrloisdhjtruymzapoewqmjfhthew',
            bolumInfo: null,
            sinif: 1,
            statistics: null,
            skills: null,
            student: null,
            grader: null,
            graderNotVeriyormu: null,
            renk: {
                0: '#ff0000',//kirmizi
                1: '#008000',//yesil
                2: '#30D5C8',//turkuaz
                3: '#0000ff',//mavi
                4: '#660099',//mor
            },
            dataa: [
                {
                    labels: ['Başarı Oranı', 'Başarısızlık Oranı'],
                    datasets: [
                        {
                            backgroundColor: ['red', 'grey'],
                            data: [1, 0],
                            skillIndex: 0,
                        },
                    ],
                }
            ],

            optionss: {
                responsive: true,
                maintainAspectRatio: false,
                onClick: async (event, activeElements, chart) => {
                    // 'activeElements' tıklanan elemanları içerir.
                    if (activeElements.length > 0) {
                        const firstPoint = activeElements[0];
                        const datasetIndex = firstPoint.datasetIndex;
                        const index = firstPoint.index;
                        const value = chart.data.datasets[datasetIndex].data[index];

                        const dataIndex = firstPoint.index; // Index of the data within the dataset
                        const chartDataset = chart.data.datasets[datasetIndex];
                        const skillIndex = chartDataset.skillIndex;
                        // Burada istediğiniz fonksiyonu çağırabilirsiniz.
                        //this.someFunction(label, value);
                        if (datasetIndex === 1 && value != 100) {
                            console.log("Bu segment için işlem yapılabilir.");
                            // Burada uygun işlev çağrılabilir
                            console.log(this.skills[skillIndex].namee);
                            await this.skilleriGetir();
                            this.goToSubskillforStatistics(this.skills[skillIndex]);
                        } else if (datasetIndex === 0 && value != 100) {
                            console.log("Bu segment için işlem yapılabilir.");
                            // Burada uygun işlev çağrılabilir
                            console.log(this.skills[skillIndex].namee);
                            await this.skilleriGetir();
                            this.goToSubskillforStatistics(this.skills[skillIndex]);
                        } else {
                            // Koşulu karşılamayan segmentler için bir mesaj gösterilebilir veya hiçbir işlem yapılmayabilir
                            console.log("Bu segment için işlem yapılmayacak.");
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {

                            label: (tooltipItem, data) => {
                                return tooltipItem.raw + '%';
                            }
                        }
                    }
                }

            },
        };
    },
    methods: {
        encryptData(data) {
  return cryptoJs.AES.encrypt(JSON.stringify(data), this.key).toString()
},

 decryptData(encryptedData) {
  const bytes = cryptoJs.AES.decrypt(encryptedData, this.key)
  return JSON.parse(bytes.toString(cryptoJs.enc.Utf8))
},
        goToSubskillforStatistics(skill) {
            this.$router.push({ name: 'altbeceristatistics', query: { skill: this.encryptData(skill), year: this.encryptData(this.sinif), student: this.encryptData(this.student), bolumInfo: this.encryptData(this.bolumInfo), secondGrader: this.encryptData(this.grader), secondGraderNotVerebilirmi: this.encryptData(this.graderNotVeriyormu) } });

        },
        verileriAyarla() {
            let yeniDatasetData = [];
            const yeniLabels = [];
            //this.dataa.datasets[0].backgroundColor = this.renk[this.sinif % this.renk.length];
            this.dataa = [];
            // skills array'indeki her bir skill için bir bar oluştur
            this.skills.forEach((skill, index) => {
                //yeniLabels.push(skill.namee); // skill adını label olarak ekle
                // Renk dizisinden renk seç
                //const kisaltma = skill.namee.length > 10 ? skill.namee.substring(0, 10) + '...' : skill.namee;
                //yeniLabels.push(kisaltma); // Kısaltılmış skill adını label olarak ekle

                // allStatistics array'indeki ilgili sınıfın verilerini bul
                let sinifIstatistikleri = this.statistics.allStatistics[this.sinif - 1] || [];
                // skillId'ye göre bu sınıfta bir değer var mı kontrol et
                let bulunanIstatistik = sinifIstatistikleri.find(stat => stat.skillId === skill.skillId);
                // Değer varsa kullan, yoksa 0 kullan
                yeniDatasetData.push(bulunanIstatistik ? bulunanIstatistik.successRate / 5 * 100 : 0);
                yeniDatasetData.push(bulunanIstatistik ? 100 - (bulunanIstatistik.successRate / 5 * 100) : 100);
                let dataaaaa = {
                    labels: ['Başarı Oranı', 'Başarısızlık Oranı'],
                    datasets: [
                        {
                            backgroundColor: [this.renk[index % 5], 'grey'],
                            data: yeniDatasetData,
                            skillIndex: index,
                        },
                    ],
                };
                if (this.dataa.length === 0) {
                    this.dataa = [dataaaaa]
                } else {
                    this.dataa.push(dataaaaa);
                }

                yeniDatasetData = [];

            });


            // Grafik datasetini güncelle
            //this.dataa.labels = yeniLabels;
            //this.dataa.datasets[0].data = yeniDatasetData;
            // Grafik komponentini güncellemek için reaktif verileri değiştir
            //this.dataa = dataaa;
        },
        async skilleriGetir() {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://localhost:8082/nursapp/app/teacher/getskills/${this.$store.state.user.bolumId}/${this.sinif}/${this.bolumInfo.secondGraderId}`,
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },

                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        } else {
                            return null;
                        }
                    })
                    .then(res => {
                        if (res != null) {
                            this.skills = res.skills;
                            this.grader = res.secondGrader;
                            this.graderNotVeriyormu = res.secondGraderNotVerebilirmi;
                            console.log(this.skills);

                        }
                    })
            } else if (this.$store.state.user.userRole === 1) {
                await fetch(`http://localhost:8082/nursapp/app/student/getskills/${this.$store.state.user.bolumId}/${this.sinif}/${this.bolumInfo.secondGraderId}`,
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },

                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        } else {
                            return null;
                        }
                    })
                    .then(res => {
                        if (res != null) {
                            this.skills = res.skills;
                            this.grader = res.secondGrader;
                            this.graderNotVeriyormu = res.secondGraderNotVerebilirmi;
                            console.log(this.skills);

                        }
                    })
            }
            else if (this.$store.state.user.userRole === 2) {
                console.log("if e girdi")
                await fetch(`http://localhost:8082/nursapp/app/secondgrader/getskills/${this.$store.state.user.bolumId}/${this.sinif}/${this.bolumInfo.secondGraderId}`,
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },

                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        } else {
                            return null;
                        }
                    })
                    .then(res => {
                        if (res != null) {
                            this.skills = res.skills;
                            this.grader = res.secondGrader;
                            this.graderNotVeriyormu = res.secondGraderNotVerebilirmi;
                            console.log(this.skills);


                        }
                    })
            }

        },
        async statisticleriGetir() {
            await this.skilleriGetir();

            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://localhost:8082/nursapp/app/teacher/getstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.bolumInfo.yilSayisi}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + this.$store.state.user.token
                    },
                }).then(res => {
                    if (res.status == 200) {
                        return res.json();
                    }
                    else {
                        return null;
                    }
                })
                    .then(res => {
                        if (res != null) {
                            this.statistics = res;
                            //this.statistics.skillCount = 66;
                            console.log(this.statistics);
                            this.verileriAyarla();


                        }
                    });
            }
            else if (this.$store.state.user.userRole === 1) {
                await fetch(`http://localhost:8082/nursapp/app/student/getstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.bolumInfo.yilSayisi}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + this.$store.state.user.token
                    },
                }).then(res => {
                    if (res.status == 200) {
                        return res.json();
                    }
                    else {
                        return null;
                    }
                })
                    .then(res => {
                        if (res != null) {
                            this.statistics = res;
                            //this.statistics.skillCount = 66;
                            console.log(this.statistics);
                            //this.verileriAyarla();
                            this.verileriAyarla();

                        }
                    });
            }
            else if (this.$store.state.user.userRole === 2) {
                await fetch(`http://localhost:8082/nursapp/app/secondgrader/getstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.bolumInfo.yilSayisi}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + this.$store.state.user.token
                    },
                }).then(res => {
                    if (res.status == 200) {
                        return res.json();
                    }
                    else {
                        return null;
                    }
                })
                    .then(res => {
                        if (res != null) {
                            this.statistics = res;
                            //this.statistics.skillCount = 66;
                            console.log(this.statistics);
                            this.verileriAyarla();
                        }
                    });
            }
        },

    }
}
</script>

<style scoped>
.qwer {
    background-color: black;
}

.label {
    text-align: center;
    font-size: x-large;
    margin-top: 20;

}

.asdf {
    flex-direction: column;
    text-align: center;
    align-items: center;
    justify-content: center;
    height: 95%;

}

.chart-container {
    display: flex;
    flex-direction: column;
}

.chart-row {
    margin-top: 3%;
    display: flex;
    justify-content: space-between;
}

.chart-item {
    margin-top: 4%;

    flex-basis: 48%;
    /* Pie chart'ların genişliği, aralarında biraz boşluk bırakacak şekilde ayarlanır */
}

.chartt {
    flex-direction: column;
    background-color: red;
}

.page {
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: auto;

    /* Üst boşluğu azaltır */
}

.header {
    display: flex;
    flex-direction: row;
    margin-top: 3%;
    margin-bottom: 3%;
    margin: 3%;
    text-align: left;
    /* Butonlar arasında boşluk oluşturur */
}

.headerrr {
    display: flex;
    flex-direction: row;
    margin: 0 3%;
    text-align: left;
    /* Butonlar arasında boşluk oluşturur */
}

.headerr {
    display: flex;
    flex-direction: column;
    margin-top: 3%;
    margin-bottom: 1%;
    margin-left: 3%;
    /* Butonlar arasında boşluk oluşturur */
}

.chart {
    display: flex;
    flex-direction: row;
    background-color: grey;
    /* Butonlar arasında boşluk oluşturur */
}


.login-buttons button:hover {
    background-color: #938e8e;
    /* Fare üzerindeyken arka plan rengini değiştirir */
}
</style>