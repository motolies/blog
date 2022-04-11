package kr.hvy.blog.service;

import kr.hvy.blog.entity.Authority;
import kr.hvy.blog.entity.AuthorityName;
import kr.hvy.blog.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Slf4j
@Service("authorityService")
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final EntityManager em;

    private final AuthorityRepository authorityRepository;

    public Authority findByName(AuthorityName name) {
        return authorityRepository.findByName(name);
    }

    @Transactional
    public void makeAdminUserDeptMap(String empNo) {
        Session session = em.unwrap(Session.class);
        try {
            StoredProcedureQuery query = session.createStoredProcedureQuery("usp_po_admin_user_dept_map_make");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.setParameter(1, empNo);
            query.execute();
        } catch (RuntimeException he) {
            he.printStackTrace();
            throw he;
        } finally {
            session.close();
        }
    }
}
