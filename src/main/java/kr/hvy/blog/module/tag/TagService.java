package kr.hvy.blog.module.tag;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import kr.hvy.blog.module.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TagService {

    @PersistenceContext
    private EntityManager em;

    private final TagRepository tagRepository;

    @Transactional
    public Tag save(Tag tag) {
        Tag existingTag = tagRepository.findByName(tag.getName());
        if (existingTag == null) {
            tagRepository.saveAndFlush(tag);
            em.detach(tag);
            return tagRepository.findById(tag.getId())
                    .orElseThrow(() -> new IllegalArgumentException("태그가 존재하지 않습니다."));
        } else {
            return existingTag;
        }
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Transactional
    public void deleteById(int id) {
        tagRepository.deleteById(id);
    }

    public Set<Tag> findByNameContainingOrderByName(String name) {
        if (StringUtils.isBlank(name)) {
            return tagRepository.findAllByOrderByName();
        } else {
            return tagRepository.findByNameContainingOrderByName(name);
        }
    }

    public Set<Tag> findByIdIn(Set<Integer> ids) {
        return tagRepository.findByIdIn(ids);
    }

    public Tag findById(int id) {
        return tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("태그가 존재하지 않습니다."));

    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }
}
