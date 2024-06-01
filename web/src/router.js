import { createRouter, createWebHistory } from 'vue-router';
import store from './store.js';
import Welcome from './views/Welcome.vue'
import Karne from './components/Karne.vue'
import AdminHome from './views/AdminViews/AdminHome.vue'
import AdminLogin from './views/AdminViews/AdminLogin.vue'
import UserManagement from './views/AdminViews/ManageUsers/UserManagement.vue';
import AddUser from './views/AdminViews/ManageUsers/AddUser.vue';
import UpdateUser from './views/AdminViews/ManageUsers/UpdateUser.vue';
import ChangePasswordModal from './views/AdminViews/ManageUsers/ChangePasswordModal.vue';
import AddFromExcel from './views/AdminViews/ManageUsers/AddFromExcel.vue';
import Skills from './views/AdminViews/ManageSkills/Skills.vue'
import Subskills from './views/AdminViews/ManageSkills/Subskills.vue';
import SubskillSteps from './views/AdminViews/ManageSkills/SubskillSteps.vue';
import LoginActivities from './views/AdminViews/Loglar/LoginActivities.vue';
import Activities from './views/AdminViews/Loglar/Activities.vue';
import AvgYears from './views/AdminViews/AvgYears.vue';
import StudentLogin from './views/StudentViews/StudentLogin.vue';
import StudentHome from './views/StudentViews/StudentHome.vue';
import StudentProfile from './views/StudentViews/StudentProfile.vue';
import StudentChangePassword from './views/StudentViews/StudentChangePassword.vue' ; 
import TeacherLogin from './views/TeacherViews/TeacherLogin.vue';
import TeacherHome from './views/TeacherViews/TeacherHome.vue';
import teacherProfile from './views/TeacherViews/TeacherProfile.vue';
import teacherChangePassword from './views/TeacherViews/TeacherChangePassword.vue';
import OgrenciSec from './views/TeacherViews/OgrenciSec.vue';
import SinifSec from './views/TeacherViews/SinifSec.vue';
import BeceriSec from './views/TeacherViews/BeceriSec.vue';
import AltBeceriSec from './views/TeacherViews/AltBeceriSec.vue';
import AltBeceriBasamagiSec from './views/TeacherViews/AltBeceriBasamagi.vue';
import Statistics from './views/TeacherViews/Statistics.vue';
import DurumGor from './views/TeacherViews/DurumGor.vue';
import StatisticsAltBeceri from './views/TeacherViews/StatisticsAltBeceri.vue';
import StatisticsAltBeceriBasamagi from './views/TeacherViews/StatisticsAltBeceriBasamagi.vue';
import NurseHome from './views/NurseViews/NurseHome.vue';
import NurseLogin from './views/NurseViews/NurseLogin.vue';
import nurseProfile from './views/NurseViews/NurseProfile.vue';
import nurseChangePassword from './views/NurseViews/NurseChangePassword.vue';
import StudentSelectGrade from './views/StudentViews/StudentSelectGrade.vue';
import StudentSkills from  './views/StudentViews/StudentSkills.vue';
import StudentSubskills from  './views/StudentViews/StudentSubSkills.vue';
import StudentSubskillSteps from  './views/StudentViews/StudentSubSkillSteps.vue';
import StudentStatistics from './views/StudentViews/StudentStatistics.vue';
import StudentStatisticsSubSkills from './views/StudentViews/StudentStatisticsSubSkill.vue';
import StudentStatisticsSubSkillSteps from './views/StudentViews/StudentStatisticsSubSkillSteps.vue';
import Hakkında from './views/Hakkında.vue';

