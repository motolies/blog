package kr.hvy.blog.service;

import kr.hvy.blog.mapper.UuidMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UuidService {

    private final UuidMapper uuidMapper;

    public String uuid() {
        return uuidMapper.uuid();
    }


}
