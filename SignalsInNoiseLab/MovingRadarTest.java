

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MovingRadarTest.
 *
 * @author  aamleshi
 * @version yolo
 */
public class MovingRadarTest
{
    /**
     * Default constructor for test class MovingRadarTest
     */
    public MovingRadarTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void testStateOne()
    {
        /*
         * Creates a 100X100 grid
         * dy = 3
         * dx = -3
         * col = 25
         * row = 75
         */
        MovingRadar radar = new MovingRadar(100,100,3,-3,25,75);
        for(int i = 0; i <= 10; i ++)
        {
            radar.scan();
        }
        if ( radar.returnDyDx()[0] == 3 )
        {
            assertNotNull("expected dy =3");
        }
        if ( radar.returnDyDx()[0] == -3 )
        {
            assertNotNull("expected dx =-3");
        }
    }
    
    @Test
    public void testStateTwo()
    {
        /*
         * Creates a 100X100 grid
         * dy = -1
         * dx = 5
         * col = 100
         * row = 3
         */
        MovingRadar radar = new MovingRadar(100,100,-1,5,100,3);
        for(int i = 0; i <= 10; i ++)
        {
            radar.scan();
        }
        if ( radar.returnDyDx()[0] == -1 )
        {
            assertNotNull("expected dy =-1");
        }
        if ( radar.returnDyDx()[0] == 5 )
        {
            assertNotNull("expected dx =5");
        }
    }
        
}
