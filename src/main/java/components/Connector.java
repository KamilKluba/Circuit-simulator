package components;

import data.Names;
import data.SerializableColor;
import data.Sizes;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Connector extends Component implements Serializable {
    private static final long serialVersionUID = 50000000000L;
    private ArrayList<Line> arrayListLines = new ArrayList<>();
    private SerializableColor selectionColor = new SerializableColor(0.459, 0, 0, 1);
    private SerializableColor onColor = new SerializableColor(0, 0.8, 0.8, 1);

    public Connector(){}

    public Connector(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, null, null);

        name = Names.connectorName;
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
        selected = (Math.abs(x - this.pointCenter.getX()) <= 3 * Sizes.lineSelectDistance && Math.abs(y - pointCenter.getY()) <= 3 * Sizes.lineSelectDistance);
    }

    public void select(double x1, double y1, double x2, double y2){
        selected = pointCenter.getX() > x1 && pointCenter.getX() < x2 && pointCenter.getY() > y1 && pointCenter.getY() < y2;
    }

    public boolean checkIfCouldBeSelected(double x, double y){
        return (Math.abs(x - this.pointCenter.getX()) <= 3 * Sizes.lineSelectDistance && Math.abs(y - pointCenter.getY()) <= 3 * Sizes.lineSelectDistance);
    }

    public void selectForDrag(double x, double y){
        selectedForDrag = (Math.abs(x - this.pointCenter.getX()) <= 3 * Sizes.lineSelectDistance && Math.abs(y - pointCenter.getY()) <= 3 * Sizes.lineSelectDistance);
    }

    public void draw(GraphicsContext graphicsContext){
        if(selected){
            graphicsContext.setFill(selectionColor.getFXColor());
        }
        else if (output.get()){
            graphicsContext.setFill(onColor.getFXColor());
        }
        else{
            graphicsContext.setFill(Color.BLACK);
        }
        graphicsContext.fillOval(pointCenter.getX() - Sizes.lineSelectDistance, pointCenter.getY() - Sizes.lineSelectDistance
                , 2 * Sizes.lineSelectDistance, 2 * Sizes.lineSelectDistance);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("" + id, pointCenter.getX() - 35, pointCenter.getY() + 35);
    }

    public ArrayList<Line> getArrayListLines() {
        return arrayListLines;
    }

    public void move(double x, double y, double mousePressX, double mousePressY, boolean fitToCheck){
        if(fitToCheck){
            double x1 = x % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double y1 = y % Sizes.fitComponentPlace > Sizes.fitComponentPlace / 2 ? Sizes.fitComponentPlace : 0;
            double fitXValue = x - x % Sizes.fitComponentPlace + x1;
            double fitYValue = y - y % Sizes.fitComponentPlace + y1;

            pointCenter.setX(fitXValue);
            pointCenter.setY(fitYValue);

            for (Line l : arrayListLines) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(fitXValue);
                    l.setY1(fitYValue);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(fitXValue);
                    l.setY2(fitYValue);
                }
            }
        }
        else {
            pointCenter.setX(pointCenter.getX() + x - mousePressX);
            pointCenter.setY(pointCenter.getY() + y - mousePressY);

            for (Line l : arrayListLines) {
                if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                    l.setX1(pointCenter.getX() + x - mousePressX);
                    l.setY1(pointCenter.getY() + y - mousePressY);
                } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                    l.setX2(pointCenter.getX() + x - mousePressX);
                    l.setY2(pointCenter.getY() + y - mousePressY);
                }
            }
        }
    }

    public void movePoints(){
        for (Line l : arrayListLines) {
            if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                l.setX1(pointCenter.getX());
                l.setY1(pointCenter.getY());
            } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                l.setX2(pointCenter.getX());
                l.setY2(pointCenter.getY());
            }
        }
    }
}
