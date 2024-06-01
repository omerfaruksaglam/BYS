package com.baskent.Nursapp.Teacher.Service;
import com.baskent.Nursapp.Admin.DTO.AllUsersResponseUser;
import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.*;
import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.Grades.Grades;
import com.baskent.Nursapp.Entity.Role;
import com.baskent.Nursapp.Entity.SecondGraderSinif.SecondGraderSinif;
import com.baskent.Nursapp.Entity.SecondGraderSinif.SecondGraderSinifSuperKeyId;
import com.baskent.Nursapp.Entity.Statistics.Statistics;
import com.baskent.Nursapp.Entity.User.Users;
import com.baskent.Nursapp.Teacher.DTO.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import com.baskent.Nursapp.DataAccessLayer.UserRepository;
import com.baskent.Nursapp.Teacher.DTO.SubSkillwithGrades;
import com.baskent.Nursapp.Teacher.DTO.SubskillStepsWithGrades;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TeacherService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final Action_RecordRepository action_recordRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private final SkillsRespository skillsRespository;

    @Autowired
    private final BolumRepository bolumRepository;

    @Autowired
    private final ClientIpBean clientIpBean;

    @Autowired
    private final SecondGraderRepository secondGraderRepository;

    @Autowired
    private final SecondGraderSinifRepository secondGraderSinifRepository;

    @Autowired
    private final GradesRepository gradesRepository;

    @Autowired
    private final EquipmentRepository equipmentRepository;

    @Autowired
    private final StatisticsRepository statisticsRepository;

    public List<SubskillStepsWithGrades> getSubskillStepsWithGrades(Long studentId, Long bolumId, Long classLevel, Long subskillId) {
        Query query = entityManager.createNativeQuery("SELECT * FROM subskillsteps_with_grades(:studentId, :bolumId, :classLevel, :subskillId)", SubskillStepsWithGrades.class);
        query.setParameter("studentId", studentId);
        query.setParameter("bolumId", bolumId);
        query.setParameter("classLevel", classLevel);
        query.setParameter("subskillId",subskillId);
        return query.getResultList();
    }

    public List<SubSkillwithGrades> getSubskillWithGrades(Integer studentId, Integer bolumId, Integer classLevel, Integer skillId) {
        Query query = entityManager.createNativeQuery("SELECT * FROM subskill_with_grades(:studentId, :bolumId, :classLevel, :skillId)", SubSkillwithGrades.class);
        query.setParameter("studentId", studentId);
        query.setParameter("bolumId", bolumId);
        query.setParameter("classLevel", classLevel);
        query.setParameter("skillId",skillId);
        return query.getResultList();
    }


    public List<AllUsersResponseUser> getUsersByBolumIdandRol(Long bolumid) {
        List<Users> users = userRepository.getAllUsersByBolumIdandRol(bolumid);
        List<AllUsersResponseUser> allUsersResponseUsers = new ArrayList<>();
        System.out.println(users);
        for(Users user : users){
            allUsersResponseUsers.add(AllUsersResponseUser.builder().userId(user.getUserId()).bolumId(user.getBolumId()).name(user.getName()).surname(user.getSurname()).userRole(user.getUserRole().getValue()).isActive(user.isActive()).build());

        }
        return allUsersResponseUsers;
    }


    public SkillsWithSecondGraderInfo getSkillsByBolumId(Long bolumId, Long yilsayisi, Long secondgraderid){
        if(secondgraderid == 0){
            return SkillsWithSecondGraderInfo.builder().skills(skillsRespository.getAllSkillsFromBolumId(bolumId)).secondGrader(secondGraderRepository.getSecondGraderFromId(secondgraderid)).secondGraderNotVerebilirmi(false).build();

        }else{
            Optional<SecondGraderSinif> secondGraderSinif = secondGraderSinifRepository.findById(SecondGraderSinifSuperKeyId.builder().secondGraderId(secondgraderid).sinifNumarasi(yilsayisi).build());
            return SkillsWithSecondGraderInfo.builder().skills(skillsRespository.getAllSkillsFromBolumId(bolumId)).secondGrader(secondGraderRepository.getSecondGraderFromId(secondgraderid)).secondGraderNotVerebilirmi(secondGraderSinif.isPresent()).build();

        }
    }


    public List<SubSkillwithGrades> getSubSkillsBySkillid(Integer studentid, Integer bolumid, Integer classlevel, Integer skillid) {
        return getSubskillWithGrades(studentid, bolumid, classlevel, skillid);
    }

    public SubskillsForStatistics getSubSkillsForStatistics(Integer studentid, Integer bolumid, Integer classlevel, Integer skillid, Integer baserole) {
        //List<List<SubSkillwithGrades>> subskills = new ArrayList<>();
        List<SubSkillwithGrades> ss;
        List<SubSkillwithGrades> sss;
        //Long skillCount = skillsRespository.getSkillCountFromBolumId((long) bolumid);
        BigDecimal num0 = new BigDecimal("0.0");
        BigDecimal num1 = new BigDecimal("1.0");
        BigDecimal num2 = new BigDecimal("2.0");
        BigDecimal num3 = new BigDecimal("3.0");
        BigDecimal num4 = new BigDecimal("4.0");
        ss = getSubskillWithGrades(studentid, bolumid, classlevel, skillid);
        sss = new ArrayList<>(ss);
        System.out.println("ss:");
        System.out.println(ss);
        List<SubSkillwithGrades> nullValues;
        List<SubSkillwithGrades> zeroGrades;
        List<SubSkillwithGrades> firstGrades;
        List<SubSkillwithGrades> secondGrades;
        List<SubSkillwithGrades> thirdGrades;
        List<SubSkillwithGrades> forthGrades;
        if(baserole == 0){
            nullValues = new ArrayList<>(ss.stream().filter(subs -> subs.getTeacherGrade() == null).toList());
            ss.removeIf(subs -> subs.getTeacherGrade() == null);
            zeroGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade().doubleValue(), num0.doubleValue())).toList());
            firstGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade().doubleValue(), num1.doubleValue())).toList());
            secondGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade().doubleValue(), num2.doubleValue())).toList());
            thirdGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade().doubleValue(), num3.doubleValue())).toList());
            forthGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade().doubleValue(), num4.doubleValue())).toList());

        }else if(baserole == 1){
            nullValues = new ArrayList<>(ss.stream().filter(subs -> subs.getStudentGrade() == null).toList());
            ss.removeIf(subs -> subs.getStudentGrade() == null);
            zeroGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getStudentGrade().doubleValue(), num0.doubleValue())).toList());
            firstGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getStudentGrade().doubleValue(), num1.doubleValue())).toList());
            secondGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getStudentGrade().doubleValue(), num2.doubleValue())).toList());
            thirdGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getStudentGrade().doubleValue(), num3.doubleValue())).toList());
            forthGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getStudentGrade().doubleValue(), num4.doubleValue())).toList());

        } else if(baserole == 2){
            nullValues = new ArrayList<>(ss.stream().filter(subs -> subs.getNurseGrade() == null).toList());
            ss.removeIf(subs -> subs.getNurseGrade() == null);
            zeroGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade().doubleValue(), num0.doubleValue())).toList());
            firstGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade().doubleValue(), num1.doubleValue())).toList());
            secondGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade().doubleValue(), num2.doubleValue())).toList());
            thirdGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade().doubleValue(), num3.doubleValue())).toList());
            forthGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade().doubleValue(), num4.doubleValue())).toList());

        }else{
            nullValues = new ArrayList<>();
            zeroGrades = new ArrayList<>();
            firstGrades = new ArrayList<>();
            secondGrades = new ArrayList<>();
            thirdGrades = new ArrayList<>();
            forthGrades = new ArrayList<>();

        }


        //ss.removeIf(subs -> Objects.equals(subs.getTeacherGrade(), num));


        zeroGrades.sort(Comparator.comparing(SubSkillwithGrades::getStepNo));
        firstGrades.sort(Comparator.comparing(SubSkillwithGrades::getStepNo));
        secondGrades.sort(Comparator.comparing(SubSkillwithGrades::getStepNo));
        thirdGrades.sort(Comparator.comparing(SubSkillwithGrades::getStepNo));
        forthGrades.sort(Comparator.comparing(SubSkillwithGrades::getStepNo));

        if(nullValues.size() == 0 ){
            nullValues.add(SubSkillwithGrades.builder().subskillId((long)-1).name("Böyle bir kayıt bulunmamaktadır.").skillId(skillid).stepNo(1).studentGradeDate(null).studentGrade(null).teacherGradeDate(null).teacherGrade(null).nurseGradeDate(null).nurseGrade(null).build());
        }if(zeroGrades.size() == 0 ){
            zeroGrades.add(SubSkillwithGrades.builder().subskillId((long)-2).name("Böyle bir kayıt bulunmamaktadır.").skillId(skillid).stepNo(1).studentGradeDate(null).studentGrade(null).teacherGradeDate(null).teacherGrade(null).nurseGradeDate(null).nurseGrade(null).build());
        }if(firstGrades.size() == 0 ){
            firstGrades.add(SubSkillwithGrades.builder().subskillId((long)-3).name("Böyle bir kayıt bulunmamaktadır.").skillId(skillid).stepNo(1).studentGradeDate(null).studentGrade(null).teacherGradeDate(null).teacherGrade(null).nurseGradeDate(null).nurseGrade(null).build());
        }if(secondGrades.size() == 0){
            secondGrades.add(SubSkillwithGrades.builder().subskillId((long)-4).name("Böyle bir kayıt bulunmamaktadır.").skillId(skillid).stepNo(1).studentGradeDate(null).studentGrade(null).teacherGradeDate(null).teacherGrade(null).nurseGradeDate(null).nurseGrade(null).build());
        }if(thirdGrades.size() == 0){
            thirdGrades.add(SubSkillwithGrades.builder().subskillId((long)-5).name("Böyle bir kayıt bulunmamaktadır.").skillId(skillid).stepNo(1).studentGradeDate(null).studentGrade(null).teacherGradeDate(null).teacherGrade(null).nurseGradeDate(null).nurseGrade(null).build());
        }if(forthGrades.size() == 0){
            forthGrades.add(SubSkillwithGrades.builder().subskillId((long)-6).name("Böyle bir kayıt bulunmamaktadır.").skillId(skillid).stepNo(1).studentGradeDate(null).studentGrade(null).teacherGradeDate(null).teacherGrade(null).nurseGradeDate(null).nurseGrade(null).build());
        }
        //subskills.add(ss);

        return SubskillsForStatistics.builder().subskills(sss).nullGrades(nullValues).zeroGrades(zeroGrades).firstGrades(firstGrades).secondGrades(secondGrades).thirdGrades(thirdGrades).forthGrades(forthGrades).build();
    }

    public SubskillStepsForStatistics getSubSkillStepsForStatistics(Long subskillid, Long studentid, Long bolumid, Long classlevel, Integer baserole) {
        List<SubskillStepsWithGrades> ss;

        ss = getSubskillStepsWithGrades(studentid, bolumid, classlevel, subskillid);

        System.out.println("ss:");
        System.out.println(ss);
        List<SubskillStepsWithGrades> sss;
        sss = new ArrayList<>(ss);

        ss.removeIf(SubskillStepsWithGrades::getIsHead);
        //ss.removeIf(SubskillStepsWithGrades::getIsHead);
        List<SubskillStepsWithGrades> nullValues = new ArrayList<>(ss.stream().filter(subs -> subs.getTeacherGrade() == null).toList());
        ss.removeIf(subs -> subs.getTeacherGrade() == null);

        List<SubskillStepsWithGrades> zeroGrades;
        List<SubskillStepsWithGrades> firstGrades;
        List<SubskillStepsWithGrades> secondGrades;
        List<SubskillStepsWithGrades> thirdGrades;
        List<SubskillStepsWithGrades> forthGrades;


        if(baserole == 0){
            zeroGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade(), 0.0)).toList());
            firstGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade(), 1.0)).toList());
            secondGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade(), 2.0)).toList());
            thirdGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade(), 3.0)).toList());
            forthGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getTeacherGrade(), 4.0)).toList());

        } else if (baserole == 1) {
            zeroGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getPerformGrade(), 0.0)).toList());
            firstGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getPerformGrade(), 1.0)).toList());
            secondGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getPerformGrade(), 2.0)).toList());
            thirdGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getPerformGrade(), 3.0)).toList());
            forthGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getPerformGrade(), 4.0)).toList());

        } else if (baserole == 2) {
            zeroGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade(), 0.0)).toList());
            firstGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade(), 1.0)).toList());
            secondGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade(), 2.0)).toList());
            thirdGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade(), 3.0)).toList());
            forthGrades = new ArrayList<>(ss.stream().filter(subs -> Objects.equals(subs.getNurseGrade(), 4.0)).toList());

        }else{
            zeroGrades = new ArrayList<>();
            firstGrades = new ArrayList<>();
            secondGrades = new ArrayList<>();
            thirdGrades = new ArrayList<>();
            forthGrades = new ArrayList<>();

        }

        nullValues.sort(Comparator.comparing(SubskillStepsWithGrades::getStepNo));
        zeroGrades.sort(Comparator.comparing(SubskillStepsWithGrades::getStepNo));
        firstGrades.sort(Comparator.comparing(SubskillStepsWithGrades::getStepNo));
        secondGrades.sort(Comparator.comparing(SubskillStepsWithGrades::getStepNo));
        thirdGrades.sort(Comparator.comparing(SubskillStepsWithGrades::getStepNo));
        forthGrades.sort(Comparator.comparing(SubskillStepsWithGrades::getStepNo));



        if(nullValues.size() == 0 ){
            nullValues.add(SubskillStepsWithGrades.builder().subskillStepsId((long)-1).isHead(false).subskillId(subskillid).name("Böyle bir kayıt bulunmamaktadır.").stepNo(1).performDate(null).performGrade(null).gradeDateTeacher(null).teacherGrade(null).gradeDateNurse(null).nurseGrade(null).build());
        }if(zeroGrades.size() == 0 ){
            zeroGrades.add(SubskillStepsWithGrades.builder().subskillStepsId((long)-2).isHead(false).subskillId(subskillid).name("Böyle bir kayıt bulunmamaktadır.").stepNo(1).performDate(null).performGrade(null).gradeDateTeacher(null).teacherGrade(null).gradeDateNurse(null).nurseGrade(null).build());
        }if(firstGrades.size() == 0 ){
            firstGrades.add(SubskillStepsWithGrades.builder().subskillStepsId((long)-3).isHead(false).subskillId(subskillid).name("Böyle bir kayıt bulunmamaktadır.").stepNo(1).performDate(null).performGrade(null).gradeDateTeacher(null).teacherGrade(null).gradeDateNurse(null).nurseGrade(null).build());
        }if(secondGrades.size() == 0){
            secondGrades.add(SubskillStepsWithGrades.builder().subskillStepsId((long)-4).isHead(false).subskillId(subskillid).name("Böyle bir kayıt bulunmamaktadır.").stepNo(1).performDate(null).performGrade(null).gradeDateTeacher(null).teacherGrade(null).gradeDateNurse(null).nurseGrade(null).build());
        }if(thirdGrades.size() == 0){
            thirdGrades.add(SubskillStepsWithGrades.builder().subskillStepsId((long)-5).isHead(false).subskillId(subskillid).name("Böyle bir kayıt bulunmamaktadır.").stepNo(1).performDate(null).performGrade(null).gradeDateTeacher(null).teacherGrade(null).gradeDateNurse(null).nurseGrade(null).build());
        }if(forthGrades.size() == 0){
            forthGrades.add(SubskillStepsWithGrades.builder().subskillStepsId((long)-6).isHead(false).subskillId(subskillid).name("Böyle bir kayıt bulunmamaktadır.").stepNo(1).performDate(null).performGrade(null).gradeDateTeacher(null).teacherGrade(null).gradeDateNurse(null).nurseGrade(null).build());
        }



        return SubskillStepsForStatistics.builder().subskillSteps(sss).nullGrades(nullValues).zeroGrades(zeroGrades).firstGrades(firstGrades).secondGrades(secondGrades).thirdGrades(thirdGrades).forthGrades(forthGrades).build();
    }

    public SubskillStepsAndEquipments getSubSkillStepsBySubSkillid(Long subskillid, Long studentid, Long bolumid, Long classlevel) {
        return SubskillStepsAndEquipments.builder().subskillStepsWithGrades(getSubskillStepsWithGrades(studentid, bolumid, classlevel, subskillid)).equipments(equipmentRepository.getAllEquipmentsFromSubskillId(subskillid)).build();
    }
           // gradesRepository.upsertGrade(grade.getStudentId(), grade.getBolumId(), grade.getGraderId(), grade.getBolumId(), grade.getSubSkillStepsId(), grade.getSinif(), grade.getTeacherGrade());

    public void saveGrades(SaveGrades saveGrades) {
        StringBuilder actionText = new StringBuilder("[" + saveGrades.getNewGrades().get(0).getGraderId().toString()+ ", " + saveGrades.getNewGrades().get(0).getBolumId().toString() + "]" + " userId ve bolumId ye sahip kullanıcı, " + "[" + saveGrades.getNewGrades().get(0).getStudentId().toString()+ ", " + saveGrades.getNewGrades().get(0).getBolumId().toString() + "]" + " userId ve bolumId ye sahip öğrencinin [" + saveGrades.getNewGrades().get(0).getSinif().toString() + "]. sınıfı için, ");
        List<Long> yeniSS = new ArrayList<>();
        List<Long> updateSS = new ArrayList<>();
        for (GradesDTO grade : saveGrades.getNewGrades()) {
            boolean exists = gradesRepository.existsByCustomId(grade.getStudentId(), grade.getBolumId(), grade.getSubSkillStepsId(), grade.getSinif(), 0L); // Örneğin, graderRole için sabit bir değer kullanıyorsunuz gibi görünüyor.
            if (exists) {
                updateSS.add(grade.getSubSkillStepsId());
                // Kayıt var, burada güncelleme işlemini yapın.
                // Özel bir güncelleme sorgusu yazabilir veya kaydı bulup değerlerini güncelleyip save metodunu kullanabilirsiniz.
                gradesRepository.updateGrade(grade.getStudentId(),grade.getBolumId(),grade.getSubSkillStepsId(),grade.getSinif(),0L,grade.getGrade(), grade.getGraderId(), ZonedDateTime.now());
            } else {
                yeniSS.add(grade.getSubSkillStepsId());
                // Kayıt yok, yeni kaydı ekle.
                gradesRepository.save(Grades.builder().studentId(grade.getStudentId()).studentBolumId(grade.getBolumId()).graderId(grade.getGraderId()).graderBolumId(grade.getBolumId()).subskillStepsId(grade.getSubSkillStepsId()).classLevel(grade.getSinif()).grade(grade.getGrade()).gradeDate(ZonedDateTime.now()).build()); // Grades nesnesini DTO'dan oluşturun.
            }
        }
        if (!yeniSS.isEmpty()){
            actionText.append("[");
            for( Long i : yeniSS){
                actionText.append(i.toString());
                if(i.equals(yeniSS.get(yeniSS.size()-1))){
                    actionText.append("] ");
                }else{
                    actionText.append(", ");
                }
            }
            if(yeniSS.size() != 1) {
                actionText.append("numaralı id lere sahip alt beceri basamaklarına not girişi yaptı");
            }else{
                actionText.append("numaralı id ye sahip alt beceri basamağına not girişi yaptı");
            }
            if(!updateSS.isEmpty()){
                actionText.append(" ve ");
            }
        }
        if (!updateSS.isEmpty()){
            actionText.append("[");
            for( Long i : updateSS){
                actionText.append(i.toString());
                if(i.equals(updateSS.get(updateSS.size()-1))){
                    actionText.append("] ");
                }else{
                    actionText.append(", ");
                }
            }
            if(updateSS.size() != 1) {
                actionText.append("numaralı id lere sahip alt beceri basamaklarına not güncellemesi yaptı");
            }else{
                actionText.append("numaralı id ye sahip alt beceri basamağına not güncellemesi yaptı");
            }

        }
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(saveGrades.getNewGrades().get(0).getGraderId()).bolumId(saveGrades.getNewGrades().get(0).getBolumId()).actionText(actionText.toString()).build());
    }

    public StatisticsWithAverage getStatistics(Long studentid, Long bolumid, Long yilsayisi) {
        List<List<Statistics>> allstats = new ArrayList<>();
        List<Statistics> statistics;
        Double d = 0.0;
        Long skillCount = skillsRespository.getSkillCountFromBolumId(bolumid);
        //List<Double> averages  =  new ArrayList<>();
        for(int i = 1; i <= yilsayisi; i++){
            statistics = statisticsRepository.getStatisticsByStudentIdBolumIdClassLevel(bolumid, studentid, (long) i);
            if(statistics == null){
                allstats.add(null);
                //averages.add(0.0);
            }else{
                allstats.add(statistics);
                /*for(Statistics statistic : statistics){
                    d += statistic.getSuccessRate();
                }
                d =  d / (skillCount * 4);
                averages.add(d);
                d = 0.0;*/
            }
        }
        return StatisticsWithAverage.builder().allStatistics(allstats)/*.averages(averages)*/.skillCount(skillCount).build();
    }

    public GraderInfo getGraderInfo(Long graderid, Long bolumid, Long graderrole) {
        Users grader = userRepository.getKullaniciByIdandBolumandRole(graderid, bolumid, Role.fromValue(graderrole.intValue()));

        return GraderInfo.builder().name(grader.getName()).surname(grader.getSurname()).build();
    }
}
