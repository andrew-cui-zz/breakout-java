# cis120-finalproject

=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
Name: Andrew (Yiqun) Cui
Game: Breakout
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=


==========================
=: Overview of the game :=
==========================

Download: files.zip, with the src .java files, four text files, and junit.jar
Run: Terminal (Linux):
  unzip *.zip
  javac -cp junit.jar *.java && java Game
  
Play:
  Spacebar: start game (velocities can only be +/- 2 at most)
  Arrow keys: move from side to side
  n: start new game after losing
  
Instructions:
  Use the bar to hit the ball, bouncing it off to destroy the bricks.
  Some bricks will take more hits to break. Most bricks change colors when hit.
  Some bricks have special effects and are different colors.
  You will get more points for hitting certain bricks than others.
  Win either by destroying all of the bricks.
  Lose by letting the ball hit the dashed line.
  Top ten scores are saved in LEADERBOARD.TXT
  
================================
=: Overview of class function :=
================================


Ball: Ball is an object class rouhgly based on the Circle class in the Mushroom of Doom program. It
  represents the ball that flies around the screen and is responsible for destroying bricks. When
  a ball hits an object, GameCourt calls the bounce(Object obj) function, which uses an algorithm I
  designed to bounce it off by changing its velocity (ex: bounce off left side --> v_x = -v_x). The 
  ball's velocity can also be adjusted by functions within the rest of the program, such as the 
  SpecialBrick's fastEffect(Ball b) and slowEffect(Ball b) functions. However, there is a function
  in the Ball class called trimVelocity() that reduces the magnitude of a component velocity to 2
  if it exceeds 2 to avoid the ball becoming too fast for the user. This works for both negative and 
  positive velocities(ex: if v_y = 3, then v_y is set to 2; if v_y = -3, it is set to -2).


Direction: Direction is an enumerated class that was provided in the code for the Mushroom of Doom.
  It is used with the Ball class and some of the collisions, notably ball.bounce(ball.hitWall());


Brick: Brick is a class that implements BrickInterface and creates rectangular instances of regular 
  bricks that the user attempts to destroy. There are several functions within Brick, like getX(), 
  getY(), getLength, and getHeight(), that return these values. The constructor has built-in 
  functionality to prevent the X or Y values from being negative, and sets one to 0 if it is passed 
  in as a negative argument (the length and height cannot be changed). The draw(Graphics gc) 
  function draws the brick on the screen, which involves a white border filled in with colors. The 
  brick also has an internal variable called mohs, which refers to the hardness of the brick, or the 
  number of hits it takes to destroy the brick. This value is initialized in the constructor, and if 
  the value is 0 or negative it is set to 1. getHard() returns the current value of this variable, 
  while setHard() changes the value of the variable, but does not allow it to be set to a zero or 
  negative number. 
  
  The fill color of the bricks, done in draw(Graphics gc) is influenced by the 
  hardness -- hardness of 1 is red, 2 is yellow, 3 is green, and 4 is dark blue. The Brick class 
  also has a function called isHit(Ball b). When a brick is hit by a ball, it decrements the mohs 
  variable and returns “keep” if the result is 1 or higher and “remove” otherwise. Remove indicates 
  that the brick no longer can take any more hits and should be destroyed. The actual removal of the 
  brick occurs in the GameCourt class, where the brick is removed from the TreeMap of keys and 
  bricks and is thereafter never involved in the instance of the game.


BrickInterface: BrickInterface is an interface that provides the structure for how the Brick and
  SpecialBrick (which extend Brick and implement BrickInterface) classes are constructed. It has
  shells of functions including: draw(Graphics gc), isHit(Ball b), setHard (int hard), getHard(),
  getX(), getY(), getLength(), and getHeight(). BrickInterface also controls the length and height 
  values, which are constant and equal for all bricks -- length = 50, and height = 25.


