package kr.hvy.blog.module.auth;

import kr.hvy.blog.module.auth.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, byte[]> {

    Optional<User> findByUsername(String username);

    @NotNull
    Optional<User> findById(byte[] id);


}
