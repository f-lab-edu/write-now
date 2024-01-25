package kr.co.writenow.writenow.domain.user;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FOLLOW")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FOLLOWER")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "FOLLOWEE")
    private User followee;

    public Follow(User follower, User followee){
        this.follower = follower;
        this.followee = followee;
    }

}
