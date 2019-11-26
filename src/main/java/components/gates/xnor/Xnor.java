package components.gates.xnor;

import com.sun.glass.ui.Size;
import components.Line;
import components.gates.Gate;
import data.Sizes;

public abstract class Xnor extends Gate {
    public Xnor(double x, double y){
        super(x, y);
    }

    public void lifeCycle(){
        while(true){
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
                output.set(nextState);
                for (Line l : arrayListLinesOutput){
                    l.setState(output.get());
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
