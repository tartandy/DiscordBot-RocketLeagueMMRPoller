package DAO;

import Model.Player;
import Model.Players;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHandler {

    public static String getToken() {
        //gets the token from text file and returns
        String fn = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "BotData"
                + File.separator + "token.txt";
        String line;
        //load file into BR
        try {
            File file = new File(fn);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<Player> loadPlayers() {
        String fn = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "BotData"
                + File.separator + "players.txt";
        String line;
        try {
            //open connection to file if one exists, otherwise exit
            File file = new File(fn);
            if (!file.exists()) {
                System.out.println("No players file to load.");
                return null;
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<Player> players = new ArrayList<>();
            //for each line, load new player and add to ArrayList
            while((line = br.readLine()) != null ){
                String[] split = line.split(",");
                Player player = new Player(split[0],split[1],split[2], Integer.parseInt(split[3]), split[4]);
                players.add(player);
            }
            return players;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean storePlayers(Players players){
        String fn = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "BotData"
                + File.separator + "players.txt";
        String output;
        //build file to string
        StringBuilder outputBuilder = new StringBuilder();
        for (Player player : players.getPlayers()) {
            outputBuilder.append(player.toString()).append("\n");
        }
        output = outputBuilder.toString().trim();
        File file = new File(fn);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        //write to file
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(output);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int[] getRankPageData(String linkText) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        StringBuilder tmpDataBuilder = new StringBuilder();
        String tmpData;

        //load the URL and download the page to String
        try {
            url = new URL(linkText);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            //for each line, write to string
            while ((line = br.readLine()) != null) {
                tmpDataBuilder.append(line.trim()).append("\n");
            }
        } catch (IOException mue) {
            return null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ignored) {
            }
        }

        //parse data to int[]
        tmpData = tmpDataBuilder.toString();
        //trim to the playlist tracking rating data
        //first check the page is valid, and exit if not
        if (!tmpData.contains("$('#playlist-tracking-rating').highcharts({")) return null;
        tmpData = tmpData.substring(tmpData.indexOf("$('#playlist-tracking-rating').highcharts({"));
        tmpData = tmpData.substring(0, tmpData.indexOf("</script>"));
        //trim to interesting text
        tmpData = tmpData.substring(tmpData.indexOf("series:"));
        tmpData = tmpData.substring(0, tmpData.indexOf("$('#playlist-tracking')"));
        tmpData = tmpData.substring(tmpData.indexOf("\n"), tmpData.indexOf("\n]"));
        tmpData = tmpData.replace("\n\n\n", "\n");
        tmpData = tmpData.trim();

        //split lines to get each rank
        String[] rankLine = tmpData.split("\n");
        int[] ranks = new int[8];
        //give default value of 0 incase rank data missing
        Arrays.fill(ranks, 0);

        //for each line, populate rank data
        for(String s : rankLine){
            int mode;
            if((mode = getGamemode(s)) != -1){
                System.out.println(s);
                ranks[mode] = parseRankLine(s);
            }
        }
        return ranks;
    }

    private static int getGamemode(String line){
        //get the gamemode and return its corresponding index in ranks[]
        String[] modes = {"1v1", "2v2", "Solo Standard", "Standard 3v3", "Hoop", "Rumble", "Dropshot", "Snowday"};
        for(int i = 0; i < 8; i++){
            if(line.contains(modes[i])){
                return i;
            }
        }
        //returns if unranked mode
        return -1;
    }

    private static int parseRankLine(String input) {
        //parses the rank line to get the rank from the string
        //get the list of rank numbers
        input = input.replace(" ", "");
        input = input.substring(input.indexOf("[")+1,input.indexOf("]"));
        //split out all the ranks and get the last one
        String[] ranks = input.split(",");
        System.out.println(ranks[ranks.length-1]);
        if(ranks[ranks.length-1].isEmpty()) return 0;
        return Integer.parseInt(ranks[ranks.length-1]);
    }
}
