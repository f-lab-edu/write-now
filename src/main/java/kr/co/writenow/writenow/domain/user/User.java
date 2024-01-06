package kr.co.writenow.writenow.domain.user;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "EMAIL")
    private String email;
}
