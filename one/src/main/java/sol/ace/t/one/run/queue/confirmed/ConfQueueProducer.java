package sol.ace.t.one.run.queue.confirmed;

import com.solacesystems.jcsmp.*;
import sol.ace.t.one.support.MsgInfo;
import sol.ace.t.one.support.MsgProducers;

import java.util.concurrent.CountDownLatch;

import static sol.ace.t.one.support.Config.CONFIG;

public class ConfQueueProducer {
    private static final int count = 2 * CONFIG.getIntProperty("solace.count");

    public static void main(String[] args) throws JCSMPException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(count);
        XMLMessageProducer producer = MsgProducers.producerCorrelation(latch);
        Queue queue = JCSMPFactory.onlyInstance().createQueue(CONFIG.getProperty("solace.queue"));
//        List<MsgInfo> msgList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            TextMessage msg =   JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            msg.setDeliveryMode(DeliveryMode.PERSISTENT);
//            msg.setAckImmediately(true);
            String text = "Confirmed Publish Tutorial! Message ID: "+ i;
            msg.setText(text);
            MsgInfo info = new MsgInfo(i);
            info.sessionIndependentMessage = msg;
            msg.setCorrelationKey(info);
            msg.setTimeToLive(400L);
//            msgList.add(info);
            producer.send(msg, queue);
            Thread.sleep(CONFIG.getLongProperty("solace.producer.sleep"));
        }
        latch.await();
    }
}


