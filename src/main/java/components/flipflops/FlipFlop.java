package components.flipflops;

import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class FlipFlop {
    protected boolean selected = false;
    protected boolean selectedForDrag = false;
    protected boolean state;
    protected boolean clock = false;
    protected boolean reset = false;
    protected ArrayList<Line> arrayListLinesInput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutput = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesOutputReverted = new ArrayList<>();
    protected ArrayList<Line> arrayListLinesClock = new ArrayList<>();
    protected ArrayList<Line> arrayListLineReset = new ArrayList<>();
    protected Point pointCenter;
    protected Point pointInput;
    protected Point pointOutput;
    protected Point pointOutputReversed;
    protected Point pointReset;
    protected Point pointClock;
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;

    public FlipFlop(double x, double y){
        pointCenter = new Point("Center", x, y);
        pointInput = new Point("Input", x, y);
        pointOutput = new Point("Output", x, y);
        pointOutputReversed = new Point("Output reversed", x, y);
        pointReset = new Point("Reset", x, y);
        pointClock = new Point("Clock", x, y);
    }

    public void draw(GraphicsContext graphicsContext) {
        if (selected) {
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseFlipFlopXShift, pointCenter.getY() - Sizes.baseFlipFlopYShift);
        } else if (state) {
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

        pointInput.setX(pointInput.getX() + x - mousePressX);
        pointInput.setY(pointInput.getY() + y - mousePressY);
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

    public ArrayList<Line> getArrayListLineReset() {
        return arrayListLineReset;
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
