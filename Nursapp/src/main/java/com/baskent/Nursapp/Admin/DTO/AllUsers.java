package com.baskent.Nursapp.Admin.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AllUsers {
    List<AllUsersResponseUser> Teachers;
    List<AllUsersResponseUser> Nurses;
    List<AllUsersResponseUser> Students;
    List<AllUsersResponseUser> Admins;
}
