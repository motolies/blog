package kr.hvy.blog.service;

import kr.hvy.blog.entity.Content;
import kr.hvy.blog.entity.File;
import kr.hvy.blog.repository.ContentRepository;
import kr.hvy.blog.repository.FileRepository;
import kr.hvy.blog.util.FileHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {


    private final FileRepository fileRepository;

    private final ContentRepository contentRepository;

    private static Path rootLocation;

    @Value("${path.upload}")
    public void setRootLocation(String path) {
        this.rootLocation = Paths.get(path);
    }

    public File load(byte[] id) {
        return fileRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(byte[] id) {

        File file = fileRepository.findById(id).orElse(null);

        if (file != null) {
            String path = file.getPath();
            fileRepository.delete(file);

            FileHelper.deleteFile(rootLocation.toString(), path);
        }
    }

    public Resource loadAsResource(String fileName) throws Exception {
        try {
            if (fileName.toCharArray()[0] == '/') {
                fileName = fileName.substring(1);
            }

            // 불러올 때 폴더
            String realPath = rootLocation.toString().replace(java.io.File.separatorChar, '/') + java.io.File.separator + fileName;

            Path file = loadPath(realPath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("Could not read file: " + fileName);
            }
        } catch (Exception e) {
            throw new Exception("Could not read file: " + fileName);
        }
    }

    private Path loadPath(String fileName) {
        return rootLocation.resolve(fileName);
    }

    public File store(MultipartFile file, int contentId) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String saveFileName = FileHelper.fileSave(rootLocation.toString(), file, contentId);

            if (saveFileName.toCharArray()[0] == '/') {
                saveFileName = saveFileName.substring(1);
            }

            Resource resource = loadAsResource(saveFileName);

            Content content = contentRepository.findById(contentId).orElse(null);
            if (content == null) {
                throw new Exception("Content is Null (ID) = " + contentId);
            }

            File sfile = new File();
            sfile.setContent(content);
            sfile.setOriginFileName(file.getOriginalFilename());
            sfile.setFileSize(resource.contentLength());
            sfile.setType(file.getContentType());
            sfile.setPath(saveFileName);

            sfile = fileRepository.save(sfile);

            return sfile;
        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public List<File> findByContentId(int contentId) {
        return fileRepository.findByContentId(contentId);
    }

}
