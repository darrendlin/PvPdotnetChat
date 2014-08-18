package us.darrenlin.pvpdotnetchat.FriendList.model;

/**
 * Created by Darren on 8/17/2014.
 */
public class FriendListDrawerItem extends FriendListItem {

    private String summoner;
    private String status;
    private int summonerIcon;
    private int availabilityIcon;

    public FriendListDrawerItem() {
        this("Summoner", "Status", 0, 0);
    }

    public FriendListDrawerItem(String summoner) {
        this(summoner, "", 0, 0);
    }

    public FriendListDrawerItem(String summoner, String status, int summonerIcon, int availabilityIcon) {
        this.summoner = summoner;
        this.status = status;
        this.summonerIcon = summonerIcon;
        this.availabilityIcon = availabilityIcon;
    }

    public String getSummonerName() {
        return summoner;
    }

    public void setSummonerName(String summoner) {
        this.summoner = summoner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSummonerIcon() {
        return summonerIcon;
    }

    public void setSummonerIcon(int summonerIcon) {
        this.summonerIcon = summonerIcon;
    }

    public int getAvailabilityIcon() {
        return availabilityIcon;
    }

    public void setAvailabilityIcon(int availabilityIcon) {
        this.availabilityIcon = availabilityIcon;
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}
