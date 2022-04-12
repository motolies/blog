package kr.hvy.blog.service;

import kr.hvy.blog.entity.RsaMap;
import kr.hvy.blog.repository.RsaMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RsaMapService {

    private final RsaMapRepository rsaMapRepository;

    public RsaMap findById(byte[] publicKey) {
        return rsaMapRepository.findById(publicKey).orElse(null);
    }

    public void save(RsaMap map) {
        rsaMapRepository.saveAndFlush(map);
    }

    public void deleteByUpdateDate() {
        rsaMapRepository.deleteByCreateDate();
    }

    public void delete(RsaMap map) {
        rsaMapRepository.delete(map);
    }

}
