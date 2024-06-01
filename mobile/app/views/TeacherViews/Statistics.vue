<!--<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="student.name + ' ' + student.surname" class="action-bar" />
            <ScrollView orientation="horizontal">
            <FlexBoxLayout :style="{ width: containerWidth }" class="charts-container">
                <FlexBoxLayout v-for="(item, index) in bolumInfo.skillCount" :key="index" class="chart-container">
                    <label class="header" :text="'Başlık ' + (index + 1)" />

                    <PieChart ref="pieChart" width="100%" @loaded="onChartLoaded(index)" />

                </FlexBoxLayout>
            </FlexBoxLayout>
        </ScrollView>
    </Page>
</template>-->

<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="student.name + ' ' + student.surname" class="action-bar" flat />

        <GridLayout rows="*, *, 20*">
            <label row="0" class="headerr" text="Grafiksel Gösterimler" />
            <FlexBoxLayout row="1" @tap="sinifSecc">
                <label class="headerr" text="Sınıf Seç: " />
                <label class="headerrrrr" :text="sinif" />
            </FlexBoxLayout>
            <ScrollView row="2">
                <label class="header" text='1. Sınıf'></label>

                <FlexBoxLayout class="form" flexDirection="column" :style="{ height: containerWidth }">

                    <FlexBoxLayout class="chart" v-for="(item, index) in skills" flexDirection="column">
                        <GridLayout rows="4*, 40*" flexDirection="column"
                            :style="{ width: '100%', margin: '0 0 35 0' }">


                            <label row="0" :key="index + 'header'" class="headerrrr" :text='item.namee' />


                            <FlexBoxLayout row="1" @tap="goToX(index)">
                                <PieChart :key="index" ref="pieChart" @loaded="onChartLoaded(index)" />
                            </FlexBoxLayout>
                        </GridLayout>
                    </FlexBoxLayout>

                </FlexBoxLayout>
            </ScrollView>
        </GridLayout>

    </Page>
</template>
<!--<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="student.name + ' ' + student.surname" class="action-bar" />

        <ScrollView orientation="horizontal" class="scroll-view">
            <FlexBoxLayout flexDirection="row" class="charts-container">

                <FlexBoxLayout v-for="(item, index) in bolumInfo.skillCount" flexDirection="column" :key="index" class="chart-container">
                    <label :key="index + 'header'" class="header" :text='"Başlık " + (index + 1)'></label>
                    <PieChart ref="pieChart" width="300" height="300" @loaded="onChartLoaded(index)" />
                </FlexBoxLayout>

            </FlexBoxLayout>
        </ScrollView>

    </Page>
</template>-->

