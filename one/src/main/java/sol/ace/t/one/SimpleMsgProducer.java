package sol.ace.t.one;

import com.solacesystems.jcsmp.*;

public class SimpleMsgProducer {
    public static XMLMessageProducer producer() throws JCSMPException {
        return new SolOneTConnector().connect().getMessageProducer(new JCSMPStreamingPublishEventHandler() {

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",messageID,timestamp,e);
            }
        });
    }
}
