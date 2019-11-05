package sol.ace.t.one;

import com.solacesystems.jcsmp.*;

import java.util.concurrent.CountDownLatch;

public class SolOneTConsumer {
    public static void main(String[] args) throws JCSMPException {
        JCSMPSession session = new SolOneTConnector().connect();

        CountDownLatch latch = new CountDownLatch(50);

        XMLMessageConsumer consumer = session.getMessageConsumer(new XMLMessageListener() {

            @Override
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    System.out.printf("TextMessage received: '%s'%n",
                                      ((TextMessage)msg).getText());
                } else {
                    System.out.println("Message received.");
                    System.out.printf("Message Dump:%n%s%n",msg.dump());
                }
                latch.countDown();  // unblock main thread
            }

            @Override
            public void onException(JCSMPException e) {
                System.out.printf("Consumer received exception: %s%n",e);
                latch.countDown();  // unblock main thread
            }
        });

        Topic topic = JCSMPFactory.onlyInstance().createTopic("tutorial/topic");
        session.addSubscription(topic);
        consumer.start();

        try {
            latch.await(); // block here until message received, and latch will flip
        }
        catch (InterruptedException e) {
            System.out.println("I was awoken while waiting");
        }
    }
}
