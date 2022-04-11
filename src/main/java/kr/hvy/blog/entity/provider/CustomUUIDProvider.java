package kr.hvy.blog.entity.provider;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class CustomUUIDProvider implements IdentifierGenerator, Configurable {
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
    }

    private static final String QUERY_CALL_STORE_FUNC = "{ ? = call fn_ordered_uuid() }";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        byte[] result = null;
        Connection connection = null;
        try {
            connection = session.getJdbcConnectionAccess().obtainConnection();
            CallableStatement callableStmt = connection.prepareCall(QUERY_CALL_STORE_FUNC);
            callableStmt.executeQuery();
            // get result from out parameter #1
            result = callableStmt.getBytes(1);
            log.debug("binary pk : {}", byteArrayToHex(result));
        } catch (SQLException sqlException) {
            throw new HibernateException(sqlException);
        } finally {
            try {
                if (!connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }
}