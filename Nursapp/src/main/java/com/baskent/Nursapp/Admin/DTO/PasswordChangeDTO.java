package com.baskent.Nursapp.Admin.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PasswordChangeDTO {
    private Long userId;
    private Long bolumId;
    private String password;
    private Long changerId;
}
