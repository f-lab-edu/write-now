package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import kr.co.writenow.writenow.domain.tag.Tag;
import kr.co.writenow.writenow.domain.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "POST")
@Getter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_NO")
    private Long postNo;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User writer;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "LIKE_COUNT")
    private Integer likeCount;

    @Column(name = "CATEGORY_CODE")
    private String categoryCode;

    @OneToMany
    @JoinColumn(name = "IMG_NO")
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "POST_TAG",
            joinColumns = @JoinColumn(name = "POST_NO"),
            inverseJoinColumns = @JoinColumn(name = "TAG_NO"))
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "LIKE_POST",
            joinColumns = @JoinColumn(name = "POST_NO"),
            inverseJoinColumns = @JoinColumn(name = "USER_NO")
    )
    private Set<User> likeUsers;
}
