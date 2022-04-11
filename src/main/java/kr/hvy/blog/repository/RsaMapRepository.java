package kr.hvy.blog.repository;

import kr.hvy.blog.model.RsaMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RsaMapRepository extends JpaRepository<RsaMap, byte[]> {

    @SuppressWarnings({"unchecked", "JpaQlInspection"})
    @Query(value = "DELETE FROM RSA_MAP WHERE CREATEDATE < DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY)", nativeQuery = true)
    void deleteByCreateDate();
}
