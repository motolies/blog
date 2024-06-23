package kr.hvy.blog.module.helper.search;

import kr.hvy.blog.module.helper.search.domain.SearchEngine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchEngineRepository extends JpaRepository<SearchEngine, Integer> {

    List<SearchEngine> findAll();

}
