/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created class so that the Game.java GUI class could access the leaderboard entries
 * @author Yiqun
 *
 */
public class Leaderboard {

    private final File current = new File("LEADERBOARD.TXT");

    public Leaderboard() {
    }

    /**
     * Returns the list of string entires, read in from the "LEADERBOARD.TXT" file
     * @return
     */
    public ArrayList<String> getEntries() {
        try {
            FileReader f = new FileReader(current);
            BufferedReader r = new BufferedReader(f);
            ArrayList<String> list = new ArrayList<String>();
            String toRead;
            while ((toRead = r.readLine()) != null) { // While there are still lines to read
                list.add(toRead + "      ");
                System.out.println("Added " + toRead);
            }
            r.close();
            return list;
        } catch (Exception e) {
            System.out.println("getLeaderBoard game error. Please hit New Game to play again!");
            return new ArrayList<String>();
        }
    }


}
