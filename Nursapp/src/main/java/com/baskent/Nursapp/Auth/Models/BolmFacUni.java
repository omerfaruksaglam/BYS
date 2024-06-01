package com.baskent.Nursapp.Auth.Models;

import com.baskent.Nursapp.Entity.Bolum;
import com.baskent.Nursapp.Entity.Faculty;
import com.baskent.Nursapp.Entity.University;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BolmFacUni {
    List<University> unis;
    List<Faculty> facs;
    List<Bolum> bolums;
}
