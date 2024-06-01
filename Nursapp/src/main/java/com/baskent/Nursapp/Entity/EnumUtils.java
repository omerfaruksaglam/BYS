package com.baskent.Nursapp.Entity;

import java.util.HashMap;
import java.util.Map;

public class EnumUtils {

    private static final Map<Role, String> roleMap = new HashMap<>();

    static {
        roleMap.put(Role.TEACHER, "ÖĞRETİM ELEMANI");
        roleMap.put(Role.STUDENT, "ÖĞRENCİ");
        roleMap.put(Role.ADMIN, "YÖNETİCİ");
        roleMap.put(Role.SECOND_GRADER, "HEMŞİRE");
    }

    public static String getTurkishRole(Role role){
        return roleMap.getOrDefault(role, role.name());
    }

    public static String getActiveStatus(boolean isActive){
        return isActive ? "aktif" : "aktif değil";
    }

}
