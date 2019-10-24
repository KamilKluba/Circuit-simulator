package components.gates;

import components.Line;
import components.Point;
import data.Names;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Gate {
    protected double inputsNumber;
    protected String name;
    protected Line[] arrayLines;
    protected Line lineOutput;
    protected boolean selected = false;
    protected Color color = Color.BLACK;
    protected Point pointCenter;
    protected Point pointOutput;
    protected Point[] arrayPointsInputs;
    protected int rotation = 0; //0 - right, 1 - down, 2 - left, 3 - up
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;



    public Gate(){
    }

    public Gate(double x, double y){
        pointCenter = new Point(Names.pointCenterName, x, y);
        pointOutput = new Point(Names.pointOutputName, x + 93, y);
    }

    public void setOutput(){

    }

    public Line[] getArrayLines() {
        return arrayLines;
    }

    public Integer getArrayListInputsSize(){
        return 2;
    }

    public void setLineOutput(Line lineOutput) {
        this.lineOutput = lineOutput;
    }

    public Line getLineOutput() {
        return lineOutput;
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

    public void select(double x, double y) {
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift);
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

        for (int i = 0; i < arrayLines.length; i++) {
            Line l = arrayLines[i];
            if(arrayPointsInputs[i] != null) {
                arrayPointsInputs[i].setX(pointCenter.getX() + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                arrayPointsInputs[i].setY(pointCenter.getY() + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
            }
            if(l != null) {
                if (l.getGate1() != null && l.getGate1().equals(this)) {
                    l.setX1(pointCenter.getX() + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                    l.setY1(pointCenter.getY() + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
                }
                else if(l.getGate2() != null && l.getGate2().equals(this)){
                    l.setX2(pointCenter.getX() + rotatedInputX + nextInputsX * 60 * (double) i / (inputsNumber - 1));
                    l.setY2(pointCenter.getY() + rotatedInputY + nextInputsY * 60 * (double) i / (inputsNumber - 1));
                }
            }
        }
        pointOutput.setX(pointCenter.getX() + rotatedOutputX);
        pointOutput.setY(pointCenter.getY() + rotatedOutputY);
        if(lineOutput != null){
            if (lineOutput.getGate1() != null && lineOutput.getGate1().equals(this)) {
                lineOutput.setX1(pointCenter.getX() + rotatedOutputX);
                lineOutput.setY1(pointCenter.getY() + rotatedOutputY);
            }
            else if(lineOutput.getGate2() != null && lineOutput.getGate2().equals(this)){
                lineOutput.setX2(pointCenter.getX() + rotatedOutputX);
                lineOutput.setY2(pointCenter.getY() + rotatedOutputY);
            }
        }
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        //THERE IS A BUG HERE PROBABLY, CHECK IT LATER///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if(lineOutput != null && lineOutput.isSignal()){
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        else {
            graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
    }

    public void move(double x, double y, double mousePressX, double mousePressY){
        pointCenter.setX(pointCenter.getX() + x - mousePressX);
        pointCenter.setY(pointCenter.getY() + y - mousePressY);

        for(Line l : arrayLines){
            changePointCoordinates(l, x, y, mousePressX, mousePressY);
        }

        for(Point p : arrayPointsInputs){
            p.setX(p.getX() + x - mousePressX);
            p.setY(p.getY() + y - mousePressY);
        }

        changePointCoordinates(lineOutput, x, y, mousePressX, mousePressY);

        pointOutput.setX(pointOutput.getX() + x - mousePressX);
        pointOutput.setY(pointOutput.getY() + y - mousePressY);
    }

    private void changePointCoordinates(Line l, double x, double y, double mousePressX, double mousePressY){
        if(l != null) {
            if (l.getGate1() != null && l.getGate1().equals(this)) {
                l.setX1(l.getX1() + x - mousePressX);
                l.setY1(l.getY1() + y - mousePressY);
            }
            else if(l.getGate2() != null && l.getGate2().equals(this)) {
                l.setX2(l.getX2() + x - mousePressX);
                l.setY2(l.getY2() + y - mousePressY);
            }
        }
    }
}
