<template>
    <div class="page">
        <div class="teacher-home">
            <Button v-for="skill in skills" :key="skill.skillId" @click="goSubSkill(skill)" class="custom-button">{{ skill.namee }}</Button>
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

        this.skilleriGetir();

        
    },
    data() {
        return {
            key: 'amutrloisdhjtruymzapoewqmjfhthew',
            bolumInfo: {},
            notVer: {},
            student: {},
            year: {},
            skills: [],
            grader: {},
            graderNotVeriyormu: {},
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
        goSubSkill(skill) {
            /*this.$navigateTo(AltBeceriSec, {
                props: { notVer: this.notVer, fromStatistic: this.fromStatistic, student: this.student, sinif: this.sinif, skill: skill, bolumInfo: this.bolumInfo, secondGrader: this.grader, secondGraderNotVeriyormu: this.graderNotVeriyormu },
                // ... other navigation options
            });*/
            if(this.notVer){
                this.$router.push({name:'altbecerisec',query:{notVer:this.encryptData(this.notVer), skill:this.encryptData(skill), year: this.encryptData(this.year), student: this.encryptData(this.student), bolumInfo:this.encryptData(this.bolumInfo), secondGrader:this.encryptData(this.grader), secondGraderNotVerebilirmi:this.encryptData(this.graderNotVeriyormu)}});
            
            }else {
                this.$router.push({name:'altbeceristatistics',query:{ skill:this.encryptData(skill), year: this.encryptData(this.year), student: this.encryptData(this.student), bolumInfo:this.encryptData(this.bolumInfo), secondGrader:this.encryptData(this.grader), secondGraderNotVerebilirmi:this.encryptData(this.graderNotVeriyormu)}});
            }
        },

        async skilleriGetir() {
            if(this.$store.state.user.userRole === 0){
                await fetch(`http://localhost:8082/nursapp/app/teacher/getskills/${this.$store.state.user.bolumId}/${this.year}/${this.bolumInfo.secondGraderId}`,
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
                   if(res != null){
                    this.skills = res.skills;
                    this.grader = res.secondGrader;
                    this.graderNotVeriyormu = res.secondGraderNotVerebilirmi;
                    console.log(this.grader);
                    console.log(this.graderNotVeriyormu);
                    console.log(res.secondGrader);
                    console.log(res.secondGraderNotVerebilirmi);
                   }
                })
            }else if(this.$store.state.user.userRole === 2){
                console.log("if e girdi")
                await fetch(`http://localhost:8082/nursapp/app/secondgrader/getskills/${this.$store.state.user.bolumId}/${this.year}/${this.bolumInfo.secondGraderId}`,
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
                   if(res != null){
                    this.skills = res.skills;
                    this.grader = res.secondGrader;
                    this.graderNotVeriyormu = res.secondGraderNotVerebilirmi;
                    console.log(this.grader);
                    console.log(this.graderNotVeriyormu);
                    console.log(res.secondGrader);
                    console.log(res.secondGraderNotVerebilirmi);
                    if(this.graderNotVeriyormu === false){
                        this.notVer = false;
                    }
                   }
                })
            }

        },

        },


};
</script>
  
<style scoped>
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
.teacher-home {
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
}
</style>
  