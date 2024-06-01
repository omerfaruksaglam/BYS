package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.Bolum;
import com.baskent.Nursapp.Entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BolumRepository extends JpaRepository<Bolum, Long> {
    @Query("SELECT k FROM Bolum k WHERE k.bolumId = :bolumId")
    Bolum getBolum(@Param("bolumId") Long bolumId);
}
