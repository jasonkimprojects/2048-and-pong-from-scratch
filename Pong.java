import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
/**
 * Pong.java  
 *
 * @author: Jason, Sidd, Travis, Terry 
 * Assignment #: Final Project - Pong
 * 
 * Brief Program Description:
 * A replica of the game Pong to experiment with animations
 *
 */
public class Pong extends JPanel implements ActionListener
{
    private static Timer timer;

    /** Instance variables for location and speed of the puck*/
    private static int puckX, puckY;
    private static int speedX, speedY;

    /** The player's paddle location */
    private static final int p1X = 10; 
    private static int p1Y;

    /** AI paddle location */
    private static final int p2X = 485;
    private static int p2Y;

    /** Keeps track of score */
    private static int p1Score, p2Score;

    /** Incremented with each tick of timer, determines when puck speeds up */
    private static int count;

    /** Constants for paddle width and height for player and AI */
    private static final int PADDLE_WIDTH = 5;
    private static final int PADDLE_HEIGHT = 60;

    /** Constant for puck size */
    private static final int PUCK_SIZE = 10;

    /** Constants for the bounds of the floor */
    private static final int UPPER_X1 = 10;
    private static final int UPPER_Y1= 100;
    private static final int UPPER_X2 = 490;
    private static final int UPPER_Y2 = 100;

    private static final int LOWER_X1 = 10;
    private static final int LOWER_Y1 = 400;
    private static final int LOWER_X2 = 490;
    private static final int LOWER_Y2 = 400;

    /** Player's paddle speed, and AI's paddle speed (difficulty level) */
    private static int paddleSpeed;
    private static int AISpeed = 1;

    private static Color boardColor;

    public Pong()
    {

        /** Reset puck location and speed */
        resetPuck();       

        /** Initialize score and counter*/
        p1Score = 0;
        p2Score = 0;
        count = 0;

        /** Make this panel visible and take key input */
        super.addNotify();
        setFocusable(true);
        setVisible(true);

        /** Initialize timer */
        timer = new Timer(20, this);
        timer.start();

        /** Set gameboard color */
        boardColor = new Color(193,154,107);

    }

    /**
     * Called every time someone scores and at the start of the game
     */
    public void resetPuck()
    {
        /** Initialize paddle location */
        p1Y = 200;
        p2Y = 200;
        /** Initialize puck location*/
        puckX = 250;
        puckY = 250;
        /** Initialize puck speed randomly*/
        int rand = (int)(Math.random()*2); //returns either 0 or 1
        if(rand == 0)
            speedX = 1;
        else
            speedX = -1;
        rand = (int)(Math.random()*2); //rand is overwritten
        if(rand == 0)
            speedY = 1;
        else
            speedY = -1;
        paddleSpeed = 10; //player's paddle speed
        count = 0;
    }

    /**
     * Called at every tick of the timer
     */
    public void actionPerformed(ActionEvent e)
    {
        count++;
        if(count == 50) //speeds up puck after 1 second
        {
            if(speedX > 0)
                speedX+=2;
            else
                speedX-=2;
            if(speedY > 0)
                speedY+=2;
            else
                speedY-=2;
        }
        /** Shift the puck's location */
        puckX += speedX; 
        puckY += speedY;
        /** Activate AI if puck is on its side and is moving towards the paddle */
        if(puckX >= (UPPER_X1 + UPPER_X2) / 2 && speedX > 0)
            AIPlayer();
        repaint();
    }

    /**
     * Controls for the computer player (blue paddle)
     */
    public void AIPlayer()
    {
        int midpoint = p2Y+(PADDLE_HEIGHT / 2); //midpoint of AI's paddle
        /** Adjust AI paddle location based on the puck's location */
        if(puckY > midpoint && p2Y <= LOWER_Y1 - PADDLE_HEIGHT)
            p2Y += AISpeed;
        else if(puckY < midpoint && p2Y >= UPPER_Y1)
            p2Y -= AISpeed;
    }

    /**
     * Gets rid of Applet started text in lower left corner
     * and replaces with instructions
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /**
         * Check for winning conditions, end game if a player wins
         */
        if(p1Score == 7)
        {
            g.setColor(Color.BLACK); 
            g.drawString("Player 1 Wins!", 200, 250);
            timer.stop();
            return;
        }
        else if(p2Score == 7)
        {
            g.setColor(Color.BLACK);
            g.drawString("Player 2 Wins!", 200, 250);
            timer.stop();
            return;
        }

        /** Draw background */
        g.setColor(boardColor);
        g.fillRect(UPPER_X1, UPPER_Y1, UPPER_X2 - UPPER_X1, LOWER_Y1 - UPPER_Y1);

