<template>
    <Page class="page" @navigatedTo="onNavigatedTo" >
        <ActionBar title="Bir Ögrenci Seçin" class="action-bar" flat/>
        <ScrollView>
            <StackLayout>
                <TextField v-model="searchQuery" hint="Ara ..." @textChange="onSearch" class="search-input" />

                <Label v-if="filteredStudents" text="Öğrenciler" class="list-header" />
                <StackLayout v-for="student in filteredStudents" :key="student.userId" @tap="onStudentTap(student)">
                    <Label :text="`${student.userId} - ${student.name} ${student.surname}`" class="list-item" />
                </StackLayout>

            </StackLayout>
        </ScrollView>
    </Page>
</template>
<script scoped>
import SinifSec from '~/views/TeacherViews/SinifSec.vue';
import DurumGor from '~/views/TeacherViews/DurumGor.vue';
export default {
    props: ['notVer', 'bolumInfo'],
    data() {
        return {
            students: null,
            filteredStudents: null,
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

        onSearch(searchQuery) {
            const searchText = searchQuery.text || searchQuery.value;
            console.log("Arama yapılıyor:", searchText);

            if (!searchText) {

                this.filteredStudents = this.students;
            } else {
                this.filteredStudents = this.handleSearch(searchText);
            }
        },
        

        onStudentTap(student) {
            if (this.notVer) {
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
                });
            }
        },

        async kullanicilarigetir() {
            if(this.$store.state.user.userRole === 0){
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getusers/${this.$store.state.user.bolumId}`,
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
            }else if (this.$store.state.user.userRole === 2){
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getusers/${this.$store.state.user.bolumId}`,
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
        onNavigatedTo() {
            this.kullanicilarigetir();
            if (!this.props) {
                console.log(this.notVer);
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
.ns-dark .search-input {
    margin: 10;
    margin-left: 20;
    margin-right: 20;
    padding: 10;
    border-radius: 10;
    color: white;
    background-color: grey;
    placeholder-color: #CCCCCC;
    /* Diğer stil tanımlamaları */
}

.ns-dark .list-header {
    font-size: 16;
    font-weight: bold;
    padding: 10;
    margin: 5;
    text-align: left;
    color: #ffffff;
}

.ns-dark .list-item {
    margin-top: 5;
    margin-bottom: 5;
    margin-left: 15;
    margin-right: 15;
    padding: 10;
    background-color: grey;
    border-radius: 5;
    color: #ffffff;
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

.ns-dark .number-button {
    margin-top: 30em;
    background-color: transparent;
    border-color: #D32F2F;
    border-width: 8em;
    color: #fff;
    border-radius: 50%;
    font-size: 24em;
    height: 100em;
    width: 100em;
    vertical-align: middle;
    text-align: center;
    line-height: 100em;
}

.ns-dark .exit-button {
    margin-top: 10em;
    background-color: red;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;

}

.ns-dark .btn {
    margin-top: 10em;
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;

}











.ns-light .search-input {
    margin: 10;
    margin-left: 20;
    margin-right: 20;
    padding: 10;
    border-radius: 10;
    color: #000000;
    background-color: darkgrey;
    placeholder-color: #000000;
    /* Diğer stil tanımlamaları */
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
    margin-top: 5;
    margin-bottom: 5;
    margin-left: 15;
    margin-right: 15;
    padding: 10;
    background-color: darkgrey;
    border-radius: 5;
    color: #000000;
}


.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
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
    color: #000000;
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

.ns-light .number-button {
    margin-top: 30em;
    background-color: transparent;
    border-color: #D32F2F;
    border-width: 8em;
    color: #fff;
    border-radius: 50%;
    font-size: 24em;
    height: 100em;
    width: 100em;
    vertical-align: middle;
    text-align: center;
    line-height: 100em;
}

.ns-light .exit-button {
    margin-top: 10em;
    background-color: red;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;

}

.ns-light .btn {
    margin-top: 10em;
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;

}
</style>