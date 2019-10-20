package components.gates.or;

import components.Line;
import components.Point;
import components.gates.Gate;
import data.Sizes;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Or2 extends Gate {

    public Or2(){
        name = "Or 2";
        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateImageInTableXSize, Sizes.baseGateImageInTableYSize, false, false));
    }

    public Or2(double x, double y) {
        super(x, y);

        inputsNumber = 2;
        arrayLines = new Line[2];
        arrayPointsInputs = new Point[2];
        arrayPointsInputs[0] = new Point("Input1", x - 93, y - 30);
        arrayPointsInputs[1] = new Point("Input2", x - 93, y + 30);
        name = "Or 2";

        imageViewOff = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_off.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewOn = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_on.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
        imageViewSelected = new ImageView(new Image(getClass().getResource("/graphics/or2_gate_selected.png").toExternalForm(), Sizes.baseGateXSize , Sizes.baseGateYSize, false, false));
    }
}