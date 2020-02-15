package Service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    private JDA jda;

    public void setJDA(JDA jda) { this.jda = jda; }

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
