package Service;

import DAO.FileHandler;
import Model.Players;
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
    private Players players;

    public Handler(MessageReceivedEvent event, JDA jda, Players players){
        this.event = event;
        this.jda = jda;
        this.players = players;
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
                    //getting content display strips markdown and keeps case.
                    sendMessage(processNickname(event.getMessage().getContentDisplay()));
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

    private void sendTyping() {
        event.getMessage().getChannel().sendTyping().queue();
    }

    private void sendEmbed(MessageEmbed embed) {
        event.getMessage().getChannel().sendMessage(embed).queue();
    }


    private void sendMessage(String message){
        event.getMessage().getChannel().sendMessage(message).queue();
    }

    private String processNickname(String message) {
        //check player exists first
        synchronized (Players.getLock()){
            if(!players.isPlayer(event.getAuthor().getId())) return "Please setup the poller first.";
        }
        //extracts the nickname from message, removes comma otherwise it'll break text file
        String nickname = message.substring(14).replace(",", "");
        //return if name is over length limit
        if(nickname.length() > 18) return "Error: name must not exceed 18 characters.";
        //update then store players to reflect change
        synchronized (Players.getLock()){
            players.setNickname(event.getAuthor().getId(), nickname);
            FileHandler.storePlayers(players);
        }
        return "Nickname **" + nickname + "** set!";
    }

    private String processLink(String[] split){
        return null;
    }

    private String processMode(String[] split) {
        return null;
    }

    private MessageEmbed processHelp(){
        //creates and builds an embed for better readability and content density
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("MMR Bot Help");
        builder.setColor(Color.blue);
        builder.setDescription("Use [Tracker Network](https://rocketleague.tracker.network/) for links.");
        builder.setFooter("MMRBot | Created by Tartandy#7567", "https://ih0.redbubble.net/image.712240200.0272/flat,128x128,075,f-pad,128x128,f8f8f8.u2.jpg");
        builder.addField("***Create Account Poller***", "Setup the poller for a new user.```!mmr setup [link] [Nickname]```", false);
        builder.addField("***Update Nickname***", "Sets your nickname, duh. ```!mmr nickname [nickname]```", false);
        builder.addField("***Update Link***", "Set the web address for the bot to poll rank data from.```!mmr link [link]```", false);
        builder.addField("***Update Preferred Gamemode***", "Sets your preferred gamemode to display in nickname.```!mmr gamemode [ID number]```", false);
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
