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

    public void setPlayers(ArrayList<Player> players) {
            this.players = players;
    }
}
