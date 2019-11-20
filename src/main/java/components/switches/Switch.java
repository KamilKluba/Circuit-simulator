package components.switches;

import components.Component;
import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Switch extends Component {
    protected AtomicBoolean state = new AtomicBoolean(false);
    protected ArrayList<Line> arrayListlines = new ArrayList<>();
    protected Point pointLineHook;
    protected ImageView imageViewOn;
    protected ImageView imageViewOff;
    protected ImageView imageViewSelectedOn;
    protected ImageView imageViewSelectedOff;


    public Switch(double x, double y){
        super(x, y);
        this.pointLineHook = new Point("Output", x, y - 35);
    }

    public void sendSignal(){
        for(Line l : arrayListlines){
            l.setState(state.get());
        }
    }

    public void select(double x, double y){
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseSwitchXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseSwitchYShift);
    }

    public boolean checkIfCouldBeSelected(double x, double y){
        return (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseSwitchXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseSwitchYShift);
    }

    public void select(double x1, double y1, double x2, double y2){
        selected = pointCenter.getX() > x1 && pointCenter.getX() < x2 && pointCenter.getY() > y1 && pointCenter.getY() < y2;
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            if(state.get()){
                graphicsContext.drawImage(imageViewSelectedOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            }
            else{
                graphicsContext.drawImage(imageViewSelectedOff.getImage(),pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            }
        }
        else {
            if (state.get()) {
                graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            } else {
                graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            }
        }
    }

    public void selectForDrag(double x, double y){
        selectedForDrag = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseGateXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseGateYShift);
    }

    public boolean inside(double x, double y){
        return Math.abs(x - this.pointCenter.getX()) <= Sizes.baseSwitchXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseSwitchYShift;
    }

    public void rotate(){
        rotation++;
        if(rotation == 4){
            rotation = 0;
        }

        if(rotation == 0){
            pointLineHook.setX(pointCenter.getX());
            pointLineHook.setY(pointCenter.getY() - 35);
        }
        else if(rotation == 1){
            pointLineHook.setX(pointCenter.getX() + 20);
            pointLineHook.setY(pointCenter.getY());
        }
        else if(rotation == 2){
            pointLineHook.setX(pointCenter.getX());
            pointLineHook.setY(pointCenter.getY() + 35);
        }
        else{
            pointLineHook.setX(pointCenter.getX() - 20);
            pointLineHook.setY(pointCenter.getY());
        }

        for(Line l : arrayListlines){
            if(l.getSwitch1() != null && l.getSwitch1().equals(this)){
                l.setX1(pointLineHook.getX());
                l.setY1(pointLineHook.getY());
            }
            else if(l.getSwitch2() != null && l.getSwitch2().equals(this)){
                l.setX2(pointLineHook.getX());
                l.setY2(pointLineHook.getY());
            }
        }
    }

    public void move(double x, double y, double mousePressX, double mousePressY) {
        pointCenter.setX(pointCenter.getX() + x - mousePressX);
        pointCenter.setY(pointCenter.getY() + y - mousePressY);

        for(Line l : arrayListlines){
            if(l.getSwitch1() != null && l.getSwitch1().equals(this)){
                l.setX1(pointLineHook.getX() + x - mousePressX);
                l.setY1(pointLineHook.getY() + y - mousePressY);
            }
            else if(l.getSwitch2() != null && l.getSwitch2().equals(this)){
                l.setX2(pointLineHook.getX() + x - mousePressX);
                l.setY2(pointLineHook.getY() + y - mousePressY);
            }
        }

        pointLineHook.setX(pointLineHook.getX() + x - mousePressX);
        pointLineHook.setY(pointLineHook.getY() + y - mousePressY);
    }

    public void invertState(){
        boolean bufferValue = state.get();
        state.set(!bufferValue);
        for(Line l : arrayListlines){
            l.setState(state.get());
        }
    }

    public String getName() {
        return name;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public Point getPointCenter() {
        return pointCenter;
    }

    public void setPointCenter(Point pointCenter) {
        this.pointCenter = pointCenter;
    }

    public boolean isState() {
        return state.get();
    }

    public void setState(boolean state) {
        this.state.set(state);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectedForDrag() {return selectedForDrag;}

    public void setSelectedForDrag(boolean selectedForDrag) {
        this.selectedForDrag = selectedForDrag;
    }

    public ArrayList<Line> getArrayListlines() {
        return arrayListlines;
    }

    public Point getPointLineHook() {
        return pointLineHook;
    }

    public void setPointLineHook(Point pointLineHook) {
        this.pointLineHook = pointLineHook;
    }
}
