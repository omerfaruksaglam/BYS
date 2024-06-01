package com.baskent.Nursapp.Entity.SecondGraderSinif;


import com.baskent.Nursapp.Entity.User.SuperKeyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "secondgradersinif")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SecondGraderSinifSuperKeyId.class)

public class SecondGraderSinif {
    @Id
    @Column(name = "second_grader_id")
    private Long secondGraderId;

    @Id
    @Column(name = "sinif_numarasi")
    private Long sinifNumarasi;

}
