package com.baskent.Nursapp.Entity.ActionRecords;

import com.baskent.Nursapp.Entity.LoginRecords.LoginSuperKeyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "action_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ActionRecordsSuperKeyId.class)
public class Action_Records {

    @Id
    @Column(name = "actiontime")
    private Instant actionTime;

    @Column(name = "ip_address")
    private String ipAddress;
    @Id
    @Column(name = "userid")
    private Long userid;
    @Id
    @Column(name = "bolumid")
    private Long bolumId;

    @Column(name = "actiontext")
    private String actionText;
}
