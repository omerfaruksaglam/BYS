package com.baskent.Nursapp.Entity.LoginRecords;

import java.io.Serializable;
import java.time.Instant;

public class LoginSuperKeyId implements Serializable {
    private Instant actionTime;

    private Long requestedUserId;

    private Long requestedBolumId;

}
