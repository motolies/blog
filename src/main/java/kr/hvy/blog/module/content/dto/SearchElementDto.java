package kr.hvy.blog.module.content.dto;

import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SearchElementDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -7032938059288406829L;

    private String id;

    private String name;

    @Override
    public String toString() {
        return "SearchElementDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
