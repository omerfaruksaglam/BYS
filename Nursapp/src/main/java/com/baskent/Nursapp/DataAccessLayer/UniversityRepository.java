package com.baskent.Nursapp.DataAccessLayer;


import com.baskent.Nursapp.Entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
}
