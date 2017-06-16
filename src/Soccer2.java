
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author jonej9442
 */
public class Soccer2 extends JComponent {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    //Title of the window
    String title = "My Game";
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    // YOUR GAME VARIABLES WOULD GO HERE
    //making players 
    Rectangle player1 = new Rectangle(0, 0, 25, 60);
    Rectangle player2 = new Rectangle(WIDTH - 25, HEIGHT - 100, 25, 60);
    //creating the grass 
    Color grass = new Color(127, 219, 74);
    //creating the ball and width and height divided by 2 to get the exact middle
    Rectangle ball = new Rectangle(WIDTH / 2, HEIGHT / 2, 25, 25);
    //player 1 movement
    boolean downPressed;
    boolean upPressed;
    boolean leftPressed;
    boolean rightPressed;
    //player 2 movement 
    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    //shooting keys variables
    boolean spacePressed;
    boolean enterPressed;
    //ball speed
    int velocityx = 2;
    int velocityY = 2;
    
    //to be able to hold onto the ball
    int playerBall = 0;
    //player scores
    int player1Score = 0;
    int player2Score = 0;
    Font myFont = new Font("Arial", Font.BOLD, 75);

    // GAME VARIABLES END HERE   
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Soccer2() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Soccer2.Keyboard());
        Soccer2.Mouse m = new Soccer2.Mouse();

        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE
        g.setColor(grass);
        g.fillRect(0, 0, 800, 600);

        //creating the rectangles
        g.setColor(Color.RED);
        g.drawRect(player1.x, player1.y, player1.width, player1.height);
        g.setColor(Color.BLUE);
        g.drawRect(player2.x, player2.y, player2.width, player2.height);

        //creating the ball 
        g.setColor(Color.WHITE);

        //so players can hold the ball (=1 when player 1 is holding it and 2 for player 2)
        if (playerBall == 1) {
            ball.x = player1.x;
            ball.y = player1.y;
        } else if (playerBall == 2) {
            ball.x = player2.x;
            ball.y = player2.y;
        }
        //drawing of the ball
        g.fillRect(ball.x, ball.y, ball.width, ball.height);


        //drawing line
        g.setColor(Color.WHITE);
        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);

        //making the text and its colour 
        g.setFont(myFont);
        g.setColor(Color.red);
        //drawing score for player 1 and 2
        g.drawString("" + player1Score, WIDTH / 2 - 100, 75);
        g.drawString("" + player2Score, WIDTH / 2 + 50, 75);

        //creating the nets 
        g.setColor(Color.BLACK);
        g.drawRect(0, 200, 30, 200);
        g.drawRect(768, 200, 30, 200);
        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void preSetup() {
        // Any of your pre setup before the loop starts should go here
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;

        long deltaTime;

        preSetup();

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 

            //player 2 movement (up,down,left,right)
            if (upPressed) {
                player2.y = player2.y - 5;

            }
            if (downPressed) {
                player2.y = player2.y + 5;

            }
            if (leftPressed) {
                player2.x = player2.x - 5;
            }
            if (rightPressed) {
                player2.x = player2.x + 5;
            }
            //player 1 movement (up,down,left,right)
            if (wPressed) {
                player1.y = player1.y - 5;

            }
            if (sPressed) {
                player1.y = player1.y + 5;

            }
            if (aPressed) {
                player1.x = player1.x - 5;

            }
            if (dPressed) {
                player1.x = player1.x + 5;
               
            }
            //updates the x coordinate every second 
            //adding to the velocity of the ball 
            //movement of the ball
            ball.x += velocityx;
            ball.y += velocityY;

            //collisions to work 
            collisions();
            
            //functions for the shooting button. 
            //velocity is inversed for player 2 so that it shoots in the right direction
            if (enterPressed && playerBall == 2) {
                playerBall = 0;
               velocityx = -4;
                velocityY = 4;
            }
            //player 1 shooting (velocity is 4 when let go)
            if (spacePressed && playerBall == 1) {
                playerBall = 0;
                velocityx = 4;
                velocityY = 4;
            }
            //when the ball is in the net a point is added and the rest of the wall it bounces off 
            //if the player has the ball then it doesn't add a point if they go into the net 
                if (playerBall == 1) {
                    //so the players can not get a point if they are holding the ball in the net
                    if (player1.x <= 0) {
                        player1.x = 0;
                }
                if (player1.x >= 770) {
                    player1.x = 770;
                }
            } else {
                    //if player 1 has actually scored then the score will be updated and ball reset
                if (ball.x >= 770 && player1.x >= 400 && ball.y >= 200 && ball.y <= 400) {
                    resetBall();
                    player1Score++;
                }
            }
                //player 2 scoring and bouncing off the walls 
            if (playerBall == 2) {
                //so player 2 can not go through the side walls and get points when holding the ball
                if (player2.x >= 770) {
                    player2.x = 770;
                }
                if (player2.x <= 0) {
                    player2.x = 0;
                }
            } else {
                //if the player has actually scored then a point will be added and the ball will be reset

    if (ball.x <= 0 && player2.x <= 400 && ball.y >= 200 && ball.y <= 400) {
                    resetBall();
                    player2Score++;
                }
            }


            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {
        // if a mouse button has been pressed down

        @Override
        public void mousePressed(MouseEvent e) {
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {
        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {
        // if a key has been pressed down

        @Override
        public void keyPressed(KeyEvent e) {
            // if keys are pressed then = true and the keys will move
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            //if keys are released and the movement stops 
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                spacePressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                enterPressed = false;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        Soccer2 game = new Soccer2();

        // starts the game loop
        game.run();
    }

// if the ball hits the players
    public void collisions() {
        //when the players intersect the ball they hold onto it
        if (ball.intersects(player1)) {
            velocityx = 0;
            velocityY = 0;
            playerBall = 1;

            //set velocity to 0 and as it moves up or down then add 1 eachtime 
        }
        //when player 2 intersects the ball it stops it and now moves with the player
        if (ball.intersects(player2)) {
            //inverse the velocity
            velocityx = 0;
            velocityY = 0;
            playerBall = 2;
        }
        //stealing the ball
        if (player1.intersects(player2)) {
            if (playerBall == 1) {
                playerBall = 2;
            }
        }
        if (player2.intersects(player1)) {
            if (playerBall == 2) {
                playerBall = 1;
            }
        }

        //ball speed
        if (ball.y <= 0) {
            velocityY = 4;
        }

        //bouncing off the side walls 
        if (ball.y + ball.height >= HEIGHT) {
            velocityY = -velocityY;
        }

        if (ball.x + ball.width >= WIDTH) {
            velocityx = -velocityx;
        }
        if (ball.x <= 0) {
            velocityx = -velocityx;
        }

        //player y does now go off the screen to the negatives
        if (player1.y <= 0) {
            player1.y = 0;
        }

        if (player1.y + player1.height >= HEIGHT) {
            player1.y = player1.y - 5;
        }

        if (player2.y <= 0) {
            player2.y = 0;
        }

        //bottom part
        if (player2.y + player2.height >= HEIGHT) {
            //subtract by speed
            player2.y = player2.y - 5;
        }
        //bouncing off the opposite walls 
        if (player1.x <= 0) {
            player1.x = 0;
        }

        if (player2.x >= 770) {
            player2.x = 770;
        }

        if (player1.x >= 770) {
            player1.x = 770;
        }
        if (player2.x <= 0) {
            player2.x = 0;
        }
    }

    //reseting the ball in the middle 
    public void resetBall() {
        ball.x = WIDTH /2;
        ball.y = HEIGHT /2;
    }
}

