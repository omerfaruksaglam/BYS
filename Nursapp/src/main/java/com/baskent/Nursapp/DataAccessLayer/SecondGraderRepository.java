package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.SecondGrader;
import com.baskent.Nursapp.Entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondGraderRepository extends JpaRepository<SecondGrader, Long> {
    @Query("SELECT k FROM SecondGrader k WHERE k.secondGraderId = :secondGraderId")
    SecondGrader getSecondGraderFromId(@Param("secondGraderId") Long secondGraderId);
}
