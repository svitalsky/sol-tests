package sol.ace.t.one.run.procon;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.support.SolOneTConnector;

import java.util.concurrent.CountDownLatch;

import static sol.ace.t.one.support.Config.CONFIG;

public class SolOneTConsumer {
    public static void main(String[] args) throws JCSMPException {
        JCSMPSession session = new SolOneTConnector().connect(args.length == 0);

        CountDownLatch latch = new CountDownLatch(CONFIG.getIntProperty("solace.count"));

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

        Topic topic = JCSMPFactory.onlyInstance().createTopic(CONFIG.getProperty("solace.topic"));
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
