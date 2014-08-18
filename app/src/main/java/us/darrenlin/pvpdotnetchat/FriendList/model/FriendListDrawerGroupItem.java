package us.darrenlin.pvpdotnetchat.FriendList.model;

/**
 * Created by Darren on 8/17/2014.
 */
public class FriendListDrawerGroupItem extends FriendListItem {

    private String expand;
    private String group;
    private String people;

    public FriendListDrawerGroupItem() {
        this("-", "Group", "(0/200)");
    }

    public FriendListDrawerGroupItem(String expand, String group,String people) {
        this.expand = expand;
        this.group = group;
        this.people = people;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    @Override
    public boolean isHeader() {
        return true;
    }
}
