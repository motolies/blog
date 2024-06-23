package kr.hvy.blog.module.file.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDto {
    private int contentId;
    private MultipartFile file;
}
