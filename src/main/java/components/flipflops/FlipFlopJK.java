package components.flipflops;

import components.Line;
import data.Point;
import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class FlipFlopJK extends FlipFlop implements Serializable {
    private static final long serialVersionUID = 30200000000L;
    private boolean signalInputK = false;
    private ArrayList<Line> arrayListLinesInputK = new ArrayList<>();
    private Point pointInputK;

    public FlipFlopJK(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);

        name = Names.flipFlopJK;

        pointInput.setName("Input J");
        pointInputK = new Point("Input K", x - 145, y + 75);

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
    }

    @Override
    public void move(double x, double y, double mousePressX, double mousePressY){
        super.move(x, y, mousePressX, mousePressY);

        pointInputK.setX(pointInputK.getX() + x - mousePressX);
        pointInputK.setY(pointInputK.getY() + y - mousePressY);

        for (Line l : arrayListLinesInputK) {
            if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                l.setX1(pointInputK.getX() + x - mousePressX);
                l.setY1(pointInputK.getY() + y - mousePressY);
            } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                l.setX2(pointInputK.getX() + x - mousePressX);
                l.setY2(pointInputK.getY() + y - mousePressY);
            }
        }
    }

    @Override
    public void movePoints(){
        super.movePoints();
        pointInputK.setX(pointCenter.getX() - 145);
        pointInputK.setY(pointCenter.getY() + 75);

        for (Line l : arrayListLinesInputK) {
            if (l.getComponent1() != null && l.getComponent1().equals(this)) {
                l.setX1(pointCenter.getX() - 145);
                l.setY1(pointCenter.getY() + 75);
            } else if (l.getComponent2() != null && l.getComponent2().equals(this)) {
                l.setX2(pointCenter.getX() - 145);
                l.setY2(pointCenter.getY() + 75);
            }
        }
    }

    public void lifeCycle(){
        while(alive) {
            try{
                signalReset = !(arrayListLinesReset.size() > 0 && arrayListLinesReset.get(0).isSignalOutput());
                if (signalReset) {
                    output.set(false);
                    signalReversedOutput.set(true);
                }
                else {
                    signalAsynchronousInput = arrayListLinesAsynchronousInput.size() > 0 && arrayListLinesAsynchronousInput.get(0).isSignalOutput();
                    if(signalAsynchronousInput){
                        output.set(true);
                        signalReversedOutput.set(false);
                    }
                    else {
                        signalClock = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                        if (signalClock && risingEdge) {
                            boolean nextStateJ = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();
                            boolean nextStateK = arrayListLinesInputK.size() > 0 && arrayListLinesInputK.get(0).isSignalOutput();
                            risingEdge = false;
                            if(!nextStateJ && !nextStateK){
                                try {
                                    Thread.sleep(Sizes.flipFlopPropagationTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                boolean signalClock2 = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                                boolean nextStateJ2 = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();
                                boolean nextStateK2 = arrayListLinesInputK.size() > 0 && arrayListLinesInputK.get(0).isSignalOutput();

                                if (nextStateJ == nextStateJ2 && nextStateK == nextStateK2 && signalClock == signalClock2) {
                                    boolean signalBuffer = output.get();
                                    output.set(signalBuffer);
                                    signalReversedOutput.set(!signalBuffer);
                                    addDataToSeries();
                                    stateChanged.set(true);
                                }
                            }
                            else if(!nextStateJ && nextStateK){
                                try {
                                    Thread.sleep(Sizes.flipFlopPropagationTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                boolean signalClock2 = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                                boolean nextStateJ2 = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();
                                boolean nextStateK2 = arrayListLinesInputK.size() > 0 && arrayListLinesInputK.get(0).isSignalOutput();

                                if (nextStateJ == nextStateJ2 && nextStateK == nextStateK2 && signalClock == signalClock2) {
                                    output.set(false);
                                    signalReversedOutput.set(true);
                                    addDataToSeries();
                                    stateChanged.set(true);
                                }
                            }
                            else if(nextStateJ && !nextStateK){
                                try {
                                    Thread.sleep(Sizes.flipFlopPropagationTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                boolean signalClock2 = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                                boolean nextStateJ2 = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();
                                boolean nextStateK2 = arrayListLinesInputK.size() > 0 && arrayListLinesInputK.get(0).isSignalOutput();

                                if (nextStateJ == nextStateJ2 && nextStateK == nextStateK2 && signalClock == signalClock2) {
                                    output.set(true);
                                    signalReversedOutput.set(false);
                                    addDataToSeries();
                                    stateChanged.set(true);
                                }
                            }
                            else {
                                try {
                                    Thread.sleep(Sizes.flipFlopPropagationTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                boolean signalClock2 = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                                boolean nextStateJ2 = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();
                                boolean nextStateK2 = arrayListLinesInputK.size() > 0 && arrayListLinesInputK.get(0).isSignalOutput();

                                if (nextStateJ == nextStateJ2 && nextStateK == nextStateK2 && signalClock == signalClock2) {
                                    boolean signalBuffer = output.get();
                                    output.set(!signalBuffer);
                                    signalReversedOutput.set(signalBuffer);
                                    addDataToSeries();
                                    stateChanged.set(true);
                                }
                            }
                        }
                        else if(!signalClock){
                            risingEdge = true;
                        }
                    }
                }
                Thread.sleep(Sizes.flipFlopSleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSignalInputK() {
        return signalInputK;
    }

    public void setSignalInputK(boolean signalInputK) {
        this.signalInputK = signalInputK;
    }

    public ArrayList<Line> getArrayListLinesInputK() {
        return arrayListLinesInputK;
    }

    public Point getPointInputK() {
        return pointInputK;
    }
}
