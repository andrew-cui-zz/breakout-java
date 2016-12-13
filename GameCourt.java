/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.*;


/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 *
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    private int score;

    // the state of the game logic
    private User user; // The User bar
    private Ball ball; // The Ball that hits bricks
    private String curr_player; // The current player

    private static TreeMap<Integer, Brick> LEVEL_1; // TreeMap of Brick

    public boolean playing = false; // Whether the game is running
    public boolean startGame = true; // Does a new game have to start

    private JLabel status; // Current status text (i.e. Running...)
    private JLabel scoreboard; // Current score
    private JLabel player; // Current player

    protected static File leaderboards = new File("LEADERBOARD.TXT");

    // Game constants
    public static final int COURT_WIDTH = 700;
    public static final int COURT_HEIGHT = 600;
    public static final int USER_VELOCITY = 2;
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 4;

    public GameCourt(JLabel status, JLabel player, JLabel scoreboard) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Timer object
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        setFocusable(true);

        // Mouseclicks for user bar
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    user.v_x = -USER_VELOCITY;
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    user.v_x = USER_VELOCITY;
            }

            public void keyReleased(KeyEvent e) {
                user.v_x = 0;
            }
        });
        score = 0;
        setBackground(Color.BLACK);
        this.status = status;
        this.scoreboard = scoreboard;
        this.player = player;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        score = 0;
        playing = false;

        // Setting default JLabels
        status.setText("Click space to set up a new game!");
        player.setText("Player: ");
        scoreboard.setText("          Score: 0");

        // Creating game objects
        user = new User(COURT_WIDTH, COURT_HEIGHT);
        ball = new Ball(COURT_WIDTH, COURT_HEIGHT);

        // Starts back at Level 1
        LEVEL_1 = new TreeMap<Integer, Brick>();
        createLevel1();

        repaint();

        // Start the game by pressing spacebar
        addKeyListener(new KeyAdapter() {
           public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                   if (playing || !startGame) { return; }
                   else {
                       JTextField Field1 = new JTextField(1);
                       JTextField Field2 = new JTextField(1);

                       JPanel myPanel = new JPanel(new GridLayout(3, 1, 0, 1));
                       myPanel.add(new Label("X-Velocity:"));
                       myPanel.add(Field1);
                       myPanel.add(new Label("Y-Velocity:"));
                       myPanel.add(Field2);
                       myPanel.add(new Label("Insert your name here: "));

                       // From Pennstagram GUI
                       String name = JOptionPane.showInputDialog(myPanel);

                       try {
                           int result_x = Integer.parseInt(Field1.getText());
                           int result_y = Integer.parseInt(Field2.getText());

                           if (result_x > -3 && result_x < 3 && result_y > -3 && result_y < 3) {
                               ball.v_x = result_x;
                               ball.v_y = result_y;
                               startGame = false;
                               playing = true;
                               status.setText("Playing Breakout...");
                               curr_player = name;
                               player.setText("Player: " + curr_player);
                           }
                       } catch (NumberFormatException n) {
                           status.setText("Error: Please enter velocities between -3 and 3. Press "
                                   + "New to start again");
                           return;
                       } catch (HeadlessException h) {
                           status.setText("Error: Please hit New to start again");
                           return;
                       }
                   }
               }
           }
        });


        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            // Advance the square in current direction
            user.move();
            ball.move();

            // Bounce off walls
            ball.bounce(ball.hitWall());

            for (int i = LEVEL_1.firstKey(); i <= LEVEL_1.lastKey(); i++) {
                if (LEVEL_1.containsKey(i)) {
                    Brick b = LEVEL_1.get(i);
                    if (ball.intersects(b)) {
                        int adder = b.getHard();
                        if (adder > 3) { adder -= 2; }

                        // Updating score and JLabels
                        status.setText("Hit brick number: " + i + ", gained " + adder + " points!");
                        score += adder;
                        scoreboard.setText("          Score: " + score + " points     ");
                        String s = b.isHit(ball);

                        // Typical brick removed, triggers effect if it is a fast/slow brick
                        if (s.equals("remove")) {
                            ball.bounce(b);
                            LEVEL_1.remove(i);
                            if (LEVEL_1.size() == 0) {
                                endGame(this.score, this.curr_player);
                                break;
                            }
                        } else if (s.equals("grow")) { // Case of a "grow" special brick
                            ball.bounce(b);
                            LEVEL_1.remove(i);
                            // Increases hardness of all the other bricks
                            for (Brick c : LEVEL_1.values()) {
                                c.setHard(c.getHard() + 1);
                                System.out.print("Grew a brick! ");
                            }
                            System.out.println();
                            repaint();
                        } else {
                            ball.bounce(b);
                        }
                        repaint();
                        break;
                    }
                }
            }
            if (ball.intersects(user)) {
                ball.pos_y = 545;
                ball.v_y *= -1;
                status.setText("Bounced off the user!");
            } else if (ball.pos_y > 570) {
                try {
                    endGame(this.score, this.curr_player);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_N) { reset(); startGame = true; }
                    }
                 });
            }

            // Update the display
            repaint();
        }
    }

    public void endGame(int score, String play) {
        try {
            playing = false;
            ArrayList<String> highscores = getLeaderboard();
            ArrayList<Integer> scores = getScores(highscores);
            if (checkHighScore(score, scores)) {
                int i = getLeaderboardIndex(score, scores);
                editLeaderboard(score, play, i, highscores);
                status.setText("You scored: " + score + " and that was a high score!");
            } else { status.setText("You scored: " + score + ". Press N or New to try again!"); }
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("endGame game error. Please hit New Game to play again!");
        }
    }

    // Border line at which is considered losing the game
    Stroke endLine = new BasicStroke(2, BasicStroke.CAP_BUTT,
                                        BasicStroke.JOIN_BEVEL, 0,
                                        new float[]{9}, 0);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        user.draw(g);
        ball.draw(g);
        if (!LEVEL_1.isEmpty()) {
            for (Brick b : LEVEL_1.values()) {
                b.draw(g);
            }
        }

        // Drawing dashed line
        ((Graphics2D) g).setStroke(endLine);
        g.setColor(Color.PINK);
        g.drawLine(0, 592, 700, 592);
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    // I/O Functions
    /**
     * Determines if the last game was a high score (top 10)
     * @param score the most recent score
     * @return true if high score, false otherwise
     * @throws FileNotFoundException
     */
    public static boolean checkHighScore(int score, ArrayList<Integer> scores) {
        try {
            if (scores.size() < 10) { System.out.println("Is a high score!"); return true; }
            if (score > scores.get(scores.size() - 1)) { System.out.println("Is a high score!"); return true; }
            else { System.out.println("Not a high score, sorry!"); return false; }
        } catch (Exception e) {
            System.out.println("checkHighScore game error. Please hit New Game to play again!");
            return false;
        }
    }

    /**
     * Finds the index in the LinkedList that we have to insert the new score into
     * It is assumed that we are only calling this on a high score
     * @param score The score that is a high score
     * @return index The index of the LinkedList
     * @throws FileNotFoundException
     */
    public static int getLeaderboardIndex(int score, ArrayList<Integer> list) {
        for(int i = 0; i < list.size(); i++) { // Checks the first 9 elements
            if (score > list.get(i)) {
                System.out.println("Index " + i);
                return i;
            }
        }
        System.out.println("Lowest high score");
        if (list.size() == 10) return 9;
        else return list.size(); // Returns to be the last index
    }

    /**
     * Extracts current scores
     * @param LinkedList of String entries
     * @return LinkedList of Integers
     * @throws IOException
     */
    public static ArrayList<Integer> getScores(ArrayList<String> highscores) {
        try {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (String esser : highscores) {
                if (esser.equals("")) {}
                else {
                    Integer terry = Integer.parseInt(esser.substring(esser.indexOf(',') + 2));
                    System.out.println("ScoreOf " + terry);
                    list.add(terry);
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println("getScores() game error. Please hit New Game to play again!");
            return new ArrayList<Integer>();
        }
    }

    /**
     * Extracts current leaderboard
     * @param none
     * @return the list of ten lines
     * @throws IOException
     */
    public static ArrayList<String> getLeaderboard() {
        try {
            FileReader f = new FileReader(leaderboards);
            BufferedReader r = new BufferedReader(f);
            ArrayList<String> list = new ArrayList<String>();
            String toRead;
            while ((toRead = r.readLine()) != null) { // While there are still lines to read
                list.add(toRead);
                System.out.println("Added " + toRead);
            }
            r.close();
            return list;
        } catch (Exception e) {
            System.out.println("getLeaderBoard game error. Please hit New Game to play again!");
            return new ArrayList<String>();
        }
    }

    /**
     * Changes the new LEADERBOARD.TXT
     * @param score, name
     * @param index
     * @return nothing
     * @throws IOException
     */
    public static void editLeaderboard(int score, String player,
            int index, ArrayList<String> entries) throws IOException {
        String addition = player + ", " + score + '\n';
        System.out.println(entries);

        BufferedWriter f = new BufferedWriter(new FileWriter(leaderboards));
        if (entries.size() == 0) {
            System.out.println("First entry!");
            f.write(addition);
            f.close();
            return;
        }
        for (int i = 0; i < index; i++) {
            f.write(entries.get(i) + '\n');
            System.out.println("Wrote " + entries.get(i));
        }
        f.write(addition);
        System.out.println("Wrote " + addition);
        for (int i = index; i < entries.size(); i++) {
            if (i > 8) {
                System.out.println("Entry overrided: " + i + " ScoreOf " + entries.get(i));
            }
            else {
                f.write(entries.get(i) + '\n');
                System.out.println("Wrote " + entries.get(i));
            }
        }
        f.close();
        return;
    }

    /**
     * A way to access the TreeMap of Integers and Bricks for the level.
     * @return
     */
    public static TreeMap<Integer, Brick> getBricks_LEVEL1() {
        return LEVEL_1;
    }

    /**
     * Creating the level with a 2D array, then using these values to create the actual bricks.
     * Key for array values:
     *      1 = regular brick hardness 1
     *      2 = regular brick hardness 2
     *      and so forth for 3 and 4 -- Regular bricks can have hardness > 4, but not at creation
     *      5 = "fast" special brick
     *      6 = "slow" special brick
     *      7 = "grow" special brick
     */
    public void createLevel1() {
        int[][] BRICKS_1 = new int[10][5];

        // Regular hardness
        for (int y = 0; y < 5; y += 4) {
                for (int x = 0; x <= 9; x++) {
                    // Sets the hardness
                    BRICKS_1[x][y] = 1; }
            }
        for (int y = 1; y < 4; y += 2) {
            for (int x = 0; x <= 9; x++) { BRICKS_1[x][y] = 2; }
        }
        for (int y = 2; y < 3; y ++) {
            for (int x = 0; x <= 9; x++) { BRICKS_1[x][y] = 3; }
            for (int x = 3; x <= 6; x++) { BRICKS_1[x][y] = 4; }
        }

        // Sets the array values of the special bricks
        BRICKS_1[1][4] = 5;
        BRICKS_1[8][1] = 6;
        BRICKS_1[3][0] = 7;
        BRICKS_1[5][4] = 7;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                if (BRICKS_1[x][y] != 0) {
                    if (BRICKS_1[x][y] == 5) {
                        LEVEL_1.put(LEVEL_1.size(),
                                new SpecialBrick(x * 50 + 100, y * 25 + 125, BRICKS_1[x][y], "fast", COURT_WIDTH, COURT_HEIGHT));
                    }
                    else if (BRICKS_1[x][y] == 6) {
                        LEVEL_1.put(LEVEL_1.size(),
                                new SpecialBrick(x * 50 + 100, y * 25 + 125, BRICKS_1[x][y], "slow", COURT_WIDTH, COURT_HEIGHT));
                    }
                    else if (BRICKS_1[x][y] == 7) {
                        LEVEL_1.put(LEVEL_1.size(),
                                new SpecialBrick(x * 50 + 100, y * 25 + 125, BRICKS_1[x][y], "grow", COURT_WIDTH, COURT_HEIGHT));
                    }
                    else {
                        LEVEL_1.put(LEVEL_1.size(),
                                new Brick(x * 50 + 100, y * 25 + 125, BRICKS_1[x][y], COURT_WIDTH, COURT_HEIGHT));
                    }

                }
                else System.out.println("Not a brick at value (" + x + ", " + y + ")");
            }
        }
    }
}
