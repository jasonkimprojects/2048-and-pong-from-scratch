import java.util.ArrayList;
/**
 * GameBoard.java  
 *
 * @author: Jason, Sidd, Travis, Terry
 * Assignment #: Final Project - 2048
 * 
 * Brief Program Description:
 * The game 2048 from scratch in Java
 *
 */
public class GameBoard 
{
    /** The 2D array to be used as the gameboard */
    private int[][] array2d;
    /** Boolean toggles for shifting in each direction */
    private boolean leftShifted = false, rightShifted = false, upShifted = false, downShifted = false;
    /** Current score */
    private int score;
    /** Backup score and backup 2D gameboard array */
    private int backupScore;
    private int[][] backup;

    /** Constructor, initializes vars and creates a copy*/
    public GameBoard()
    {
        array2d = new int[4][4];
        randomSpawn(2);
        score = 0;
        backupScore = 0;
        backup = new int[4][4];
        copy();
        //makes 2 random values on the board and copies the board to the backup
    }

    /** Copies the contents of the current gameboard into the backup and copies the score */
    private void copy()
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                backup[row][col] = array2d[row][col];
            }
        }
        backupScore = score;
    }

    /** Copies the contents of the back up gameboard and the score*/
    public void undo()
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                array2d[row][col] = backup[row][col];
            }
        }
        score = backupScore;
    }

    /**
     * Getter methods
     */
    public boolean getLeftShifted()
    { return leftShifted; }

    public boolean getRightShifted()
    { return rightShifted; }

    public boolean getUpShifted()
    { return upShifted; }

    public boolean getDownShifted()
    { return downShifted; }

    /**
     * Called at the end of every move and resets the boolean toggles
     */
    public void reset()
    {
        leftShifted = false;
        rightShifted = false;
        upShifted = false;
        downShifted = false;
    }

    /** Just another getter method */
    public String getScore()
    { return "Score: "+score;}

    /**
     * @param the number of random values spawned on the board
     * Finds an empty spot on the gameboard and fills it in with a 2
     * If there are no empty spots, this method does nothing
     */
    public void randomSpawn(int num)
    {
        /**
         * Important - prevents infinite loop!
         */
        for(int i=0; i<num; i++)
        {
            if(FullBoard())
                return;
            boolean filled = false;
            while(!filled)
            {
                int randomRow = (int) (Math.random()*4);
                int randomCol = (int) (Math.random()*4);
                if(array2d[randomRow][randomCol] == 0)
                {
                    array2d[randomRow][randomCol] = 2;
                    filled = true;
                }
            }
        }
    }

    /**
     * Helper method for moving left
     */
    private void moveLeft() 
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 1; col < 4; col++) //this order matters -- if shifting left, process elements on left first
            {
                if(array2d[row][col] != 0 && array2d[row][col-1] == 0) //shifts nonzero elements to the left
                {
                    leftShifted = true;
                    int newColumn = col;
                    while(newColumn >0 && array2d[row][newColumn-1] == 0)
                    {
                        newColumn--;
                    }
                    array2d[row][newColumn] = array2d[row][col];
                    array2d[row][col] = 0;
                }
            }
        }
    }

    /**
     * @return true if any elements in the board can be moved left
     * false otherwise
     */
    private boolean checkLeft()
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 1; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row][col-1] == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * If possible, copies the current array into backup before modification
     * and shifts elements to the left and adds identical elements up
     */
    public void left()
    {
        if(checkLeft())
            copy();
        moveLeft();
        for(int row = 0; row < 4; row++)
        {
            for(int col = 1; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row][col] == array2d[row][col-1]) //sum up identical elements
                {
                    array2d[row][col-1] = 2*array2d[row][col-1];
                    score += array2d[row][col-1];
                    array2d[row][col] = 0;
                    moveLeft();
                    if(!leftShifted)
                        randomSpawn(1);
                }
            }
        }
        //System.out.println(this);
    }

    /**
     * Helper method for moving right
     */
    private void moveRight()
    {
        for(int row = 0; row < 4; row++) 
        {
            for(int col = 2; col >=0; col--) //this order matters -- if shifting right, process elements on right first
            {
                if(array2d[row][col] != 0 && array2d[row][col+1] == 0) //shifts nonzero elements to the right
                {
                    rightShifted = true;
                    int newColumn = col;
                    while(newColumn < 3 && array2d[row][newColumn+1] == 0)
                    {
                        newColumn++;
                    }
                    array2d[row][newColumn] = array2d[row][col];
                    array2d[row][col] = 0;
                }
            }
        }
    }

    /**
     * @return true if any elements can be shifted right
     * false otherwise
     */
    private boolean checkRight()
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 2; col >= 0; col--)
            {
                if(array2d[row][col] != 0 && array2d[row][col+1] == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * If possible, copies the current array into backup before modification
     * and shifts all elements to the right and adds identical elements
     */
    public void right()
    {
        if(checkRight())
            copy();
        moveRight();
        for(int row = 0; row < 4; row++)
        {
            for(int col = 2; col >= 0; col--)
            {
                if(array2d[row][col] != 0 && array2d[row][col] == array2d[row][col+1]) //sum up identical elements
                {
                    array2d[row][col+1] = 2*array2d[row][col+1];
                    score += array2d[row][col+1];
                    array2d[row][col] = 0;
                    moveRight();
                    if(!rightShifted)
                        randomSpawn(1);
                }
            }
        }
    }

    /**
     * Helper method for moving up
     */
    private void moveUp()
    {
        for(int row = 1; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row-1][col] == 0) //shifts nonzero elements to the top
                {
                    upShifted = true;
                    int newRow = row;
                    while(newRow > 0 && array2d[newRow-1][col] == 0)
                    {
                        newRow--;
                    }
                    array2d[newRow][col] = array2d[row][col];
                    array2d[row][col] = 0;
                }
            }
        }
    }

    /**
     * @return true if at least one element can be moved up
     * false otherwise
     */
    private boolean checkUp()
    {
        for(int row = 1; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row-1][col] == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * If possible, copies the array into backup before modification
     * and moves all elements up and adds identical elements
     */
    public void up()
    {
        if(checkUp())
            copy();
        moveUp();
        for(int row = 1; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row][col] == array2d[row-1][col]) //sums up identical elements
                {
                    array2d[row-1][col] = 2*array2d[row-1][col];
                    score += array2d[row-1][col];
                    array2d[row][col] = 0;
                    moveUp();
                    if(!upShifted)
                        randomSpawn(1);
                }
            }
        }
    }

    /**
     * Helper method for moving down
     */
    private void moveDown()
    {
        for(int row = 2; row >= 0; row--)
        {
            for(int col = 0; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row+1][col] == 0) //shifts nonzero elements to the bottom
                {
                    downShifted = true;
                    int newRow = row;
                    while(newRow < 3 && array2d[newRow+1][col] == 0)
                    {
                        newRow++;
                    }
                    array2d[newRow][col] = array2d[row][col];
                    array2d[row][col] = 0;
                }
            }
        }
    }

    /**
     * @return true if at least one element can be moved down
     * false otherwise
     */
    private boolean checkDown()
    {
        for(int row = 2; row >= 0; row--)
        {
            for(int col = 0; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row+1][col] == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * If possible, copies gameboard into backup before modification
     * and moves all elements down and adds identical elements
     */
    public void down()
    {
        if(checkDown())
            copy();
        moveDown();
        for(int row = 2; row >= 0; row--)
        {
            for(int col = 0; col < 4; col++)
            {
                if(array2d[row][col] != 0 && array2d[row][col] == array2d[row+1][col]) //sums up identical elements
                {
                    array2d[row+1][col] = 2*array2d[row+1][col];
                    score += array2d[row+1][col];
                    array2d[row][col] = 0;
                    moveDown();
                    if(!downShifted)
                        randomSpawn(1);
                }
            }
        }
        //System.out.println(this);
    }

    /**
     * @return the String representation of the gameboard
     */
    public String toString()
    {
        String output = "";
        for(int[] rowArray: array2d)
        {
            for(int i: rowArray)
            {
                if(i == 0)
                {
                    output += ".\t";
                }
                else
                {
                    output += i+"\t";
                }
            }
            output += "\n";
        }
        return output;
    }

    /** Getter method for 2D array */
    public int[][] getGameBoard()
    {
        return array2d;
    }

    /**
     * @return true if at least one tile on the gameboard is 2048
     * false otherwise
     */
    public boolean playerWin()
    {
        for(int[] rowArray: array2d)
        {
            for(int i: rowArray)
            {
                if(i == 2048)
                    return true;
            }
        }
        return false;
    }

    /**
     * @return true if the board is full
     * and the player can't make a horizontal or vertical move
     * false otherwise
     */
    public boolean playerLose() 

    {
        if(FullBoard() && !horizontalMoveExists() && !verticalMoveExists())
            return true;
        return false;
    }

    /**
     * @return true if the gameboard is full, false otherwise
     */
    private boolean FullBoard()
    {
        for(int[] rowArray: array2d)
        {
            for(int i: rowArray)
            {
                if(i == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * @return true if any horizontal element can be added up, false otherwise
     */
    private boolean horizontalMoveExists() 
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 1; col < 3; col++)
            {

                boolean existsLeft = array2d[row][col] == array2d[row][col-1];
                boolean existsRight = array2d[row][col] == array2d[row][col+1];
                if(existsLeft || existsRight)
                    return true;

            }
        }
        return false;
    }

    /**
     * @return true if any vertical element can be added up, false otherwise
     */
    private boolean verticalMoveExists() 
    {
        for(int row = 1; row < 3; row++)
        {
            for(int col = 0; col < 4; col++)
            {

                boolean existsTop = array2d[row][col] == array2d[row-1][col];
                boolean existsBottom = array2d[row][col] == array2d[row+1][col];
                if(existsTop || existsBottom)
                    return true;

            }
        }
        return false;
    }
}
