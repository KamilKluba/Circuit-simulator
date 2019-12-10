package components.gates.and;

import components.Line;
import components.gates.Gate;
import data.Sizes;
import javafx.scene.chart.XYChart;

public abstract class And extends Gate {
    public And(double x, double y, boolean startLife, XYChart.Series<Integer, String> series) {
        super(x, y, startLife, series);
    }

    public void lifeCycle(){
        while(alive){
            for(int i = 0; i < arrayArrayListLines.length; i++){
                arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
            }
            boolean nextState = true;
            for(boolean b : arraySignalsInputs) {
                if (!b){
                    nextState = false;
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
                boolean nextState2 = true;
                for(boolean b : arraySignalsInputs) {
                    if (!b){
                        nextState2 = false;
                        break;
                    }
                }
                if(nextState == nextState2) {
                    output.set(nextState);
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
