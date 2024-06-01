<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="this.subSkill.name" class="action-bar" />
        <GridLayout :rows="equipments !== null && equipments.length > 0 ? 'auto, 5*, auto, 26*, auto' : 'auto ,auto, auto'">

            <Label text="Malzeme Listesi:" row="0" class="equipment-list-title" v-if="equipments !== null && equipments.length > 0" />

            <ScrollView row="1" v-if="equipments !== null && equipments.length > 0" class="equipment-list">
                <StackLayout>
                    <Label v-for="equipment in equipments" :key="equipment.equipmentId" :text="equipment.name"
                        class="equipment-item" />
                </StackLayout>
            </ScrollView>
            <GridLayout
                :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi ? ' 1, *,' : '') + '1, *,' + (secondGraderNotVeriyormu ? ' 1, *' : '')"
                rows="auto, auto" class="list-header" :row="equipments !== null && equipments.length > 0 ? 2 : 0">
                <Label text=" " col="0" row="0" />
                <Label text="KB" col="2" row="0" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi" />
                <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi ? "4" : "2"' row="0" class="header" />
                <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi ? (secondGraderNotVeriyormu ? '6' : '6') : (secondGraderNotVeriyormu ? '4' : '4')"
                    row="0" class="header" v-if="secondGraderNotVeriyormu" />
                <StackLayout class="horizontal-divider" row="1" col="0" />
                <StackLayout class="horizontal-divider" row="1" col="1" />
                <StackLayout class="horizontal-divider" row="1" col="2" />
                <StackLayout class="horizontal-divider" row="1" col="3"
                    v-if="bolumInfo.ogrenciNotVerebilirmi || secondGraderNotVeriyormu" />
                <StackLayout class="horizontal-divider" row="1" col="4"
                    v-if="bolumInfo.ogrenciNotVerebilirmi || secondGraderNotVeriyormu" />
                <StackLayout class="horizontal-divider" row="1" col="5"
                    v-if="bolumInfo.ogrenciNotVerebilirmi && secondGraderNotVeriyormu" />
                <StackLayout class="horizontal-divider" row="1" col="6"
                    v-if="bolumInfo.ogrenciNotVerebilirmi && secondGraderNotVeriyormu" />
            </GridLayout>
            <ScrollView :row="equipments !== null && equipments.length > 0 ? 3 : 1">
                <StackLayout class="list-container">
                    <!-- Header layout with conditionally displayed columns -->


                    <!-- List items -->
                    <StackLayout v-for="item in subSkillSteps" :key="item.subSkillStepsId" class="list-group-item">
                        <GridLayout
                            :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi ? ' 1, *,' : '') + '1, *,' + (secondGraderNotVeriyormu ? ' 1, *' : '')"
                            rows="auto">
                            <Label :text="item.name" col="0" class="list-group-item-heading" />
                            <!-- performGrade -->
                            <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi" />
                            <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                                class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && !item.isHead"
                                @tap="notVer && $store.state.user.userRole === 1 && !item.isHead ? pickGrade(item, 'performGrade', item.performGrade) : !item.isHead && item.performGrade !== null ? findGraderAndShow(item, 1) :  null" />
                            <!-- teacherGrade -->
                            <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi ? '3' : '1'" class="divider" />
                            <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()"
                                :col="bolumInfo.ogrenciNotVerebilirmi ? '4' : '2'" class="list-group-item-score"
                                @tap="notVer && $store.state.user.userRole === 0 && !item.isHead ? null : !item.isHead && item.teacherGrade !== null ? findGraderAndShow(item, 0) :  null"
                                v-if="!item.isHead" />
                            <!-- nurseGrade -->
                            <StackLayout
                                :col="bolumInfo.ogrenciNotVerebilirmi ? (secondGraderNotVeriyormu ? '5' : '3') : (secondGraderNotVeriyormu ? '3' : '1')"
                                class="divider" v-if="secondGraderNotVeriyormu" />
                            <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                                :col="bolumInfo.ogrenciNotVerebilirmi ? (secondGraderNotVeriyormu ? '6' : '-1') : (secondGraderNotVeriyormu ? '4' : '-1')"
                                class="list-group-item-score" v-if="secondGraderNotVeriyormu && !item.isHead"
                                @tap="notVer && $store.state.user.userRole === 2 && !item.isHead ? null : !item.isHead && item.nurseGrade !== null ? findGraderAndShow(item, 2) :  null" />
                        </GridLayout>
                        <StackLayout class="horizontal-divider" />
                    </StackLayout>
                </StackLayout>
            </ScrollView>
            <Button v-if="notVer" text="Kaydet" :row="equipments && equipments.length > 0 ? 4 : 2" @tap="kaydet"
                class="btn btn-primary" />
            <StackLayout v-else :row="equipments && equipments.length > 0 ? 4 : 2" class="horizontal-dividerr" />
        </GridLayout>
    </Page>
