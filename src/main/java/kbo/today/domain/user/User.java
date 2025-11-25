package kbo.today.domain.user;

import jakarta.persistence.*;
import kbo.today.domain.common.BaseEntity;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    protected User() {}
    
    public User(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public UserRole getRole() {
        return role;
    }
}