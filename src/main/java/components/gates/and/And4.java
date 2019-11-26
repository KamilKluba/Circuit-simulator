package components.gates.and;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Names;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class And4 extends And {
    public And4(double x, double y) {
        super(x, y);

        inputsNumber = 4;
        arrayArrayListLines = new ArrayList[4];
        arrayArrayListLines[0] = new ArrayList<>();
        arrayArrayListLines[1] = new ArrayList<>();
        arrayArrayListLines[2] = new ArrayList<>();
        arrayArrayListLines[3] = new ArrayList<>();
        arrayPointsInputs = new Point[4];
        arrayPointsInputs[0] = new Point(Names.pointInputName + "1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point(Names.pointInputName + "2", x - 93, y - 10);
        arrayPointsInputs[2] = new Point(Names.pointInputName + "3", x - 93, y + 10);
        arrayPointsInputs[3] = new Point(Names.pointInputName + "4", x - 93, y + 30);
        arraySignalsInputs = new boolean[4];
        name = Names.gateAnd4Name;

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_off.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_on.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/and/and4_gate_selected.png").toExternalForm(), Sizes.baseGateXSize, Sizes.baseGateYSize, false, false));

        executorService.execute(() -> lifeCycle());
    }

    public ArrayList[] getArrayArrayListLines() {
        return arrayArrayListLines;
    }
}
