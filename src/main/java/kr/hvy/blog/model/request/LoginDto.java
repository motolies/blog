package kr.hvy.blog.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginDto implements Serializable {

    private static final long serialVersionUID = -2082798189809385756L;

    private String username;
    private String password;
    private String RsaKey;
}
