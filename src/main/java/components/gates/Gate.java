package components.gates;

import components.Component;
import components.Line;
import components.Point;
import data.Names;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Gate extends Component {
    protected double inputsNumber;
    protected ArrayList<Line>[] arrayArrayListLines;
    protected ArrayList<Line> arrayListLinesOutput = new ArrayList<>();
    protected boolean[] arraySignalsInputs;
    protected Color color = Color.BLACK;
    protected Point pointOutput;
    protected Point[] arrayPointsInputs;
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;

    public Gate(){
    }

    public Gate(double x, double y, boolean startLife, XYChart.Series<Integer, String> series, AtomicInteger chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
        pointCenter = new Point(Names.pointCenterName, x, y);
        pointOutput = new Point(Names.pointOutputName, x + 93, y);
    }

    public void lifeCycle(){
    }

    public void select(double x, double y) {
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift);
    }

    public void select(double x1, double y1, double x2, double y2){
        selected = pointCenter.getX() > x1 && pointCenter.getX() < x2 && pointCenter.getY() > y1 && pointCenter.getY() < y2;
    }

    public boolean checkIfCouldBeSelected(double x, double y){
        return (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift);
    }

    public void selectForDrag(double x, double y){
        selectedForDrag = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift);
    }

    public boolean inside(double x, double y){
        return Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift;
    }

    public void rotate(){
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        imageViewOff.setRotate(90);
        imageViewOff.setImage(imageViewOff.snapshot(snapshotParameters, null));
        imageViewOn.setRotate(90);
        imageViewOn.setImage(imageViewOn.snapshot(snapshotParameters, null));
        imageViewSelected.setRotate(90);
        imageViewSelected.setImage(imageViewSelected.snapshot(snapshotParameters, null));

        rotation++;
        if(rotation == 4){
            rotation = 0;
        }

        double rotatedInputX;
        double rotatedInputY;
        double rotatedOutputX;
        double rotatedOutputY;
        double nextInputsX;
        double nextInputsY;
        if(rotation == 0){
            rotatedInputX = -93;
            rotatedInputY = -30;
            rotatedOutputX = 93;
            rotatedOutputY = 0;
            nextInputsX = 0;
            nextInputsY = 1;
        }
        else if(rotation == 1){
            rotatedInputX = 30;
            rotatedInputY = -93;
            rotatedOutputX = 0;
            rotatedOutputY = 93;
            nextInputsX = -1;
            nextInputsY = 0;
        }
        else if(rotation == 2){
            rotatedInputX = 93;
            rotatedInputY = 30;
            rotatedOutputX = -93;
            rotatedOutputY = 0;
            nextInputsX = 0;
            nextInputsY = -1;
        }
        else{
            rotatedInputX = -30;
            rotatedInputY = 93;
            rotatedOutputX = 0;
            rotatedOutputY = -93;
            nextInputsX = 1;
            nextInputsY = 0;
        }

        if(arrayArrayListLines.length > 1) {
            for (int i = 0; i < arrayArrayListLines.length; i++) {
                ArrayList<Line> al = arrayArrayListLines[i];
                if (arrayPointsInputs[i] != null) {
                    arrayPointsInputs[i].setX(pointCenter.getX() + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                    arrayPointsInputs[i].setY(pointCenter.getY() + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
                }
                for (Line l : al) {
                    if (l != null) {
                        if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                            l.setX1(pointCenter.getX() + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                            l.setY1(pointCenter.getY() + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
                        } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                            l.setX2(pointCenter.getX() + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                            l.setY2(pointCenter.getY() + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
                        }
                    }
                }
            }
        }
        else{
            for(Line l : arrayArrayListLines[0]){
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointCenter.getX() + rotatedInputX * ((rotation + 1) % 2));
                    l.setY1(pointCenter.getY() + rotatedInputY * ((rotation) % 2));
                }
                else if(l.getComponent2() != null && l.getComponent2().equals(this)){
                    l.setX2(pointCenter.getX() + rotatedInputX * ((rotation + 1) % 2));
                    l.setY2(pointCenter.getY() + rotatedInputY * ((rotation) % 2));
                }
            }
        }
        pointOutput.setX(pointCenter.getX() + rotatedOutputX);
        pointOutput.setY(pointCenter.getY() + rotatedOutputY);
        for (Line l : arrayListLinesOutput){
            if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                l.setX1(pointCenter.getX() + rotatedOutputX);
                l.setY1(pointCenter.getY() + rotatedOutputY);
            }
            else if(l.getComponent2() != null && l.getComponent2().equals(this)){
                l.setX2(pointCenter.getX() + rotatedOutputX);
                l.setY2(pointCenter.getY() + rotatedOutputY);
            }
        }
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        //THERE IS A BUG HERE PROBABLY, CHECK IT LATER///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if(output.get()){
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        else {
            graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
    }

    public void move(double x, double y, double mousePressX, double mousePressY){
        pointCenter.setX(pointCenter.getX() + x - mousePressX);
        pointCenter.setY(pointCenter.getY() + y - mousePressY);

        for(ArrayList<Line> al : arrayArrayListLines) {
            for (Line l : al) {
                changePointCoordinates(l, x, y, mousePressX, mousePressY);
            }
        }

        for(Point p : arrayPointsInputs){
            p.setX(p.getX() + x - mousePressX);
            p.setY(p.getY() + y - mousePressY);
        }

        for(Line l : arrayListLinesOutput) {
            changePointCoordinates(l, x, y, mousePressX, mousePressY);
        }

        pointOutput.setX(pointOutput.getX() + x - mousePressX);
        pointOutput.setY(pointOutput.getY() + y - mousePressY);
    }

    private void changePointCoordinates(Line l, double x, double y, double mousePressX, double mousePressY){
        if(l != null) {
            if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                l.setX1(l.getX1() + x - mousePressX);
                l.setY1(l.getY1() + y - mousePressY);
            }
            else if(l.getComponent2() != null && l.getComponent2().equals(this)) {
                l.setX2(l.getX2() + x - mousePressX);
                l.setY2(l.getY2() + y - mousePressY);
            }
        }
    }

    public void setOutput(boolean output){
        this.output.set(output);
    }

    public boolean isSignalOutput() {return output.get();}

    public ArrayList<Line>[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }

    public ArrayList<Line> getArrayListLinesOutput() {
        return arrayListLinesOutput;
    }

    public boolean[] getArraySignalsInputs() {
        return arraySignalsInputs;
    }

    public void setArraySignalsInputs(boolean[] arraySignalsInputs) {
        this.arraySignalsInputs = arraySignalsInputs;
    }

    public ImageView getImageViewOff(){
        return imageViewOff;
    }

    public ImageView getImageViewOn() {
        return imageViewOn;
    }

    public ImageView getImageViewSelected() {
        return imageViewSelected;
    }

    public String getName(){
        return name;
    }

    public Point getPointCenter() {
        return pointCenter;
    }

    public Point getPointOutput() {
        return pointOutput;
    }

    public Point[] getArrayPointsInputs() {
        return arrayPointsInputs;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelectedForDrag(boolean selectedForDrag) {
        this.selectedForDrag = selectedForDrag;
    }

    public boolean isSelectedForDrag() {return selectedForDrag;}


}
