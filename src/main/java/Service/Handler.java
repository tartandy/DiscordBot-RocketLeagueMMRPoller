package Service;

import DAO.FileHandler;
import Model.Player;
import Model.Players;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;

public class Handler extends Thread{

    private MessageReceivedEvent event;
    private JDA jda;
    private final String prefix = "!mmr";
    private Players players;
    private static final String[] displayGameMode = {"1v1", "2v2", "S3v3", "3v3", "Hoop", "Rmbl", "Drop", "Snow"};

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
                    sendMessage(processMode(split));
                    break;
                case "help":
                    sendEmbed(processHelp());
                    break;
                case "setup":
                    sendMessage(processSetup(split));
                    break;
                default:
                    sendMessage("Bad command. See **!mmr help** for use.");
                    break;
            }
        }
    }

    private String processSetup(String[] split) {
        //check player doesn't exist first
        synchronized (Players.getLock()){
            if(players.isPlayer(event.getAuthor().getId())) return "You've already completed setup.";
        }
        sendTyping();
        //get discord IDs
        String guildID = event.getGuild().getId();
        String discordID = event.getAuthor().getId();
        //must have at least 4 args
        if(split.length < 4) return "Bad command. See **!mmr help** for use.";
        //process link is valid first
        String link = split[2];
        //if ranks pulled successfully, set link for player and store in file
        if(FileHandler.getRankPageData(link) == null){
            return "Error processing link, make sure it's valid and try again.";
        }

        //build nickname from remaining args
        StringBuilder nickname = new StringBuilder();
        for(int i = 3; i < split.length; i++){
            nickname.append(split[i]).append(" ");
        }
        //get name as string, check name is valid length
        String name = nickname.toString().trim();
        name = name.replace(",", "");
        if(name.length() > 18) return "Error: name must not exceed 18 characters.";
        if(name.length() == 0) return "Error: name cannot be empty.";
        Player player = new Player(discordID, link, name, 3, guildID);
        synchronized (Players.getLock()){
            players.addPlayer(player);
            FileHandler.storePlayers(players);
        }
        return "Player setup successfully!";
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
            setNickname(nickname, players.getPlayer(event.getAuthor().getId()));
        }
        return "Nickname **" + nickname + "** set!";
    }

    private void setNickname(String nickname, Player player) {
        //Below would be where setting names would occur
        try{
            Guild guild = jda.getGuildById(player.getGuildID());
            assert guild != null;
            Member member = guild.getMemberById(player.getDiscordID());
            assert member != null;
            member.modifyNickname(player.getRanks()[player.getDisplayRank()]
                    + " (" + displayGameMode[player.getDisplayRank()] + ") | " + player.getDisplayName()).queue();
        } catch(PermissionException e){
            //can't change owner nickname
        } catch (NullPointerException e){
            System.out.println("Null pointer exception trying to set nickname. Is the bot kicked from the guild?");
        }
    }

    private String processLink(String[] split){
        //check player exists first
        synchronized (Players.getLock()){
            if(!players.isPlayer(event.getAuthor().getId())) return "Please setup the poller first.";
        }
        //can take a while, so send typing to show progress
        sendTyping();
        //check link is correct number of args
        if(split.length != 3) return "Bad command. See **!mmr help** for use.";
        String link = split[2];
        //if ranks pulled successfully, set link for player and store in file
        if(FileHandler.getRankPageData(link) != null){
            synchronized (Players.getLock()){
                players.setLink(event.getAuthor().getId(), link);
                FileHandler.storePlayers(players);
            }
            return "Link valid, set!";
        } else {
            return "Error processing link, make sure it's valid and try again.";
        }

    }

    private String processMode(String[] split) {
        String[] ranks = {"Solo", "Doubles", "Solo Standard", "Standard", "Hoops", "Rumble", "Dropshot", "Snowday"};
        //check player exists first
        synchronized (Players.getLock()){
            if(!players.isPlayer(event.getAuthor().getId())) return "Please setup the poller first.";
        }
        //check command is correct length
        if(split.length != 3) return "Bad command. See **!mmr help** for use.";
        int gamemode;
        switch (split[2]) {
            case "1":
                gamemode = 0;
                break;
            case "2":
                gamemode = 1;
                break;
            case "3":
                gamemode = 3;
                break;
            case "4":
                gamemode = 2;
                break;
            case "5":
                gamemode = 4;
                break;
            case "6":
                gamemode = 5;
                break;
            case "7":
                gamemode = 6;
                break;
            case "8":
                gamemode = 7;
                break;
            default:
                return "Error processing gamemode ID, use **!mmr help** for a list of IDs.";
        }
        synchronized (Players.getLock()){
            players.setDisplayRank(event.getAuthor().getId(), gamemode);
            FileHandler.storePlayers(players);
        }
        return "Preferred gamemode: **" + ranks[gamemode] + "** set!";
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
