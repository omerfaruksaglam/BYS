package com.baskent.Nursapp.Entity.LoginRecords;

import com.baskent.Nursapp.Entity.Performs.PerformsSuperKeyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Table(name = "login_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(LoginSuperKeyId.class)
public class Login_Records {

    @Id
    @Column(name = "actiontime")
    private Instant actionTime;

    @Column(name = "ip_address")
    private String ipAddress;

    @Id
    @Column(name = "requested_user_id")
    private Long requestedUserId;

    @Id
    @Column(name = "requested_bolum_id")
    private Long requestedBolumId;

    @Column(name = "issuccess")
    private Boolean isSuccess;

    @Column(name = "failedreason")
    private String failedReason;

}
