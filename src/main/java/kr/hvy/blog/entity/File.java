package kr.hvy.blog.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import kr.hvy.blog.annotation.SpecialCharacterListener;
import kr.hvy.blog.util.ByteUtil;
import kr.hvy.blog.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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

  @JsonGetter("id")
  public String getHexId() {
    return ByteUtil.byteArrayToHex(this.id);
  }

  @JsonSetter("id")
  public void setHexId(String id) {
    this.id = ByteUtil.hexToByteArray(id);
  }

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

  @JsonProperty("isDelete")
  @Column(name = "IsDelete", nullable = false, length = 1)
  private boolean isDelete;

  @Column(name = "CreateDate")
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
    this.createDate = TimeUtil.getUtcTimestamp();
    this.resourceUri = "/api/file/" + ByteUtil.byteArrayToHex(this.id);
  }

}
