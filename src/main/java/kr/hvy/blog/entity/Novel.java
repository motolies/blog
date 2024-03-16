package kr.hvy.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import kr.hvy.blog.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "seq"})})
public class Novel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "title", nullable = false, length = 128)
  private String title;

  @NotNull
  private int seq;

  @Setter
  @NotNull
  @Column(name = "Content", nullable = false, columnDefinition = "LONGTEXT")
  private String content;

  @Column(name = "CreateDate", nullable = false)
  private Timestamp createDate;

  @Column(name = "UpdateDate", nullable = false)
  private Timestamp updateDate;

  @PrePersist
  protected void onCreate() {
    createDate = updateDate = TimeUtil.getUtcTimestamp();
  }

  @PreUpdate
  protected void onUpdate() {
    updateDate = TimeUtil.getUtcTimestamp();
  }

}
