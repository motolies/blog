package kr.hvy.blog.module.content.dto;

import java.io.Serial;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContentNoBodyResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1192209274850300944L;

    private int id;
    private String subject;
    private String categoryName;
    private int viewCount;
    private java.sql.Timestamp createDate;
    private java.sql.Timestamp updateDate;

}

