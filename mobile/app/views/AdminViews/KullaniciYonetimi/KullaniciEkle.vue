<template>
    <Page class="page">
        <ActionBar class="action-bar" />
        <ScrollView>
            <FlexboxLayout class="form-container" flexDirection="column" justifyContent="space-around" alignItems="center">
                <Label class="label2">Kullanıcı Oluştur</Label>
                <StackLayout>
                    <Label class="label">Adı</Label>
                    <TextField class="input-field" v-model="ad" />
                </StackLayout>

                <StackLayout>
                    <Label class="label">Soyadı</Label>
                    <TextField class="input-field" v-model="soyad" />
                </StackLayout>
                <StackLayout>
                    <Label class="label">Numarası</Label>
                    <TextField class="input-field" keyboardType="number" v-model="numara" />
                </StackLayout>
                <StackLayout>
                    <Label class="label">Rolü</Label>
                    <Button :text="seciliRol" @tap="showRols" class="input-field" />
                </StackLayout>
                <StackLayout>
                    <Label class="label">Şifre</Label>
                    <TextField class="input-field" secure="true" v-model="sifre" />
                </StackLayout>
                <StackLayout>
                    <Label class="label">Kayit Tarihi</Label>
                    <TextField class="input-field" hint="yyyy-aa-gg" v-model="kayitTarihi" />
                </StackLayout>
                <Button text="Kullanıcıyı Kaydet" @tap="saveUser" class="save-button" />
            </FlexboxLayout>
        </ScrollView>
    </Page>
</template>

<script>



export default {
    data() {
        return {
            ad: "",
            soyad: "",
            numara: "",
            seciliRol: "Rol Seçin",
            rolId: null,
            sifre: "",
            kayitTarihi: "",
        };
    },
    methods: {
        async saveUser() {
            const dateRegex = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/;
            this.numara = parseInt(this.numara);
            console.log(this.numara + " " + this.kayitTarihi + " " + dateRegex.test(this.kayitTarihi));

            if (this.ad && this.soyad && !isNaN(this.numara) && this.rolId != null && this.sifre && this.kayitTarihi && dateRegex.test(this.kayitTarihi)) {
                try {
                    const response = await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/admin/register`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                        body: JSON.stringify({
                            "userId": this.numara,
                            "name": this.ad,
                            "surname": this.soyad,
                            "kayit_tarihi": this.kayitTarihi,
                            "password": this.sifre,
                            "userRole": this.rolId,
                            "bolumId": this.$store.state.user.bolumId,
                            "isActive": true,
                            "islem_yapan_admin": this.$store.state.user.userId
                        })
                    });

                    const data = await response.json();
                    console.log(data);
                    console.log(this.kayitTarihi);
                    if (data.userId == this.numara) {
                        console.log("Kullanici basariyla kaydedildi!");
                        this.$navigateBack()
                    } else {
                        console.log("Kullanici kaydedilemedi!");
                    }
                } catch (error) {
                    console.error("Kayıt işlemi sırasında hata oluştu: ", error);
                }
            } else {
                console.log("Giriş bilgilerini kontrol edin!");
            }
        },
        showRols() {
            const options = {
                title: "Rol Seç",
                message: "Lütfen yeni kullanicinin kaydini seçin:",
                cancelButtonText: "İptal",
                actions: [
                    "Öğrenci",
                    "Öğretim elemanı",
                    "Hemşire",
                    "Yönetici"
                ],
            };

            action(options).then((result) => {
                if (result !== "İptal") {
                    if (result == "Öğrenci") {
                        this.rolId = 1;
                        this.seciliRol = "Öğrenci";
                        console.log(this.seciliRol + " " + this.rolId)
                    } else if (result == "Öğretim elemanı") {
                        this.rolId = 0;
                        this.seciliRol = "Öğretim elemanı";
                        console.log(this.seciliRol + " " + this.rolId)

                    } else if (result == "Hemşire") {
                        this.rolId = 2;
                        this.seciliRol = "Hemşire";
                        console.log(this.seciliRol + " " + this.rolId)

                    } else if (result == "Yönetici") {
                        this.rolId = 3;
                        this.seciliRol = "Yönetici";
                        console.log(this.seciliRol + " " + this.rolId)

                    } else {
                        this.rolId = null;
                        this.seciliRol = "Rol Seçin";
                        console.log(this.seciliRol + " " + this.rolId)

                    }
                    console.log(result);
                }
            });
        },
    },
};
</script>
  
<style scoped>
.ns-light .label {
    color: #000;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
}

.ns-light .label2 {
    color: #000;
    font-size: 24;
    font-weight: bold;
    width: 80%;
    margin-bottom: 5em;
    text-align: center;
}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000;
}

.ns-light .form-container {
    padding: 20;
}

.ns-light .input-field {
    width: 80%;
    height: 90%;
    margin: 5%;
    padding: 8%;
    background-color: darkgray;
    color: #000;
    border-radius: 3em;
    border: none;
}

.ns-light .save-button {
    margin-top: 10em;
    background-color: #33c5c9;
    color: #000;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;

}









.ns-dark .label {
    color: #ffffff;
    font-size: 16;
    width: 80%;
    margin-bottom: 5em;
    text-align: left;
}

.ns-dark .label2 {
    color: #ffffff;
    font-size: 24;
    font-weight: bold;
    width: 80%;
    margin-bottom: 5em;
    text-align: center;
}

.ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
}

.ns-dark .form-container {
    padding: 20;
}

.ns-dark .input-field {
    width: 80%;
    height: 90%;
    margin: 5%;
    padding: 8%;
    background-color: #ffffff;
    color: #333333;
    border-radius: 3em;
    border: none;
}

.ns-dark .save-button {
    margin-top: 10em;
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 7em;
    padding: 10px;
    width: 60%;
    height: 40em;
    text-align: center;

}
</style>