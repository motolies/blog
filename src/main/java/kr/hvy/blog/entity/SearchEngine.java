package kr.hvy.blog.entity;

import kr.hvy.blog.annotation.SpecialCharacterListener;
import kr.hvy.blog.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners({SpecialCharacterListener.class})
@Table(name = "`search`")
public class SearchEngine {

    @Column(name = "Id", nullable = false, length = 11)
    @Id
    @GeneratedValue(generator = "CONTENT_ID_GENERATOR")
    @GenericGenerator(name = "CONTENT_ID_GENERATOR", strategy = "native")
    private int id;

    @Column(name = "Name", nullable = false, length = 32)
    private String Name;

    @Column(name = "Url", nullable = false, length = 512)
    private String url;

    @Column(name = "`Order`", nullable = false, length = 11)
    private int order;

    @Column(name = "CreateDate", nullable = false)
    private java.sql.Timestamp createDate;

    @Column(name = "UpdateDate", nullable = false)
    private java.sql.Timestamp updateDate;

    @PrePersist
    protected void onCreate() {
        createDate = updateDate = TimeUtil.getUtcTimestamp();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = TimeUtil.getUtcTimestamp();
    }

}
