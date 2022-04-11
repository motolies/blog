package kr.hvy.blog.service;

import kr.hvy.blog.entity.RsaMap;

public interface RsaMapService {

    RsaMap findById(byte[] publicKey);

    void save(RsaMap map);

    void deleteByUpdateDate();

    void delete(RsaMap map);
}
