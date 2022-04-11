package kr.hvy.blog.service;

import kr.hvy.blog.mapper.UuidMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("uuidService")
@RequiredArgsConstructor
public class UuidServiceImpl implements UuidService {

    private final UuidMapper uuidMapper;

    public String uuid() {
        return uuidMapper.uuid();
    }


}
