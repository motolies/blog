package kr.hvy.blog.repository;

import kr.hvy.blog.entity.SearchEngine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchEngineRepository extends JpaRepository<SearchEngine, Integer> {

    List<SearchEngine> findAll();

}
