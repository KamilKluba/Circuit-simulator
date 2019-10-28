package components.gates.or;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Or3 extends Gate {

    public Or3(double x, double y) {
        super(x, y);

        inputsNumber = 3;
        arrayLines = new Line[3];
        arrayPointsInputs = new Point[3];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y);
        arrayPointsInputs[2] = new Point(Names.pointInputName + "3", x - 93, y + 30);
        arraySignalsInputs = new boolean[3];
        name = Names.gateOr3Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/or/or3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }

    @Override
    public void computeSignal(){
        output = false;
        for(boolean b : arraySignalsInputs) {
            if (b){
                output = true;
                break;
            }
        }
        if(lineOutput != null){
            lineOutput.setState(output);
        }
    }

    public Line[] getArrayLines() {
        return arrayLines;
    }
}
