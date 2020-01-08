package components.flipflops;

import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class FlipFlopT extends FlipFlop implements Serializable {
    private static final long serialVersionUID = 30300000000L;

    public FlipFlopT(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);

        name = Names.flipFlopT;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewSelectedOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/t_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
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
                        boolean nextState = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();
                        risingEdge = false;
                        if (output.get() != nextState) {
                            try {
                                Thread.sleep(Sizes.flipFlopPropagationTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            boolean signalClock2 = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                            boolean nextState2 = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();

                            if (nextState == nextState2 && signalClock == signalClock2) {
                                output.set(true);
                                signalReversedOutput.set(false);
                                addDataToSeries();
                                stateChanged.set(true);
                            }
                        }
                        else{
                            try {
                                Thread.sleep(Sizes.flipFlopPropagationTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            boolean signalClock2 = arrayListLinesClock.size() > 0 && arrayListLinesClock.get(0).isSignalOutput();
                            boolean nextState2 = arrayListLinesInput.size() > 0 && arrayListLinesInput.get(0).isSignalOutput();

                            if (nextState == nextState2 && signalClock == signalClock2) {
                                output.set(false);
                                signalReversedOutput.set(true);
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
            try {
                Thread.sleep(Sizes.flipFlopSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
