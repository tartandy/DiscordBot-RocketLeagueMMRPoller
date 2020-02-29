package Service;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

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
            //split message into words
            String[] split = event.getMessage().getContentRaw().split(" ");
            //ignore commands with nothing after
            if(split.length < 2){
                sendMessage("Bad command. See **!mmr help** for use.");
                return;
            }
            //switch based on base command type
            switch (split[1].toLowerCase()){
                case "nickname":
                    //check whether setting link or getting link and respond
                    sendMessage(processNickname(split));
                    break;
                case "link":
                    sendMessage(processLink(split));
                    break;
                case "gamemode":
                    break;
                case "help":
                    sendEmbed(processHelp());
                    break;
                default:
                    sendMessage("Bad command. See **!mmr help** for use.");
                    break;
            }
        }
    }

    private void sendEmbed(MessageEmbed embed) {
        event.getMessage().getChannel().sendMessage(embed).queue();
    }


    private void sendMessage(String message){
        event.getMessage().getChannel().sendMessage(message).queue();
    }

    private String processNickname(String[] split) {
        return null;
    }

    private String processLink(String[] split){
        return null;
    }

    private String processMode(String[] split) {
        return null;
    }

    private MessageEmbed processHelp(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("MMR Bot Help");
        builder.setColor(Color.blue);
        builder.setDescription("See below for a list of commands.");
        builder.setFooter("MMRBot", "https://ih0.redbubble.net/image.712240200.0272/flat,128x128,075,f-pad,128x128,f8f8f8.u2.jpg");
        builder.addField("Set Nickname", "Sets your nickname, duh. ```!mmr nickname [name]```", false);
        builder.addField("Set Link", "Set the web address for the bot to poll rank data from. Use [Tracker Network](https://rocketleague.tracker.network/).```!mmr link [link]```", false);
        builder.addField("Set Gamemode", "Sets your preferred gamemode to display in nickname.```!mmr gamemode [ID number]```", false);
        builder.addField("1v1", "ID: 1", true);
        builder.addField("2v2", "ID: 2", true);
        builder.addField("3v3", "ID: 3", true);
        builder.addField("Solo 3v3", "ID: 4", true);
        builder.addField("Hoops", "ID: 5", true);
        builder.addField("Rumble", "ID: 6", true);
        builder.addField("Dropshot", "ID: 7", true);
        builder.addField("Snowday", "ID: 8", true);
        builder.addBlankField(true);
        return builder.build();

    }
}