<script>
//<FlexboxLayout v-for="index in this.statistics.skillCount" :key="index" @tap="onBarTap(index)" class="transparent-container" ></FlexboxLayout>
import { PieChart } from '@nativescript-community/ui-chart/charts/PieChart';
import { PieDataSet } from '@nativescript-community/ui-chart/data/PieDataSet';
import { PieData } from '@nativescript-community/ui-chart/data/PieData';
import BeceriSec from './BeceriSec.vue';
import StatisticicinAltBeceri from './StatisticicinAltBeceri.vue';
import { GridLayout } from '@nativescript/core';
export default {
    props: ['student', 'bolumInfo'],
    data() {
        return {
            skills: null,
            grader: null,
            graderNotVeriyormu: null,
            sinif: 1,
            statistics: {},
            renk: {
                0: 'red',
                1: 'green',
                2: 'turquoise',
                3: 'blue',
                4: 'purple',
            }
        };
    },
    computed: {
        containerWidth() {
            return `${this.bolumInfo.skillCount * 52}%`; // Eğer 3 chart varsa 300vw olacak
        }
    },
    methods: {
        sinifSecc() {

            const options = {
                title: "Sınıf Seçin",
                message: "Lütfen grafiğini görmek istediğiniz sınıfı seçin:",
                cancelButtonText: "İptal",
                actions: Array.from({ length: this.bolumInfo.yilSayisi }, (_, i) => `${i + 1}. Sınıf`),
            };

            action(options).then((result) => {
                if (result !== "İptal") {
                    const deger = parseInt(result.substring(0, 1), 10);
                    //deger = parseInt(deger, 10);
                    this.sinif = deger;
                    this.setDatas();
                }
            });

        },
        async goToX(index) {
            await this.skilleriGetir();
            this.$navigateTo(StatisticicinAltBeceri, {
                props: { student: this.student, sinif: this.sinif, skill: this.skills[index], bolumInfo: this.bolumInfo, secondGrader: this.grader, secondGraderNotVeriyormu: this.graderNotVeriyormu },
                // ... other navigation options
            });
        },
        onBarTap(index) {
            console.log(`Tıklanan bar indexi: ${index}`);
            this.$navigateTo(BeceriSec, {
                props: { notVer: false, fromStatistic: true, student: this.student, sinif: index, bolumInfo: this.bolumInfo },
            });
        },
        async statisticleriGetir() {
            await this.skilleriGetir();
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.bolumInfo.yilSayisi}`, {
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
                            console.log(this.skills.length);
                            this.setDatas();
                        }
                    });
            }
            else if (this.$store.state.user.userRole === 1) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.bolumInfo.yilSayisi}`, {
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
                            console.log(this.skills.length);  
                            this.setDatas();                          //this.setDatas(this.statistics);
                        }
                    });
            }
            else if (this.$store.state.user.userRole === 2) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.bolumInfo.yilSayisi}`, {
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
                            console.log(this.skills.length); 
                            this.setDatas();                           //this.setDatas(this.statistics);
                        }
                    });
            }
        },
        onChartLoaded(index) {

            this.createPieChart(index);

        },
        createPieChart(index) {
            const chart = this.$refs.pieChart[index].nativeView;


           /* const entries = [
                { y: 75, label: 'Başarı %75' },
                { y: 25, label: '' }
            ];
            const dataSet = new PieDataSet(entries, '');
            dataSet.setColors([this.renk[index % 5], '#C0C0C0']); // Kırmızı dolu, gri boş alanlar için renkler

            const data = new PieData([dataSet]);
            data.setValueTextSize(14);
            data.setValueTextColors(['white', 'transparent']);
            data.setDrawValues(true);

            chart.setData(data);
*/
            // Chart'ın yüzde olarak değerleri göstermesini sağlayın
            chart.setUsePercentValues(false);
            /*data.setValueFormatter(
                (value, entry, index, count, dataSetIndex, viewPortHandler) => {
                    // 'value', yüzdelik olarak verilir
                    return `${value.toFixed(1)}%`;
                }
            );*/

            // Diğer chart ayarları
            chart.setDrawHoleEnabled(false);
            chart.getLegend().setEnabled(false);
            chart.getDescription().setEnabled(false);

            // Chart'ı güncelle
            chart.notifyDataSetChanged();
            chart.invalidate();

            // Animasyon ile gösterim
            //chart.animateY(1400);

        },
        async skilleriGetir() {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getskills/${this.$store.state.user.bolumId}/${this.sinif}/${this.bolumInfo.secondGraderId}`,
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
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getskills/${this.$store.state.user.bolumId}/${this.sinif}/${this.bolumInfo.secondGraderId}`,
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
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getskills/${this.$store.state.user.bolumId}/${this.sinif}/${this.bolumInfo.secondGraderId}`,
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
        setDatas() {
            const currentStatistics = this.statistics.allStatistics[this.sinif - 1] || [];

            // this.skills üzerinde döngü yaparak her bir skill için veri setini güncelle
            this.skills.forEach((skill, i) => {
                // Eşleşen istatistikleri bul
                const matchingStat = currentStatistics.find(stat => stat.skillId === skill.skillId);
                const successPercentage = matchingStat ? matchingStat.successRate : 0;

                // Grafikte kullanılacak veri setini oluştur
                const entries = [
                    { y: successPercentage/5, label: `Başarı %${successPercentage*100/5}` },
                    { y: 1 - (successPercentage/5), label: '' } // Boş kısım için geri kalan yüzde
                ];
                console.log(entries);

                // Veri setini ve diğer grafik ayarlarını belirle
                const dataSet = new PieDataSet(entries, '');
                dataSet.setColors([this.renk[i  % 5], '#C0C0C0']); // Renkleri ayarla
                const data = new PieData([dataSet]);
                data.setValueTextSize(14);
                data.setValueTextColors(['white', 'transparent']);
                data.setDrawValues(true);

                // Grafik referansını al ve veri setiyle güncelle
                const chart = this.$refs.pieChart[i].nativeView;
                chart.setData(data);

                // Diğer grafik ayarlarını yap
                chart.setUsePercentValues(true);
                chart.setDrawHoleEnabled(false);
                chart.getLegend().setEnabled(false);
                chart.getDescription().setEnabled(false);
                chart.notifyDataSetChanged();
                chart.invalidate();
            });
        },

        onNavigatedTo() {
            this.statisticleriGetir();
        },
    },
};
//88.8%
</script>

