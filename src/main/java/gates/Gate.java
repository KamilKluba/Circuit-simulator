package gates;

import data.Line;
import data.Sizes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Gate {
    protected String name;
    protected ArrayList<Boolean> arrayListInputs = new ArrayList<>();
    protected ArrayList<Line> arrayListLines = new ArrayList();
    protected Boolean output = false;
    protected Color color = Color.BLACK;
    protected double x;
    protected double y;
    protected int direction;
    protected ImageView image;


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

    public ImageView getImage(){
        return image;
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

    public boolean isSelected(double x, double y){
        return (Math.abs(x - this.x) <= Sizes.baseGateXShift &&
                Math.abs(y - this.y) <= Sizes.baseGateYShift);
    }


}
