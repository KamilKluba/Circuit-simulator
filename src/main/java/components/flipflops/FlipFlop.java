package components.flipflops;

import components.Component;
import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class FlipFlop extends Component {
    protected boolean signalInput = false;
    protected boolean signalOutput = false;
    protected boolean signalClock = false;
    protected boolean signalReset = false;
    protected ArrayList<Line> arrayListLinesInput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutputReverted = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesClock = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesReset = new ArrayList<>();
    protected Point pointInput;
    protected Point pointOutput;
    protected Point pointOutputReversed;
    protected Point pointReset;
    protected Point pointClock;
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;

    public FlipFlop(double x, double y){
        super(x, y);
        pointInput = new Point("Input", x - 145, y - 75);
        pointOutput = new Point("Output", x + 145, y - 75);
        pointOutputReversed = new Point("Output reversed", x + 145, y + 75);
        pointReset = new Point("Reset", x, y - 195);
        pointClock = new Point("Clock", x, y + 195);
    }

    public void draw(GraphicsContext graphicsContext) {
        if (selected) {
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        } else if (signalOutput) {
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        } else {
            graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        }
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
        selectedForDrag = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseFlipFlopXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseFlipFlopYShift);
    }

    public boolean inside(double x, double y){
        return Math.abs(x - this.pointCenter.getX()) <= Sizes.baseFlipFlopXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseFlipFlopYShift;
    }

    public void move(double x, double y, double mousePressX, double mousePressY) {
        pointCenter.setX(pointCenter.getX() + x - mousePressX);
        pointCenter.setY(pointCenter.getY() + y - mousePressY);

        pointInput.setX(pointInput.getX() + x - mousePressX);
        pointInput.setY(pointInput.getY() + y - mousePressY);

        pointOutput.setX(pointOutput.getX() + x - mousePressX);
        pointOutput.setY(pointOutput.getY() + y - mousePressY);

        pointOutputReversed.setX(pointOutputReversed.getX() + x - mousePressX);
        pointOutputReversed.setY(pointOutputReversed.getY() + y - mousePressY);

        pointReset.setX(pointReset.getX() + x - mousePressX);
        pointReset.setY(pointReset.getY() + y - mousePressY);

        pointClock.setX(pointClock.getX() + x - mousePressX);
        pointClock.setY(pointClock.getY() + y - mousePressY);

        for(Line l : arrayListLinesInput){
            if(l.getFlipFlop1() != null && l.getFlipFlop1().equals(this)){
                l.setX1(pointInput.getX() + x - mousePressX);
                l.setY1(pointInput.getY() + y - mousePressY);
            }
            else if(l.getFlipFlop2() != null && l.getFlipFlop2().equals(this)){
                l.setX2(pointInput.getX() + x - mousePressX);
                l.setY2(pointInput.getY() + y - mousePressY);
            }
        }

        for(Line l : arrayListLinesOutput){
            if(l.getFlipFlop1() != null && l.getFlipFlop1().equals(this)){
                l.setX1(pointOutput.getX() + x - mousePressX);
                l.setY1(pointOutput.getY() + y - mousePressY);
            }
            else if(l.getFlipFlop2() != null && l.getFlipFlop2().equals(this)){
                l.setX2(pointOutput.getX() + x - mousePressX);
                l.setY2(pointOutput.getY() + y - mousePressY);
            }
        }

        for(Line l : arrayListLinesOutputReverted){
            if(l.getFlipFlop1() != null && l.getFlipFlop1().equals(this)){
                l.setX1(pointOutputReversed.getX() + x - mousePressX);
                l.setY1(pointOutputReversed.getY() + y - mousePressY);
            }
            else if(l.getFlipFlop2() != null && l.getFlipFlop2().equals(this)){
                l.setX2(pointOutputReversed.getX() + x - mousePressX);
                l.setY2(pointOutputReversed.getY() + y - mousePressY);
            }
        }

        for(Line l : arrayListLinesClock){
            if(l.getFlipFlop1() != null && l.getFlipFlop1().equals(this)){
                l.setX1(pointClock.getX() + x - mousePressX);
                l.setY1(pointClock.getY() + y - mousePressY);
            }
            else if(l.getFlipFlop2() != null && l.getFlipFlop2().equals(this)){
                l.setX2(pointClock.getX() + x - mousePressX);
                l.setY2(pointClock.getY() + y - mousePressY);
            }
        }

        for(Line l : arrayListLinesReset){
            if(l.getFlipFlop1() != null && l.getFlipFlop1().equals(this)){
                l.setX1(pointReset.getX() + x - mousePressX);
                l.setY1(pointReset.getY() + y - mousePressY);
            }
            else if(l.getFlipFlop2() != null && l.getFlipFlop2().equals(this)){
                l.setX2(pointReset.getX() + x - mousePressX);
                l.setY2(pointReset.getY() + y - mousePressY);
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
        return signalOutput;
    }

    public void setSignalOutput(boolean signalOutput) {
        this.signalOutput = signalOutput;
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

    public Point getPointReset() {
        return pointReset;
    }

    public Point getPointClock() {
        return pointClock;
    }
}