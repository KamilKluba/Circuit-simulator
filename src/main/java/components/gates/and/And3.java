package components.gates.and;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class And3 extends Gate {
    public And3(double x, double y) {
        super(x, y);

        inputsNumber = 3;
        arrayArrayListLines = new ArrayList[3];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayArrayListLines[1] = new ArrayList<>();
        arrayArrayListLines[2] = new ArrayList<>();
        arrayPointsInputs = new Point[3];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y);
        arrayPointsInputs[2] = new Point(Names.pointInputName + "3", x - 93, y + 30);
        arraySignalsInputs = new boolean[3];
        name = Names.gateAnd3Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/and/and3_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
    }

    @Override
    public void computeSignal(){
        output.set(true);
        for(boolean b : arraySignalsInputs) {
            if (!b){
                output.set(false);
                break;
            }
        }
        for (Line l : arrayListLinesOutput){
            l.setState(output.get());
        }
    }

    public ArrayList[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }
}
