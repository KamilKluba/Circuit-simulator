package components;

import components.gates.and.And2;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComponentTest {

    @Test
    public void testCheckIfCouldBeSelected() {
        And2 and2 = new And2(500, 500, false, null, null);

        //test inside gate
        assertTrue(and2.checkIfCouldBeSelected(520, 520));

        //test outside gate
        assertFalse(and2.checkIfCouldBeSelected(570, 570));

        //tests edges
        assertTrue(and2.checkIfCouldBeSelected(451, 451));
        assertTrue(and2.checkIfCouldBeSelected(450, 450));
        assertFalse(and2.checkIfCouldBeSelected(449, 449));
    }

    @Test
    public void testMove() {
        And2 and2 = new And2(500, 500, false, null, null);

        //move right-down
        and2.move(1100, 1100, 1000, 1000);
        assertEquals(and2.getPointCenter().getX(), 600, 0);
        assertEquals(and2.getPointCenter().getY(), 600, 0);

        //move left-up
        and2.move(950, 950, 1200, 1200);
        assertEquals(and2.getPointCenter().getX(), 350, 0);
        assertEquals(and2.getPointCenter().getY(), 350, 0);
    }

    @Test
    public void testKill(){
        And2 and2 = new And2(500, 500, true, null, null);
        and2.kill();
        assertFalse(and2.isAlive());
    }
}