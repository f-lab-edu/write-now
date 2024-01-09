package kr.co.writenow.writenow.domain.user;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLE")
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
