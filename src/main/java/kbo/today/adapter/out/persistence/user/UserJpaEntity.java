package kbo.today.adapter.out.persistence.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.enumerable.UserRole;

@Entity
@Table(name = "users")
public class UserJpaEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected UserJpaEntity() {}

    public UserJpaEntity(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public static UserJpaEntity from(User user) {
        return new UserJpaEntity(user.getEmail(), user.getPassword(), user.getNickname(), user.getRole());
    }

    public User toDomain() {
        return new User(getId(), email, password, nickname);
    }
}
