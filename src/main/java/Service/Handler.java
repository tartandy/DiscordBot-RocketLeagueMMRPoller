package Service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Handler extends Thread{

    private MessageReceivedEvent event;
    private JDA jda;
    private final String prefix = "!mmr";

    public Handler(MessageReceivedEvent event, JDA jda){
        this.event = event;
        this.jda = jda;
    }

    @Override
    public void run() {
        if(event.getMessage().getContentRaw().toLowerCase().startsWith(prefix)){
            //is a command to be processed.
            System.out.println("Found a command");
        }
    }
}
