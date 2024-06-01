<template>
    <Page class="page" @navigatedTo="onNavigatedTo">
        <ActionBar :title="skill.namee" class="action-bar" flat />


        <GridLayout :rows="!puanliSiralama ? 'auto,auto,26*' : 'auto,26*'">
            <GridLayout row="0"  columns="2.8*,*" >
            <Label col="0" :text="'Alt Beceri: ' + subSkill.name " class="skilllabel"></Label>
            <Button @tap="openModal" text="Filtrele" col="1"  class="filteleButton"/>
                
            
        </GridLayout>

            
            <GridLayout
                :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                rows="auto, auto" class="list-header" row="1" v-if="!puanliSiralama">
                <Label text=" " col="0" row="0" />
                <Label text="KB" col="2" row="0" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' row="0" class="header" />
                <Label :text="customizeSecondGraderName()"
                :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                    row="0" class="header" v-if="secondGraderNotVeriyormu && hg " />
                <StackLayout class="horizontal-divider" row="1" col="0" />
                <StackLayout class="horizontal-divider" row="1" col="1" />
                <StackLayout class="horizontal-divider" row="1" col="2" />
                <StackLayout class="horizontal-divider" row="1" col="3"
                    v-if="bolumInfo.ogrenciNotVerebilirmi && kb || secondGraderNotVeriyormu  && hg" />
                <StackLayout class="horizontal-divider" row="1" col="4"
                    v-if="bolumInfo.ogrenciNotVerebilirmi && kb || secondGraderNotVeriyormu  && hg" />
                <StackLayout class="horizontal-divider" row="1" col="5"
                    v-if="bolumInfo.ogrenciNotVerebilirmi && kb && oeg && secondGraderNotVeriyormu  && hg" />
                <StackLayout class="horizontal-divider" row="1" col="6"
                    v-if="bolumInfo.ogrenciNotVerebilirmi && kb && hg && secondGraderNotVeriyormu  && hg" />
            </GridLayout>
            <ScrollView row="2" v-if="!puanliSiralama">
                <StackLayout class="list-container">
                    <!-- Header layout with conditionally displayed columns -->


                    <!-- List items -->
                    <StackLayout v-for="item in subSkillSteps" :key="item.subSkillStepsId" class="list-group-item">
                        <GridLayout
                            :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu  && hg? ' 1, *' : '')"
                            rows="auto">
                            <Label v-if="item.isHead":text="item.name" col="0" class="list-group-item-headingg" />
                            <Label v-else :text="item.name" col="0" class="list-group-item-heading" />
                            <!-- performGrade -->
                            <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                            <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                                class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb"
                                @tap="notVer && $store.state.user.userRole === 1 && !item.isHead ? null : !item.isHead && item.performGrade !== null ? findGraderAndShow(item, 1) :  null" />
                            <!-- teacherGrade -->
                            <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />
                            <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                                :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score"
                                @tap="notVer && $store.state.user.userRole === 0 && !item.isHead ? null : !item.isHead && item.teacherGrade !== null ? findGraderAndShow(item, 0) :  null"
                                 />
                            <!-- nurseGrade -->
                            <StackLayout
                            :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                                 class="divider" v-if="secondGraderNotVeriyormu && hg" />
                            <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                            :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                                class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg && !item.isHead"
                                @tap="notVer && $store.state.user.userRole === 2 && !item.isHead ? null : !item.isHead && item.nurseGrade !== null ? findGraderAndShow(item, 2) :  null" />
                        </GridLayout>
                        <StackLayout class="horizontal-divider" />
                    </StackLayout>
                </StackLayout>
            </ScrollView>









        <ScrollView class="scroll" row="1" v-if="puanliSiralama">
            <GridLayout  :rows="subSkillsnull.length !== 0 && subSkillsnull !== null ? '*,*,*,*,*,*,*,*,*,*,*,*' : '*,*,*,*,*,*,*,*,*,*' "><!--,*,*,*,*,*,*-->
            <Label :row="artanPuanSirasi ? 0 : 8" :class="artanPuanSirasi ? 'headerrr' : 'headerr'" @tap="()=>artanPuanSirasi = !artanPuanSirasi" text="Puanı 0 Olan Alt Beceri Basamakları" /><!---->

            <StackLayout :row="artanPuanSirasi ? 1 : 9" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"
                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                        
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills0" :key="item.subSkillStepsId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @tap="!item.isHead && item.performGrade !== null ? findGraderAndShow(item,1) : null"/>
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" @tap="!item.isHead && item.teacherGrade !== null ? findGraderAndShow(item,0) : null"/>
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" @tap="!item.isHead && item.nurseGrade !== null ? findGraderAndShow(item,2) : null"/>
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>




            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" :row="artanPuanSirasi ? 2 : 6" class="headerr" text="Puanı 1 Olan Alt Beceri Basamakları" /><!---->

            <StackLayout :row="artanPuanSirasi ? 3 : 7" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                        class="header" v-if="secondGraderNotVeriyormu && hg " />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills1" :key="item.subSkillStepsId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @tap="!item.isHead && item.performGrade !== null ? findGraderAndShow(item,1) : null" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" @tap="!item.isHead && item.teacherGrade !== null ? findGraderAndShow(item,0) : null" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                            class="divider" v-if="secondGraderNotVeriyormu && hg    " />
                        <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg  " @tap="!item.isHead && item.nurseGrade !== null ? findGraderAndShow(item,2) : null"/>
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>
            



            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" row="4" class="headerr" text="Puanı 2 Olan Alt Beceri Basamakları" /><!---->

            <StackLayout row="5" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu   && hg   ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                        class="header" v-if="secondGraderNotVeriyormu && hg " />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills2" :key="item.subSkillStepsId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @tap="!item.isHead && item.performGrade !== null ? findGraderAndShow(item,1) : null" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" @tap="!item.isHead && item.teacherGrade !== null ? findGraderAndShow(item,0) : null" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                            class="divider" v-if="secondGraderNotVeriyormu && hg " />
                        <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                            class="list-group-item-score" v-if="secondGraderNotVeriyormu  && hg" @tap="!item.isHead && item.nurseGrade !== null ? findGraderAndShow(item,2) : null"/>
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>




            <Label  @tap="()=>artanPuanSirasi = !artanPuanSirasi" :row="artanPuanSirasi ? 6 : 2" class="headerr" text="Puanı 3 Olan Alt Beceri Basamakları" /><!---->

            <StackLayout :row="artanPuanSirasi ? 7 : 3" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu  && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills3" :key="item.subSkillStepsId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg  ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @tap="!item.isHead && item.performGrade !== null ? findGraderAndShow(item,1) : null" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" @tap="!item.isHead && item.teacherGrade !== null ? findGraderAndShow(item,0) : null" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg " @tap="!item.isHead && item.nurseGrade !== null ? findGraderAndShow(item,2) : null" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>





            <Label @tap="()=>artanPuanSirasi = !artanPuanSirasi" :row="artanPuanSirasi ? 8 : 0" :class="!artanPuanSirasi ? 'headerrr' : 'headerr'" text="Puanı 4 Olan Alt Beceri Basamakları" /><!---->

            <StackLayout :row="artanPuanSirasi ? 9 : 1" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkills4" :key="item.subSkillStepsId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @tap="!item.isHead && item.performGrade !== null ? findGraderAndShow(item,1) : null" />
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" @tap="!item.isHead && item.teacherGrade !== null ? findGraderAndShow(item,0) : null" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                            class="divider" v-if="secondGraderNotVeriyormu && hg" />
                        <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                            class="list-group-item-score" v-if="secondGraderNotVeriyormu && hg" @tap="!item.isHead && item.nurseGrade !== null ? findGraderAndShow(item,2) : null" />
                    </GridLayout>
                    <!-- Horizontal divider -->
                    <StackLayout class="horizontal-divider" />
                </StackLayout>
            </StackLayout>





            <Label v-if="subSkillsnull.length !== 0 && subSkillsnull !== null"  @tap="()=>artanPuanSirasi = !artanPuanSirasi" row="10" class="headerr" text="Puanı Olmayan Alt Beceri Basamakları" /><!---->

            <StackLayout v-if="subSkillsnull.length !== 0 && subSkillsnull !== null" row="11" class="list-container"><!---->
                <!-- Header layout with equally spaced columns -->
                <GridLayout 
                    :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu && hg   ? ' 1, *' : '')"
                    rows="auto" class="list-header">
                    <Label text=" " col="0" />
                    <Label text="KB" col="2" class="header" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />
                    <Label text="ÖEG" v-if="oeg" :col='bolumInfo.ogrenciNotVerebilirmi && kb ? "4" : "2"' class="header" />
                    <Label :text="customizeSecondGraderName()"
                    :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                        class="header" v-if="secondGraderNotVeriyormu && hg" />
                </GridLayout>
                <StackLayout class="horizontal-divider" />
                <!-- List items -->
                <StackLayout v-for="item in subSkillsnull" :key="item.subSkillStepsId" class="list-group-item">
                    <GridLayout 
                        :columns="'5*,' + (bolumInfo.ogrenciNotVerebilirmi && kb ? ' 1, *,' : '') + (oeg ? '1, *,' : '') + (secondGraderNotVeriyormu  && hg ? ' 1, *' : '')"
                        rows="auto">
                        <Label :text="item.name" col="0" class="list-group-item-heading" @tap="item.name === 'Böyle bir kayıt bulunmamaktadır.' ? null : goSubSkillSteps(item)" />
                        <!-- Vertical dividers -->
                        <StackLayout col="1" class="divider" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" />

                        <!-- Scores -->
                        <Label :text="item.performGrade === null ? '-' : item.performGrade.toString()" col="2"
                            class="list-group-item-score" v-if="bolumInfo.ogrenciNotVerebilirmi && kb" @tap="!item.isHead && item.performGrade !== null ? findGraderAndShow(item,1) : null"/>
                        <StackLayout :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '3' : '1'" class="divider" v-if="oeg" />

                        <Label :text="item.teacherGrade === null ? '-' : item.teacherGrade.toString()" v-if="oeg"
                            :col="bolumInfo.ogrenciNotVerebilirmi && kb ? '4' : '2'" class="list-group-item-score" @tap="!item.isHead && item.teacherGrade !== null ? findGraderAndShow(item,0) : null" />
                        <StackLayout
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '5':'3' : '3') : (secondGraderNotVeriyormu  && hg? oeg ? '3' : '1' : '1')"                            class="divider" v-if="secondGraderNotVeriyormu  && hg" />
                        <Label :text="item.nurseGrade === null ? '-' : item.nurseGrade.toString()"
                        :col="bolumInfo.ogrenciNotVerebilirmi  && kb ? (secondGraderNotVeriyormu && hg ? oeg ? '6' : '4' : '6') : (secondGraderNotVeriyormu && hg ? oeg ? '4' : '2' : '4')"                            class="list-group-item-score" v-if="secondGraderNotVeriyormu  && hg" @tap="!item.isHead && item.nurseGrade !== null ? findGraderAndShow(item,2) : null"/>
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
import { Dialogs } from '@nativescript/core';
import ModalDialog from './ModalDialog.vue';
import ModalDialogLight from '~/views/TeacherViews/ModalDialogLight.vue';
import { ApplicationSettings } from '@nativescript/core';
export default {
    props: ['student', 'sinif', 'bolumInfo', 'skill', 'subSkill', 'secondGrader', 'secondGraderNotVeriyormu'],
    data() {
        return {
            kbSira: false,
            oegSira: true,
            hgSira: false,
            oeg: true,
            kb: false,
            hg: false,
            puanliSiralama: false,
            subSkillSteps: [],
            subSkillsnull: [],
            subSkills0: [],
            subSkills1: [],
            subSkills2: [],
            subSkills3: [],
            subSkills4: [],
            graderInfo: null,
            artanPuanSirasi: true,

            secondGraderName: null,
        };
    },

    methods: {
        goSubSkillSteps(){},
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
    async findGraderAndShow(subskillsteps, graderRole) {
            if (graderRole === 0) {
                if (this.$store.state.user.userRole === 0 ) {
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        }
                        else {
                            return null;
                        }
                    })
                        .then(res => {
                            this.graderInfo = res;
                            console.log('graderInfoooo:');
                            console.log(this.graderInfo);
                            var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        })
                }
                else if (this.$store.state.user.userRole === 1) {



                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        }
                        else {
                            return null;
                        }
                    })
                        .then(res => {
                            this.graderInfo = res;
                            console.log('graderInfo:');
                            console.log(this.graderInfo);
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + subskillsteps.gradeDateTeacher.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });



                } else if (this.$store.state.user.userRole === 2) {



                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getgrader/${subskillsteps.teacherId}/${this.$store.state.user.bolumId}/${graderRole}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        }
                        else {
                            return null;
                        }
                    })
                        .then(res => {
                            this.graderInfo = res;
                            console.log('graderInfo:');
                            console.log(this.graderInfo);
                            var localDateTimeString = new Date(subskillsteps.gradeDateTeacher).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + subskillsteps.gradeDateTeacher.substring(0,10) + " tarihinde verilmiştir.");
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı öğretim elemanı tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });
                }



            } else if (graderRole === 1) {


                if (this.$store.state.user.userRole === 1 ) {
                    var localDateTimeString = new Date(subskillsteps.performDate).toLocaleString();
                    var date = new Date(localDateTimeString);
                    var year = date.getFullYear();
                    var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                    var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                    var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                    var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    //alert("Bu puan sizin tarafınızdan " + subskillsteps.gradeDateTeacher.substring(0,10) + " tarihinde verilmiştir.");
                    Dialogs.alert({
                        title: "Puan Bilgisi",
                        message: "Bu puan sizin tarafınızdan " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                        okButtonText: "Tamam"
                    });

                } else if (this.$store.state.user.userRole !== 1) {
                    var localDateTimeString = new Date(subskillsteps.performDate).toLocaleString();
                    var date = new Date(localDateTimeString);
                    var year = date.getFullYear();
                    var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                    var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                    var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                    var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                    //var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                    //alert("Bu puan "+ this.student.name + " " + this.student.surname+" adlı öğrenci tarafından " + subskillsteps.performDate.substring(0,10) + " tarihinde verilmiştir.");
                    Dialogs.alert({
                        title: "Puan Bilgisi",
                        message: "Bu puan " + this.student.name + " " + this.student.surname + " adlı öğrenci tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                        okButtonText: "Tamam"
                    });
                }


            } else if (graderRole === 2) {
                if (this.$store.state.user.userRole === 0) {



                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        }
                        else {
                            return null;
                        }
                    })
                        .then(res => {
                            this.graderInfo = res;
                            console.log('graderInfo:');
                            console.log(this.graderInfo);
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + subskillsteps.gradeDateNurse.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();

                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " + this.secondGrader.secondGraderName + " tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });




                } else if (this.$store.state.user.userRole === 1) {




                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        }
                        else {
                            return null;
                        }
                    })
                        .then(res => {
                            this.graderInfo = res;
                            console.log('graderInfo:');
                            console.log(this.graderInfo);
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + subskillsteps.gradeDateNurse.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " + this.secondGrader.secondGraderName + " tarafından " + + day + "-" + month + "-" + year + " " + hours + ":" + minutes + + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });




                } else if (this.$store.state.user.userRole === 2) {
                    await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getgrader/${subskillsteps.nurseId}/${this.$store.state.user.bolumId}/${graderRole}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": "Bearer " + this.$store.state.user.token
                        },
                    }).then(res => {
                        if (res.status == 200) {
                            return res.json();
                        }
                        else {
                            return null;
                        }
                    })
                        .then(res => {
                            this.graderInfo = res;
                            console.log('graderInfo:');
                            console.log(this.graderInfo);
                            //alert("Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " +  this.secondGrader.secondGraderName  +" tarafından " + subskillsteps.gradeDateNurse.substring(0,10) + " tarihinde verilmiştir.");
                            var localDateTimeString = new Date(subskillsteps.gradeDateNurse).toLocaleString();
                            var date = new Date(localDateTimeString);
                            var year = date.getFullYear();
                            var month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1); // Aylar 0'dan başlar, bu yüzden 1 ekliyoruz
                            var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
                            var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                            var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                            Dialogs.alert({
                                title: "Puan Bilgisi",
                                message: "Bu puan " + this.graderInfo.name + ' ' + this.graderInfo.surname + " adlı " + this.secondGrader.secondGraderName +  " tarafından " + day + "-" + month + "-" + year + " " + hours + ":" + minutes + " tarihinde verilmiştir.",
                                okButtonText: "Tamam"
                            });
                        });

                }
            }

        },
        
        customizeSecondGraderName() {

            const nameParts = this.secondGrader.secondGraderName.split(' ');

            const initials = nameParts.map(part => part[0]).join('');

            const customizedSecondGraderName = (initials + 'G').toUpperCase();;

            return customizedSecondGraderName;
        },
        async subSkilleriGetir(basedRole) {
            if (this.$store.state.user.userRole === 0) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/teacher/getsubskillstepsforstatistics/${this.subSkill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${basedRole}`,
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
                            console.log('res in icindeyiz');
                            console.log(res.thirdGrades);
                            console.log(res.thirdGrades.length === 0);
                            console.log(this.subSkills3);


                            this.subSkillSteps = res.subskillSteps;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;
                            console.log(this.subSkills0);

                            /*if(this.subSkills4.length === 0 || this.subSkills4 === null){
                                console.log('4 in icindeyiz');

                                this.subSkills4 = [{subssubskillStepsIdkillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills3.length === 0 || this.subSkills3 === null){
                                console.log('3 in icindeyiz');

                                this.subSkills3 = [{subskillStepsId: -2, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills2.length === 0 || this.subSkills2 === null){
                                console.log('2 in icindeyiz');

                                this.subSkills2 = [{subskillStepsId: -3, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills1.length === 0 || this.subSkills1 === null){
                                console.log('1 in icindeyiz');

                                this.subSkills1 = [{subskillStepsId: -4, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills0.length === 0 || this.subSkills0 === null){
                                console.log('0 in icindeyiz');

                                this.subSkills0 = [{subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkillsnull.length === 0 || this.subSkillsnull === null){
                                this.subSkillsnull = [{subskillStepsId: -6, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }*/

                            this.secondGraderName = this.customizeSecondGraderName();
                            console.log(this.subSkills3);
                            console.log("ULAULAULAULA:");
                            console.log(this.subSkills0);


                            //console.log(res.length);
                        }

                    })
            } else if (this.$store.state.user.userRole === 1) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/student/getsubskillstepsforstatistics/${this.subSkill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${basedRole}`,
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
                            console.log('res in icindeyiz');
                            console.log(res.thirdGrades);
                            console.log(res.thirdGrades.length === 0);
                            console.log(this.subSkills3);


                            this.subSkillSteps = res.subskillSteps;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;
                            console.log(this.subSkills0);


                            /*if(this.subSkills4.length === 0 || this.subSkills4 === null){
                                console.log('4 in icindeyiz');

                                this.subSkills4 = [{subssubskillStepsIdkillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills3.length === 0 || this.subSkills3 === null){
                                console.log('3 in icindeyiz');

                                this.subSkills3 = [{subskillStepsId: -2, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills2.length === 0 || this.subSkills2 === null){
                                console.log('2 in icindeyiz');

                                this.subSkills2 = [{subskillStepsId: -3, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills1.length === 0 || this.subSkills1 === null){
                                console.log('1 in icindeyiz');

                                this.subSkills1 = [{subskillStepsId: -4, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkills0.length === 0 || this.subSkills0 === null){
                                console.log('0 in icindeyiz');

                                this.subSkills0 = [{subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }if(this.subSkillsnull.length === 0 || this.subSkillsnull === null){
                                this.subSkillsnull = [{subskillStepsId: -6, name: "Böyle bir kayıt bulunmamaktadır.", performGrade: null, teacherGrade: null, nurseGrade: null}];
                            }*/

                            this.secondGraderName = this.customizeSecondGraderName();
                            console.log(this.subSkills3);

                            //console.log(res.length);
                        }

                    })
            }
            else if (this.$store.state.user.userRole === 2) {
                await fetch(`http://${this.$store.state.user.hostIp}:8082/nursapp/app/secondgrader/getsubskillstepsforstatistics/${this.subSkill.subskillId}/${this.student.userId}/${this.$store.state.user.bolumId}/${this.sinif}/${basedRole}`,
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
                            console.log('res in icindeyiz');

                            this.subSkillSteps = res.subskillSteps;
                            this.subSkillsnull = res.nullGrades;
                            this.subSkills0 = res.zeroGrades;
                            this.subSkills1 = res.firstGrades;
                            this.subSkills2 = res.secondGrades;
                            this.subSkills3 = res.thirdGrades;
                            this.subSkills4 = res.forthGrades;
                            /*if(this.subSkills4 == []){
                                this.subSkills4.push({subskillId: -1, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills3 == []){
                                this.subSkills3.push({subskillId: -2, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills2 == []){
                                this.subSkills2.push({subskillId: -3, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills1 == []){
                                this.subSkills1.push({subskillId: -4, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }else if(this.subSkills0 == []){
                                this.subSkills0.push({subskillId: -5, name: "Böyle bir kayıt bulunmamaktadır.", studentGrade: null, teacherGrade: null, nurseGrade: null});
                            }*/
                            this.secondGraderName = this.customizeSecondGraderName();
                            console.log(this.subSkills4);

                            console.log(res.length);
                        }

                    })
            }

        },
        onNavigatedTo() {
            this.subSkilleriGetir(0);
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


.ns-dark .filteleButton{
    background-color: #08F7FE80;
    color: #ffffff;
    border-radius: 15em;
    
    text-align: center;
    vertical-align: center;
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
    color: #fff;
    background-color: transparent;
    text-wrap: true;
    font-size: 13%;
}
.ns-dark .list-group-item-headingg {
    padding: 10;
    color: #fff;
    background-color: transparent;
    text-wrap: true;
    font-size: 13%;
    font-weight: bold;
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


.ns-light .filteleButton{
    background-color: #33c5c9;
    color: #000000;
    border-radius: 15em;
    text-align: center;
    vertical-align: center;
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
    color: #000000;
    background-color: transparent;
    text-wrap: true;
    font-size: 13%;
}
.ns-light .list-group-item-headingg {
    padding: 10;
    color: #000000;
    background-color: transparent;
    text-wrap: true;
    font-size: 13%;
    font-weight: bold;
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