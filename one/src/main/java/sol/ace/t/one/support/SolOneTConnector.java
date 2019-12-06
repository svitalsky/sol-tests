package sol.ace.t.one.support;

import com.solacesystems.jcsmp.*;

import static sol.ace.t.one.support.Config.CONFIG;

public class SolOneTConnector {
    public JCSMPSession connect() throws JCSMPException {
        return connect(true);
    }

    public JCSMPSession connect(boolean defaultClient) throws JCSMPException {
        JCSMPSession session = JCSMPFactory.onlyInstance().createSession(prepareProperties(defaultClient));
        session.connect();
        return session;
    }

    private JCSMPProperties prepareProperties(boolean defaultClient) {
        JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, CONFIG.getProperty("solace.host"));
        properties.setProperty(JCSMPProperties.VPN_NAME,  CONFIG.getProperty("solace.vpn"));
        if (CONFIG.getBoolProperty("solace.kerberos")) {
            properties.setProperty(JCSMPProperties.AUTHENTICATION_SCHEME, JCSMPProperties.AUTHENTICATION_SCHEME_GSS_KRB);
            properties.setProperty(JCSMPProperties.KRB_SERVICE_NAME, CONFIG.getProperty("solace.kerberos.service-name"));
        }
        else {
            if (defaultClient) {
                System.out.println("Using default client");
                properties.setProperty(JCSMPProperties.USERNAME, CONFIG.getProperty("solace.username"));
                properties.setProperty(JCSMPProperties.PASSWORD, CONFIG.getProperty("solace.password"));
            }
            else {
                System.out.println("Using another client");
                properties.setProperty(JCSMPProperties.USERNAME, CONFIG.getProperty("solace.another.username"));
                properties.setProperty(JCSMPProperties.PASSWORD, CONFIG.getProperty("solace.another.password"));
            }
        }
        return properties;
    }
}
