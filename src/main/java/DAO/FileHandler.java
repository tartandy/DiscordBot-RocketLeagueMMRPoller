package DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

}
