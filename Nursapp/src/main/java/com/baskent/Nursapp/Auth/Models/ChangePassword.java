package com.baskent.Nursapp.Auth.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    private Long userId;
    private String password;
    private String newPassword;
    private Long bolumId;
}
