package gates;

import data.Line;
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
    protected double x;
    protected double y;
    protected int rotation = 0; //0 - right, 1 - down, 2 - left, 3 - up
    protected ImageView imageViewOff;
    protected ImageView imageViewOn;
    protected ImageView imageViewSelected;


    public Gate(){}

    public Gate(double x, double y){
        this.x = x;
        this.y = y;
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

    public void setX(double x){
        this.x = x;
    }

    public double getX(){
        return x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getY(){
        return y;
    }

    public boolean isSelected(){
        return selected;
    }

    public void select(double x, double y) {
        selected = (Math.abs(x - this.x) <= Sizes.baseGateXShift && Math.abs(y - this.y) <= Sizes.baseGateYShift);


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
            graphicsContext.drawImage(imageViewSelected.getImage(), x - Sizes.baseGateXShift, y - Sizes.baseGateYShift);
        }
        else if(output) {
            graphicsContext.drawImage(imageViewOn.getImage(), x - Sizes.baseGateXShift, y - Sizes.baseGateYShift);
        }
        else {
            graphicsContext.drawImage(imageViewOff.getImage(), x - Sizes.baseGateXShift, y - Sizes.baseGateYShift);
        }
    }
}
