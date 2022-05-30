package kr.hvy.blog.service;

import kr.hvy.blog.entity.SearchEngine;
import kr.hvy.blog.repository.SearchEngineRepository;
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
