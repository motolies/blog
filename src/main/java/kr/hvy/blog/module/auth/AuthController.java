package kr.hvy.blog.module.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import kr.hvy.blog.module.auth.domain.RsaHash;
import kr.hvy.blog.module.auth.dto.LoginDto;
import kr.hvy.blog.module.auth.dto.MyProfileResponseDto;
import kr.hvy.blog.infra.security.JwtTokenProvider;
import kr.hvy.blog.infra.security.JwtUser;
import kr.hvy.blog.infra.support.ByteUtil;
import kr.hvy.blog.infra.security.CookieProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "로그인, 로그아웃")
@RequestMapping("/api/auth")
public class AuthController {

  @Value("${jwt.header.name}")
  private String tokenHeader;


  private final RsaHashService rsaHashService;

  private final UserService userService;

  private final JwtTokenProvider jwtTokenProvider;


  @Operation(summary = "로그인한 사용자 조회")
  @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = MyProfileResponseDto.class))})
  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public ResponseEntity<?> getMeInfo(Authentication auth) {

    if (ObjectUtils.isEmpty(auth.getPrincipal())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 토큰에서 정보 빼서 넘겨준다
    byte[] userId = ((JwtUser) auth.getPrincipal()).getId();
    MyProfileResponseDto profile = userService.getMyProfile(userId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(profile);
  }

  @Operation(summary = "로그인 패스워드 암호화를 위한 공개키 조회")
  @RequestMapping(value = "/shake", method = RequestMethod.POST)
  public ResponseEntity<?> createRsaKeyToken() {
    RsaHash hash = rsaHashService.random();
    HashMap<String, Object> a = new HashMap<String, Object>();
    a.put("rsaKey", hash.getPublicKey());

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(a);
  }


  @Operation(summary = "로그인")
  @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = MyProfileResponseDto.class))})
  @PostMapping(value = {"login"})
  public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginDto loginDto)
      throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

    String token = userService.login(loginDto);
    ResponseCookie springCookie = CookieProvider.setSpringCookie(request, tokenHeader, token);

    String hexId = jwtTokenProvider.getUserHexId(token);
    MyProfileResponseDto profile = userService.getMyProfile(ByteUtil.hexToByteArray(hexId));

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, springCookie.toString())
        .body(profile);

  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Operation(summary = "로그아웃")
  @ApiResponse(responseCode = "200")
  @GetMapping(value = {"logout"})
  public ResponseEntity<?> logout(HttpServletRequest request) {
    ResponseCookie springCookie = CookieProvider.removeSpringCookie(request, tokenHeader);
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.SET_COOKIE, springCookie.toString())
        .build();

  }
}
