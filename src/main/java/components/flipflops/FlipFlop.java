package components.flipflops;

import components.Line;
import components.Point;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public abstract class FlipFlop {
    protected boolean state;
    protected boolean clock = false;
    protected boolean reset = false;
    protected ArrayList<Line> arrayListLinesInput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutputReverted = new ArrayList<>();
    protected Point pointCenter;
    protected Point pointInput;
    protected Point pointOutput;
    protected Point pointOutputReversed;
    protected Point pointReset;
    protected Point pointClock;

    public FlipFlop(double x, double y){
        pointCenter = new Point("Center", x, y);
        pointInput = new Point("Input", x, y);
        pointOutput = new Point("Output", x, y);
        pointOutputReversed = new Point("Output reversed", x, y);
        pointReset = new Point("Reset", x, y);
        pointClock = new Point("Clock", x, y);
    }

    public void draw(GraphicsContext graphicsContext){

    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isClock() {
        return clock;
    }

    public void setClock(boolean clock) {
        this.clock = clock;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
