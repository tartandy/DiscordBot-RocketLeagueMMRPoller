package Model;

import java.util.ArrayList;

public class Players {

    private ArrayList<Player> players;

    public Players(){
    }

    public ArrayList<Player> getPlayers() {
        synchronized (Players.class){
            return players;
        }
    }

    public void setPlayers(ArrayList<Player> players) {
        synchronized (Players.class){
            this.players = players;
        }
    }
}
