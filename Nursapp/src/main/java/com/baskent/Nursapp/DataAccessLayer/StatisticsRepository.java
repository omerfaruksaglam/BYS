package com.baskent.Nursapp.DataAccessLayer;


import com.baskent.Nursapp.Entity.Skills;
import com.baskent.Nursapp.Entity.Statistics.Statistics;
import com.baskent.Nursapp.Entity.Statistics.StatisticsSuperKeyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, StatisticsSuperKeyId> {
    @Query("SELECT k FROM Statistics k WHERE k.bolumId = :bolumId AND k.studentId = :studentId AND k.classLevel = :classLevel ORDER BY skillId ")
    List<Statistics> getStatisticsByStudentIdBolumIdClassLevel(@Param("bolumId") Long bolumId, @Param("studentId") Long studentId, @Param("classLevel") Long classLevel);
}
