package kr.hvy.blog.service;

import kr.hvy.blog.entity.RsaMap;
import kr.hvy.blog.entity.User;
import kr.hvy.blog.model.request.LoginDto;
import kr.hvy.blog.repository.RsaMapRepository;
import kr.hvy.blog.repository.UserRepository;
import kr.hvy.blog.security.JwtTokenProvider;
import kr.hvy.blog.security.RSAEncryptHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    @PersistenceContext
    private EntityManager em;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final RsaMapRepository rsaMapRepository;
    private final UserRepository userRepository;

    public User findById(byte[] id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String login(LoginDto loginDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        User user = userRepository.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        RsaMap rsaMap = rsaMapRepository.findById(RSAEncryptHelper.getKeyBytes(loginDto.getRsaKey()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키입니다."));
        String passwd = RSAEncryptHelper.getDecryptMessage(loginDto.getPassword(), rsaMap.getPrivateKey());

        if (!passwordEncoder.matches(passwd, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.createToken(user);

    }

}
