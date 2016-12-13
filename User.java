/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

import java.awt.*;

/**
 * The user bar.
 */
public class User extends GameObj {
    public static final int LENGTH = 100;
    public static final int WIDTH = 15;
    public static final int INIT_X = 300; // Top-left corner
    public static final int INIT_Y = 565; // Top-left corner
    public static final int INIT_VEL = 0; // Only in x-direction

    /**
     * Constructs the user bar.
     */
    public User(int courtWidth, int courtHeight) {
        super(INIT_VEL, 0, INIT_X, INIT_Y, LENGTH, WIDTH, courtWidth,
                courtHeight);
    }

    /**
     * Draws the user bar.
     */
    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(255, 150, 150));
        g.fillRect(pos_x, pos_y + 1, width, height);
        g.setColor(Color.BLUE);
        g.fillRect(pos_x + 2, pos_y + 1, width - 4, height);
        g.setColor(Color.WHITE);
        g.fillRect(pos_x + 4, pos_y + 1, width - 8, height);
        g.setColor(new Color(127, 255, 0));
        g.fillRect(pos_x + 10, pos_y + 8, width - 20, height - 13);
    }
}
