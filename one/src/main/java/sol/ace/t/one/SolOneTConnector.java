package sol.ace.t.one;

import com.solacesystems.jcsmp.*;

class SolOneTConnector {
    JCSMPSession connect() throws JCSMPException {
        JCSMPSession session = JCSMPFactory.onlyInstance().createSession(prepareProperties());
        session.connect();
        return session;
    }

    private JCSMPProperties prepareProperties() {
        JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, "tcp://mr1qvxdm3zgl19.messaging.solace.cloud:20128");
        properties.setProperty(JCSMPProperties.USERNAME, "solace-cloud-client");
        properties.setProperty(JCSMPProperties.VPN_NAME,  "msgvpn-1u6o37qn54lb");
        properties.setProperty(JCSMPProperties.PASSWORD, "126sognumc4fk0hqgb4rt2gn9h");
        return properties;
    }
}
