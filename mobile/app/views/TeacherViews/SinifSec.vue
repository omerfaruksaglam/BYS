<template>
  <Page class="page">
    <ActionBar class="action-bar" title="" flat />
    <ScrollView class="home-panel">
      <FlexboxLayout
        class="home-panel"
        flexDirection="column"
        justifyContent="center"
        alignItems="center"
      >
        <Label :text="student.name + textt" row="1" col="0" class="header" />

        <GridLayout
          v-for="row in Math.ceil(bolumInfo.yilSayisi / 2)"
          :key="row"
          columns="*,*"
          rows="auto"
          class="button-grid"
        >
          <Button
            v-for="col in 2"
            :key="col"
            :text="row * 2 - 2 + col <= bolumInfo.yilSayisi ? row * 2 - 2 + col : ''"
            :col="col - 1"
            class="number-button"
            @tap="eylemeGit(row * 2 - 2 + col)"
            v-if="row * 2 - 2 + col <= bolumInfo.yilSayisi"
          />
        </GridLayout>
      </FlexboxLayout>
    </ScrollView>
  </Page>
</template>

<script>
import BeceriSec from '~/views/TeacherViews/BeceriSec.vue';

export default {
  props: ['notVer', 'student', 'bolumInfo'],
  data() {
    return {
      textt: this.notVer
        ? ' adlı öğrenciye not vermek için lütfen sınıf seçiniz'
        : ' adlı öğrenciye ait notları görmek için lütfen sınıf seçiniz',
    };
  },
  methods: {
    eylemeGit(sinif) {
      if (sinif <= this.bolumInfo.yilSayisi) {
        this.$navigateTo(BeceriSec, {
          props: { student: this.student, fromStatistic: false, sinif: sinif, notVer: this.notVer, bolumInfo: this.bolumInfo },
        });
      }
    },
  },
};
</script>
  <style scoped>
  
 .ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
  }
  
  .ns-dark .home-panel {
    padding: 20;
  }
  
  .ns-dark .button-grid {
    margin: 10px 0;
  }
  
  .ns-dark .number-button {
  
  
    margin-top: 80em;
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

  .ns-dark .header {
    display: flex;
    text-align: center;
    color: #ffffff;
    font-size: 20em;
    text-wrap: true;
}
.ns-dark .underlined-text {
  text-decoration: underline;
}













.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
  }
  
  .ns-light .home-panel {
    padding: 20;
  }
  
  .ns-light .button-grid {
    margin: 10px 0;
  }
  
  .ns-light .number-button {
  
  
    margin-top: 80em;
    background-color: transparent;
    border-color: #eb4c4c;
    border-width: 8em;
    color: #000000;
    border-radius: 50%;
    font-size: 24em;
    height: 100em;
    width: 100em;
    vertical-align: middle;
    text-align: center;
    line-height: 100em;
  }

  .ns-light .header {
    display: flex;
    text-align: center;
    color: #000000;
    font-size: 20em;
    text-wrap: true;
}
.ns-light .underlined-text {
  text-decoration: underline;
}

</style>