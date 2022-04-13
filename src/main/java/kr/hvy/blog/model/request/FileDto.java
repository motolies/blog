package kr.hvy.blog.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileDto {
    private int contentId;
    private MultipartFile file;
}
