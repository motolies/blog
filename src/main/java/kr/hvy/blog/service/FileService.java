package kr.hvy.blog.service;

import kr.hvy.blog.model.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File load(byte[] id);

    void deleteById(byte[] id);

    Resource loadAsResource(String fileName) throws Exception;

    File store(MultipartFile file, int contentId) throws Exception;

    List<File> findByContentId(int contentId);
}
