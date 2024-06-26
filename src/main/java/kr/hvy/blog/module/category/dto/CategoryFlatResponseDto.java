package kr.hvy.blog.module.category.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CategoryFlatResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -4865232214529293019L;

    private String Id;
    private String name;
    private int order;
    private String fullName;
    private String pId;

    @JsonGetter
    private String getTreeName() {

        Character c = (char) Integer.parseInt("3000", 16);

        long level = this.fullName.chars().filter(f -> f == '/').count() - 2;
        if (level == 0) {
            return this.name;
        }
        String prefixLevel = "";
        for (int i = 0; i < level; i++) {
            // 공백 특수문자

            prefixLevel += c;
        }
        return prefixLevel + "└─" + c + this.name;
    }

    public String getLabel() {
        return name;
    }
}
