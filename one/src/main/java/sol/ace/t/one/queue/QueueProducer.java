package sol.ace.t.one.queue;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.SolOneTConnector;

import java.text.DateFormat;
import java.util.Date;

import static sol.ace.t.one.Config.CONFIG;

public class QueueProducer {
    public static void main(String[] args) throws JCSMPException {
        JCSMPSession session = new SolOneTConnector().connect();

        XMLMessageProducer producer = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",messageID,timestamp,e);
            }
        });

        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
        String text = "Persistent Queue Tutorial! " +
                      DateFormat.getDateTimeInstance().format(new Date());
        msg.setText(text);
        // Delivery not yet confirmed. See ConfirmedPublish.java
        producer.send(msg, queue);
    }
}
