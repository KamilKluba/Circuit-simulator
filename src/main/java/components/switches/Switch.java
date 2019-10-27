package components.switches;

import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public abstract class Switch {
    private int rotation = 0;
    private Point pointCenter;
    private boolean state = false;
    private boolean selected = false;
    private boolean selectedForDrag = false;
    private Line line;
    private Point pointLineHook;
    protected ImageView imageViewOn;
    protected ImageView imageViewOff;


    public Switch(double x, double y){
        this.pointCenter = new Point("Center", x, y);
        this.pointLineHook = new Point("Output", x, y - 35);
    }

    public void sendSignal(){
        if(line != null){
            line.setState(state);
        }
    }

    public void select(double x, double y){
        selected = (Math.abs(x - this.pointCenter.getX()) <= Sizes.baseSwitchXShift && Math.abs(y - pointCenter.getY()) <= Sizes.baseSwitchYShift);
        if(selected){
            state = !state;
        }
        sendSignal();
    }

    public void draw(GraphicsContext graphicsContext){
        if(state){
            graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
        }
        else{
            graphicsContext.drawImage(imageViewOff.getImage(),pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
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

        if(line != null){
            if(line.getSwitch1() != null && line.getSwitch1().equals(this)){
                line.setX1(pointLineHook.getX());
                line.setY1(pointLineHook.getY());
            }
            else if(line.getSwitch2() != null && line.getSwitch2().equals(this)){
                line.setX2(pointLineHook.getX());
                line.setY2(pointLineHook.getY());
            }
        }
    }

    public void move(double x, double y, double mousePressX, double mousePressY) {
        pointCenter.setX(pointCenter.getX() + x - mousePressX);
        pointCenter.setY(pointCenter.getY() + y - mousePressY);

        if(line != null){
            if(line.getSwitch1() != null && line.getSwitch1().equals(this)){
                line.setX1(pointLineHook.getX() + x - mousePressX);
                line.setY1(pointLineHook.getY() + y - mousePressY);
            }
            else if(line.getSwitch2() != null && line.getSwitch2().equals(this)){
                line.setX2(pointLineHook.getX() + x - mousePressX);
                line.setY2(pointLineHook.getY() + y - mousePressY);
            }
        }

        pointLineHook.setX(pointLineHook.getX() + x - mousePressX);
        pointLineHook.setY(pointLineHook.getY() + y - mousePressY);
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
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
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

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Point getPointLineHook() {
        return pointLineHook;
    }

    public void setPointLineHook(Point pointLineHook) {
        this.pointLineHook = pointLineHook;
    }
}
