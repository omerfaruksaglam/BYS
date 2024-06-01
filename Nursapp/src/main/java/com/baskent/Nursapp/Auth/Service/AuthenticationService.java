package com.baskent.Nursapp.Auth.Service;
import com.baskent.Nursapp.Admin.DTO.PasswordChangeDTO;
import com.baskent.Nursapp.Auth.Models.*;
import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.Auth.config.CustomAuthenticationToken;
import com.baskent.Nursapp.DataAccessLayer.*;
import com.baskent.Nursapp.Entity.*;
import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.LoginRecords.Login_Records;
import com.baskent.Nursapp.Entity.User.Users;
import com.baskent.Nursapp.Teacher.DTO.BolumInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private ClientIpBean clientIpBean;

    private final Action_RecordRepository action_recordRepository;
    private final UserRepository repository;

    private final BolumRepository bolumRepository;

    private final FacultyRepository facultyRepository;

    private final UniversityRepository universityRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final SkillsRespository skillsRespository;

    private final Login_RecordsRepository login_recordsRepository;


    public Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // veya uygun bir hata yönetimi
        }
    }
    public AuthenticationResponse register(RegisterRequest request) {
        var user = Users.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .surname(request.getSurname())
                .kayit_tarihi(convertStringToDate(request.getKayit_tarihi()))
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(Role.fromValue(request.getUserRole()))
                .bolumId(request.getBolumId())
                .isActive(request.getIsActive())
                .build();
        repository.save(user);
        StringBuilder actionText = new StringBuilder(
                "[" + request.getIslem_yapan_admin() + ", "+ user.getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici, " + user.getUserId() + " numaralı " + user.getName() + " "+ user.getSurname() +" isimli kullanıcıyı " + EnumUtils.getTurkishRole(user.getUserRole())  +" rolü ile sisteme kayıt etti.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(request.getIslem_yapan_admin()).bolumId(user.getBolumId()).actionText(actionText.toString()).build());

        return AuthenticationResponse.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).kayit_tarihi(user.getKayit_tarihi()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).isActive(user.isActive()).build();
    }
    /*public AuthResponseWithInfos authenticate(AuthenticationRequest request) {
        //BolumInfo.builder().malzemelerVarmi(bolum.getMalzemelerVarmi()).yilSayisi(bolum.getYilSayisi()).ogrenciNotVerebilirmi(bolum.getOgrenciNotVerebilirmi()).build()
        //AuthenticationResponse.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).kayit_tarihi(user.getKayit_tarihi()).password(user.getPassword()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).token(jwtToken).build();
        try{
            authenticationManager.authenticate( new CustomAuthenticationToken(request.getUserId(), request.getPassword(), request.getBolumId()));

        }catch (Error e){
            login_recordsRepository.save(Login_Records.builder().actionTime(ZonedDateTime.now())..build());
        }
        var user = repository.getKullaniciByIdandBolum(request.getUserId(), request.getBolumId());
        System.out.println(user.toString());
        var jwtToken = jwtService.generateToken(user);
        var bolum = bolumRepository.getBolum(user.getBolumId());
        return AuthResponseWithInfos.builder().authenticationResponse(AuthenticationResponse.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).kayit_tarihi(user.getKayit_tarihi()).password(user.getPassword()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).token(jwtToken).build()).bolumInfo(BolumInfo.builder().malzemelerVarmi(bolum.getMalzemelerVarmi()).yilSayisi(bolum.getYilSayisi()).ogrenciNotVerebilirmi(bolum.getOgrenciNotVerebilirmi()).secondGraderId(bolum.getSecondGraderId()).skillCount(skillsRespository.getSkillCountFromBolumId(bolum.getBolumId())).build()).build();
    }*/

    public AuthResponseWithInfos authenticate(AuthenticationRequest request) {
        Login_Records loginRecord = null;
        boolean isFailed = false;
        String failedReason = "";
        try {
            authenticationManager.authenticate(new CustomAuthenticationToken(request.getUserId(), request.getPassword(), request.getBolumId()));
            // Oturum açma başarılı
            loginRecord = Login_Records.builder()
                    .actionTime(Instant.now())
                    .ipAddress(clientIpBean.getClientIp())
                    .requestedUserId(request.getUserId())
                    .requestedBolumId(request.getBolumId())
                    .isSuccess(true)
                    .build();
            if (loginRecord != null) {
                login_recordsRepository.save(loginRecord);
            }
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                failedReason = "Şifre yanlış";
            } else if (e instanceof UsernameNotFoundException) {
                failedReason = "Kullanıcı ID ve Bölüm ID eşleşmiyor";
            } else {
                failedReason = "Kimlik doğrulama hatası";
            }
            // Oturum açma başarısız
            loginRecord = Login_Records.builder()
                    .actionTime(Instant.now())
                    .ipAddress(clientIpBean.getClientIp())
                    .requestedUserId(request.getUserId())
                    .requestedBolumId(request.getBolumId())
                    .isSuccess(false)
                    .failedReason(failedReason)
                    .build();


            if (loginRecord != null) {
                login_recordsRepository.save(loginRecord);
            }
            throw e;
        }


        //authenticationManager.authenticate(new CustomAuthenticationToken(request.getUserId(), request.getPassword(), request.getBolumId()));


        var user = repository.getKullaniciByIdandBolum(request.getUserId(), request.getBolumId());
        System.out.println(user.toString());
        var jwtToken = jwtService.generateToken(user);
        var bolum = bolumRepository.getBolum(user.getBolumId());
        return AuthResponseWithInfos.builder().authenticationResponse(AuthenticationResponse.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).kayit_tarihi(user.getKayit_tarihi()).password(user.getPassword()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).token(jwtToken).build()).bolumInfo(BolumInfo.builder().malzemelerVarmi(bolum.getMalzemelerVarmi()).yilSayisi(bolum.getYilSayisi()).ogrenciNotVerebilirmi(bolum.getOgrenciNotVerebilirmi()).secondGraderId(bolum.getSecondGraderId()).skillCount(skillsRespository.getSkillCountFromBolumId(bolum.getBolumId())).build()).build();
    }



    public BolmFacUni getBolFacUni(GetById getById){
        List<Users> users = repository.getAllUsersById(getById.getUserId(), Role.fromValue(getById.getUserRole()));
        List<Bolum> bolums = new ArrayList<>();
        List<Faculty> faculties = new ArrayList<>();
        List<University> unis = new ArrayList<>();
        for(Users user : users){
            Optional<Bolum> bolum = bolumRepository.findById(user.getBolumId());
            if (bolum.isPresent()) {
                bolums.add(bolum.get());
            }
        }
        for(Bolum bolum : bolums){
            Optional<Faculty> faculty = facultyRepository.findById(bolum.getFacId());
            int count = 0;
            if (faculty.isPresent()) {
                for(Faculty fac : faculties){
                    if(Objects.equals(fac.getFacId(), faculty.get().getFacId())){
                        count ++;
                    }
                }
                if(count == 0){
                    faculties.add(faculty.get());
                }

            }
        }
        for(Faculty faculty : faculties){
            Optional<University> uni = universityRepository.findById(faculty.getUniId());
            int count = 0;
            if (uni.isPresent()) {
                for(University unies : unis){
                    if(Objects.equals(unies.getUniId(), uni.get().getUniId())){
                        count ++;
                    }
                }
                if(count == 0){
                    unis.add(uni.get());
                }
            }
        }
        BolmFacUni bolmFacUni = new BolmFacUni(unis,faculties,bolums);
        System.out.println(bolmFacUni);
        return bolmFacUni;
    }

    public AuthenticationResponse changepassword(ChangePassword changePassword) {
        Users user = repository.getKullaniciByIdandBolumAndPassword(changePassword.getUserId(), changePassword.getBolumId(), changePassword.getPassword());
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        repository.save(user);
        StringBuilder actionText = new StringBuilder(
                "[" + changePassword.getUserId() + ", "+ changePassword.getBolumId() + "] numaralı userId ve bolumId ye sahip kullanıcı kendi şifresini güncelledi. " );
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(changePassword.getUserId()).bolumId(user.getBolumId()).actionText(actionText.toString()).build());

        return AuthenticationResponse.builder().userId(user.getUserId()).name(user.getName()).surname(user.getSurname()).kayit_tarihi(user.getKayit_tarihi()).password(user.getPassword()).userRole(user.getUserRole().getValue()).bolumId(user.getBolumId()).build();
    }

    // overload method for admin.
    public ResponseEntity changepassword(PasswordChangeDTO passwordChangeDTO) {
        try {
            Users user = repository.getKullaniciByIdandBolum(passwordChangeDTO.getUserId(), passwordChangeDTO.getBolumId());
            if (user == null) {
                throw new IllegalArgumentException("Kullanıcı veya bölüm bulunamadı.");
            }
            user.setPassword(passwordEncoder.encode(passwordChangeDTO.getPassword()));
            repository.save(user);
            StringBuilder actionText = new StringBuilder(
                    "[" + passwordChangeDTO.getChangerId() + ", "+ passwordChangeDTO.getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici, " + passwordChangeDTO.getUserId() + " numaralı " + repository.getKullaniciByIdandBolum(passwordChangeDTO.getUserId(),passwordChangeDTO.getBolumId()).getName() + " "+ repository.getKullaniciByIdandBolum(passwordChangeDTO.getUserId(), passwordChangeDTO.getBolumId()).getSurname() +" isimli kullanıcının şifresini değiştirdi.");
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(passwordChangeDTO.getChangerId()).bolumId(passwordChangeDTO.getBolumId()).actionText(actionText.toString()).build());

        } catch (IllegalArgumentException e) {
            // Loglama yapmak veya kullanıcıya geri dönülecek bir hata mesajı hazırlamak.
            System.out.println("Hata: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Beklenmeyen diğer hatalar için genel bir yakalama bloğu
            System.out.println("Kullanıcı güncellenirken beklenmeyen bir hata oluştu: " + e.getMessage());
            throw new RuntimeException("Kullanıcı güncelleme işlemi sırasında bir hata oluştu.", e);
        }
        return ResponseEntity.ok().body("Şifre değiştirme işlemi başarılı");
    }

}

