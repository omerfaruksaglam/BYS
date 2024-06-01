package com.baskent.Nursapp.Auth.Models;

import com.baskent.Nursapp.Teacher.DTO.BolumInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseWithInfos {
    private AuthenticationResponse authenticationResponse;
    private BolumInfo bolumInfo;
}
