package sol.ace.t.one;

import com.solacesystems.jcsmp.*;

public class SolOneTProducer {
    public static void main(String[] args) throws JCSMPException {
        JCSMPSession session = new SolOneTConnector().connect();

        XMLMessageProducer producer = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",
                                  messageID,timestamp,e);
            }
        });

        Topic topic = JCSMPFactory.onlyInstance().createTopic("tutorial/topic");
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        String text = "Hello world! Isn't this a fine day?!";
        msg.setText(text);
        producer.send(msg,topic);
    }
}
