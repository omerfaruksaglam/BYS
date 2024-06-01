<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="this.skill.namee" class="action-bar" flat />
        <GridLayout rows="*,12*">
                <GridLayout row="0"  columns="2.8*,*" >
            <Label col="0" :text="'Beceri: ' + skill.namee " class="skilllabel"></Label>
            <Button @tap="openModal" text="Filtrele" col="1"  class="filteleButton"/>
                
            
        </GridLayout>
        <ScrollView class="scroll" v-if="!puanliSiralama" row="1">
            <StackLayout class="list-container">
                <!-- Header layout with equally spaced columns -->
                <GridLayout :columns="'5*,' + ( bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')" rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi  && kb"/>
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi  && kb ? "4" : "2" ' class="header" v-if="oeg" />
                    <Label :text="customizeSecondGraderName()" :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')" class="header" v-if="secondGraderNotVeriyormu && hg"/>
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills" :key="item.subskillId" class="list-group-item">
                    <GridLayout :columns="'5*,' + ( bolumInfo.ogrenciNotVerebilirmi  && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')" rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi  && kb"/>

                        <!-- Scores -->
                        <Label @tap="goSubSkillSteps(item)" :text="item.studentGrade === null ? '-':item.studentGrade.toString()" col="2" class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi  && kb"/>
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3':'1'" class="divider" v-if="oeg" />

                        <Label @tap="goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-':item.teacherGrade.toString()" :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? '4' : '2'" class="list-group-item-score" v-if="oeg" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')" class="divider" v-if="secondGraderNotVeriyormu && hg"/>
                        <Label @tap="goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-':item.nurseGrade.toString()"  :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')" class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>
        </ScrollView>




        <ScrollView class="scroll" v-if="puanliSiralama" row="1" >
            <GridLayout  :rows="subSkillsnull.length !== 0 && subSkillsnull !== null ? '*,*,*,*,*,*,*,*,*,*,*,*' : '*,*,*,*,*,*,*,*,*,*' "><!--,*,*,*,*,*,*-->
            <Label :row="artanPuanSirasi ? 0 : 8" :class="artanPuanSirasi ? 'headerrr' : 'headerr'" text="Puanı 0 Olan Alt Beceriler"  @tap="()=>artanPuanSirasi = !artanPuanSirasi"/><!---->

            <StackLayout :row="artanPuanSirasi ? 1 : 9" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi   && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" v-if="oeg"/>
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills0" :key="item.subskillId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.studentGrade === null ? '-' : item.studentGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score"  v-if="oeg"/>
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"
                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>




            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" :row="artanPuanSirasi ? 2 : 6" class="headerr" text="Puanı 1 Olan Alt Beceriler" /><!---->

            <StackLayout :row="artanPuanSirasi ? 3 : 7" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi  && kb? "4" : "2"' class="header" v-if="oeg" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills1" :key="item.subskillId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi  && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.studentGrade === null ? '-' : item.studentGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi  && kb? '3' : '1'" class="divider"  v-if="oeg" />

                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()"
                            :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? '4' : '2'" class="list-group-item-score"  v-if="oeg" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"
                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>
            



            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" row="4" class="headerr" text="Puanı 2 Olan Alt Beceriler" /><!---->

            <StackLayout row="5" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" v-if="oeg" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills2" :key="item.subskillId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.studentGrade === null ? '-' : item.studentGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi  && kb? '3' : '1'" class="divider" v-if="oeg" />

                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" v-if="oeg" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"
                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>




            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" :row="artanPuanSirasi ? 6 : 2" class="headerr" text="Puanı 3 Olan Alt Beceriler" /><!---->

            <StackLayout :row="artanPuanSirasi ? 7 : 3" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi  && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi  && kb? "4" : "2"' class="header" v-if="oeg" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills3" :key="item.subskillId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi  && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.studentGrade === null ? '-' : item.studentGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" 
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" v-if="oeg" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"
                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>





            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" :row="artanPuanSirasi ? 8 : 0" :class="!artanPuanSirasi ? 'headerrr' : 'headerr'" text="Puanı 4 Olan Alt Beceriler" /><!---->

            <StackLayout :row="artanPuanSirasi ? 9 : 1" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi  && kb? "4" : "2"' class="header" v-if="oeg" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills4" :key="item.subskillId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.studentGrade === null ? '-' : item.studentGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()"
                            :col="bolumInfo.ogrenciNotVerebilirmi  && kb? '4' : '2'" class="list-group-item-score" v-if="oeg" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"
                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>





            <Label v-if="subSkillsnull.length !== 0 && subSkillsnull !== null" @tap="()=>artanPuanSirasi = !artanPuanSirasi" row="10" class="headerr" text="Puanı Olmayan Alt Beceriler" /><!---->

            <StackLayout v-if="subSkillsnull.length !== 0 && subSkillsnull !== null" row="11" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi  && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" :col='bolumInfo.ogrenciNotVerebilirmi  && kb? "4" : "2"' class="header" v-if="oeg" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkillsnull" :key="item.subskillId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi  && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label  :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.studentGrade === null ? '-' : item.studentGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi  && kb? '3' : '1'" class="divider" v-if="oeg" />

                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()"
                            :col="bolumInfo.ogrenciNotVerebilirmi  && kb? '4' : '2'" class="list-group-item-score" v-if="oeg" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"
                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>
            </GridLayout>
        </ScrollView>
        </GridLayout>
       
    </Page>
