package us.darrenlin.pvpdotnetchat.FriendList.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class FriendListDrawerAdapter extends ArrayAdapter<FriendListItem> {

    private Context context;
    private ArrayList<FriendListItem> friendListItems;

    private LayoutInflater inflater;

    private enum RowType {
        FRIEND, GROUP
    }

    public FriendListDrawerAdapter(Context context, ArrayList<FriendListItem> friendListItems) {
        super(context, 0, friendListItems);
        this.context = context;
        this.friendListItems = friendListItems;
        inflater = (LayoutInflater)context.getSystemService((Activity.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader() ? 0 : 1;
    }

    @Override
    public int getCount() {
        return friendListItems.size();
    }

    @Override
    public FriendListItem getItem(int position) {
        return friendListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Very ugly
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final FriendListItem row = friendListItems.get(position);

        ViewItemHolder itemHolder = new ViewItemHolder();
        ViewHeaderHolder headerHolder = new ViewHeaderHolder();

        if (view == null) {

            if (!row.isHeader()) {
                view = inflater.inflate(R.layout.friend_list_item, null);

                itemHolder.icon = (ImageView)view.findViewById(R.id.imageViewIcon);
                itemHolder.name = (TextView)view.findViewById(R.id.textViewName);
                itemHolder.status = (TextView)view.findViewById(R.id.textViewStatus);
                itemHolder.statusIcon = (ImageView)view.findViewById(R.id.imageViewStatus);

                view.setTag(itemHolder);

            } else {
                view = inflater.inflate(R.layout.friend_list_group_item, null);

                headerHolder.expand = (TextView)view.findViewById(R.id.textViewExpandable);
                headerHolder.group = (TextView)view.findViewById(R.id.textViewGroupName);
                headerHolder.people = (TextView)view.findViewById(R.id.textViewGroupCount);

                view.setTag(headerHolder);
            }

        } else {
            if (!row.isHeader()) {
                itemHolder = (ViewItemHolder)view.getTag();
            } else {
                headerHolder = (ViewHeaderHolder)view.getTag();
            }
        }

        if (row != null) {
            if (!row.isHeader()) {
                FriendListDrawerItem item = (FriendListDrawerItem)row;

                itemHolder.icon.setImageResource(item.getSummonerIcon());
                itemHolder.name.setText(item.getSummonerName());
                itemHolder.status.setText(item.getStatus());
                itemHolder.statusIcon.setImageResource(item.getAvailabilityIcon());
            } else {
                FriendListDrawerGroupItem item = (FriendListDrawerGroupItem)row;

                headerHolder.expand.setText(item.getExpand());
                headerHolder.group.setText(item.getGroup());
                headerHolder.people.setText(item.getPeople());
            }
        }

        return view;
    }

    public void updateFriendList(ArrayList<FriendListItem> friendListItems) {
        this.friendListItems = friendListItems;

        notifyDataSetChanged();
    }

    public static class ViewItemHolder {
        public ViewItemHolder() {
            icon = null;
            name = null;
            status = null;
            statusIcon = null;
        }
        public ViewItemHolder(ViewItemHolder h) {
            icon = h.icon;
            name = h.name;
            status = h.status;
            statusIcon = h.statusIcon;
        }
        public ImageView icon;
        public TextView name;
        public TextView status;
        public ImageView statusIcon;
    }

    public static class ViewHeaderHolder {
        public ViewHeaderHolder() {
            expand = null;
            group = null;
            people = null;
        }
        public ViewHeaderHolder(ViewHeaderHolder h) {
            expand = h.expand;
            group = h.group;
            people = h.people;
        }
        public TextView expand;
        public TextView group;
        public TextView people;
    }
}
