package Model;

public class Player {

    private String discordID;
    private String trackerURL;
    private String displayName;
    private int rankSolo;
    private int rankDoubles;
    private int rankStandard;
    private int rankSoloStandard;
    private int rankHoops;
    private int rankRumble;
    private int rankDropshot;
    private int rankSnowday;

    public Player(String discordID, String trackerURL, String displayName) {
        this.discordID = discordID;
        this.trackerURL = trackerURL;
        this.displayName = displayName;
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

    public void setRanks(int[] ranks){
        this.rankSolo = ranks[0];
        this.rankDoubles = ranks[1];
        this.rankStandard = ranks[2];
        this.rankSoloStandard = ranks[3];
        this.rankHoops = ranks[4];
        this.rankRumble = ranks[5];
        this.rankDropshot = ranks[6];
        this.rankSnowday = ranks[7];
    }

    public int getRankSolo() {
        return rankSolo;
    }


    public int getRankDoubles() {
        return rankDoubles;
    }

    public int getRankStandard() {
        return rankStandard;
    }

    public int getRankSoloStandard() {
        return rankSoloStandard;
    }

    public int getRankHoops() {
        return rankHoops;
    }

    public int getRankRumble() {
        return rankRumble;
    }

    public int getRankDropshot() {
        return rankDropshot;
    }

    public int getRankSnowday() {
        return rankSnowday;
    }

    @Override
    public String toString() {
        return "Player{" +
                "discordID='" + discordID + '\'' +
                ", trackerURL='" + trackerURL + '\'' +
                ", displayName='" + displayName + '\'' +
                ", rankSolo=" + rankSolo +
                ", rankDoubles=" + rankDoubles +
                ", rankStandard=" + rankStandard +
                ", rankSoloStandard=" + rankSoloStandard +
                ", rankHoops=" + rankHoops +
                ", rankRumble=" + rankRumble +
                ", rankDropshot=" + rankDropshot +
                ", rankSnowday=" + rankSnowday +
                '}';
    }
}
