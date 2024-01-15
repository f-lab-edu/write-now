package kr.co.writenow.writenow.domain.user;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails{

    private static final int MAX_LOGIN_FAIL_COUNT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "ID", unique = true)
    private String userId;

    @Column(name = "NICKNAME", unique = true)
    private String nickname;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "LOGIN_FAIL_COUNT")
    private Integer loginFailCount;

    @Column(name = "IS_LOCKED")
    private Boolean isLocked;

    @Column(name = "PROFILE_IMAGE_PATH")
    private String profileImagePath;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_NO"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_NO"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userNo, user.userNo) && Objects.equals(email, user.email) && Objects.equals(userId, user.userId) && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNo, email, userId, nickname);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(it-> new SimpleGrantedAuthority(it.getType().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isLocked;
    }

    public void locked() {
        this.isLocked = true;
    }

    public boolean isMaxLoginFailCount() {
        return this.loginFailCount == MAX_LOGIN_FAIL_COUNT;
    }
}
