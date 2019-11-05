package sol.ace.t.one.procon;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.MsgProducers;

import static sol.ace.t.one.Config.CONFIG;

public class SolOneTProducer {
    public static void main(String[] args) throws JCSMPException {
        XMLMessageProducer producer = MsgProducers.producerSimple();
        Topic topic = JCSMPFactory.onlyInstance().createTopic(CONFIG.getProperty("solace.topic"));
        int count = CONFIG.getIntProperty("solace.count");
        for (int index = 0; index < count; index++) {
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            String text = "Hello world! Isn't this a fine day no. " + (index + 1) + "?!";
            msg.setText(text);
            producer.send(msg, topic);
        }
    }
}
