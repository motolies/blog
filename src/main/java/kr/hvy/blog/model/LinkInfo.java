package kr.hvy.blog.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class LinkInfo implements Serializable,  Comparable<LinkInfo>  {

    private static final long serialVersionUID = -1891101554187696900L;

    private String link;
    private String title;
    private int seq;

    @Override
    public int compareTo(LinkInfo other) {
        return Integer.compare(this.seq, other.seq);
    }
}
