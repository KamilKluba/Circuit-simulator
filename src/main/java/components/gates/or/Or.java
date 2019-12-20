package components.gates.or;

import components.Line;
import components.gates.Gate;
import data.Sizes;
import javafx.scene.chart.XYChart;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Or extends Gate {
    public Or(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
    }

    public void lifeCycle(){
        while(alive){
            for(int i = 0; i < arrayArrayListLines.length; i++){
                arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
            }
            boolean nextState = false;
            for(boolean b : arraySignalsInputs) {
                if (b) {
                    nextState = true;
                    break;
                }
            }
            if(output.get() != nextState) {
                try {
                    Thread.sleep(Sizes.gatePropagationTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < arrayArrayListLines.length; i++){
                    arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
                }
                boolean nextState2 = false;
                for(boolean b : arraySignalsInputs) {
                    if (b) {
                        nextState2 = true;
                        break;
                    }
                }
                if(nextState == nextState2) {
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
}
