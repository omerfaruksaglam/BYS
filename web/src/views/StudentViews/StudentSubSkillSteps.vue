<template>
    <div class="page">
        <div class="teacher-home">
            <label class="headerr"> Beceri: {{ skill.namee }}</label>
            <label class="header"> Alt Beceri: {{ subskill.name }}</label>
            <div class="equipments-list">
                <label>Malzemeler: </label>
                <span v-if="equipments.length !== 0" v-for="(equipment, index) in equipments" :key="equipment.equipmentId">
                    {{ equipment.name }}<span v-if="index < equipments.length - 1">, </span>
                </span>
            </div>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi">KB</th>
                        <th>ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu">{{ secodGraderName }}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="subskillstep in subskillSteps" :key="subskillstep.subskillStepsId">
                        <td :class="!subskillstep.isHead ? null : 'warning'" >{{ subskillstep.name }}</td>

                        <td v-if="bolumInfo.ogrenciNotVerebilirmi && (subskillstep.isHead || !this.notVer)" @click="!subskillstep.isHead && subskillstep.performGrade !== null ? findGraderAndShow(subskillstep, 1) : null">
                            {{ subskillstep.performGrade === null ? '-' :
                                subskillstep.performGrade }}</td>

                        <td v-if="bolumInfo.ogrenciNotVerebilirmi && !subskillstep.isHead && this.notVer">
                            <!-- Dropdown buraya eklenecek -->
                            <select v-model="subskillstep.performGrade" @change="updateChangedGrades(subskillstep)">
                                <option class="custom-select" :value="null" disabled selected
                                    v-if="subskillstep.performGrade === null">-</option>
                                <option class="custom-select" :value=0>0</option>
                                <option class="custom-select" :value=1>1</option>
                                <option class="custom-select" :value=2>2</option>
                                <option class="custom-select" :value=3>3</option>
                                <option class="custom-select" :value=4>4</option>
                            </select>
                        </td>



                        <td @click="!subskillstep.isHead && subskillstep.teacherGrade !== null ? findGraderAndShow(subskillstep, 0) : null">{{
                            subskillstep.teacherGrade === null ? '-' : subskillstep.teacherGrade }}</td>



                        <td v-if="secondGraderNotVeriyormu" @click="!subskillstep.isHead && subskillstep.nurseGrade !== null ? findGraderAndShow(subskillstep, 2) : null">{{ subskillstep.nurseGrade === null ? '-' :
                            subskillstep.nurseGrade }}</td>


                    </tr>
                </tbody>
            </table>
            <Button v-if="this.notVer" @click="kaydet" class="custom-button">Kaydet</Button>
        </div>
    </div>
</template>
  
<script>
import Swal from 'sweetalert2';
import cryptoJs from 'crypto-js';

