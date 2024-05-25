package kr.hvy.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import kr.hvy.blog.annotation.SpecialCharacterListener;
import kr.hvy.blog.util.ByteUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Table(name = "user")
@EntityListeners({SpecialCharacterListener.class})
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = -5404087020256270934L;


  @Id
  @Column(columnDefinition = "BINARY(16)", name = "Id")
  @GenericGenerator(name = "customUuid", strategy = "kr.hvy.blog.entity.provider.CustomUUIDProvider")
  @GeneratedValue(generator = "customUuid")
  private byte[] id;

  public String getHexId() {
    return ByteUtil.byteArrayToHex(this.id);
  }

  @Column(name = "Name", nullable = false, length = 128)
  private String name;

  @Column(name = "LoginId", length = 32, unique = true)
  @NotNull
  private String username;

  @JsonIgnore
  @Column(name = "Password", length = 64)
  @NotNull
  private String password;


  @JsonIgnore
  @Column(name = "Salt")
  private String salt;

  @JsonIgnore
  @Column(name = "IsEnable")
  @NotNull
  private Boolean enabled;

  @JsonIgnore
  @ManyToMany(mappedBy = "user", targetEntity = Authority.class, fetch = FetchType.EAGER)
  @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
  private java.util.Set<Authority> authority;

  @JsonIgnore
  @OneToMany(mappedBy = "user", targetEntity = Content.class)
  @Cascade({CascadeType.SAVE_UPDATE, CascadeType.LOCK})
  @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
  private java.util.Set<Content> content;

}