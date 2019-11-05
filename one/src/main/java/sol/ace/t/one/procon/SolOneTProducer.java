package sol.ace.t.one.procon;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.SimpleMsgProducer;

import static sol.ace.t.one.Config.CONFIG;

public class SolOneTProducer {
    public static void main(String[] args) throws JCSMPException {
        XMLMessageProducer producer = SimpleMsgProducer.producer();
        Topic topic = JCSMPFactory.onlyInstance().createTopic(CONFIG.getProperty("solace.topic"));
        for (int index = 0; index < 50; index++) {
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            String text = "Hello world! Isn't this a fine day no. " + (index + 1) + "?!";
            msg.setText(text);
            producer.send(msg, topic);
        }
    }
}
