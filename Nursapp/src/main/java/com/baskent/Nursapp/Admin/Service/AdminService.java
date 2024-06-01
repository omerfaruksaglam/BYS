package com.baskent.Nursapp.Admin.Service;

import com.baskent.Nursapp.Admin.DTO.UserUpdateDTO;
import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.Login_RecordsRepository;
import com.baskent.Nursapp.DataAccessLayer.UserRepository;
import com.baskent.Nursapp.Entity.EnumUtils;
import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.LoginRecords.Login_Records;
import com.baskent.Nursapp.Entity.User.Users;
import com.baskent.Nursapp.Admin.DTO.AllUsers;
import com.baskent.Nursapp.Admin.DTO.AllUsersResponseUser;
import com.baskent.Nursapp.Admin.DTO.AvgWithYear;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    @Autowired
    private ClientIpBean clientIpBean;
    private final UserRepository userRepository;
    @Autowired
    private final Login_RecordsRepository login_recordsRepository;
    @Autowired
    private final Action_RecordRepository action_recordRepository;


    private final EntityManager entityManager;

    public List<AvgWithYear> getAvgWithYears(Integer skillId) {
        Query query = entityManager.createNativeQuery("SELECT * FROM calculate_yearly_skill_averages(:skillId)", AvgWithYear.class);
        query.setParameter("skillId",skillId);
        return query.getResultList();
    }


    public AllUsers getUsersByBolumId(Long bolumid) {
        List<Users> users = userRepository.getAllUsersByBolumId(bolumid);
        List<AllUsersResponseUser> students = new ArrayList<>();
        List<AllUsersResponseUser> teachers = new ArrayList<>();
        List<AllUsersResponseUser> admins = new ArrayList<>();
        List<AllUsersResponseUser> nurses = new ArrayList<>();

        for (Users user : users){
            if(user.getUserRole().getValue() == 0){
                teachers.add(AllUsersResponseUser.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).isActive(user.isActive()).build());
            } else if (user.getUserRole().getValue() == 1) {
                students.add(AllUsersResponseUser.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).isActive(user.isActive()).build());
            } else if (user.getUserRole().getValue() == 2) {
                nurses.add(AllUsersResponseUser.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).isActive(user.isActive()).build());
            }else {
                admins.add(AllUsersResponseUser.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).isActive(user.isActive()).build());
            }
        }
        AllUsers allUsers = new AllUsers(teachers,nurses,students,admins);
        return allUsers;
    }

    public void saveUsers(List<Users> users, Long admin) {
        userRepository.saveAll(users);
        System.out.println(users.size());
        System.out.println(users);

        for (int i = 0; i < users.size(); i++) {
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", "+ users.get(i).getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici, " + users.get(i).getUserId() + " numaralı " + users.get(i).getName() + " "+ users.get(i).getSurname() +" isimli kullanıcıyı " + EnumUtils.getTurkishRole(users.get(i).getUserRole()) +" rolü ile sisteme kayıt etti.");
           action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(users.get(i).getBolumId()).actionText(actionText.toString()).build());
        }
      //    IntStream.iterate(0, i -> users.size()).mapToObj(i -> new StringBuilder(
      //       "[" + admin + ", " + users.get(i).getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici " + users.get(i).getUserId() + " numaralı " + users.get(i).getName() + users.get(i).getSurname() + " isimli kullanıcıyı " + users.get(i).getUserRole() + " rolü ile sisteme kayıt etti.")).forEach(actionText -> action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(users.get(0).getBolumId()).actionText(actionText.toString()).build()));
    }

    public void deleteUser(Long userId, Long bolumId, Long admin) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+ bolumId + "] numaralı userId ve bolumId ye sahip yönetici, " + userId + " numaralı " + userRepository.getKullaniciByIdandBolum(userId,bolumId).getName() + " "+ userRepository.getKullaniciByIdandBolum(userId,bolumId).getSurname() +" isimli kullanıcıyı sildi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(bolumId).actionText(actionText.toString()).build());
        userRepository.deleteByIdAndBolum(userId, bolumId);
    }

    public void updateUser(Long userid, Long bolumid, UserUpdateDTO userDetails, Long admin) {
        try {
            Users user = userRepository.getKullaniciByIdandBolumForActiveUptade(userid, bolumid);
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", "+ bolumid + "] numaralı userId ve bolumId ye sahip yönetici, " + userid + " numaralı " + user.getName() + " "+ user.getSurname() +" isimli kullanıcıyı güncelledi. Yeni kayıt ismi " + userDetails.getName() + " "+ userDetails.getSurname() );
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(bolumid).actionText(actionText.toString()).build());

            if (user == null) {
                throw new IllegalArgumentException("Kullanıcı veya bölüm bulunamadı.");
            }
            user.setName(userDetails.getName());
            user.setSurname(userDetails.getSurname());
            //Role userRole = Role.fromValue(userDetails.getUserRole());
            //user.setUserRole(userRole);

            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            // Burada, özel durumlar için özel işlemler yapılabilir, örneğin:
            // Loglama yapmak veya kullanıcıya geri dönülecek bir hata mesajı hazırlamak.
            System.out.println("Hata: " + e.getMessage());
            throw e; // veya özel bir hata nesnesi döndürebilirsiniz.
        } catch (Exception e) {
            // Beklenmeyen diğer hatalar için genel bir yakalama bloğu
            System.out.println("Kullanıcı güncellenirken beklenmeyen bir hata oluştu: " + e.getMessage());
            throw new RuntimeException("Kullanıcı güncelleme işlemi sırasında bir hata oluştu.", e);
        }
    }

    public void updateActiveStatus(Long userId, Long bolumId, boolean activeStatus, Long admin) {
        Users user = userRepository.getKullaniciByIdandBolumForActiveUptade(userId, bolumId);
        user.setActive(activeStatus);
        userRepository.save(user);
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+ bolumId + "] numaralı userId ve bolumId ye sahip yönetici, " + userId + " numaralı " + userRepository.getKullaniciByIdandBolumForActiveUptade(userId,bolumId).getName() + " "+ userRepository.getKullaniciByIdandBolumForActiveUptade(userId,bolumId).getSurname() +" isimli kullanıcının aktiflik durumunu güncelledi. Güncel aktiflik durumu " + EnumUtils.getActiveStatus(activeStatus));
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(bolumId).actionText(actionText.toString()).build());

    }

    public List<Login_Records> getloginActivities(Long bolumId) {
        return  login_recordsRepository.getLogin_RecordsByRequestedBolumId(bolumId);
    }

    public List<Action_Records> getActionRecords(Long bolumId) {
        return action_recordRepository.getAllByBolumId(bolumId);
    }

}
