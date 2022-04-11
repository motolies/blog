package kr.hvy.blog.service;

import kr.hvy.blog.entity.Authority;
import kr.hvy.blog.entity.AuthorityName;

public interface AuthorityService {
     Authority findByName(AuthorityName name);
}