</template>

<script scoped>
import { Dialogs } from '@nativescript/core';



export default {
    props: ['notVer', 'fromStatistic','student', 'sinif', 'skill', 'subSkill', 'bolumInfo', 'secondGrader', 'secondGraderNotVeriyormu'],
    data() {
        return {
            subSkillSteps: null,
            equipments: null,
            changedGrades: [],
            gradeOptions: ['0', '1', '2', '3', '4'],
            graderInfo: null,
        };
    },
    methods: {
        async yonlendirmeKB(item, gradeType, performgrade, graderRole){
            //@tap="notVer && $store.state.user.userRole === 1 && !item.isHead ? pickGrade(item, 'performGrade', item.performGrade) : null"
            if(this.notVer && !item.isHead){
                 if( this.$store.state.user.userRole === 1){

                 }
        }else if( !this.notVer && this.$store.state.user.userRole === 0 && !item.isHead){
            return true;
    }
},
        /*
        await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getgrader/${subskillsteps.}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                });
        */
        async findGraderAndShow(subskillsteps, graderRole) {
            if (graderRole === 0) {
                if (this.$store.state.user.userRole === 0 && this.notVer === false) {
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        })
                }
                else if (this.$store.state.user.userRole === 1) {



                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + subskillsteps.gradeDateTeacher.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });



                } else if (this.$store.state.user.userRole === 2) {



                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + subskillsteps.gradeDateTeacher.substring(0,10) + " tarihinde verilmiştir.");
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });
                }



            } else if (graderRole === 1) {


                if (this.$store.state.user.userRole === 1 && this.notVer === false) {
                    var localDateTimeString = new Date(subskillsteps.performDate).toLocaleString();
                    var date = new Date(localDateTimeString);
                    var year = date.getFullYear();
                    var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                    var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                    var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                    var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    //alert("Bu puan sizin tarafınızdan " + subskillsteps.gradeDateTeacher.substring(0,10) + " tarihinde verilmiştir.");
                    Dialogs.alert({
                        title: "Puan Bilgisi",
                        message: "Bu puan sizin tarafınızdan " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                        okButtonText: "Tamam"
                    });

                } else if (this.$store.state.user.userRole !== 1) {
                    var localDateTimeString = new Date(subskillsteps.performDate).toLocaleString();
                    var date = new Date(localDateTimeString);
                    var year = date.getFullYear();
                    var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                    var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                    var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                    var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    //var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                    //alert("Bu puan "+ this.student.name + " " + this.student.surname+" adlı öğrenci tarafından " + subskillsteps.performDate.substring(0,10) + " tarihinde verilmiştir.");
                    Dialogs.alert({
                        title: "Puan Bilgisi",
                        message: "Bu puan " + this.student.name + " " + this.student.surname + " adlı öğrenci tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                        okButtonText: "Tamam"
                    });
                }


            } else if (graderRole === 2) {
                if (this.$store.state.user.userRole === 0) {



                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + subskillsteps.gradeDateNurse.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();

                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " + this.secondGrader.secondGraderName + " tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });




                } else if (this.$store.state.user.userRole === 1) {




                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + subskillsteps.gradeDateNurse.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " + this.secondGrader.secondGraderName + " tarafından " +  day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });




                } else if (this.$store.state.user.userRole === 2 && this.notVer === false) {
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
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
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + subskillsteps.gradeDateNurse.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " + this.secondGrader.secondGraderName +  " tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
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
        pickGrade(item, gradeType, value) {
            const options = {
                title: "Puan Seçin",
                message: "Lütfen puan seçin:",
                cancelButtonText: "İptal",
                actions: this.gradeOptions.map(u => u.toString())
            };
            action(options).then((result) => {
                if (result !== "İptal") {
                    this.updateChangedGrades(item, gradeType, result);
                    if (gradeType === 'performGrade') {
                        item.performGrade = result;
                    }
                    else if (gradeType === 'teacherGrade') {
                        item.teacherGrade = result;
                    }
                    else {
                        item.nurseGrade = result;
                    }
                }
            });
        },
        updateChangedGrades(item, gradeType, value) {
            // Update logic here, including setting item's grade to selected value and updating changedGrades
            // Then update changedGrades array
            const existing = this.changedGrades.find(changed => changed.subSkillStepsId === item.subskillStepsId);
            if (existing) {
                    existing.performGrade = value;
                
                
                
            }
            else {
                if (gradeType === 'performGrade' && parseInt(value, 10) !== item.performGrade) {
                    const newItem = { subSkillStepsId: item.subskillStepsId, performGrade: parseInt(value, 10), studentId: this.student.userId, bolumId: this.$store.state.user.bolumId, sinif: this.sinif };
                    this.changedGrades.push(newItem);
                }
                
            }
            console.log(this.changedGrades);
            console.log('asdffdsafsdafsadfadfsadfsfdasfdsfadsadfs');
        },
        async subSkillBasamaklariniGetir() {
            
                
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getsubskillsteps/${this.subSkill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}`, {
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
                        this.subSkillSteps = res.subskillStepsWithGrades;
                        this.equipments = res.equipments;
                        console.log('res:');
                        console.log(res);
                        console.log('subskills:');
                        console.log(this.subSkillSteps);
                        console.log('equipments:');
                        console.log(this.equipments);
                    });
               
            
            
        },
        onNavigatedTo() {
            this.subSkillBasamaklariniGetir();
        },
        async kaydet() {
            if (this.$store.state.user.userRole === 0 && this.changedGrades.length > 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/savegrades`, {
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
                        this.$navigateBack();
                    }
                });
            }
            else if (this.$store.state.user.userRole === 1 && this.changedGrades.length > 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/savegrades`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + this.$store.state.user.token
                    },
                    body: JSON.stringify({ "newGrades": this.changedGrades})
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
                        this.$navigateBack();
                    }
                });
            }
            else if (this.$store.state.user.userRole === 2 && this.changedGrades.length > 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/savegrades`, {
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
                        this.$navigateBack();
                    }
                });
            }
        },
    },
    mounted() {
    },
};
</script>

    
<style scoped>
.ns-dark .equipment-list {
    background-color: transparent;
}

