package components;

import data.Names;
import data.Sizes;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Bulb extends Component implements Serializable {
    private static final long serialVersionUID = 40000000000L;
    private Point pointOutput;
    private Point pointLineHook;
    private ArrayList<Line> arrayListLines = new ArrayList<>();

    private double pointOutputXShift = 0;
    private double pointOutputYShift = 100;

    public Bulb(){
    }

    public Bulb(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
        pointLineHook = new Point(Names.pointInputName, x, y + 100);

        name = Names.bulbName;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/bulb/bulb_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/bulb/bulb_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/bulb/bulb_selected_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelectedOff = new ImageView(new Image(getClass().getResource("/graphics/bulb/bulb_selected_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
    }

    public void lifeCycle(){
        while(alive){
            boolean nextState = arrayListLines.size() > 0 && arrayListLines.get(0).isSignalOutput();

            if(output.get() != nextState){
                try {
                    Thread.sleep(Sizes.lineSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean nextState2 = arrayListLines.size() > 0 && arrayListLines.get(0).isSignalOutput();

                if(nextState == nextState2){
                    output.set(nextState);
                    addDataToSeries();
                    stateChanged.set(true);
                }
            }
            try {
                Thread.sleep(Sizes.gateSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            if(output.get()){
                graphicsContext.drawImage(imageViewSelectedOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
            }
            else{
                graphicsContext.drawImage(imageViewSelectedOff.getImage(),pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
            }
        }
        else {
            if (output.get()) {
                graphicsContext.drawImage(imageViewOn.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
            } else {
                graphicsContext.drawImage(imageViewOff.getImage(), pointCenter.getX() - Sizes.baseGateXShift, pointCenter.getY() - Sizes.baseGateYShift);
            }
        }
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("" + id, pointCenter.getX() - 50, pointCenter.getY() + 50);
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
        imageViewSelectedOff.setRotate(90);
        imageViewSelectedOff.setImage(imageViewSelectedOff.snapshot(snapshotParameters, null));

        rotation++;
        if(rotation == 4){
            rotation = 0;
        }

        movePoints();

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

    public void movePoints(){
        if(rotation == 0){
            pointLineHook.setX(pointCenter.getX());
            pointLineHook.setY(pointCenter.getY() + 100);
            pointOutputXShift = 0;
            pointOutputYShift = + 100;
        }
        else if(rotation == 1){
            pointLineHook.setX(pointCenter.getX() - 100);
            pointLineHook.setY(pointCenter.getY());
            pointOutputXShift = 20;
            pointOutputYShift = 0;
        }
        else if(rotation == 2){
            pointLineHook.setX(pointCenter.getX());
            pointLineHook.setY(pointCenter.getY() - 100);
            pointOutputXShift = 0;
            pointOutputYShift = - 100;
        }
        else{
            pointLineHook.setX(pointCenter.getX() + 100);
            pointLineHook.setY(pointCenter.getY());
            pointOutputXShift = 100;
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
    }

    public ArrayList<Line> getArrayListLines() {
        return arrayListLines;
    }

    public Point getPointLineHook() {
        return pointLineHook;
    }
}
