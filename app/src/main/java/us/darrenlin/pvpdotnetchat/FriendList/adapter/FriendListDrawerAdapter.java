package us.darrenlin.pvpdotnetchat.FriendList.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListDrawerGroupItem;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListDrawerItem;
import us.darrenlin.pvpdotnetchat.FriendList.model.FriendListItem;
import us.darrenlin.pvpdotnetchat.R;

/**
 * Created by Darren on 8/17/2014.
 */
public class FriendListDrawerAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<FriendListItem> friendListItems;

    public FriendListDrawerAdapter(Context context, ArrayList<FriendListItem> friendListItems) {
        this.context = context;
        this.friendListItems = friendListItems;
    }

    @Override
    public int getCount() {
        return friendListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return friendListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService((Activity.LAYOUT_INFLATER_SERVICE));

            if (friendListItems.get(position) instanceof FriendListDrawerItem) {
                convertView = inflater.inflate(R.layout.friend_list_item, null);

                ImageView summonerIcon = (ImageView)convertView.findViewById(R.id.imageViewIcon);
                TextView summonerName = (TextView)convertView.findViewById(R.id.textViewName);
                TextView summonerStatus = (TextView)convertView.findViewById(R.id.textViewStatus);
                ImageView summonerStatusIcon = (ImageView)convertView.findViewById(R.id.imageViewStatus);

                summonerIcon.setImageResource(((FriendListDrawerItem)friendListItems.get(position)).getSummonerIcon());
                summonerName.setText(((FriendListDrawerItem)friendListItems.get(position)).getSummonerName());
                summonerStatus.setText(((FriendListDrawerItem)friendListItems.get(position)).getStatus());
                summonerStatusIcon.setImageResource(((FriendListDrawerItem)friendListItems.get(position)).getAvailabilityIcon());

            } else if (friendListItems.get(position) instanceof FriendListDrawerGroupItem) {
                convertView = inflater.inflate(R.layout.friend_list_group_item, null);

                TextView expand = (TextView)convertView.findViewById(R.id.textViewExpandable);
                TextView group = (TextView)convertView.findViewById(R.id.textViewGroupName);
                TextView people = (TextView)convertView.findViewById(R.id.textViewGroupCount);

                expand.setText(((FriendListDrawerGroupItem)friendListItems.get(position)).getExpand());
                group.setText(((FriendListDrawerGroupItem)friendListItems.get(position)).getGroup());
                people.setText(((FriendListDrawerGroupItem)friendListItems.get(position)).getExpand());
            }
        }

        return convertView;
    }
}
