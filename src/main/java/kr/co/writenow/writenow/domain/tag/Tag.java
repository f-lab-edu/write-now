package kr.co.writenow.writenow.domain.tag;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@Entity
@Table(name = "TAG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_NO")
    private Long tagNo;

    @Column(name = "CONTENT")
    private String content;

    public Tag(String content){
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagNo, tag.tagNo) && Objects.equals(content, tag.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagNo, content);
    }
}
