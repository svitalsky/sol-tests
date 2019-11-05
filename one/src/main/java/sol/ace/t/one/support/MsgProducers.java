package sol.ace.t.one.support;

import com.solacesystems.jcsmp.*;

import java.util.concurrent.CountDownLatch;

public class MsgProducers {
    public static XMLMessageProducer producerSimple() throws JCSMPException {
        return new SolOneTConnector().connect().getMessageProducer(publishEventHandlerSimple());
    }

    private static JCSMPStreamingPublishEventHandler publishEventHandlerSimple() {
        return new JCSMPStreamingPublishEventHandler() {

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",messageID,timestamp,e);
            }
        };
    }

    public static XMLMessageProducer producerCorrelation(CountDownLatch latch) throws JCSMPException {
        return new SolOneTConnector().connect().getMessageProducer(publishEventHandlerCorrelating(latch));
    }

    private static JCSMPStreamingPublishEventHandler publishEventHandlerCorrelating(CountDownLatch latch) {

        return new JCSMPStreamingPublishCorrelatingEventHandler() {
            public void handleErrorEx(Object key, JCSMPException cause, long timestamp) {
                if (key instanceof MsgInfo) {
                    MsgInfo i = (MsgInfo) key;
                    i.acked = true;
                    System.out.printf("Message response (rejected) received for %s, error was %s \n", i, cause);
                }
                latch.countDown();
            }

            public void responseReceivedEx(Object key) {
                if (key instanceof MsgInfo) {
                    MsgInfo i = (MsgInfo) key;
                    i.acked = true;
                    i.publishedSuccessfully = true;
                    System.out.printf("Message response (accepted) received for %s \n", i);
                }
                latch.countDown();
            }

            public void handleError(String messageID, JCSMPException cause, long timestamp) {
                // Never called
            }

            public void responseReceived(String messageID) {
                // Never called
            }
        };
    }
}
