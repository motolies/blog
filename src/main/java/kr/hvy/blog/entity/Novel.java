package kr.hvy.blog.entity;

import kr.hvy.blog.util.TimeUtil;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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
