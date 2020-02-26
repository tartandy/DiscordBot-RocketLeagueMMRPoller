package DAO;

import Model.Player;

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
                Player player = new Player(split[0],split[1],split[2]);
                players.add(player);
            }
            return players;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] getRankPageData(String linkText) {
        System.out.println("Attempting to load: " + linkText);
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

        //split text and parse each phase to get rank
        String[] rankLine = tmpData.split("\n");
        for (int i = 0; i < rankLine.length; i++) {
            rankLine[i] = parseRankLine(rankLine[i]);
        }
        //remove unranked MMR
        rankLine = Arrays.copyOfRange(rankLine,1,9);
        //convert to int and return
        int[] ranks = new int[8];
        for(int i = 0; i < ranks.length ; i++){
            ranks[i] = Integer.parseInt(rankLine[i]);
        }
        return ranks;
    }

    private static String parseRankLine(String input) {
        //parses the rank line to get the rank from the string

        //get the list of rank numbers
        input = input.replace(" ", "");
        input = input.substring(input.indexOf("["),input.indexOf("]"));
        //split out all the ranks and get the last one
        String[] ranks = input.split(",");
        return ranks[ranks.length-1];
    }
}