SpecialBrick: SpecialBrick is a class that extends Brick via inheritance. As it extends Brick and 
  has no differences in how it is placed or sized, it does not contain functions for getX(), getY(),
  getLength(), or getWidth() -- if SpecialBrick.getX() is called, for example, dynamic dispatch will
  call on Brick.getX() and return the x value of this SpecialBrick using that function. The 
  constructor for SpecialBrick also ensures that the x and y values cannot be negative, but the mohs 
  variable here is set differently -- it is set to either 1, 2, or 3, depending if the brick is a 
  “slow”, “fast”, or “grow” brick, respectively. If a string in the constructor arguments passed in 
  for the type of the special brick does not match one of “slow”, “fast”, or “grow”, the constructor 
  creates a SpecialBrick that functions just like a regular brick. While the mohs input for the 
  super() constructor may be different from this value, it is not considered because what is 
  important is the assigned mohs value for that specific SpecialBrick. This is also why there is a 
  getHard() function and a setHard() function in SpecialBrick, because they retrieve or change that 
  specific value, not the one instantiated in the superclass. 
  
  Draw leads to different colors than the regular bricks; this is an instance of override. isHit() 
  is also an instance of override due to the very different results of calling this method from the 
  isHit() method in Brick.java The decrement in the mohs variable is the same, but if mohs hits zero
  for fast and slow bricks it will trigger the fastEffect() and slowEffect() functions, which take 
  in a Ball object and then change the velocities. fastEffect increases the magnitude of velocity up
  to a magnitude of 2 , slowEffect decreases it (but if either component hits 0, it is set to a 
  velocity of 1). For fast and slow bricks, keyword “remove” is returned by isHit(), and the 
  SpecialBricks are removed by the GameCourt. For “grow” bricks, the keyword “grow” is removed -- 
  then, the GameCourt increases the hardness of the other bricks while removing this one. These 
  SpecialBricks are a big factor in making the game more interesting and challenging for the player.


User: User is the bar that the player moves around to bounce bricks off. It extends GameObj. There
  are no functions within User outside of draw() and the constructor. The primary focus of the User
  objects is to be moved by arrow key input for the player to control and bounce the ball off with.


Game: Game is a class that sets up the game and runs it. It was included in the Mushroom of Doom.
  Within Game, there is a top and bottom panel in addition to a panel for the GameCourt. Each Game
  includes the instantiation of GameCourt. In the top bar, the current player's name is displayed.
  The running score, which is incremented each time a brick is hit, is also displayed. New button 
  sets up a new game and resets the GameCourt. Instructions button views the instructions frame. 


