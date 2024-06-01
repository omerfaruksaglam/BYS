package com.baskent.Nursapp.Auth.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Long userId;
    private String name;
    private String surname;
    private String kayit_tarihi;
    private String password;
    private int userRole;
    private Long bolumId;
    private Boolean isActive;
    private Long islem_yapan_admin;
}
