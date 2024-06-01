package com.baskent.Nursapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bolum")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bolum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bolum_id")
    private Long bolumId;

    @Column(name = "bolum_name")
    private String bolumName;

    @Column(name = "yil_sayisi")
    private Long yilSayisi;

    @Column(name = "ogrenci_not_verebilirmi")
    private Boolean ogrenciNotVerebilirmi;

    @Column(name = "malzemeler_varmi")
    private Boolean malzemelerVarmi;

    @Column(name = "second_grader_id")
    private Long secondGraderId;

    @Column(name = "fac_id")
    private Long facId;
}
