package com.baskent.Nursapp.Entity.User;

import com.baskent.Nursapp.Entity.Role;
import com.baskent.Nursapp.Entity.User.SuperKeyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SuperKeyId.class)
public class Users implements UserDetails{

    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(nullable = false, length = 255, name = "namee")
    private String name;

    @Column(nullable = false, length = 255, name="surname")
    private String surname;

    @Column(name = "kayit_tarihi")
    private Date kayit_tarihi;

    @Column(length = 255, name="passwordd")
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_roles_id")
    private Role userRole;

    @Id
    @Column(name = "bolum_id")
    private Long bolumId;

    @Column(name = "isactive")
    private boolean isActive;

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return String.valueOf(bolumId)+ "/" +String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
