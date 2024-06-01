package com.baskent.Nursapp.Auth.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private Long userId;
    private String name;
    private String surname;
    private Date kayit_tarihi;
    private String password;
    private int userRole;
    private Long bolumId;
    private String token;
    private Boolean isActive;
}
