<template>
    <div class="page">

        <div v-if="showModal" class="update-user">
            <div class="overlay" @click="closeModal"></div>
            <div class="modal">
                <!-- Filtreleme Formu -->
                <form @submit.prevent="applyFilters">
                    <h3>Filtreler</h3>

                    <!-- Puanlı Sıralama Checkbox -->
                    <!-- Azalan Puan Sırası Checkbox -->
                    <div>
                        <input type="checkbox" id="kendibasina" v-model="kb"
                            :disabled="!bolumInfo.ogrenciNotVerebilirmi">
                        <label for="kendibasina">Ogrenci Sütununu Gör</label>
                    </div>

                    <div>
                        <input type="checkbox" id="ogretimElemanı" v-model="oeg"
                            :disabled="kb === false && hg === false ? true : false">
                        <label for="ogretimElemanı">Ogretim Elemanı Sütununu Gör</label>
                    </div>

                    <!-- Azalan Puan Sırası Checkbox -->
                    <div>
                        <input type="checkbox" id="IkincilPuanlandiriciCheckBox" v-model="hg"
                            :disabled="!secondGraderNotVeriyormu">
                        <label for="IkincilPuanlandiriciCheckBox">Ikincil Puanlandırıcı Sütununu Gör</label>
                    </div>

                    <div>
                        <input type="checkbox" id="puanliSiralamaCheckbox" v-model="puanliSiralama">
                        <label for="puanliSiralamaCheckbox">Puana Göre Sıralama</label>
                    </div>

                    <!-- Azalan Puan Sırası Checkbox -->
                    <div v-if="puanliSiralama" class="indent">
                        <input type="checkbox" id="azalanSiraCheckbox" v-model="azalanSira">
                        <label for="azalanSiraCheckbox">Azalan Puan Sırası</label>
                    </div>


                    <div v-if="puanliSiralama" class="indent">
                        <input type="checkbox" id="kbsira" v-model="kbSira" @change="selectOnlyOne('kb')"
                            :disabled="kb === false ? true : false">
                        <label for="kbsira">Ogrenci Notuna Göre</label>
                    </div>
                    <div v-if="puanliSiralama" class="indent">
                        <input type="checkbox" id="oegSira" v-model="oegSira"
                            :disabled="kbSira === false && hgSira === false || oeg === false ? true : false"
                            @change="selectOnlyOne('oeg')">
                        <label for="oegSira">Ogretim Elemanı Notuna Göre</label>
                    </div>
                    <div v-if="puanliSiralama" class="indent">
                        <input type="checkbox" id="hgSira" v-model="hgSira" @change="selectOnlyOne('hg')"
                            :disabled="hg === false ? true : false">
                        <label for="hgSira">Ikincil Puanlandırıcı Notuna Göre</label>
                    </div>

                    <!-- Filtreleme Uygula Butonu -->
                    <button type="submit">Kapat</button>
                </form>
            </div>
        </div>


        <div class="teacher-home" v-if="puanliSiralama">
            <label class="header"> Beceri: {{skill.namee}}</label>
            <div class="subskill-checkbox">
                    <label class="headerrr">Alt Beceri: {{ subskill.name }}</label>
                    <div class="checkbox-container">
                    <button @click="showModal = !showModal">
                        <font-awesome-icon icon="filter" /> Filtrele
                    </button>
                </div>
                </div>
            <label @click="azalanSira = !azalanSira"  class="headerr"> {{ !azalanSira ? "Puanı 0 Olan Alt Beceri Basamakları" : "Puanı 4 Olan Alt Beceri Basamakları"}}</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="!azalanSira" v-for="subskill in subSkills0" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td  @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td  @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>

                    <tr v-if="azalanSira" v-for="subskill in subSkills4" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                </tbody>
            </table>

            <label @click="azalanSira = !azalanSira" class="headerr">{{ !azalanSira ? "Puanı 1 Olan Alt Beceri Basamakları" : "Puanı 3 Olan Alt Beceri Basamakları"}}</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="!azalanSira" v-for="subskill in subSkills1" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td  v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>

                    <tr v-if="azalanSira" v-for="subskill in subSkills3" :key="subskill.subSkillStepsId">
                        <td>{{ subskill.name }}</td>

                        <td @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                </tbody>
            </table>

            <label @click="azalanSira = !azalanSira" class="headerr"> Puanı 2 Olan Alt Beceri Basamakları</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr  v-for="subskill in subSkills2" :key="subskill.subSkillStepsId">
                        <td>{{ subskill.name }}</td>

                        <td @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td  v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                    
                </tbody>
            </table>

            <label @click="azalanSira = !azalanSira" class="headerr">{{ !azalanSira ? "Puanı 3 Olan Alt Beceri Basamakları" : "Puanı 1 Olan Alt Beceri Basamakları"}}</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="!azalanSira" v-for="subskill in subSkills3" :key="subskill.subSkillStepsId">
                        <td  >{{ subskill.name }}</td>

                        <td @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                    <tr v-if="azalanSira" v-for="subskill in subSkills1" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null" v-if="bolumInfo.ogrenciNotVerebilirmi && kb">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null" > {{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null" v-if="secondGraderNotVeriyormu && hg">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                </tbody>
            </table>

            <label @click="azalanSira = !azalanSira" class="headerr">{{ !azalanSira ? "Puanı 4 Olan Alt Beceri Basamakları" : "Puanı 0 Olan Alt Beceri Basamakları"}}</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr  v-if="!azalanSira" v-for="subskill in subSkills4" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td v-if="secondGraderNotVeriyormu && hg" @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                    <tr v-if="azalanSira"  v-for="subskill in subSkills0" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td v-if="secondGraderNotVeriyormu && hg" @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                </tbody>
            </table>

            <label v-if="subSkillsnull.length !== 0 && subSkillsnull !== null" @click="azalanSira = !azalanSira" class="headerr">Puanı olmayan Alt Beceri Basamakları</label>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{secodGraderName}}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-if="subSkillsnull.length !== 0 && subSkillsnull !== null" v-for="subskill in subSkillsnull" :key="subskill.subSkillStepsId">
                        <td >{{ subskill.name }}</td>

                        <td v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @click="!subskill.isHead && subskill.performGrade !== null ? findGraderAndShow(subskill, 1) : null">{{ subskill.performGrade  === null ? '-' : subskill.performGrade}}</td>

                        <td v-if="oeg" @click="!subskill.isHead && subskill.teacherGrade !== null ? findGraderAndShow(subskill, 0) : null">{{ subskill.teacherGrade === null ? '-' : subskill.teacherGrade }}</td>

                        <td v-if="secondGraderNotVeriyormu && hg" @click="!subskill.isHead && subskill.nurseGrade !== null ? findGraderAndShow(subskill, 2) : null">{{ subskill.nurseGrade === null ? '-' : subskill.nurseGrade }}</td>
                    </tr>
                    
                </tbody>
            </table>
        </div>



        <div class="teacher-home" v-if="!puanliSiralama">
            <label class="headerr"> Beceri: {{ skill.namee }}</label>
            <div class="subskill-checkbox">
                    <label class="headerrr">Alt Beceri: {{ subskill.name }}</label>
                    <div class="checkbox-container">
                    <button @click="showModal = !showModal">
                        <font-awesome-icon icon="filter" /> Filtrele
                    </button>
                </div>
                </div>
            <table>
                <thead>
                    <tr>
                        <th> </th>
                        <th v-if="bolumInfo.ogrenciNotVerebilirmi && kb">KB</th>
                        <th v-if="oeg">ÖEG</th>
                        <th v-if="secondGraderNotVeriyormu && hg">{{ secodGraderName }}</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="subskillstep in subskillSteps" :key="subskillstep.subskillStepsId">
                        <td :class="!subskillstep.isHead ? null : 'warning'">{{ subskillstep.name }}</td>

                        <td
                            v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @click="!subskillstep.isHead && subskillstep.performGrade !== null ? findGraderAndShow(subskillstep, 1) : null">
                            {{ subskillstep.performGrade === null ? '-' :
                                subskillstep.performGrade }}</td>




                        <td v-if="oeg" @click="!subskillstep.isHead && subskillstep.teacherGrade !== null ? findGraderAndShow(subskillstep, 0) : null">{{
                            subskillstep.teacherGrade === null ? '-' : subskillstep.teacherGrade }}</td>
                        


                        <td v-if="secondGraderNotVeriyormu && hg" @click="!subskillstep.isHead && subskillstep.nurseGrade !== null ? findGraderAndShow(subskillstep, 2) : null">{{ subskillstep.nurseGrade === null ? '-' :
                            subskillstep.nurseGrade }}</td>

                        
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
  
<script>
import Swal from 'sweetalert2';
import cryptoJs from 'crypto-js';

export default {
    mounted() {

        this.bolumInfo = this.decryptData(this.$route.query.bolumInfo);
        this.bolumInfo.ogrenciNotVerebilirmi ? this.kb = true : this.kb = false;
        this.student = this.decryptData(this.$route.query.student);
        this.year = this.decryptData(this.$route.query.year);
        this.skill = this.decryptData(this.$route.query.skill);
        this.subskill = this.decryptData(this.$route.query.subskill);
        this.secondGrader = this.decryptData(this.$route.query.secondGrader);
        this.secondGraderNotVeriyormu = this.decryptData(this.$route.query.secondGraderNotVerebilirmi);
        this.secondGraderNotVeriyormu ? this.hg = true : this.hg = false;
        this.secodGraderName = this.customizeSecondGraderName();
        this.subSkilleriGetir(0);
        console.log(this.year);

    },
    watch: {
        kb(val) {
            if (val === false && this.puanliSiralama === true) {
                console.log('asdffff')
                this.selectOnlyOne("oeg");
            }
            this.checkOeg();
        },
        hg(val) {
            if (val === false && this.puanliSiralama === true) {
                console.log('asdhggggggg')
                this.selectOnlyOne("oeg");
            }
            this.checkOeg();
        },

    },
    data() {
        return {
            key: 'mitughjazbvrtpoyqjewlirtunmxshfr',
            kbSira: false,
            oegSira: true,
            hgSira: false,
            oeg: true,
            kb: false,
            hg: false,
            showModal: false,
            puanliSiralama: false,
            subskillSteps: [],
            bolumInfo: {},
            student: {},
            year: {},
            skill: [],
            subskill: {},
            secondGrader: {},
            secodGraderName: '',
            secondGraderNotVeriyormu: {},
            subSkillsnull: [],
            subSkills0: [],
            subSkills1: [],
            subSkills2: [],
            subSkills3: [],
            subSkills4: [],
            azalanSira: false,
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
        selectOnlyOne(siraType) {
            if (siraType === 'kb') {
                this.oegSira = false;
                this.hgSira = false;
                this.subSkilleriGetir(1);

            } else if (siraType === 'oeg') {
                this.kbSira = false;
                this.hgSira = false;
                this.subSkilleriGetir(0);

            } else if (siraType === 'hg') {
                this.kbSira = false;
                this.oegSira = false;
                this.subSkilleriGetir(2);

            }
            if (this.kbSira === false && this.hgSira === false) {
                this.oegSira = true;
                this.subSkilleriGetir(0);
            }

            //statisticler icin puanlar cagrilacak true olan degere gore
        },
        checkOeg() {
            if (this.kb === false && this.hg === false) {
                this.oeg = true;
                this.selectOnlyOne("oeg");
            }

        },
        closeModal() {
            this.showModal = false;
        },
        applyFilters() {
            // Filtreleme işlemleri burada yapılır
            console.log("Puanlı Sıralama:", this.puanliSiralama);
            console.log("Azalan Sıra:", this.azalanSira);
            this.closeModal();
        },
        async findGraderAndShow(subskillsteps, graderRole){
            if(graderRole === 0){
                if(this.$store.state.user.userRole === 0){
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


                if(this.$store.state.user.userRole === 1 ){
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




                }else if(this.$store.state.user.userRole === 2 ){
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
        customizeSecondGraderName() {            
            const nameParts = this.secondGrader.secondGraderName.split(' ');

            const initials = nameParts.map(part => part[0]).join('');

            const customizedSecondGraderName = (initials + 'G').toUpperCase();;

            return customizedSecondGraderName;
        },
        async subSkilleriGetir(baseRole) {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://localhost:8082/nursapp/app/teacher/getsubskillstepsforstatistics/${this.subskill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}/${baseRole}`,
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
                            console.log('res in icindeyiz');
                            console.log(res.thirdGrades);
                            console.log(res.thirdGrades.length === 0);
                            console.log(this.subSkills3);


                            this.subskillSteps = res.subskillSteps;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;
                            console.log(this.subSkills0);

                            /*if(this.subSkills4.length === 0 || this.subSkills4 === null){
                                console.log('4 in icindeyiz');

                                this.subSkills4 = [{subssubskillStepsIdkillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills3.length === 0 || this.subSkills3 === null){
                                console.log('3 in icindeyiz');

                                this.subSkills3 = [{subskillStepsId: -2, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills2.length === 0 || this.subSkills2 === null){
                                console.log('2 in icindeyiz');

                                this.subSkills2 = [{subskillStepsId: -3, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills1.length === 0 || this.subSkills1 === null){
                                console.log('1 in icindeyiz');

                                this.subSkills1 = [{subskillStepsId: -4, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills0.length === 0 || this.subSkills0 === null){
                                console.log('0 in icindeyiz');

                                this.subSkills0 = [{subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkillsnull.length === 0 || this.subSkillsnull === null){
                                this.subSkillsnull = [{subskillStepsId: -6, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }*/

                            this.secondGraderName = this.customizeSecondGraderName();
                            console.log(this.subSkills3);
                            console.log("ULAULAULAULA:");
                            console.log(this.subSkills0);


                            //console.log(res.length);
                        }

                    })
            }else if (this.$store.state.user.userRole === 1) {
                await fetch(`http://localhost:8082/nursapp/app/student/getsubskillstepsforstatistics/${this.subskill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}/${baseRole}`,
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
                            console.log('res in icindeyiz');
                            console.log(res.thirdGrades);
                            console.log(res.thirdGrades.length === 0);
                            console.log(this.subSkills3);


                            this.subskillSteps = res.subskillSteps;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;
                            console.log(this.subSkills0);


                            /*if(this.subSkills4.length === 0 || this.subSkills4 === null){
                                console.log('4 in icindeyiz');

                                this.subSkills4 = [{subssubskillStepsIdkillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills3.length === 0 || this.subSkills3 === null){
                                console.log('3 in icindeyiz');

                                this.subSkills3 = [{subskillStepsId: -2, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills2.length === 0 || this.subSkills2 === null){
                                console.log('2 in icindeyiz');

                                this.subSkills2 = [{subskillStepsId: -3, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills1.length === 0 || this.subSkills1 === null){
                                console.log('1 in icindeyiz');

                                this.subSkills1 = [{subskillStepsId: -4, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills0.length === 0 || this.subSkills0 === null){
                                console.log('0 in icindeyiz');

                                this.subSkills0 = [{subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkillsnull.length === 0 || this.subSkillsnull === null){
                                this.subSkillsnull = [{subskillStepsId: -6, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }*/

                            this.secondGraderName = this.customizeSecondGraderName();
                            console.log(this.subSkills3);

                            //console.log(res.length);
                        }

                    })
            } 
            else if (this.$store.state.user.userRole === 2) {
                await fetch(`http://localhost:8082/nursapp/app/secondgrader/getsubskillstepsforstatistics/${this.subskill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.year}/${baseRole}`,
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
                            console.log('res in icindeyiz');

                            this.subskillSteps = res.subskillSteps;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;
                            /*if(this.subSkills4 == []){
                                this.subSkills4.push({subskillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills3 == []){
                                this.subSkills3.push({subskillId: -2, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills2 == []){
                                this.subSkills2.push({subskillId: -3, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills1 == []){
                                this.subSkills1.push({subskillId: -4, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills0 == []){
                                this.subSkills0.push({subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }*/
                            this.secondGraderName = this.customizeSecondGraderName();
                            console.log(this.subSkills4);

                            console.log(res.length);
                        }

                    })
            }

        },
    },


};
</script>
  
<style scoped>
.indent {
    margin-left: 15px;
    /* veya istediğiniz değer */
}

.header{
    text-align: center;
width: 95%;
margin-top: 3%;
font-weight: bold;
}

.headerr{
    text-align: center;
width: 95%;
margin-top: 3%;
text-decoration: underline;

}
.headerrr{
    text-align: left;
width: 95%;
margin-top: 3%;
}

.checkbox-container {
    display: flex;
    align-items: flex-end;
    justify-content: end;
    background-color: transparent;
    width: 100%;

}
.subskill-checkbox {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 95%;
    background-color: transparent;
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
}
.warning{
    font-weight: bold;
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


.modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    z-index: 1000;
}

.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
}


form {
    display: flex;
    flex-direction: column;
    width: 300px;
    margin: auto;
}

label {
    font-weight: bold;
    margin-top: 15px;
}

input,
select {
    margin-top: 5px;
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
}

button {
    margin-top: 15px;
    padding: 10px;
    background-color: rgb(210, 77, 77);
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

button:hover {
    background-color: rgb(255, 0, 0);
}


</style>
  