GameCourt: GameCourt is a class that manages the active game's field state. It was included in the
  Mushroom of Doom. The GameCourt takes in the player, status, and score JLabels from the Game class 
  in its parameters, which it updates throughout the progression of the game.

  The reset() function clears a previous game state and restores to a new game state, resetting the 
  score, player, and status. It also creates new instances of the user and ball. This is called by 
  the Game class’s New button, or by clicking n after losing a game. The reset() function is used to 
  set up new games after one ends. In the reset function, the bricks are also created. There is a 2D 
  array of bricks values, where each entry is an integer -- this is present. When reset() is called,
  a function to create the level is created. This takes the array values and makes bricks depending 
  on what these values are. If the value at a certain array entry is 1, 2, 3, or 4, then a regular 
  brick with that number hardness is created (the row and column values of the array are multiplied 
  by the length and height, respectively, of bricks and added to a constant so that the bricks do 
  not overlap and are not too close to the sides). If it is 5, it is a fast brick; if it is 6, it is
  a slow brick; if it is 7, it is a grow brick -- this creates a SpecialBrick with that type of 
  special effect. All these bricks are put into a TreeMap of integer indexes and Bricks.
  
  GameCourt’s paintComponent function allows for the drawing of the objects on the screen -- it 
  draws the ball, user, the line that indicates when a game is lost, and every brick in the TreeMap 
  by calling on the draw (Graphics gc) function in each of these classes.

  There is a variable called playing, which is true if a game is active and false if not. Within the
  reset() function, the playing variable is initially set to false. When the player hits spacebar, a 
  KeyListener is activated and begins the process of creating a new game. This includes inputting 
  initial velocities (which only are accepted if between -2 and 2) and the user’s name. The name is 
  stored as variable String curr_player, and is updated to the JLabel of player and displayed in the 
  top display panel (which is monitored by the Game class). After this, it sets playing to be true. 

  GameCourt also has a timer, which is in charge of essentially running the game functions. When the
  game is playing, then the tick function of the timer manages the functions within the game and how
  they interact to allow the user to play. This calls functions for the user and ball to move, and 
  for the ball to bounce off walls if it hits any. It also includes collision detection between the
  ball and a brick. If the ball hits the user, it bounces off by multiplying y-velocity by -1.

  The program is constantly scanning to see if the ball intersects any of the bricks in the TreeMap
  using the intersects() function from GameObj. If that happens, then the Brick.isHit() function is
  called on that brick (overrided by SpecialBrick.isHit() if it is a special brick). This affects 
  the state of the brick, and this function returns “keep” if the brick can take more hits, “remove”
  if it has to be destroyed, and “grow” if it destroyed a “grow” SpecialBrick. If it is “keep,” then
  the ball bounces off, the score is incremented and the top panel label is updated, the status 
  label is set to “Hit a brick…,” and the game continues. If it is “remove,” then the brick is 
  removed from the entire set of bricks, and the rest of the effects from “keep” (score increment, 
  etc.) also happen. Finally, if it is “grow,” then the brick is removed, and the same effects from
  “keep” happen. However, the GameCourt also goes through every remaining brick in the TreeMap and
  increases its hardness variable, which also triggers a change in color.

  If at any time the TreeMap<Integer, Brick> has size 0, then the game ends. Playing is set to false
  the status bar is changed, and the endGame(this.score, this.player) function is called. Similarly,
  the player can lose if the ball hits the bottom line without bouncing off the user. 

  endGame is used to check if it is a high score or not. It retrieves an ArrayList<String> of high 
  score entries and an ArrayList<Integer> of values of high scores, and uses checkHighScore() to 
  compare the current score. If it is not a high score, it sets the status bar to say so and then 
  the user can use the New button to play again. If it is, then it changes the status bar label to 
  congratulate the user. To update the leaderboard, it first uses getLeaderboardIndex(), which uses 
  a BufferedReader on the file LEADERBOARD.TXT to determine what index in the ArrayList of high 
  scores the new score should go. Then, it creates an entry string with name + “, “ + score, in the 
  high score format of the leaderboard. Finally, it uses editLeaderboard() to adjust the 
  ArrayList<String> of high scores to include the new entry. It trims this ArrayList to just 10 
  values if there are more (the game only records top 10 scores). It uses a BufferedWriter to write 
  out these 10 string entries to the LEADERBOARD.TXT file and displays the leaderboard for the user. 
  Now, the user will see the 10 highest scores, including their score, and the scores will be 
  ordered by the value of the score. They cal also click on the Leaderboard button in the top panel,
  which is controlled by Game.java class, to view the leaderboards.
  

GameObj: GameObj is a class that provides the structure for all objects within the game, and was 
  included in the Mushroom of Doom. Brick, Ball, and User all extend GameObj. GameObj includes 
  functions that include move(), used in GameCourt to make objects move; clip(), which avoids the 
  object going out of bounds; hitWall(), which balls use to bounce off walls; and intersects(),
  which determines if the bounding boxes of objects are overlapping and is used in determining
  collisions between Balls and Bricks in GameCourt, crucial to the process of removing bricks.


Leaderboard: Leaderboard is a class that serves exactly one function: for the Game class to extract 
  the current leaderboard entries without having to go into the GameCourt class. Its getEntries()
  function returns an ArrayList of strings, each string being an entry in the form Name, Score for 
  the leaderboard. This is called by the Game function whenever the Leaderboard button is pressed, 
  and it updates the leaderboard frame/panel so that the leaderboard displays the updated set of
  highest scores.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

For my dashed lines and the JOptionPane for entering the velocities and names at the start of the
game, and the GridLayout for that panel, I referenced the following Oracle docs:
- http://docs.oracle.com/javase/6/docs/api/java/awt/BasicStroke.html
- http://docs.oracle.com/javase/7/docs/api/java/awt/Graphics2D.html
- http://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html
- https://docs.oracle.com/javase/8/docs/api/java/awt/GridLayout.html
  
I also looked at the implementation of the popup panels and graphics in the provided Paint.java
and my Pennstagram.java code, as they were not related to my core concepts. The implementation of
these in the final design of my project is a modified version of this code. 
  
With my instructions pane, I wrote it out in HTML since I had been struggling with using JLabels for
some reason. References include, both for the idea to use HTML in the JLabel and HTML elements:
- CIS 120 Piazza @2179
- http://stackoverflow.com/questions/685521/multiline-text-in-jlabel
- http://www.w3schools.com/html/html_elements.asp
