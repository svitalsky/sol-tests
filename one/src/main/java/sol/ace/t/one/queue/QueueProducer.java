package sol.ace.t.one.queue;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.SimpleMsgProducer;

import java.text.DateFormat;
import java.util.Date;

import static sol.ace.t.one.Config.CONFIG;

public class QueueProducer {
    public static void main(String[] args) throws JCSMPException {
        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
        String text = "Persistent Queue Tutorial! " +
                      DateFormat.getDateTimeInstance().format(new Date());
        msg.setText(text);
        // Delivery not yet confirmed. See ConfirmedPublish.java
        SimpleMsgProducer.producer().send(msg, queue);
    }
}
