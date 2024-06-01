package com.baskent.Nursapp.Teacher.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BolumInfo {
    private Long yilSayisi;
    private Boolean ogrenciNotVerebilirmi;
    private Boolean malzemelerVarmi;
    private Long secondGraderId;
    private Long skillCount;
    //private Long secondGraderId;
}
