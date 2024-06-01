package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.LoginRecords.LoginSuperKeyId;
import com.baskent.Nursapp.Entity.LoginRecords.Login_Records;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Login_RecordsRepository extends JpaRepository<Login_Records, LoginSuperKeyId> {

    List<Login_Records> getLogin_RecordsByRequestedBolumId(Long requestedBolumId);
}
