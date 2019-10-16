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
    protected ArrayList<Boolean> arrayListInputs = new ArrayList<>();
    protected ArrayList<Line> arrayListLines = new ArrayList();
    protected boolean output = false;
    protected boolean selected = false;
    protected Color color = Color.BLACK;
    protected Point pointCenter;
    protected Point pointOutput;
    protected ArrayList<Point> arrayListPointsInputs = new ArrayList<>();
    protected int rotation = 0; //0 - right, 1 - down, 2 - left, 3 - up
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;



    public Gate(){}

    public Gate(double x, double y){
        pointCenter = new Point(x, y);

        pointOutput = new Point(x + 100, y);
        arrayListPointsInputs.add(new Point(x - 100, y - 30));
        arrayListPointsInputs.add(new Point(x - 100, y + 30));
    }

    public void setOutput(){

    }

    public ArrayList<Boolean> getArrayListInputs() {
        return arrayListInputs;
    }

    public Integer getArrayListInputsSize(){
        return arrayListInputs.size();
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

    public ArrayList<Line> getArrayListLines() {
        return arrayListLines;
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
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            graphicsContext.drawImage(imageViewSelected.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        else if(output) {
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
        else {
            graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
        }
    }
}
