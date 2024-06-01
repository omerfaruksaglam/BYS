<template>
    <div class="page">
        <div class="button-container">
            <input v-model="searchQuery" @input="onSearch" type="text" placeholder="Kullanıcı ara" class="search-box" />
        </div>
        <div class="content">
            <h2 v-if="filteredStudents.length">Öğrenciler</h2>
            <div v-for="student in filteredStudents" :key="student.userId" @click="onStudentTap(student)" class="list-item">
                {{ student.userId }} - {{ student.name }} {{ student.surname }}
            </div>
        </div>
    </div>
</template>
  
<script>
import cryptoJs from 'crypto-js';

export default {
    mounted() {
        if (this.$store.state.user && this.$store.state.user.token) {
            this.kullanicilarigetir();
        } else {
            console.error('Store or user info is not ready yet.');
        }
        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.notVer = this.decryptData(this.$route.query.notVer);
    },
    data() {
        return {
            key: 'amutrloisdhjtruymzapoewqmjfhthew',
            bolumInfo: null,
            notVer: null,
            students: [],
            filteredStudents: [],
            searchQuery: '',
        };
    },

    methods: {
        handleSearch(searchQuery) {
            searchQuery = String(searchQuery).trim();

            if (!searchQuery) {
                return this.students;
            }

            const searchLower = searchQuery.toLowerCase();

            return this.students.filter((student) => {
                const userIdString = String(student.userId).toLowerCase();
                const nameLower = student.name.toLowerCase();
                const surnameLower = student.surname.toLowerCase();

                if (!isNaN(parseInt(searchQuery[0]))) {
                    return userIdString.includes(searchLower);
                } else {
                    return (
                        nameLower.includes(searchLower) ||
                        surnameLower.includes(searchLower)
                    );
                }
            });
        },

        onSearch() {
            console.log("Arama yapılıyor:", this.searchQuery);

            this.filteredStudents = this.handleSearch(this.searchQuery);
        },
        encryptData(data) {
  return cryptoJs.AES.encrypt(JSON.stringify(data), this.key).toString()
},

 decryptData(encryptedData) {
  const bytes = cryptoJs.AES.decrypt(encryptedData, this.key)
  return JSON.parse(bytes.toString(cryptoJs.enc.Utf8))
},


        onStudentTap(student) {
            console.log("Öğrenci seçildi:", student);
            if(this.notVer){
                this.$router.push({name: 'sinifsec', query: {bolumInfo: this.encryptData(this.bolumInfo), notVer: this.encryptData(this.notVer), student: this.encryptData(student)}}); 
            } else {
                this.$router.push({name: 'durumgor', query: {bolumInfo: this.encryptData(this.bolumInfo), notVer: this.encryptData(this.notVer), student: this.encryptData(student)}}); 

            }
            /*if (this.notVer) {
                this.$navigateTo(SinifSec, {
                    props: {
                        student: student,
                        notVer: this.notVer,
                        bolumInfo: this.bolumInfo,
                    }
                });
            } else {
                this.$navigateTo(DurumGor, {
                    props: {
                        student: student,
                        yilSayisi: this.yilSayisi,
                        bolumInfo: this.bolumInfo,
                    }
                });*/
        },


        async kullanicilarigetir() {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://localhost:8082/nursapp/app/teacher/getusers/${this.$store.state.user.bolumId}`,
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
                            this.students = res;
                            this.filteredStudents = this.students;
                            console.log(this.filteredStudents);
                        }

                    })
            } else if (this.$store.state.user.userRole === 2) {
                await fetch(`http://localhost:8082/nursapp/app/secondgrader/getusers/${this.$store.state.user.bolumId}`,
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
                            this.students = res;
                            this.filteredStudents = this.students;
                            console.log(this.filteredStudents);
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
    padding: 0.625rem;
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
</style>
  