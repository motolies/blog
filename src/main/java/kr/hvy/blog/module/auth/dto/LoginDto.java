package kr.hvy.blog.module.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class LoginDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2082798189809385756L;

    private String username;
    private String password;
    private String RsaKey;
}
