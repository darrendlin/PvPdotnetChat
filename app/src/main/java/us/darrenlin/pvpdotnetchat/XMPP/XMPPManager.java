package us.darrenlin.pvpdotnetchat.XMPP;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.spark.util.DummySSLSocketFactory;

import java.io.IOException;

/**
 * Created by Darren on 8/17/2014.
 */
public class XMPPManager {

    private String server;
    private int port;
    private String service;

    private String presence =
            "<body>\n" +
            "  <profileIcon>657</profileIcon>\n" +
            "  <level>30</level>\n" +
            "  <wins>1779</wins>\n" +
            "  <leaves>74</leaves>\n" +
            "  <odinWins>21</odinWins>\n" +
            "  <odinLeaves>8</odinLeaves>\n" +
            "  <queueType />\n" +
            "  <tier>GOLD</tier>\n" +
            "  <rankedLeagueName>Varus\'s Outriders</rankedLeagueName>\n" +
            "  <rankedLeagueDivision>V</rankedLeagueDivision>\n" +
            "  <rankedLeagueTier>DIAMOND</rankedLeagueTier>\n" +
            "  <rankedLeagueQueue>RANKED_SOLO_5x5</rankedLeagueQueue\n" +
            "  <rankedWins>271</rankedWins>\n" +
            "  <gameStatus>outOfGame</gameStatus>\n" +
            "  <statusMsg>rip</statusMsg>\n" +
            "</body>";

    /**
     *
     * @param server "chat.na1.lol.riotgames.com"
     * @param port 5223
     * @param service "pvp.net"
     */
    public XMPPManager(String server, int port, String service) {
        this.server = server;
        this.port = port;
        this.service = service;
    }

    /**
     *
     * @return the XMPPConnection or null if unsuccessful
     */
    public XMPPConnection connect() {

        if (server.isEmpty() || service.isEmpty())
            throw new IllegalArgumentException();

        ConnectionConfiguration connectCfg = new ConnectionConfiguration(server, port, service);

        connectCfg.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        connectCfg.setSocketFactory(new DummySSLSocketFactory());

        XMPPConnection connection = new XMPPTCPConnection(connectCfg);

        try {
            connection.connect();
            return connection;
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param username login username
     * @param password login password
     * @return the Roster from the login provided by username and password or null if unsuccessful
     */
    public Roster login(XMPPConnection connection, String username, String password) {

        if (connection == null || !connection.isConnected())
            throw new NullPointerException();
        if (username.isEmpty() || password.isEmpty())
            throw new IllegalArgumentException();

        try {
            connection.login(username, password);
            return connection.getRoster();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setStatus(String status) {

    }
}
