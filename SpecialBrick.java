/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

import java.awt.Color;
import java.awt.Graphics;

/**
 * An interface for special bricks
 */

public class SpecialBrick extends Brick implements BrickInterface {
    private int mohs; // hardness of the brick
    public String type;

    // Coordinates of the top-left corner
    private int x;
    private int y;

    // Put into map
    public SpecialBrick(int x, int y, int hard, String specialtype, int COURT_WIDTH, int COURT_HEIGHT) {
        super(x, y, hard, COURT_WIDTH, COURT_HEIGHT);
        if (x < 0) { this.x = 0; }
            else { this.x = x; }
        if (y < 0) { this.y = 0; }
            else { this.y = y; }
        type = specialtype;
        switch (type) {
            case "slow":
                mohs = 1;
                break;
            case "fast":
                mohs = 2;
                break;
            case "grow":
                mohs = 3;
                break;
            default: // Treat it like a regular brick then
                if (hard <= 0) { mohs = 1; }
                else { mohs = hard; }
        }
    }

    /** Drawing the bricks in the grid */
    @Override
    public void draw(Graphics gc) {
        Color c = gc.getColor();

        gc.setColor(Color.WHITE);
        gc.drawRect(x, y, brickl, brickh);

        if (type.equals("slow") || type.equals("fast") || type.equals("grow")) {
            if (mohs >= 4) { gc.setColor(new Color(225, 255, 255)); }
            else if (mohs == 3) { gc.setColor(new Color(190, 250, 190)); }
            else if (mohs == 2) { gc.setColor(new Color(255, 240, 180)); }
            else { gc.setColor(new Color(255, 200, 200)); }
        } else {
            // Same as regular bricks!
            if (mohs >= 4) { gc.setColor(new Color(60, 60, 240)); }
            else if (mohs == 3) { gc.setColor(new Color(100, 200, 40)); }
            else if (mohs == 2) { gc.setColor(new Color(255, 240, 60)); }
            else { gc.setColor(new Color(255, 50, 50)); }
        }

        gc.fillRect(x + 1, y + 1, brickl - 2, brickh - 2);
        gc.setColor(c); // Set back to initial color;
    }

    /** Results of the ball hitting this brick */
    @Override
    public String isHit(Ball b) {
         mohs--;
         if (mohs == 0) {
             switch (type) {
                 case "slow":
                     slowEffect(b);
                     return "remove";
                 case "fast":
                     fastEffect(b);
                     return "remove";
                 case "grow":
                     return "grow";
                 default:
                     return "remove";
             }
         }
         return "keep";
    }

    /**
     * Makes the ball go faster, for "fast" bricks.
     * Calls the Ball.trimVelocity() function to avoid velocities too high.
     * @param b The Ball
     */
    public void fastEffect(Ball b) {
        if (b.v_y < 0) {
            if (b.v_x < 0) { b.v_x--; b.v_y--; } else { b.v_x++; b.v_y--; }

        } else { // b.v_y > 0
            if (b.v_x < 0) { b.v_x--; b.v_y++; } else { b.v_x++; b.v_y++; }
        }
        b.trimVelocity();
    }

    /**
     * Makes the ball go slower, for "slow" bricks.
     * If either velocity component hits 0, it is set to positive 1 automatically.
     * @param b The Ball
     */
    public void slowEffect(Ball b) {
        if (b.v_y < 0) {
            if (b.v_x < 0) { b.v_x++; b.v_y++; } else { b.v_x--; b.v_y++; }
        } else {
            if (b.v_x < 0) { b.v_x++; b.v_y--; } else { b.v_x--; b.v_y--; }
        }
        if (b.v_x == 0) { b.v_x = 1; }
        if (b.v_y == 0) { b.v_y = 1; }
    }

    /**
     * Adjusts the hardness of the brick (number of hits needed).
     * This is used with "grow" SpecialBricks.
     */
    @Override
    public void setHard(int hard) {
        if (hard <= 0) { mohs = 1; }
        else { mohs = hard; }
    }

    /**
     * Returns the current hardness/number of hits needed.
     */
    @Override
    public int getHard() {
        return mohs;
    }


}
