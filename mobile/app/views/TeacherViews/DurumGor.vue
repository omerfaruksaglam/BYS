<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar title="Seçilen ögrenci ile bir eylem seçin" class="action-bar" flat/>

                <ScrollView>
                    <FlexboxLayout class="form" flexDirection="column" justifyContent="space-around" alignItems="center">

                        <Image src="~/assets/baskent.png" stretch="aspectFit" class="logo" />
                        <GridLayout columns="*,*" rows="auto" >
                            <Label text="Seçilen ögrenci:  " col="0" row="0" class="header" />
                            <Label col="1" row="0" class="headerr">
                                <FormattedString>
                                    <Span :text="student.name " class=" underlined-text" />
                                    <Span text=" " class=" underlined-text" />
                                    <Span :text="student.surname " class=" underlined-text" />
                                </FormattedString>
                            </Label>
                        </GridLayout>
                        <Button text="Grafiksel Gösterimleri Gör" @tap="goToStatistics" class="btn btn-primary" />
                        <Button text="Notları Gör" @tap="goToSinifSec" class="btn btn-primary" />
                        <Button text="Beceri Karnesi" @tap="karne" class="btn btn-primary" />


                    </FlexboxLayout>
                </ScrollView>
           
    </Page>
</template>
<script scoped>
import SinifSec from '~/views/TeacherViews/SinifSec.vue';
import Statistics from '~/views/TeacherViews/Statistics.vue';
import Report from '~/views/Report/Report.vue';

export default {
    props: ['student', 'bolumInfo'],
    data() {
        return {
            students: null,
            filteredStudents: null,
            searchQuery: '',
        };
    },

    methods: {
        karne() {
            console.log("Karne tıklandı!");
            this.$navigateTo(Report, {
                props: { studentId: this.student.userId, bolumId: this.$store.state.user.bolumId }
            });
        },
        goToSinifSec(){
            this.$navigateTo(SinifSec, {
                props: { notVer: false, student: this.student, bolumInfo: this.bolumInfo},
                // ... other navigation options
              });
        },
        goToStatistics(){
            this.$navigateTo(Statistics, {
                props: { student: this.student, bolumInfo: this.bolumInfo},
                // ... other navigation options
              });
        },
        
        }
    

};
</script>
    
<style scoped>
.ns-dark .label {
    color: #ffffff;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
}

.ns-dark .logo {
    height: 70%;
    width: 70%;
    margin-bottom: 60em;
    margin-top: 60em;
}

.ns-dark .action-bar {
    background-color: #19191B;
    color: #ffffff;
}

.ns-dark .input-field {
    width: 80%;
    margin: 5%;
    padding: 8%;
    background-color: #ffffff;
    color: #333333;
    border-radius: 3em;
    border: none;
}

.ns-dark .btn-primary {
    margin-top: 20em;
    background-color: #4CAF50;
    color: #ffffff;
    border-radius: 5em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;
}

.ns-dark .logoo {
    height: 150em;
    width: 150em;
    margin-left: 90em;
}

.ns-dark .logo {
    height: 80%;
    width: 80%;
    margin-bottom: 20em;
}

.ns-dark .form {
    margin-left: 20em;
    margin-right: 20em;
    margin-bottom: 20em;
    padding-left: 20em;
    padding-bottom: 20em;
    padding-right: 20em;


}

.ns-dark .header {
    display: flex;
    text-align: right;
    color: #ffffff;
    font-size: 18em;
    text-wrap: true;
    margin-bottom: 25em;
    margin-left: 2;
}
.ns-dark .headerr {
    display: flex;
    text-align: left;
    color: #ffffff;
    font-size: 18em;
    text-wrap: true;
    margin-bottom: 25em;
    margin-right: 2;
}
.ns-dark .underlined-text {
  text-decoration: underline;
}

.ns-dark .btn {
    margin-bottom: 5em;
    border-radius: 11em;
    background-color: #08F7FE80;
    width: 90%;
    color: #fff;
}















.ns-light .label {
    color: #000000;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
}

.ns-light .logo {
    height: 70%;
    width: 70%;
    margin-bottom: 60em;
    margin-top: 60em;
}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
}

.ns-light .input-field {
    width: 80%;
    margin: 5%;
    padding: 8%;
    background-color: #000000;
    color: #333333;
    border-radius: 3em;
    border: none;
}

.ns-light .btn-primary {
    margin-top: 20em;
    background-color: rgb(104, 197, 104);
    color: #000000;
    border-radius: 5em;
    padding: 10px;
    width: 80%;
    height: 40em;
    text-align: center;
}

.ns-light .logoo {
    height: 150em;
    width: 150em;
    margin-left: 90em;
}

.ns-light .logo {
    height: 80%;
    width: 80%;
    margin-bottom: 20em;
}

.ns-light .form {
    margin-left: 20em;
    margin-right: 20em;
    margin-bottom: 20em;
    padding-left: 20em;
    padding-bottom: 20em;
    padding-right: 20em;


}

.ns-light .header {
    display: flex;
    text-align: right;
    color: #000000;
    font-size: 18em;
    text-wrap: true;
    margin-bottom: 25em;
    margin-right: 2;
}
.ns-light .headerr {
    display: flex;
    text-align: left;
    color: #000000;
    font-size: 18em;
    text-wrap: true;
    margin-bottom: 25em;
    margin-left: 2;
}
.ns-light .underlined-text {
  text-decoration: underline;

}

.ns-light .btn {
    margin-bottom: 5em;
    border-radius: 11em;
    background-color: #33c5c9;
    width: 90%;
    color: #000000;
}
</style>