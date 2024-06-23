package kr.hvy.blog.module.helper.search.domain;

import io.github.motolies.util.time.TimeUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import kr.hvy.blog.module.content.annotation.SpecialCharacterListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


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
