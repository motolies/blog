package kr.hvy.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.hvy.blog.annotation.SpecialCharacterListener;
import kr.hvy.blog.util.Common;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners({SpecialCharacterListener.class})
@Table(name = "`file`")
public class File {

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "Id")
    @GenericGenerator(name = "customUuid", strategy = "kr.hvy.blog.entity.provider.CustomUUIDProvider")
    @GeneratedValue(generator = "customUuid")
    private byte[] id;

    @JsonBackReference
    @ManyToOne(targetEntity = Content.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns({@JoinColumn(name = "ContentId", referencedColumnName = "Id", nullable = false)})
    private Content content;

    @Column(name = "OriginFileName", nullable = false, length = 256)
    private String originFileName;

    @Column(name = "Type", nullable = false, length = 512)
    private String type;

    @JsonIgnore
    @Column(name = "Path", nullable = false, length = 512)
    private String path;

    @Column(name = "FileSize", nullable = false, columnDefinition = "BIGINT")
    private long fileSize;

    @Column(name = "IsDelete", nullable = false, length = 1)
    private boolean isDelete;

    @Column(name = "CreateDate", nullable = false)
    private java.sql.Timestamp createDate;

    @Transient
    private String resourceUri;

    @PostPersist
    private void onInsert() {
        makeResourceUri();
    }

    @PostLoad
    private void onLoad() {
        makeResourceUri();
    }

    private void makeResourceUri() {
        this.createDate = Common.getUtcTimestamp();
        this.resourceUri = "/file?id=" + Common.BinaryToEncodeURIBase64(this.id);
    }

}
