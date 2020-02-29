package Model;

import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class Players {

    private ArrayList<Player> players;
    private final static Object lock = new Object();

    public Players(){
    }

    public static Object getLock() {
        return lock;
    }

    public ArrayList<Player> getPlayers() {
            return players;
    }

    public void setNickname(String id, String nickname){
        for (Player player : players){
            if(player.getDiscordID().equals(id)){
                player.setDisplayName(nickname);
            }
        }
    }

    public void setDisplayRank(String id, int rank){
        for (Player player : players){
            if(player.getDiscordID().equals(id)){
                player.setDisplayRank(rank);
            }
        }
    }

    public void setLink(String id, String link){
        for (Player player : players){
            if(player.getDiscordID().equals(id)){
                player.setTrackerURL(link);
            }
        }
    }

    public boolean isPlayer(String id){
        for (Player player : players){
            if(player.getDiscordID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void setPlayers(ArrayList<Player> players) {
            this.players = players;
    }
}
