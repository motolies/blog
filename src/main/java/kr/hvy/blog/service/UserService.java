package kr.hvy.blog.service;

import kr.hvy.blog.entity.Authority;
import kr.hvy.blog.entity.AuthorityName;
import kr.hvy.blog.entity.User;
import kr.hvy.blog.entity.redis.RsaHash;
import kr.hvy.blog.model.request.LoginDto;
import kr.hvy.blog.model.response.MyProfileResponseDto;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final RsaHashService rsaHashService;
    private final UserRepository userRepository;

    public User findById(byte[] id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    public String login(LoginDto loginDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        RsaHash hash = rsaHashService.findByPublicKey(loginDto.getRsaKey())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 키입니다."));

        String passwd = RSAEncryptHelper.getDecryptMessage(loginDto.getPassword(), hash.getPrivateKeyBytes());

        if (!passwordEncoder.matches(passwd, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.createToken(user);
    }

    public MyProfileResponseDto getMyProfile(byte[] userId) {
        User user = findById(userId);

        List<AuthorityName> roles = user.getAuthority().stream().map(Authority::getName).collect(Collectors.toList());

        return MyProfileResponseDto.builder()
                .LoginId(user.getUsername())
                .UserName(user.getName())
                .Role(roles)
                .build();
    }

}
