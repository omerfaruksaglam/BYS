package com.baskent.Nursapp.Admin.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserUpdateDTO {
    private String name;
    private String surname;
    //private int userRole;
}
