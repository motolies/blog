package kr.hvy.blog.repository;

import kr.hvy.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, byte[]> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(byte[] id);


}
