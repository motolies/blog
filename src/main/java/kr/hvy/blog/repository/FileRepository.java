package kr.hvy.blog.repository;

import kr.hvy.blog.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, byte[]> {

    List<File> findByContentId(int contentId);
}