</template>
<script scoped>
import ssAltBeceri from '~/views/TeacherViews/ssAltBeceri.vue';
import ModalDialog from '~/views/TeacherViews/ModalDialog.vue';
import ModalDialogLight from '~/views/TeacherViews/ModalDialogLight.vue';
import { ApplicationSettings } from '@nativescript/core';
export default {
    props: ['student', 'sinif', 'bolumInfo', 'skill', 'secondGrader', 'secondGraderNotVeriyormu'],
    watch: {

    },
    data() {
        return {
            kbSira: false,
            oegSira: true,
            hgSira: false,
            oeg: true,
            kb: false,
            hg: false,
            puanliSiralama: false,
            subSkills: [],
            subSkillsnull: [],
            subSkills0: [],
            subSkills1: [],
            subSkills2: [],
            subSkills3: [],
            subSkills4: [],
            artanPuanSirasi: true,

            secondGraderName: null,
        };
    },
    
    methods: {
        openModal() {
            console.log("fonksiyonun icindeyiz.")
      if(ApplicationSettings.getString('theme') === 'dark'){
        this.$showModal(ModalDialog, {
    props: {
      kbSira: this.kbSira,
      oegSira: this.oegSira,
      hgSira: this.hgSira,
      oeg: this.oeg,
      kb: this.kb,
      hg: this.hg,
      puanliSiralama: this.puanliSiralama,
      bolumInfo: this.bolumInfo,
      artanPuanSirasi: this.artanPuanSirasi,
      secondGraderNotVeriyormu: this.secondGraderNotVeriyormu
    },
    fullscreen: true, // Tam ekran olup olmayacağını belirler
    animated: true, // Açılış animasyonu olup olmayacağını belirler
    // Modal kapandığında çalışacak fonksiyon
    
  }).then(returnedData => {
    console.log("returning dataaa");
        console.log(returnedData);

      if (returnedData) {
        console.log(returnedData);
        // Update your data properties based on returnedData
        this.kbSira = returnedData.kbSira;
        this.oegSira = returnedData.oegSira;
        this.hgSira = returnedData.hgSira;
        this.oeg = returnedData.oeg;
        this.kb = returnedData.kb;
        this.hg = returnedData.hg;
        this.puanliSiralama = returnedData.puanliSiralama;
        this.artanPuanSirasi = returnedData.artanPuanSirasi;
        // Update more props as needed
        if(this.kbSira === true && this.oegSira === false && this.hgSira === false){
            this.subSkilleriGetir(1);
        }else if(this.kbSira === false && this.oegSira === true && this.hgSira === false){
            this.subSkilleriGetir(0);
        }else if(this.kbSira === false && this.oegSira === false && this.hgSira === true){
            this.subSkilleriGetir(2);
        }
        // Optionally, you can refresh or perform actions based on the new state
      }
  });

      }else{
        this.$showModal(ModalDialog, {
    props: {
      kbSira: this.kbSira,
      oegSira: this.oegSira,
      hgSira: this.hgSira,
      oeg: this.oeg,
      kb: this.kb,
      hg: this.hg,
      puanliSiralama: this.puanliSiralama,
      bolumInfo: this.bolumInfo,
      artanPuanSirasi: this.artanPuanSirasi,
      secondGraderNotVeriyormu: this.secondGraderNotVeriyormu
    },
    fullscreen: true, // Tam ekran olup olmayacağını belirler
    animated: true, // Açılış animasyonu olup olmayacağını belirler
    // Modal kapandığında çalışacak fonksiyon
    
  }).then(returnedData => {
    console.log("returning dataaa");
        console.log(returnedData);

      if (returnedData) {
        console.log(returnedData);
        // Update your data properties based on returnedData
        this.kbSira = returnedData.kbSira;
        this.oegSira = returnedData.oegSira;
        this.hgSira = returnedData.hgSira;
        this.oeg = returnedData.oeg;
        this.kb = returnedData.kb;
        this.hg = returnedData.hg;
        this.puanliSiralama = returnedData.puanliSiralama;
        this.artanPuanSirasi = returnedData.artanPuanSirasi;
        // Update more props as needed
        if(this.kbSira === true && this.oegSira === false && this.hgSira === false){
            this.subSkilleriGetir(1);
        }else if(this.kbSira === false && this.oegSira === true && this.hgSira === false){
            this.subSkilleriGetir(0);
        }else if(this.kbSira === false && this.oegSira === false && this.hgSira === true){
            this.subSkilleriGetir(2);
        }
        // Optionally, you can refresh or perform actions based on the new state
      }
  });
      }
    },
        
    
        
        goSubSkillSteps(subSkill) {
            this.$navigateTo(ssAltBeceri, {
                props: {  student: this.student, sinif: this.sinif, skill: this.skill, subSkill: subSkill, bolumInfo: this.bolumInfo, secondGrader: this.secondGrader, secondGraderNotVeriyormu: this.secondGraderNotVeriyormu },
                // ... other navigation options
            });
        },
        customizeSecondGraderName() {

            const nameParts = this.secondGrader.secondGraderName.split(' ');

            const initials = nameParts.map(part => part[0]).join('');

            const customizedSecondGraderName = (initials + 'G').toUpperCase();;

            return customizedSecondGraderName;
        },
        async subSkilleriGetir(basedRole) {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getsubskillsforstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}/${basedRole}`,
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
                            //console.log('res in icindeyiz');
                            //console.log(res.thirdGrades);
                            //console.log(res.thirdGrades.length === 0);
                            //console.log(this.subSkills3);


                            this.subSkills = res.subskills;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;

                            /*if(this.subSkills4.length === 0 || this.subSkills4 === null){
                                console.log('4 in icindeyiz');

                                this.subSkills4 = [{subskillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills3.length === 0 || this.subSkills3 === null){
                                console.log('3 in icindeyiz');

                                this.subSkills3 = [{subskillId: -2, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills2.length === 0 || this.subSkills2 === null){
                                console.log('2 in icindeyiz');

                                this.subSkills2 = [{subskillId: -3, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills1.length === 0 || this.subSkills1 === null){
                                console.log('1 in icindeyiz');

                                this.subSkills1 = [{subskillId: -4, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills0.length === 0 || this.subSkills0 === null){
                                console.log('0 in icindeyiz');

                                this.subSkills0 = [{subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkillsnull.length === 0 || this.subSkillsnull === null){
                                this.subSkillsnull = [{subskillId: -6, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }*/

                            this.secondGraderName = this.customizeSecondGraderName();
                            //console.log(this.subSkills3);

                            //console.log(res.length);
                        }

                    })
            }else if (this.$store.state.user.userRole === 1) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getsubskillsforstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}/${basedRole}`,
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
                            //console.log('res in icindeyiz');
                            //console.log(res.thirdGrades);
                            //console.log(res.thirdGrades.length === 0);
                            //console.log(this.subSkills3);


                            this.subSkills = res.subskills;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;

                            /*if(this.subSkills4.length === 0 || this.subSkills4 === null){
                                console.log('4 in icindeyiz');

                                this.subSkills4 = [{subskillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills3.length === 0 || this.subSkills3 === null){
                                console.log('3 in icindeyiz');

                                this.subSkills3 = [{subskillId: -2, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills2.length === 0 || this.subSkills2 === null){
                                console.log('2 in icindeyiz');

                                this.subSkills2 = [{subskillId: -3, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills1.length === 0 || this.subSkills1 === null){
                                console.log('1 in icindeyiz');

                                this.subSkills1 = [{subskillId: -4, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills0.length === 0 || this.subSkills0 === null){
                                console.log('0 in icindeyiz');

                                this.subSkills0 = [{subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkillsnull.length === 0 || this.subSkillsnull === null){
                                this.subSkillsnull = [{subskillId: -6, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null}];
                            }*/

                            this.secondGraderName = this.customizeSecondGraderName();
                            //console.log(this.subSkills3);

                            //console.log(res.length);
                        }

                    })
            }
             else if (this.$store.state.user.userRole === 2) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getsubskillsforstatistics/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${this.skill.skillId}/${basedRole}`,
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
                            //console.log('res in icindeyiz');
                            //console.log(res.thirdGrades);
                           // console.log(res.thirdGrades.length === 0);
                            //console.log(this.subSkills3);


                            this.subSkills = res.subskills;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;

                            

                            this.secondGraderName = this.customizeSecondGraderName();
                            //console.log(this.subSkills3);

                            //console.log(res.length);
                        }

                    })
            }

        },
        onNavigatedTo() {
            
            this.subSkilleriGetir(0);
            if (!this.props) {
                //console.log(this.notVer);
                //console.log(this.student);
                //console.log(this.sinif);
                // Do something with notVer
            } else {
                //console.log('Navigation context is not defined.');
            }
        },

    },
    mounted() {
        this.bolumInfo.ogrenciNotVerebilirmi ? this.kb = true : this.kb = false;
        this.secondGraderNotVeriyormu ? this.hg = true : this.hg = false;
    }

};
</script>
    
<style scoped>
.ns-dark .skilllabel{
    font-size: 16;
    font-weight: bold;
    text-align: left;
    color: white;
    text-wrap: true;
    margin-left: 10;
}

.ns-dark .headerr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: white;
    text-decoration: underline;
    margin-left: 10;
    margin-top: 60;

}
.ns-dark .headerrr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: white;
    text-decoration: underline;
    margin-left: 10;
    margin-top: 15;

}

.ns-dark .scroll {
    margin-bottom: 15;
}

.ns-dark .action-bar {
    background-color: #19191b;
    color: #fff;
    text-wrap: true;
}

.ns-dark .filteleButton{
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 15em;
    
    text-align: center;
    vertical-align: center;
   
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
















.ns-light .skilllabel{
    font-size: 16;
    font-weight: bold;
    text-align: left;
    color: #000000;
    text-wrap: true;
    margin-left: 10;
}

.ns-light .headerr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
    text-decoration: underline;
    margin-left: 10;
    margin-top: 60;

}
.ns-light .headerrr {
    font-size: x-large;
    font-weight: bold;
    text-align: left;
    color: #000000;
    text-decoration: underline;
    margin-left: 10;
    margin-top: 15;

}

.ns-light .scroll {
    margin-bottom: 15;
}

.ns-light .action-bar {
    background-color: #dddddd;
    color: #000000;
    text-wrap: true;
}

.ns-light .filteleButton{
    background-color: #33c5c9;
    color: #000000;
    border-radius: 15em;
    
    text-align: center;
    vertical-align: center;
   
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
    color: #000000;
    border-radius: 15em;
    padding: 10px;
    width: 90%;
    height: 50;
    text-align: center;
    vertical-align: center;

}
</style>