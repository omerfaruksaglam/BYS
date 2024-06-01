package com.baskent.Nursapp.Teacher.DTO;


import com.baskent.Nursapp.Entity.SecondGrader;
import com.baskent.Nursapp.Entity.SecondGraderSinif.SecondGraderSinif;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnemliBilgiler {
    private BolumInfo bolumInfo;
    private SecondGrader secondGrader;
    private List<SecondGraderSinif> secondGraderSinif;
}
