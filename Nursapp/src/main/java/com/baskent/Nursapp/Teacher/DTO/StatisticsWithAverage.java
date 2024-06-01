package com.baskent.Nursapp.Teacher.DTO;

import com.baskent.Nursapp.Entity.Statistics.Statistics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsWithAverage {
    List<List<Statistics>> allStatistics;
    //List<Double> averages;
    Long skillCount;
}
