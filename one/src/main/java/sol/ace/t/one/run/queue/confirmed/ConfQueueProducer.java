package sol.ace.t.one.run.queue.confirmed;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.support.MsgInfo;
import sol.ace.t.one.support.MsgProducers;

import java.util.concurrent.CountDownLatch;

import static sol.ace.t.one.support.Config.CONFIG;

public class ConfQueueProducer {
    private static final int count = CONFIG.getIntProperty("solace.count");

    public static void main(String[] args) throws JCSMPException {
        CountDownLatch latch = new CountDownLatch(count);
        XMLMessageProducer producer = MsgProducers.producerCorrelation(latch);
        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
//        List<MsgInfo> msgList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            TextMessage msg =   JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
            String text = "Confirmed Publish Tutorial! Message ID: "+ i;
            msg.setText(text);
            MsgInfo info = new MsgInfo(i);
            info.sessionIndependentMessage = msg;
            msg.setCorrelationKey(info);
//            msgList.add(info);
            producer.send(msg, queue);
        }

        try {
            latch.await();
        }
        catch (InterruptedException e) {
            System.out.println("I was awoken while waiting");
        }
    }
}


