<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar title="Beceri seçin" class="action-bar" flat />
        <ScrollView>
            <StackLayout>
                <StackLayout v-for="skill in skills" :key="skill.skillId">
                    <Button :text=" fromStatistic ?  skill.stepNo  + ' - ' + skill.namee  : skill.namee " @tap="goSubSkill(skill)" class="btn btn-primary" />
                </StackLayout>

            </StackLayout>
        </ScrollView>
    </Page>
</template>
<script scoped>
import AltBeceriSec from '~/views/TeacherViews/AltBeceriSec.vue';
import StatisticicinAltBeceri from './StatisticicinAltBeceri.vue';
export default {
    props: ['notVer', 'fromStatistic', 'student', 'sinif', 'bolumInfo'],
    data() {
        return {
            skills: null,
            grader: null,
            graderNotVeriyormu: null,
        };
    },

    methods: {
        
        goSubSkill(skill) { //this.$store.state.user.userRole === 1 && !bolumInfo.ogrenciNotVerebilirmi ? false : this.notVer
            if(!this.notVer){
                this.$navigateTo(StatisticicinAltBeceri, {
                props: {  student: this.student, sinif: this.sinif, skill: skill, bolumInfo: this.bolumInfo, secondGrader: this.grader, secondGraderNotVeriyormu: this.graderNotVeriyormu },
                // ... other navigation options
            });
            }else{
                this.$navigateTo(AltBeceriSec, {
                props: { notVer: this.notVer, fromStatistic: this.fromStatistic, student: this.student, sinif: this.sinif, skill: skill, bolumInfo: this.bolumInfo, secondGrader: this.grader, secondGraderNotVeriyormu: this.graderNotVeriyormu },
                // ... other navigation options
            });
            }
            
       },

        async skilleriGetir() {
            if(this.$store.state.user.userRole === 0){
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
            }else if ( this.$store.state.user.userRole === 1 ){
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
            }
            else if(this.$store.state.user.userRole === 2){
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
        onNavigatedTo() {
            this.skilleriGetir();
            if (!this.props) {
                console.log(this.notVer);
                console.log(this.student);
                console.log(this.sinif);
                // Do something with notVer
            } else {
                console.log('Navigation context is not defined.');
            }
        }
    },
    mounted() {
    }

};
</script>
    
<style scoped>

.ns-dark .list-header {
    font-size: 16;
    font-weight: bold;
    padding: 10;
    margin: 5;
    text-align: left;
    color: #ffffff;
}

.ns-dark .list-item {
    margin-top: 15;
    margin-bottom: 15;
    margin-left: 15;
    margin-right: 15;
    padding: 10;
    background-color: grey;
    border-radius: 5;
    color: #ffffff;
    text-align: center;
}

.ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
}

.ns-dark .home-panel {
    padding: 20;
}

.ns-dark .logo {
    width: 70em;
    height: 70em;
    margin-left: 120em auto;
}

.ns-dark .profile-container {
    align-items: center;
}

.ns-dark .action-label {
    display: flex;
    text-align: center;
    color: #ffffff;
    font-size: 18em;
    text-wrap: true;
    margin-bottom: 10em;
}

.ns-dark .welcome-label {
    color: #fff;
    text-align: center;
    font-size: 16em;
}

.ns-dark .profile-icon {
    width: 180em;
    height: 180pem;
    border-radius: 20em;
    margin: 0em 0;
}

.ns-dark .button-grid {
    margin: 10px 0;
}

.ns-dark .btn {
    margin-top: 10em;
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 15em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;

}










.ns-light .list-header {
    font-size: 16;
    font-weight: bold;
    padding: 10;
    margin: 5;
    text-align: left;
    color: #000000;
}

.ns-light .list-item {
    margin-top: 15;
    margin-bottom: 15;
    margin-left: 15;
    margin-right: 15;
    padding: 10;
    background-color: darkgrey;
    border-radius: 5;
    color: #000000;
    text-align: center;
}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000;
}

.ns-light .home-panel {
    padding: 20;
}

.ns-light .logo {
    width: 70em;
    height: 70em;
    margin-left: 120em auto;
}

.ns-light .profile-container {
    align-items: center;
}

.ns-light .action-label {
    display: flex;
    text-align: center;
    color: #000000;
    font-size: 18em;
    text-wrap: true;
    margin-bottom: 10em;
}

.ns-light .welcome-label {
    color: #000;
    text-align: center;
    font-size: 16em;
}

.ns-light .profile-icon {
    width: 180em;
    height: 180pem;
    border-radius: 20em;
    margin: 0em 0;
}

.ns-light .button-grid {
    margin: 10px 0;
}

.ns-light .btn {
    margin-top: 10em;
    background-color: #33c5c9;
    color: #000000;
    border-radius: 15em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;

}
</style>