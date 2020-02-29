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
        //locking players class during load
        synchronized (Players.getLock()){
            players.setPlayers(FileHandler.loadPlayers());
        }
        //start poller in new thread
        Poller poller = new Poller();
        poller.setJDA(jda);
        poller.setPlayers(players);
        //poller.start();
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
