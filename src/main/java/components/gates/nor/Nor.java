package components.gates.nor;

import components.Line;
import components.gates.Gate;
import data.Sizes;

public abstract class Nor extends Gate {
    public Nor(double x, double y, boolean startLife) {
        super(x, y, startLife);
    }

    public void lifeCycle(){
        while(true){
            for(int i = 0; i < arrayArrayListLines.length; i++){
                arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
            }
            boolean nextState = true;
            for(boolean b : arraySignalsInputs) {
                if (b) {
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
                    if (b) {
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
