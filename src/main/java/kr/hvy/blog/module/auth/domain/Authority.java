package kr.hvy.blog.module.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import kr.hvy.blog.module.content.annotation.SpecialCharacterListener;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "authority")
@EntityListeners({SpecialCharacterListener.class})
public class Authority implements Serializable {

  @Serial
  private static final long serialVersionUID = 7361534753922626001L;

  @Column(name = "Id", nullable = false, length = 11)
  @Id
  @GeneratedValue(generator = "AUTHORITY_ID_GENERATOR")
  @org.hibernate.annotations.GenericGenerator(name = "AUTHORITY_ID_GENERATOR", strategy = "native")
  private int id;

  @Column(name = "NAME", length = 50)
  @NotNull
  private AuthorityName name;

  @JsonIgnore
  @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
  @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
  @JoinTable(name = "user_authority_map", joinColumns = {@JoinColumn(name = "AuthorityId")}, inverseJoinColumns = {@JoinColumn(name = "UserId")})
  private java.util.Set<User> user = new java.util.HashSet<User>();


}