/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

import java.awt.Color;
import java.awt.Graphics;

/**
 * An interface for bricks
 */

public class Brick extends GameObj implements BrickInterface {
    private int mohs; // hardness of the brick

    // Coordinates of the top-left corner
    private int x;
    private int y;

    /**
     * Creating the brick object
     * @param x
     * @param y
     * @param hard
     * @param COURT_WIDTH
     * @param COURT_HEIGHT
     */
    public Brick(int x, int y, int hard, int COURT_WIDTH, int COURT_HEIGHT) {
        super(0, 0, x, y, brickl, brickh, COURT_WIDTH, COURT_HEIGHT);
        if (x < 0) { this.x = 0; }
            else { this.x = x; }
        if (y < 0) { this.y = 0; }
            else { this.y = y; }
        if (hard <= 0) { mohs = 1; }
            else { mohs = hard; }
    }

    /**
     * Drawing the bricks in the grid
     *
     */
    public void draw(Graphics gc) {
        Color c = gc.getColor();
        // Draw border
        gc.setColor(Color.WHITE);
            gc.drawRect(x, y, brickl, brickh);

        // Adjust fill depending on the brick!
        if (mohs >= 4) { gc.setColor(new Color(60, 60, 240)); }
        else if (mohs == 3) { gc.setColor(new Color(100, 200, 40)); }
        else if (mohs == 2) { gc.setColor(new Color(255, 240, 60)); }
        else { gc.setColor(new Color(255, 50, 50)); }

        gc.fillRect(x + 1, y + 1, brickl - 2, brickh - 2);
        gc.setColor(c); // Set back to initial color;
    }

    /**
     * Results of the ball hitting this brick, sends command to remove brick if it hits 0.
     */
    public String isHit(Ball b) {
         mohs--;
         if (mohs == 0) {
             return "remove";
         }
         return "keep";
    }

    /**
     * Adjusts the hardness of the brick (number of hits needed).
     * This is used with "grow" SpecialBricks.
     */
    public void setHard(int hard) {
        if (hard <= 0) { mohs = 1; }
        else { mohs = hard; }
    }

    /**
     * Returns the current hardness/number of hits needed.
     */
    public int getHard() {
        return mohs;
    }

    /**
     * Obtains the x-position of the top-left corner of the brick
     */
    public int getX() { return x; }
    public int getY() { return y; }

    /**
     * Obtains the length and width/height of the bricks, which are set in BrickInterface
     */
    public int getLength() { return brickl; }
    public int getHeight() { return brickh; }




}
