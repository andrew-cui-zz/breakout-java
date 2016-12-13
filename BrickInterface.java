/**
CIS 120 Game Project
Yiqun Cui
Game: Breakout
*/

import java.awt.Graphics;

/**
 * An interface for bricks
 */
public interface BrickInterface {

    // for all bricks
    public final int brickl = 50;
    public final int brickh = 25;

    /** Drawing the bricks in the grid */
    public void draw(Graphics gc);

    /** Results of the ball hitting this brick */
    public String isHit(Ball b);

    /** Changes the hardness */
    public void setHard(int hard);

    /** Returns the hardness for scoring purposes! */
    public int getHard();

    /**
     * Obtains the x-position of the top-left corner of the brick
     */
    public int getX();
    public int getY();

    /**
     * Obtains the length and width of the bricks, which are set in BrickInterface
     */
    public int getLength();
    public int getHeight();

}