const routes = [
    {
        path: '/',
        name: 'Welcome',
        component: Welcome
    },
    {
        path: '/hakkinda',
        name: 'Hakkında',
        component: Hakkında
    },
    {
        path: '/karne/:bolumId/:studentId',
        name: 'karne',
        component: Karne,
        props: true
    },
    {
        path: '/loginactivities',
        name: 'loginactivities',
        component: LoginActivities,
        meta: {header:true}
    },
    {
        path: '/activities',
        name: 'activities',
        component: Activities,
        meta: {header:true}
    },
    {
        path: '/yıllar',
        name: 'yıllar',
        component: AvgYears,
        meta: {header:true}
    },
    {
        path: '/admin-login',
        name: 'adminlogin',
        component: AdminLogin,
    }, 
    {
        path: '/admin-home',
        name: 'AdminHome',
        component: AdminHome,
        meta: {header:true}
    },
    {
        path: '/usermanagement',
        name: 'usermanagement',
        component: UserManagement,
        meta: {header:true}
    },
    {
        path: '/adduser',
        name: 'adduser',
        component: AddUser,
        meta:{header:true}
    },
    {
        path: '/addwithexcel',
        name: 'addwithexcel',
        component: AddFromExcel,
        meta:{header:true}
    },
    {
        path: '/updateuser',
        name: 'updateuser',
        component: UpdateUser
    },
    {
        path: '/changepassword',
        name: 'changepassword',
        component: ChangePasswordModal
    },
    {
        path: '/skills',
        name: 'skills',
        component: Skills,
        meta: {header:true}
    },
    {
        path: '/subskills/:skillId/:skillName',
        name: 'SubSkills',
        component: Subskills,
        meta: {header:true},
        props: true
    },
    {
        path: '/subskillsteps/:subSkillId/:subskillName',
        name: 'SubSkillSteps',
        component: SubskillSteps,
        meta: {header:true},
        props: true
    },
    {
        path: '/teacher-login',
        name: 'teacherlogin',
        component: TeacherLogin
    },
    {
        path: '/grader-home',
        name: 'teacherhome',
        component: TeacherHome,
        meta: {header:true},
    },
    {
        path: '/profile',
        name: 'teacherprofile',
        component: teacherProfile,
        meta: {header:true}
    },
    {
        path: '/change-password',
        name: 'teacherchangepassword',
        component: teacherChangePassword,
        meta: {header:true}
    },
    {
        path: '/show-charts',
        name: 'teacherstatistics',
        component: Statistics,
        meta: {header:true}
    },
    {
        path: '/ogrenci-sec',
        name: 'ogrencisec',
        component: OgrenciSec,
        meta: {header:true}
    },
    {
        path: '/sinif-sec',
        name: 'sinifsec',
        component: SinifSec,
        meta: {header:true}
    },
    {
        path: '/beceri-sec',
        name: 'becerisec',
        component: BeceriSec,
        meta: {header:true}
    },
    {
        path: '/alt-beceri-sec',
        name: 'altbecerisec',
        component: AltBeceriSec,
        meta: {header:true}
    },
    {
        path: '/alt-beceri-basamagi',
        name: 'altbeceribasamagisec',
        component: AltBeceriBasamagiSec,
        meta: {header:true}
    },
    {
        path: '/alt-beceri-statistics',
        name: 'altbeceristatistics',
        component: StatisticsAltBeceri,
        meta: {header:true}
    },
    {
        path: '/alt-beceri-basamagi-statistics',
        name: 'statisticsaltbeceribasamagi',
        component: StatisticsAltBeceriBasamagi,
        meta: {header:true}
    },
    {
        path: '/ogrenci-durum-gor',
        name: 'durumgor',
        component: DurumGor,
        meta: {header:true}
    },
    {
        path: '/student-login',
        name: 'studentlogin',
        component: StudentLogin
    },
    {
        path: '/student-home',
        name: 'studenthome',
        component: StudentHome,
        meta: {header:true},
    },
    {
        path: '/student-profile',
        name: 'studentprofile',
        component: StudentProfile,
        meta: {header:true}
    },
    {
        path: '/student-change-password',
        name: 'studentchangepassword',
        component: StudentChangePassword,
        meta: {header:true}
    },
    {
        path: '/student-select-grade',
        name: 'studentselectgrade',
        component: StudentSelectGrade,
        meta: {header:true}
    },
    {
        path: '/student-skills',
        name: 'studentskills',
        component: StudentSkills,
        meta: {header:true}   
    },
    {
        path: '/student-subskills',
        name: 'studentsubskills',
        component: StudentSubskills,
        meta: {header:true}
    },
    {
        path: '/student-subskill-steps',
        name: 'studentsubskillsteps',
        component: StudentSubskillSteps,
        meta: {header:true}
    },
    {
        path: '/student-statistics',
        name: 'studentstatistics',
        component: StudentStatistics,
        meta: {header:true}
    },
    {
        path: '/student-statistics-subskills',
        name: 'studentstatisticsubskills',
        component: StudentStatisticsSubSkills,
        meta: {header:true}
    },
    {
        path: '/student-statistics-subskillsteps',
        name: 'studentstatisticsteps',
        component: StudentStatisticsSubSkillSteps,
        meta: {header:true}
    },
    {
        path: '/nurse-login',
        name: 'nuerselogin',
        component: NurseLogin
    },
    {
        path: '/nurse-home',
        name: 'nursehome',
        component: NurseHome,
        meta: {header:true}
    },
    {
        path: '/nurse-profile',
        name: 'nurseprofile',
        component: nurseProfile,
        meta: {header:true}
    },
    {
        path: '/nurse-change-password',
        name: 'nursechangepassword',
        component: nurseChangePassword,
        meta: {header:true}
    }
]

const router = createRouter({
    routes: routes,
    history: createWebHistory()
})

router.beforeEach((to, from, next) => {
    const publicPages = ['/student-login', '/teacher-login', '/admin-login', '/nurse-login', '/'];  // Giriş yapmayı gerektirmeyen sayfalar
    const authRequired = !publicPages.includes(to.path);  // Giriş gerektiren sayfa mı?
    const loggedIn = store.state.user.token != '' ? true : false;  // Kullanıcının giriş yapıp yapmadığı bilgisi

    // Eğer giriş yapma gerekiyorsa ve kullanıcı giriş yapmamışsa, giriş sayfasına yönlendir
    if (authRequired && !loggedIn) {
        next({ path: '/' });  // Burada varsayılan olarak öğrenci giriş sayfasına yönlendiriyoruz.
    } else {
        next();  // Herhangi bir koşul sağlanmıyorsa rotaya izin ver
    }
});

export default router;