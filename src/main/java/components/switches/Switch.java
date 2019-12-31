package components.switches;

import components.Component;
import components.Line;
import components.Point;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Switch extends Component implements Serializable {
    private static final long serialVersionUID = 20000000000L;
    protected ArrayList<Line> arrayListLines = new ArrayList<>();
    protected Point pointLineHook;
    private double pointOutputXShift = 0;
    private double pointOutputYShift = -35;

    public Switch(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
        this.pointLineHook = new Point("Output", x, y - 35);
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
            if(output.get()){
                graphicsContext.drawImage(imageViewSelectedOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            }
            else{
                graphicsContext.drawImage(imageViewSelectedOff.getImage(),pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            }
        }
        else {
            if (output.get()) {
                graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            } else {
                graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseSwitchXShift, pointCenter.getY() - Sizes.baseSwitchYShift);
            }
        }
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("" + id, pointCenter.getX() - 50, pointCenter.getY() + 50);
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
            pointOutputXShift = 0;
            pointOutputYShift = -35;
        }
        else if(rotation == 1){
            pointLineHook.setX(pointCenter.getX() + 20);
            pointLineHook.setY(pointCenter.getY());
            pointOutputXShift = 20;
            pointOutputYShift = 0;
        }
        else if(rotation == 2){
            pointLineHook.setX(pointCenter.getX());
            pointLineHook.setY(pointCenter.getY() + 35);
            pointOutputXShift = 0;
            pointOutputYShift = +35;
        }
        else{
            pointLineHook.setX(pointCenter.getX() - 20);
            pointLineHook.setY(pointCenter.getY());
            pointOutputXShift = -20;
            pointOutputYShift = 0;
        }

        for(Line l : arrayListLines){
            if(l.getComponent1() != null && l.getComponent1().equals(this)){
                l.setX1(pointLineHook.getX());
                l.setY1(pointLineHook.getY());
            }
            else if(l.getComponent2() != null && l.getComponent2().equals(this)){
                l.setX2(pointLineHook.getX());
                l.setY2(pointLineHook.getY());
            }
        }
        stateChanged.set(true);
    }

    public void move(double x, double y, double mousePressX, double mousePressY, boolean fitToCheck) {
        if(fitToCheck){
            double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double fitXValue = x - x % Sizes.fitComponentPlace + x1;
            double fitYValue = y - y % Sizes.fitComponentPlace + y1;

            pointCenter.setX(fitXValue);
            pointCenter.setY(fitYValue);

            pointLineHook.setX(fitXValue + pointOutputXShift);
            pointLineHook.setY(fitYValue + pointOutputYShift);

            for (Line l : arrayListLines) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue + pointOutputXShift);
                    l.setY1(fitYValue + pointOutputYShift);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue + pointOutputXShift);
                    l.setY2(fitYValue + pointOutputYShift);
                }
            }
        }
        else {
            pointCenter.setX(pointCenter.getX() + x - mousePressX);
            pointCenter.setY(pointCenter.getY() + y - mousePressY);

            pointLineHook.setX(pointLineHook.getX() + x - mousePressX);
            pointLineHook.setY(pointLineHook.getY() + y - mousePressY);

            for (Line l : arrayListLines) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointLineHook.getX() + x - mousePressX);
                    l.setY1(pointLineHook.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointLineHook.getX() + x - mousePressX);
                    l.setY2(pointLineHook.getY() + y - mousePressY);
                }
            }
        }
    }

    public void invertState(){
        boolean bufferValue = output.get();
        output.set(!bufferValue);
        for(Line l : arrayListLines){
            l.setState(output.get());
        }
        addDataToSeries();
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
        return output.get();
    }

    public void setState(boolean state) {
        this.output.set(state);
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

    public ArrayList<Line> getArrayListLines() {
        return arrayListLines;
    }

    public Point getPointLineHook() {
        return pointLineHook;
    }

    public void setPointLineHook(Point pointLineHook) {
        this.pointLineHook = pointLineHook;
    }
}
