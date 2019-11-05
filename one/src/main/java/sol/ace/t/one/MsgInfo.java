package sol.ace.t.one;

import com.solacesystems.jcsmp.BytesXMLMessage;

public class MsgInfo {
    public volatile boolean acked = false;
    public volatile boolean publishedSuccessfully = false;
    public BytesXMLMessage sessionIndependentMessage = null;
    public final long id;

    public MsgInfo(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Message ID: %d, PubConf: %b, PubSuccessful: %b",
                             id,
                             acked,
                             publishedSuccessfully);
    }
}
