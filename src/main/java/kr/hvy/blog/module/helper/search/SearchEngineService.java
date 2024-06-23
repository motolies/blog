package kr.hvy.blog.module.helper.search;

import kr.hvy.blog.module.helper.search.domain.SearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchEngineService {

    private final SearchEngineRepository searchEngineRepository;

    public List<SearchEngine> findAll() {
        return searchEngineRepository.findAll();
    }


}
