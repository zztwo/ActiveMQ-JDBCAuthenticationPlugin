package indiv.zztwo.activemq.security;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.jaas.GroupPrincipal;
import org.apache.activemq.security.AbstractAuthenticationBroker;
import org.apache.activemq.security.SecurityContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by zhouzheng on 16/8/3.
 */
public class JdbcAuthenticationBroker extends AbstractAuthenticationBroker {
    private JdbcTemplate jdbcTemplate;
    private String verifySql;

    public JdbcAuthenticationBroker(Broker next) {
        super(next);
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        SecurityContext securityContext = context.getSecurityContext();
        if (securityContext == null) {
            securityContext = authenticate(info.getUserName(), info.getPassword(), null);
            context.setSecurityContext(securityContext);
            this.securityContexts.add(securityContext);
        }
        try {
            super.addConnection(context, info);
        } catch (Exception e) {
            this.securityContexts.remove(securityContext);
            context.setSecurityContext(null);
            throw e;
        }

        //Map<String, Object> result = this.getJdbcTemplate().query(this.getVerifySql(), info.getUserName(), info.getPassword());

        super.addConnection(context, info);
    }

    public SecurityContext authenticate(String username, String password, X509Certificate[] certificates) throws SecurityException {
        Map<String, Object> result = this.getJdbcTemplate().queryForMap(this.getVerifySql(), username);

        if (result == null || !password.equals(result.get("password"))) {
            throw new SecurityException("User name [" + username + "] or password is invalid.");
        }

        final Set<Principal> groups = new HashSet();
        StringTokenizer tokenizer = new StringTokenizer(result.get("groups").toString(), ",");
        while (tokenizer.hasMoreTokens()) {
            String name = tokenizer.nextToken().trim();
            groups.add(new GroupPrincipal(name));
        }

        SecurityContext securityContext = new SecurityContext(username) {
            public Set<Principal> getPrincipals() {
                return groups;
            }
        };

        return securityContext;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getVerifySql() {
        return verifySql;
    }

    public void setVerifySql(String verifySql) {
        this.verifySql = verifySql;
    }
}
