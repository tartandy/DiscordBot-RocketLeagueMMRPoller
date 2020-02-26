package Service;

import DAO.FileHandler;
import Model.Player;
import Model.Players;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Listener extends ListenerAdapter {

    private JDA jda;
    private Players players;

    public Listener(){
        players = new Players();
    }

    public void setJDA(JDA jda) { this.jda = jda; }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        //will load the players into arraylist
        players.setPlayers(FileHandler.loadPlayers());
        //get MMR for each player
        if(players.getPlayers() != null) {
            for (Player player : players.getPlayers()) {
                int[] ranks = FileHandler.getRankPageData(player.getTrackerURL());
                if(ranks == null) System.out.println("Could not load player: " + player.getDisplayName() + "; " + player.getTrackerURL());
                else player.setRanks(ranks);
            }
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        //receives input and determines the message type, if any,
        // before handing off to appropriate handler thread to process in background

        //ignore PMs and messages from other bots
        Message msg = event.getMessage();
        if (event.getAuthor().isBot()) return;
        if (!msg.isFromGuild()) return;

        //creates the handler to check for commands on a new thread
        Handler handler = new Handler(event, jda);
        handler.start();
    }
}
