package components.gates.xnor;

import com.sun.glass.ui.Size;
import components.Line;
import components.gates.Gate;
import data.Sizes;
import javafx.scene.chart.XYChart;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Xnor extends Gate {
    private static final long serialVersionUID = 10500000000L;

    public Xnor(double x, double y, boolean startLife, XYChart.Series<Long, String> series, Long chartMillisCounter){
        super(x, y, startLife, series, chartMillisCounter);
    }

    public void lifeCycle(){
        while(alive){
            for(int i = 0; i < arrayArrayListLines.length; i++){
                arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
            }
            int numberOfHighSignals = 0;
            for(boolean b : arraySignalsInputs) {
                if (b){
                    numberOfHighSignals++;
                }
            }
            boolean nextState;
            if(numberOfHighSignals % 2 == 1){
                nextState = false;
            }
            else{
                nextState = true;
            }
            if(output.get() != nextState){
                try {
                    Thread.sleep(Sizes.gatePropagationTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i < arrayArrayListLines.length; i++){
                    arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
                }
                int numberOfHighSignals2 = 0;
                for(boolean b : arraySignalsInputs) {
                    if (b){
                        numberOfHighSignals2++;
                    }
                }
                boolean nextState2;
                if(numberOfHighSignals2 % 2 == 1){
                    nextState2 = false;
                }
                else{
                    nextState2 = true;
                }
                if(nextState2 == nextState) {
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
