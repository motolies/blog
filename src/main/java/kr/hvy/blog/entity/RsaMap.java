package kr.hvy.blog.entity;

import kr.hvy.blog.annotation.SpecialCharacterListener;
import kr.hvy.blog.util.Common;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "rsa_map")
@EntityListeners({SpecialCharacterListener.class})
public class RsaMap implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4481430333261050228L;

    @Id
    @Column(columnDefinition = "VARBINARY(512)", name = "PublicKey")
    private byte[] publicKey;

    @Column(columnDefinition = "VARBINARY(2048)", name = "PrivateKey")
    private byte[] privateKey;

    @Column(name = "CreateDate", columnDefinition = "TIMESTAMP(6)", nullable = false)
    private java.sql.Timestamp createDate;

    @PrePersist
    protected void onCreate() {
        createDate = Common.getUtcTimestamp();
    }

}