export default {
    async mounted() {

        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.notVer = this.decryptData(this.$route.query.notVer);
        this.student = this.decryptData(this.$route.query.student);
        this.year = this.decryptData(this.$route.query.year);
        this.skill = this.decryptData(this.$route.query.skill);
        this.subskill = this.decryptData(this.$route.query.subskill);
        this.secondGrader = this.decryptData(this.$route.query.secondGrader);
        this.secondGraderNotVeriyormu = this.decryptData(this.$route.query.secondGraderNotVerebilirmi);
        this.secodGraderName = this.customizeSecondGraderName();
        await this.subSkilleriGetir();
        this.subskillStepsReal = JSON.parse(JSON.stringify(this.subskillSteps));;
        console.log(this.subskillSteps);

        console.log(this.subskillStepsReal);


    },
    data() {
        return {
            key: 'mitughjazbvrtpoyqjewlirtunmxshfr',
            bolumInfo: {},
            notVer: {},
            student: {},
            year: {},
            skill: [],
            subskill: [],
            secondGrader: {},
            secodGraderName: '',
            secondGraderNotVeriyormu: {},
            changedGrades: [],
            subskillSteps: [],
            subskillStepsReal: [],
            equipments: [],
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
        async findGraderAndShow(subskillsteps, graderRole){
            if(graderRole === 0){
                if(this.$store.state.user.userRole === 0 && this.notVer === false){
                    await fetch(`http://localhost:8082/nursapp/app/teacher/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                    this.graderInfo = res;
                    console.log('graderInfoooo:');
                    console.log(this.graderInfo);
                    var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            
                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                })
                }
                else if(this.$store.state.user.userRole === 1){



                    await fetch(`http://localhost:8082/nursapp/app/student/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                    this.graderInfo = res;
                    console.log('graderInfo:');
                    console.log(this.graderInfo);
                    var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                });



                }else if(this.$store.state.user.userRole === 2){



                    await fetch(`http://localhost:8082/nursapp/app/secondgrader/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                    this.graderInfo = res;
                    console.log('graderInfo:');
                    console.log(this.graderInfo);
                    var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();                     Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                });
                }



            }else if(graderRole === 1){


                if(this.$store.state.user.userRole === 1 && this.notVer === false){
                    var localDateTimeString = new Date(subskillsteps.performDate).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan sizin tarafınızdan " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });

                }else if(this.$store.state.user.userRole !== 1){
                    var localDateTimeString = new Date(subskillsteps.performDate).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan "+ this.student.name + " " + this.student.surname+" adlı öğrenci tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                }


            }else if(graderRole === 2){
                if(this.$store.state.user.userRole === 0){



                    await fetch(`http://localhost:8082/nursapp/app/teacher/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                    this.graderInfo = res;
                    console.log('graderInfo:');
                    console.log(this.graderInfo);
                    var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                });



                    
                }else if(this.$store.state.user.userRole === 1){




                    await fetch(`http://localhost:8082/nursapp/app/student/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                    this.graderInfo = res;
                    console.log('graderInfo:');
                    console.log(this.graderInfo);
                    var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                });




                }else if(this.$store.state.user.userRole === 2 && this.notVer === false){
                    await fetch(`http://localhost:8082/nursapp/app/secondgrader/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                    this.graderInfo = res;
                    console.log('graderInfo:');
                    console.log(this.graderInfo);
                    var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();                    Swal.fire({
                title: 'Puan Bilgisi',
                text: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes +  " tarihinde verilmiştir.",
                icon: 'success',
                confirmButtonText: 'Tamam',
            });
                });

                }
            }

        },
        async kaydet() {

            if (this.changedGrades.length > 0) {
                await fetch(`http://localhost:8082/nursapp/app/student/savegrades`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + this.$store.state.user.token
                    },
                    body: JSON.stringify({ "newGrades": this.changedGrades })
                }).then(response => {
                    if (response.status == 200) {
                        console.log('respons status: ' + response.status);
                        return 1;
                    }
                    else {
                        console.log('nullAQ:');
                        return null;
                    }
                }).then(data => {
                    if (data != null) {
                        console.log('data:');
                        console.log(data);
                        this.changedGrades = [];
                        this.$router.go(-1);
                    }
                });
            }

        },
        updateChangedGrades(item) {
            // Update logic here, including setting item's grade to selected value and updating changedGrades
            // Then update changedGrades array
            console.log('item:')
            console.log(item);
            const existing = this.changedGrades.find(changed => changed.subSkillStepsId === item.subskillStepsId);


            if (existing !== undefined) {
                existing.performGrade = parseInt(item.performGrade, 10);
            }
            else {
                const value = this.subskillStepsReal.find(changed => changed.subskillStepsId === item.subskillStepsId);
                console.log("value");
                console.log(value);
                if (value.performGrade !== parseInt(item.performGrade, 10)) {
                    const newItem = { subSkillStepsId: item.subskillStepsId, performGrade: parseInt(item.performGrade, 10), studentId: this.student.userId, bolumId: this.$store.state.user.bolumId, sinif: this.year };
                    this.changedGrades.push(newItem);
                }

            }
            console.log(this.changedGrades);
            console.log('asdffdsafsdafsadfadfsadfsfdasfdsfadsadfs');
        },


        customizeSecondGraderName() {
            const nameParts = this.secondGrader.secondGraderName.split(' ');

            const initials = nameParts.map(part => part[0]).join('');

            const customizedSecondGraderName = (initials + 'G').toUpperCase();;

            return customizedSecondGraderName;
        },
        async subSkilleriGetir() {



            console.log("if e girdi")
            await fetch(`http://localhost:8082/nursapp/app/student/getsubskillsteps/${this.subskill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}`,
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
                        this.subskillSteps = res.subskillStepsWithGrades;
                        this.equipments = res.equipments;


                    }
                })
        }

    },




};
</script>
  
<style scoped>
.header {
    text-align: left;
    width: 95%;
    margin-top: 3%;
}

.headerr {
    text-align: center;
    font-weight: bold;
    width: 95%;
    margin-top: 3%;
}

.custom-select {
    width: 100%;
    /* Başlangıç genişliğini 1 karakter genişliğinde ayarla */
    height: 100%;
    overflow: hidden;
    /* Taşan içeriği gizle */
    text-overflow: ellipsis;
    /* Uzun metinleri kes ve üç nokta ile göster */
}

table {
    width: 95%;
    /* Tablonun tam ekran genişliğini kaplamasını sağlar */
    border-collapse: collapse;
    /* Kenar çizgilerini düzgün gösterir */
    border-spacing: 0;
    /* Kenar çizgileri arasındaki boşluğu kaldırır */
    margin-bottom: 2%;
}

.equipments-list {
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    /* Flexbox kullanımını etkinleştirir */
    flex-wrap: wrap;
    /* İçerik ekran genişliğini aştığında yeni satıra geçer */
    /* İçerikleri dikey olarak ortalar */
    text-align: left;
    overflow: auto;
    padding: 1%;

    /* Malzemeler arasında boşluk bırakır */

    width: 95%;
    /* Genişliği ayarlar */
}

td {
    text-align: left;
    /* İçeriği ortalar */
    border-bottom: 1px solid #000;
    /* Her satırın altında yatay çizgi */
    padding: 8px;
    /* Hücre içi boşluk */

}

th {
    text-align: left;
    /* İçeriği ortalar */
    border-bottom: 1px solid #000;
    /* Her satırın altında yatay çizgi */
    padding: 8px;
    /* Hücre içi boşluk */

}

td:not(:last-child) {
    border-right: 1px solid #000;
}

th:first-child,
td:first-child {
    border-left: none;
}

th:first-child,
th:last-child,
td:last-child {
    border-right: none;
}

th:nth-child(1),
td:nth-child(1) {
    width: 79%;
    /* İlk sütun için genişlik */
}
.warning {
  
  font-weight: bold; /* Metni kalın yap */
}
th:nth-child(2),
td:nth-child(2),
th:nth-child(3),
td:nth-child(3),
th:nth-child(4),
td:nth-child(4) {
    width: 7%;
    /* Diğer sütunlar için genişlik */
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
  