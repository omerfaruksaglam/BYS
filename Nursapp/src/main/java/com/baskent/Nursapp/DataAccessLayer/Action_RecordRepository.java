package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.ActionRecords.ActionRecordsSuperKeyId;
import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Action_RecordRepository extends JpaRepository<Action_Records, ActionRecordsSuperKeyId> {

    List<Action_Records> getAllByBolumId(Long bolumId);
}
