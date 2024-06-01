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
                alert("Şifreler farklı, Düzeltip tekrar deneyiniz!");
            }
            else {
                await fetch('http://localhost:8082/nursapp/app/nurse/changepassword',
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
        },
    },
};
</script>
  
<style scoped>
.form {
    margin-top: 60em;
    margin-left: 20em;
    margin-right: 20em;
    margin-bottom: 20em;
    padding-left: 20em;
    padding-bottom: 20em;
    padding-right: 20em;

}

.label {
    color: #ffffff;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
}

.input-field {
    width: 80%;
    margin: 5%;
    padding: 8%;
    background-color: #ffffff;
    color: #333333;
    border-radius: 3em;
    border: none;
}



.page {
    background-image: url('~/assets/backgroundd.jpeg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    display: flex;
}

.action-bar {
    background-color: #19191b;
    color: #fff;
}

.home-panel {
    padding: 20;
}


.profile-container {
    align-items: center;
}

.action-label {
    display: flex;
    text-align: left;
    justify-content: flex-start;
    color: #ffffff;
    font-size: 16em;
    text-wrap: false;
    margin-bottom: 10em;
    margin-right: 50em;
}

.welcome-label {
    color: #fff;
    text-align: left;
    font-size: 24em;
    font-weight: bold;
    width: 80%;

}



.save-button {
    margin-top: 10em;
    background-color: green;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 40%;
    height: 40em;
    text-align: center;

}
</style>