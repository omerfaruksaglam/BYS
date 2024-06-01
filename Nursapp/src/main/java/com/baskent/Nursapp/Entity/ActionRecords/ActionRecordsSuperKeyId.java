package com.baskent.Nursapp.Entity.ActionRecords;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ActionRecordsSuperKeyId implements Serializable {

    private Instant actionTime;

    private Long userid;

    private Long bolumId;

    public ActionRecordsSuperKeyId(){}

}
