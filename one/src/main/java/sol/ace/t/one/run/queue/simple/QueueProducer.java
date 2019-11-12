package sol.ace.t.one.run.queue.simple;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.support.MsgProducers;

import static sol.ace.t.one.support.Config.CONFIG;

public class QueueProducer {
    public static void main(String[] args) throws JCSMPException, InterruptedException {
        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
        XMLMessageProducer producer = MsgProducers.producerSimple();
        int count = CONFIG.getIntProperty("solace.count");

        for (int i = 1; i <= 2 * count; i++) {
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
            String text = "Persistent Queue Tutorial! Message ID: " + i;
            msg.setText(text);
            // Delivery not yet confirmed. See ConfirmedPublish.java
            producer.send(msg, queue);
            Thread.sleep(CONFIG.getLongProperty("solace.producer.sleep"));
        }
    }
}
