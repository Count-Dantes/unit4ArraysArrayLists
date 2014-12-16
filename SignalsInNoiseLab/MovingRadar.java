
/**
 * The model for radar scan and accumulator
 * 
 * @author @gcschmit
 * @version 19 July 2014
 */
public class MovingRadar
{
    
    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;
    
    // value of each cell is incremented for each scan in which that cell triggers detection 
    private int[][] accumulator;
    private boolean[][] lastScan;
    
    private int[][] dydx = new int[11][11];
    
    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;
    
    // number of scans of the radar since construction
    private int numScans;
    
    private int dy;
    private int dx;

    /**
     * Constructor for objects of class Radar
     * 
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     */
    public MovingRadar(
        int rows, int cols,
        int DY, int DX,
        int startCol, int startRow)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        accumulator = new int[rows][cols]; // elements will be set to 0
        lastScan = new boolean[rows][cols];
        // randomly set the location of the monster (can be explicity set through the
        //  setMonsterLocation method
        monsterLocationRow = startCol;
        monsterLocationCol = startRow;
        
        dy = DY;
        dx = DX;
        
        noiseFraction = 0.05;
        numScans= 0;
    }
    
    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        // sets last scan to be the current scan
        for(int row = 0; row < currentScan.length-5; row++)
        {
            for (int col = 0; col < currentScan[0].length-5; col++)
            {
                lastScan[row][col] = currentScan[row][col];
            }
        }
        // zero the current scan grid
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                currentScan[row][col] = false;
            }
        }
        //update monster location
        
        updateMonsterLocation(dy,dx);
        
        // detect the monster
        if ( (monsterLocationRow < currentScan.length) && 
             (monsterLocationCol < currentScan[0].length)&&
             (monsterLocationRow > 5)&&
             (monsterLocationCol > 5)
           )
        {
            currentScan[monsterLocationRow][monsterLocationCol] = true;
        }
        else
        {
            return;
        }
        // inject noise into the grid
        injectNoise();
        
        // udpate the dydx array
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(lastScan[row][col] == true)
                {
                    for (int testDx = -5; testDx <= 5; testDx++)
                    {
                        for (int testDy = -5; testDy <=5; testDy++)
                        {
                            if (currentScan[row+testDx][col+testDy] == true)
                            {
                                dydx[testDx+5][testDy+5] +=1;
                            }  
                        }
                    }
                }
            }
        }
        
        // keep track of the total number of scans
        numScans++;
    }
    

    /**
     * Sets the location of the monster
     * 
     * @param   row     the row in which the monster is located
     * @param   col     the column in which the monster is located
     * @pre row and col must be within the bounds of the radar grid
     */
    public void setMonsterLocation(int row, int col)
    {
        // remember the row and col of the monster's location
        monsterLocationRow = row;
        monsterLocationCol = col;
        
        // update the radar grid to show that something was detected at the specified location
        currentScan[row][col] = true;
    }
    
    /**
     * Updates the location of the monster
     * 
     * @param   dy     how much the monstor will move in the y
     * @param   dx      how much the monstor will move in the x
     * @pre row and col must be within the bounds of the radar grid
     */
    public void updateMonsterLocation(int dy,int dx)
    {
        monsterLocationRow += dx;
        monsterLocationCol += dy;
    }
    
    
     /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }
    
    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean isDetected(int row, int col)
    {
        return currentScan[row][col];
    }
    
    /**
     * Returns the number of times that the specified location in the radar grid has triggered a
     *  detection since the constructor of the radar object.
     * 
     * @param   row     the row of the location to query for accumulated detections
     * @param   col     the column of the location to query for accumulated detections
     * @return the number of times that the specified location in the radar grid has
     *          triggered a detection since the constructor of the radar object
     */
    public int getAccumulatedDetection(int row, int col)
    {
        return accumulator[row][col];
    }
    
    public int[] returnDyDx()
    {
        int highestValue = 0;
        int highDy = 0;
        int highDx = 0;
        for(int row = 0; row <= 10; row++)
        {
            for (int col = 0; col <= 10; col++)
            {
                if (dydx[row][col] > highestValue)
                {
                    highDy = row;
                    highDx = col;
                }
            }
        }
        int[] returnValue = new int[2];
        returnValue[0] = highDy-5;
        returnValue[1] = highDx-5;
        return returnValue;
    }
    
    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }
    
    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }
    
    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }
    
    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 5; row < currentScan.length-5; row++)
        {
            for(int col = 5; col < currentScan[0].length-5; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }
    
}
