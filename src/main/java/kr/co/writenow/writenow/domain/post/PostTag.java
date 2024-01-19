package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.tag.Tag;

@Entity
@Table(name = "POST_TAG")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TAG_NO")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "POST_NO")
    private Post post;
}
