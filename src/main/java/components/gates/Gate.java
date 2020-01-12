package components.gates;

import components.Component;
import components.Line;
import components.Point;
import data.Names;
import data.SerializableColor;
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
    private static final long serialVersionUID = 10000000000L;
    protected double inputsNumber;
    protected ArrayList<Line>[] arrayArrayListLines;
    protected ArrayList<Line> arrayListLinesOutput = new ArrayList<>();
    protected boolean[] arraySignalsInputs;
    protected Point pointOutput;
    protected Point[] arrayPointsInputs;

    private double rotatedInputX = -93;
    private double rotatedInputY = -30;
    private double rotatedOutputX = 93;
    private double rotatedOutputY = 0;
    private double nextInputsX = 0;
    private double nextInputsY = 1;

    public Gate(){
    }

    public Gate(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
        pointCenter = new Point(Names.pointCenterName, x, y);
        pointOutput = new Point(Names.pointOutputName, x + 93, y);
    }

    public void lifeCycle(){
    }

    public void select(double x, double y) {
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateSelect && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateSelect);
    }

    public void select(double x1, double y1, double x2, double y2){
        selected = pointCenter.getX() > x1 && pointCenter.getX() < x2 && pointCenter.getY() > y1 && pointCenter.getY() < y2;
    }

    public boolean checkIfCouldBeSelected(double x, double y){
        return (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateSelect && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateSelect);
    }

    public void selectForDrag(double x, double y){
        selectedForDrag = checkIfCouldBeSelected(x, y);
    }

    public void rotate(){
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        imageViewOff.setRotate(90);
        imageViewOff.setImage(imageViewOff.snapshot(snapshotParameters, null));
        imageViewOn.setRotate(90);
        imageViewOn.setImage(imageViewOn.snapshot(snapshotParameters, null));
        imageViewSelectedOn.setRotate(90);
        imageViewSelectedOn.setImage(imageViewSelectedOn.snapshot(snapshotParameters, null));

        rotation++;
        if(rotation == 4){
            rotation = 0;
        }

        movePoints();

        stateChanged.set(true);
    }

    public void movePoints(){
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
            graphicsContext.drawImage(imageViewSelectedOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        //THERE IS A BUG HERE PROBABLY, CHECK IT LATER///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if(output.get()){
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        else {
            graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("" + id, pointCenter.getX() - 8, pointCenter.getY() + 8);
    }

    public void move(double x, double y, double mousePressX, double mousePressY, boolean fitToCheck){
        if(fitToCheck) {
            double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double fitXValue = x - x % Sizes.fitComponentPlace + x1;
            double fitYValue = y - y % Sizes.fitComponentPlace + y1;

            pointCenter.setX(fitXValue);
            pointCenter.setY(fitYValue);

            pointOutput.setX(fitXValue + rotatedOutputX);
            pointOutput.setY(fitYValue + rotatedOutputY);

            for(int i = 0; i < arrayArrayListLines.length; i++){
                arrayPointsInputs[i].setX(fitXValue + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                arrayPointsInputs[i].setY(fitYValue + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
            }
        }
        else{
            pointCenter.setX(pointCenter.getX() + x - mousePressX);
            pointCenter.setY(pointCenter.getY() + y - mousePressY);

            pointOutput.setX(pointOutput.getX() + x - mousePressX);
            pointOutput.setY(pointOutput.getY() + y - mousePressY);

            for(Point p : arrayPointsInputs){
                p.setX(p.getX() + x - mousePressX);
                p.setY(p.getY() + y - mousePressY);
            }
        }

        for(int i = 0; i < arrayArrayListLines.length; i++) {
            for (Line l : arrayArrayListLines[i]) {
                changePointCoordinates(l, x, y, mousePressX, mousePressY, fitToCheck, arrayArrayListLines.length, i);
            }
        }
        for(Line l : arrayListLinesOutput) {
            changePointOutputCoordinates(l, x, y, mousePressX, mousePressY, fitToCheck);
        }
    }

    private void changePointCoordinates(Line l, double x, double y, double mousePressX, double mousePressY, boolean fitToCheck, int listsAmount, int listNumber){
        if(fitToCheck){
            double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double fitXValue = x - x % Sizes.fitComponentPlace + x1;
            double fitYValue = y - y % Sizes.fitComponentPlace + y1;

            if(listsAmount > 1) {
                if (l != null) {
                    if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                        l.setX1(fitXValue + rotatedInputX + nextInputsX * 60 * (double) listNumber / (inputsNumber - 1));
                        l.setY1(fitYValue + rotatedInputY + nextInputsY * 60 * (double) listNumber / (inputsNumber - 1));
                    } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                        l.setX2(fitXValue + rotatedInputX + nextInputsX * 60 * (double) listNumber / (inputsNumber - 1));
                        l.setY2(fitYValue + rotatedInputY + nextInputsY * 60 * (double) listNumber / (inputsNumber - 1));
                    }
                }
            }
            else {
                for (Line l1 : arrayArrayListLines[0]) {
                    if (l1.getComponent1() != null && l1.getComponent1().equals(this)) {
                        l1.setX1(fitXValue + rotatedInputX * ((rotation + 1) % 2));
                        l1.setY1(fitYValue + rotatedInputY * ((rotation) % 2));
                    } else if (l1.getComponent2() != null && l1.getComponent2().equals(this)) {
                        l1.setX2(fitXValue + rotatedInputX * ((rotation + 1) % 2));
                        l1.setY2(fitYValue + rotatedInputY * ((rotation) % 2));

                    }
                }
            }
        }
        else {
            if (l != null) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(l.getX1() + x - mousePressX);
                    l.setY1(l.getY1() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(l.getX2() + x - mousePressX);
                    l.setY2(l.getY2() + y - mousePressY);
                }
            }
        }
    }

    private void changePointOutputCoordinates(Line l, double x, double y, double mousePressX, double mousePressY, boolean fitToCheck){
        if(fitToCheck){
            double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double fitXValue = x - x % Sizes.fitComponentPlace + x1;
            double fitYValue = y - y % Sizes.fitComponentPlace + y1;

            if (l != null) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue + rotatedOutputX);
                    l.setY1(fitYValue + rotatedOutputY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue + rotatedOutputX);
                    l.setY2(fitYValue + rotatedOutputY);
                }
            }
        }
        else {
            if (l != null) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(l.getX1() + x - mousePressX);
                    l.setY1(l.getY1() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(l.getX2() + x - mousePressX);
                    l.setY2(l.getY2() + y - mousePressY);
                }
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
        return imageViewSelectedOn;
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