        /** Draw field */
        g.setColor(Color.BLACK);
        g.drawLine(UPPER_X1, UPPER_Y1, UPPER_X2, UPPER_Y2);
        g.drawLine(LOWER_X1, LOWER_Y1, LOWER_X2, LOWER_Y2);
        g.drawLine((UPPER_X1+UPPER_X2)/2, UPPER_Y1,(UPPER_X1+UPPER_X2)/2, LOWER_Y1);
        g.drawOval(220,220,60,60);
        g.fillOval(247,247,6,6);
        g.setColor(Color.RED);
        g.drawLine(UPPER_X1, UPPER_Y1, LOWER_X1, LOWER_Y1);
        g.drawArc(-20,UPPER_Y1,60,300,270,180);
        g.setColor(Color.BLUE);
        g.drawLine(UPPER_X2, UPPER_Y2, LOWER_X2, LOWER_Y2);
        g.drawArc(UPPER_X2-30, UPPER_Y1, 60, 300, 90, 180);

        /** Display player 1's paddle */
        g.setColor(Color.RED);
        g.fillRect(p1X, p1Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        /** Display player 2's paddle */
        g.setColor(Color.BLUE);
        g.fillRect(p2X, p2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        /** Display puck */
        g.setColor(Color.ORANGE);
        g.fillOval(puckX, puckY, PUCK_SIZE, PUCK_SIZE);

        /** Display score and difficulty level */
        g.setColor(Color.RED);
        g.drawString("P1 Score: "+Integer.toString(p1Score), 10, 80);
        g.setColor(Color.BLUE);
        g.drawString("Computer Score: "+Integer.toString(p2Score), 375, 80);
        g.drawString("Difficulty Level: "+Integer.toString(AISpeed), 200, 450);

        /** Conditions to score point */
        if(puckX <= UPPER_X1)
        {
            p2Score++;
            resetPuck();
            return;
        }
        else if(puckX + PUCK_SIZE >= UPPER_X2)
        {
            p1Score++;
            resetPuck();
            return;
        }

        /** Rebound off top and bottom walls */
        if( Math.abs(puckY - UPPER_Y1) <= Math.abs(speedY) || Math.abs(puckY - (LOWER_Y1 - PUCK_SIZE)) <= Math.abs(speedY)) //top and bottom walls
        {
            speedY *= -1; //reverse y speed
        }

        /** Detect collision with paddles */
        if( (Math.abs(puckX - (p1X + PADDLE_WIDTH)) <= Math.abs(speedX) && puckY >= p1Y && puckY <= p1Y + PADDLE_HEIGHT)  || 
        (Math.abs(p2X - (puckX + PUCK_SIZE)) <= Math.abs(speedX) && puckY >= p2Y && puckY <= p2Y + PADDLE_HEIGHT)) //sides of paddles
        {
            speedX *= -1;
        }

    }

    /**
     * Run this method to play the game
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Pong: W/S for movement, 1/2/3 for difficulty level");
        frame.setPreferredSize(new Dimension(520,500));
        JPanel panel = new Pong();
        frame.add(panel); //JFrame to display this panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        /** Input and Action maps for key bindings */ 
        InputMap inputMap = panel.getInputMap();
        ActionMap actionMap = panel.getActionMap();

        /** Set for moving the player's paddle up */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "P1_Up");
        actionMap.put("P1_Up", new P1_UpAction());

        /** Set for moving the player's paddle down */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "P1_Down");
        actionMap.put("P1_Down", new P1_DownAction());

        /** Set for setting the difficulty to easy */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, false), "EasyMode");
        actionMap.put("EasyMode", new EasyMode());

        /** Set for setting the difficulty to normal */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, false), "NormalMode");
        actionMap.put("NormalMode", new NormalMode());

        /** Set for setting the difficulty to hard */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, false), "HardMode");
        actionMap.put("HardMode", new HardMode());

    }

    /**
     * Nested classes that extend from AbstractAction 
     * which are called for each keystroke mapped to the input map
     * From the action map
     */
    static class EasyMode extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            AISpeed = 1;
        }
    }

    static class NormalMode extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            AISpeed = 2;
        }
    }

    static class HardMode extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            AISpeed = 3;
        }
    }

    static class P1_UpAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            if(p1Y >= UPPER_Y1)
                p1Y -= paddleSpeed;
        }
    }

    static class P1_DownAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent ae)
        {
            if(p1Y <= LOWER_Y1 - PADDLE_HEIGHT)
                p1Y += paddleSpeed; 
        }
    }

}
