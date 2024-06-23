package kr.hvy.blog.module.category.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kr.hvy.blog.module.content.domain.Content;
import kr.hvy.blog.module.content.annotation.SpecialCharacterListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Getter
@Setter
@EntityListeners({SpecialCharacterListener.class})
@Table(name = "category")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 6223962078070813686L;

    @Column(name = "Id", nullable = false, length = 32)
    @Id
    private String id;

    @Column(name = "Name", nullable = false, length = 64)
    private String name;

    @Column(name = "`Order`", nullable = false, length = 11)
    private int order;

    @Column(name = "FullName", nullable = false, length = 512)
    private String fullName;

    @Column(name = "FullPath", nullable = false, length = 512)
    private String fullPath;


    // 제네릭 생성과 초기화를 한 번에 하는 방법
    // https://www.baeldung.com/java-initialize-hashmap
    @Transient
    private Map<String, Boolean> state = Stream.of(new Object[][]{{"opened", true}, {"selected", false},})
            .collect(Collectors.toMap(data -> (String) data[0], data -> (Boolean) data[1]));

    // serialize deserialize 이름 다르게 하려면 아래처럼 get set 함수 위에 다른 프로퍼티명을 쓴다
    @Column(name = "PId", columnDefinition = "VARCHAR(32)")
//    @GeneratedValue(generator = "CATEGORY_PID_CATEGORYID_GENERATOR")
    @GenericGenerator(name = "CATEGORY_PID_CATEGORYID_GENERATOR", strategy = "foreign", parameters = @Parameter(name = "property", value = "p"))
    private String pId;

    @JsonSetter("pId")
    public void setPId(String pId) {
        this.pId = pId;
    }

    @JsonGetter("pId")
    public String getPId() {
        return this.pId;
    }

    @JsonBackReference
    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns({@JoinColumn(name = "PId", referencedColumnName = "Id", nullable = true, insertable = false, updatable = false)})
    private Category p;

    @JsonIgnore
    @OneToMany(mappedBy = "category", targetEntity = Content.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
    private java.util.Set<Content> content = new java.util.HashSet<Content>();


    @SuppressWarnings({"unchecked", "JpaQlInspection"})
    @JsonProperty("children")
    @JsonManagedReference
    @OrderBy("Order ASC, Name ASC")
    @OneToMany(mappedBy = "p", targetEntity = Category.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})
    @org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
    private java.util.List<Category> category = new java.util.ArrayList<Category>();


    @Formula("(select count(*) from Content as con where con.categoryId = id )")
    private int contentCount;

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


    @PrePersist
    protected void onCreate() {
        cleanUp();
    }

    @PreUpdate
    protected void onUpdate() {
        cleanUp();
    }

    public void cleanUp() {
        this.fullName = "/" + this.name + "/";
        this.fullPath = "/" + this.name + "/";
    }

}
