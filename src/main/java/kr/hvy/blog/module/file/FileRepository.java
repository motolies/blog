package kr.hvy.blog.module.file;

import kr.hvy.blog.module.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, byte[]> {

    List<File> findByContentId(int contentId);
}
