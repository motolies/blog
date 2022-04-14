package kr.hvy.blog.entity.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.spi.NativeQueryImplementor;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class CustomUUIDProvider implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        NativeQueryImplementor rows = session.createSQLQuery("SELECT FN_ORDERED_UUID()");
        return (byte[]) rows.list().get(0);
    }

}