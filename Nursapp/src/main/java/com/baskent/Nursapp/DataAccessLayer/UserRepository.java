package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.Role;
import com.baskent.Nursapp.Entity.User.SuperKeyId;
import com.baskent.Nursapp.Entity.User.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<Users, SuperKeyId> {

    @Query("SELECT k FROM Users k WHERE k.userId = :userId AND k.bolumId = :bolumId AND k.password = :password AND k.isActive = true")
    Users getKullaniciByIdandBolumAndPassword(@Param("userId") Long userId, @Param("bolumId") Long bolumId, @Param("password") String password);
    @Query("SELECT k FROM Users k WHERE k.userId = :userId AND k.bolumId = :bolumId AND k.isActive = true")
    Users getKullaniciByIdandBolum(@Param("userId") Long userId, @Param("bolumId") Long bolumId);

    @Query("SELECT k FROM Users k WHERE k.userId = :userId AND k.bolumId = :bolumId AND k.userRole = :userRole AND k.isActive = true")
    Users getKullaniciByIdandBolumandRole(@Param("userId") Long userId, @Param("bolumId") Long bolumId, @Param("userRole") Role userRole);

    @Query("SELECT k FROM Users k WHERE k.userId = :userId AND k.userRole = :userRole AND k.isActive = true")
    List<Users> getAllUsersById(@Param("userId") Long userId, @Param("userRole") Role userRole);

    @Query("SELECT k FROM Users k WHERE k.bolumId = :bolumId")
    List<Users> getAllUsersByBolumId(@Param("bolumId") Long bolumId);


    @Query("SELECT k FROM Users k WHERE k.bolumId = :bolumId AND k.userRole = 1 AND k.isActive = true")
    List<Users> getAllUsersByBolumIdandRol(@Param("bolumId") Long bolumId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Users u WHERE u.userId = :id AND u.bolumId = :bolum ")
    void deleteByIdAndBolum(@Param("id") Long userId, @Param("bolum") Long bolumId);

    // Pasif hale getirilen kullanıcıyı tekrar aktif hale getirebilmek için kullanılır.
    @Query("SELECT k FROM Users k WHERE k.userId = :userId AND k.bolumId = :bolumId")
    Users getKullaniciByIdandBolumForActiveUptade(@Param("userId") Long userId, @Param("bolumId") Long bolumId);
}