package com.baskent.Nursapp.Entity.SecondGraderSinif;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
public class SecondGraderSinifSuperKeyId implements Serializable {
    private Long secondGraderId;
    private Long sinifNumarasi;

    public SecondGraderSinifSuperKeyId(){}
}
