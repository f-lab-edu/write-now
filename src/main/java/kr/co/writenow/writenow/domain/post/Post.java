package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import kr.co.writenow.writenow.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "post")
    private Set<PostTag> tags;


    @OneToMany(mappedBy = "post")
    private List<LikePost> likePosts;
}
