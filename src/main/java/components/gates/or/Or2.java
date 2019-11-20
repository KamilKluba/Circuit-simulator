package components.gates.or;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Or2 extends Gate {

    public Or2(double x, double y) {
        super(x, y);

        inputsNumber = 2;
        arrayArrayListLines = new ArrayList[2];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayArrayListLines[1] = new ArrayList<>();
        arrayPointsInputs = new Point[2];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y + 30);
        arraySignalsInputs = new boolean[2];
        name = Names.gateOr2Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/or/or2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));

        executorService.execute(() -> lifeCycle());
    }

    public void lifeCycle(){
        while(true){
            System.out.println("haluwa " + output.get());
            output.set(false);

            for(int i = 0; i < arrayArrayListLines.length; i++){
                boolean atLeastOneHigh = false;
                for(Line l : arrayArrayListLines[i]){
                    if (l.isState()){
                        atLeastOneHigh = true;
                    }
                }
                for(Line l : arrayArrayListLines[i]){
                    l.setState(atLeastOneHigh);
                }
                arraySignalsInputs[i] = atLeastOneHigh;
            }

            for(boolean b : arraySignalsInputs) {
                if (b) {
                    output.set(true);
                    break;
                }
            }
            for (Line l : arrayListLinesOutput){
                l.setState(output.get());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void computeSignal(){
//        output.set(false);
//        for(boolean b : arraySignalsInputs) {
//            if (b){
//                output.set(true);
//                break;
//            }
//        }
//        for (Line l : arrayListLinesOutput){
//            l.setState(output.get());
//        }
    }

    public ArrayList[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }
}
