<template>
    <Page class="page">
        <ActionBar class="action-bar" text="" flat />
        <ScrollView class="home-panel">
            <FlexboxLayout class="home-panel" flexDirection="column" justifyContent="space-around" alignItems="center">
                <StackLayout class="profile-container">

                    <Label text="Şifre Değiştir" class="welcome-label" />

                    <FlexboxLayout class="form" flexDirection="column" justifyContent="center" alignItems="center">


                        <label class="label">Yeni Şifre</label>
                        <TextField secure="true" v-model="yeniSifre" class="input-field" />
                        <label class="label">Yeni Şifre Tekrar</label>
                        <TextField secure="true" v-model="sifreTekrar" class="input-field" />
                        <Button text="Kaydet" @tap="kaydet" class="save-button" horizontalAlignment="center"
                            verticalAlignment="middle" />
                    </FlexboxLayout>


                </StackLayout>

            </FlexboxLayout>
        </ScrollView>
    </Page>
</template>
  
<script scoped>
export default {
    data() {
        return {

            yeniSifre: '',
            sifreTekrar: '',
        };
    },

    methods: {
        
        async kaydet() {
            if (this.yeniSifre != this.sifreTekrar) {
                console.log("Şifreler farkli");
            }
            else {
                if(this.$store.state.user.userRole === 0){
                    await fetch('http://localhost:8082/nursapp/app/teacher/changepassword',
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                        body: JSON.stringify({
                            "userId": this.$store.state.user.userId,
                            "password": this.$store.state.user.password,
                            "newPassword": this.yeniSifre,
                            "bolumId": this.$store.state.user.bolumId,
                        })

                    }).then(response => {
                        if (response.status == 200) {
                            return response.json();
                        } else {
                            return null;
                        }
                    })
                    
                    .then(data => {
                        if (data != null) {
                            data.token = this.$store.state.user.token;
                            console.log(data);
                            this.$store.commit('LOGIN', data);
                            console.log("Şifre değiştirildi");
                            this.$navigateBack();
                        }
                        else {
                            alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                        }
                    });
                }else if(this.$store.state.user.userRole === 2){
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/changepassword`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                        body: JSON.stringify({
                            "userId": this.$store.state.user.userId,
                            "password": this.$store.state.user.password,
                            "newPassword": this.yeniSifre,
                            "bolumId": this.$store.state.user.bolumId,
                        })

                    }).then(response => {
                        if (response.status == 200) {
                            return response.json();
                        } else {
                            return null;
                        }
                    })
                    
                    .then(data => {
                        if (data != null) {
                            data.token = this.$store.state.user.token;
                            console.log(data);
                            this.$store.commit('LOGIN', data);
                            console.log("Şifre değiştirildi");
                            this.$navigateBack();
                        }
                        else {
                            alert("Şifre değiştirilemedi, lütfen tekrar deneyiniz!");
                        }
                    });
                }

            }
        },
    },
};
</script>
  
<style scoped>
.ns-dark .form {
    margin-top: 60em;
    margin-left: 20em;
    margin-right: 20em;
    margin-bottom: 20em;
    padding-left: 20em;
    padding-bottom: 20em;
    padding-right: 20em;

}

.ns-dark .label {
    color: #ffffff;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
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


.ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
}

.ns-dark .home-panel {
    padding: 20;
}


.ns-dark .profile-container {
    align-items: center;
}


.ns-dark .welcome-label {
    color: #fff;
    text-align: left;
    font-size: 24em;
    font-weight: bold;
    width: 80%;

}


.ns-dark .save-button {
    margin-top: 10em;
    background-color: green;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 40%;
    height: 40em;
    text-align: center;

}















.ns-light .form {
    margin-top: 60em;
    margin-left: 20em;
    margin-right: 20em;
    margin-bottom: 20em;
    padding-left: 20em;
    padding-bottom: 20em;
    padding-right: 20em;

}

.ns-light .label {
    color: #000000;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
}

.ns-light .input-field {
    width: 80%;
    margin: 5%;
    padding: 8%;
    background-color: darkgrey;
    border-color: #000000;
    color: #333333;
    border-radius: 3em;
    border: none;
}


.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
}

.ns-light .home-panel {
    padding: 20;
}


.ns-light .profile-container {
    align-items: center;
}


.ns-light .welcome-label {
    color: #000000;
    text-align: left;
    font-size: 24em;
    font-weight: bold;
    width: 80%;

}


.ns-light .save-button {
    margin-top: 10em;
    background-color: rgb(104, 197, 104);
    color: #000000;
    border-radius: 7em;
    padding: 10px;
    width: 40%;
    height: 40em;
    text-align: center;

}
</style>