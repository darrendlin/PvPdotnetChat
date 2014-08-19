package us.darrenlin.pvpdotnetchat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

import us.darrenlin.pvpdotnetchat.FriendList.adapter.FriendListDrawerAdapter;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListDrawerGroupItem;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListDrawerItem;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListItem;
import us.darrenlin.pvpdotnetchat.R;
import us.darrenlin.pvpdotnetchat.XML.Parser;
import us.darrenlin.pvpdotnetchat.XMPP.XMPPLogic;


public class ConversationActivity extends Activity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;

    private ArrayList<FriendListItem> friendListItems = new ArrayList<FriendListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        XMPPConnection connection = XMPPLogic.getInstance().getConnection();
        final Roster roster = connection.getRoster();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.list_slidermenu);

        final FriendListDrawerAdapter adapter = new FriendListDrawerAdapter(getApplicationContext(), friendListItems);
        drawerList.setAdapter(adapter);

        friendListItems = getFriendList(roster);
        adapter.notifyDataSetChanged();

        roster.addRosterListener(new RosterListener() {

            @Override
            public void entriesAdded(Collection<String> strings) {
                friendListItems = getFriendList(roster);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void entriesUpdated(Collection<String> strings) {
                friendListItems = getFriendList(roster);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void entriesDeleted(Collection<String> strings) {
                friendListItems = getFriendList(roster);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void presenceChanged(Presence presence) {
                friendListItems = getFriendList(roster);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
        });

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(drawerList);
            }
        });
    }

    private ArrayList<FriendListItem> getFriendList(Roster roster) {
        ArrayList<FriendListItem> friends = new ArrayList<FriendListItem>();
        for (RosterGroup group : roster.getGroups()) {
            friendListItems.add(new FriendListDrawerGroupItem("-", getGroupName(group), getOnlineCount(roster, group)));
            for (RosterEntry entry : group.getEntries()) {
                Element person = getPersonInfo(roster, entry);
                if (person != null) { // TODO: show offline friends
                    friendListItems.add(new FriendListDrawerItem(entry.getName(), getPersonInfoTag(person, "statusMsg"), 0, getPersonAvailability(person)));
                }
            }
        }
        return friends;
    }

    private String getGroupName(RosterGroup group) {
        return group.getName().equals("**Default") ? "General" : group.getName();
    }

    private String getOnlineCount(Roster roster, RosterGroup group) {
        int online = 0;
        for (RosterEntry entry : group.getEntries())
            if (roster.getPresence(entry.getUser()).getType() != Presence.Type.unavailable)
                online++;
        return "(" + online + "/" + group.getEntryCount() + ")";
    }

    private Element getPersonInfo(Roster roster, RosterEntry entry) {
        Parser parser = new Parser();
        String xml = roster.getPresence(entry.getUser()).getStatus();
        if (xml != null) {
            Document doc = parser.getDomElement(xml);
            return (Element)doc.getElementsByTagName("body").item(0);
        }
        return null;
    }

    private String getPersonInfoTag(Element e, String tag) {
        if (e != null && !tag.isEmpty()) {
            return new Parser().getValue(e, tag);
        }
        return "";
    }

    private int getPersonAvailability(Element e) {
        String s = getPersonInfoTag(e, "gameStatus");
        if (s.equalsIgnoreCase("outOfGame")) {
            return R.drawable.available;
        } else if (s.equalsIgnoreCase("inQueue")) {
            return R.drawable.away;
        } else if (s.equalsIgnoreCase("spectating")) {
            return R.drawable.busy;
        } else if (s.equalsIgnoreCase("championSelect")) {
            return R.drawable.away;
        } else if (s.equalsIgnoreCase("inGame")) {
            return R.drawable.busy;
        } else if (s.equalsIgnoreCase("hostingPracticeGame")) {
            return R.drawable.away;
        }
        return R.drawable.unavailable;
    }
}
