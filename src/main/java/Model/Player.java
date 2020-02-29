package Model;

public class Player {

    private String discordID;
    private String trackerURL;
    private String displayName;
    private String guildID;
    private int[] ranks;
    private int displayRank;

    public Player(String discordID, String trackerURL, String displayName, int displayRank, String guildID) {
        this.discordID = discordID;
        this.trackerURL = trackerURL;
        this.displayName = displayName;
        this.displayRank = displayRank;
        this.guildID = guildID;
    }

    public String getDiscordID() {
        return discordID;
    }

    public void setDiscordID(String discordID) {
        this.discordID = discordID;
    }

    public String getTrackerURL() {
        return trackerURL;
    }

    public void setTrackerURL(String trackerURL) {
        this.trackerURL = trackerURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getDisplayRank() {
        return displayRank;
    }

    public void setDisplayRank(int displayRank) {
        this.displayRank = displayRank;
    }

    public void setRanks(int[] ranks){
        this.ranks = ranks;
    }

    public int[] getRanks(){
        return ranks;
    }

    public String getGuildID() {
        return guildID;
    }

    @Override
    public String toString() {
        return "Player{" +
                "discordID='" + discordID + '\'' +
                ", trackerURL='" + trackerURL + '\'' +
                ", displayName='" + displayName + '\'' +
                ", rankSolo=" + ranks[0] +
                ", rankDoubles=" + ranks[1] +
                ", rankStandard=" + ranks[2] +
                ", rankSoloStandard=" + ranks[3] +
                ", rankHoops=" + ranks[4] +
                ", rankRumble=" + ranks[5] +
                ", rankDropshot=" + ranks[6] +
                ", rankSnowday=" + ranks[8] +
                '}';
    }
}
