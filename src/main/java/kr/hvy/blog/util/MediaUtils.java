package kr.hvy.blog.util;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MediaUtils {
    
    private static final Map<String, MediaType> MEDIA_TYPE_MAP;
    
    static {
        MEDIA_TYPE_MAP = new HashMap<>();
        
        MEDIA_TYPE_MAP.put("JPEG", MediaType.IMAGE_JPEG);
        MEDIA_TYPE_MAP.put("JPG", MediaType.IMAGE_JPEG);
        MEDIA_TYPE_MAP.put("GIF", MediaType.IMAGE_GIF);
        MEDIA_TYPE_MAP.put("PNG", MediaType.IMAGE_PNG);
    }
    
    public static MediaType getMediaType(String type) {
        return MEDIA_TYPE_MAP.get(type.toUpperCase());
    }
    
    public static boolean containsImageMediaType(String mediaType) {
        return MEDIA_TYPE_MAP.containsValue(MediaType.valueOf(mediaType));
    }
    
}
