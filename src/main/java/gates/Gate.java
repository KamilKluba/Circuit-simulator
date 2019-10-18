package gates;

import data.Line;
import data.Point;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Gate {
    protected String name;
    protected ArrayList<Line> arrayListLines = new ArrayList<>();
    protected Line lineOutput;
    protected boolean selected = false;
    protected Color color = Color.BLACK;
    protected Point pointCenter;
    protected Point pointOutput;
    protected ArrayList<Point> arrayListPointsInputs = new ArrayList<>();
    protected int rotation = 0; //0 - right, 1 - down, 2 - left, 3 - up
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;



    public Gate(){
    }

    public Gate(double x, double y){
        pointCenter = new Point("Center", x, y);

        pointOutput = new Point("Output", x + 100, y);
        arrayListPointsInputs.add(new Point("Input1", x - 93, y - 30));
        arrayListPointsInputs.add(new Point("Input2", x - 93, y + 30));
    }

    public void setOutput(){

    }

    public ArrayList<Line> getArrayListLines() {
        return arrayListLines;
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

    public ArrayList<Point> getArrayListPointsInputs() {
        return arrayListPointsInputs;
    }

    public boolean isSelected(){
        return selected;
    }

    public void select(double x, double y) {
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift);
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

        if(rotation == 0){
            if(arrayListLines.size() > 0) {
                arrayListLines.get(0).setX2(pointCenter.getX() - 93);
                arrayListLines.get(0).setY2(pointCenter.getY() - 30);
                if(arrayListLines.size() > 1) {
                    arrayListLines.get(1).setX2(pointCenter.getX() - 93);
                    arrayListLines.get(1).setY2(pointCenter.getY() + 30);
                }
            }
            pointOutput.setX(pointCenter.getX() + 93);
            pointOutput.setY(pointCenter.getY());
        }
        else if(rotation == 1){
            if(arrayListLines.size() > 0) {
                arrayListLines.get(0).setX2(pointCenter.getX() + 30);
                arrayListLines.get(0).setY2(pointCenter.getY() - 93);
                if (arrayListLines.size() > 1) {
                    arrayListLines.get(1).setX2(pointCenter.getX() - 30);
                    arrayListLines.get(1).setY2(pointCenter.getY() - 93);
                }
            }
            pointOutput.setX(pointCenter.getX());
            pointOutput.setY(pointCenter.getY() + 93);
        }
        else if(rotation == 2){
            if(arrayListLines.size() > 0) {
                arrayListLines.get(0).setX2(pointCenter.getX() + 93);
                arrayListLines.get(0).setY2(pointCenter.getY() + 30);
                if (arrayListLines.size() > 1) {
                    arrayListLines.get(1).setX2(pointCenter.getX() + 93);
                    arrayListLines.get(1).setY2(pointCenter.getY() - 30);
                }
            }
            pointOutput.setX(pointCenter.getX() - 93);
            pointOutput.setY(pointCenter.getY());
        }
        else if(rotation == 3){
            if (arrayListLines.size() > 0) {
                arrayListLines.get(0).setX2(pointCenter.getX() - 30);
                arrayListLines.get(0).setY2(pointCenter.getY() + 93);
                if (arrayListLines.size() > 1) {
                    arrayListLines.get(1).setX2(pointCenter.getX() + 30);
                    arrayListLines.get(1).setY2(pointCenter.getY() + 93);
                }
            }
            pointOutput.setX(pointCenter.getX());
            pointOutput.setY(pointCenter.getY() - 93);
        }
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
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

        pointOutput.setX(pointOutput.getX() + x - mousePressX);
        pointOutput.setY(pointOutput.getY() + y - mousePressY);

        for(Line l : arrayListLines){
            l.setX2(l.getX2() + x - mousePressX);
            l.setY2(l.getY2() + y - mousePressY);
        }

        for(Point p : arrayListPointsInputs){
            p.setX(p.getX() + x - mousePressX);
            p.setY(p.getY() + y - mousePressY);
        }
    }
}
