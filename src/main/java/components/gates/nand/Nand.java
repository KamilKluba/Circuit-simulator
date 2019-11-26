package components.gates.nand;

import components.Line;
import components.gates.Gate;
import data.Sizes;

public abstract class Nand extends Gate {
    public Nand(double x, double y){
        super(x, y);
    }

    public void lifeCycle(){
        while(true){
            for(int i = 0; i < arrayArrayListLines.length; i++){
                arraySignalsInputs[i] = arrayArrayListLines[i].size() > 0 && arrayArrayListLines[i].get(0).isState();
            }
            boolean nextState = false;
            for(boolean b : arraySignalsInputs) {
                if (!b){
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
                output.set(nextState);
                for (Line l : arrayListLinesOutput) {
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