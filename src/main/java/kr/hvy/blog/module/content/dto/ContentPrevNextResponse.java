package kr.hvy.blog.module.content.dto;

import java.io.Serial;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContentPrevNextResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8943731195456273018L;

    private int prev;
    private int next;

}
