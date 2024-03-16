package kr.hvy.blog.entity.provider;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@Slf4j
@RequiredArgsConstructor
public class CustomUUIDProvider implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
    return (Serializable) session.createQuery("SELECT FN_ORDERED_UUID()", byte[].class);
  }

}