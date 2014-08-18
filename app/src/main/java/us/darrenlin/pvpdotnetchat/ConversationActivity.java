package us.darrenlin.pvpdotnetchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;

import us.darrenlin.pvpdotnetchat.FriendList.adapter.FriendListDrawerAdapter;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListDrawerGroupItem;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListDrawerItem;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListItem;
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
        Roster roster = connection.getRoster();

        for (RosterGroup group : roster.getGroups()) {
            friendListItems.add(new FriendListDrawerGroupItem("-", group.getName(), String.valueOf(group.getEntryCount())));
            for (RosterEntry entry : group.getEntries()) {
                friendListItems.add(new FriendListDrawerItem(entry.getName(), "status", 0, 0));
            }
        }

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.list_slidermenu);

        FriendListDrawerAdapter adapter = new FriendListDrawerAdapter(getApplicationContext(), friendListItems);
        drawerList.setAdapter(adapter);

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
}
