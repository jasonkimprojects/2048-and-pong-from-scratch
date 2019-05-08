import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.*;
/**
 * GUIVersion_2048.java  
 *
 * @author: Jason, Sidd, Travis, Terry
 * Assignment #: Final Project - 2048
 * 
 * Brief Program Description:
 * The game 2048 from scratch in Java
 *
 */
public class GUIVersion_2048 extends JPanel implements KeyListener
{
    private GameBoard game;
    private int[][] board;

    /**
     * Called once at the start of the Applet
     */
    public GUIVersion_2048() 
    {
        /**
         * Initialize the game and
         * Call the getter method for the 2d array
         */
        game = new GameBoard();
        board = game.getGameBoard();

        /**
         * Add the key listener and set up this panel
         */
        addKeyListener(this);
        super.addNotify();
        setFocusable(true); //important - this makes KeyListener work on the panel
        setVisible(true);

    }

    /**
     * Implementation for interface KeyListener which we won't use
     */
    public void keyPressed(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

    /**
     * Listens to User Input and moves the tiles accordingly
     */
    public void keyTyped(KeyEvent e)
    {
        char ch = e.getKeyChar();
        /**
         * Check what char input is and implement motion
         */
        if(ch == 'w') //move up
        {
            game.up();
            if(game.getUpShifted())
                game.randomSpawn(1);
            game.reset();
        }
        else if(ch == 'a') //move left
        {
            game.left();
            if(game.getLeftShifted())
                game.randomSpawn(1);
            game.reset();
        }
        else if(ch == 's') //move down
        {
            game.down();
            if(game.getDownShifted())
                game.randomSpawn(1);
            game.reset();
        }
        else if(ch == 'd') //move right
        {
            game.right();
            if(game.getRightShifted())
                game.randomSpawn(1);
            game.reset();
        }
        else if(ch == 'u') //undo once
        {
            game.undo();
            game.reset();
        }
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /**
         * Checks if the player won or lost on each call of repaint
         * If so, makes a message box 
         */
        if(game.playerWin())
        {
            JOptionPane.showMessageDialog(null, "You win!");
            return;
        }
        else if(game.playerLose())
        {
            JOptionPane.showMessageDialog(null, "You lose!");
            return;
        }

        /**
         * Fills the yellow background square
         */
        g.setColor(new Color(255,255,204));
        g.fillRect(50,50,400,400); 

        /**
         * Makes the 4x4 grid
         */
        g.setColor(Color.BLACK);
        g.drawLine(50,50,450,50);
        g.drawLine(450,50,450,450);
        g.drawLine(50,450,450,450);
        g.drawLine(50,50,50,450);
        g.drawLine(150,50,150,450);
        g.drawLine(250,50,250,450);
        g.drawLine(350,50,350,450);
        g.drawLine(50,150,450,150);
        g.drawLine(50,250,450,250);
        g.drawLine(50,350,450,350);

        /**
         * Draw a String for each position in the grid
         * If the value in the gameboard array is nonzero
         */
        int xPos = 100;
        int yPos = 100;
        for(int row = 0; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                if(board[row][col] != 0)
                {
                    String num = Integer.toString(board[row][col]);
                    /** Determines tile color based on number */ 
                    if(board[row][col] == 2)
                        g.setColor(Color.RED);
                    else if(board[row][col] == 4)
                        g.setColor(new Color(184,138,0));
                    else if(board[row][col] == 8)
                        g.setColor(Color.BLUE);
                    else if(board[row][col] == 16)
                        g.setColor(Color.PINK);
                    else if(board[row][col] == 32)
                        g.setColor(Color.MAGENTA);
                    else if(board[row][col] == 64)
                        g.setColor(new Color(0,153,0));
                    else if(board[row][col] == 128)
                        g.setColor(new Color(153,77,0));
                    else if(board[row][col] == 256)
                        g.setColor(new Color(0,102,102));
                    else if(board[row][col] == 512)
                        g.setColor(new Color(102,0,102));
                    else if(board[row][col] == 1024)
                        g.setColor(new Color(77,0,0));
                    else
                        g.setColor(new Color(255,215,0));
                    /** Fill in the tile and draw the number in*/
                    g.fillRect(xPos-45, yPos-45, 90, 90);

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                    g.drawString(num, xPos-10, yPos);
                }
                if(xPos == 400) //at end of row, skips to the next row
                {
                    xPos = 100;
                    yPos += 100;
                }
                else 
                {
                    xPos += 100;
                }
            }
        }

        /**
         * Prints the score on the screen
         */
        g.setColor(Color.BLACK);
        g.drawString(game.getScore(), 220, 20);
    }

    /**
     * Run this method to play the game
     */
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("2048: WASD for movement, U to undo once");
        frame.setPreferredSize(new Dimension(520,500));
        JPanel panel = new GUIVersion_2048();
        frame.add(panel); //puts this JPanel inside a JFrame to display it
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
