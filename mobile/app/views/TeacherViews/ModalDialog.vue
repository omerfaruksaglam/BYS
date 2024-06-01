<template>
  <Page class="page">
    <StackLayout>
      <!--<check-box :checked="kbSira" @checkedChange="kbSira = $event.value" />-->
      <FlexBoxLayout class="space">
        <Label text="Oğrenci Sütununu Gör" class="headerrr" />
        <Switch :checked="kb" @checkedChange="onCheckedChangeKb"  :isEnabled="bolumInfo.ogrenciNotVerebilirmi"/>
      </FlexBoxLayout>

      <FlexBoxLayout class="space">
        <Label text="Ogretim Elemanı Sütununu Gör" class="headerrr" />
        <Switch :checked="oeg" @checkedChange="onCheckedChangeoeg"  :isEnabled="kb === false && hg === false ? false : true"/>
      </FlexBoxLayout>

      <FlexBoxLayout class="space">
        <Label text="Ikincil Puanlandırıcı Sütununu Gör" class="headerrr" />
        <Switch :checked="hg" @checkedChange="onCheckedChangehg"  :isEnabled="secondGraderNotVeriyormu"/>
      </FlexBoxLayout>

      <FlexBoxLayout class="space">
        <Label text="Puana Göre Sıralama" class="headerrr" />
        <Switch :checked="puanliSiralama" @checkedChange="onCheckedChangepuanliSiralama" />
      </FlexBoxLayout>

      <FlexBoxLayout v-if="puanliSiralama" class="space">
        <Label text="Azalan Puan Sırası" class="headerrrr" />
        <Switch :checked="!artanPuanSirasi" @checkedChange="onCheckedChangeartanPuanSirasi"  />
      </FlexBoxLayout>

      <FlexBoxLayout v-if="puanliSiralama" class="space">
        <Label text="Ogrenci Notuna Göre" class="headerrrr" />
        <Switch :checked="kbSira" @checkedChange="onCheckedChangekbSira" :isEnabled="kb === false ? false : true"/>
      </FlexBoxLayout>

      <FlexBoxLayout v-if="puanliSiralama" class="space">
        <Label text="Ogretim Elemanı Notuna Göre" class="headerrrr" />
        <Switch :checked="oegSira" @checkedChange="onCheckedChangeoegSira" :isEnabled="kbSira === false && hgSira === false || oeg === false ? false : true"/>
      </FlexBoxLayout>

      <FlexBoxLayout v-if="puanliSiralama" class="space">
        <Label text="Ikincil Puanlandırıcı Notuna Göre" class="headerrrr" />
        <Switch :checked="hgSira" @checkedChange="onCheckedChangehgSira" :isEnabled="hg === false ? false : true" />
      </FlexBoxLayout>
      
       <!-- İhtiyacınıza göre diğer koşullu label'lar -->
      <Button class="btn" @tap="onCloseTap" text="Kapat" />
    </StackLayout>
  </Page>
</template>

<script>
import { justifyContentProperty } from '@nativescript/core/ui/layouts/flexbox-layout';


