<template>
    <div class="page">
        <div class="st-home">
            <label class="header"> Beceri: {{skill.namee}}</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi">KB</th>
                        <th>ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="subskill in SubSkills" :key="subskill.subskillId">
                        <td @click="goToSubskillSteps(subskill)">{{ subskill.name }}</td>

                        <td @click="goToSubskillSteps(subskill)" v-if="bolumInfo.ogrenciNotVerebilirmi">{{ subskill.studentGrade  === null ? '-' : subskill.studentGrade}}</td>

                        <td @click="goToSubskillSteps(subskill)" >{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="goToSubskillSteps(subskill)" v-if="secondGraderNotVeriyormu">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
  
<script>
import cryptoJs from 'crypto-js';

export default {
    mounted() {

        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.notVer = this.decryptData(this.$route.query.notVer);
        this.student = this.decryptData(this.$route.query.student);
        this.year = this.decryptData(this.$route.query.year);
        this.skill = this.decryptData(this.$route.query.skill);
        this.secondGrader = this.decryptData(this.$route.query.secondGrader);
        this.secondGraderNotVeriyormu = this.decryptData(this.$route.query.secondGraderNotVerebilirmi);
        this.secodGraderName = this.customizeSecondGraderName();
        this.subSkilleriGetir();


    },
    data() {
        return {
            key: 'mitughjazbvrtpoyqjewlirtunmxshfr',
            bolumInfo: {},
            notVer: {},
            student: {},
            year: {},
            skill: [],
            secondGrader: {},
            secodGraderName: '',
            secondGraderNotVeriyormu: {},
            SubSkills: [],
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
        goToSubskillSteps(subskill) {
            /*this.$navigateTo(AltBeceriSec, {
                props: { notVer: this.notVer, fromStatistic: this.fromStatistic, student: this.student, sinif: this.sinif, skill: skill, bolumInfo: this.bolumInfo, secondGrader: this.grader, secondGraderNotVeriyormu: this.graderNotVeriyormu },
                // ... other navigation options
            });*/
            console.log("seaaaa oldu.");
            this.$router.push({name: "studentsubskillsteps" , query:{notVer: this.encryptData(this.notVer), subskill: this.encryptData(subskill), year: this.encryptData(this.year), student: this.encryptData(this.student), bolumInfo:this.encryptData(this.bolumInfo), skill:this.encryptData(this.skill), secondGrader:this.encryptData(this.secondGrader), secondGraderNotVerebilirmi:this.encryptData(this.secondGraderNotVeriyormu)}});
        },

        customizeSecondGraderName() {            
            const nameParts = this.secondGrader.secondGraderName.split(' ');

            const initials = nameParts.map(part => part[0]).join('');

            const customizedSecondGraderName = (initials + 'G').toUpperCase();;

            return customizedSecondGraderName;
        },
        async subSkilleriGetir() {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://localhost:8082/nursapp/app/teacher/getsubskills/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}/${this.skill.skillId}`,
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
                            console.log(res);
                            this.SubSkills = res;


                        }
                    })
                }
            else if(this.$store.state.user.userRole === 1){

                console.log("if e girdi")
                await fetch(`http://localhost:8082/nursapp/app/student/getsubskills/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}/${this.skill.skillId}`,
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
                            console.log(res);
                            this.SubSkills = res;


                        }
                    })
            } else if (this.$store.state.user.userRole === 2) {
                console.log("if e girdi")
                await fetch(`http://localhost:8082/nursapp/app/secondgrader/getsubskills/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}/${this.skill.skillId}`,
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
                            console.log(res);
                            this.SubSkills = res;


                        }
                    })
            }

        },

    },


};
</script>
  
<style scoped>
.header{
text-align: left;
width: 95%;
margin-top: 3%;
}

table {
  width: 95%; /* Tablonun tam ekran genişliğini kaplamasını sağlar */
  border-collapse: collapse; /* Kenar çizgilerini düzgün gösterir */
  border-spacing: 0; /* Kenar çizgileri arasındaki boşluğu kaldırır */
  margin-bottom: 2%;
}

td {
  text-align: left; /* İçeriği ortalar */
  border-bottom: 1px solid #000; /* Her satırın altında yatay çizgi */
  padding: 8px; /* Hücre içi boşluk */
 
}
th {
  text-align: left; /* İçeriği ortalar */
  border-bottom: 1px solid #000; /* Her satırın altında yatay çizgi */
  padding: 8px; /* Hücre içi boşluk */
 
}

td:not(:last-child) {
  border-right: 1px solid #000;
}

th:first-child, td:first-child {
  border-left: none;
}

th:first-child, th:last-child,
 td:last-child {
  border-right: none;
}
th:nth-child(1), td:nth-child(1) {
  width: 79%; /* İlk sütun için genişlik */
  color: blue;
  text-decoration: underline;
}

th:nth-child(2), td:nth-child(2),
th:nth-child(3), td:nth-child(3),
th:nth-child(4), td:nth-child(4)
 {
  width:  7%; /* Diğer sütunlar için genişlik */
  text-align: center;
}
.horizontal-divider {
    height: 2;
    background-color: #000000;
    width: 100%;
}
.vertical-divider {
    background-color: #000000;

}
.page {
    display: flex;
    flex-direction: column;
    width: 100%;
}

.button-container {
    display: flex;
    justify-content: center;
    padding-top: 10rem;

    /* 10px */
}

.search-box {
    padding: 0.625rem;
    /* 10px */
    border-radius: 0.3125rem;
    /* 5px */
    border: 1px solid #ccc;
    font-size: 1rem;
    /* 16px */
    width: 80%;
    margin-right: 1rem;
}

.list-item {
    margin: 0.3125rem;
    /* 5px */
    padding: 0.625rem;
    /* 10px */
    background-color: grey;
    border-radius: 0.3125rem;
    /* 5px */
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
    display: flex;
    justify-content: space-between;
}

.list-item:hover {
    background-color: #505050;
}

.st-home {
    background-color: var(--Color, #fff);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    overflow: auto;
}

.img-3 {
    /* Profil resmi */
    width: 4%;
    /* Profil resmi butonuna sığdırır */
    height: auto;
}

.delete-button {
    background-color: #f44336;
    /* Red */
    border: none;
    color: white;
    padding: 5px 15px;
    text-align: center;
    font-size: 14px;
    cursor: pointer;
}

.add-user-button {
    padding: 10px 20px;
    background-color: #96B0BE;
    /* yeşil */
    border: none;
    border-radius: 5%;
    color: white;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.marginr {
    margin-right: 1rem;
}

.add-user-button:hover {
    background-color: #608295;
}

.custom-button {
    background-color: #d81f1f;
    border: none;
    color: white;
    padding: 0.8rem 1.6rem;
    font-size: 1rem;
    margin: 0.5rem;
    border-radius: 4px;
    transition: background-color 0.3s;
    max-width: 250px;
    width: 100%;
}

.custom-button:hover {
    background-color: #ddd;
}

@media (max-width: 991px) {

    .custom-button {
        width: auto;
        /* Mobil görünümde buton genişliği ayarlanır */
    }
}</style>
  