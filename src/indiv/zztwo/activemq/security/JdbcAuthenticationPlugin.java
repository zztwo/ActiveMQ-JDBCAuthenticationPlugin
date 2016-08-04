package indiv.zztwo.activemq.security;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by zhouzheng on 16/8/3.
 */
public class JdbcAuthenticationPlugin implements BrokerPlugin {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Broker installPlugin(Broker parent) throws Exception {
        JdbcAuthenticationBroker broker = new JdbcAuthenticationBroker(parent);
        broker.setJdbcTemplate(this.getJdbcTemplate());
        return broker;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
