package components.flipflops;

import components.Component;
import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FlipFlop extends Component implements Serializable {
    private static final long serialVersionUID = 30000000000L;
    protected boolean risingEdge = true;
    protected boolean lastState = false;
    protected boolean signalInput = false;
    protected AtomicBoolean signalReversedOutput = new AtomicBoolean(true);
    protected boolean signalAsynchronousInput = false;
    protected boolean signalClock = false;
    protected boolean signalReset = true;
    protected ArrayList<Line> arrayListLinesInput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutputReverted = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesAsynchronousInput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesClock = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesReset = new ArrayList<>();
    protected Point pointInput;
    protected Point pointOutput;
    protected Point pointOutputReversed;
    protected Point pointAsynchronousInput;
    protected Point pointReset;
    protected Point pointClock;

    public FlipFlop(){
        super();
    }

    public FlipFlop(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);

        pointInput = new Point("Input", x - 145, y - 75);
        pointOutput = new Point("Output", x + 145, y - 75);
        pointOutputReversed = new Point("Output reversed", x + 145, y + 75);
        pointAsynchronousInput = new Point("Asynchronous input", x, y - 195);
        pointReset = new Point("Reset", x, y + 195);
        pointClock = new Point("Clock", x - 145, y);
    }

    public void draw(GraphicsContext graphicsContext) {
        if (selected) {
            graphicsContext.drawImage(imageViewSelectedOn.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        } else if (output.get()) {
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        } else {
            graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        }
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("" + id, pointCenter.getX() - 8, pointCenter.getY() + 8);
    }

    public void select(double x, double y){
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseFlipFlopXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseFlipFlopYShift);
    }

    public boolean checkIfCouldBeSelected(double x, double y){
        return (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseFlipFlopXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseFlipFlopYShift);
    }

    public void select(double x1, double y1, double x2, double y2){
        selected = pointCenter.getX() > x1 && pointCenter.getX() < x2 && pointCenter.getY() > y1 && pointCenter.getY() < y2;
    }

    public void selectForDrag(double x, double y){
        selectedForDrag = checkIfCouldBeSelected(x, y);
    }

    public boolean inside(double x, double y){
        return Math.abs(x - this.pointCenter.getX()) <= Sizes.baseFlipFlopXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseFlipFlopYShift;
    }

    public void move(double x, double y, double mousePressX, double mousePressY, boolean fitToCheck) {
        if(fitToCheck) {
            double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double fitXValue = x - x % Sizes.fitComponentPlace + x1;
            double fitYValue = y - y % Sizes.fitComponentPlace + y1;

            pointCenter.setX(fitXValue);
            pointCenter.setY(fitYValue);

            pointInput.setX(fitXValue - 145);
            pointInput.setY(fitYValue - 75);

            pointOutput.setX(fitXValue + 145);
            pointOutput.setY(fitYValue - 75);

            pointOutputReversed.setX(fitXValue + 145);
            pointOutputReversed.setY(fitYValue + 75);

            pointAsynchronousInput.setX(fitXValue);
            pointAsynchronousInput.setY(fitYValue - 195);

            pointReset.setX(fitXValue);
            pointReset.setY(fitYValue + 195);

            pointClock.setX(fitXValue - 145);
            pointClock.setY(fitYValue);

            for (Line l : arrayListLinesInput) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue - 145);
                    l.setY1(fitYValue - 75);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue - 145);
                    l.setY2(fitYValue - 75);
                }
            }

            for (Line l : arrayListLinesOutput) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue + 145);
                    l.setY1(fitYValue - 75);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue + 145);
                    l.setY2(fitYValue - 75);
                }
            }

            for (Line l : arrayListLinesOutputReverted) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue + 145);
                    l.setY1(fitYValue + 75);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue + 145);
                    l.setY2(fitYValue + 75);
                }
            }

            for (Line l : arrayListLinesAsynchronousInput) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue);
                    l.setY1(fitYValue - 195);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue);
                    l.setY2(fitYValue - 195);
                }
            }

            for (Line l : arrayListLinesClock) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue - 145);
                    l.setY1(fitYValue);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue - 145);
                    l.setY2(fitYValue);
                }
            }

            for (Line l : arrayListLinesReset) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue);
                    l.setY1(fitYValue + 195);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue);
                    l.setY2(fitYValue + 195);
                }
            }
        }
        else {
            pointCenter.setX(pointCenter.getX() + x - mousePressX);
            pointCenter.setY(pointCenter.getY() + y - mousePressY);

            pointInput.setX(pointInput.getX() + x - mousePressX);
            pointInput.setY(pointInput.getY() + y - mousePressY);

            pointOutput.setX(pointOutput.getX() + x - mousePressX);
            pointOutput.setY(pointOutput.getY() + y - mousePressY);

            pointOutputReversed.setX(pointOutputReversed.getX() + x - mousePressX);
            pointOutputReversed.setY(pointOutputReversed.getY() + y - mousePressY);

            pointAsynchronousInput.setX(pointAsynchronousInput.getX() + x - mousePressX);
            pointAsynchronousInput.setY(pointAsynchronousInput.getY() + y - mousePressY);

            pointReset.setX(pointReset.getX() + x - mousePressX);
            pointReset.setY(pointReset.getY() + y - mousePressY);

            pointClock.setX(pointClock.getX() + x - mousePressX);
            pointClock.setY(pointClock.getY() + y - mousePressY);

            for (Line l : arrayListLinesInput) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointInput.getX() + x - mousePressX);
                    l.setY1(pointInput.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointInput.getX() + x - mousePressX);
                    l.setY2(pointInput.getY() + y - mousePressY);
                }
            }

            for (Line l : arrayListLinesOutput) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointOutput.getX() + x - mousePressX);
                    l.setY1(pointOutput.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointOutput.getX() + x - mousePressX);
                    l.setY2(pointOutput.getY() + y - mousePressY);
                }
            }

            for (Line l : arrayListLinesOutputReverted) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointOutputReversed.getX() + x - mousePressX);
                    l.setY1(pointOutputReversed.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointOutputReversed.getX() + x - mousePressX);
                    l.setY2(pointOutputReversed.getY() + y - mousePressY);
                }
            }

            for (Line l : arrayListLinesAsynchronousInput) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointAsynchronousInput.getX() + x - mousePressX);
                    l.setY1(pointAsynchronousInput.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointAsynchronousInput.getX() + x - mousePressX);
                    l.setY2(pointAsynchronousInput.getY() + y - mousePressY);
                }
            }

            for (Line l : arrayListLinesClock) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointClock.getX() + x - mousePressX);
                    l.setY1(pointClock.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointClock.getX() + x - mousePressX);
                    l.setY2(pointClock.getY() + y - mousePressY);
                }
            }

            for (Line l : arrayListLinesReset) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointReset.getX() + x - mousePressX);
                    l.setY1(pointReset.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointReset.getX() + x - mousePressX);
                    l.setY2(pointReset.getY() + y - mousePressY);
                }
            }
        }
    }


    public boolean isSignalInput() {
        return signalInput;
    }

    public void setSignalInput(boolean signalInput) {
        this.signalInput = signalInput;
    }

    public boolean isSignalOutput() {
        return output.get();
    }

    public void setOutput(boolean output) {
        this.output.set(output);
    }

    public boolean isSignalReversedOutput() {
        return signalReversedOutput.get();
    }

    public void setSignalReversedOutput(boolean signalReversedOutput) {
        this.signalReversedOutput.set(signalReversedOutput);
    }

    public boolean isSignalAsynchronousInput() {
        return signalAsynchronousInput;
    }

    public void setSignalAsynchronousInput(boolean signalAsynchronousInput) {
        this.signalAsynchronousInput = signalAsynchronousInput;
    }

    public boolean isSignalClock() {
        return signalClock;
    }

    public void setSignalClock(boolean signalClock) {
        this.signalClock = signalClock;
    }

    public boolean isSignalReset() {
        return signalReset;
    }

    public void setSignalReset(boolean signalReset) {
        this.signalReset = signalReset;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectedForDrag() {
        return selectedForDrag;
    }

    public void setSelectedForDrag(boolean selectedForDrag) {
        this.selectedForDrag = selectedForDrag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Line> getArrayListLinesInput() {
        return arrayListLinesInput;
    }

    public ArrayList<Line> getArrayListLinesOutput() {
        return arrayListLinesOutput;
    }

    public ArrayList<Line> getArrayListLinesOutputReverted() {
        return arrayListLinesOutputReverted;
    }

    public ArrayList<Line> getArrayListLinesAsynchronousInput() {
        return arrayListLinesAsynchronousInput;
    }

    public ArrayList<Line> getArrayListLinesClock() {
        return arrayListLinesClock;
    }

    public ArrayList<Line> getArrayListLinesReset() {
        return arrayListLinesReset;
    }

    public Point getPointCenter() {
        return pointCenter;
    }

    public Point getPointInput() {
        return pointInput;
    }

    public Point getPointOutput() {
        return pointOutput;
    }

    public Point getPointOutputReversed() {
        return pointOutputReversed;
    }

    public Point getPointAsynchronousInput() {
        return pointAsynchronousInput;
    }

    public Point getPointReset() {
        return pointReset;
    }

    public Point getPointClock() {
        return pointClock;
    }
}
