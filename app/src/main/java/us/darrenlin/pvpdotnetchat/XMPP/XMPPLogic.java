package us.darrenlin.pvpdotnetchat.XMPP;

import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by Darren on 8/17/2014.
 */
public class XMPPLogic {

    private XMPPConnection connection;
    private static XMPPLogic instance;

    public synchronized static XMPPLogic getInstance() {
        if (instance == null) {
            instance = new XMPPLogic();
        }
        return instance;
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }

    public XMPPConnection getConnection() {
        return connection;
    }
}
