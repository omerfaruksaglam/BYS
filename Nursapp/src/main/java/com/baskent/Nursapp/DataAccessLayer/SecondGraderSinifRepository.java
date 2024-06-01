package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.SecondGrader;
import com.baskent.Nursapp.Entity.SecondGraderSinif.SecondGraderSinif;
import com.baskent.Nursapp.Entity.SecondGraderSinif.SecondGraderSinifSuperKeyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondGraderSinifRepository extends JpaRepository<SecondGraderSinif, SecondGraderSinifSuperKeyId> {
    @Query("SELECT k FROM SecondGraderSinif k WHERE k.secondGraderId = :secondGraderId order by k.sinifNumarasi ASC")
    List<SecondGraderSinif> getAllSecondGraderSinifFromId(@Param("secondGraderId") Long secondGraderId);
}
