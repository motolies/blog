package kr.hvy.blog.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {

    private static final long serialVersionUID = -4865232214529293019L;

    private String Id;
    private String name;
    private int order;
    private String fullName;
    private String pId;

    public String getLabel() {
        return name;
    }
}
