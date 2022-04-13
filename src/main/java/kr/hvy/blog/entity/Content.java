package kr.hvy.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.hvy.blog.util.Common;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
//@EntityListeners({ SpecialCharacterListener.class })
@Table(name = "content")
public class Content implements Serializable {

    private static final long serialVersionUID = 3019806640718052303L;

    public Content() {
        this.createDate = this.updateDate = Common.getUtcTimestamp();
    }

    @Column(name = "Id", nullable = false, length = 11)
    @Id
    @GeneratedValue(generator = "CONTENT_ID_GENERATOR")
    @GenericGenerator(name = "CONTENT_ID_GENERATOR", strategy = "native")
    private int id;

    @NotNull
    @Column(name = "Subject", nullable = false, length = 512)
    private String subject = "";

    @NotNull
    @Column(name = "Body", nullable = false, columnDefinition = "LONGTEXT")
    private String body;


    @JsonIgnore
    @Column(name = "NormalBody", nullable = false, columnDefinition = "LONGTEXT")
    private String normalBody;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns({@JoinColumn(name = "UserId", referencedColumnName = "Id", nullable = false)})
    private User user;

    @Column(name = "CategoryId", columnDefinition = "VARCHAR(32)", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(generator = "CONTENT_CATEGORYID_GENERATOR")
    @GenericGenerator(name = "CONTENT_CATEGORYID_GENERATOR", strategy = "foreign", parameters = @Parameter(name = "property", value = "category"))
    private String categoryId;

    @Column(name = "SyncKey", columnDefinition = "VARCHAR(32)")
    private String syncKey;

    @Column(name = "IsBulkDone", nullable = false, length = 1)
    private boolean isBulkDone = false;

    @JsonIgnore
    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "CategoryId", referencedColumnName = "Id", nullable = false)}, foreignKey = @ForeignKey(name = "fk_content_categoryid_category_id"))
    private Category category;

    @Column(name = "IsPublic", nullable = false, length = 1)
    private boolean isPublic = false;

    @Column(name = "IsMain", nullable = false, length = 1)
    private boolean isMain = false;

    @Column(name = "ViewCount", nullable = false, length = 11)
    private int viewCount = 0;

    @Column(name = "CreateDate", nullable = false)
    private java.sql.Timestamp createDate;

    @Column(name = "UpdateDate", nullable = false)
    private java.sql.Timestamp updateDate;

    @PrePersist
    protected void onCreate() {
        createDate = updateDate = Common.getUtcTimestamp();
        updateNormalBody();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = Common.getUtcTimestamp();
        updateNormalBody();
    }

    @SuppressWarnings({"unchecked", "JpaQlInspection"})
    @OrderBy("Name ASC")
    @ManyToMany(mappedBy = "content", targetEntity = Tag.class)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
    private java.util.Set<Tag> tag = new java.util.HashSet<Tag>();

    @SuppressWarnings({"unchecked", "JpaQlInspection"})
//    @JsonIgnore
    @OrderBy("OriginFileName ASC")
    @OneToMany(mappedBy = "content", targetEntity = File.class)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
    private java.util.Set<File> file = new java.util.HashSet<File>();

    private void updateNormalBody() {
        if (this.body == null)
            this.normalBody = this.body = "";
        else
            this.normalBody = body.replaceAll("<[^>]*>", "");
    }

    // https://thoughts-on-java.org/hibernate-tips-the-best-way-to-remove-entities-from-a-many-to-many-association/
    public void removeTag(Tag tag) {
        this.tag.remove(tag);
        tag.getContent().remove(this);
    }

    public void addTag(Tag tag) {
        this.tag.add(tag);
        tag.getContent().add(this);
    }

    @Formula(value = "(SELECT IFNULL(MIN(n.id),0) FROM Content as n WHERE n.id > id)")
    private int next;

    @Formula(value = "(SELECT IFNULL(MAX(p.id),0) FROM Content as p WHERE p.id < id)")
    private int prev;

}
