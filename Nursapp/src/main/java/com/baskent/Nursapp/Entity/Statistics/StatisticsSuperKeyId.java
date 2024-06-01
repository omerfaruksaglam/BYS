package com.baskent.Nursapp.Entity.Statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsSuperKeyId implements Serializable {
    private Long studentId;
    private Long skillId;
    private Long classLevel;
    private Long bolumId;



}
