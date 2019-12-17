package components.flipflops;

import data.Names;
import data.Sizes;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.atomic.AtomicInteger;


public class FlipFlopD extends FlipFlop{
    public FlipFlopD(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);

        name = Names.flipFlopD;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_off.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_on.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/flipflops/d_selected.png").toExternalForm(), Sizes.baseFlipFlopXSize, Sizes.baseFlipFlopYSize, false, false));
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
                                output.set(nextState);
                                signalReversedOutput.set(!nextState);
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
}
