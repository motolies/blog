package kr.hvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hvy.blog.entity.AuthorityName;
import kr.hvy.blog.entity.RsaMap;
import kr.hvy.blog.entity.User;
import kr.hvy.blog.model.request.LoginDto;
import kr.hvy.blog.model.response.LoginResponseDto;
import kr.hvy.blog.model.response.MyProfileDto;
import kr.hvy.blog.security.RSAEncryptHelper;
import kr.hvy.blog.service.RsaMapService;
import kr.hvy.blog.service.UserService;
import kr.hvy.blog.util.CookieProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "로그인, 로그아웃")
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.header.name}")
    private String tokenHeader;

    private final RsaMapService rsaMapService;

    private final UserService userService;


    @Operation(summary = "로그인한 사용자 조회")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getMeInfo(Authentication auth) {

        // 토큰에서 정보 빼서 넘겨준다
        User user = userService.findByUsername(auth.getName());

        List<AuthorityName> roles = user.getAuthority().stream().map(a -> {
            return a.getName();
        }).collect(Collectors.toList());

        MyProfileDto profile = MyProfileDto.builder()
                .LoginId(user.getUsername())
                .UserName(user.getName())
                .Role(roles)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profile);
    }

    @Operation(summary = "로그인 패스워드 암호화를 위한 공개키 조회")
    @RequestMapping(value = "/shake", method = RequestMethod.POST)
    public ResponseEntity<?> createRsaKeyToken() throws Exception {
        //rsa key 삽입
        Map<String, Object> pair = RSAEncryptHelper.makeKey();
        byte[] publicKey = (byte[]) pair.get("publicKey");
        byte[] privateKey = (byte[]) pair.get("privateKey");

        RsaMap map = new RsaMap();
        map.setPublicKey(publicKey);
        map.setPrivateKey(privateKey);
        rsaMapService.save(map);
        rsaMapService.deleteByUpdateDate();

        HashMap<String, Object> a = new HashMap<String, Object>();
        a.put("rsaKey", pair.get("publicKeyString"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(a);
    }


    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = kr.hvy.blog.model.response.LoginResponseDto.class))})
    @PostMapping(value = {"login"})
    public ResponseEntity login(@RequestBody LoginDto loginDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        String token = userService.login(loginDto);
        ResponseCookie springCookie = CookieProvider.setSpringCookie(tokenHeader, token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(LoginResponseDto.builder().token(token).build());

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = {"logout"})
    public ResponseEntity logout() {
        ResponseCookie springCookie = CookieProvider.removeSpringCookie(tokenHeader);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .build();

    }
}
