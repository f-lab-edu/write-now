package kr.co.writenow.writenow.domain.tag;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import lombok.Getter;

@Getter
@Entity
@Table(name = "TAG")
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_NO")
    private Long tagNo;

    @Column(name = "CONTENT")
    private String content;
}
