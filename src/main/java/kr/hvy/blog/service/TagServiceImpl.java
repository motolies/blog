package kr.hvy.blog.service;

import kr.hvy.blog.model.Tag;
import kr.hvy.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service("tagService")
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Transactional
    public void deleteById(int id) {
        tagRepository.deleteById(id);
    }

    public List<Tag> findByNameContaining(String name) {
        return tagRepository.findByNameContaining(name);
    }

    public Tag findById(int id) {
        return tagRepository.findById(id).orElse(null);
    }
}