.ns-dark .equipment-list-title {
    color: #fff;
    font-weight: bold;
    padding: 3;
    padding-left: 7;

    /* Diğer stil ayarlarınız */
}

.ns-dark .equipment-item {
    text-align: left;
    /* Bu, metni sola yaslanmış (italik) yapar */
    color: #fff;
    padding: 3;
    padding-left: 7;
    /* Diğer stil ayarlarınız */
}

.ns-dark .action-bar {
    background-color: #19191b;
    color: #ffffff;
    text-wrap: true;
}

.ns-dark .list-group-item-score {
    /* This class will style the score labels */
    padding: 10;
    color: #fff;
    background-color: transparent;
    text-align: center;
    vertical-align: center;
}

.ns-dark .list-container {
    margin: 0;
    padding: 0;
}

.ns-dark .header {
    color: #fff;
    font-weight: bold;
    text-align: center;
    vertical-align: center;
    padding: 10;
}

.ns-dark .list-group-item-heading {
    padding: 10;
    color: #fff;
    background-color: transparent;
    text-wrap: true;
    font-size: 13%;
}

.ns-dark .list-group-item {
    background-color: transparent;
}

.ns-dark .divider {
    background-color: #ffffff;
}

.ns-dark .horizontal-divider {
    height: 2;
    background-color: #ffffff;
    width: 100%;
}

.ns-dark .horizontal-dividerr {
    height: 2;
    background-color: transparent;
    width: 100%;
}

.ns-dark .btn {
    margin: 15;
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 15em;
    padding: 10px;
    width: 90%;
    height: 50;
    text-align: center;
    vertical-align: center;

}











.ns-light .equipment-list {
    background-color: transparent;
}

.ns-light .equipment-list-title {
    color: #000000;
    font-weight: bold;
    padding: 3;
    padding-left: 7;

    /* Diğer stil ayarlarınız */
}

.ns-light .equipment-item {
    text-align: left;
    /* Bu, metni sola yaslanmış (italik) yapar */
    color: #000000;
    padding: 3;
    padding-left: 7;
    /* Diğer stil ayarlarınız */
}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
    text-wrap: true;
}

.ns-light .list-group-item-score {
    /* This class will style the score labels */
    padding: 10;
    color: #000000;
    background-color: transparent;
    text-align: center;
    vertical-align: center;
}

.ns-light .list-container {
    margin: 0;
    padding: 0;
}

.ns-light .header {
    color: #000000;
    font-weight: bold;
    text-align: center;
    vertical-align: center;
    padding: 10;
}

.ns-light .list-group-item-heading {
    padding: 10;
    color: #000000;
    background-color: transparent;
    text-wrap: true;
    font-size: 13%;
}

.ns-light .list-group-item {
    background-color: transparent;
}

.ns-light .divider {
    background-color: #000000;
}

.ns-light .horizontal-divider {
    height: 2;
    background-color: #000000;
    width: 100%;
}

.ns-light .horizontal-dividerr {
    height: 2;
    background-color: transparent;
    width: 100%;
}

.ns-light .btn {
    margin: 15;
    background-color: #33c5c9;
    color: #000000;
    border-radius: 15em;
    padding: 10px;
    width: 90%;
    height: 50;
    text-align: center;
    vertical-align: center;

}
</style>