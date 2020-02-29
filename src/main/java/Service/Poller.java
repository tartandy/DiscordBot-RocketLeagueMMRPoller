package Service;

import DAO.FileHandler;
import Model.Player;
import Model.Players;
import net.dv8tion.jda.api.JDA;

import java.util.Objects;

public class Poller extends Thread{

    private JDA jda;
    private Players players;
    private static final String[] displayGameMode = {"1v1\", \"2v2\", \"S3v3\", \"3v3\", \"Hoop\", \"Rmbl\", \"Drop\", \"Snow"};

    @Override
    public void run(){
        //loop forever
        do{
            //get players lock, then start loading
            synchronized (Players.getLock()){
                if(players.getPlayers() != null) {
                    for (Player player : players.getPlayers()) {
                        int[] ranks = FileHandler.getRankPageData(player.getTrackerURL());
                        if(ranks == null) System.out.println("Could not load player: " + player.getDisplayName() + "; " + player.getTrackerURL());
                        else{
                            player.setRanks(ranks);
                            setNickName(player);
                        }
                    }
                }
            }
            try {
                //sleep for 10s after releasing lock, so it can be changed
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(true);
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    public void setPlayers(Players players){
        this.players = players;
    }

    private void setNickName(Player player) {
        //Below would be where setting names would occur
        Objects.requireNonNull(Objects.requireNonNull(
                jda.getGuildById(player.getGuildID()))
                .getMemberById(player.getDiscordID()))
                .modifyNickname(player.getRanks()[player.getDisplayRank()]
                        + " (" + displayGameMode[player.getDisplayRank()] + ") | " + player.getDisplayName())
                .queue();
    }

}
