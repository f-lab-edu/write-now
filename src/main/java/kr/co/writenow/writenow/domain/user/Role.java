package kr.co.writenow.writenow.domain.user;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "ROLE")
@Getter
public class Role {

    @Id
    @Column(name = "ROLE_NO")
    private Long roleNo;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private RoleType type;

    public Role() {
        this.roleNo = 100L;
        this.type = RoleType.ROLE_USER;
    }
}
