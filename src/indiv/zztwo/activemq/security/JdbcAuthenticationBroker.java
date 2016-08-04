package indiv.zztwo.activemq.security;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

/**
 * Created by zhouzheng on 16/8/3.
 */
public class JdbcAuthenticationBroker extends BrokerFilter {
    private static Logger logger = Logger.getLogger(JdbcAuthenticationBroker.class);
    private JdbcTemplate jdbcTemplate;

    public JdbcAuthenticationBroker(Broker next) {
        super(next);
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        logger.info("=====================enter addConnection");

        Map<String, Object> result = this.getJdbcTemplate().queryForMap("select * from conn_users where username = ? ", info.getUserName());

        if (result == null || !info.getPassword().equals(result.get("password"))) {
            throw new SecurityException("User name [" + info.getUserName() + "] or password is invalid.");
        }

        super.addConnection(context, info);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
