/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/



import java.awt.*;

/**
 * A basic game object displayed as a yellow circle, starting in the upper left
 * corner of the game court.
 *
 */
public class Ball extends GameObj {

    public static final int SIZE = 20;
    public static final int INIT_POS_X = 340;
    public static final int INIT_POS_Y = 545;
    public static final int INIT_VEL_X = -3;
    public static final int INIT_VEL_Y = -5;

    /**
     * Constructs a ball object.
     * @param courtWidth
     * @param courtHeight
     */
    public Ball(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE,
                courtWidth, courtHeight);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(pos_x, pos_y, width, height);
        g.setColor(Color.WHITE);
        g.fillOval(pos_x + 1, pos_y + 1, width - 2, height - 2);
    }

    /**
     * If the magnitude of either the x/y component velocity is greater than 2, it is set at +/- 2.
     * This is to avoid the game getting too fast to the point where it might outspeed the ticks.
     */
    public void trimVelocity() {
        if (v_x < -2) { v_x = -2; }
        if (v_y < -2) { v_y = -2; }
        if (v_x > 2) { v_x = 2; }
        if (v_y > 2) { v_y = 2; }
    }

    /**
     * Has the ball bounce off of bricks, both in left/right cases and corner cases.
     * @param b
     */
    public void bounce(Brick b) {
        int bounce_case = 0;

        // Regular bounce cases
        if (this.pos_y == (b.pos_y + b.height)) { }
        else if (pos_y == b.pos_y) { }
        else if (pos_x == (b.pos_x + b.width)) { bounce_case = 1; }
        else if (pos_x + width == b.pos_x) { bounce_case = 1; }

        // Corner cases
        // Top-left
        else if (pos_x + width == b.pos_x && pos_y + height == b.pos_y) { bounce_case = 2; }
        // Top-right
        else if (pos_x == (b.pos_x + b.width) && pos_y + height == b.pos_y) { bounce_case = 2; }
        // Bottom-left
        else if (pos_x + width == b.pos_x && pos_y == b.pos_y + b.height) { bounce_case = 2; }
        // Bottom-right
        else if (pos_x == (b.pos_x + b.width) && pos_y + height == b.pos_y + b.height) { bounce_case = 2; }


        switch (bounce_case) {
            case 1: // Hits left or right
                v_x = -v_x;
                move();
                break;
            case 2: // Hits corners
                v_x = -v_x;
                v_y = -v_y;
                move();
                break;
            default: // Case 0, Hits top or bottom
                v_y = -v_y;
                move();
                break;
        }
    }

}
