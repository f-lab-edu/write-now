package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import kr.co.writenow.writenow.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<PostTag> tags = new HashSet<>();


    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<LikePost> likePosts = new ArrayList<>();

    public Post(User user, String content, String categoryCode) {
        this.writer = user;
        this.content = content;
        this.categoryCode = categoryCode;
        this.likeCount = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postNo, post.postNo) && Objects.equals(writer, post.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postNo, writer);
    }

    public void addPostTags(Set<PostTag> tags) {
        this.tags.addAll(tags);
    }

    public void addPostImages(List<PostImage> images) {
        this.postImages.addAll(images);
    }
}