<style scoped>
.ns-dark .headerrrrr {
    font-size: x-large;
    font-weight: bold;
    text-align: center;
    justify-content: center;
    color: white;
    text-wrap: true;
    /* Uzun metni satırlara böl */

}

.ns-dark .bg {
    align-items: center;
    /* Çocukları yatay olarak ortalar */
    justify-content: center;
    /* Çocukları dikey olarak ortalar */
    width: 100%;
}

.ns-dark .scroll-view {
    width: 100%;
    height: 100%;
    /* Veya istediğiniz bir yükseklik */

}

.ns-dark .charts-container {
    flex-direction: row;

}

.ns-dark .chart-container {
    width: 100%;
    /* Ekranın tam genişliği */
    flex-direction: column;
    align-items: center;
    /* Çocukları yatay olarak ortalar */
    justify-content: center;
    /* Çocukları dikey olarak ortalar */
}

.ns-dark .header {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: white;
}

.ns-dark .headerrrr {
    display: flex;
    font-size: 16%;
    font-weight: bold;
    text-align: center;
    color: white;
    margin-bottom: 5;
    overflow: visible;
    white-space: nowrap;
}

.ns-dark .headerrrrr{
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: white;
    margin-left: 10;
    border-color: white;
    border-inline-color: white;
}
.ns-dark .headerr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: white;
    text-decoration: underline;
    margin-left: 10;

}

.ns-dark .xdd {
    margin-left: 10;
}

.ns-dark .xddd {
    margin: 10;
}

.ns-dark .axis-label {
    color: white;
    font-size: 12%;
    text-wrap: true;
    text-align: left;
}

.ns-dark .axis-label1 {
    color: white;
    background-color: transparent;
    font-size: 6%;
    width: 100%;
    height: 100%;
    text-wrap: true;
    align-items: flex-end;
    justify-content: flex-end;
}

.ns-dark .axis-label2 {
    color: white;
    font-size: 10%;
    text-wrap: true;

}

.ns-dark .y-axis-label-container {
    justify-content: flex-start;
    align-items: flex-end;
    margin-top: 50;
    margin-right: 10;
}

.ns-dark .x-axis-label-container {
    justify-content: flex-end;
    align-items: flex-start;
    margin-bottom: 50;
    margin-left: 10;
}

.ns-dark .chart-container {
    display: flex;
    width: 100%;
    /* Konteynerin genişliğini ekran genişliğine sığacak şekilde ayarla */
    height: 100%;
    /* İçeriğe göre yükseklik ayarlama */
    align-items: center;
    /* İçerikleri dikey olarak ortala */
    justify-content: space-between;
    /* İçerikleri eşit aralıklarla dağıt */
    padding-left: 15.2;
    padding-right: 15.9;
    margin-top: 50;
    margin-bottom: 50;
    background-color: transparent;
    opacity: 0.5;
}

.ns-dark .transparent-container {
    background-color: red;
    /* Konteyneri şeffaf yap */
    margin-left: 3.2;

    /* Örnek: Sabit yükseklik, BarChart'ın yüksekliği ile aynı olmalı */
    width: 100%;
    height: 100%;
    /* Genişlik JavaScript ile dinamik olarak ayarlanacak */
}

