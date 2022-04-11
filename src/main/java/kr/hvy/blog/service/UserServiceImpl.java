package kr.hvy.blog.service;

import kr.hvy.blog.model.User;
import kr.hvy.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager em;

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
