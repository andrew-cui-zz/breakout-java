/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.util.ArrayList;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    static Leaderboard leaders;

    public void run() {
        /**
         * Instructions frame
         */
        final JFrame instructions = new JFrame("BREAKOUT - made by Yiqun Cui");
        instructions.setMinimumSize(new Dimension(420,320));
        instructions.setLocation(300, 175);
        final JPanel instructs = new JPanel();
        instructs.setBackground(Color.WHITE);
        JLabel label = new JLabel("<html>"
                + "<br>"
                + "      CIS 120 Final Project: BREAKOUT      <br><br><br>"
                + "      You are the bar with blue and pink sides      <br>"
                + "      Move the bar using the left/right arrow keys     <br><br>"
                + "      Bounce the ball off the bar      <br>"
                + "      Hit and break as many bricks as possible!      <br><br>"
                + "      Some bricks will take more hits to destroy      <br>"
                + "      but will be worth far more points      <br><br><br>"
                + "      Special bricks can either make you faster or slower,     <br>"
                + "      or they can make all bricks harder to break.       <br><br>"
                + "      They are different colors from the normal bricks!       <br>"
                + "      However, you can't tell what the effect is until you hit it...       <br><br><br>"
                + "      Don't let the ball hit the dashed line. Good luck!      <br>"
                + "<br>"
                + "      Press [ENTER] to start the game...      <br><br><br>"
                + "</html>");
            instructs.add(label);
        instructions.add(instructs, BorderLayout.CENTER);
        instructions.setBackground(Color.WHITE);

        // Set visible on screen
        instructions.pack();
        instructions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructions.setVisible(true);


        /**
         * Leaderboard frame
         */
        leaders = new Leaderboard();
        final JFrame leaderboard = new JFrame("High Scores");
        final JPanel display = new JPanel();
        leaderboard.setMinimumSize(new Dimension(350,350));
        leaderboard.setLocation(300, 175);
        display.setBackground(Color.WHITE);

        ArrayList<String> l = leaders.getEntries();
        String bigString = "";

        for (String s : l) {
            bigString = (bigString + "      " + s + "      " + "<br>");
        }
        final JLabel adder = new JLabel("<html>" + "<br><br>"
                + "      Highest scores for Brickbreaker so far      <br><br><br>"
                + bigString
                + "</html>");

        System.out.println("Added entries part 2");
        display.add(adder);
        leaderboard.add(display);

        System.out.println("Created leaderboard");
        leaderboard.add(display, BorderLayout.CENTER);
        leaderboard.setBackground(Color.WHITE);

        leaderboard.pack();
        leaderboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        leaderboard.setVisible(false);


        /**
         * Game frame
         */
        final JFrame frame = new JFrame("Breakout");
        frame.setLocation(240, 0);
        frame.setBackground(Color.BLACK);


        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);

        // Control JPanel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Player label
        final JLabel player = new JLabel("Player: ");
        control_panel.add(player);

        // Score label
        final JLabel scoreboard = new JLabel("   Score: " + 0 + " points     ");
        control_panel.add(scoreboard);

        // Status label
        final JLabel status = new JLabel("Game in progress...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status, player, scoreboard);
        frame.add(court, BorderLayout.CENTER);
        court.setBorder(new LineBorder(Color.GRAY));

        // Reset button
        final JButton reset = new JButton("New");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
                court.startGame = true;
            }
        });
        control_panel.add(reset);


        final JButton inst = new JButton("Instructions");
        inst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.setVisible(false);
                instructions.setVisible(true);
                court.reset();
                court.playing = false;
            }
        });
        control_panel.add(inst);

        // Updates the leaderboard when button pressed
        final JButton lead = new JButton("Leaderboard");
        lead.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display.removeAll();
                ArrayList<String> l = leaders.getEntries();
                String bigString = "";

                for (int i = 0; i < l.size(); i++) {
                    bigString = (bigString + (i+1) + ".       " + l.get(i) + "      " + "<br>");
                }
                final JLabel adder = new JLabel("<html>" + "<br><br>"
                        + "      Highest scores for Brickbreaker so far      <br><br><br>"
                        + bigString
                        + "</html>");
                leaderboard.remove(display);
                leaderboard.setMinimumSize(new Dimension(350,350));
                leaderboard.setLocation(300, 175);
                display.add(adder);
                System.out.println("Added entries part 2");
                leaderboard.add(display);
                leaderboard.setVisible(true);
                //frame.setVisible(false);
                court.reset();
                court.playing = false;
            }
        });
        control_panel.add(lead);

        final JPanel leaderboard_panel = new JPanel();
        leaderboard.add(leaderboard_panel, BorderLayout.NORTH);

        final JButton returner = new JButton("Return");
        returner.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leaderboard.setVisible(false);
                leaderboard.setTitle("High Scores");
            }
        });
        leaderboard_panel.add(returner);

        JButton   quit  = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        control_panel.add(quit);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        instructions.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    instructions.setVisible(false);
                    frame.setVisible(true);
                }
            }
        });

        // Set visible on screen, instructions starts first
        instructions.pack();
        instructions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructions.setVisible(true);

        // Start game
        court.reset();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

}
