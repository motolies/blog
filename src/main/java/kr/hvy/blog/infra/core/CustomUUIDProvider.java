package kr.hvy.blog.infra.core;

import java.io.Serializable;
import java.util.Arrays;
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
    return (Serializable) Arrays.stream(session.createQuery("SELECT FN_ORDERED_UUID()", Object[].class).uniqueResult())
        .toList()
        .get(0);
  }
}