package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "POST_IMAGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMG_NO")
    private Long imgNo;

    @ManyToOne
    @JoinColumn(name = "POST_NO")
    private Post post;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "PATH")
    private String filePath;

    public PostImage(Post post, String fileName, String filePath){
        this.post = post;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
