package sol.ace.t.one.run.queue.simple;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.support.MsgProducers;

import java.text.DateFormat;
import java.util.Date;

import static sol.ace.t.one.support.Config.CONFIG;

public class QueueProducer {
    public static void main(String[] args) throws JCSMPException {
        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
        XMLMessageProducer producer = MsgProducers.producerSimple();
        int count = CONFIG.getIntProperty("solace.count");

        for (int i = 1; i <= count; i++) {
            TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
            String text = "Persistent Queue Tutorial! " +
                          DateFormat.getDateTimeInstance().format(new Date());
            msg.setText(text);
            // Delivery not yet confirmed. See ConfirmedPublish.java
            producer.send(msg, queue);
        }
    }
}