.ns-dark .form {
    background-color: transparent;
    width: 90%;
    height: 400%;
    padding-top: 15;
    padding-bottom: 15;


}

.ns-dark .asd {
    background-color: transparent;
    margin-bottom: 15;
    width: 100%;
    height: 30%;

}

.ns-dark .form2 {
    background-color: transparent;
    width: 100%;
    height: 60%;
    padding-top: 15;
    padding-bottom: 15;


}

.ns-dark .action-bar {
    background-color: #19191B;
    color: #ffffff;
}






















.ns-light .headerrrrr {
    font-size: x-large;
    font-weight: bold;
    text-align: center;
    justify-content: center;
    color: #000000;
    text-wrap: true;
    /* Uzun metni satırlara böl */

}

.ns-light .bg {
    align-items: center;
    /* Çocukları yatay olarak ortalar */
    justify-content: center;
    /* Çocukları dikey olarak ortalar */
    width: 100%;
}

.ns-light .scroll-view {
    width: 100%;
    height: 100%;
    /* Veya istediğiniz bir yükseklik */

}

.ns-light .charts-container {
    flex-direction: row;

}

.ns-light .chart-container {
    width: 100%;
    /* Ekranın tam genişliği */
    flex-direction: column;
    align-items: center;
    /* Çocukları yatay olarak ortalar */
    justify-content: center;
    /* Çocukları dikey olarak ortalar */
}

.ns-light .header {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
}

.ns-light .headerrrr {
    display: flex;
    font-size: 16%;
    font-weight: bold;
    text-align: center;
    color: #000000;
    margin-bottom: 5;
    overflow: visible;
    white-space: nowrap;
}

.ns-light .headerrrrr{
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
    margin-left: 10;
    border-color: #000000;
    border-inline-color: #000000;
}
.ns-light .headerr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
    text-decoration: underline;
    margin-left: 10;

}

.ns-light .xdd {
    margin-left: 10;
}

.ns-light .xddd {
    margin: 10;
}

.ns-light .axis-label {
    color: #000000;
    font-size: 12%;
    text-wrap: true;
    text-align: left;
}

.ns-light .axis-label1 {
    color: #000000;
    background-color: transparent;
    font-size: 6%;
    width: 100%;
    height: 100%;
    text-wrap: true;
    align-items: flex-end;
    justify-content: flex-end;
}

.ns-light .axis-label2 {
    color: #000000;
    font-size: 10%;
    text-wrap: true;

}

.ns-light .y-axis-label-container {
    justify-content: flex-start;
    align-items: flex-end;
    margin-top: 50;
    margin-right: 10;
}

.ns-light .x-axis-label-container {
    justify-content: flex-end;
    align-items: flex-start;
    margin-bottom: 50;
    margin-left: 10;
}

.ns-light .chart-container {
    display: flex;
    width: 100%;
    /* Konteynerin genişliğini ekran genişliğine sığacak şekilde ayarla */
    height: 100%;
    /* İçeriğe göre yükseklik ayarlama */
    align-items: center;
    /* İçerikleri dikey olarak ortala */
    justify-content: space-between;
    /* İçerikleri eşit aralıklarla dağıt */
    padding-left: 15.2;
    padding-right: 15.9;
    margin-top: 50;
    margin-bottom: 50;
    background-color: transparent;
    opacity: 0.5;
}

.ns-light .transparent-container {
    background-color: red;
    /* Konteyneri şeffaf yap */
    margin-left: 3.2;

    /* Örnek: Sabit yükseklik, BarChart'ın yüksekliği ile aynı olmalı */
    width: 100%;
    height: 100%;
    /* Genişlik JavaScript ile dinamik olarak ayarlanacak */
}

.ns-light .form {
    background-color: transparent;
    width: 90%;
    height: 400%;
    padding-top: 15;
    padding-bottom: 15;


}

.ns-light .asd {
    background-color: transparent;
    margin-bottom: 15;
    width: 100%;
    height: 30%;

}

.ns-light .form2 {
    background-color: transparent;
    width: 100%;
    height: 60%;
    padding-top: 15;
    padding-bottom: 15;


}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
}

</style>
