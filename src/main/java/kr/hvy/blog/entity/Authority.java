package kr.hvy.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.hvy.blog.annotation.SpecialCharacterListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "authority")
@EntityListeners({SpecialCharacterListener.class})
public class Authority implements Serializable {

    private static final long serialVersionUID = 7361534753922626001L;

    @Column(name = "Id", nullable = false, length = 11)
    @Id
    @GeneratedValue(generator = "AUTHORITY_ID_GENERATOR")
    @org.hibernate.annotations.GenericGenerator(name = "AUTHORITY_ID_GENERATOR", strategy = "native")
    private int id;

    @Column(name = "NAME", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
    @JoinTable(name = "user_authority_map", joinColumns = {@JoinColumn(name = "AuthorityId")}, inverseJoinColumns = {@JoinColumn(name = "UserId")})
    private java.util.Set<User> user = new java.util.HashSet<User>();


}