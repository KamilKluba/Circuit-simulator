package components.gates.nor;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Nor2 extends Gate {

    public Nor2(double x, double y) {
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

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/nor/nor2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }

    @Override
    public void computeSignal(){
        output.set(false);
        for(boolean b : arraySignalsInputs) {
            if (b){
                output.set(true);
                break;
            }
        }
        boolean bufferValue = output.get();
        output.set(!bufferValue);
        for (Line l : arrayListLinesOutput){
            l.setState(output.get());
        }
    }

    public ArrayList[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }
}
