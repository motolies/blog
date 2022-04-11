package kr.hvy.blog.service;

import kr.hvy.blog.model.RsaMap;
import kr.hvy.blog.repository.RsaMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service("rsaMapService")
public class RsaMapServiceImpl implements RsaMapService {

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
