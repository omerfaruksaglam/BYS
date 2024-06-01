package com.baskent.Nursapp.Admin.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersResponseUser {
    private Long userId;
    private String name;
    private String surname;
    private int userRole;
    private Long bolumId;
    private boolean isActive;

}
