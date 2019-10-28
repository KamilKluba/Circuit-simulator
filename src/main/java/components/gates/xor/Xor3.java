package components.gates.xor;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Xor3 extends Gate {

    public Xor3(double x, double y) {
        super(x, y);

        inputsNumber = 3;
        arrayLines = new Line[3];
        arrayPointsInputs = new Point[3];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y);
        arrayPointsInputs[2] = new Point(Names.pointInputName + "3", x - 93, y + 30);
        arraySignalsInputs = new boolean[3];
        name = Names.gateXor3Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/xor/xor3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }

    @Override
    public void computeSignal(){
        int numberOfHighSignals = 0;
        for(boolean b : arraySignalsInputs) {
            if (b){
                numberOfHighSignals++;
            }
        }
        if(numberOfHighSignals % 2 == 1){
            output = true;
        }
        else{
            output = false;
        }

        if(lineOutput != null){
            lineOutput.setState(output);
        }
    }

    public Line[] getArrayLines() {
        return arrayLines;
    }
}
