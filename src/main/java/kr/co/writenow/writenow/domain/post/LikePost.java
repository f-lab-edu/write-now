package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.user.User;

@Entity
@Table(name = "LIKE_POST")
public class LikePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_POST_NO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_NO")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;


}
