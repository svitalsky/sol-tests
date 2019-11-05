package sol.ace.t.one.support;

import com.solacesystems.jcsmp.*;

import static sol.ace.t.one.support.Config.CONFIG;

public class SolOneTConnector {
    public JCSMPSession connect() throws JCSMPException {
        JCSMPSession session = JCSMPFactory.onlyInstance().createSession(prepareProperties());
        session.connect();
        return session;
    }

    private JCSMPProperties prepareProperties() {
        JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, CONFIG.getProperty("solace.host"));
        properties.setProperty(JCSMPProperties.USERNAME, CONFIG.getProperty("solace.username"));
        properties.setProperty(JCSMPProperties.VPN_NAME,  CONFIG.getProperty("solace.vpn"));
        properties.setProperty(JCSMPProperties.PASSWORD, CONFIG.getProperty("solace.password"));
        return properties;
    }
}
