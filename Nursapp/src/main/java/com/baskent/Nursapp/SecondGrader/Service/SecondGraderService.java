package com.baskent.Nursapp.SecondGrader.Service;

import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.GradesRepository;
import com.baskent.Nursapp.DataAccessLayer.UserRepository;
import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.Grades.Grades;
import com.baskent.Nursapp.Teacher.DTO.GradesDTO;
import com.baskent.Nursapp.Teacher.DTO.SaveGrades;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecondGraderService {
    private final UserRepository userRepository;
    private final GradesRepository gradesRepository;

    @Autowired
    private final ClientIpBean clientIpBean;

    @Autowired
    private final Action_RecordRepository action_recordRepository;
    public void saveGrades(SaveGrades saveGrades) {
        StringBuilder actionText = new StringBuilder("[" + saveGrades.getNewGrades().get(0).getGraderId().toString()+ ", " + saveGrades.getNewGrades().get(0).getBolumId().toString() + "]" + " userId ve bolumId ye sahip kullanıcı, " + "[" + saveGrades.getNewGrades().get(0).getStudentId().toString()+ ", " + saveGrades.getNewGrades().get(0).getBolumId().toString() + "]" + " userId ve bolumId ye sahip öğrencinin [" + saveGrades.getNewGrades().get(0).getSinif().toString() + "]. sınıfı için, ");
        List<Long> yeniSS = new ArrayList<>();
        List<Long> updateSS = new ArrayList<>();
        for (GradesDTO grade : saveGrades.getNewGrades()) {
            boolean exists = gradesRepository.existsByCustomId(grade.getStudentId(), grade.getBolumId(), grade.getSubSkillStepsId(), grade.getSinif(), 2L); // Örneğin, graderRole için sabit bir değer kullanıyorsunuz gibi görünüyor.
            if (exists) {
                updateSS.add(grade.getSubSkillStepsId());

                // Kayıt var, burada güncelleme işlemini yapın.
                // Özel bir güncelleme sorgusu yazabilir veya kaydı bulup değerlerini güncelleyip save metodunu kullanabilirsiniz.
                gradesRepository.updateGrade(grade.getStudentId(),grade.getBolumId(),grade.getSubSkillStepsId(),grade.getSinif(),2L,grade.getGrade(), grade.getGraderId(), ZonedDateTime.now());
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
}
