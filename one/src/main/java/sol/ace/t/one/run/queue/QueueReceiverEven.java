package sol.ace.t.one.run.queue;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.support.SolOneTConnector;

import java.util.concurrent.CountDownLatch;

import static sol.ace.t.one.support.Config.CONFIG;

public class QueueReceiverEven {
    public static void main(String[] args) throws JCSMPException {
        JCSMPSession session = new SolOneTConnector().connect();
        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
        ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
        flow_prop.setEndpoint(queue);
        flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

        EndpointProperties endpoint_props = new EndpointProperties();
        endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);

        CountDownLatch latch = new CountDownLatch(CONFIG.getIntProperty("solace.count"));

        FlowReceiver consumer = session.createFlow(new XMLMessageListener() {
            @Override
            public void onReceive(BytesXMLMessage msg) {
                if (msg instanceof TextMessage) {
                    String text = ((TextMessage)msg).getText();
                    String[] splitted = text.split(": ");
                    if (splitted.length == 2) {
                        int num = Integer.parseInt(splitted[1]);
                        if (num % 2 == 0) {
                            msg.ackMessage();
                            System.out.printf("TextMessage received: '%s'%n", text);
                        }
                        else return;
                    }
                }
                else {
                    msg.ackMessage();
                    System.out.println("Message received.");
                }
//                System.out.printf("Message Dump:%n%s%n", msg.dump());
                // When the ack mode is set to SUPPORTED_MESSAGE_ACK_CLIENT,
                // guaranteed delivery messages are acknowledged after
                // processing
                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {/**/}
                latch.countDown(); // unblock main thread
            }

            @Override
            public void onException(JCSMPException e) {
                System.out.printf("Consumer received exception: %s%n", e);
                latch.countDown(); // unblock main thread
            }
        }, flow_prop, endpoint_props);

        consumer.start();

        try {
            latch.await(); // block here until message received, and latch will flip
        }
        catch (InterruptedException e) {
            System.out.println("I was awoken while waiting");
        }

        // calling this removes delivered msgs from the Solace queue
        // only necessary when delivery confirmation is not enabled and processed
        consumer.close();
    }
}