export default {
  props: ['kbSira', 'oegSira', 'hgSira', 'oeg', 'kb', 'hg', 'puanliSiralama', 'bolumInfo', 'artanPuanSirasi',  'secondGraderNotVeriyormu'],
  watch: {
    kb(val) {
      if (val === false && this.puanliSiralama === true) {
        console.log('asdffff')
        //this.onCheckedChangeoegSira(true);
      }
     // this.checkOeg();
    },
    hg(val) {
      if (val === false && this.puanliSiralama === true) {
        console.log('asdhggggggg')
        //this.onCheckedChangeoegSira(true);
      }
      //this.checkOeg();
    },

  },
  methods: {
    checkOeg(event) {
            if (this.kb === false && this.hg === false) {
                if(event.value === true){
                  this.oeg = event.value;
                }else{
                  this.oeg = !event.value;
                }
                this.onCheckedChangeoegSira(event);
            }

        },
    onCheckedChangeKb(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'oeg' veri özelliğini günceller
      //console.log('event.value ', event)
      this.kb = event.value;
      if (event.value === false && this.puanliSiralama === true) {
        console.log('asdffff')
        this.oegSiraTrueYap(event);
        
      }
      this.checkOeg(event);
    },
    onCheckedChangeoeg(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'oeg' veri özelliğini günceller
      this.oeg = event.value;
    },
    onCheckedChangehg(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'hg' veri özelliğini günceller
      this.hg = event.value;
      if (event.value === false && this.puanliSiralama === true) {
        console.log('asdffff')
        this.oegSiraTrueYap(event);
        
      }
      this.checkOeg(event);
    },
    onCheckedChangepuanliSiralama(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'hg' veri özelliğini günceller
      this.puanliSiralama = event.value;
    },
    onCheckedChangeartanPuanSirasi(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'hg' veri özelliğini günceller
      this.artanPuanSirasi = !event.value;
    },
    onCloseTap: function () {
      // someData, ana component'e geri göndermek istediğiniz veriyi temsil eder
      // Örnek kullanım: this.$modal.close({ someKey: 'someValue' });
      const returnedData = {
    kbSira: this.kbSira,
    oegSira: this.oegSira,
    hgSira: this.hgSira,
    oeg: this.oeg,
    kb: this.kb,
    hg: this.hg,
    puanliSiralama: this.puanliSiralama,
    artanPuanSirasi: this.artanPuanSirasi,
    // Add more props as needed
  };
  console.log("Returning data:", returnedData);

      this.$modal.close(returnedData);
    },
    onCheckedChangekbSira(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'hg' veri özelliğini günceller
      if(event.value === true){
        console.log('event.dsafasdfasdf ')
        this.kbSira = event.value;
        this.oegSira=!event.value;
        this.hgSira=!event.value;
        //this.selectOnlyOne('kb');
        if(this.kbSira === false && this.hgSira === false){
        this.oegSira = event.value;
      }
       }else{
        console.log('event.dsafasdfasdf false')

        this.kbSira = event.value;
        
        //this.selectOnlyOne('asdf');
        if(this.kbSira === false && this.hgSira === false){
        this.oegSira = !event.value;
      }
       }
      

    },
    oegSiraTrueYap(event){
      if(event.value === true){
        this.oegSira = event.value;
        this.kbSira=!event.value;
        this.hgSira=!event.value;
        
      }else{
        this.oegSira = !event.value;
        this.kbSira=event.value;
        this.hgSira=event.value;
        
      }
    },
    onCheckedChangeoegSira(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'hg' veri özelliğini günceller
      //this.oegSira = event.value;
      //this.selectOnlyOne('oeg');
      if(event.value === true){
        this.oegSira = event.value;
        this.kbSira=!event.value;
        this.hgSira=!event.value;
        if(this.kbSira === false && this.hgSira === false){
        this.oegSira = event.value;
      }
      }else{
        this.oegSira = event.value;
        if(this.kbSira === false && this.hgSira === false){
        this.oegSira = !event.value;
      }
      }
      

    },
    onCheckedChangehgSira(event) {
      // Bu metod, Switch'in değeri değiştiğinde çağrılır ve 'hg' veri özelliğini günceller
      //this.hgSira = event.value;
      //this.selectOnlyOne('hg');
      if(event.value === true){
        this.hgSira = event.value;
        this.kbSira=!event.value;
        this.oegSira=!event.value;
        if(this.kbSira === false && this.hgSira === false){
        this.oegSira = event.value;
      }
      }else{
        this.hgSira = event.value;
        this.kbSira=false;
        this.oegSira=false;
        if(this.kbSira === false && this.hgSira === false){
        this.oegSira = !event.value;
      }
      }
      
    },
   /* selectOnlyOne(siraType) {
      if (siraType === 'kb') {
        this.oegSira = false;
        this.hgSira = false;
        //this.subSkilleriGetir(1);

      } else if (siraType === 'oeg') {
        this.kbSira = false;
        this.hgSira = false;
        //this.subSkilleriGetir(0);

      } else if (siraType === 'hg') {
        this.kbSira = false;
        this.oegSira = false;
        //this.subSkilleriGetir(2);

      }
      if (this.kbSira === false && this.hgSira === false) {
        this.oegSira = true;
        //this.subSkilleriGetir(0);
      }

      .ns-light .space{
  justify-content: space-between;

}
.ns-light .page {
  background-image: url('~/assets/background.jpeg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
}
.ns-light .headerrr {
  font-size: x-large;
  font-weight: bold;
  text-align: left;
  color: #000000;
  margin-left: 10;
  

}
.ns-light .headerrrr {
  font-size: x-large;
  font-weight: bold;
  text-align: left;
  color: #000000;
  margin-left: 50;
  

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


      //statisticler icin puanlar cagrilacak true olan degere gore
    },*/
  }
};
</script>

<style scoped>
.ns-dark .space{
  justify-content: space-between;

}


.ns-dark .headerrr {
  font-size: x-large;
  font-weight: bold;
  text-align: left;
  color: white;
  margin-left: 10;
  

}
.ns-dark .headerrrr {
  font-size: x-large;
  font-weight: bold;
  text-align: left;
  color: white;
  margin-left: 50;
  

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













.ns-light .page {
    background-image: url('~/assets/background.jpeg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    display: flex;
  }
  .ns-light .space{
    justify-content: space-between;

}

.ns-light  .headerrr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
    margin-left: 10;
    
  
  }
  .ns-light  .headerrrr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
    margin-left: 50;
    
  
  }
  
  .ns-light  .btn {
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