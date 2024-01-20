package kr.co.writenow.writenow.domain.tag;

import jakarta.persistence.*;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import lombok.Getter;

import java.util.*;

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

    public Tag(String content){
        this.content = content;
    }

    public static Set<Tag> makeTagSet(String[] tagValues){
        if(tagValues.length == 0){
            return Collections.emptySet();
        }

        Set<Tag> tags = new HashSet<>();
        for(String value: tagValues){
            tags.add(new Tag(value));
        }
        return tags;
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
