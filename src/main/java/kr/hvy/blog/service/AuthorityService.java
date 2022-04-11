package kr.hvy.blog.service;

import kr.hvy.blog.model.Authority;
import kr.hvy.blog.model.AuthorityName;

public interface AuthorityService {
     Authority findByName(AuthorityName name);
}
