package kr.hvy.blog.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContentNoBody implements Serializable {

    private static final long serialVersionUID = -1192209274850300944L;

    private int id;
    private String subject;
    private String categoryName;
    private int viewCount;
    private java.sql.Timestamp createDate;
    private java.sql.Timestamp updateDate;

}

