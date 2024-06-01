<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="this.skill.namee" class="action-bar" flat />
        
        <ScrollView class="scroll">
            <StackLayout class="list-container">
                <!-- Header layout with equally spaced columns -->
                <GridLayout :columns="'5*,' + ( bolumInfo.ogrenciNotVerebilirmi ? ' 1, *,' : '') + '1, *,' + (secondGraderNotVeriyormu ? ' 1, *' : '')" rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi"/>
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi ? "4" : "2" ' class="header" />
                    <Label :text="customizeSecondGraderName()" :col="bolumInfo.ogrenciNotVerebilirmi ? (secondGraderNotVeriyormu ? '6' : '6') : (secondGraderNotVeriyormu ? '4' : '4')" class="header" v-if="secondGraderNotVeriyormu"/>
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills" :key="item.subskillId" class="list-group-item">
                    <GridLayout :columns="'5*,' + ( bolumInfo.ogrenciNotVerebilirmi ? ' 1, *,' : '') + '1, *,' + (secondGraderNotVeriyormu ? ' 1, *' : '')" rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi"/>

                        <!-- Scores -->
                        <Label @tap="goSubSkillSteps(item)" :text="item.studentGrade === null ? '-':item.studentGrade.toString()" col="2" class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi"/>
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi ? '3':'1'" class="divider" />

                        <Label @tap="goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-':item.teacherGrade.toString()" :col="bolumInfo.ogrenciNotVerebilirmi ? '4' : '2'" class="list-group-item-score" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi ? (secondGraderNotVeriyormu ? '5' : '3') : (secondGraderNotVeriyormu ? '3' : '1')" class="divider" v-if="secondGraderNotVeriyormu"/>
                        <Label @tap="goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-':item.nurseGrade.toString()"  :col="bolumInfo.ogrenciNotVerebilirmi ? (secondGraderNotVeriyormu ? '6' : '-1') : (secondGraderNotVeriyormu ? '4' : '-1')" class="list-group-item-score" v-if="secondGraderNotVeriyormu"/>
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>
        </ScrollView>
    
    </Page>
</template>
<script scoped>
import AltBeceriBasamagi from '~/views/TeacherViews/AltBeceriBasamagi.vue';

export default {
    props: ['notVer', 'fromStatistic', 'student', 'sinif', 'skill', 'bolumInfo', 'secondGrader', 'secondGraderNotVeriyormu'],
    data() {
        return {
            subSkills: null,
            secondGraderName: null,
        };
    },

    methods: {
        goSubSkillSteps(subSkill) {
            this.$navigateTo(AltBeceriBasamagi, {
                props: { notVer: this.notVer, fromStatistic: this.fromStatistic, student: this.student, sinif: this.sinif, skill: this.skill, subSkill: subSkill, bolumInfo: this.bolumInfo, secondGrader: this.secondGrader, secondGraderNotVeriyormu: this.secondGraderNotVeriyormu },
                // ... other navigation options
            });
        },
        customizeSecondGraderName() {            
            const nameParts = this.secondGrader.secondGraderName.split(' ');

            const initials = nameParts.map(part => part[0]).join('');

            const customizedSecondGraderName = (initials + 'G').toUpperCase();;

            return customizedSecondGraderName;
        },
        async subSkilleriGetir() {
            if(this.$store.state.user.userRole === 0){
                if(this.fromStatistic === false){
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getsubskills/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}`,
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
                        this.subSkills = res;
                        this.secondGraderName = this.customizeSecondGraderName();
                    console.log(this.subSkills);
                    console.log(this.subSkills.length);
                    }

                })
                }else{
                    //statistic sayfasından geldiyse getsubskillsforstatistics
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getsubskillsforstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}`,
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
                        this.subSkills = res;
                        this.secondGraderName = this.customizeSecondGraderName();
                    console.log(this.subSkills);
                    console.log(this.subSkills.length);
                    }

                })
                }
            }else if (this.$store.state.user.userRole === 1){
                if(this.fromStatistic === false){
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getsubskills/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}`,
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
                        this.subSkills = res;
                        this.secondGraderName = this.customizeSecondGraderName();
                    console.log(this.subSkills);
                    console.log(this.subSkills.length);
                    }

                })
                }else{
                    //statistic sayfasından geldiyse getsubskillsforstatistics
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getsubskillsforstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}`,
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
                        this.subSkills = res;
                        this.secondGraderName = this.customizeSecondGraderName();
                    console.log(this.subSkills);
                    console.log(this.subSkills.length);
                    }

                })
                }
            }
            else if ( this.$store.state.user.userRole === 2){
                if(this.fromStatistic === false){
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getsubskills/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}`,
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
                        this.subSkills = res;
                        this.secondGraderName = this.customizeSecondGraderName();
                    console.log(this.subSkills);
                    console.log(this.subSkills.length);
                    }

                })
                }else{
                    //statistic sayfasından geldiyse
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getsubskillsforstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}`,
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
                        this.subSkills = res;
                        this.secondGraderName = this.customizeSecondGraderName();
                    console.log(this.subSkills);
                    console.log(this.subSkills.length);
                    }

                })
                }
            }

        },
        onNavigatedTo() {
            this.subSkilleriGetir();
            if (!this.props) {
                console.log(this.notVer);
                console.log(this.student);
                console.log(this.sinif);
                // Do something with notVer
            } else {
                console.log('Navigation context is not defined.');
            }
        },
        
    },
    mounted() {
    }

};
</script>
    
<style scoped>

.ns-dark .scroll{
margin-bottom: 15;
}

.ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
    text-wrap: true;
}

.ns-dark .list-group-item-score {
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
    color: #b1ebffdb;
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








.ns-light .scroll{
margin-bottom: 15;

}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000;
    text-wrap: true;
}

.ns-light .list-group-item-score {
    /* This class will style the score labels */
    padding: 10;
    color: #000;
    background-color: transparent;
    text-align: center;
    vertical-align: center;
}

.ns-light .list-container {
    margin: 0;
    padding: 0;
}

.ns-light .header {
    color: #000;
    font-weight: bold;
    text-align: center;
    vertical-align: center;
    padding: 10;
}

.ns-light .list-group-item-heading {
    padding: 10;
    color: blue;
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

.ns-light .btn {
    margin: 15;
    background-color: #33c5c9;
    color: #ffffff;
    border-radius: 15em;
    padding: 10px;
    width: 90%;
    height: 50;
    text-align: center;
    vertical-align: center;

}
</style>