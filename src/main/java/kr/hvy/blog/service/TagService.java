package kr.hvy.blog.service;

import kr.hvy.blog.entity.Tag;

import java.util.List;

public interface TagService {

    Tag save(Tag tag);

    List<Tag> findAll();

    void deleteById(int id);

    List<Tag> findByNameContaining(String name);

    Tag findById(int id);
}
