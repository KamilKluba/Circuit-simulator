package components.flipflops;

import components.Line;
import components.Point;
import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FlipFlopJK extends FlipFlop{
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
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/flipflops/jk_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
    }

    @Override
    public void move(double x, double y, double mousePressX, double mousePressY){
        super.move(x, y, mousePressX, mousePressY);

        pointInputK.setX(pointInputK.getX() + x - mousePressX);
        pointInputK.setY(pointInputK.getY() + y - mousePressY);

        for(Line l : arrayListLinesInputK){
            if(l.getComponent1() != null && l.getComponent1().equals(this)){
                l.setX1(pointInputK.getX() + x - mousePressX);
                l.setY1(pointInputK.getY() + y - mousePressY);
            }
            else if(l.getComponent2() != null && l.getComponent2().equals(this)){
                l.setX2(pointInputK.getX() + x - mousePressX);
                l.setY2(pointInputK.getY() + y - mousePressY);
            }
        }
    }

    public void lifeCycle(){
        while(alive) {
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
                            }
                        }
                    }
                    else if(!signalClock){
                        risingEdge = true;
                    }
                }
            }
            try {
                Thread.sleep(Sizes.flipFlopSleepTime);
            } catch (InterruptedException e) {
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
