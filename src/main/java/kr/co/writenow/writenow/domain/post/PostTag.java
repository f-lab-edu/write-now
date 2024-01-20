package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "POST_TAG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TAG_NO")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "POST_NO")
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostTag postTag = (PostTag) o;
        return Objects.equals(id, postTag.id) && Objects.equals(tag, postTag.tag) && Objects.equals(post, postTag.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, post);
    }

    public PostTag(Tag tag, Post post){
        this.tag = tag;
        this.post = post;
    }
}
