package kr.hvy.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.hvy.blog.annotation.SpecialCharacterListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EntityListeners({SpecialCharacterListener.class})
@Table(name = "`tag`")
public class Tag implements Serializable {

    private static final long serialVersionUID = 3493527380866893072L;

    @Column(name = "Id", nullable = false, length = 11)
    @Id
    @GeneratedValue(generator = "TAG_ID_GENERATOR")
    @org.hibernate.annotations.GenericGenerator(name = "TAG_ID_GENERATOR", strategy = "native")
    private int id;

    @Column(name = "Name", nullable = false, length = 64)
    private String name;

    @JsonIgnore
    @ManyToMany(targetEntity = Content.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
    @JoinTable(name = "content_tag_map", joinColumns = {@JoinColumn(name = "TagId")}, inverseJoinColumns = {@JoinColumn(name = "ContentId")})
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
    private java.util.Set<Content> content = new java.util.HashSet<Content>();

    @Formula("(select count(*) from content_tag_map as m where m.TagId = id)")
    private int ContentCount;

}
