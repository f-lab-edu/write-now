package kr.co.writenow.writenow.domain.post;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;

@Entity
@Table(name = "POST_IMAGE")
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMG_NO")
    private Long imgNo;

    @Column(name = "POST_NO")
    private Long postNo;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "PATH")
    private String filePath;

}
