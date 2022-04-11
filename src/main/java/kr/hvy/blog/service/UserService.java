package kr.hvy.blog.service;

import kr.hvy.blog.model.User;

public interface UserService {

    User findByUsername(String username);
}
