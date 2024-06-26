package kr.hvy.blog.module.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class FileUtil {

    /**
     * @return 생성된 파일 명(유일한 값)
     * @throws IllegalStateException
     * @throws IOException
     */
    public static String fileSave(String uploadPath, MultipartFile file, int contentId) throws IllegalStateException, IOException {

        File uploadPathDir = new File(uploadPath);

        if (!uploadPathDir.exists()) {
            uploadPathDir.mkdirs();
        }

        // 파일 중복명 처리
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-", "");

        String originalfileName = file.getOriginalFilename();
        String fileExtension = getExtension(originalfileName);
        String saveFileName = genId + "." + fileExtension;

        //		String savePath = calcPath(uploadPath);

        String savePath = calcPath(uploadPath, contentId);

        File target = new File(uploadPath + savePath, saveFileName);

        FileCopyUtils.copy(file.getBytes(), target);

        return makeFilePath(uploadPath, savePath, saveFileName);
    }

    /**
     * 파일이름으로부터 확장자를 반환
     *
     * @param fileName 확장자를 포함한 파일 명
     * @return 파일 이름에서 뒤의 확장자 이름만을 반환
     */
    public static String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

    private static String calcPath(String uploadPath, int contentId) {
        // 저장할 때 폴더
        String groupPath = File.separator + calcGroupPath(contentId);
        String contentPath = File.separator + Integer.toString(contentId);


        makeDir(uploadPath, groupPath);
        makeDir(uploadPath + groupPath, contentPath);

        return groupPath + contentPath;
    }

    private static String calcGroupPath(int numericId) {
        double result = Math.floor((numericId + 1000) / 1000) * 1000;
        return String.valueOf(Math.round(result));
    }

    private static void makeDir(String uploadPath, String... paths) {

        System.out.println(paths[paths.length - 1] + " : " + new File(paths[paths.length - 1]).exists());
        if (new File(paths[paths.length - 1]).exists()) {
            return;
        }

        for (String path : paths) {
            File dirPath = new File(uploadPath + path);

            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
        }
    }

    private static String makeFilePath(String uploadPath, String path, String fileName) throws IOException {
        String filePath = uploadPath + path + File.separator + fileName;
        return filePath.substring(uploadPath.length()).replace(File.separatorChar, '/');
    }


    public static void deleteFile(String uploadPath, String fileName) {
        File file = new File(uploadPath + File.separatorChar + fileName);
        file.delete();
    }

    public static void deleteFolder(String uploadPath, int contentId) {
        String targetFolder = (uploadPath + File.separatorChar + calcGroupPath(contentId) + File.separatorChar + contentId);
        FileSystemUtils.deleteRecursively(new File(targetFolder));
    }

}
