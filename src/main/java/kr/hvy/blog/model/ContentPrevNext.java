package kr.hvy.blog.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContentPrevNext implements Serializable {

    private static final long serialVersionUID = -8943731195456273018L;

    private int prev;
    private int next;

}
