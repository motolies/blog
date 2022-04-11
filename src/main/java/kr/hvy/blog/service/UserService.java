package kr.hvy.blog.service;

import kr.hvy.blog.entity.User;
import kr.hvy.blog.model.request.LoginRequestDto;

public interface UserService {

    User findByUsername(String username);

    String login(LoginRequestDto loginDto);
}
